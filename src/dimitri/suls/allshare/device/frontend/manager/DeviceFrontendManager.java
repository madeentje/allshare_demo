package dimitri.suls.allshare.device.frontend.manager;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.sec.android.allshare.Device;

import dimitri.suls.allshare.activities.helpers.TabManager;
import dimitri.suls.allshare.device.frontend.listadapters.DeviceAdapter;
import dimitri.suls.allshare.device.model.manager.DeviceManager;
import dimitri.suls.allshare.device.model.manager.DeviceObserver;

public class DeviceFrontendManager implements DeviceObserver {
	private Context context = null;
	private DeviceManager deviceManager = null;
	private ListView listViewDevices = null;
	private TextView textViewSelectedDevice = null;
	private String deviceType = null;
	private TabManager tabManager = null;
	private int tabIndex = 0;

	private class DeviceListItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
			Device selectedDevice = (Device) adapterView.getItemAtPosition(position);

			deviceManager.setSelectedDevice(selectedDevice);
		}
	}

	public DeviceFrontendManager(Context context, DeviceManager deviceManager, ListView listViewDevices, TextView textViewSelectedDevice,
			String deviceType, TabManager tabManager, int tabIndex) {
		this.context = context;
		this.deviceManager = deviceManager;
		this.listViewDevices = listViewDevices;
		this.textViewSelectedDevice = textViewSelectedDevice;
		this.deviceType = deviceType;
		this.tabManager = tabManager;
		this.tabIndex = tabIndex;

		deviceManager.addObserver(this);

		listViewDevices.setOnItemClickListener(new DeviceListItemClickListener());

		refreshDeviceList();
	}

	@Override
	public void changedSelectedDevice(Device selectedDevice) {
		if (selectedDevice == null) {
			textViewSelectedDevice.setText("No " + deviceType + " selected.");

			tabManager.resetCurrentTab();

			tabManager.setTabEnabledStatus(tabIndex, false);
		} else {
			textViewSelectedDevice.setText("Selected " + deviceType + ": " + selectedDevice.getName());

			tabManager.setTabEnabledStatus(tabIndex, true);
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