package dimitri.suls.allshare.gui.activities;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.sec.android.allshare.Device;
import com.sec.android.allshare.Device.DeviceType;
import com.sec.android.allshare.Item;
import com.sec.android.allshare.control.TVController;
import com.sec.android.allshare.control.TVController.RemoteKey;
import com.sec.android.allshare.media.AVPlayer;
import com.sec.android.allshare.media.ContentInfo;

import dimitri.suls.allshare.R;
import dimitri.suls.allshare.control.tv.TVTouchListener;
import dimitri.suls.allshare.gui.helpers.DeviceListItemClickListener;
import dimitri.suls.allshare.gui.helpers.FrontendDeviceObserver;
import dimitri.suls.allshare.gui.helpers.TabManager;
import dimitri.suls.allshare.gui.listadapters.DeviceAdapter;
import dimitri.suls.allshare.gui.listadapters.MediaItemAdapter;
import dimitri.suls.allshare.managers.device.DeviceCommand;
import dimitri.suls.allshare.managers.device.DeviceManager;
import dimitri.suls.allshare.managers.serviceprovider.ServiceProviderManager;
import dimitri.suls.allshare.managers.serviceprovider.ServiceProviderObserver;
import dimitri.suls.allshare.media.MediaFinder;

public class Main extends Activity {
	private ServiceProviderManager serviceProviderManager = null;
	private DeviceManager tvControllerDeviceManager = null;
	private DeviceManager avPlayerDeviceManager = null;
	private DeviceManager imageViewerDeviceManager = null;
	private MediaFinder mediaFinder = null;
	private TabManager tabManager = null;
	private ListView listViewTVControllers = null;
	private ListView listViewAVPlayers = null;
	private ListView listViewImageViewers = null;
	private TextView textViewSelectedDevice = null;
	private EditText editTextBrowseTerm = null;
	private ListView listViewSongs = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mediaFinder = new MediaFinder(this);
		tabManager = new TabManager(this);

		initializeViews();

		try {
			serviceProviderManager = new ServiceProviderManager(this);
			serviceProviderManager.addObserver(new ServiceProviderObserver() {
				@Override
				public void createdServiceProvider() {
					// TODO: Add textView for other selected device-types
					// besides TV-controller.
					tvControllerDeviceManager = new DeviceManager(DeviceType.DEVICE_TV_CONTROLLER, serviceProviderManager);
					tvControllerDeviceManager.addObserver(new FrontendDeviceObserver(tabManager, listViewTVControllers, textViewSelectedDevice,
							"TV-controller"));

					avPlayerDeviceManager = new DeviceManager(DeviceType.DEVICE_AVPLAYER, serviceProviderManager);
					avPlayerDeviceManager.addObserver(new FrontendDeviceObserver(tabManager, listViewAVPlayers, textViewSelectedDevice, "AV-player"));

					imageViewerDeviceManager = new DeviceManager(DeviceType.DEVICE_IMAGEVIEWER, serviceProviderManager);
					imageViewerDeviceManager.addObserver(new FrontendDeviceObserver(tabManager, listViewImageViewers, textViewSelectedDevice,
							"image-viewer"));

					initializeTVTouchListener();

					refreshAllDeviceLists();
				}
			});
		} catch (Exception exception) {
			final Activity activityMain = this;
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();

			alertDialog.setCancelable(false);
			alertDialog.setMessage("Error occured: \r\n" + exception.getMessage());
			alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					activityMain.finish();
				}
			});

			alertDialog.show();
		}
	}

	@Override
	protected void onDestroy() {
		serviceProviderManager.close();

		super.onDestroy();
	}

	private void initializeViews() {
		initializeEachDeviceListViews();
		initializeTextViewSelectedDevice();
		initializeEditTextBrowseTerm();
		initializeListViewSongs();
	}

	private void initializeEachDeviceListViews() {
		listViewTVControllers = (ListView) findViewById(R.id.listViewTVControllers);
		listViewTVControllers.setOnItemClickListener(new DeviceListItemClickListener(tvControllerDeviceManager));

		listViewAVPlayers = (ListView) findViewById(R.id.listViewAVPlayers);
		listViewAVPlayers.setOnItemClickListener(new DeviceListItemClickListener(avPlayerDeviceManager));

		listViewImageViewers = (ListView) findViewById(R.id.listViewImageViewers);
		listViewImageViewers.setOnItemClickListener(new DeviceListItemClickListener(imageViewerDeviceManager));
	}

	private void initializeTextViewSelectedDevice() {
		textViewSelectedDevice = (TextView) findViewById(R.id.textViewSelectedDevice);
	}

	private void initializeEditTextBrowseTerm() {
		editTextBrowseTerm = (EditText) findViewById(R.id.editTextBrowseTerm);
	}

	private void initializeListViewSongs() {
		listViewSongs = (ListView) findViewById(R.id.listViewSongs);

		listViewSongs.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				final Item selectedSong = (Item) adapterView.getItemAtPosition(position);
				final ContentInfo contentInfo = new ContentInfo.Builder().build();

				avPlayerDeviceManager.execute(new DeviceCommand() {
					@Override
					public void execute(Device selectedDevice) {
						AVPlayer avPlayer = (AVPlayer) selectedDevice;

						avPlayer.play(selectedSong, contentInfo);
					}
				});
			}
		});

		refreshSongList();
	}

	// TODO: Add slider to adjust speed of motion-control.
	private void initializeTVTouchListener() {
		View tabTouch = findViewById(R.id.tabTouch);

		tabTouch.setOnTouchListener(new TVTouchListener(tvControllerDeviceManager));
	}

	private void refreshAllDeviceLists() {
		refreshDeviceList(tvControllerDeviceManager, listViewTVControllers);
		refreshDeviceList(avPlayerDeviceManager, listViewAVPlayers);
		refreshDeviceList(imageViewerDeviceManager, listViewImageViewers);
	}

	private void refreshDeviceList(DeviceManager deviceManager, ListView listView) {
		List<Device> devices = deviceManager.getDevices();
		DeviceAdapter deviceAdapter = new DeviceAdapter(this, devices);

		listView.setAdapter(deviceAdapter);
		deviceManager.setSelectedDevice(null);
	}

	private void refreshSongList() {
		List<Item> songs = mediaFinder.findAllSongsOnExternalStorageOfDevice();
		MediaItemAdapter mediaItemAdapter = new MediaItemAdapter(this, songs);

		listViewSongs.setAdapter(mediaItemAdapter);
		// TODO: Implement observers for mediaItems, just like with devices.
		// mediaFinder.setSelectedSong(null);
	}

	// TODO: Add button to disconnect from the selected device

	public void refreshDeviceListEvent(View view) {
		refreshAllDeviceLists();
	}

	private void sendRemoteKey(final RemoteKey remoteKey) {
		tvControllerDeviceManager.execute(new DeviceCommand() {
			@Override
			public void execute(Device selectedDevice) {
				TVController tvController = (TVController) selectedDevice;

				tvController.sendRemoteKey(remoteKey);
			}
		});
	}

	public void volumeUpKeyEvent(View view) {
		sendRemoteKey(RemoteKey.KEY_VOLUP);
	}

	public void volumeDownKeyEvent(View view) {
		sendRemoteKey(RemoteKey.KEY_VOLDOWN);
	}

	public void muteKeyEvent(View view) {
		sendRemoteKey(RemoteKey.KEY_MUTE);
	}

	public void channelUpKeyEvent(View view) {
		sendRemoteKey(RemoteKey.KEY_CHUP);
	}

	public void channelDownKeyEvent(View view) {
		sendRemoteKey(RemoteKey.KEY_CHDOWN);
	}

	public void preChKeyEvent(View view) {
		sendRemoteKey(RemoteKey.KEY_PRECH);
	}

	public void chListKeyEvent(View view) {
		sendRemoteKey(RemoteKey.KEY_CH_LIST);
	}

	public void arrowUpKeyEvent(View view) {
		sendRemoteKey(RemoteKey.KEY_UP);
	}

	public void arrowDownKeyEvent(View view) {
		sendRemoteKey(RemoteKey.KEY_DOWN);
	}

	public void arrowLeftKeyEvent(View view) {
		sendRemoteKey(RemoteKey.KEY_LEFT);
	}

	public void arrowRightKeyEvent(View view) {
		sendRemoteKey(RemoteKey.KEY_RIGHT);
	}

	public void enterKeyEvent(View view) {
		sendRemoteKey(RemoteKey.KEY_ENTER);
	}

	public void toolsKeyEvent(View view) {
		sendRemoteKey(RemoteKey.KEY_TOOLS);
	}

	public void infoKeyEvent(View view) {
		sendRemoteKey(RemoteKey.KEY_INFO);
	}

	public void returnKeyEvent(View view) {
		sendRemoteKey(RemoteKey.KEY_RETURN);
	}

	public void exitKeyEvent(View view) {
		sendRemoteKey(RemoteKey.KEY_EXIT);
	}

	public void smartHubKeyEvent(View view) {
		sendRemoteKey(RemoteKey.KEY_CONTENTS);
	}

	public void menuKeyEvent(View view) {
		sendRemoteKey(RemoteKey.KEY_MENU);
	}

	public void sourceKeyEvent(View view) {
		sendRemoteKey(RemoteKey.KEY_SOURCE);
	}

	public void powerOffKeyEvent(View view) {
		sendRemoteKey(RemoteKey.KEY_POWEROFF);
	}

	public void sendTouchClickEvent(View view) {
		tvControllerDeviceManager.execute(new DeviceCommand() {
			@Override
			public void execute(Device selectedDevice) {
				TVController tvController = (TVController) selectedDevice;

				tvController.sendTouchClick();
			}
		});
	}

	// TODO: Add more buttons for numeric keys/dash, play-keys, color-keys, ..

	public void openWebPageEvent(View view) {
		tvControllerDeviceManager.execute(new DeviceCommand() {
			@Override
			public void execute(Device selectedDevice) {
				TVController tvController = (TVController) selectedDevice;
				String URL = editTextBrowseTerm.getText().toString();

				tvController.openWebPage(URL);
			}
		});
	}

	public void searchInternetEvent(View view) {
		tvControllerDeviceManager.execute(new DeviceCommand() {
			@Override
			public void execute(Device selectedDevice) {
				TVController tvController = (TVController) selectedDevice;
				String searchTerm = editTextBrowseTerm.getText().toString();

				tvController.openWebPage("http://www.google.com/search?q=" + searchTerm);
			}
		});
	}

	public void sendKeyboardStringEvent(View view) {
		tvControllerDeviceManager.execute(new DeviceCommand() {
			@Override
			public void execute(Device selectedDevice) {
				TVController tvController = (TVController) selectedDevice;
				String text = editTextBrowseTerm.getText().toString();

				tvController.sendKeyboardString(text);
				tvController.sendKeyboardEnd();
			}
		});
	}

	public void closeWebPageEvent(View view) {
		tvControllerDeviceManager.execute(new DeviceCommand() {
			@Override
			public void execute(Device selectedDevice) {
				TVController tvController = (TVController) selectedDevice;

				tvController.closeWebPage();
			}
		});
	}

	public void goHomePageEvent(View view) {
		tvControllerDeviceManager.execute(new DeviceCommand() {
			@Override
			public void execute(Device selectedDevice) {
				TVController tvController = (TVController) selectedDevice;

				tvController.goHomePage();
			}
		});
	}

	public void pauseEvent(View view) {
		// mediaFinder.pause();
	}

	public void resumeEvent(View view) {
		// mediaFinder.resume();
	}

	public void stopEvent(View view) {
		// mediaFinder.stop();
	}
}