package com.yang.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.yang.App;
import com.yang.MainActivity;
import com.yang.adapter.AlertsRecyclerAdapter;
import com.yang.customviews.GravitySnapHelper;
import com.yang.download.DownloadItemInfo;
import com.yang.download.DownloadManager;
import com.yang.download.HttpProgressOnNextListener;
import com.yang.net2request.Job;
import com.yang.netokhttp3.ProgressBody;
import com.yang.netokhttp3.RetrofitHttp;
import com.yang.netokhttp3.RxResultHelper;
import com.yang.netokhttp3.UploadProgressListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


/**
 * Created by Administrator on 2016/9/14.
 */
public class AlertsFragment extends Fragment implements AlertsRecyclerAdapter.OnItemClickListener, View.OnClickListener {
    RecyclerView recyclerView;
    AlertsRecyclerAdapter alertsRecyclerAdapter;
    MainActivity mActivity;

    XRefreshView refreshView;
    public int fragmentViewStatus = 0;
    public static final int ALERT_FRAGMENT_MAIN = 0;
    List<Job> mJobs = new ArrayList<Job>();

    Job selectedJob;
    private TextView mNoDataTv;
    private static final String TAG = "AlertsFragment";

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentViewStatus = ALERT_FRAGMENT_MAIN;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(com.yang.R.layout.fragment_alerts, container, false);
        recyclerView = (RecyclerView) view.findViewById(com.yang.R.id.alert_recyclerview);
        mNoDataTv = (TextView) view.findViewById(com.yang.R.id.no_data_tv);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        alertsRecyclerAdapter = new AlertsRecyclerAdapter(mActivity, mJobs, this);
        recyclerView.setAdapter(alertsRecyclerAdapter);
        new GravitySnapHelper(Gravity.START).attachToRecyclerView(recyclerView);

        refreshView = (XRefreshView) view.findViewById(com.yang.R.id.alert_refreshview);
        refreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh() {
                super.onRefresh();
                getAlertsList();
                refreshView.stopRefresh();
            }
        });

        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
//        getAlertsList();
        downloadApk();
        downloadApk0();
    }

    void uploadImage(){
        File file=new File("/storage/emulated/0/Download/11.jpg");
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"),file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("image",file.getName(),new ProgressBody(new UploadProgressListener() {
            @Override
            public void onProgress(long totalsize, long currentsize) {
                Log.i(totalsize+"","currentSize:"+currentsize);
            }
        },requestBody));
        RetrofitHttp.getInstance().uploadImage("",part);
    }

    void getAlertsList() {
        mActivity.getDialog().showLoading();
        RetrofitHttp.getInstance()
                .getNewAlerts(App.app.getLoginUser().getUid() + "")
                .compose(RxResultHelper.handleRespbeanResult())
                .compose(RxResultHelper.transformer)
                .subscribe(getAlertsRespBean -> {
                    System.out.println("getAlertsRespBean:"+getAlertsRespBean.getResp());
                }, throwable -> {
                    throwable.printStackTrace();
                });
    }

    void downloadApk(){
        DownloadItemInfo downloadItemInfo=new DownloadItemInfo();
        downloadItemInfo.setUrl("http://pro.sunshinelunch.com/upload/dl/sunshinelunch.apk");
        downloadItemInfo.setId(System.currentTimeMillis());
        downloadItemInfo.setSDownload_Status(DownloadItemInfo.DOWNLOAD_START);
        downloadItemInfo.setSavePath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+System.currentTimeMillis()+"-sunshinelunch.apk");
        downloadItemInfo.setListener(httpProgressOnNextListener);
        DownloadManager.getInstance().startDownload(downloadItemInfo);
    }

    void downloadApk0(){
        DownloadItemInfo downloadItemInfo=new DownloadItemInfo();
        downloadItemInfo.setUrl("http://pro.sunshinelunch.com/upload/dl/ims.apk");
        downloadItemInfo.setId(System.currentTimeMillis());
        downloadItemInfo.setSDownload_Status(DownloadItemInfo.DOWNLOAD_START);
        downloadItemInfo.setSavePath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+System.currentTimeMillis()+"-ims.apk");
        downloadItemInfo.setListener(httpProgressOnNextListener0);
        DownloadManager.getInstance().startDownload(downloadItemInfo);
    }
    HttpProgressOnNextListener<DownloadItemInfo> httpProgressOnNextListener0 = new HttpProgressOnNextListener<DownloadItemInfo>() {
        @Override
        public void onNext(DownloadItemInfo downloadItemInfo) {

        }

        @Override
        public void onStart() {

        }

        @Override
        public void onComplete() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onPause() {

        }

        @Override
        public void onStop() {

        }

        @Override
        public void updateProgress(long readLength, long countLength) {
            double percent = (double)readLength/countLength;
            int per = (int)(percent * 100);
            System.out.println("000---ReadLength:"+readLength +"---"+"CountLength:"+countLength + "---Percent:"+per+"/100");
        }
    };
    HttpProgressOnNextListener<DownloadItemInfo> httpProgressOnNextListener = new HttpProgressOnNextListener<DownloadItemInfo>() {
        @Override
        public void onNext(DownloadItemInfo downloadItemInfo) {

        }

        @Override
        public void onStart() {

        }

        @Override
        public void onComplete() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onPause() {

        }

        @Override
        public void onStop() {

        }

        @Override
        public void updateProgress(long readLength, long countLength) {
            double percent = (double)readLength/countLength;
            int per = (int)(percent * 100);
            System.out.println("ReadLength:"+readLength +"---"+"CountLength:"+countLength + "---Percent:"+per+"/100");
        }
    };

    @Override
    public void onItemClick(View view, int position, Job job) {
        selectedJob = job;
        mActivity.setTv_title(com.yang.R.string.alerts_detail);
    }
    @Optional
    @OnClick({com.yang.R.id.panel_left, com.yang.R.id.panel_mid, com.yang.R.id.panel_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case com.yang.R.id.panel_left:
                break;
            case com.yang.R.id.panel_mid:
                break;
            case com.yang.R.id.panel_right:
                break;
        }
    }
}
