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

	private void createNewTab(TabHost tabHost, int contentId, String tag, String title) {
		TabSpec tabSpec = tabHost.newTabSpec(tag);

		tabSpec.setContent(contentId);
		tabSpec.setIndicator(title);

		tabHost.addTab(tabSpec);
	}

	private void initializeTabHostMain() {
		tabHostMain = (TabHost) activity.findViewById(R.id.tabHostMain);

		tabHostMain.setup();

		createNewTab(tabHostMain, R.id.tabDevices, "devices", "Devices");
		createNewTab(tabHostMain, R.id.tabTV, "tv", "TV");
		createNewTab(tabHostMain, R.id.tabAVPlayer, "avPlayer", "AV-player");
		createNewTab(tabHostMain, R.id.tabImageViewer, "imageViewer", "Image viewer");
	}

	private void initializeTabHostTV() {
		TabHost tabHostTV = (TabHost) activity.findViewById(R.id.tabHostTV);

		tabHostTV.setup();

		createNewTab(tabHostTV, R.id.tabTVRemote, "tvRemote", "Remote");
		createNewTab(tabHostTV, R.id.tabTVBrowser, "tvBrowser", "Browser");
		createNewTab(tabHostTV, R.id.tabTVTouch, "tvTouch", "Touch");
	}

	private void initializeTabHostAVPlayer() {
		TabHost tabHostAVPlayer = (TabHost) activity.findViewById(R.id.tabHostAVPlayer);

		tabHostAVPlayer.setup();

		createNewTab(tabHostAVPlayer, R.id.tabAVPlayerSongs, "avPlayerSongs", "Songs");
		createNewTab(tabHostAVPlayer, R.id.tabAVPlayerVideos, "avPlayerVideos", "Videos");
		createNewTab(tabHostAVPlayer, R.id.tabAVPlayerControls, "avPlayerControls", "Controls");
	}
}