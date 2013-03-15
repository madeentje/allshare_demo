package dimitri.suls.allshare.gui.helpers;

import java.util.List;

import android.content.Context;
import android.widget.ListView;
import android.widget.TextView;

import com.sec.android.allshare.Device;

import dimitri.suls.allshare.gui.listadapters.DeviceAdapter;
import dimitri.suls.allshare.managers.device.DeviceManager;
import dimitri.suls.allshare.managers.device.DeviceObserver;

public class DeviceFrontendManager implements DeviceObserver {
	private Context context = null;
	private DeviceManager deviceManager = null;
	private TabManager tabManager = null;
	private ListView listViewDevices = null;
	private TextView textViewSelectedDevice = null;
	private String deviceType = null;

	public DeviceFrontendManager(Context context, DeviceManager deviceManager, TabManager tabManager, ListView listViewDevices,
			TextView textViewSelectedDevice, String deviceType) {
		this.context = context;
		this.deviceManager = deviceManager;
		this.tabManager = tabManager;
		this.listViewDevices = listViewDevices;
		this.textViewSelectedDevice = textViewSelectedDevice;
		this.deviceType = deviceType;

		deviceManager.addObserver(this);

		listViewDevices.setOnItemClickListener(new DeviceListItemClickListener(deviceManager));

		refreshDeviceList();
	}

	@Override
	public void changedSelectedDevice(Device selectedDevice) {
		if (selectedDevice == null) {
			textViewSelectedDevice.setText("No " + deviceType + " selected.");

			tabManager.resetCurrentTab();

			// TODO: Find way to enable/disable correct tabs
			tabManager.setEnabledTabRemote(false);
		} else {
			textViewSelectedDevice.setText("Selected " + deviceType + ": " + selectedDevice.getName());

			tabManager.setEnabledTabRemote(true);
		}
	}

	@Override
	public void addedDevice(Device device) {
		DeviceAdapter arrayAdapter = (DeviceAdapter) listViewDevices.getAdapter();

		arrayAdapter.add(device);
	}

	@Override
	public void removedDevice(Device device) {
		DeviceAdapter arrayAdapter = (DeviceAdapter) listViewDevices.getAdapter();

		arrayAdapter.remove(device);
	}

	public void refreshDeviceList() {
		List<Device> devices = deviceManager.getDevices();
		DeviceAdapter deviceAdapter = new DeviceAdapter(context, devices);

		listViewDevices.setAdapter(deviceAdapter);
		deviceManager.setSelectedDevice(null);
	}
}