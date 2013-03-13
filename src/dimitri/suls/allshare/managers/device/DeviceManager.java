package dimitri.suls.allshare.managers.device;

import java.util.ArrayList;
import java.util.List;

import com.sec.android.allshare.Device;
import com.sec.android.allshare.Device.DeviceType;
import com.sec.android.allshare.DeviceFinder;
import com.sec.android.allshare.DeviceFinder.IDeviceFinderEventListener;
import com.sec.android.allshare.ERROR;
import com.sec.android.allshare.control.TVController;

import dimitri.suls.allshare.control.tv.TVEventListener;
import dimitri.suls.allshare.control.tv.TVResponseListener;
import dimitri.suls.allshare.managers.serviceprovider.ServiceProviderManager;

public class DeviceManager<T extends Device> {
	private List<DeviceObserver<T>> deviceObservers = null;
	private DeviceType deviceType = null;
	private DeviceFinder deviceFinder = null;
	private List<Device> devices = null;
	private T selectedDevice = null;

	public DeviceManager(DeviceType deviceType, ServiceProviderManager serviceProviderManager) {
		this.deviceObservers = new ArrayList<DeviceObserver<T>>();
		this.deviceType = deviceType;
		this.deviceFinder = serviceProviderManager.getDeviceFinder();
	}

	public void addObserver(DeviceObserver<T> deviceObserver) {
		deviceObservers.add(deviceObserver);
	}

	public void removeObserver(DeviceObserver<T> deviceObserver) {
		deviceObservers.remove(deviceObserver);
	}

	public void setSelectedDevice(T selectedDevice) {
		this.selectedDevice = selectedDevice;

		if (selectedDevice != null) {
			// TODO: Finish adding other devicetypes
			if (selectedDevice.getDeviceType() == DeviceType.DEVICE_TV_CONTROLLER) {
				TVController tvController = (TVController) selectedDevice;

				tvController.setEventListener(new TVEventListener());
				tvController.setResponseListener(new TVResponseListener());

				tvController.connect();
			}
		}

		notifySelectedDeviceHasChanged();
	}

	public List<Device> getDevices() {
		deviceFinder.setDeviceFinderEventListener(deviceType, new IDeviceFinderEventListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onDeviceAdded(DeviceType deviceType, Device device, ERROR error) {
				devices.add(device);

				// TODO: Bug when device is added twice after sleeping, or
				// turning Wi-FI on.. (not sure why)
				// TODO: Add Listener to check when user deactivated Wi-Fi while
				// the app is already started
				// TODO: Add a "retry"-button so the user can activate the Wi-FI
				// without having to restart the app.
				notifyDeviceWasAdded((T) device);
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onDeviceRemoved(DeviceType deviceType, Device device, ERROR error) {
				devices.remove(device);

				notifyDeviceWasRemoved((T) device);

				if (device == selectedDevice) {
					setSelectedDevice(null);
				}
			}
		});

		devices = deviceFinder.getDevices(deviceType);

		return devices;
	}

	private void notifySelectedDeviceHasChanged() {
		for (DeviceObserver<T> deviceObserver : deviceObservers) {
			deviceObserver.changedSelectedDevice(selectedDevice);
		}
	}

	private void notifyDeviceWasAdded(T device) {
		for (DeviceObserver<T> deviceObserver : deviceObservers) {
			deviceObserver.addedDevice(device);
		}
	}

	private void notifyDeviceWasRemoved(T device) {
		for (DeviceObserver<T> deviceObserver : deviceObservers) {
			deviceObserver.removedDevice(device);
		}
	}

	public void execute(DeviceCommand<T> deviceCommand) {
		deviceCommand.execute(selectedDevice);
	}
}