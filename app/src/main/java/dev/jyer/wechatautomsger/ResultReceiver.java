package dev.jyer.wechatautomsger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ResultReceiver extends BroadcastReceiver {

    public ResultReceiver(){

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        showRes(context,intent.getBooleanExtra("result",false));
    }

    private void showRes(Context context, boolean res){
        String resStr;
        if (res){
            resStr = "发送请求成功发送";
        }else{
            resStr = "发送请求发送失败";
        }
        Toast.makeText(context,resStr,Toast.LENGTH_LONG).show();
    }
}
