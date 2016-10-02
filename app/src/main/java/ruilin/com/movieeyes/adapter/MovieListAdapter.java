package ruilin.com.movieeyes.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ruilin.com.movieeyes.Helper.SearchResultHelper;
import ruilin.com.movieeyes.Helper.ToastHelper;
import ruilin.com.movieeyes.R;
import ruilin.com.movieeyes.activity.MainActivity;
import ruilin.com.movieeyes.db.bean.SearchResultDb;
import ruilin.com.movieeyes.fragment.MovieListFragment.OnListFragmentInteractionListener;
import ruilin.com.movieeyes.util.DateUtil;

/**
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MovieListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM_TYPE_CONTENT = 0;
    private static final int ITEM_TYPE_FOOTER = 1;
    private final ArrayList<SearchResultDb> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Activity mActivity;
    private String mKey;
    private int mPage;

    public MovieListAdapter(Activity context, ArrayList<SearchResultDb> items, OnListFragmentInteractionListener listener) {
        mActivity = context;
        mValues = items;
        mListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return position == mValues.size() ? ITEM_TYPE_FOOTER : ITEM_TYPE_CONTENT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_CONTENT) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_movielist, parent, false);
            return new ContentViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_foot_movielist, parent, false);
            return new FooterViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == ITEM_TYPE_CONTENT) {
            final ContentViewHolder holder = (ContentViewHolder)viewHolder;
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).getTag());
            holder.mContentView.setText(String.format(mActivity.getString(R.string.movie_item_author), mValues.get(position).getAuthor()));
            holder.mDateView.setText(DateUtil.determineDateFormat(mValues.get(position).getDate()));
            holder.mFavoriteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /* 添加收藏 */
                    SearchResultHelper.getInstance().add(holder.mItem);
                    ToastHelper.show(v.getContext(), v.getContext().getResources().getString(R.string.toast_add_favorite));
                }
            });
            holder.mContentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.doUrl(mActivity, holder.mItem.getAuthorUrl());
                }
            });
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.onListFragmentInteraction(holder.mItem);
                    }
                    MainActivity.doUrl(mActivity, holder.mItem.getUrl());
                }
            });
        } else {
            final FooterViewHolder holder = (FooterViewHolder)viewHolder;
            holder.mTvName.setText(mActivity.getString(R.string.movie_next_page));
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        mListener.onNextPage(mKey, mPage);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size() + 1;
    }

    public void setInfo(String key, int page) {
        this.mKey = key;
        this.mPage = page;
    }

    public class ContentViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final TextView mDateView;
        public final ImageView mFavoriteView;
        public SearchResultDb mItem;

        public ContentViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.tv_name);
            mContentView = (TextView) view.findViewById(R.id.tv_author);
            mDateView = (TextView) view.findViewById(R.id.tv_date);
            mFavoriteView = (ImageView) view.findViewById(R.id.iv_favorite);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTvName;

        public FooterViewHolder(View view) {
            super(view);
            mView = view;
            mTvName = (TextView) view.findViewById(R.id.tv_name);
        }
    }
}
