package dimitri.suls.allshare.managers.serviceprovider;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.sec.android.allshare.DeviceFinder;
import com.sec.android.allshare.ERROR;
import com.sec.android.allshare.ServiceConnector;
import com.sec.android.allshare.ServiceConnector.IServiceConnectEventListener;
import com.sec.android.allshare.ServiceConnector.ServiceState;
import com.sec.android.allshare.ServiceProvider;

public class ServiceProviderManager {
	private List<ServiceProviderObserver> serviceProviderObservers = null;
	private ServiceProvider serviceProvider = null;

	public ServiceProviderManager(Context context) throws Exception {
		this.serviceProviderObservers = new ArrayList<ServiceProviderObserver>();

		ERROR error = ServiceConnector.createServiceProvider(context, new IServiceConnectEventListener() {
			@Override
			public void onDeleted(ServiceProvider theServiceProvider) {
				serviceProvider = null;
			}

			@Override
			public void onCreated(ServiceProvider theServiceProvider, ServiceState serviceState) {
				serviceProvider = theServiceProvider;

				notifyServiceProviderWasCreated();
			}
		});

		if (!isWifiConnected(context)) {
			throw new Exception("Wi-Fi is not connected!");
		} else if (error != ERROR.SUCCESS) {
			throw new Exception(error.toString() + "!");
		}
	}

	private boolean isWifiConnected(Context context) {
		boolean isWifiConnected = false;

		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] allNetworkInfo = connectivityManager.getAllNetworkInfo();

		for (NetworkInfo networkInfo : allNetworkInfo) {
			if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI && networkInfo.isConnected()) {
				isWifiConnected = true;
			}
		}

		return isWifiConnected;
	}

	public void addObserver(ServiceProviderObserver serviceProviderObserver) {
		serviceProviderObservers.add(serviceProviderObserver);
	}

	public void removeObserver(ServiceProviderObserver serviceProviderObserver) {
		serviceProviderObservers.remove(serviceProviderObserver);
	}

	private void notifyServiceProviderWasCreated() {
		for (ServiceProviderObserver serviceProviderObserver : serviceProviderObservers) {
			serviceProviderObserver.createdServiceProvider();
		}
	}

	public DeviceFinder getDeviceFinder() {
		return serviceProvider.getDeviceFinder();
	}

	public void close() {
		ServiceConnector.deleteServiceProvider(serviceProvider);
	}
}