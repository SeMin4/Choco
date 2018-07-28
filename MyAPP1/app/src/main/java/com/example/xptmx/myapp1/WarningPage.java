package com.example.xptmx.myapp1;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;
import static android.speech.tts.TextToSpeech.ERROR;

public class WarningPage extends AppCompatActivity {

    private TextToSpeech tts;

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

        Button btn = (Button) findViewById(R.id.button_warning);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        tts.speak(msg, TextToSpeech.QUEUE_FLUSH, null);
        Button btn2 = (Button) findViewById(R.id.stop_speek_button);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts.speak(msg, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        TextView tv = (TextView) findViewById(R.id.textView_warning);
        tv.setText(intent.getStringExtra("msg") + "\n" + intent.getStringExtra("create_date"));
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts.speak(msg, TextToSpeech.QUEUE_FLUSH, null);
                //tts.stop();
            }
        });

        Button btn3=(Button)findViewById(R.id.light_off_button);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UtilFlash.flash_off();
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