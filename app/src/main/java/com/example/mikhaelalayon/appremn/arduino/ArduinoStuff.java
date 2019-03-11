package com.example.mikhaelalayon.appremn.arduino;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

import static android.content.Context.BIND_AUTO_CREATE;

public class ArduinoStuff {

    private static final String TAG = "arduino";

    private static final UUID DIST_CHAR_UUID;

    static {
        DIST_CHAR_UUID = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb");
    }

    private Handler handler;
    private BluetoothDevice device;
    private Context ctx;
    private ArduinoInterface arduino;
    private BluetoothLeService mBluetoothLeService;
    private ServiceConnection mServiceConnection;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics;
    private BluetoothGattCharacteristic mNotifyCharacteristic;

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                Log.d(TAG, "Gatt Connected");
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                Log.d(TAG, "Gatt Disconnected");
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                Log.d(TAG, "Gatt Discovered");
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                Log.d(TAG, "Gatt Data Received");
                int dist = intent.getIntExtra(BluetoothLeService.EXTRA_DATA, -1);
                Log.i(TAG, "data: " + String.valueOf(dist));
                if (dist == -1) {
                    Log.e(TAG, "Invalid Data Received");
                } else {
                    arduino.update(dist);
                }
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    ArduinoStuff(ArduinoInterface arduino, Context ctx) {
        handler = new Handler();
        this.arduino = arduino;
        this.ctx = ctx;
        mGattCharacteristics = new ArrayList<>();

        //check paired devices
        BluetoothDevice sketch = getSketch();
        device = sketch;
        if (sketch == null) {
            Log.e(TAG, "Couldn't find Sketch");
            return;
        }

        mServiceConnection = getService(sketch.getAddress());
        registerHandler();
    }

    private void registerDistChar(BluetoothGattCharacteristic characteristic) {
        ctx.registerReceiver(mGattUpdateReceiver, SampleGattAttributes.makeGattUpdateIntentFilter());
        mNotifyCharacteristic = characteristic;
        mBluetoothLeService.setCharacteristicNotification(
                characteristic, true);
    }

    private void onServiceConnected() {
        Log.i(TAG, "Service connected.");
        handler.postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void run() {
                Log.i(TAG, "Delayed 2 secs");
                for(BluetoothGattService service : mBluetoothLeService.getSupportedGattServices()) {
                    for(BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                        if (characteristic.getUuid().equals(DIST_CHAR_UUID)) {
                            registerDistChar(characteristic);
                        }
                    }
                }
            }
        }, 2000);

    }

    void registerHandler() {
        Intent gattServiceIntent = new Intent(ctx, BluetoothLeService.class);
        try {
            if (!ctx.bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE)) {
                Log.i(TAG, "error");
            }
        } catch (SecurityException e) {
            e.printStackTrace();
            Log.e(TAG, "Needs permissions");
        }
    }

    private ServiceConnection getService(final String address) {
        Log.i(TAG, "Getting service address: " + address);
        return new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder service) {
                Log.i(TAG, "Connected");
                mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
                if (!mBluetoothLeService.initialize()) {
                    Log.e(TAG, "Unable to initialize Bluetooth");
                    return;
                }
                // Automatically connects to the device upon successful start-up initialization.
                mBluetoothLeService.connect(address);

                ArduinoStuff.this.onServiceConnected();
            }
            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                mBluetoothLeService = null;
            }
        };
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private BluetoothDevice getSketch() {
        Log.i(TAG, "getting sketch");
        final BluetoothManager bluetoothManager =
                (BluetoothManager) ctx.getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter mBluetoothAdapter = bluetoothManager.getAdapter();

        mBluetoothAdapter.enable();

        Log.i(TAG, "Paired devices: " + String.valueOf(mBluetoothAdapter.getBondedDevices().size()));

        for (BluetoothDevice device : mBluetoothAdapter.getBondedDevices()) {
            Log.i(TAG, device.getName());
            if (device.getName().equals("DistanceSketch"))
                return device;
        }
        return null;
    }

}