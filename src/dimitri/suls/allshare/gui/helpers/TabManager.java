package dimitri.suls.allshare.gui.helpers;

import android.app.Activity;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import dimitri.suls.allshare.R;

public class TabManager {
	private Activity activity = null;
	private TabHost tabHostMain = null;
	private View tabRemote = null;
	private View tabMedia = null;

	public TabManager(Activity activity) {
		this.activity = activity;

		initializeTabHostMain();
		initializeTabHostRemote();
	}

	public void resetCurrentTab() {
		tabHostMain.setCurrentTab(0);
	}

	public void setEnabledTabRemote(boolean isEnabled) {
		tabRemote.setEnabled(isEnabled);
	}

	public void setEnabledTabMedia(boolean isEnabled) {
		tabMedia.setEnabled(isEnabled);
	}

	private TabHost initializeTabHostMain() {
		tabHostMain = (TabHost) activity.findViewById(R.id.tabHostMain);

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

		return tabHostMain;
	}

	private void initializeTabHostRemote() {
		TabHost tabHostRemote = (TabHost) activity.findViewById(R.id.tabHostRemote);

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
}