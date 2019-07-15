package dev.jyer.wechatautomsger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.send);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMsg();
            }
        });
        Button buttonPic = findViewById(R.id.sendPic);
        buttonPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendPicMsg();
            }
        });
        Button buttonSns = findViewById(R.id.sendSns);
        buttonSns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSnsMsg();
            }
        });
    }

    private Intent sendMsgCommon(){
        Intent intent = new Intent("dev.jyer.wechat.sendAutoMsg");
        //写入WeChatId,格式wxid_xxxxxxxxxxxxx,需要是正确的Id，否则发送失败
        intent.putExtra("wechatId", "wxid_xxxxxxxxxxxxx");
        return intent;
    }

    private void sendMsg(){
        Intent intent = sendMsgCommon();
        intent.putExtra("type", 0);
        intent.putExtra("content", "Auto String From Remote");
        this.sendBroadcast(intent);
    }

    private void sendPicMsg(){
        Intent intent = sendMsgCommon();
        intent.putExtra("type", 1);
        ArrayList<String> pics = new ArrayList<>();
        pics.add("/storage/emulated/0/1.jpg");
        intent.putExtra("pictures",pics);
        this.sendBroadcast(intent);
    }

    private void sendSnsMsg(){
        Intent intent = new Intent("dev.jyer.wechat.sendAutoMsg");
        intent.putExtra("type", 2);
        ArrayList<String> pics = new ArrayList<>();
        pics.add("/storage/emulated/0/2.jpg");
        intent.putExtra("content", "Auto Sns Msg");
        intent.putExtra("pictures",pics);
        this.sendBroadcast(intent);
    }
}
