package org.csix.android.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.csix.android.fragments.GroupFragment;
import org.csix.android.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupAdapterViewHolder> {

    private final String LOG_TAG = GroupAdapter.class.getSimpleName();

    private Cursor mCursor;
    private final Context mContext;
    private final View mEmptyView;
    private final GroupAdapterOnClickHandler mClickHandler;

    public class GroupAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // @Bind(R.id.ivGroup)
        // ImageView groupLogo;
        @Bind(R.id.tvName)
        public TextView groupName;

        public GroupAdapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            mClickHandler.onClick(mCursor.getLong(GroupFragment.COL_GROUP_ID), this);
        }
    }

    public interface GroupAdapterOnClickHandler {
        void onClick(Long groupId, GroupAdapterViewHolder vh);
    }

    public GroupAdapter(Context context, GroupAdapterOnClickHandler clickHandler, View emptyView) {
        mContext = context;
        mClickHandler = clickHandler;
        mEmptyView = emptyView;
    }

    public GroupAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewTyped) {
        if (viewGroup instanceof RecyclerView) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_group, viewGroup, false);
            view.setFocusable(true);
            return new GroupAdapterViewHolder(view);
        } else {
            throw new RuntimeException("Not bound to RecyclerView");
        }
    }

    @Override
    public void onBindViewHolder(GroupAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        holder.groupName.setText("" + mCursor.getString(GroupFragment.COL_GROUP_NAME));
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
