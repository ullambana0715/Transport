package com.yang.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.XWebView;
import com.daimajia.numberprogressbar.NumberProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/11.
 */

public class JobSheetBigHolder extends RecyclerView.ViewHolder {
//    @Nullable
//    @BindView(R.id.item_refreshview)
//    XRefreshView item_refreshview;
    @Nullable
    @BindView(com.yang.R.id.name)
    TextView name;
    @Nullable
    @BindView(com.yang.R.id.phone1)
    TextView phone1;
    @Nullable
    @BindView(com.yang.R.id.phone2)
    TextView phone2;
    @Nullable
    @BindView(com.yang.R.id.forman_notes_title)
    TextView forman_notes_title;
    @Nullable
    @BindView(com.yang.R.id.address_container)
    LinearLayout address_container;
    @Nullable
    @BindView(com.yang.R.id.address_layout1)
    LinearLayout address_layout1;

    @Nullable
    @BindView(com.yang.R.id.attachitems_name)
    TextView attachitems_name;
    @Nullable
    @BindView(com.yang.R.id.truck_type)
    TextView truck_type;
    @Nullable
    @BindView(com.yang.R.id.truck_capacity)
    TextView truck_capacity;
    @Nullable
    @BindView(com.yang.R.id.truck_gvm)
    TextView truck_gvm;
    @Nullable
    @BindView(com.yang.R.id.truck_tm)
    TextView truck_tm;
    @Nullable
    @BindView(com.yang.R.id.tv_equipment)
    TextView tv_equipment;
    @Nullable
    @BindView(com.yang.R.id.truck_men)
    TextView truck_men;
    @Nullable
    @BindView(com.yang.R.id.equipment_no)
    TextView equipment_no;
    @Nullable
    @BindView(com.yang.R.id.equipment_value)
    TextView equipment_value;
    @Nullable
    @BindView(com.yang.R.id.ll_discounts)
    LinearLayout ll_discounts;

    @Nullable
    @BindView(com.yang.R.id.truck_equipment_name)
    TextView truck_equipment_name;
    @Nullable
    @BindView(com.yang.R.id.truck_equipment_1)
    TextView truck_equipment_1;
    @Nullable
    @BindView(com.yang.R.id.job_date)
    TextView job_date;
    @Nullable
    @BindView(com.yang.R.id.starttime)
    TextView starttime;
    @Nullable
    @BindView(com.yang.R.id.job_type)
    TextView job_type;

    @Nullable
    @BindView(com.yang.R.id.job_movesize)
    TextView job_movesize;
    @Nullable
    @BindView(com.yang.R.id.job_est1)
    TextView job_est1;
    @Nullable
    @BindView(com.yang.R.id.job_est2)
    TextView job_est2;
    @Nullable
    @BindView(com.yang.R.id.est_time)
    TextView job_est_time;
    @Nullable
    @BindView(com.yang.R.id.job_rate_type)
    TextView job_rate_type;



    @Nullable
    @BindView(com.yang.R.id.rate)
    TextView rate;
    @Nullable
    @BindView(com.yang.R.id.call_out_fee)
    TextView call_out_fee;
    @Nullable
    @BindView(com.yang.R.id.return_fee)
    TextView return_fee;
    @Nullable
    @BindView(com.yang.R.id.extra_charge_name0)
    TextView extra_charge_name0;
    @Nullable
    @BindView(com.yang.R.id.extra_charge_value0)
    TextView extra_charge_value0;

    @Nullable
    @BindView(com.yang.R.id.job_min_time)
    TextView job_min_time;
    @Nullable
    @BindView(com.yang.R.id.time_increments)
    TextView time_increments;
    @Nullable
    @BindView(com.yang.R.id.dicount_value)
    TextView dicount_value;
    @Nullable
    @BindView(com.yang.R.id.dicount_name)
    TextView dicount_name;
    @Nullable
    @BindView(com.yang.R.id.break_name)
    TextView break_name;

    @Nullable
    @BindView(com.yang.R.id.roster_action_name)
    TextView action_name;
    @Nullable
    @BindView(com.yang.R.id.roster_action_value)
    TextView action_value;

    @Nullable
    @BindView(com.yang.R.id.dispatch_time)
    TextView dispatch_time;


    @Nullable
    @BindView(com.yang.R.id.arrival_time)
    TextView arrival_time;
    @Nullable
    @BindView(com.yang.R.id.timesheet_starttime)
    TextView timesheet_starttime;
    @Nullable
    @BindView(com.yang.R.id.break_time)
    TextView break_time;
    @Nullable
    @BindView(com.yang.R.id.finish_time)
    TextView finish_time;
    @Nullable
    @BindView(com.yang.R.id.total_time)
    TextView total_time;
    @Nullable
    @BindView(com.yang.R.id.vehicle_charge)
    TextView vehicle_charge;
    @Nullable
    @BindView(com.yang.R.id.extra_charge)
    TextView extra_charge;
    @Nullable
    @BindView(com.yang.R.id.total_discount)
    TextView total_discount;
    @Nullable
    @BindView(com.yang.R.id.sub_total)
    TextView sub_total;
    @Nullable
    @BindView(com.yang.R.id.tax)
    TextView tax;
    @Nullable
    @BindView(com.yang.R.id.total)
    TextView total;
    @Nullable
    @BindView(com.yang.R.id.general_notes)
    TextView general_notes;
    @Nullable
    @BindView(com.yang.R.id.forman_notes)
    TextView forman_notes;
    @Nullable
    @BindView(com.yang.R.id.terms)
    TextView terms;

    @Nullable
    @BindView(com.yang.R.id.job_web_progress)
    NumberProgressBar progressBar;

    @Nullable
    @BindView(com.yang.R.id.job_webView)
    XWebView mWebview;

    @Nullable
    @BindView(com.yang.R.id.items_container)
    LinearLayout items_container;
    @Nullable
    @BindView(com.yang.R.id.tv_inventory)
    TextView tv_inventory;
    @Nullable
    @BindView(com.yang.R.id.inventory_container)
    LinearLayout inventory_container;


    @Nullable
    @BindView(com.yang.R.id.ll_payment)
    LinearLayout llPayment;

    @Nullable
    @BindView(com.yang.R.id.basepay_pay_type)
    TextView basepay_pay_type;
    @Nullable
    @BindView(com.yang.R.id.basepay_pay_amount)
    TextView basepay_pay_amount;
  /*  @Nullable
    @BindView(R.id.basepay_call_out_fee)
    TextView basepay_call_out_fee;
    @Nullable
    @BindView(R.id.basepay_return_fee)
    TextView basepay_return_fee;
    @Nullable
    @BindView(R.id.extra_payments_name0)
    TextView extra_payments_name0;
    @Nullable
    @BindView(R.id.extra_payments_value0)
    TextView extra_payments_value0;
    @Nullable
    @BindView(R.id.time_minimun_time)
    TextView time_minimun_time;
    @Nullable
    @BindView(R.id.time_increments_time)
    TextView time_increments_time;*/
    @Nullable
    @BindView(com.yang.R.id.forman_text_clickable)
    TextView forman_text_clickable;
    @Nullable
    @BindView(com.yang.R.id.general_text_clickable)
    TextView general_text_clickable;
    @Nullable
    @BindView(com.yang.R.id.address1_text_clickable)
    TextView address1_text_clickable;


    @Nullable
    @BindView(com.yang.R.id.contact)
    LinearLayout contact;
    @Nullable
    @BindView(com.yang.R.id.ll1)
    LinearLayout ll1;
    @Nullable
    @BindView(com.yang.R.id.ll2)
    LinearLayout ll2;


    public JobSheetBigHolder(View itemView, boolean hasLoaded) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
