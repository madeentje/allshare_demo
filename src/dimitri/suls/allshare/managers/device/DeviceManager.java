package dimitri.suls.allshare.managers.device;

import java.util.ArrayList;
import java.util.List;

import com.sec.android.allshare.Device;
import com.sec.android.allshare.Device.DeviceType;
import com.sec.android.allshare.DeviceFinder;
import com.sec.android.allshare.DeviceFinder.IDeviceFinderEventListener;
import com.sec.android.allshare.ERROR;
import com.sec.android.allshare.control.TVController;

import dimitri.suls.allshare.control.tv.EventListener;
import dimitri.suls.allshare.control.tv.ResponseListener;
import dimitri.suls.allshare.managers.serviceprovider.ServiceProviderManager;

public class DeviceManager {
	private List<DeviceObserver> deviceObservers = null;
	private DeviceFinder deviceFinder = null;
	private List<Device> devices = null;
	private Device selectedDevice = null;

	public DeviceManager(ServiceProviderManager serviceProviderManager) {
		this.deviceObservers = new ArrayList<DeviceObserver>();
		this.deviceFinder = serviceProviderManager.getDeviceFinder();
	}

	public void addObserver(DeviceObserver deviceObserver) {
		deviceObservers.add(deviceObserver);
	}

	public void removeObserver(DeviceObserver deviceObserver) {
		deviceObservers.remove(deviceObserver);
	}

	public void setSelectedDevice(Device selectedDevice) {
		this.selectedDevice = selectedDevice;

		if (selectedDevice != null) {
			if (selectedDevice.getDeviceType() == DeviceType.DEVICE_TV_CONTROLLER) {
				TVController tvController = (TVController) selectedDevice;

				tvController.setEventListener(new EventListener());
				tvController.setResponseListener(new ResponseListener());

				tvController.connect();
			}
		}

		notifySelectedDeviceHasChanged();
	}

	public List<Device> getDevices(DeviceType deviceType) {
		deviceFinder.setDeviceFinderEventListener(deviceType, new IDeviceFinderEventListener() {
			@Override
			public void onDeviceAdded(DeviceType deviceType, Device device, ERROR error) {
				devices.add(device);

				// TODO: Bug when device is added twice after sleeping, or
				// turning Wi-FI on.. (not sure why)
				// TODO: Add Listener to check when user deactivated Wi-Fi while
				// the app is already started
				// TODO: Add a "retry"-button so the user can activate the Wi-FI
				// without having to restart the app.
				notifyDeviceWasAdded(device);
			}

			@Override
			public void onDeviceRemoved(DeviceType deviceType, Device device, ERROR error) {
				devices.remove(device);

				notifyDeviceWasRemoved(device);

				if (device == selectedDevice) {
					setSelectedDevice(null);
				}
			}
		});

		devices = deviceFinder.getDevices(deviceType);

		return devices;
	}

	private void notifySelectedDeviceHasChanged() {
		for (DeviceObserver deviceObserver : deviceObservers) {
			deviceObserver.changedSelectedDevice(selectedDevice);
		}
	}

	private void notifyDeviceWasAdded(Device device) {
		for (DeviceObserver deviceObserver : deviceObservers) {
			deviceObserver.addedDevice(device);
		}
	}

	private void notifyDeviceWasRemoved(Device device) {
		for (DeviceObserver deviceObserver : deviceObservers) {
			deviceObserver.removedDevice(device);
		}
	}

	public void execute(DeviceCommand deviceCommand) {
		deviceCommand.execute(selectedDevice);
	}
}