package com.example.tim.audiotranslator.presenter;

import java.util.Map;

/**
 * Created by Tim on 30.07.2017.
 */

public interface ITranslationPresenter {
    void getTranslation(String textToTranslation);
    void speechTranslation (String translation);
    void saveSpeechTranslation (String saveTranslation);
    Map<String, String> mapForming(String textToTranslation);
    void onStop();
}
