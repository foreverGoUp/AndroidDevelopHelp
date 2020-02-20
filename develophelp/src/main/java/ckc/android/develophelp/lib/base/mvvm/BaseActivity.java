package ckc.android.develophelp.lib.base.mvvm;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;

import ckc.android.develophelp.lib.base.common.RootActivity;


public abstract class BaseActivity<DB extends ViewDataBinding, VM extends BaseViewModel> extends RootActivity {

    protected DB mDataBinding;

    protected VM mViewModel;

    @Override
    protected boolean onConfigSubActivitySetContentView() {
        mDataBinding = DataBindingUtil.setContentView(this, onConfigContentViewLayout());

        onConfigViewModelAndBindDataBinding();
        return true;
    }

    /**
     * 初始化ViewModel并绑定DataBinding
     */
    protected abstract void onConfigViewModelAndBindDataBinding();


}
