package com.example.xptmx.myapp1;

import android.content.Intent;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.util.Locale;
import static android.speech.tts.TextToSpeech.ERROR;

public class WarningPage extends AppCompatActivity {

    private TextToSpeech tts;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning_page);

        Intent intent = getIntent();
        final String msg = intent.getStringExtra("msg");

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != ERROR) {
                    // 언어를 선택한다.
                    tts.setLanguage(Locale.KOREAN);
                }
            }
        });

        ImageView rabbit = (ImageView) findViewById(R.id.gif_image);
        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(rabbit);
        Glide.with(this).load(R.drawable.warning6).into(gifImage);


        tts.speak(msg, TextToSpeech.QUEUE_FLUSH, null);


        final ImageButton lightbutton = (ImageButton) findViewById(R.id.lightbutton);
        lightbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = 1 - i;
                if (i == 1) {
                    lightbutton.setImageResource(R.drawable.light_offbutton);
                    UtilFlash.flash_on();
                } else {
                    lightbutton.setImageResource(R.drawable.light_onbutton);
                    UtilFlash.flash_off();
                }

            }
        });

        //
        ImageButton homegobutton = (ImageButton) findViewById(R.id.homegobutton);
        homegobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        ImageButton shelfindbutton = (ImageButton) findViewById(R.id.shelfindbutton);
        shelfindbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Shelter.class);
                startActivity(intent);
            }
        });


        TextView tv = (TextView) findViewById(R.id.textView_warning);
        tv.setText(intent.getStringExtra("msg") + "\n" + intent.getStringExtra("create_date"));
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts.speak(msg, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }

    protected void onDestroy() {
        super.onDestroy();
        // TTS 객체가 남아있다면 실행을 중지하고 메모리에서 제거한다.
        if (tts != null) {
            tts.stop();
            tts.shutdown();
            tts = null;
        }
    }
}