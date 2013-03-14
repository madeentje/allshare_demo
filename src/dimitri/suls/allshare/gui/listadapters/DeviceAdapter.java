package dimitri.suls.allshare.gui.listadapters;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sec.android.allshare.Device;

public class DeviceAdapter<T extends Device> extends ArrayAdapter<T> {
	public DeviceAdapter(Context context, List<T> objects) {
		super(context, 0, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		T device = getItem(position);
		TextView textView = (TextView) convertView;

		if (textView == null) {
			textView = new TextView(getContext());
		}

		textView.setText(device.getName());
		textView.setTextSize(20);
		textView.setPadding(10, 10, 10, 10);

		return textView;
	}
}