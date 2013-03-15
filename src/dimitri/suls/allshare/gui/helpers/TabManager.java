package dimitri.suls.allshare.gui.helpers;

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
		initializeTabHostAV();
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

		TabSpec tabSpecAV = tabHostMain.newTabSpec("av");
		tabSpecAV.setContent(R.id.tabAV);
		tabSpecAV.setIndicator("AV");
		tabHostMain.addTab(tabSpecAV);

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

	private void initializeTabHostAV() {
		TabHost tabHostAV = (TabHost) activity.findViewById(R.id.tabHostAV);

		tabHostAV.setup();

		TabSpec tabSpecAVSongs = tabHostAV.newTabSpec("avSongs");
		tabSpecAVSongs.setContent(R.id.tabAVSongs);
		tabSpecAVSongs.setIndicator("Songs");
		tabHostAV.addTab(tabSpecAVSongs);

		TabSpec tabSpecAVVideos = tabHostAV.newTabSpec("avVideos");
		tabSpecAVVideos.setContent(R.id.tabAVVideos);
		tabSpecAVVideos.setIndicator("Videos");
		tabHostAV.addTab(tabSpecAVVideos);

		TabSpec tabSpecAVControls = tabHostAV.newTabSpec("avControls");
		tabSpecAVControls.setContent(R.id.tabAVControls);
		tabSpecAVControls.setIndicator("Controls");
		tabHostAV.addTab(tabSpecAVControls);
	}
}