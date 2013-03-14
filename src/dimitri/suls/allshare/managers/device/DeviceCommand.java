package dimitri.suls.allshare.managers.device;

import com.sec.android.allshare.Device;

public interface DeviceCommand {
	void execute(Device selectedDevice);
}