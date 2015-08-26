package org.csix.android;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.csix.android.data.CSixContract;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GroupListAdapter extends CursorAdapter {

    private final String LOG_TAG = GroupListAdapter.class.getSimpleName();

    public static class ViewHolder {

        @Bind(R.id.ivGroup)
        ImageView groupLogo;
        @Bind(R.id.tvName)
        TextView groupName;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public GroupListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.group_card, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        viewHolder.groupName.setText("" + cursor.getString(cursor.getColumnIndex(CSixContract.GroupEntry.COLUMN_NAME)));
    }
}
