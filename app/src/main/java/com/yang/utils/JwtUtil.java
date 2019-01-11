package com.yang.utils;

import android.text.TextUtils;

import com.yang.netokhttp2.HttpConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;


public class JwtUtil {
    public static <T> String encrypt(T t) {
        return Jwts.builder()
                .setPayload(GsonUtil.getGson().toJson(t))
                .signWith(SignatureAlgorithm.HS256, HttpConstants.REGISTER_PASSWORD_KEY.getBytes())

                .compact();
    }

    public static <T> String encrypt(T t, String key) {
        return Jwts.builder()
                .setPayload(GsonUtil.getGson().toJson(t))
                .signWith(SignatureAlgorithm.HS256, key.getBytes())

                .compact();
    }

    public static <T> T isPayloadTrustable(String payload, Class<T> clazz) {
        try {
            if (!TextUtils.isEmpty(payload)) {
                Claims body = Jwts.parser()
                        .setSigningKey(HttpConstants.REGISTER_PASSWORD_KEY.getBytes())
                        .parseClaimsJws(payload)
                        .getBody();
                return GsonUtil.getGson().fromJson(GsonUtil.getGson().toJsonTree(body), clazz);
            }
            return null;
        } catch (SignatureException se) {
            return null;
        }
    }
}
