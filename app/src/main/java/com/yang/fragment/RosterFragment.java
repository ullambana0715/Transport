package com.yang.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.orhanobut.logger.Logger;
import com.yang.adapter.RosterRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import com.yang.App;
import com.yang.MainActivity;
import com.yang.R;
import com.yang.customviews.GravitySnapHelper;
import com.yang.net2request.Job;
import com.yang.netokhttp3.RetrofitHttp;
import com.yang.netokhttp3.RxResultHelper;

/**
 * Created by Administrator on 2016/9/14.
 */
public class RosterFragment extends Fragment implements RosterRecyclerAdapter.OnItemClickListener, View.OnClickListener {
    RecyclerView recyclerView;
    RosterRecyclerAdapter rosterRecyclerAdapter;

    MainActivity mActivity;
    List<Job> mJobs = new ArrayList<Job>();
    Job selectedJob;
    @BindView(R.id.panel_left)
    ImageView panelLeft;
    @BindView(R.id.panel_mid)
    ImageView panelMid;
    @BindView(R.id.panel_right)
    ImageView panelRight;

    XRefreshView refreshView;
    private TextView mNoDataTv;

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        getRosterList();
    }

    public void getRosterList() {
        mActivity.getDialog().showLoading();
        RetrofitHttp.getInstance().getRosters(App.app.getLoginUser().getUid() + "")
                .compose(RxResultHelper.handleResult())
                .compose(RxResultHelper.applyIoSchedulers())
                .subscribe(getRosterRespBean -> {
//                    mActivity.getDialog().dismiss();
//                    mJobs.clear();
//                    Logger.e("getRosterList" + getRosterRespBean.toString());
//                    FileUtils.saveStringToFile("net_request_logs", "getRosterList" + new Date().toString(), mActivity);
//                    if (getRosterRespBean.getToday() != null && getRosterRespBean.getToday().size() != 0) {
//                        for (Job job : getRosterRespBean.getToday()) {
//                            job.setDay(RosterRecyclerAdapter.TODAY);
//                        }
//                    }
//                    if (getRosterRespBean.getTomorrow() != null && getRosterRespBean.getTomorrow().size() != 0) {
//                        for (Job job : getRosterRespBean.getTomorrow()) {
//                            job.setDay(RosterRecyclerAdapter.TOMMOROW);
//                        }
//                    }
//                    if (getRosterRespBean.getUpcoming() != null && getRosterRespBean.getUpcoming().size() != 0) {
//                        for (Job job : getRosterRespBean.getUpcoming()) {
//                            job.setDay(RosterRecyclerAdapter.UPCOMING);
//                        }
//                    }
//                    mJobs.addAll(getRosterRespBean.getToday());
//                    mJobs.addAll(getRosterRespBean.getTomorrow());
//                    mJobs.addAll(getRosterRespBean.getUpcoming());
//                    if (mJobs.size() == 0) {
//                        recyclerView.setVisibility(View.GONE);
//                        mNoDataTv.setVisibility(View.VISIBLE);
//                    } else {
//                        recyclerView.setVisibility(View.VISIBLE);
//                        mNoDataTv.setVisibility(View.GONE);
//                    }
//                    mActivity.showBadgeView(MainActivity.FRAGMENT_ALERTS, getRosterRespBean.getUnreadAlertJob());
//                    mActivity.showBadgeView(MainActivity.FRAGMENT_JOB, getRosterRespBean.getUnreadActiveJob());
//                    rosterRecyclerAdapter.refreshList(mJobs);
//                    rosterRecyclerAdapter.notifyDataSetChanged();
                }, throwable -> {
                    Logger.e("getRosterList" + throwable.toString());
                });
    }

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_roster, container, false);
        refreshView = (XRefreshView) view.findViewById(R.id.roster_refreshview);
        mNoDataTv = (TextView) view.findViewById(R.id.no_data_tv);
        refreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh() {
                super.onRefresh();
                getRosterList();
                refreshView.stopRefresh();
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.roster_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        rosterRecyclerAdapter = new RosterRecyclerAdapter(mActivity, mJobs, this);
        recyclerView.setAdapter(rosterRecyclerAdapter);

        return view;
    }

    View getRosterListView() {
        return LayoutInflater.from(mActivity).inflate(R.layout.fragment_roster, null);
    }

    @Override
    public void onItemClick(View view, int position, Job job) {
        selectedJob = job;
    }

    void loadMainView() {
        LinearLayout ll = (LinearLayout) getView();
        ll.removeAllViews();

        ll.addView(getRosterListView());
        refreshView = (XRefreshView) view.findViewById(R.id.roster_refreshview);
        refreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh() {
                super.onRefresh();
                getRosterList();
                refreshView.stopRefresh();
            }
        });
        recyclerView = (RecyclerView) ll.findViewById(R.id.roster_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        if (null == rosterRecyclerAdapter) {
            rosterRecyclerAdapter = new RosterRecyclerAdapter(mActivity, mJobs, this);
        }
        recyclerView.setAdapter(rosterRecyclerAdapter);
        new GravitySnapHelper(Gravity.START).attachToRecyclerView(recyclerView);

        panelLeft.setVisibility(View.GONE);
        panelRight.setVisibility(View.GONE);
        panelMid.setVisibility(View.GONE);
        mActivity.showLefuMenuSlide();
    }

    @OnClick({R.id.panel_left, R.id.panel_mid, R.id.panel_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.panel_mid:
                break;
            case R.id.panel_left:
                mActivity.setTv_title(R.string.position4_fragment);
                mActivity.showPanel();
                loadMainView();
                break;
        }
    }
}
