package jp.baroqueworksdev.service.fragment;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.app.Fragment;

import java.util.List;

import jp.baroqueworksdev.service.ISampleService;
import jp.baroqueworksdev.service.ISampleServiceObserver;
import jp.baroqueworksdev.service.SampleService;


public class ServiceControllerFragment extends Fragment {
    public static final String TAG = "ControllerFragment";
    private static final int POST_DELAY_TIME = 100;

    private ISampleService mISampleService;

    public ServiceControllerFragment() {
        // Required empty public constructor
    }

    public ServiceControllerFragment newInstance() {
        ServiceControllerFragment fragment = new ServiceControllerFragment();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Intent intent = new Intent(getActivity().getApplication(), SampleService.class);
        activity.bindService(intent, mSampleServiceConnection, Activity.BIND_AUTO_CREATE);
    }

    @Override
    public void onDetach() {
        try {
            mISampleService.unregisterObserver(mObserver);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        getActivity().unbindService(mSampleServiceConnection);
        super.onDetach();
    }

    /**
     * connect to service
     */
    private ServiceConnection mSampleServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mISampleService = ISampleService.Stub.asInterface(service);

            try {
                mISampleService.registerObserver(mObserver);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mISampleService = null;
        }
    };

    /**
     * observer which notify from Service
     */
    private ISampleServiceObserver mObserver = new ISampleServiceObserver.Stub() {
        @Override
        public void onResponse(int requestType) throws RemoteException {
            List<Fragment> fragmentList = getFragmentManager().getFragments();
            if (fragmentList != null) {
                for (Fragment fragment : fragmentList) {
                    if (fragment instanceof ServiceControllerInterface) {
                        ((ServiceControllerInterface) fragment).onResponse(requestType);
                    }
                }
            }
        }
    };

    public void performRequest(int type) {
        mHandler.sendMessage(mHandler.obtainMessage(type));
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (mISampleService == null) {
                Message newMsg = obtainMessage();
                newMsg.copyFrom(msg);
                sendMessageDelayed(newMsg, POST_DELAY_TIME);
            } else {
                try {
                    mISampleService.performRequest(msg.what);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    };

}
