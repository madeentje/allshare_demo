package dimitri.suls.allshare.media.frontend.managers;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.sec.android.allshare.Device;
import com.sec.android.allshare.Item;
import com.sec.android.allshare.media.AVPlayer;
import com.sec.android.allshare.media.ContentInfo;
import com.sec.android.allshare.media.ImageViewer;

import dimitri.suls.allshare.device.model.manager.DeviceCommand;
import dimitri.suls.allshare.device.model.manager.DeviceManager;
import dimitri.suls.allshare.media.frontend.listadapters.MediaItemAdapter;
import dimitri.suls.allshare.media.model.managers.MediaManager;
import dimitri.suls.allshare.media.model.managers.MediaManager.MediaType;

public class MediaFrontendManager {
	private Context context;
	private ListView listViewMedia;
	private DeviceManager deviceManager;
	private MediaManager mediaManager;
	private MediaType mediaType;

	private class MediaListItemClickListener implements OnItemClickListener {
		private Item selectedItem;
		private ContentInfo contentInfo;

		@Override
		public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
			this.selectedItem = (Item) adapterView.getItemAtPosition(position);
			this.contentInfo = new ContentInfo.Builder().build();

			deviceManager.execute(new DeviceCommand() {
				@Override
				public void execute(Device selectedDevice) {
					if (mediaType == MediaType.IMAGES) {
						ImageViewer imageViewer = (ImageViewer) selectedDevice;

						imageViewer.show(selectedItem, contentInfo);
					} else {
						AVPlayer avPlayer = (AVPlayer) selectedDevice;

						avPlayer.play(selectedItem, contentInfo);
					}
				}
			});
		}
	}

	public MediaFrontendManager(Context context, ListView listViewMedia, DeviceManager deviceManager, MediaManager mediaManager, MediaType mediaType) {
		this.context = context;
		this.listViewMedia = listViewMedia;
		this.deviceManager = deviceManager;
		this.mediaManager = mediaManager;
		this.mediaType = mediaType;

		listViewMedia.setOnItemClickListener(new MediaListItemClickListener());

		refreshMediaList();
	}

	public void refreshMediaList() {
		List<Item> mediaItems = mediaManager.findAllMediaItems(mediaType);
		MediaItemAdapter mediaItemAdapter = new MediaItemAdapter(context, mediaItems);

		listViewMedia.setAdapter(mediaItemAdapter);
		// TODO: Implement observers for mediaItems, just like with devices.
		// mediaManager.setSelectedSong(null);
	}
}