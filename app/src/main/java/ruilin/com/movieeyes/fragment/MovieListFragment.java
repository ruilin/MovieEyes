package ruilin.com.movieeyes.fragment;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ruilin.com.movieeyes.R;
import ruilin.com.movieeyes.activity.MainActivity;
import ruilin.com.movieeyes.adapter.MovieListAdapter;
import ruilin.com.movieeyes.base.BaseFragment;
import ruilin.com.movieeyes.modle.MovieUrl;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class MovieListFragment extends BaseFragment {
    // TODO: Customize parameter argument names
    private static final String ARG_KEY = "ARG_KEY";
    private static final String ARG_PAGE = "ARG_PAGE";
    // TODO: Customize parameters
    private OnListFragmentInteractionListener mListener;
    private MovieListAdapter mAdapter;
    private ArrayList<MovieUrl> mItems;
    private View mContentView;
    private View mEmptyView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MovieListFragment newInstance() {
        MovieListFragment fragment = new MovieListFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_KEY, key);
//        args.putInt(ARG_PAGE, page);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mKey = getArguments().getString(ARG_KEY);
//            mPage = getArguments().getInt(ARG_PAGE);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_movielist, container, false);
        mEmptyView = contentView.findViewById(R.id.tv_empty);
        View view = contentView.findViewById(R.id.list);
        mContentView = view;
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.list_item_space);
            recyclerView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            mAdapter = new MovieListAdapter(getActivity(), mItems, mListener);
            recyclerView.setAdapter(mAdapter);
        }
        contentView.findViewById(R.id.bt_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onMovielistClose();
                }
            }
        });
        return contentView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            MainActivity main = (MainActivity) context;
            mItems = main.getMovieList();
        }
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void update(String key, int page) {
        mAdapter.setInfo(key, page);
        mAdapter.notifyDataSetChanged();
        showEmpty(mItems.size() == 1);
    }

    private void showEmpty(boolean isEmpty) {
        if (isEmpty) {
            mContentView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mContentView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        }
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

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(MovieUrl item);
        void onMovielistClose();
        void onNextPage(String key, int currentPage);
    }
}
