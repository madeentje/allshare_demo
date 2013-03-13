package dimitri.suls.allshare.media;

import java.util.ArrayList;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.sec.android.allshare.Device;
import com.sec.android.allshare.Device.DeviceType;
import com.sec.android.allshare.DeviceFinder;
import com.sec.android.allshare.Item;
import com.sec.android.allshare.Item.LocalContentBuilder;
import com.sec.android.allshare.media.AVPlayer;
import com.sec.android.allshare.media.ContentInfo;

import dimitri.suls.allshare.managers.serviceprovider.ServiceProviderManager;

public class MediaFinder {
	private Context context;
	private ServiceProviderManager serviceProviderManager;
	private AVPlayer avPlayer;

	public MediaFinder(Context context, ServiceProviderManager serviceProviderManager) {
		this.context = context;
		this.serviceProviderManager = serviceProviderManager;

		DeviceFinder deviceFinder = serviceProviderManager.getDeviceFinder();
		ArrayList<Device> avPlayerList = deviceFinder.getDevices(DeviceType.DEVICE_AVPLAYER);
		ArrayList<Device> imageViewerList = deviceFinder.getDevices(DeviceType.DEVICE_IMAGEVIEWER);

		this.avPlayer = (AVPlayer) avPlayerList.get(0);
	}

	public void playSong() {
		Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		String[] projection = { "*" };
		String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

		CursorLoader cursorLoader = new CursorLoader(context, uri, projection, selection, null, null);
		Cursor cursor = cursorLoader.loadInBackground();

		String filePath = null;
		String mimeType = null;

		if (cursor.moveToFirst()) {
			filePath = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
			mimeType = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE));
		}

		LocalContentBuilder localContentBuilder = new LocalContentBuilder(filePath, mimeType);
		localContentBuilder.setTitle("Dimitri's song");
		Item item = localContentBuilder.build();

		ContentInfo contentInfo = new ContentInfo.Builder().build();

		avPlayer.play(item, contentInfo);
	}

	public void playVideo() {
		Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
		String[] projection = { "*" };

		CursorLoader cursorLoader = new CursorLoader(context, uri, projection, null, null, null);
		Cursor cursor = cursorLoader.loadInBackground();

		String filePath = null;
		String mimeType = null;

		if (cursor.moveToFirst()) {
			filePath = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
			mimeType = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE));
		}

		LocalContentBuilder localContentBuilder = new LocalContentBuilder(filePath, mimeType);
		localContentBuilder.setTitle("Dimitri's song");
		Item item = localContentBuilder.build();

		ContentInfo contentInfo = new ContentInfo.Builder().build();

		avPlayer.play(item, contentInfo);
	}

	public void pause() {
		avPlayer.pause();
	}

	public void resume() {
		avPlayer.resume();
	}

	public void stop() {
		avPlayer.stop();
	}
}