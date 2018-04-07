package com.inter.ui.base;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.View;

import com.inter.face.LiveView;
import com.inter.ui.base.BaseActivity;
import com.inter.ui.util.ThemeManager;
import com.inter.util.LiveViewManager;
import com.inter.util.QKPreference;

/**
 * Created by ZCYL on 2018/4/6.
 */
public class BaseFragment extends Fragment {
    protected BaseActivity mContext;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = (BaseActivity) activity;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LiveViewManager.registerView(QKPreference.BACKGROUND, this, new LiveView() {
            @Override
            public void refresh(String key) {
                if (getView() != null) {
                    getView().setBackgroundColor(ThemeManager.getBackgroundColor());
                }
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

}
