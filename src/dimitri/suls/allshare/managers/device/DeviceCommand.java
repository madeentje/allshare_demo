package dimitri.suls.allshare.managers.device;

import com.sec.android.allshare.Device;

public interface DeviceCommand<T extends Device> {
	void execute(T selectedDevice);
}