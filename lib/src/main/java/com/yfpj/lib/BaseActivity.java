package com.yfpj.lib;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.yfpj.lib.base.presenter.BasePresenter;
import com.yfpj.lib.base.view.BaseView;
import com.yfpj.lib.permission.RxPermissions;
import com.yfpj.lib.util.BaseUtils;
import com.yfpj.lib.util.MeasureUtil;
import com.yfpj.lib.waitdialog.WaitDialog;

import butterknife.ButterKnife;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class BaseActivity<T extends BasePresenter> extends FragmentActivity implements BaseView {
    protected Context mContext;
    protected String TAG = BaseActivity.this.getClass().getSimpleName();
    protected RxPermissions mRxPermissions;
    protected T mPresenter;

    /**
     * 应用被系统回收，再次进入，不保存应用状态
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

    @SuppressLint("CheckResult")
    protected void callPhone(final String phoneNumber) {
        mRxPermissions.request(Manifest.permission.CALL_PHONE).subscribe(aBoolean -> {
            if (aBoolean) {
                BaseUtils.callPhone(BaseActivity.this, phoneNumber);
            } else {
                toast(R.string.str_call_phone_no_permisson);
            }
        });
    }

    /**
     * 左上角back按钮
     */
    public void leftBack(View view) {
        if (preBackExitPage()) {
            return;
        }
        finishWithAnim();
    }

    /**
     * 设置recycle完全显示
     */
    protected void initRecycler(RecyclerView recycle, RecyclerView.LayoutManager manager) {
        manager.setAutoMeasureEnabled(true);
        recycle.setLayoutManager(manager);
        recycle.setHasFixedSize(true);//item的改变不会影响recyclerview的宽高
        recycle.setNestedScrollingEnabled(false);//保证滑动的流畅性
    }

    /**
     * 设置头部占位
     * 因6.0后状态栏高度为24。 6.0以前为25，固通过代码动态设置
     */
    protected void initPlaceView(View view) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        layoutParams.height = MeasureUtil.getStatusBarHeight(mContext);
        view.requestLayout();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WaitDialog.destroyDialog(this);
        if (mPresenter != null)
            mPresenter.onDestory();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null)
            mPresenter.onResume();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        getArgs();
        initListInfoData();
        initData();
        initView();
        asyncUiInfo();
    }

    protected void initListInfoData() {

    }

    protected void initView() {

    }


    protected void log(String msg) {
        BaseUtils.logh(TAG, msg);
    }

    protected String getEditText(EditText et) {
        return BaseUtils.getEditTextString(et);
    }


    /**
     * 获取传递的数据
     */
    protected void getArgs() {

    }

    protected void initData() {

    }

    /**
     * 获取基本数据后刷新
     */
    protected void asyncUiInfo() {

    }

    protected void forword(Class<? extends BaseActivity> c) {
        startActivity(new Intent(mContext, c));
    }

    /**
     * 退出之前，如果需要额外处理，调用此方法
     * true：	直接返回，停留在当前页面；
     * false：	继续执行退出后续操作。
     */
    protected boolean preBackExitPage() {
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (preBackExitPage()) {
                return true;
            }
            finishWithAnim();
        } else if (keyCode == KeyEvent.KEYCODE_MENU)
            return true;
        else if (keyCode == KeyEvent.KEYCODE_HOME)
            return true;
        return super.onKeyDown(keyCode, event);
    }

    public void finishWithAnim() {
        switch (getAnimType()) {
            case ANIM_TYPE_RIGHT_IN:
                finishWithAnimRightOut();
                break;
            case ANIM_TYPE_UP_IN:
                finishWithAnimDownOut();
                break;
            case ANIM_TYPE_DOWN_IN:
                finishWithAnimUpOut();
                break;
            case ANIM_TYPE_SCALE_CENTER:
                finishWithAnimScaleCenter();
                break;
            default:
                finish();
                break;
        }
    }

    private void finishWithAnimRightOut() {
        finish();
        overridePendingTransition(R.anim.ac_slide_left_in, R.anim.ac_slide_right_out);
    }

    private void finishWithAnimUpOut() {
        finish();
        overridePendingTransition(0, R.anim.ac_slide_up_out);
    }

    private void finishWithAnimDownOut() {
        finish();
        overridePendingTransition(0, R.anim.ac_slide_down_out);
    }

    private void finishWithAnimScaleCenter() {
        finish();
        overridePendingTransition(0, R.anim.ac_scale_shrink_center);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switch (getAnimType()) {
            case ANIM_TYPE_RIGHT_IN:
                overridePendingTransition(R.anim.ac_slide_right_in, R.anim.ac_slide_left_out);
                break;
            case ANIM_TYPE_UP_IN:
                overridePendingTransition(R.anim.ac_slide_up_in, 0);
                break;
            case ANIM_TYPE_DOWN_IN:
                overridePendingTransition(R.anim.ac_slide_down_in, R.anim.ac_none);
                break;
            case ANIM_TYPE_SCALE_CENTER:
                overridePendingTransition(R.anim.ac_scale_magnify_center, R.anim.ac_none);
                break;
            default:
                break;
        }
        //屏幕充满全屏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//状态栏字体变黑
        }
        mContext = this;
        mRxPermissions = new RxPermissions(this);
    }

    @Override
    public void toast(@Nullable String msg) {
        BaseUtils.toastShort(mContext, msg);
    }

    @Override
    public void toast(@StringRes int msg) {
        BaseUtils.toastShort(mContext, msg);
    }

    @Override
    public void showProgress() {
        WaitDialog.getInstance(mContext).showWaitDialog();
    }

    @Override
    public void hideProgress() {
        WaitDialog.getInstance(mContext).dismissWaitDialog();
    }

    public enum AnimType {
        ANIM_TYPE_DOWN_IN,
        ANIM_TYPE_RIGHT_IN, // 右侧滑动进入
        ANIM_TYPE_UP_IN, //
        ANIM_TYPE_SCALE_CENTER // 中心缩放显示/隐藏
    }

    public AnimType getAnimType() {
        return AnimType.ANIM_TYPE_RIGHT_IN;
    }

    /**
     * 点击edittext之外的地方隐藏键盘
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN){
            View v = getCurrentFocus();
            if (isShouldHideInput(v,ev)){
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null){
                    imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                    if(v instanceof EditText){
                        v.clearFocus();
                    }
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        if (getWindow().superDispatchTouchEvent(ev)){
            return true;
        }
        return onTouchEvent(ev);
    }

    /**
     * 是否隐藏键盘
     * @param v
     * @param ev
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (ev.getX() > left && ev.getX() < right
                    && ev.getY() > top && ev.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
}
