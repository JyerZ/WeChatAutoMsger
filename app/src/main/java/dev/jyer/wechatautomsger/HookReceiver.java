package dev.jyer.wechatautomsger;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

public class HookReceiver extends BroadcastReceiver {

    private HookHelper helper;
    private Activity activity;

    public HookReceiver(HookHelper helper, Activity activity){
        this.helper = helper;
        this.activity = activity;
        this.helper.sendAutoMsgInit(this.activity);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String wechatId = intent.getStringExtra("wechatId");
        int type = intent.getIntExtra("type",0);
        boolean result = false;
        switch (type){
            case 0:
                {
                    String content = intent.getStringExtra("content");
                    result = helper.sendAutoMsg(wechatId,content);
                }
                break;
            case 1:
                {
                    ArrayList files = (ArrayList)intent.getSerializableExtra("pictures");
                    result = helper.sendPictureAutoMsg(wechatId,files);
                }

                break;
            case 2:
                {
                    String content = intent.getStringExtra("content");
                    ArrayList files = (ArrayList)intent.getSerializableExtra("pictures");
                    result = helper.sendSnsAutoMsg(content,files);
                }
                break;
            default:
                break;
        }
        Intent replyIntent = new Intent("dev.jyer.wechat.sendResult");
        replyIntent.putExtra("result", result);
        activity.sendBroadcast(replyIntent);
    }
}
