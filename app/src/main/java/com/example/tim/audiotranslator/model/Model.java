package com.example.tim.audiotranslator.model;

import com.example.tim.audiotranslator.model.api.ApiModule;

import java.util.Map;
import rx.Observable;

/**
 * Created by Tim on 30.07.2017.
 */

public class Model {

    public static final String TAG = "Model";

    public Observable<Object> getTranslation(Map<String, String> fields) {
        return ApiModule.getApiInterface().translate(fields);
    }
}
