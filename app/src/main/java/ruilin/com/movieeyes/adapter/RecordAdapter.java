package ruilin.com.movieeyes.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ruilin.com.movieeyes.Helper.SearchKeyHelper;
import ruilin.com.movieeyes.R;
import ruilin.com.movieeyes.db.bean.SearchRecordDb;
import ruilin.com.movieeyes.fragment.RecordFagment.OnRecordItemListener;

/**
 * specified {@link OnRecordItemListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class RecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final OnRecordItemListener mListener;
    private Activity mActivity;
    private List<SearchRecordDb> mValues;

    public RecordAdapter(Activity context, OnRecordItemListener listener) {
        mActivity = context;
        mValues = SearchKeyHelper.getInstance().getList();
        mListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_record, parent, false);
        return new ContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        final ContentViewHolder holder = (ContentViewHolder) viewHolder;
        holder.mItem = mValues.get(position);
        holder.mKeyTv.setText(mValues.get(position).getKey());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onRecordItemClicked(holder.mItem, position);
            }
        });
        holder.mDeleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchKeyHelper.getInstance().delete(holder.mItem);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }


    public void deleteAllItem() {
        SearchKeyHelper.getInstance().clear();
        mValues = SearchKeyHelper.getInstance().getList();
        notifyDataSetChanged();
    }

    public class ContentViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mKeyTv;
        public final ImageView mSelectView;
        public final ImageView mDeleteView;
        public SearchRecordDb mItem;

        public ContentViewHolder(View view) {
            super(view);
            mView = view;
            mKeyTv = (TextView) view.findViewById(R.id.tv_key);
            mSelectView = (ImageView) view.findViewById(R.id.iv_select);
            mDeleteView = (ImageView) view.findViewById(R.id.iv_delete);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mKeyTv.getText() + "'";
        }
    }

}
