package org.csix.android;

import android.support.v7.widget.RecyclerView;

public interface Callback {
    void onItemSelected(long id, RecyclerView.ViewHolder viewHolder);
}
