package com.yang.netokhttp3;


import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import static okhttp3.internal.Util.UTF_8;
final class NewGsonResponseBodyConverter<T> implements  Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    NewGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        System.out.println("Response data before convert:"+ response);

//        JsonReader jsonReader = gson.newJsonReader(value.charStream());
        MediaType mediaType = value.contentType();
        Charset charset = mediaType != null ? mediaType.charset(UTF_8) : UTF_8;
        ByteArrayInputStream bis = new ByteArrayInputStream(response.getBytes());
        InputStreamReader reader = new InputStreamReader(bis, charset);
        JsonReader jsonReader = gson.newJsonReader(reader);
        try{
            return adapter.read(jsonReader);
        }finally {
            value.close();
        }

//        BaseResult re = gson.fromJson(response, BaseResult.class);
//        //关注的重点，自定义响应码中非0的情况，一律抛出ApiException异常。
//        //这样，我们就成功的将该异常交给onError()去处理了。
//        if (re.getCode() != BaseContent.basecode) {
//            value.close();
//            throw new ApiException(re.getCode(), re.getMessage());
//        }
//
//        MediaType mediaType = value.contentType();
//        Charset charset = mediaType != null ? mediaType.charset(UTF_8) : UTF_8;
//        ByteArrayInputStream bis = new ByteArrayInputStream(response.getBytes());
//        InputStreamReader reader = new InputStreamReader(bis, charset);
//        JsonReader jsonReader = gson.newJsonReader(reader);
//        try {
//            return adapter.read(jsonReader);
//        } finally {
//            value.close();
//        }
    }
}