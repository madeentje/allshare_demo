package dimitri.suls.allshare.avplayer.frontend.managers;

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

import dimitri.suls.allshare.avplayer.frontend.listadapters.MediaItemAdapter;
import dimitri.suls.allshare.avplayer.model.managers.MediaManager;
import dimitri.suls.allshare.avplayer.model.managers.MediaManager.MediaType;
import dimitri.suls.allshare.device.model.manager.DeviceCommand;
import dimitri.suls.allshare.device.model.manager.DeviceManager;

public class MediaFrontendManager {
	private Context context;
	private ListView listViewMedia;
	private DeviceManager avPlayerDeviceManager;
	private MediaManager mediaManager;
	private MediaType mediaType;

	private class MediaListItemClickListener implements OnItemClickListener {
		private Item selectedSong;
		private ContentInfo contentInfo;

		@Override
		public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
			this.selectedSong = (Item) adapterView.getItemAtPosition(position);
			this.contentInfo = new ContentInfo.Builder().build();

			avPlayerDeviceManager.execute(new DeviceCommand() {
				@Override
				public void execute(Device selectedDevice) {
					AVPlayer avPlayer = (AVPlayer) selectedDevice;

					avPlayer.play(selectedSong, contentInfo);
				}
			});
		}
	}

	public MediaFrontendManager(Context context, ListView listViewMedia, DeviceManager avPlayerDeviceManager, MediaManager mediaManager,
			MediaType mediaType) {
		this.context = context;
		this.listViewMedia = listViewMedia;
		this.avPlayerDeviceManager = avPlayerDeviceManager;
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