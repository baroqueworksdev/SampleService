package jp.baroqueworksdev.service.fragment;


import android.os.RemoteException;
import android.support.v4.app.Fragment;

import java.util.List;

public class UsingServiceFragment extends Fragment implements ServiceControllerInterface {


    public UsingServiceFragment() {
        // Required empty public constructor
    }

    /**
     * Request to Service
     *
     * @param type
     * @throws RemoteException
     */
    public void performRequest(int type) throws RemoteException {
        getController().performRequest(type);
    }

    /**
     * get ControllerFragment
     */
    private ServiceControllerFragment getController() {
        ServiceControllerFragment fragment =
                (ServiceControllerFragment) getActivity().getSupportFragmentManager().findFragmentByTag(ServiceControllerFragment.TAG);
        return fragment;
    }

    @Override
    public void onResponse(int type) {
        List<Fragment> fragmentList = getChildFragmentManager().getFragments();
        if (fragmentList != null) {
            for (Fragment fragment : fragmentList) {
                if (fragment instanceof ServiceControllerInterface) {
                    ((ServiceControllerInterface) fragment).onResponse(type);
                }
            }
        }
    }
}
