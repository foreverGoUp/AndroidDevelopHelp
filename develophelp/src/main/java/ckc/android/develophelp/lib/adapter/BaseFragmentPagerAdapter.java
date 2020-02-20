package ckc.android.develophelp.lib.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;


/**
 * 片段超出不可见片段范围后会摧毁界面，但仍保持片段实例。适合少片段的页面（底部标签页面）的显示。
 */
public class BaseFragmentPagerAdapter<T extends Fragment> extends FragmentPagerAdapter {
    private Context context;
    private List<T> mList;
    private String[] mTitles;

    public BaseFragmentPagerAdapter(Context context, FragmentManager fm, List<T> list) {
        this(context, fm, list, null);
    }

    public BaseFragmentPagerAdapter(Context context, FragmentManager fm, List<T> list, String[] titles) {
        super(fm);
        this.mList = list;
        this.context = context;
        mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (mTitles != null && position < mTitles.length) {
            return mTitles[position];
        }
        return super.getPageTitle(position);
    }
}
