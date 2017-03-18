package edu.uw.tessa_and_sam.melodious;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.*;
import android.widget.*;

/**
 * Created by Tessa on 3/17/17.
 */

public class GridAdapter extends BaseAdapter {
    private Context context;
    private SharedPreferences prefs;
    private TextView[] notes;
    private int screenWidth;

    public GridAdapter(Context context, int width) {
        this.context = context;
        this.screenWidth = width;
        this.prefs = context.getSharedPreferences(context.getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        String noteSequence = this.prefs.getString(context.getString(R.string.note_sequence_key),
                context.getString(R.string.default_note_sequence));
        this.notes = new TextView[Integer.parseInt(noteSequence) + 1];
    }

    public int getCount() {
        return this.notes.length;
    }

    public Object getItem(int position) {
        return this.notes[position];
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        int boxSize = 280;
        if (this.notes.length < 3) {
            boxSize = (this.screenWidth / (this.notes.length * 2) + (75 * this.notes.length));
        }

        TextView tv;
        if (convertView == null) {
            tv = new TextView(context);
            tv.setLayoutParams(new GridView.LayoutParams(boxSize, boxSize));
        }
        else {
            tv = (TextView) convertView;
        }

        tv.setTextColor(Color.parseColor("#FFFFFF"));
        tv.setBackgroundResource(R.drawable.border);
//        tv.setText(notes[position]);
        tv.setText("A3");
        tv.setTextSize(boxSize / 6);
        tv.setGravity(Gravity.CENTER);
        return tv;
    }
}
