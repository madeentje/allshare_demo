package dimitri.suls.allshare.serviceprovider.model.managers;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

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

		if (error != ERROR.SUCCESS) {
			throw new Exception(error.toString() + "!");
		}
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