package dimitri.suls.allshare.device;

import com.sec.android.allshare.Device;

public interface DeviceObserver {
	void changedSelectedDevice(Device selectedDevice);

	void addedDevice(Device device);

	void removedDevice(Device device);
}