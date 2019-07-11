package com.example.bluetoothgetzyme;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ChatActivity extends AppCompatActivity {

    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    private String connectedDeviceName = null;

    private StringBuffer outStringBuffer;
    private BluetoothAdapter bluetoothAdapter = null;
    private ChatService chatService = null;

    Button btn_A, btn_B, btn_C;
    private boolean isPermissionGranted = false;
    RequestQueue requestQueue;

    ArrayList<VerticalModel> vm;

    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case ChatService.STATE_CONNECTED:
                            setStatus(getString(R.string.title_connected_to,
                                    connectedDeviceName));
                            break;
                        case ChatService.STATE_CONNECTING:
                            setStatus(R.string.title_connecting);
                            break;
                        case ChatService.STATE_LISTEN:
                        case ChatService.STATE_NONE:
                            setStatus(R.string.title_not_connected);
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;

                    break;
                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;

                    String readMessage = new String(readBuf, 0, msg.arg1);
                    //chatArrayAdapter.add(connectedDeviceName + ":  " + readMessage);

                    if (readMessage.length() < 4) {
                        final String substring = readMessage.substring(1);
                        char btn_text = readMessage.charAt(0);

                        if (btn_text == 'A') {
                            btn_A.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    btn_A.setBackgroundColor(Color.BLACK);
                                    btn_A.setTextColor(Color.WHITE);

                                    getDataFromAPI(substring);
                                }
                            }, 2000);
                        } else if (btn_text == 'B') {
                            btn_B.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    btn_B.setBackgroundColor(Color.BLACK);
                                    btn_B.setTextColor(Color.WHITE);
                                    getDataFromAPI(substring);
                                }
                            }, 2000);
                        } else if (btn_text == 'C') {
                            btn_C.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    btn_C.setBackgroundColor(Color.BLACK);
                                    btn_C.setTextColor(Color.WHITE);
                                    getDataFromAPI(substring);
                                }
                            }, 2000);
                        }
                    } else{
                        startActivity(new Intent(ChatActivity.this,MyDataActivity.class)
                        .putExtra("KEY_STRING",readMessage));

                    }
                    break;
                case MESSAGE_DEVICE_NAME:

                    connectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(),
                            "Connected to " + connectedDeviceName,
                            Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(),
                            msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
                            .show();
                    break;

            }
            return false;
        }
    });

    private void getDataFromAPI(String substring) {

        String url = getResources().getString(R.string.api_url) + substring;

        //Log.e("URL", url);

        /*final JsonObjectRequest mStringRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

               // String responseData = response;

                sendMessage(response.toString(), MESSAGE_WRITE);
            }
//
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();


            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                //params.put("Authorization", "Bearer " + accessToken);
                return params;
            }
        };*/

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET,url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());

                        sendMessage(response.toString(), MESSAGE_WRITE);

                        //String response1 =  response.toString();
                        //JSONObject jo = new JSONObject(response1);
                       /* vm = new ArrayList<>();
                        try{

                            int a = response.length();

                            for(int i=1 ; i<=a; i++){

                                String re = response.getString(String.valueOf(i));
                                Log.e(""+i,re);

                                VerticalModel vvm = new VerticalModel(re);
                                vm.add(vvm);

                            }

                            sendMessage(response.toString(), MESSAGE_WRITE);

                        }catch (Exception e){
                            Log.e("ABBB",e+"");
                        }*/

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

// Adding request to request queue
        requestQueue.add(jsObjRequest);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        requestQueue = Volley.newRequestQueue(this);

        getWidgetReferences();
        bindEventHandler();

        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available",
                    Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        //.............Permission Demands for Coarse Loactaion for Pairing............//
        CustomPermissionClass cp = new CustomPermissionClass(ChatActivity.this);
        if (!cp.checkPermissionForCoarseLocation()) {
            cp.requestPermissionForCoarseLoaction();
        }
    }

    private void getWidgetReferences() {
        btn_A = findViewById(R.id.btn_A);
        btn_B = findViewById(R.id.btn_B);
        btn_C = findViewById(R.id.btn_C);
    }

    private void bindEventHandler() {

        btn_A.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String message = "A";
                generateRandomNumberAndSendMessage(message);

            }
        });

        btn_B.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String message = "B";
                generateRandomNumberAndSendMessage(message);
            }
        });

        btn_C.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String message = "C";
                generateRandomNumberAndSendMessage(message);

                // sendMessage(message,  MESSAGE_WRITE);
            }
        });
    }

    private void generateRandomNumberAndSendMessage(String message) {

        try {
            Random r1 = new Random();
            int number = r1.nextInt(3) + 10;
            if (number < 10) {
                generateRandomNumberAndSendMessage(message);
            } else {
                sendMessage(message + number, MESSAGE_WRITE);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    setupChat();
                } else {
                    Toast.makeText(this, R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }

    private void connectDevice(Intent data, boolean secure) {
        String address = data.getExtras().getString(
                DeviceListActivity.DEVICE_ADDRESS);
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
        chatService.connect(device, secure);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    private void setupChat() {


        chatService = new ChatService(this, handler);

        outStringBuffer = new StringBuffer("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent serverIntent = null;
        switch (item.getItemId()) {
            case R.id.secure_connect_scan:
                serverIntent = new Intent(this, DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
                return true;
            case R.id.discoverable:
                ensureDiscoverable();
                return true;
        }
        return false;
    }

    private void ensureDiscoverable() {
        if (bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(
                    BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    private void sendMessage(String message, int key) {
        if (chatService.getState() != ChatService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        if (message.length() > 0) {
            byte[] send = message.getBytes();
            chatService.write(send, key);

            outStringBuffer.setLength(0);
        }
    }

    private TextView.OnEditorActionListener mWriteListener = new TextView.OnEditorActionListener() {
        public boolean onEditorAction(TextView view, int actionId,
                                      KeyEvent event) {
            if (actionId == EditorInfo.IME_NULL
                    && event.getAction() == KeyEvent.ACTION_UP) {
                String message = view.getText().toString();
                sendMessage(message, MESSAGE_WRITE);
            }
            return true;
        }
    };

    private final void setStatus(int resId) {
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setSubtitle(resId);
    }

    private final void setStatus(CharSequence subTitle) {
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setSubtitle(subTitle);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 3: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        isPermissionGranted = true;
                        //Request location updates:
                        Toast.makeText(this, "Premission granted", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(this, "Premission denied", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }


    @Override
    public void onStart() {
        super.onStart();

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } else {
            if (chatService == null)
                setupChat();
        }
    }

    @Override
    public synchronized void onResume() {
        super.onResume();

        if (chatService != null) {
            if (chatService.getState() == ChatService.STATE_NONE) {
                chatService.start();
            }
        }
    }

    @Override
    public synchronized void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (chatService != null)
            chatService.stop();
    }

}