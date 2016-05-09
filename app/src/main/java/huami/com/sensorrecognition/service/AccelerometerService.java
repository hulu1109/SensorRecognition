package huami.com.sensorrecognition.service;

import android.content.Intent;
import android.hardware.Sensor;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by test on 2016/5/5.
 */
public class AccelerometerService extends BaseSensorService{
    public AccelerometerService(){
        mName = "ACC";
        mColumnNum = 3;
        mColumnHeader = "AccX\tAccY\tAccZ";
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }
}
