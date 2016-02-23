package mikevanderheijden.nl.bluetoothtrigger;

import android.bluetooth.BluetoothDevice;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Mike on 18-2-2016.
 */
public class BluetoothBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            Set<String> devices = getDevicesFromFile(context);
            if (devices.contains(device.getName())) {
                startApplication(context);
            }
        }
    }

    private void startApplication(Context context) {
        try {
            final Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(new ComponentName("com.spotify.music", "com.spotify.music.MainActivity"));
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.i("ActivityNotFound", "Unable to launch application");
            Toast.makeText(context, "Unable to launch application Spotify.", Toast.LENGTH_LONG).show();
        }
    }

    private Set<String> getDevicesFromFile(Context context) {
        String filename = context.getResources().getString(R.string.filename);
        try {
            FileInputStream fileInputStream = context.openFileInput(filename);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            return (Set<String>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            Log.i("IOException", "Unable to read file");
            Toast.makeText(context, "Unable to load your checked devices.", Toast.LENGTH_LONG).show();
            return new HashSet<>();
        }
    }
}