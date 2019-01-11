package com.yang.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import java.lang.reflect.Type;
import java.util.Date;


public class GsonUtil {
    private static Gson sGson;

    public static Gson getGson() {
        if (sGson == null) {
            synchronized (GsonUtil.class) {
                if (sGson == null) {
                    GsonBuilder builder = new GsonBuilder();
                    builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                            return DateTime.parse(json.getAsString(), ISODateTimeFormat.dateTimeParser()).toDate();
                        }
                    });
                    builder.registerTypeAdapter(Date.class, new JsonSerializer<Date>() {
                        @Override
                        public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
                            return new JsonPrimitive(new DateTime(src).toString());
                        }
                    });
                    sGson = builder.create();
                }
            }
        }
        return sGson;
    }
}
