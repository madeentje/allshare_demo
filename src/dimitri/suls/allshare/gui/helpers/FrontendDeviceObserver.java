package dimitri.suls.allshare.gui.helpers;

import android.widget.ListView;
import android.widget.TextView;

import com.sec.android.allshare.Device;

import dimitri.suls.allshare.gui.listadapters.DeviceAdapter;
import dimitri.suls.allshare.managers.device.DeviceObserver;

public class FrontendDeviceObserver implements DeviceObserver {
	private TabManager tabManager = null;
	private ListView listViewDevices = null;
	private TextView textViewSelectedDevice = null;
	private String deviceType = null;

	public FrontendDeviceObserver(TabManager tabManager, ListView listViewDevices, TextView textViewSelectedDevice, String deviceType) {
		this.tabManager = tabManager;
		this.listViewDevices = listViewDevices;
		this.textViewSelectedDevice = textViewSelectedDevice;
		this.deviceType = deviceType;
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
}