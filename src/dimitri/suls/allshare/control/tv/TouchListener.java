package dimitri.suls.allshare.control.tv;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.sec.android.allshare.Device;
import com.sec.android.allshare.control.TVController;

import dimitri.suls.allshare.managers.device.DeviceCommand;
import dimitri.suls.allshare.managers.device.DeviceManager;

public class TouchListener implements OnTouchListener {
	private DeviceManager deviceManager = null;
	private int previousPositionX = 0;
	private int previousPositionY = 0;

	public TouchListener(DeviceManager deviceManager) {
		this.deviceManager = deviceManager;
	}

	@Override
	public boolean onTouch(View view, MotionEvent motionEvent) {
		boolean hasConsumedEvent = false;

		int currentPositionX = (int) motionEvent.getX();
		int currentPositionY = (int) motionEvent.getY();

		switch (motionEvent.getAction()) {
		case MotionEvent.ACTION_DOWN:
			deviceManager.execute(new DeviceCommand() {
				@Override
				public void execute(Device selectedDevice) {
					TVController tvController = (TVController) selectedDevice;

					tvController.sendTouchDown();
				}

			});

			break;
		case MotionEvent.ACTION_UP:
			deviceManager.execute(new DeviceCommand() {
				@Override
				public void execute(Device selectedDevice) {
					TVController tvController = (TVController) selectedDevice;

					tvController.sendTouchUp();
				}
			});

			break;
		case MotionEvent.ACTION_MOVE:
			hasConsumedEvent = true;

			final int differenceBetweenCurrentAndPreviousPositionX = (currentPositionX - previousPositionX) * 2;
			final int differenceBetweenCurrentAndPreviousPositionY = (currentPositionY - previousPositionY) * 2;

			deviceManager.execute(new DeviceCommand() {
				@Override
				public void execute(Device selectedDevice) {
					TVController tvController = (TVController) selectedDevice;

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