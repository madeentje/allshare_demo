package dimitri.suls.allshare.device;

import com.sec.android.allshare.Device;

public interface DeviceCommand {
	void execute(Device selectedDevice);
}