package dimitri.suls.allshare.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

public class InitializeManager {
	private Main mainActivity = null;
	private AlertDialog alertDialog = null;
	private BroadcastReceiver broadcastReceiver = null;

	public InitializeManager(Main mainActivity) {
		this.mainActivity = mainActivity;

		this.broadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				NetworkInfo wifiNetworkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);

				if (!wifiNetworkInfo.isConnected()) {
					showErrorDialog("Wi-Fi is not connected!\r\nPlease connect to a Wi-Fi network.");
				} else {
					closeErrorDialog();

					tryToInitialize();
				}
			}
		};

		IntentFilter intentFilter = new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);

		this.mainActivity.registerReceiver(broadcastReceiver, intentFilter);
	}

	private void tryToInitialize() {
		try {
			mainActivity.initialize();
		} catch (Exception exception) {
			String message = exception.getMessage();

			showErrorDialog(message);
		}
	}

	private void showErrorDialog(String message) {
		closeErrorDialog();

		alertDialog = new AlertDialog.Builder(mainActivity).create();

		alertDialog.setCancelable(false);
		alertDialog.setMessage("Error occured: \r\n" + message);

		alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Exit", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mainActivity.finish();
			}
		});

		alertDialog.show();
	}

	private void closeErrorDialog() {
		if (alertDialog != null) {
			alertDialog.cancel();

			alertDialog = null;
		}
	}
}