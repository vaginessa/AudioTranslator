package com.example.tim.audiotranslator.model.api;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Tim on 30.07.2017.
 */

public interface ApiInterface {

    @FormUrlEncoded
    @POST("/api/v1.5/tr.json/translate")
    Observable<Object> translate(@FieldMap Map<String, String> fields);
}