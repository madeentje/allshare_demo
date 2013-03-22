package dimitri.suls.allshare.device.model.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.sec.android.allshare.Device;
import com.sec.android.allshare.Device.DeviceType;
import com.sec.android.allshare.DeviceFinder;
import com.sec.android.allshare.DeviceFinder.IDeviceFinderEventListener;
import com.sec.android.allshare.ERROR;
import com.sec.android.allshare.control.TVController;
import com.sec.android.allshare.media.AVPlayer;

import dimitri.suls.allshare.avplayer.listeners.AVPlayerEventListener;
import dimitri.suls.allshare.avplayer.listeners.AVPlayerPlaybackResponseListener;
import dimitri.suls.allshare.avplayer.listeners.AVPlayerVolumeResponseListener;
import dimitri.suls.allshare.serviceprovider.model.managers.ServiceProviderManager;
import dimitri.suls.allshare.tv.listeners.TVEventListener;
import dimitri.suls.allshare.tv.listeners.TVResponseListener;

public class DeviceManager {
	private List<DeviceObserver> deviceObservers = null;
	private DeviceType deviceType = null;
	private DeviceFinder deviceFinder = null;
	private HashMap<String, Device> devices = null;
	private Device selectedDevice = null;

	public DeviceManager(DeviceType deviceType, ServiceProviderManager serviceProviderManager) {
		this.deviceObservers = new ArrayList<DeviceObserver>();
		this.deviceType = deviceType;
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
			// TODO: Finish adding other devicetypes
			if (deviceType == DeviceType.DEVICE_TV_CONTROLLER) {
				TVController tvController = (TVController) selectedDevice;

				tvController.setEventListener(new TVEventListener());
				tvController.setResponseListener(new TVResponseListener());

				tvController.connect();
			} else if (deviceType == DeviceType.DEVICE_AVPLAYER) {
				AVPlayer avPlayer = (AVPlayer) selectedDevice;

				avPlayer.setEventListener(new AVPlayerEventListener());
				avPlayer.setResponseListener(new AVPlayerPlaybackResponseListener());
				avPlayer.setResponseListener(new AVPlayerVolumeResponseListener());

				avPlayer.setVolume(20);
				avPlayer.setMute(false);
			}
		}

		notifySelectedDeviceHasChanged();
	}

	public List<Device> getDevices() {
		deviceFinder.setDeviceFinderEventListener(deviceType, new IDeviceFinderEventListener() {
			@Override
			public void onDeviceAdded(DeviceType deviceType, Device device, ERROR error) {
				if (!devices.containsKey(device.getIPAddress())) {
					devices.put(device.getIPAddress(), device);

					notifyDeviceWasAdded(device);
				}
			}

			@Override
			public void onDeviceRemoved(DeviceType deviceType, Device device, ERROR error) {
				devices.remove(device.getIPAddress());

				notifyDeviceWasRemoved(device);

				if (device == selectedDevice) {
					setSelectedDevice(null);
				}
			}
		});

		devices = new HashMap<String, Device>();

		ArrayList<Device> initialDeviceList = deviceFinder.getDevices(deviceType);

		for (Device device : initialDeviceList) {
			devices.put(device.getIPAddress(), device);
		}

		return initialDeviceList;
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