package com.example.otto.internalsensor;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;
import android.os.Bundle;
import android.widget.TextView;



class TriggerListener extends TriggerEventListener {

    private Context mContext;
    private TextView mTextView;

    TriggerListener(Context context, TextView textView) {
        mContext = context;
        mTextView = textView;
    }

    public void onTrigger(TriggerEvent event){
        if (event.values[0] == 1) {
            mTextView.append(mContext.getString(R.string.sig_motion) + "\n");
            mTextView.append(mContext.getString(R.string.sig_motion_auto_disabled) + "\n");
        }
    }
}

public class MainActivity extends Activity {

    private SensorManager mSensorManager;
    private Sensor mSigMotion;
    private TriggerListener mListener;
    private TextView mTextView;


    protected void onResume() {
        super.onResume();
        if (mSigMotion != null && mSensorManager.requestTriggerSensor(mListener, mSigMotion))
            mTextView.append(getString(R.string.sig_motion_enabled) + "\n");

    }
    protected void onPause() {
        super.onPause();
        if (mSigMotion != null) mSensorManager.cancelTriggerSensor(mListener, mSigMotion);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSigMotion = mSensorManager.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION);
        mTextView = (TextView)findViewById(R.id.textView);
        mListener = new TriggerListener(this, mTextView);

        if (mSigMotion == null) {
            mTextView.append(getString(R.string.no_sig_motion) + "\n");
        }

    }
    protected void onStop() {
        if (mSigMotion != null) mSensorManager.cancelTriggerSensor(mListener, mSigMotion);
        super.onStop();
    }



}


