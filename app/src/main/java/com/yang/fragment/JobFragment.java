package com.yang.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.yang.MainActivity;
import com.yang.adapter.JobListAdapter;
import com.yang.customviews.GravitySnapHelper;
import com.yang.net2request.Job;
import com.yang.net2request.TransactionFee;
import com.yang.netokhttp3.RetrofitHttp;
import com.yang.netokhttp3.RxResultHelper;
import com.yang.utils.MultipartUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.yang.bean.ActiveJobRespBean;
import com.yang.bean.PayCreditCardReqBean;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscription;

/**
 * Created by yangjinyu on 2016/9/18.
 */
public class JobFragment extends Fragment implements JobListAdapter.OnItemClickListener,View.OnClickListener {
    public MainActivity mActivity;

    public static final int JOB_LIST = 11;
    public static final int JOB_DISPATCH_1 = 0;
    public static final int JOB_INSPECTION = 4;
    public static final int JOB_PRE_1 = 5;
    public static final int JOB_PRE_2 = 6;
    public static final int JOB_IN_PLAY = 7;
    public static final int JOB_PAUSE_TIME = 81;
    public static final int JOB_SHEET = 100;
    public static final int JOB_FINISH_COLLECT = 200;

    public static final int START_TIMER = 300;

    @BindView(com.yang.R.id.job_view_container)
    RelativeLayout jobViewContainer;
    int minTimeIndex = 0;
    int maxTimeIndex = 0;
    JobListAdapter jobListAdapter;
    RecyclerView jobListRecyclerView;

    int card_sp_index;
    @Nullable
    @BindView(com.yang.R.id.creadit_card_et)
    EditText creadit_card_et;
    List<String> spinnerList = new ArrayList<>();
    List<String> attachmentPicPath;
    public Job mJob;
    public int fragmentStatus = 0;
    double totalMoney;
    double cardMoney;
    double surchargePercentage = 5.0;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.from(mActivity).inflate(com.yang.R.layout.fragment_job, null);
        ButterKnife.bind(this, view);
        jobViewContainer.setGravity(Gravity.TOP);
        jobViewContainer.addView(LayoutInflater.from(mActivity).inflate(com.yang.R.layout.fragment_job_list, null));
        jobListAdapter = new JobListAdapter(mActivity, this);
        jobListRecyclerView = (RecyclerView) view.findViewById(com.yang.R.id.job_list_recyclerview);
        jobListRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        jobListRecyclerView.setAdapter(jobListAdapter);
        new GravitySnapHelper(Gravity.START).attachToRecyclerView(jobListRecyclerView);
        return view;
    }
    @Override
    public void onItemClick(View view, int position, ActiveJobRespBean respBean) {
        mJob = respBean.getJob();
    }

    public void onClick(@Nullable View view) {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == mActivity.REQUEST_IMAGE) {
            if (data != null) {
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                attachmentPicPath = path;
//                Glide.with(mActivity).load(new File(path.get(0))).into(attach_pic_iv);
            }
        }
    }

    public void showJobSheet() {
        fragmentStatus = JOB_SHEET;
    }

    void inspectionJob(String filePath) {
        Map<String, RequestBody> map = MultipartUtil.getFilesBody(filePath, "signImage");
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), mJob.getJobId() + "");
        RequestBody requestBody0 = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), minTimeIndex + 1 + "");
        RequestBody requestBody1 = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), maxTimeIndex + 1 + "");
        RequestBody requestBody2 = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "true");
        RequestBody requestBody3 = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "true");
        map.put("jobId", requestBody);
        map.put("estimateTimeFrom", requestBody0);
        map.put("estimateTimeTo", requestBody1);
        map.put("termsOfService", requestBody2);
        map.put("chargesAndCosts", requestBody3);
        Subscription subscription = RetrofitHttp.getInstance()
                .inspectionJob(map)
                .compose(RxResultHelper.handleResult())
                .compose(RxResultHelper.applyIoSchedulers())
                .subscribe(inspectionJob -> {
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    mActivity.showTitle();
                    spinnerList.clear();
                }, throwable -> {
//                    Toast.makeText(mActivity, "inspectionJob fail", Toast.LENGTH_SHORT).show();
                    Logger.e("inspectionJob:" + throwable.getMessage());
                });
    }

    void getExtraCharge() {
        Subscription subscription = RetrofitHttp.getInstance()
                .getJobExtraCharge(mJob.getJobId())
                .compose(RxResultHelper.handleResult())
                .compose(RxResultHelper.applyIoSchedulers())
                .subscribe(getJobExtraChargeRespBean -> {
                }, throwable -> {
//                    Toast.makeText(mActivity, "getJobExtraChargeRespBean fail", Toast.LENGTH_SHORT).show();
                    Logger.e("getJobExtraChargeRespBean:" + throwable.getMessage());
                });
    }

    void inspectAndSign() {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("jobId ",  RequestBody.create(MediaType.parse("application/json; charset=utf-8"), mJob.getJobId() + ""));
        Subscription subscription = RetrofitHttp.getInstance()
                .inspectAndSign(map)
                .compose(RxResultHelper.handleResult())
                .compose(RxResultHelper.applyIoSchedulers())
                .subscribe(inspectAndSign -> {
                }, throwable -> {
//                    Toast.makeText(mActivity, "inspectAndSign fail", Toast.LENGTH_SHORT).show();
                    Logger.e("inspectAndSign:" + throwable.getMessage());
                });
    }

    void inspectAndSign(String filePath) {
        Map<String, RequestBody> map = MultipartUtil.getFilesBody(filePath, "signImage");
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), mJob.getJobId() + "");
        RequestBody requestBody2 = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "true");
//        RequestBody requestBody3 = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (isSignTerm == 0 ? true:false) +"");
//        RequestBody requestBody4= RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (isSignCharge == 0 ? true:false) +"");
        map.put("jobId",requestBody);
        map.put("sustomerSigned",requestBody2);
//        map.put("npDamages", requestBody3);
//        map.put("chargesAndCosts", requestBody4);
        Subscription subscription = RetrofitHttp.getInstance()
                .inspectAndSign(map)
                .compose(RxResultHelper.handleResult())
                .compose(RxResultHelper.applyIoSchedulers())
                .subscribe(inspectAndSign -> {
//                    Toast.makeText(mActivity, "inspectAndSign success", Toast.LENGTH_SHORT).show();
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    mActivity.showTitle();
                }, throwable -> {
//                    Toast.makeText(mActivity, "inspectAndSign fail", Toast.LENGTH_SHORT).show();
                    Logger.e("inspectAndSign:" + throwable.getMessage());
                });
    }

    void payCreditCard() {
        if ((creadit_card_et.getText().toString().trim().isEmpty())) {
            Toast.makeText(mActivity, "Please input the Card No.!", Toast.LENGTH_SHORT).show();
            return;
        }
        if ((creadit_card_et.getText().toString().trim().length() < 15) || (creadit_card_et.getText().toString().trim().length() > 25)) {
            Toast.makeText(mActivity, "Please Check the CreditCard No. is Available!", Toast.LENGTH_SHORT).show();
            return;
        }
        PayCreditCardReqBean reqBean = new PayCreditCardReqBean();
        reqBean.setJobId(mJob.getJobId());
        reqBean.setCardNo(creadit_card_et.getText().toString());
        reqBean.setCardType(card_sp_index);
        reqBean.setPayment(cardMoney);
        reqBean.setPayType(2);
        TransactionFee fee = new TransactionFee();
        fee.setRate(surchargePercentage);
        reqBean.setTransactionFee(fee);
        Gson gson = new Gson();
        String s = gson.toJson(reqBean);
        RetrofitHttp.getInstance()
                .payCreditCard(s, totalMoney)
                .compose(RxResultHelper.handleResult())
                .compose(RxResultHelper.applyIoSchedulers())
                .subscribe(payCreditCard -> {
                }, throwable -> {
                    Logger.e("payCreditCard:" + throwable.getMessage());
                });
    }
}
