package mikevanderheijden.nl.bluetoothtrigger;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView listView = (ListView) findViewById(R.id.list_view_bluetooth_devices);

        BluetoothArrayAdapter bluetoothArrayAdapter = new BluetoothArrayAdapter(getApplicationContext(), R.layout.list_item_bluetooth_device, getBluetoothDevices());
        listView.setAdapter(bluetoothArrayAdapter);
    }

    private String[] getBluetoothDevices() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Your device does not support Bluetooth.", Toast.LENGTH_LONG).show();
            return new String[]{};
        }

        if (!bluetoothAdapter.isEnabled()) {
            Toast.makeText(getApplicationContext(), "You need to activate Bluetooth to select your devices.", Toast.LENGTH_LONG).show();
            return new String[]{};
        }

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() == 0) {
            Toast.makeText(getApplicationContext(), "You currently have zero paired Bluetooth devices.", Toast.LENGTH_LONG).show();
            return new String[]{};
        }

        String[] values = new String[pairedDevices.size()];
        int i = 0;
        for (BluetoothDevice bluetoothDevice : pairedDevices) {
            values[i] = bluetoothDevice.getName();
            i++;
        }
        return values;
    }
}
