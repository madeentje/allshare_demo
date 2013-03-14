package dimitri.suls.allshare.control.tv;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.sec.android.allshare.Device;
import com.sec.android.allshare.control.TVController;

import dimitri.suls.allshare.managers.device.DeviceCommand;
import dimitri.suls.allshare.managers.device.DeviceManager;

public class TVTouchListener implements OnTouchListener {
	private DeviceManager tvControllerDeviceManager = null;
	private int previousPositionX = 0;
	private int previousPositionY = 0;

	public TVTouchListener(DeviceManager tvControllerDeviceManager) {
		this.tvControllerDeviceManager = tvControllerDeviceManager;
	}

	@Override
	public boolean onTouch(View view, MotionEvent motionEvent) {
		boolean hasConsumedEvent = false;

		int currentPositionX = (int) motionEvent.getX();
		int currentPositionY = (int) motionEvent.getY();

		switch (motionEvent.getAction()) {
		case MotionEvent.ACTION_DOWN:
			tvControllerDeviceManager.execute(new DeviceCommand() {
				@Override
				public void execute(Device selectedDevice) {
					TVController tvController = (TVController) selectedDevice;

					tvController.sendTouchDown();
				}

			});

			break;
		case MotionEvent.ACTION_UP:
			tvControllerDeviceManager.execute(new DeviceCommand() {
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

			tvControllerDeviceManager.execute(new DeviceCommand() {
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