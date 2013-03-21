package dimitri.suls.allshare.avplayer.listeners;

import com.sec.android.allshare.ERROR;
import com.sec.android.allshare.media.AVPlayer.IAVPlayerVolumeResponseListener;

//TODO: Implement listener-events?
public class AVPlayerVolumeResponseListener implements IAVPlayerVolumeResponseListener {
	@Override
	public void onGetMuteResponseReceived(boolean isMuted, ERROR error) {
	}

	@Override
	public void onGetVolumeResponseReceived(int level, ERROR error) {
	}

	@Override
	public void onSetMuteResponseReceived(boolean isMuted, ERROR error) {
	}

	@Override
	public void onSetVolumeResponseReceived(int level, ERROR error) {
	}
}