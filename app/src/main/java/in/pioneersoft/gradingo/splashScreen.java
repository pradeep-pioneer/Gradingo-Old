package in.pioneersoft.gradingo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.text.Html;
import android.widget.Toast;


import com.jaredrummler.android.device.DeviceName;

import java.io.File;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class splashScreen extends AppCompatActivity {
    private View mControlsView;
    private View mContentView;
    private ProgressBar mProgressIndicator;
    private TextView mProgressLabel;
    private boolean mVisible;

    //Data
    private DeviceData data = new DeviceData();

    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);
        mProgressIndicator = (ProgressBar)findViewById(R.id.progressIndicatorBar);
        mProgressLabel = (TextView)findViewById(R.id.progressIndicator);
        // Start lengthy operation in a background thread
        start();
    }

    void start(){
        try {
            mProgressLabel.setText(Html.fromHtml("<b>Loading</b>: Branding Info..."));
            DeviceName.with(this).request(new DeviceName.Callback() {
                @Override
                public void onFinished(final DeviceName.DeviceInfo info, Exception error) {
                    data.setManufacturer(info);
                    data.setCodeName(info);
                    data.setMarketName(info);
                    data.setModel(info);
                    data.setFullName(info);
                    updateProgress("Branding Info Completed", 25);
                    updateProgress("Telephony Info", 5);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadIMEI();
                            Intent intent = new Intent(splashScreen.this, DeviceDetails.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("DeviceData", data);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            });
        }
        catch (Exception e){
            mProgressLabel.setText(e.getMessage());
        }
    }

    public void doPermissionGrantedStuffs(){
        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        data.setImeiNumber1(tm.getDeviceId(1)); //(API level 23)
        data.setImeiNumber2(tm.getDeviceId(2));
        updateProgress("Identifier completed",10);
        data.setSoftwareVersion(tm.getDeviceSoftwareVersion());
        updateProgress("Software completed",5);
        int phoneTypeId=tm.getPhoneType();
        switch (phoneTypeId)
        {
            case (TelephonyManager.PHONE_TYPE_CDMA):
                data.setPhoneType("CDMA");
                break;
            case (TelephonyManager.PHONE_TYPE_GSM):
                data.setPhoneType("GSM");
                break;
            case (TelephonyManager.PHONE_TYPE_NONE):
                data.setPhoneType("None");
                break;
            case (TelephonyManager.PHONE_TYPE_SIP):
                data.setPhoneType("SIP");
                break;
        }
        updateProgress("Phone Type completed",10);
        int simStateId=tm.getSimState();
        switch(simStateId)
        {
            case TelephonyManager.SIM_STATE_ABSENT :
                data.setSimState("Sim - Not Present");
                break;
            case TelephonyManager.SIM_STATE_NETWORK_LOCKED :
                data.setSimState("Sim - Network Locked");
                break;
            case TelephonyManager.SIM_STATE_PIN_REQUIRED :
                data.setSimState("Sim - Pin Required");
                break;
            case TelephonyManager.SIM_STATE_PUK_REQUIRED :
                data.setSimState("Sim - PUK Required");
                break;
            case TelephonyManager.SIM_STATE_READY :
                data.setSimState("Sim - Ready");
                break;
            case TelephonyManager.SIM_STATE_UNKNOWN :
                data.setSimState("Sim - Unknown");
                break;

        }
        updateProgress("Sim Type completed",10);
        //
        data.setWifiMACAddress(getMacAddr());
        updateProgress("Wi-Fi Mac Address Completed",5);
        //BluetoothAdapter.getDefaultAdapter().getAddress()
        data.setBluetoothMacAddress(getBtAddr());
        updateProgress("Bluetooth Mac Address completed",5);
        getRamSize();
        updateProgress("RAM Capacity completed",10);
        data.setInternalMemoryTotal(getTotalInternalMemorySize());
        data.setInternalMemoryAvailable(getAvailableInternalMemorySize());
        updateProgress("Internal Memory Capacity completed",10);
        data.setExternalMemoryTotal(getTotalExternalMemorySize());
        data.setExternalMemoryAvailable(getAvailableExternalMemorySize());
        updateProgress("External Memory Capacity completed",10);
    }

    public void getRamSize(){
        ActivityManager actManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        actManager.getMemoryInfo(memInfo);
        long totalMemory = memInfo.totalMem;
        data.setRamTotalSize(formatSize(totalMemory));
        data.setRamAvailableSize(formatSize(memInfo.availMem));
    }

    public static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }

    public static String getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return formatSize(availableBlocks * blockSize);
    }

    public static String getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return formatSize(totalBlocks * blockSize);
    }

    public static String getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return formatSize(availableBlocks * blockSize);
        } else {
            return "External Memory: NA";
        }
    }

    public static String getTotalExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return formatSize(totalBlocks * blockSize);
        } else {
            return "External Memory: NA";
        }
    }

    public static String formatSize(long size) {
        String suffix = null;

        if (size >= 1024) {
            suffix = " KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = " MB";
                size /= 1024;
            }
            if(size>=1024){
                suffix = " GB";
                size /= 1024;
            }
        }

        StringBuilder resultBuffer = new StringBuilder(Long.toString(size));

        int commaOffset = resultBuffer.length() - 3;
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',');
            commaOffset -= 3;
        }

        if (suffix != null) resultBuffer.append(suffix);
        return resultBuffer.toString();
    }

    public String getBtAddr(){
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


        // BluetoothAdapter.getDefaultAdapter().DEFAULT_MAC_ADDRESS;
        // if device does not support Bluetooth
        if (mBluetoothAdapter == null) {
            return "device does not support bluetooth";
        }

        String address = mBluetoothAdapter.getAddress();
        if (address.equals("02:00:00:00:00:00")) {

            try {


                ContentResolver mContentResolver = this.getContentResolver();

                address = Settings.Secure.getString(mContentResolver, "bluetooth_address");

            } catch (Exception e) {
                address = "Exception: " + e.getMessage();
            }


        }
        return address;
    }

    public String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }

    public void loadIMEI() {
        // Check if the READ_PHONE_STATE permission is already available.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            // READ_PHONE_STATE permission has not been granted.
            requestReadPhoneStatePermission();
        } else {
            // READ_PHONE_STATE permission is already been granted.
            doPermissionGrantedStuffs();
        }
    }

    /**
     * Requests the READ_PHONE_STATE permission.
     * If the permission has been denied previously, a dialog will prompt the user to grant the
     * permission, otherwise it is requested directly.
     */
    private void requestReadPhoneStatePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_PHONE_STATE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            new AlertDialog.Builder(this)
                    .setTitle("Permission Request")
                    .setMessage("Permission needed!")
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //re-request
                            ActivityCompat.requestPermissions(splashScreen.this,
                                    new String[]{Manifest.permission.READ_PHONE_STATE},
                                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
                        }
                    })
                    .setIcon(Integer.parseInt("@mipmap/ic_launcher"))
                    .show();
        } else {
            // READ_PHONE_STATE permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},
                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_READ_PHONE_STATE) {
            // Received permission result for READ_PHONE_STATE permission.est.");
            // Check if the only required permission has been granted
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // READ_PHONE_STATE permission has been granted, proceed with displaying IMEI Number
                //alertAlert(getString(R.string.permision_available_read_phone_state));
                doPermissionGrantedStuffs();
            } else {
                alertAlert("Permission not granted!");
            }
        }
    }

    private void alertAlert(String msg) {
        new AlertDialog.Builder(this)
                .setTitle("Permission Request")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do somthing here
                    }
                })
                .setIcon(Integer.parseInt("@mipmap/ic_launcher"))
                .show();
    }

    void updateProgress(final String stage, final Integer increment){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressLabel.setText(Html.fromHtml("<b>Loading: </b>" + stage + "..."));
                mProgressIndicator.incrementProgressBy(increment);
            }
        });
    }
}
