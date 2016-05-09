package huami.com.sensorrecognition.service;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by test on 2016/5/5.
 */
public class BaseSensorService extends BaseSampleService{

    protected SensorManager mSensorManager;
    protected Sensor mSensor;
    protected int mColumnNum;
    protected int mAccuracy;


    public BaseSensorService() { mAccuracy = SensorManager.SENSOR_STATUS_UNRELIABLE; }

    @Override
    public void onCreate() {
        super.onCreate();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mIsSensorReady = mSensorManager.registerListener(
            sensorEventListener, mSensor, SensorManager.SENSOR_DELAY_FASTEST
        );
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if(mIsSensorReady) {
            mSensorManager.unregisterListener(sensorEventListener);
            mIsSensorReady = false;
        }
        super.onDestroy();
    }

    private final SensorEventListener sensorEventListener;
    {
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                ++mSampleCount;

                float[] fixedValues = new float[mColumnNum];

                for(int i = 0; i <mColumnNum; i++){
                    if(i == 0)
                        fixedValues[i] = event.values[i];
                    else
                        fixedValues[i] = mAccuracy;
                }

                String line = Float.toString(fixedValues[0]);
                for(int i = 1; i <mColumnNum; ++i)
                    line += "\t" + fixedValues[i];
                printLine(line + "\n");
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                mAccuracy = accuracy;
            }
        };
    }
}
