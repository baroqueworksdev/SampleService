package jp.baroqueworksdev.service;

import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.test.ServiceTestCase;

import junit.framework.TestCase;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SampleServiceTest extends ServiceTestCase<SampleService> {

    private class TestISampleServiceImpl extends ISampleServiceObserver.Stub {
        public CountDownLatch countDownLatch;

        public boolean isResponse;
        public int type;

        public TestISampleServiceImpl() {
            countDownLatch = new CountDownLatch(1);
        }

        @Override
        public void onResponse(int requestType) throws RemoteException {
            isResponse = true;
            type = requestType;

            countDownLatch.countDown();
        }
    }


    /**
     * Constructor
     *
     */
    public SampleServiceTest() {
        super(SampleService.class);
    }

    public void testCreate() throws RemoteException, InterruptedException {

        Intent intent = new Intent();
        intent.setClass(getContext(), SampleService.class);
        startService(intent);

        Intent bindTesIntent = new Intent();
        intent.setClass(getContext(), SampleService.class);
        IBinder binder = bindService(bindTesIntent);
        ISampleService iSampleService = ISampleService.Stub.asInterface(binder);
        {

            TestISampleServiceImpl observer = new TestISampleServiceImpl();

            iSampleService.registerObserver(observer);

            iSampleService.performRequest(100);
            observer.countDownLatch.await(30, TimeUnit.SECONDS);
            iSampleService.unregisterObserver(observer);
        }

        shutdownService();

    }
}