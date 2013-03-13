package dimitri.suls.allshare.managers.device;

import com.sec.android.allshare.Device;

public interface DeviceObserver<T extends Device> {
	void changedSelectedDevice(T selectedDevice);

	void addedDevice(T device);

	void removedDevice(T device);
}