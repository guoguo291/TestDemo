package com.landicorp.speechdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.landicorp.speechdemo.utils.SpeechUtils;

public class MainActivity extends Activity implements View.OnClickListener{
    private Button btn_sentence1;
    private SpeechUtils speechUtils;
    private EditText et_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        speechUtils=SpeechUtils.getInstance(MainActivity.this);
        initView();
    }

    private void initView() {
        btn_sentence1 = (Button) findViewById(R.id.btn_sentence1);
        btn_sentence1.setOnClickListener(this);
        et_info = (EditText) findViewById(R.id.et_info);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_sentence1:
                if(speechUtils!=null){
                    Toast.makeText(MainActivity.this,"播放",Toast.LENGTH_SHORT).show();
                    String info=et_info.getText().toString();
                    if(info!=null){
                        speechUtils.speakText(info);
                    }
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if(speechUtils!=null){
            speechUtils.relaseTTS();
        }
        super.onDestroy();
    }
}
