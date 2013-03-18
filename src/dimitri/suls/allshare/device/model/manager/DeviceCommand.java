package dimitri.suls.allshare.device.model.manager;

import com.sec.android.allshare.Device;

public interface DeviceCommand {
	void execute(Device selectedDevice);
}