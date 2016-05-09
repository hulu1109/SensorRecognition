package huami.com.sensorrecognition.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import huami.com.sensorrecognition.MainActivity;

/**
 * Created by test on 2016/5/5.
 */
public class BaseSampleService extends Service {
    public final static String ACTION_HEADER = "com.huami.sensorrecognition.service.";
    private final static String TAG = "SensorACC";

    protected boolean mIsSensorReady = false;
    protected int mSampleCount = 0;
    protected File mTargetFile;

    protected String mName;
    protected String mColumnHeader;

    private FileOutputStream mWriter;
    private boolean mPrintLock = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startRecord();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        stopRecord();
        super.onDestroy();
    }

    public void startRecord(){
        mTargetFile = new File(MainActivity.sSensorCurFolder.getPath(), mName + ".txt");

        try {
            mWriter = new FileOutputStream(mTargetFile);
            mWriter.write(("Sensor Type:" + mName + "\n").getBytes());
            mWriter.write((MainActivity.DATE_FORMAT.format(MainActivity.sStartDate) + "\n").getBytes());
            mWriter.write(("Timestamp\t" + mColumnHeader + "\n").getBytes());

            if (!mIsSensorReady)
                mWriter.write(("Sensor initialization failed\n").getBytes());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void stopRecord(){
        try{
            mWriter.close();
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
        }
    }

    protected void printLine(String line){
        try{
            while(mPrintLock)
                continue;
            mPrintLock = true;
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mWriter.write((df.format(System.currentTimeMillis()) + "\t" + line).getBytes());
            mPrintLock = false;
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
        }
    }
}
