package com.example.tim.audiotranslator.presenter;

import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import com.example.tim.audiotranslator.R;
import com.example.tim.audiotranslator.model.Model;
import com.example.tim.audiotranslator.tools.InternetConnection;
import com.example.tim.audiotranslator.tools.Utils;
import com.example.tim.audiotranslator.view.MainActivity;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.example.tim.audiotranslator.tools.Config.API_KEY;

/**
 * Created by TIM on 30.07.2017.
 */

public class TranslationPresenterImpl implements ITranslationPresenter {

    private Subscription subscription;
    private MainActivity activity;
    private Model mModel;
    private TextToSpeech tts;

    public TranslationPresenterImpl(MainActivity activity) {
        this.activity = activity;
        mModel = new Model();
    }

    @Override
    public void getTranslation(String textToTranslation) {

        boolean isConnected = InternetConnection.internetConnectionChecking(activity);
        if (isConnected) {
            subscription = mModel.getTranslation(mapForming(textToTranslation))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(activity::setTranslation, (Throwable e) -> activity.showToastError(e.toString()));
        } else Utils.showToastNoInternet(activity);
    }

    @Override
    public void speechTranslation(final String translation) {
        tts = new TextToSpeech(activity, status -> {
            if(status == TextToSpeech.SUCCESS){
                int result = tts.setLanguage(Locale.GERMAN);
                if(!(result==TextToSpeech.LANG_MISSING_DATA ||
                        result==TextToSpeech.LANG_NOT_SUPPORTED)){
                    if(!(translation == null || "".equals(translation)))
                    {
                        tts.speak(translation, TextToSpeech.QUEUE_FLUSH, null);
                    }else
                        Toast.makeText(activity, "Please, translate the text", Toast.LENGTH_SHORT).show();
                        Log.e("error", "TextView Translation is empty");
                }
                else{
                    Toast.makeText(activity, "This Language is not supported", Toast.LENGTH_SHORT).show();
                    Log.e("error", "This Language is not supported");
                }
            }
            else
                Toast.makeText(activity, "Initialization Failed", Toast.LENGTH_SHORT).show();
                Log.e("error", "Initialization Failed!");
        });
        tts.setLanguage(Locale.GERMAN);
    }

    @Override
    public void saveSpeechTranslation(String saveTranslation) {

        if(!(saveTranslation == null || "".equals(saveTranslation))){

            tts = new TextToSpeech(activity, status -> {
                if(status == TextToSpeech.SUCCESS){
                    int result = tts.setLanguage(Locale.GERMAN);
                    if(!(result == TextToSpeech.LANG_MISSING_DATA ||
                            result == TextToSpeech.LANG_NOT_SUPPORTED)){

                        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                                activity.getResources().getString(R.string.folder));

                        Log.d("fileName", f.getAbsolutePath());
                        if (!f.exists()) {
                            Log.d("fileName", f.getAbsolutePath());
                            boolean v = f.mkdirs();
                        }

                        HashMap<String, String> myHashRender = new HashMap<>();
                        String soundName = String.valueOf(System.currentTimeMillis() % 100000) + "_" +
                                saveTranslation + ".wav";
                        Log.d("soundName", soundName);
                        String destFileName = f.getAbsolutePath() + File.separator + soundName;

                        myHashRender.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, saveTranslation);
                        int ttsResponse = tts.synthesizeToFile(saveTranslation, myHashRender, destFileName);
                        if (ttsResponse == 0){
                            Toast.makeText(activity, "Saved Successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(activity, "Error during saving", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(activity, "This Language is not supported", Toast.LENGTH_SHORT).show();
                        Log.e("error", "This Language is not supported");
                    }
                }
                else
                    Toast.makeText(activity, "Initialization Failed", Toast.LENGTH_SHORT).show();
                Log.e("error", "Initialization Failed!");
            });
        } else {
            Toast.makeText(activity, "Please, translate the text", Toast.LENGTH_SHORT).show();
            Log.e("error", "TextView Translation is empty");
        }
    }

    @Override
    public Map<String, String> mapForming(String textToTranslation) {

        Map<String, String> mapRequest = new HashMap<>();
        mapRequest.put("key", API_KEY);
        mapRequest.put("text", "\"" + textToTranslation + "\"");
        mapRequest.put("lang", "en-de");
        return mapRequest;
    }

    @Override
    public void onStop() {
        if (subscription != null) {
            if (!subscription.isUnsubscribed())
                subscription.unsubscribe();
        }
    }
}
