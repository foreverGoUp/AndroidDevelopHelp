package ckc.android.develophelp.lib.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 *片段超出不可见片段范围后会摧毁片段实例。适合大量片段的页面（新闻主题页面）的显示。
 */
public class BaseFragmentStatePagerAdapter<T extends Fragment> extends FragmentStatePagerAdapter {
    private Context context;
    private List<T> mList;
    private String[] mTitles;

    public BaseFragmentStatePagerAdapter(Context context, FragmentManager fm, List<T> list) {
        this(context, fm, list, null);
    }

    public BaseFragmentStatePagerAdapter(Context context, FragmentManager fm, List<T> list, String[] titles) {
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
