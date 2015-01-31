// ISampleServiceInterface.aidl
package jp.baroqueworksdev.service;
import jp.baroqueworksdev.service.ISampleServiceObserver;
// Declare any non-default types here with import statements

interface ISampleService {
    void registerObserver(ISampleServiceObserver observer);

    void unregisterObserver(ISampleServiceObserver observer);

    void performRequest(int type);

}
