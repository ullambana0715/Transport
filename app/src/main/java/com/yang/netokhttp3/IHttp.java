package com.yang.netokhttp3;

import com.yang.bean.EditUserInfoRespBean;
import com.yang.bean.GetAlertsRespBean;
import com.yang.bean.GetRosterRespBean;
import com.yang.bean.InspectionJobRespBean;
import com.yang.bean.PayCashRespBean;

import java.util.Map;

import com.yang.bean.EditUserImgRespBean;
import com.yang.bean.GetJobExtraChargeRespBean;
import com.yang.bean.GetUserInfoRespBean;
import com.yang.bean.RespBean;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by Administrator on 2016/8/31.
 */
public interface IHttp {
//    @POST("/login")
//    @FormUrlEncoded
//    rx.Observable<RespBean<LoginRespBean>> login();

    //@Path 补全url中的{}变量部分 动态获取URL地址：@Path
    //@Field 用于POST请求的表单，Formed过的body //使用@Field时记得添加@FormUrlEncoded
    //@FieldMap 相当于多个@Field，以hashmap的形式提交
    //@Body post请求过去所带的数据body，可以以文本（比如json），图片等形式传输作为一般的Requestbody，也可以用Field作为FormBody提交，FromBody extends RequestBody
    //@Query 跟在url的问号后面进行赋值操作  使用@Query注解即可完成我们的需求，在@Query(“sort”)中，short就好比是URL请求地址中的键，而它说对应的String sort中的sort则是它的值。
    //@QueryMap：相当于多个@Query
    //@Url 用于重新定义接口地址，将地址以参数的形式传入
    //@PartMap 用于post多媒体图片

    //http://blog.csdn.net/guiman/article/details/51480497
    //http://www.jianshu.com/p/3e13e5d34531

    @POST("/mlogic/app/staff/login")
    @FormUrlEncoded
    rx.Observable<RespBean> login(@Field("username") String username, @Field("password") String pwd);

    @POST("/mlogic/app/staff/checkAppVersion")
    @FormUrlEncoded
    rx.Observable<RespBean> checkVersion(@Field("deviceId") String deviceId);

    @POST("/mlogic/app/staff/logout")
    @FormUrlEncoded
    rx.Observable<RespBean> logout(@Field("uid") String uid);

    @POST("/mlogic/app/staff/getAlertsList")
    @FormUrlEncoded
    rx.Observable<RespBean> getNewAlerts(@Field("uid") String uid);

    @POST("/mlogic/app/staff/getActiveJob")
    @FormUrlEncoded
    rx.Observable<RespBean> getJobList(@Field("uid") String uid);

    @POST("/mlogic/app/staff/editUserImage")
    @FormUrlEncoded
    rx.Observable<RespBean> uploadImage(@Field("uid") String uid, @Part MultipartBody.Part file);

    @Streaming
    @GET
    rx.Observable<RespBean> downloadFile(@Header("RANGE") String start,@Url String url);

    //old web interface
    //<---------------------------->//

//    @POST("/mlogic/app/staff/logout")
//    @FormUrlEncoded
//    rx.Observable<RespBean<LogoutRespBean>> logout(@Field("uid") String uid);

    @POST("/mlogic/app/staff/editUserInfo")
    @FormUrlEncoded
    rx.Observable<RespBean<EditUserInfoRespBean>> editUserInfo(
            @Field("uid") String uid,
            @Field("username") String username,
            @Field("password") String npassword,
            @Field("firstname") String firstname,
            @Field("surname") String surname,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("isAlert") short isAlert
    );

    @POST("/mlogic/app/staff/getUserInfo")
    @FormUrlEncoded
    rx.Observable<RespBean<GetUserInfoRespBean>> getUserInfo(@Field("uid") String uid);

    @POST("/mlogic/app/staff/getAlertsList")
    @FormUrlEncoded
    rx.Observable<RespBean<GetAlertsRespBean>> getAlerts(@Field("uid") String uid);

    @POST("/mlogic/app/staff/getRosterList")
    @FormUrlEncoded
    rx.Observable<RespBean<GetRosterRespBean>> getRosters(@Field("uid") String uid);

    @POST("/mlogic/app/staff/editUserImage")
    @Multipart
    rx.Observable<RespBean<EditUserImgRespBean>> editUserImage(@Part("uid") int uid, @PartMap Map<String, RequestBody> params);

    @POST("/mlogic/app/staff/inspectionJob")
    @Multipart
    rx.Observable<RespBean<InspectionJobRespBean>> inspectionJob(@PartMap Map<String, RequestBody> params);

    @POST("/mlogic/app/staff/getJobExtraCharge")
    @FormUrlEncoded
    rx.Observable<RespBean<GetJobExtraChargeRespBean>> getJobExtraCharge(@Field("jobId") int jobId);

    @POST("/mlogic/app/staff/inspectAndSign")
    @Multipart
    rx.Observable<RespBean<InspectionJobRespBean>> inspectAndSign(@PartMap Map<String, RequestBody> params);

    @POST("/mlogic/app/staff/payCreditCard")
    @FormUrlEncoded
    rx.Observable<RespBean<PayCashRespBean>> payCreditCard(@Field("payInfoStr") String payInfoStr, @Field("totalPrice") double totalPrice);

    @POST("http://13.55.134.228/mlogic/app/staff/upLocation")
    @FormUrlEncoded
    rx.Observable<RespBean<Object>> upGps(@Field("longitude") double longitude, @Field("latitude") double latitude, @Field("devietype") int devietype, @Field("address") String address);

    @Streaming
    @GET
    rx.Observable<ResponseBody> download(@Header("RANGE") String start, @Url String url);
}
