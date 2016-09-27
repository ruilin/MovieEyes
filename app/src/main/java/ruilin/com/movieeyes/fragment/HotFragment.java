package ruilin.com.movieeyes.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import ruilin.com.movieeyes.Helper.JsoupHelper;
import ruilin.com.movieeyes.Helper.SearchKeyHelper;
import ruilin.com.movieeyes.Helper.ToastHelper;
import ruilin.com.movieeyes.R;
import ruilin.com.movieeyes.activity.MainActivity;
import ruilin.com.movieeyes.base.BaseFragment;
import ruilin.com.movieeyes.modle.HotKey;
import ruilin.com.movieeyes.widget.TagView.Tag;
import ruilin.com.movieeyes.widget.TagView.TagListView;
import ruilin.com.movieeyes.widget.TagView.TagView;

public class HotFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private OnHotKeyClickedListener mListener;
    private SwipeRefreshLayout mSwipeLayout;
    private TagListView mTagListView;
    private TextView mTitleTv;
    private ArrayList<HotKey> mHotkeyList;
    private boolean isFirst = true;

    public HotFragment() {
    }

    public static HotFragment newInstance() {
        HotFragment fragment = new HotFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHotkeyList = new ArrayList<>();
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        isFirst = true;
        View contentView = inflater.inflate(R.layout.fragment_hot, container, false);
        mSwipeLayout = (SwipeRefreshLayout) contentView.findViewById(R.id.rl_update);
        mTagListView = (TagListView) contentView.findViewById(R.id.tagview);
        final SwipeRefreshLayout srLayout = (SwipeRefreshLayout) contentView.findViewById(R.id.rl_update);
        final ScrollView scrollView = (ScrollView) contentView.findViewById(R.id.sv_content);
        mTitleTv = (TextView) contentView.findViewById(R.id.tv_title);
        mTitleTv.setText(String.format(mTitleTv.getContext().getString(R.string.hot_search_key), mHotkeyList.size()));

        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        new LoadHotKeyTask().execute();

        mTagListView.setOnTagClickListener(new TagListView.OnTagClickListener() {
            @Override
            public void onTagClick(TagView tagView, Tag tag) {
                HotKey key = (HotKey)tag;
                if (mListener != null) {
                    mListener.onHotKeyClicked(key);
                }
            }
        });

        AdView mAdView = (AdView) contentView.findViewById(R.id.adView);
//        mAdView.setAdUnitId(getString(R.string.banner_ad_unit_id));
//        mAdView.setAdSize(AdSize.WIDE_SKYSCRAPER);
        final View progressBar = contentView.findViewById(R.id.pb_ad);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);
                progressBar.setVisibility(View.GONE);
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        /* 解决scrollview 与 SwipeRefreshLayout 滚动冲突 */
        if (scrollView != null) {
            scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                    if (srLayout != null) {
                        srLayout.setEnabled(scrollView.getScrollY() == 0);
                    }
                }
            });
        }
        return contentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHotKeyClickedListener) {
            mListener = (OnHotKeyClickedListener)context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onRefresh() {
        new LoadHotKeyTask().execute();
    }

    private void showProgress(boolean show) {
        mSwipeLayout.setRefreshing(show);
    }

    public class LoadHotKeyTask extends AsyncTask<Void, Void, Integer> {

        LoadHotKeyTask() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected Integer doInBackground(Void... params) {
            return JsoupHelper.parseHtmlForHotKey(mHotkeyList);
        }

        @Override
        protected void onPostExecute(final Integer resultCode) {
            showProgress(false);
            switch (resultCode) {
                case JsoupHelper.RESULT_CODE_SUCCESS:
                    mTagListView.setTags(mHotkeyList);
                    mTitleTv.setText(String.format(mTitleTv.getContext().getString(R.string.hot_search_key), mHotkeyList.size()));
                    if (!isFirst) {
                        ToastHelper.show(getActivity(), getResources().getString(R.string.hot_search_load_finish));
                    } else {
                        isFirst = false;
                    }
                    break;
                case JsoupHelper.RESULT_CODE_TIMEOUT:
                    ToastHelper.show(getActivity(), getResources().getString(R.string.main_net_timeout_tips));
                    break;
                case JsoupHelper.RESULT_CODE_ERROR:
                default:
                    ToastHelper.show(getActivity(), getResources().getString(R.string.main_net_error_tips));
                    break;
            }
        }

        @Override
        protected void onCancelled() {
            showProgress(false);
        }
    }

    public interface OnHotKeyClickedListener {
        public void onHotKeyClicked(HotKey key);
    }
}
