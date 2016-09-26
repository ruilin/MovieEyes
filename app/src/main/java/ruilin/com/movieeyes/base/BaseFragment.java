package ruilin.com.movieeyes.base;

import com.umeng.analytics.MobclickAgent;
import android.support.v4.app.Fragment;

/**
 * Created by Ruilin on 2016/9/26.
 */

public class BaseFragment extends Fragment {
    private final String TAG = getClass().getSimpleName();

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
    }
}
