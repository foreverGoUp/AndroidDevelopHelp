package ckc.android.develophelp.lib.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.util.List;

/**
 *
 */
public class ViewPagerAdapter extends PagerAdapter {
    private List<View> mList;

    public ViewPagerAdapter(List<View> mList) {
        this.mList = mList;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        return arg0 == arg1;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // TODO Auto-generated method stub
        View view = mList.get(position);

        ViewParent vParent = view.getParent();
        if (vParent != null) {
            ViewGroup parent = (ViewGroup) vParent;
            parent.removeView(view);
        }

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        // TODO Auto-generated method stub
    }
}
