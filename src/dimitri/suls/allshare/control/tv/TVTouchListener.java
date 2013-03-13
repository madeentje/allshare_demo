package dimitri.suls.allshare.control.tv;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.sec.android.allshare.control.TVController;

import dimitri.suls.allshare.managers.device.DeviceCommand;
import dimitri.suls.allshare.managers.device.DeviceManager;

public class TVTouchListener implements OnTouchListener {
	private DeviceManager<TVController> tvDeviceManager = null;
	private int previousPositionX = 0;
	private int previousPositionY = 0;

	public TVTouchListener(DeviceManager<TVController> tvDeviceManager) {
		this.tvDeviceManager = tvDeviceManager;
	}

	@Override
	public boolean onTouch(View view, MotionEvent motionEvent) {
		boolean hasConsumedEvent = false;

		int currentPositionX = (int) motionEvent.getX();
		int currentPositionY = (int) motionEvent.getY();

		switch (motionEvent.getAction()) {
		case MotionEvent.ACTION_DOWN:
			tvDeviceManager.execute(new DeviceCommand<TVController>() {
				@Override
				public void execute(TVController tvController) {
					tvController.sendTouchDown();
				}

			});

			break;
		case MotionEvent.ACTION_UP:
			tvDeviceManager.execute(new DeviceCommand<TVController>() {
				@Override
				public void execute(TVController tvController) {
					tvController.sendTouchUp();
				}
			});

			break;
		case MotionEvent.ACTION_MOVE:
			hasConsumedEvent = true;

			final int differenceBetweenCurrentAndPreviousPositionX = (currentPositionX - previousPositionX) * 2;
			final int differenceBetweenCurrentAndPreviousPositionY = (currentPositionY - previousPositionY) * 2;

			tvDeviceManager.execute(new DeviceCommand<TVController>() {
				@Override
				public void execute(TVController tvController) {
					tvController.sendTouchMove(differenceBetweenCurrentAndPreviousPositionX, differenceBetweenCurrentAndPreviousPositionY);
				}
			});

			break;
		}

		previousPositionX = currentPositionX;
		previousPositionY = currentPositionY;

		return hasConsumedEvent;
	}
}