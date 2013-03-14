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
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.sec.android.allshare.Device;
import com.sec.android.allshare.Device.DeviceType;
import com.sec.android.allshare.Item;
import com.sec.android.allshare.control.TVController;
import com.sec.android.allshare.control.TVController.RemoteKey;
import com.sec.android.allshare.media.AVPlayer;
import com.sec.android.allshare.media.ContentInfo;
import com.sec.android.allshare.media.ImageViewer;

import dimitri.suls.allshare.R;
import dimitri.suls.allshare.control.tv.TVTouchListener;
import dimitri.suls.allshare.gui.listadapters.DeviceAdapter;
import dimitri.suls.allshare.gui.listadapters.MediaItemAdapter;
import dimitri.suls.allshare.managers.device.DeviceCommand;
import dimitri.suls.allshare.managers.device.DeviceManager;
import dimitri.suls.allshare.managers.device.DeviceObserver;
import dimitri.suls.allshare.managers.serviceprovider.ServiceProviderManager;
import dimitri.suls.allshare.managers.serviceprovider.ServiceProviderObserver;
import dimitri.suls.allshare.media.MediaFinder;

public class Main extends Activity {
	private ServiceProviderManager serviceProviderManager = null;
	private DeviceManager<TVController> tvControllerDeviceManager = null;
	private DeviceManager<AVPlayer> avPlayerDeviceManager = null;
	private DeviceManager<ImageViewer> imageViewerDeviceManager = null;
	private MediaFinder mediaFinder = null;
	private TabHost tabHostMain = null;
	private View tabRemote = null;
	private View tabMedia = null;
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

		initializeViews();

		try {
			serviceProviderManager = new ServiceProviderManager(this);
			serviceProviderManager.addObserver(new ServiceProviderObserver() {
				@Override
				public void createdServiceProvider() {
					tvControllerDeviceManager = new DeviceManager<TVController>(DeviceType.DEVICE_TV_CONTROLLER, serviceProviderManager);
					tvControllerDeviceManager.addObserver(new DeviceObserver<TVController>() {
						@Override
						public void changedSelectedDevice(TVController selectedDevice) {
							if (selectedDevice == null) {
								textViewSelectedDevice.setText("No TV-controller selected.");

								tabHostMain.setCurrentTab(0);

								tabRemote.setEnabled(false);
							} else {
								textViewSelectedDevice.setText("Selected TV-controller: " + selectedDevice.getName());

								tabRemote.setEnabled(true);
							}
						}

						@Override
						public void addedDevice(TVController device) {
							DeviceAdapter arrayAdapter = (DeviceAdapter) listViewTVControllers.getAdapter();

							arrayAdapter.add(device);
						}

						@Override
						public void removedDevice(TVController device) {
							DeviceAdapter arrayAdapter = (DeviceAdapter) listViewTVControllers.getAdapter();

							arrayAdapter.remove(device);
						}
					});

					avPlayerDeviceManager = new DeviceManager<AVPlayer>(DeviceType.DEVICE_AVPLAYER, serviceProviderManager);
					avPlayerDeviceManager.addObserver(new DeviceObserver<AVPlayer>() {
						@Override
						public void changedSelectedDevice(AVPlayer selectedDevice) {
							if (selectedDevice == null) {
								// TODO: Add textView for AV-players
								textViewSelectedDevice.setText("No AV-player selected.");

								tabHostMain.setCurrentTab(0);

								tabMedia.setEnabled(false);
							} else {
								textViewSelectedDevice.setText("Selected AV-player: " + selectedDevice.getName());

								tabMedia.setEnabled(true);
							}
						}

						@Override
						public void addedDevice(AVPlayer device) {
							DeviceAdapter arrayAdapter = (DeviceAdapter) listViewAVPlayers.getAdapter();

							arrayAdapter.add(device);
						}

						@Override
						public void removedDevice(AVPlayer device) {
							DeviceAdapter arrayAdapter = (DeviceAdapter) listViewAVPlayers.getAdapter();

							arrayAdapter.remove(device);
						}
					});

					imageViewerDeviceManager = new DeviceManager<ImageViewer>(DeviceType.DEVICE_IMAGEVIEWER, serviceProviderManager);
					imageViewerDeviceManager.addObserver(new DeviceObserver<ImageViewer>() {
						@Override
						public void changedSelectedDevice(ImageViewer selectedDevice) {
							if (selectedDevice == null) {
								// TODO: Add textView for image-viewers
								textViewSelectedDevice.setText("No image-viewer selected.");

								tabHostMain.setCurrentTab(0);

								// TODO: Finish up image-viewer functionality
							} else {
								textViewSelectedDevice.setText("Selected image-viewer: " + selectedDevice.getName());

								// TODO: Finish up image-viewer functionality
							}
						}

						@Override
						public void addedDevice(ImageViewer device) {
							DeviceAdapter arrayAdapter = (DeviceAdapter) listViewImageViewers.getAdapter();

							arrayAdapter.add(device);
						}

						@Override
						public void removedDevice(ImageViewer device) {
							DeviceAdapter arrayAdapter = (DeviceAdapter) listViewImageViewers.getAdapter();

							arrayAdapter.remove(device);
						}
					});

					initializeTabTouch();

					refreshDeviceList();
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
		initializeTabTouch();
		initializeTabHostMain();
		initializeTabHostRemote();
		initializeEachDeviceListViews();
		initializeTextViewSelectedDevice();
		initializeEditTextBrowseTerm();
		initializeListViewSongs();
	}

	private void initializeTabHostMain() {
		tabHostMain = (TabHost) findViewById(R.id.tabHostMain);

		tabHostMain.setup();

		TabSpec tabSpecDevices = tabHostMain.newTabSpec("devices");
		tabSpecDevices.setContent(R.id.tabDevices);
		tabSpecDevices.setIndicator("Devices");
		tabHostMain.addTab(tabSpecDevices);

		TabSpec tabSpecRemote = tabHostMain.newTabSpec("remote");
		tabSpecRemote.setContent(R.id.tabRemote);
		tabSpecRemote.setIndicator("Remote");
		tabHostMain.addTab(tabSpecRemote);
		tabRemote = tabHostMain.getTabWidget().getChildTabViewAt(1);
		tabRemote.setEnabled(false);

		TabSpec tabSpecMedia = tabHostMain.newTabSpec("media");
		tabSpecMedia.setContent(R.id.tabMedia);
		tabSpecMedia.setIndicator("Media");
		tabHostMain.addTab(tabSpecMedia);
		tabMedia = tabHostMain.getTabWidget().getChildTabViewAt(2);
		tabMedia.setEnabled(false);
	}

	private void initializeTabHostRemote() {
		TabHost tabHostRemote = (TabHost) findViewById(R.id.tabHostRemote);

		tabHostRemote.setup();

		TabSpec tabSpecControls = tabHostRemote.newTabSpec("controls");
		tabSpecControls.setContent(R.id.tabControls);
		tabSpecControls.setIndicator("Controls");
		tabHostRemote.addTab(tabSpecControls);

		TabSpec tabSpecBrowser = tabHostRemote.newTabSpec("browser");
		tabSpecBrowser.setContent(R.id.tabBrowser);
		tabSpecBrowser.setIndicator("Browser");
		tabHostRemote.addTab(tabSpecBrowser);

		TabSpec tabSpecTouch = tabHostRemote.newTabSpec("touch");
		tabSpecTouch.setContent(R.id.tabTouch);
		tabSpecTouch.setIndicator("Touch");
		tabHostRemote.addTab(tabSpecTouch);
	}

	// TODO: Add slider to adjust speed of motion-control.

	private void initializeTabTouch() {
		View tabTouch = findViewById(R.id.tabTouch);

		tabTouch.setOnTouchListener(new TVTouchListener(tvControllerDeviceManager));
	}

	private void initializeEachDeviceListViews() {
		listViewTVControllers = (ListView) findViewById(R.id.listViewTVControllers);
		listViewTVControllers.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				TVController selectedDevice = (TVController) adapterView.getItemAtPosition(position);

				tvControllerDeviceManager.setSelectedDevice(selectedDevice);
			}
		});

		listViewAVPlayers = (ListView) findViewById(R.id.listViewAVPlayers);
		listViewAVPlayers.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				AVPlayer selectedDevice = (AVPlayer) adapterView.getItemAtPosition(position);

				avPlayerDeviceManager.setSelectedDevice(selectedDevice);
			}
		});

		listViewImageViewers = (ListView) findViewById(R.id.listViewImageViewers);
		listViewImageViewers.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				ImageViewer selectedDevice = (ImageViewer) adapterView.getItemAtPosition(position);

				imageViewerDeviceManager.setSelectedDevice(selectedDevice);
			}
		});
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
				// TODO: Finish this.
				final Item selectedSong = (Item) adapterView.getItemAtPosition(position);
				final ContentInfo contentInfo = new ContentInfo.Builder().build();

				avPlayerDeviceManager.execute(new DeviceCommand<AVPlayer>() {
					@Override
					public void execute(AVPlayer selectedDevice) {
						selectedDevice.play(selectedSong, contentInfo);
					}
				});
			}
		});

		refreshSongList();
	}

	private void refreshDeviceList() {
		List<Device> tvControllers = tvControllerDeviceManager.getDevices();
		List<Device> avPlayers = avPlayerDeviceManager.getDevices();
		List<Device> imageViewers = imageViewerDeviceManager.getDevices();

		DeviceAdapter tvControllerAdapter = new DeviceAdapter(this, tvControllers);
		DeviceAdapter avPlayerAdapter = new DeviceAdapter(this, avPlayers);
		DeviceAdapter imageViewerAdapter = new DeviceAdapter(this, imageViewers);

		listViewTVControllers.setAdapter(tvControllerAdapter);
		listViewAVPlayers.setAdapter(avPlayerAdapter);
		listViewImageViewers.setAdapter(imageViewerAdapter);

		tvControllerDeviceManager.setSelectedDevice(null);
		avPlayerDeviceManager.setSelectedDevice(null);
		imageViewerDeviceManager.setSelectedDevice(null);
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
		refreshDeviceList();
	}

	private void sendRemoteKey(final RemoteKey remoteKey) {
		tvControllerDeviceManager.execute(new DeviceCommand<TVController>() {
			@Override
			public void execute(TVController tvController) {
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
		tvControllerDeviceManager.execute(new DeviceCommand<TVController>() {
			@Override
			public void execute(TVController tvController) {
				tvController.sendTouchClick();
			}
		});
	}

	// TODO: Add more buttons for numeric keys/dash, play-keys, color-keys, ..

	public void openWebPageEvent(View view) {
		tvControllerDeviceManager.execute(new DeviceCommand<TVController>() {
			@Override
			public void execute(TVController tvController) {
				String URL = editTextBrowseTerm.getText().toString();

				tvController.openWebPage(URL);
			}
		});
	}

	public void searchInternetEvent(View view) {
		tvControllerDeviceManager.execute(new DeviceCommand<TVController>() {
			@Override
			public void execute(TVController tvController) {
				String searchTerm = editTextBrowseTerm.getText().toString();

				tvController.openWebPage("http://www.google.com/search?q=" + searchTerm);
			}
		});
	}

	public void sendKeyboardStringEvent(View view) {
		tvControllerDeviceManager.execute(new DeviceCommand<TVController>() {
			@Override
			public void execute(TVController tvController) {
				String text = editTextBrowseTerm.getText().toString();

				tvController.sendKeyboardString(text);
				tvController.sendKeyboardEnd();
			}
		});
	}

	public void closeWebPageEvent(View view) {
		tvControllerDeviceManager.execute(new DeviceCommand<TVController>() {
			@Override
			public void execute(TVController tvController) {
				tvController.closeWebPage();
			}
		});
	}

	public void goHomePageEvent(View view) {
		tvControllerDeviceManager.execute(new DeviceCommand<TVController>() {
			@Override
			public void execute(TVController tvController) {
				tvController.goHomePage();
			}
		});
	}

	public void playSongEvent(View view) {
		// avPlayer.pause();
		// avPlayer.resume();
		// avPlayer.stop();
	}

	public void playVideoEvent(View view) {
		// mediaFinder.playVideo();
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