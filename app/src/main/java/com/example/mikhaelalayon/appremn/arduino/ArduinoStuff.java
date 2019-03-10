package com.example.mikhaelalayon.appremn.arduino;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.mikhaelalayon.appremn.R;

public class ArduinoStuff {

    private static final String TAG = "arduino";

    private Context ctx;
    private ArduinoInterface arduino;

    ArduinoStuff(ArduinoInterface arduino, Context ctx) {
        this.arduino = arduino;
        this.ctx = ctx;

        //check paired devices
        BluetoothClass.Device sketch = getSketch();
        //find "Distance Sketch"

        //add event handlers stuff
    }

    private BluetoothClass.Device getSketch() {
        final BluetoothManager bluetoothManager =
                (BluetoothManager) ctx.getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter mBluetoothAdapter = bluetoothManager.getAdapter();

        mBluetoothAdapter.enable();

        Log.i(TAG, "Paired devices: " + String.valueOf(mBluetoothAdapter.getBondedDevices().size()));

        mLeDeviceListAdapter.clear();

        for (BluetoothDevice device : mBluetoothAdapter.getBondedDevices()) {
            mLeDeviceListAdapter.addDevice(device);
        }
    }

}
