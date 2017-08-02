package com.example.tim.audiotranslator.tools;

import android.content.Context;
import android.widget.Toast;
import com.example.tim.audiotranslator.R;

/**
 * Created by TIM on 28.06.2017.
 */

public class Utils {

    public static void showToastNoInternet(Context context){
        Toast.makeText(context, context.getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
    }
}
