package ruilin.com.movieeyes.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ruilin.com.movieeyes.R;
import ruilin.com.movieeyes.widget.TagView.Tag;
import ruilin.com.movieeyes.widget.TagView.TagListView;

public class HotFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private final String[] titles = { "安全必备", "音乐", "父母学", "上班族必备",
            "360手机卫士", "QQ","输入法", "微信", "最美应用", "AndevUI", "蘑菇街" };
    private final List<Tag> mTags = new ArrayList<Tag>();
    TagListView mTagListView;


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
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void setUpData() {
        for (int i = 0; i < 10; i++) {
            Tag tag = new Tag();
            tag.setId(i);
            tag.setChecked(true);
            tag.setTitle(titles[i]);
            mTags.add(tag);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_hot, container, false);
        mTagListView = (TagListView) contentView.findViewById(R.id.tagview);
        setUpData();
        mTagListView.setTags(mTags);
        return contentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
