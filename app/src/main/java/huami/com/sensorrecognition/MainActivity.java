package huami.com.sensorrecognition;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import huami.com.sensorrecognition.service.BaseSampleService;
import huami.com.sensorrecognition.utils.SensorBuffer;
import huami.com.sensorrecognition.utils.SensorModel;

public class MainActivity extends Activity {
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
    public static final String SENSOR_TYPE = "ACC";
    public static long sStartTimestamp = 0;

    public static File sSensorCurFolder = null;
    public static File sSensorBaseFolder = null;
    public static Date sStartDate = null;

    SensorBuffer sensorBuffer;
    boolean mIsRecording = false;

    Button mStartBtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStartBtn = (Button)findViewById(R.id.btn_start);

//        sensorBuffer = new SensorBuffer();
//        for(int i = 0; i < 2000; i++){
//            SensorModel model = new SensorModel(i, i, i);
//            sensorBuffer.add(model);
//        }

        //boolean hasTargetFolder = false;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            sSensorBaseFolder = new File(Environment.getExternalStorageDirectory(), "Sensor");
            sSensorBaseFolder.mkdir();
        }

    }

    private void startRecord() {
        sStartDate = new Date();
        sStartTimestamp = sStartDate.getTime();
        sSensorCurFolder = new File(sSensorBaseFolder.getPath(), DATE_FORMAT.format(sStartDate));
        if (!sSensorBaseFolder.exists())
            sSensorBaseFolder.mkdir();
        if (!sSensorCurFolder.exists())
            sSensorCurFolder.mkdir();

        mStartBtn.setText("Stop");
        mIsRecording = true;
        startSensorService(SENSOR_TYPE);
    }

    private void stopRecord(){
        if(mIsRecording){
            mStartBtn.setText("Start");
            mIsRecording = false;
            stopSensorService(SENSOR_TYPE);
        }
    }

    private void startSensorService(String name){
        Intent mIntent = new Intent();
        mIntent.setAction(BaseSampleService.ACTION_HEADER + name);
        mIntent.setPackage(getPackageName());
        startService(mIntent);
    }

    private void stopSensorService(String name){
        Intent mIntent = new Intent();
        mIntent.setAction(BaseSampleService.ACTION_HEADER + name);
        mIntent.setPackage(getPackageName());
        stopService(mIntent);
    }

    @Override
    protected void onDestroy() {
        stopRecord();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onStartBtnClick(View source) throws IOException{
        if(!mIsRecording)
            startRecord();
        else
            stopRecord();
    }
}
