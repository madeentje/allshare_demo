package dimitri.suls.allshare.device.model.manager;

import com.sec.android.allshare.Device;

public interface DeviceObserver {
	void changedSelectedDevice(Device selectedDevice);

	void addedDevice(Device device);

	void removedDevice(Device device);
}