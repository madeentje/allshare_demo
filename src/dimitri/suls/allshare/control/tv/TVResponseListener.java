package dimitri.suls.allshare.control.tv;

import com.sec.android.allshare.ERROR;
import com.sec.android.allshare.control.TVController;
import com.sec.android.allshare.control.TVController.BrowserMode;
import com.sec.android.allshare.control.TVController.IResponseListener;
import com.sec.android.allshare.control.TVController.TVInformation;

//TODO: Implement listener-events?
public class TVResponseListener implements IResponseListener {
	@Override
	public void onBrowserScrollDownResponseReceived(TVController tv, ERROR result) {
	}

	@Override
	public void onBrowserScrollUpResponseReceived(TVController tv, ERROR result) {
	}

	@Override
	public void onBrowserZoomDefaultResponseReceived(TVController tv, ERROR result) {
	}

	@Override
	public void onBrowserZoomInResponseReceived(TVController tv, ERROR result) {
	}

	@Override
	public void onBrowserZoomOutResponseReceived(TVController tv, ERROR result) {
	}

	@Override
	public void onCloseWebPageResponseReceived(TVController tv, ERROR result) {
	}

	@Override
	public void onConnectResponseReceived(TVController tv, ERROR result) {
	}

	@Override
	public void onDisconnectResponseReceived(TVController tv, ERROR result) {
	}

	@Override
	public void onGetBrowserModeResponseReceived(TVController tv, BrowserMode mode, ERROR result) {
	}

	@Override
	public void onGetBrowserURLResponseReceived(TVController tv, String url, ERROR result) {
	}

	@Override
	public void onGetTVInformationResponseReceived(TVController tv, TVInformation tvInfo, ERROR result) {
	}

	@Override
	public void onGoHomePageResponseReceived(TVController tv, ERROR result) {
	}

	@Override
	public void onGoNextPageResponseReceived(TVController tv, ERROR result) {
	}

	@Override
	public void onGoPreviousPageResponseReceived(TVController tv, ERROR result) {
	}

	@Override
	public void onOpenWebPageResponseReceived(TVController tv, String url, ERROR result) {
	}

	@Override
	public void onRefreshWebPageResponseReceived(TVController tv, ERROR result) {
	}

	@Override
	public void onSetBrowserModeResponseReceived(TVController tv, BrowserMode browserMode, ERROR result) {
	}

	@Override
	public void onStopWebPageResponseReceived(TVController tv, ERROR result) {
	}
}