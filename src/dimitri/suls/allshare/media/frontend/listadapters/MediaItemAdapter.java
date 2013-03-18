package dimitri.suls.allshare.media.frontend.listadapters;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sec.android.allshare.Item;

public class MediaItemAdapter extends ArrayAdapter<Item> {
	public MediaItemAdapter(Context context, List<Item> objects) {
		super(context, 0, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Item mediaItem = getItem(position);
		TextView textView = (TextView) convertView;

		if (textView == null) {
			textView = new TextView(getContext());
		}

		textView.setText(mediaItem.getTitle());
		textView.setTextSize(20);
		textView.setPadding(10, 10, 10, 10);

		return textView;
	}
}