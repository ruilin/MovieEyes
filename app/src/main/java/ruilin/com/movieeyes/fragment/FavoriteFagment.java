package ruilin.com.movieeyes.fragment;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ruilin.com.movieeyes.R;
import ruilin.com.movieeyes.adapter.FavoriteAdapter;
import ruilin.com.movieeyes.adapter.RecordAdapter;
import ruilin.com.movieeyes.base.BaseFragment;
import ruilin.com.movieeyes.db.bean.SearchResultDb;

/**
 */
public class FavoriteFagment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFavoriteItemListener mListener;

    private FavoriteAdapter mAdapter;

    public FavoriteFagment() {
    }

    public static FavoriteFagment newInstance() {
        FavoriteFagment fragment = new FavoriteFagment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_favorite, container, false);
        RecyclerView recyclerView = (RecyclerView)contentView.findViewById(R.id.list);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.list_item_space);
        recyclerView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new FavoriteAdapter(getActivity(), mListener);
        recyclerView.setAdapter(mAdapter);
        return contentView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFavoriteItemListener) {
            mListener = (OnFavoriteItemListener) context;
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


    public interface OnFavoriteItemListener {
        // TODO: Update argument type and name
        void onFavoriteItemClicked(SearchResultDb item);
    }
}
