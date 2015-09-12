package org.csix.android.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.csix.android.fragments.AboutFragment;
import org.csix.android.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AboutAdapter extends RecyclerView.Adapter<AboutAdapter.AboutAdapterViewHolder> {

    private final String LOG_TAG = AboutAdapter.class.getSimpleName();

    private Cursor mCursor;
    private final Context mContext;
    private final View mEmptyView;

    public class AboutAdapterViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tvAboutTitle)
        TextView aboutTitle;
        @Bind(R.id.tvAboutContent)
        TextView aboutContent;

        public AboutAdapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }

    public AboutAdapter(Context context, View emptyView) {
        mContext = context;
        mEmptyView = emptyView;
    }

    public AboutAdapterViewHolder onCreateViewHolder(ViewGroup viewAbout, int viewTyped) {
        if (viewAbout instanceof RecyclerView) {
            View view = LayoutInflater.from(viewAbout.getContext()).inflate(R.layout.card_about, viewAbout, false);
            view.setFocusable(true);
            return new AboutAdapterViewHolder(view);
        } else {
            throw new RuntimeException("Not bound to RecyclerView");
        }
    }

    @Override
    public void onBindViewHolder(AboutAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        holder.aboutTitle.setText("" + mCursor.getString(AboutFragment.COL_ABOUT_TITLE));
        holder.aboutContent.setText("" + mCursor.getString(AboutFragment.COL_ABOUT_DESC));
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
        mEmptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }
}
