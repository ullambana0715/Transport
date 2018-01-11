package com.yang;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yang.fragment.ChatFragment;
import com.yang.fragment.RosterFragment;
import com.yang.mvpview.LoginActivity;
import com.yang.net2request.Job;
import com.yang.utils.ViewsUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.badgeview.BGABadgeImageView;
import com.yang.bean.ActiveJobRespBean;
import com.yang.fragment.AlertsFragment;
import com.yang.fragment.JobFragment;
import com.yang.mvppresenter.IPresenter;
import com.yang.mvppresenter.UserinfoPresenterCompl;
import com.yang.mvpview.EditUserInfoActivity;
import com.yang.mvpview.IUserInfoView;
import com.yang.net2request.Staff;
import com.yang.netokhttp2.HttpConstants;
import com.yang.utils.DialogUtil;
import com.yang.glidetransform.GlideCircleTransform;
import com.yang.utils.SharedprefUtil;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by Administrator on 2016/9/12.
 */
public class MainActivity extends BaseActivity implements IUserInfoView {
    UserinfoPresenterCompl userInfoPresenter;
    @BindView(R.id.alert_switch)
    ImageView iv_alert_switch;
    @BindView(R.id.title_rl)
    RelativeLayout title_rl;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.title)
    TextView tv_title;
    @BindView(R.id.title_left)
    TextView tv_menuOnMainContent;
    @BindView(R.id.chat)
    BGABadgeImageView tv_chat;
    @BindView(R.id.action)
    BGABadgeImageView tv_action;
    @BindView(R.id.alert_main)
    BGABadgeImageView tv_alert_main;
    @BindView(R.id.roster)
    BGABadgeImageView tv_roster;
    @BindView(R.id.bottom_cards)
    LinearLayout bottom_cards;

    public ChatFragment mChatFragment;
    public JobFragment mJobFragment;
    public RosterFragment mRosterFragment;
    public AlertsFragment mAlertsFragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.userinfo_icon)
    ImageView userinfoIcon;
    @BindView(R.id.firstname)
    TextView firstname;
    @BindView(R.id.lastname)
    TextView lastname;
    @BindView(R.id.email)
    TextView email;
    @BindView(R.id.mobile)
    TextView mobile;
    @BindView(R.id.user_portrait)
    ImageView userPortrait;
    @BindView(R.id.title_right)
    TextView tv_rightMenu;
    private static final String TAG = "MainActivity";

    public static final int FRAGMENT_CHAT = 10;
    public static final int FRAGMENT_JOB = 11;
    public static final int FRAGMENT_ALERTS = 12;
    public static final int FRAGMENT_ROSTER = 13;
    public static final int PERMISSIONS_REQUEST = 1000;
    private static int sFragmentNow;
    private DialogUtil mDialogUtilog;
    public int id;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userInfoPresenter = new UserinfoPresenterCompl(this, this);
        fragmentManager = getSupportFragmentManager();

        mChatFragment = new ChatFragment();
        mRosterFragment = new RosterFragment();
        mRosterFragment.onAttach(this);
        mAlertsFragment = new AlertsFragment();
        mJobFragment = new JobFragment();

        showFragment(mAlertsFragment);
    }
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onEventRoster(ActiveJobRespBean event) {
        mJobFragment.fragmentStatus = mJobFragment.JOB_DISPATCH_1;
        hidePanel();
        id = event.getJobId();
        mJobFragment.mJob = new Job();
        mJobFragment.mJob.setJobId(event.getJobId());

        if("555".equals(event.getStatus())){
            switch (event.getCurrentPage()){
                case 1:
                    mJobFragment.fragmentStatus = mJobFragment.JOB_DISPATCH_1;
                    break;
                case 2:
                    mJobFragment.fragmentStatus = mJobFragment.JOB_INSPECTION;
                    break;
                case 3:
                    break;
                case 4:
                    mJobFragment.fragmentStatus = mJobFragment.JOB_FINISH_COLLECT;
                    break;
            }

        }
        showFragment(mJobFragment);
    }


    public DialogUtil getDialog() {
        if (mDialogUtilog == null) {
            mDialogUtilog = new DialogUtil(MainActivity.this);
        }
        return mDialogUtilog;
    }

    @Override
    public IPresenter getPresenter() {
        return null;
    }

    @Override
    public int forceSetLayoutFirst() {
        return R.layout.activity_main;
    }

    boolean isExit;

    @Override
    public void onBackPressed() {
        if (!isExit) {
            isExit = true; // 准备退出
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
            Timer tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
        } else {
            finish();
        }
    }

    @Override
    public void onEditUserinfo() {
        startActivityForResult(new Intent(this, EditUserInfoActivity.class), REQUEST_IMAGE);
    }

    public void onChangeAlert(boolean b) {
        if (b) {
            iv_alert_switch.setImageResource(R.drawable.alert_switch_on);
            SharedprefUtil.getInstance(this).saveBoolean("isAlertMessageOn", true);
        } else {
            iv_alert_switch.setImageResource(R.drawable.alert_switch_off);
            SharedprefUtil.getInstance(this).saveBoolean("isAlertMessageOn", false);
        }
    }

    public void onLogout() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
    public Staff staff;

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEventLogin(Staff staff) {
        this.staff = staff;
        firstname.setText(getString(R.string.personal_first_name) + (staff.getFirstname() == null ? "" : staff.getFirstname()));
        lastname.setText(getString(R.string.personal_last_name) + (staff.getSurname() == null ? "" : staff.getSurname()));
        email.setText(getString(R.string.personal_email)+ (staff.getEmail() == null ? "" : staff.getEmail()));
        mobile.setText(getString(R.string.personal_mobile_num) + (staff.getPhone() == null ? "" : staff.getPhone()));
        username.setText(staff.getUsername());
        Glide.with(this).load(HttpConstants.TEST_URL + staff.getPhotoUrl()).bitmapTransform(new GlideCircleTransform(this)).crossFade(1000).error(R.drawable.user_portrait).into(userPortrait);
        Log.d(TAG, "onEventLogin: 111" + HttpConstants.TEST_URL + staff.getPhotoUrl());
    }


    @SuppressWarnings("ResourceType")
    @OnClick({R.id.chat, R.id.action, R.id.alert_main, R.id.roster, R.id.title,
            R.id.menu, R.id.title_left, R.id.edit_userinfo, R.id.alert_switch,
            R.id.logout_text,R.id.set_language_text,
//            R.id.user_portrait,
            R.id.title_right})
    public void onClick(View view) {
        ViewsUtils.preventViewMultipleClick(view, 1000);
        switch (view.getId()) {
            case R.id.title:
                if (bottom_cards.getVisibility() == View.VISIBLE) {
                    hidePanel();
                    setSlideable(true);
                } else {
                    showPanel();
                    setSlideable(false);
                }
                break;
            case R.id.chat:
//                Intent intenti = new Intent(MainActivity.this,SecondActivity.class);
//                startActivity(intenti);
//                showLefuMenuSlide();
//                setSlideable(false);
//                showFragment(mChatFragment);
                break;
            case R.id.action:
                mJobFragment.fragmentStatus = mJobFragment.JOB_LIST;
                hideRightMenu();
                showLefuMenuSlide();
                setSlideable(false);
                showFragment(mJobFragment);
                break;
            case R.id.alert_main:
                showFragment(mAlertsFragment);
                hideRightMenu();
                showLefuMenuSlide();
                setSlideable(true);
                break;
            case R.id.roster:
                showLefuMenuSlide();
                setSlideable(true);
                showFragment(mRosterFragment);
                hideRightMenu();
                break;
            case R.id.menu:
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case R.id.title_left:
                break;
            case R.id.edit_userinfo:
                EventBus.getDefault().postSticky(staff);
                userInfoPresenter.onEditUserinfo();
                break;
            case R.id.alert_switch:
                userInfoPresenter.onChangeAlert(staff);
                break;
            case R.id.logout_text:
                userInfoPresenter.onLogout();
                break;
            case R.id.set_language_text:
//                startActivity(new Intent(this, MultiLanguageActivity.class));
                break;
            case R.id.user_portrait:
                Intent intent = new Intent(this, MultiImageSelectorActivity.class);
                // 是否显示调用相机拍照
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);

                // 最大图片选择数量
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);

                // 设置模式 (支持 单选/MultiImageSelectorActivity.MODE_SINGLE 或者 多选/MultiImageSelectorActivity.MODE_MULTI)
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);

                startActivityForResult(intent, REQUEST_IMAGE);
                break;
            case R.id.title_right:
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                // 获取返回的图片列表
                String path = data.getStringExtra("userPortrait");
                if (!TextUtils.isEmpty(path)) {
                    Glide.with(this).load(new File(path)).bitmapTransform(new GlideCircleTransform(this)).crossFade(1000).error(R.drawable.user_portrait).into(userPortrait);
                }
            }
        }
    }

    public static int REQUEST_IMAGE = 1000;

    public void showBadgeView(int anchor, int number) {
        switch (anchor) {
            case FRAGMENT_ALERTS:
                if (number == 0) {
                    tv_alert_main.hiddenBadge();
                } else {
                    tv_alert_main.showTextBadge(number + "");
                }
                tv_alert_main.getBadgeViewHelper().setBadgeHorizontalMarginDp(10);
                break;
            case FRAGMENT_CHAT:
                if (number == 0) {
                    tv_chat.hiddenBadge();
                } else {
                    tv_chat.showTextBadge(number + "");
                }
                break;
            case FRAGMENT_JOB:
                if (number == 0) {
                    tv_action.hiddenBadge();
                } else {
                    tv_action.showTextBadge(number + "");
                }
                tv_action.getBadgeViewHelper().setBadgeHorizontalMarginDp(10);
                break;
            case FRAGMENT_ROSTER:
                break;
        }
    }

    public void showFragment(Fragment fragment) {
        tv_chat.setImageResource(R.drawable.chat_normal);
        tv_action.setImageResource(R.drawable.action_normal);
        tv_alert_main.setImageResource(R.drawable.alert_main_normal);
        tv_roster.setImageResource(R.drawable.roster_normal);

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction
//                .setCustomAnimations(R.anim.glide_fragment_horizontal_in,R.anim.glide_fragment_horizontal_out)
                .replace(R.id.fragment_container, fragment).commit();
        if (fragment instanceof ChatFragment) {
            sFragmentNow = FRAGMENT_CHAT;
            tv_title.setText(getString(R.string.main_chat));
            tv_chat.setImageResource(R.drawable.chat_select);
        } else if (fragment instanceof JobFragment) {
            sFragmentNow = FRAGMENT_JOB;
//            tv_action.showTextBadge("20");
//            tv_action.getBadgeViewHelper().setBadgeGravity(BGABadgeViewHelper.BadgeGravity.RightTop);
            tv_title.setText(getString(R.string.main_job));
            tv_action.setImageResource(R.drawable.action_select);
        } else if (fragment instanceof AlertsFragment) {
            sFragmentNow = FRAGMENT_ALERTS;
//            tv_alert_main.showTextBadge("8000");
            tv_title.setText(getString(R.string.main_alert));
            tv_alert_main.setImageResource(R.drawable.alert_main_select);
        } else if (fragment instanceof RosterFragment) {
            sFragmentNow = FRAGMENT_ROSTER;
            tv_title.setText(getString(R.string.main_roster));
            tv_roster.setImageResource(R.drawable.roster_select);
        }
    }

    public void hidePanel() {
        bottom_cards.setVisibility(View.GONE);
    }

    public void showPanel() {
        bottom_cards.setVisibility(View.VISIBLE);
    }

    public void showRightMenu() {
        tv_rightMenu.setVisibility(View.VISIBLE);
    }

    public void hideRightMenu() {
        tv_rightMenu.setVisibility(View.GONE);
    }

    public void showLefuMenuSlide() {
        tv_menuOnMainContent.setCompoundDrawablesWithIntrinsicBounds(R.drawable.menu, 0, 0, 0);
        tv_menuOnMainContent.setVisibility(View.VISIBLE);
    }

    public void hideLeftMenu() {
        tv_menuOnMainContent.setVisibility(View.GONE);
    }

    public void setLeftMenuBack() {
        tv_menuOnMainContent.setVisibility(View.VISIBLE);
        tv_menuOnMainContent.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mis_back_title, 0, 0, 0);
    }

    public void setSlideable(boolean b) {
        if (b) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED);
        } else {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    public void hideTitle(){
        title_rl.setVisibility(View.GONE);
    }

    public void showTitle(){
        title_rl.setVisibility(View.VISIBLE);
    }

    public void setTv_title(int id) {
        tv_title.setText(id);
    }

}
