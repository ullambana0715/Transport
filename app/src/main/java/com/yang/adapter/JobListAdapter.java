package com.yang.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yang.App;
import com.yang.MainActivity;
import com.yang.net2request.Job;
import com.yang.netokhttp3.RxResultHelper;
import com.yang.utils.SplitTimeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.yang.bean.ActiveJobRespBean;
import com.yang.fragment.JobFragment;
import com.yang.netokhttp3.RetrofitHttp;

/**
 * Created by Administrator on 2016/9/14.
 */
public class JobListAdapter extends RecyclerView.Adapter {
    MainActivity mContext;
    List<Job> mJobs = new ArrayList<>();
    OnItemClickListener onItemClickListener;
    JobFragment mJobFragment;
    ActiveJobRespBean activeJobRespBean;
    public JobListAdapter(Context c, JobFragment fragment) {
        mContext = (MainActivity) c;
        getJob();
        mJobFragment = fragment;
        onItemClickListener = (OnItemClickListener)fragment;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(com.yang.R.layout.item_job, parent, false);
        return new JobListViewHolder(view, false);
    }

    void getJob(){
        RetrofitHttp.getInstance()
                .getJobList(App.app.getLoginUser().getUid()+"")
                .compose(RxResultHelper.handleRespbeanResult())
                .compose(RxResultHelper.transformer)
                .subscribe(response -> {
                    System.out.println(response.getResp());
                },throwable -> {
                    throwable.printStackTrace();
                });
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (mJobs == null || mJobs.size() == 0)return;
        JobListViewHolder jobListViewHolder = (JobListViewHolder)holder;
        jobListViewHolder.jobAddress.setText(TextUtils.isEmpty(mJobs.get(position).getAddress()) ? "" : mJobs.get(position).getAddress());
        jobListViewHolder.jobEstablishtime.setText(TextUtils.isEmpty(mJobs.get(position).getEstime()) ?"" : mJobs.get(position).getEstime()+"Hours");
        jobListViewHolder.jobMovesize.setText(mJobs.get(position).getMovesize()+"CuM");
        jobListViewHolder.jobDate.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        String s = mJobs.get(position).getWeek()+"\n"
                + SplitTimeUtils.splitDate(mJobs.get(position).getMoveDate())+"\n"
                +mJobs.get(position).getStartTime();
        SpannableString startTime = new SpannableString(s);
        startTime.setSpan(new AbsoluteSizeSpan(15,true),s.lastIndexOf("\n"),s.lastIndexOf(" "),SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        jobListViewHolder.jobTime.setText(startTime);
        if (mJobs.get(position).getReadMark() == 1) {
            jobListViewHolder.check.setVisibility(View.VISIBLE);
        } else {
            jobListViewHolder.check.setVisibility(View.INVISIBLE);
        }
        jobListViewHolder.rosterItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v,position,activeJobRespBean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mJobs.size();
    }
    class JobListViewHolder extends RecyclerView.ViewHolder {
        @BindView(com.yang.R.id.job_new)
        TextView jobDate;
        @BindView(com.yang.R.id.job_address)
        TextView jobAddress;
        @BindView(com.yang.R.id.job_movesize)
        TextView jobMovesize;
        @BindView(com.yang.R.id.job_establishtime)
        TextView jobEstablishtime;
        @BindView(com.yang.R.id.job_time)
        TextView jobTime;
        @BindView(com.yang.R.id.roster_item)
        LinearLayout rosterItem;
        @BindView(com.yang.R.id.check)
        ImageView check;

        public JobListViewHolder(View itemView, boolean hasLoaded) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            if (!hasLoaded) {

            }
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position, ActiveJobRespBean activeJobRespBean);
    }
}
