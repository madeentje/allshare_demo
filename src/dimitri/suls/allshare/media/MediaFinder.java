package dimitri.suls.allshare.media;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.sec.android.allshare.Item;
import com.sec.android.allshare.Item.LocalContentBuilder;

public class MediaFinder {
	private Context context;

	public MediaFinder(Context context) {
		this.context = context;
	}

	public List<Item> findAllSongsOnExternalStorageOfDevice() {
		Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		String[] projection = { "*" };
		String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

		CursorLoader cursorLoader = new CursorLoader(context, uri, projection, selection, null, null);
		Cursor cursor = cursorLoader.loadInBackground();

		return getMediaItemsFromCursorToList(cursor);
	}

	public List<Item> findAllVideosOnExternalStorageOfDevice() {
		Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
		String[] projection = { "*" };

		CursorLoader cursorLoader = new CursorLoader(context, uri, projection, null, null, null);
		Cursor cursor = cursorLoader.loadInBackground();

		return getMediaItemsFromCursorToList(cursor);
	}

	public List<Item> getMediaItemsFromCursorToList(Cursor cursor) {
		List<Item> songs = new ArrayList<Item>();

		String filePath = null;
		String mimeType = null;
		String title = null;
		LocalContentBuilder localContentBuilder = null;
		Item item = null;

		if (cursor.moveToNext()) {
			filePath = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
			mimeType = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE));
			title = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.TITLE));

			localContentBuilder = new LocalContentBuilder(filePath, mimeType);
			localContentBuilder.setTitle(title);

			item = localContentBuilder.build();

			songs.add(item);
		}

		cursor.close();

		return songs;
	}
}