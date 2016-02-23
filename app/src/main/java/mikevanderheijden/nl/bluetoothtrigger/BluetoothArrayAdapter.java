package mikevanderheijden.nl.bluetoothtrigger;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Mike on 18-2-2016.
 */
public class BluetoothArrayAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final String[] objects;
    private Set<String> checkedDevices;

    public BluetoothArrayAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
        this.checkedDevices = getDevicesFromFile();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item_bluetooth_device, parent, false);
        final Switch aSwitch = (Switch) rowView.findViewById(R.id.device_switch);
        final String device = objects[position];
        aSwitch.setText(device);
        aSwitch.setTextColor(Color.parseColor("#000000"));
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkedDevices.add(device);
                } else {
                    checkedDevices.remove(device);
                }
                writeDevicesToFile(checkedDevices);
            }
        });
        if (checkedDevices.contains(device)) {
            aSwitch.setChecked(true);
        }
        return rowView;
    }

    private Set<String> getDevicesFromFile() {
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

    private void writeDevicesToFile(Set<String> devices) {
        String filename = context.getResources().getString(R.string.filename);
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(devices);
            objectOutputStream.close();
        } catch (IOException e) {
            Log.i("IOException", "Unable write to file");
            Toast.makeText(context, "Unable to save the devices.", Toast.LENGTH_LONG).show();
        }
    }
}
