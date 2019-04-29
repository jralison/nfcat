package com.jralison.nfcat.tagContent;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jralison.nfcat.R;

public class TagContentPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private Bundle mBundleArgs;

    public TagContentPagerAdapter(Context context, FragmentManager fm, Bundle args) {
        super(fm);
        this.mContext = context;
        this.mBundleArgs = args;
    }

    @Override
    public Fragment getItem(int i) {
        final Fragment fragment;
        switch (i) {
            case 0:
                fragment = new ContentFragment();
                break;
            case 1:
                fragment = new TechListFragment();
                break;
            default:
                fragment = null;
        }

        if (fragment != null)
            fragment.setArguments(mBundleArgs);
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getString(R.string.tab_content_info);
            case 1:
                return mContext.getString(R.string.tab_tech_list);
            default:
                return null;
        }
    }
}
