package dimitri.suls.allshare.tv;

import com.sec.android.allshare.control.TVController;

public interface TVCommand {
	void execute(TVController tvController);
}