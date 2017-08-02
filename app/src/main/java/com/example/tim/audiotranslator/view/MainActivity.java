package com.example.tim.audiotranslator.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tim.audiotranslator.R;
import com.example.tim.audiotranslator.presenter.ITranslationPresenter;
import com.example.tim.audiotranslator.presenter.TranslationPresenterImpl;
import com.google.gson.Gson;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etPutText;
    private TextView tvTranslation;
    private ITranslationPresenter mTranslationPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (mTranslationPresenter == null) {
            mTranslationPresenter = new TranslationPresenterImpl(this);
        }
        init();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_translate:

                String str = etPutText.getText().toString();

                if (!str.isEmpty()) {
                    mTranslationPresenter.getTranslation(str);
                } else {
                    Toast.makeText(this, getResources().getString(R.string.fill_edit_text), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_play:
                mTranslationPresenter.speechTranslation(tvTranslation.getText().toString());
                break;
            case R.id.btn_save:
                mTranslationPresenter.saveSpeechTranslation(tvTranslation.getText().toString());
                break;
        }
    }

    public void setTranslation(final Object object) {
        Gson gson = new Gson();
        Map map = gson.fromJson(object.toString(), Map.class);
        String translation = map.get("text").toString();
        translation = translation.substring(1, translation.length() - 1);
        tvTranslation.setText(translation);
    }

    private void init() {

        etPutText = (EditText) findViewById(R.id.et_put_text);
        tvTranslation = (TextView) findViewById(R.id.tv_translation);
        Button bTranslate = (Button) findViewById(R.id.btn_translate);
        bTranslate.setOnClickListener(this);
        Button bPlay = (Button) findViewById(R.id.btn_play);
        bPlay.setOnClickListener(this);
        Button bSave = (Button) findViewById(R.id.btn_save);
        bSave.setOnClickListener(this);
    }

    public void showToastError(String error){
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mTranslationPresenter.onStop();
    }
}
