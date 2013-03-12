package dimitri.suls.allshare.adapters;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import dimitri.suls.allshare.media.Song;

public class SongAdapter extends ArrayAdapter<Song> {
	public SongAdapter(Context context, List<Song> objects) {
		super(context, 0, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Song song = getItem(position);
		TextView textView = (TextView) convertView;

		if (textView == null) {
			textView = new TextView(getContext());
		}

		// TODO: Show song titel
		textView.setText(song);
		textView.setTextSize(20);
		textView.setPadding(10, 10, 10, 10);

		return textView;
	}
}