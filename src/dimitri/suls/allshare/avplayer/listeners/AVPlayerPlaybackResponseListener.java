package dimitri.suls.allshare.avplayer.listeners;

import com.sec.android.allshare.ERROR;
import com.sec.android.allshare.Item;
import com.sec.android.allshare.media.AVPlayer.AVPlayerState;
import com.sec.android.allshare.media.AVPlayer.IAVPlayerPlaybackResponseListener;
import com.sec.android.allshare.media.ContentInfo;
import com.sec.android.allshare.media.MediaInfo;

//TODO: Implement listener-events?
public class AVPlayerPlaybackResponseListener implements IAVPlayerPlaybackResponseListener {
	@Override
	public void onGetMediaInfoResponseReceived(MediaInfo mediaInfo, ERROR error) {

	}

	@Override
	public void onGetPlayPositionResponseReceived(long position, ERROR error) {

	}

	@Override
	public void onGetStateResponseReceived(AVPlayerState avPlayerState, ERROR error) {

	}

	@Override
	public void onPauseResponseReceived(ERROR error) {

	}

	@Override
	public void onPlayResponseReceived(Item item, ContentInfo contentInfo, ERROR error) {

	}

	@Override
	public void onResumeResponseReceived(ERROR error) {

	}

	@Override
	public void onSeekResponseReceived(long requestedPosition, ERROR error) {

	}

	@Override
	public void onStopResponseReceived(ERROR error) {

	}
}