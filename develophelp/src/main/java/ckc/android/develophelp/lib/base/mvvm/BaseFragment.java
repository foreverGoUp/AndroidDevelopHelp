package ckc.android.develophelp.lib.base.mvvm;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.View;

import ckc.android.develophelp.lib.base.common.LazyFragment;


/**
 * created by ckc on 2019-12-01
 * successfulpeter@163.com
 */
public abstract class BaseFragment<DB extends ViewDataBinding, VM extends BaseViewModel> extends LazyFragment {

    protected DB mDataBinding;

    protected VM mViewModel;

    @Override
    protected void OnInit(View view) {
        mDataBinding = DataBindingUtil.bind(view);
        onConfigViewModelAndBindDataBinding();
    }


    /**
     * 初始化ViewModel并绑定DataBinding
     */
    protected abstract void onConfigViewModelAndBindDataBinding();

}
