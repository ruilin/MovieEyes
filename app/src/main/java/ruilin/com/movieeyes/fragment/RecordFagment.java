package ruilin.com.movieeyes.fragment;

import android.content.Context;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ruilin.com.movieeyes.Helper.SearchKeyHelper;
import ruilin.com.movieeyes.R;
import ruilin.com.movieeyes.adapter.RecordAdapter;
import ruilin.com.movieeyes.base.BaseFragment;
import ruilin.com.movieeyes.modle.SearchKey;

import static anetwork.channel.http.NetworkSdkSetting.context;

/**
 */
public class RecordFagment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnRecordItemListener mListener;

    private RecordAdapter mAdapter;
    private List<String> mItems;

    public RecordFagment() {
    }

    public static RecordFagment newInstance() {
        RecordFagment fragment = new RecordFagment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_record, container, false);
        RecyclerView recyclerView = (RecyclerView)contentView.findViewById(R.id.list);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.list_item_space);
        recyclerView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mItems = SearchKeyHelper.getInstance().getList();
        mAdapter = new RecordAdapter(getActivity(), mItems, mListener);
        recyclerView.setAdapter(mAdapter);
        return contentView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onRecordItemClicked(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRecordItemListener) {
            mListener = (OnRecordItemListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRecordItemListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            if (parent.getChildPosition(view) != 0)
                outRect.top = space;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnRecordItemListener {
        // TODO: Update argument type and name
        void onRecordItemClicked(Uri uri);
    }
}
