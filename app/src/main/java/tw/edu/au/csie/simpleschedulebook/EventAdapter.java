package tw.edu.au.csie.simpleschedulebook;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Wei-Tsung on 2016/10/23.
 */

public class EventAdapter extends ArrayAdapter<Event> {

    public EventAdapter(Context context, int textViewResourceId, ArrayList<Event> objects) {
        super(context, textViewResourceId, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.item, null);
        }

        Event p = getItem(position);

        if (p != null) {
            ImageView iv_type = (ImageView) v.findViewById(R.id.iv_type);
            TextView tv_event = (TextView) v.findViewById(R.id.tv_event);
            TextView tv_datetime = (TextView) v.findViewById(R.id.tv_datetime);

            int event_type = p.getType();

            if(event_type == 0) { // Private
                iv_type.setImageResource(R.drawable.private_event);
            } else if(event_type == 1) {  // Public
                iv_type.setImageResource(R.drawable.public_event);
            }

            if (tv_event != null) {
                tv_event.setText(p.getEvent());
            }

            if (tv_datetime != null) {
                tv_datetime.setText(p.getDatetime());
            }
        }

        return v;
    }
}
