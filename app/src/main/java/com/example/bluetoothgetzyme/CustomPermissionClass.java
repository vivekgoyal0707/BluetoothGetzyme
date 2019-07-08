package com.example.bluetoothgetzyme;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class CustomPermissionClass {

    public static final int BLUETOOTH = 1;
    public static final int BLUETOOTH_ADMIN = 2;
    public static final int COARSE_LOCATION = 3;
    Activity activity;

    public CustomPermissionClass(Activity activity) {
        this.activity = activity;
    }


    public boolean checkPermissionForBluetooth(){
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermissionForBluetooth(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.BLUETOOTH)){
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.BLUETOOTH},BLUETOOTH);
//            Toast.makeText(activity, "Read External Storage permission needed for reading files. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.BLUETOOTH},BLUETOOTH);
        }
    }

    public boolean checkPermissionForBlue_Admin(){
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_ADMIN);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermissionForBlue_Admin(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.BLUETOOTH_ADMIN)){
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.BLUETOOTH_ADMIN},BLUETOOTH_ADMIN);
//            Toast.makeText(activity, "Read External Storage permission needed for reading files. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.BLUETOOTH_ADMIN},BLUETOOTH_ADMIN);
        }
    }

    public boolean checkPermissionForCoarseLocation(){
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermissionForCoarseLoaction(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_COARSE_LOCATION)){
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},COARSE_LOCATION);
//            Toast.makeText(activity, "Read External Storage permission needed for reading files. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},COARSE_LOCATION);
        }
    }

}


