package dimitri.suls.allshare.activities.helpers;

import android.app.Activity;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import dimitri.suls.allshare.R;

public class TabManager {
	private Activity activity = null;
	private TabHost tabHostMain = null;

	public TabManager(Activity activity) {
		this.activity = activity;

		initializeTabHostMain();
		initializeTabHostTV();
		initializeTabHostAVPlayer();
	}

	public void resetCurrentTab() {
		tabHostMain.setCurrentTab(0);
	}

	public void setTabEnabledStatus(int tabIndex, boolean isEnabled) {
		View childTab = tabHostMain.getTabWidget().getChildAt(tabIndex);

		childTab.setEnabled(isEnabled);
	}

	private TabHost initializeTabHostMain() {
		tabHostMain = (TabHost) activity.findViewById(R.id.tabHostMain);

		tabHostMain.setup();

		TabSpec tabSpecDevices = tabHostMain.newTabSpec("devices");
		tabSpecDevices.setContent(R.id.tabDevices);
		tabSpecDevices.setIndicator("Devices");
		tabHostMain.addTab(tabSpecDevices);

		TabSpec tabSpecTV = tabHostMain.newTabSpec("tv");
		tabSpecTV.setContent(R.id.tabTV);
		tabSpecTV.setIndicator("TV");
		tabHostMain.addTab(tabSpecTV);

		TabSpec tabSpecAVPlayer = tabHostMain.newTabSpec("avPlayer");
		tabSpecAVPlayer.setContent(R.id.tabAVPlayer);
		tabSpecAVPlayer.setIndicator("AV-player");
		tabHostMain.addTab(tabSpecAVPlayer);

		TabSpec tabSpecImageViewer = tabHostMain.newTabSpec("imageViewer");
		tabSpecImageViewer.setContent(R.id.tabImageViewer);
		tabSpecImageViewer.setIndicator("Image viewer");
		tabHostMain.addTab(tabSpecImageViewer);

		return tabHostMain;
	}

	private void initializeTabHostTV() {
		TabHost tabHostTV = (TabHost) activity.findViewById(R.id.tabHostTV);

		tabHostTV.setup();

		TabSpec tabSpecTVRemote = tabHostTV.newTabSpec("tvRemote");
		tabSpecTVRemote.setContent(R.id.tabTVRemote);
		tabSpecTVRemote.setIndicator("Remote");
		tabHostTV.addTab(tabSpecTVRemote);

		TabSpec tabSpecTVBrowser = tabHostTV.newTabSpec("tvBrowser");
		tabSpecTVBrowser.setContent(R.id.tabTVBrowser);
		tabSpecTVBrowser.setIndicator("Browser");
		tabHostTV.addTab(tabSpecTVBrowser);

		TabSpec tabSpecTVTouch = tabHostTV.newTabSpec("tvTouch");
		tabSpecTVTouch.setContent(R.id.tabTVTouch);
		tabSpecTVTouch.setIndicator("Touch");
		tabHostTV.addTab(tabSpecTVTouch);
	}

	private void initializeTabHostAVPlayer() {
		TabHost tabHostAVPlayer = (TabHost) activity.findViewById(R.id.tabHostAVPlayer);

		tabHostAVPlayer.setup();

		TabSpec tabSpecAVPlayerSongs = tabHostAVPlayer.newTabSpec("avPlayerSongs");
		tabSpecAVPlayerSongs.setContent(R.id.tabAVPlayerSongs);
		tabSpecAVPlayerSongs.setIndicator("Songs");
		tabHostAVPlayer.addTab(tabSpecAVPlayerSongs);

		TabSpec tabSpecAVPlayerVideos = tabHostAVPlayer.newTabSpec("avPlayerVideos");
		tabSpecAVPlayerVideos.setContent(R.id.tabAVPlayerVideos);
		tabSpecAVPlayerVideos.setIndicator("Videos");
		tabHostAVPlayer.addTab(tabSpecAVPlayerVideos);

		TabSpec tabSpecAVPlayerControls = tabHostAVPlayer.newTabSpec("avPlayerControls");
		tabSpecAVPlayerControls.setContent(R.id.tabAVPlayerControls);
		tabSpecAVPlayerControls.setIndicator("Controls");
		tabHostAVPlayer.addTab(tabSpecAVPlayerControls);
	}
}