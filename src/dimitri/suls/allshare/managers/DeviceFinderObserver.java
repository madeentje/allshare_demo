package dimitri.suls.allshare.managers;

import com.sec.android.allshare.Device;

public interface DeviceFinderObserver {
	void changedSelectedDevice(Device selectedDevice);

	void addedDevice(Device device);

	void removedDevice(Device device);
}