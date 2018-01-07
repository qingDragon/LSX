package com.big.yanzhuang.lsx;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import android.Manifest;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mNumberText;
    private EditText mContentText;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.app_name));
        setContentView(R.layout.activity_main);

        mNumberText = (EditText) findViewById(R.id.number);
        mContentText = (EditText) findViewById(R.id.msg_content);
        findViewById(R.id.send).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, 1);
        }
        else {
                sendMsg();

        }
        
    }

    private void sendMsg() {

            String number = mNumberText.getText().toString();

            String content = mContentText.getText().toString();
        try{
            if (TextUtils.isEmpty(number)) {
                showToast("请输入手机号");
                return;
            }
            if (TextUtils.isEmpty(content)) {
                showToast("请输入内容");
                return;
            }
            ArrayList<String> messages = SmsManager.getDefault().divideMessage(content);
            for (String text : messages) {
                SmsManager.getDefault().sendTextMessage(number, null, text, null, null);
            }
            Log.d("MainActivity", "1");
            showToast("Success!");
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults) {
        switch(requestCode) {
            case 1:
                if(grantResults.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    sendMsg();
                } else {
                    Toast.makeText(this,"您没有此权限！",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    private void showToast(String msg){
        Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
    }
}
