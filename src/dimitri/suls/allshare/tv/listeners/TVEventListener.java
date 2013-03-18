package dimitri.suls.allshare.tv.listeners;

import com.sec.android.allshare.ERROR;
import com.sec.android.allshare.control.TVController;
import com.sec.android.allshare.control.TVController.IEventListener;

// TODO: Implement listener-events?
public class TVEventListener implements IEventListener {
	@Override
	public void onDisconnected(TVController tv, ERROR result) {
	}

	@Override
	public void onStringChanged(TVController tv, String text, ERROR result) {
	}
}