package ruilin.com.movieeyes.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ruilin.com.movieeyes.R;
import ruilin.com.movieeyes.fragment.RecordFagment.OnRecordItemListener;

/**
 * specified {@link OnRecordItemListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class RecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<String> mValues;
    private final OnRecordItemListener mListener;
    private Activity mActivity;

    public RecordAdapter(Activity context, List<String> items, OnRecordItemListener listener) {
        mActivity = context;
        mValues = items;
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
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
        final ContentViewHolder holder = (ContentViewHolder) viewHolder;
        holder.mItem = mValues.get(position);
        holder.mKeyTv.setText(mValues.get(position));
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ContentViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mKeyTv;
        public String mItem;

        public ContentViewHolder(View view) {
            super(view);
            mView = view;
            mKeyTv = (TextView) view.findViewById(R.id.tv_key);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mKeyTv.getText() + "'";
        }
    }

}
