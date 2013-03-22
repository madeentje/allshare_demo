package dimitri.suls.allshare.media.frontend.managers;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.sec.android.allshare.Item;
import com.sec.android.allshare.media.ContentInfo;

import dimitri.suls.allshare.device.model.manager.DeviceCommand;
import dimitri.suls.allshare.device.model.manager.DeviceManager;
import dimitri.suls.allshare.media.frontend.listadapters.MediaItemAdapter;
import dimitri.suls.allshare.media.model.managers.MediaManager;
import dimitri.suls.allshare.media.model.managers.MediaManager.MediaType;

public class MediaFrontendManager {
	private Context context;
	private ListView listViewMedia;
	private MediaManager mediaManager;
	private MediaType mediaType;

	public static abstract class MediaListItemClickListener implements OnItemClickListener {
		private Item selectedItem;
		private ContentInfo contentInfo;

		public Item getSelectedItem() {
			return selectedItem;
		}

		public ContentInfo getContentInfo() {
			return contentInfo;
		}

		@Override
		public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
			this.selectedItem = (Item) adapterView.getItemAtPosition(position);
			this.contentInfo = new ContentInfo.Builder().build();

			DeviceManager deviceManager = getDeviceManager();
			DeviceCommand deviceCommand = getDeviceCommand();

			deviceManager.execute(deviceCommand);
		}

		public abstract DeviceManager getDeviceManager();

		public abstract DeviceCommand getDeviceCommand();
	}

	public MediaFrontendManager(Context context, ListView listViewMedia, MediaListItemClickListener mediaListItemClickListener,
			MediaManager mediaManager, MediaType mediaType) {
		this.context = context;
		this.listViewMedia = listViewMedia;
		this.mediaManager = mediaManager;
		this.mediaType = mediaType;

		listViewMedia.setOnItemClickListener(mediaListItemClickListener);

		refreshMediaList();
	}

	public void refreshMediaList() {
		List<Item> mediaItems = mediaManager.findAllMediaItems(mediaType);
		MediaItemAdapter mediaItemAdapter = new MediaItemAdapter(context, mediaItems);

		listViewMedia.setAdapter(mediaItemAdapter);
		// TODO: Implement observers for mediaItems, just like with devices.
		// mediaManager.setSelectedMediaItem(null);
	}
}