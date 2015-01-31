package jp.baroqueworksdev.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

public class SampleService extends Service {
    /**
     * Observer List
     */
    private final RemoteCallbackList<ISampleServiceObserver> mObservers
            = new RemoteCallbackList<ISampleServiceObserver>();

    public SampleService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mSampleServiceImpl;
    }

    /**
     * implement ISampleService
     */
    private ISampleService.Stub mSampleServiceImpl = new ISampleService.Stub() {
        @Override
        public void registerObserver(ISampleServiceObserver observer) throws RemoteException {
            mObservers.register(observer);

        }

        @Override
        public void unregisterObserver(ISampleServiceObserver observer) throws RemoteException {
            mObservers.unregister(observer);

        }

        @Override
        public void performRequest(int type) throws RemoteException {
            // performRequest
            //TODO: example
            notifyToObserver();
        }
    };

    // TODO: example
    private void notifyToObserver() {
        // Observerの処理を開始
        int n = mObservers.beginBroadcast();
        for (int i = 0; i < n; i++) {
            try {
                mObservers.getBroadcastItem(i).onResponse(9999);
            } catch (RemoteException e) {
                //
            }
        }
        mObservers.finishBroadcast();
    }

}
