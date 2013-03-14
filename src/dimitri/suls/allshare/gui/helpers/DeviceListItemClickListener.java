package dimitri.suls.allshare.gui.helpers;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.sec.android.allshare.Device;

import dimitri.suls.allshare.managers.device.DeviceManager;

public class DeviceListItemClickListener implements OnItemClickListener {
	private DeviceManager deviceManager = null;

	public DeviceListItemClickListener(DeviceManager deviceManager) {
		this.deviceManager = deviceManager;
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		Device selectedDevice = (Device) adapterView.getItemAtPosition(position);

		deviceManager.setSelectedDevice(selectedDevice);
	}
}