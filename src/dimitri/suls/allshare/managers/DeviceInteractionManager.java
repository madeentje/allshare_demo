package dimitri.suls.allshare.managers;

import com.sec.android.allshare.Device;
import com.sec.android.allshare.Device.DeviceType;
import com.sec.android.allshare.control.TVController;

import dimitri.suls.allshare.control.tv.EventListener;
import dimitri.suls.allshare.control.tv.ResponseListener;
import dimitri.suls.allshare.control.tv.TVCommand;

public class DeviceInteractionManager implements DeviceFinderObserver {

	private Device selectedDevice;

	public DeviceInteractionManager(DeviceFinderManager deviceFinderManager) {
		deviceFinderManager.addObserver(this);
	}

	@Override
	public void changedSelectedDevice(Device selectedDevice) {
		this.selectedDevice = selectedDevice;

		if (selectedDevice != null) {
			if (selectedDevice.getDeviceType() == DeviceType.DEVICE_TV_CONTROLLER) {
				TVController tvController = (TVController) selectedDevice;

				tvController.setEventListener(new EventListener());
				tvController.setResponseListener(new ResponseListener());

				tvController.connect();
			}
		}
	}

	@Override
	public void addedDevice(Device device) {
	}

	@Override
	public void removedDevice(Device device) {
	}

	public void execute(TVCommand tvCommand) {
		tvCommand.execute((TVController) selectedDevice);
	}
}