package ruilin.com.movieeyes.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ruilin.com.movieeyes.Helper.SearchResultHelper;
import ruilin.com.movieeyes.R;
import ruilin.com.movieeyes.activity.MainActivity;
import ruilin.com.movieeyes.db.bean.SearchResultDb;
import ruilin.com.movieeyes.fragment.FavoriteFagment.OnFavoriteItemListener;
import ruilin.com.movieeyes.util.DateUtil;

/**
 */
public class FavoriteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final OnFavoriteItemListener mListener;
    private Activity mActivity;
    private List<SearchResultDb> mValues;

    public FavoriteAdapter(Activity context, OnFavoriteItemListener listener) {
        mActivity = context;
        mValues = SearchResultHelper.getInstance().getList();
        mListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favorite, parent, false);
        return new ContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        final ContentViewHolder holder = (ContentViewHolder) viewHolder;
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getTag());
        holder.mContentView.setText(String.format(mActivity.getString(R.string.movie_item_author), mValues.get(position).getAuthor()));
        holder.mDateView.setText(DateUtil.determineDateFormat(mValues.get(position).getDate()));
        holder.mFavoriteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* 删除收藏 */
                SearchResultHelper.getInstance().delete(holder.mItem);
                notifyDataSetChanged();
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
                    mListener.onFavoriteItemClicked(holder.mItem);
                }
                MainActivity.doUrl(mActivity, holder.mItem.getUrl());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
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

}
