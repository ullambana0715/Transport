package com.yang.netokhttp3;

import android.content.Context;
import android.util.Log;

import com.yang.App;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/**
 * @Author: he.zhao
 * @Date:on 2017/9/27.
 * @E-mail:377855879@qq.com
 * //证书文件
 */

public class SslContextFactory {
    private static final String CLIENT_TRUST_PASSWORD = "ssic123456";//信任证书密码
    private static final String CLIENT_AGREEMENT = "TLS";//使用协议
    private static final String CLIENT_TRUST_MANAGER = "X509";
    private static final String CLIENT_TRUST_KEYSTORE = "BKS";
    SSLContext sslContext = null;

    public SSLContext getSslSocket(Context context) {
        try {
//取得SSL的SSLContext实例
            sslContext = SSLContext.getInstance(CLIENT_AGREEMENT);
//取得TrustManagerFactory的X509密钥管理器实例
            TrustManagerFactory trustManager = TrustManagerFactory.getInstance(CLIENT_TRUST_MANAGER);
//取得BKS密库实例
            KeyStore tks = KeyStore.getInstance(CLIENT_TRUST_KEYSTORE);
            InputStream is = context.getResources().openRawResource(com.yang.R.raw.server);
            try {
                tks.load(is, CLIENT_TRUST_PASSWORD.toCharArray());
            } finally {
                is.close();
            }
//初始化密钥管理器
            trustManager.init(tks);
//初始化SSLContext
            sslContext.init(null, trustManager.getTrustManagers(), null);
        } catch (Exception e) {
            Log.e("SslContextFactory", e.getMessage());
        }
        return sslContext;
    }

    public static X509Certificate getServerCert(){
        InputStream is = App.app.getResources().openRawResource(com.yang.R.raw.key);
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            X509Certificate x509Certificate = (X509Certificate)certificateFactory.generateCertificate(is);
            return x509Certificate;
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        return null;
    }
}
