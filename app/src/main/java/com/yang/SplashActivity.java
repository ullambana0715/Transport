package com.yang;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v13.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.squareup.picasso.RequestHandler;
import com.yang.bean.LoginRequstBean;
import com.yang.bean.TestRespBean;
import com.yang.mvppresenter.IPresenter;
import com.yang.mvpview.LoginActivity;
import com.yang.net2request.NTRespBean;
import com.yang.net2request.Staff;
import com.yang.net2request.checkVersion.CheckVersionService;
import com.yang.net2request.checkVersion.NTCheckVersionReqBean;
import com.yang.net2request.checkVersion.NTCheckVersionRespBean;
import com.yang.netokhttp2.LoadingAdapter;
import com.yang.netokhttp3.RetrofitHttp;
import com.yang.netokhttp3.RxResultHelper;
import com.yang.utils.SharedprefUtil;

/**
 * @Author: he.zhao
 * @Date:on 2017/1/11.
 * @E-mail:377855879@qq.com
 */

public class SplashActivity extends BaseActivity {
    private static final int GO_HOME = 1000;
    private static final int GO_GUIDE = 1001;
    private static final int IN_STALL = 200;
    private int version = 0;
    private File file = null;
    public static final int PERMISSIONS_REQUEST = 1000;
    // 延迟1秒
    private static final long SPLASH_DELAY_MILLIS = 1000;
    /**
     * Handler:跳转到不同界面
     */
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GO_HOME:
                    goHome();
                    break;
                case GO_GUIDE:
                    goGuide();
                    break;
                case IN_STALL:
                    installApk(file);
                    break;
            }

            super.handleMessage(msg);
        }

    };


    private void goGuide() {
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        finish();
    }

    private void goHome() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public IPresenter getPresenter() {
        return null;
    }

    @Override
    public int forceSetLayoutFirst() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoginRequstBean requstBean = new LoginRequstBean();
        requstBean.setMobile("+86109");
        requstBean.setPassword("123456");
        RetrofitHttp.getInstance().login(requstBean)
                .compose(RxResultHelper.transformer)
                .compose(RxResultHelper.handleRespbeanResult())
                .subscribe(response -> {
                    System.out.println(response instanceof TestRespBean);
                    TestRespBean bean = (TestRespBean)response;
                    System.out.println("bean id : "+bean.getId());
                    System.out.println(response.toString());
                },throwable -> {
                    System.out.println(throwable.toString());
                });

//        try {
//            PackageInfo packInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
//            version = packInfo.versionCode;
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
//                            Manifest.permission.ACCESS_COARSE_LOCATION,
//                            Manifest.permission.READ_EXTERNAL_STORAGE,
//                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    },
//                    PERMISSIONS_REQUEST);
//        } else {
//            retrofitCheckVersion();
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    retrofitCheckVersion();
                } else {
                    // 申请权限被拒
                    Toast.makeText(this, "Please Accept The Permissions", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

    /**
     * 判断是去主页面还是登陆页面
     */
    private void booleanToMain() {
        if (!TextUtils.isEmpty(SharedprefUtil.getInstance(this).get("token", ""))) {
            Staff staff = new Staff();
            staff.setCompanyId(Integer.parseInt(SharedprefUtil.getInstance(this).get("companyId", "")));
            staff.setCompanyName(SharedprefUtil.getInstance(this).get("companyName", ""));
            staff.setEmail(SharedprefUtil.getInstance(this).get("email", ""));
            staff.setFirstname(SharedprefUtil.getInstance(this).get("firstName", ""));
            staff.setIsAlert(Integer.parseInt(SharedprefUtil.getInstance(this).get("isAlert", "")));
            staff.setLastname(SharedprefUtil.getInstance(this).get("lastName", ""));
            staff.setPhone(SharedprefUtil.getInstance(this).get("phone", ""));
            staff.setPhotoUrl(SharedprefUtil.getInstance(this).get("photoUrl", ""));
            staff.setStaffId(SharedprefUtil.getInstance(this).get("staffId", ""));
            staff.setSurname(SharedprefUtil.getInstance(this).get("surname", ""));
            staff.setToken(SharedprefUtil.getInstance(this).get("token", ""));
            staff.setUid(Integer.parseInt(SharedprefUtil.getInstance(this).get("uid", "")));
            staff.setUsername(SharedprefUtil.getInstance(this).get("username", ""));
            App.app.setLoginUser(staff);
            EventBus.getDefault().postSticky(staff);//post给MainActivity的onEventLogin方法
            mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
        } else {
            mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
        }
    }

    void retrofitCheckVersion() {
        RetrofitHttp.getInstance().checkVersion("2")
                .compose(RxResultHelper.handleRespbeanResult())
                .compose(RxResultHelper.transformer)
                .subscribe(response -> {
                            System.out.println(response.toString());
                            Gson gson = new Gson();
                            NTCheckVersionRespBean.Data data = gson.fromJson(gson.toJson(response.getResp()), NTCheckVersionRespBean.Data.class);
                            if (data.getVersion() > version) {
                                if (TextUtils.isEmpty(data.getUrl())) {
                                    Toast.makeText(SplashActivity.this, "Please check the url", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                show(data.getUrl());
                            } else {
                                booleanToMain();
                            }
                        },
                        throwable -> {
                            booleanToMain();
                        });
    }

    /**
     * 检查版本
     */
    private void checkVersion() {
        NTCheckVersionReqBean reqBean = new NTCheckVersionReqBean();
        reqBean.setDevicesId("2");
//        reqBean.setDevicesId("放大镜看发动机阿克苏");
        CheckVersionService service = new CheckVersionService("checkVersion");//单独通过okHttp进行网络访问的框架，全部由个人单独设计完成，框架清晰明了。
        service.setParam(reqBean);
        service.callBack(new LoadingAdapter(this, NTCheckVersionRespBean.class) {
            @Override
            public void onRespbean(Object o) {
                super.onRespbean(o);
                NTCheckVersionRespBean respBean = (NTCheckVersionRespBean) o;
                if (respBean != null) {
                    //获得版本号和服务器返回的版本号做比较
                    if (respBean.getData().getVersion() > version) {
                        if (TextUtils.isEmpty(respBean.getData().getUrl())) {
                            Toast.makeText(SplashActivity.this, "Please check the url", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        show(respBean.getData().getUrl());
                    } else {
                        booleanToMain();
                    }
                }
            }

            @Override
            public void onFail(Object o) {
                super.onFail(o);
                if (o instanceof NTRespBean) {
                    NTRespBean bean = (NTRespBean) o;
                    Logger.e("checkVerisonRespBean:" + bean.getMsg());
                }
                booleanToMain();
            }
        });
        service.enqueue();
    }

    private void show(final String url) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("");

            builder.setTitle("The latest version is updated");
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    booleanToMain();
                }
            });
            builder.setPositiveButton("sure",
                    new AlertDialog.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            dialog.dismiss();// 把窗口关闭
                            if (!TextUtils.isEmpty(url)) {
                                downLoadApk(url);
                            }

                        }

                    });
            builder.create().show();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    /**
     * 下载app
     *
     * @param url
     */
    private void downLoadApk(String url) {
        // TODO Auto-generated method stub
        final ProgressDialog pd; // 进度条对话框
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("downLoading");
        pd.show();
        new Thread() {

            @Override
            public void run() {
                try {
                    file = getFileFromServer(url, pd);
                    // / sleep(3000);

                    pd.dismiss(); // 结束掉进度条对话框
                    mHandler.sendEmptyMessage(IN_STALL);
                } catch (Exception e) {
                    Message msg = new Message();
                    /*
                     * msg.what = DOWN_ERROR; m.sendMessage(msg);
					 */
                    e.printStackTrace();
                }
            }
        }.start();

    }

    public static File getFileFromServer(String url1, ProgressDialog pd)
            throws Exception {
        // 如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            URL url = new URL(url1);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            // 获取到文件的大小
            pd.setMax(conn.getContentLength());
            InputStream is = conn.getInputStream();
            File file = new File(Environment.getExternalStorageDirectory(),
                    "moverlogic.apk");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            int total = 0;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
                // 获取当前下载量
                pd.setProgress(total);
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        } else {
            return null;
        }
    }

    // 安装apk
    protected void installApk(File file) {
        Intent intent = new Intent();
        // 执行动作
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileProvider", file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        startActivity(intent);
    }

}
