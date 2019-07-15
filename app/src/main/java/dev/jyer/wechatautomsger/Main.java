package dev.jyer.wechatautomsger;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;

import java.lang.reflect.Method;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Main implements IXposedHookLoadPackage {

    private HookHelper helper = HookHelper.sharedInstance();

    private Activity activity;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {

        //MutilDexHook
        if (lpparam.packageName.contains("com.tencent.mm")) {
            XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                    final ClassLoader cl = ((Context) param.args[0]).getClassLoader();

                    helper.paramaClass = cl.loadClass(Global.paramaClassStr);
                    helper.paramBaseChattingUIFragmentClass = cl.loadClass(Global.paramBaseChattingUIFragmentClassStr);
                    helper.paramaeClass = cl.loadClass(Global.paramaeClassStr);
                    helper.paramafClass = cl.loadClass(Global.paramafClassStr);
                    helper.paramChatFooterClass = cl.loadClass(Global.paramChatFooterClassStr);
                    helper.paramChattingUIFragmentClass = cl.loadClass(Global.paramChattingUIFragmentClassStr);

                    helper.adClass = cl.loadClass(Global.adClassStr);

                    helper.aiClass = cl.loadClass(Global.aiClassStr);
                    helper.caClass = cl.loadClass(Global.caClassStr);

                    helper.vClass = cl.loadClass(Global.vClassStr);

                    helper.aaClass = cl.loadClass(Global.aaClassStr);
                    helper.cyClass = cl.loadClass(Global.cyClassStr);
                    helper.cqClass = cl.loadClass(Global.cqClassStr);

                    helper.hClass = cl.loadClass(Global.hClassStr);
                    helper.tClass = cl.loadClass(Global.tClassStr);
                    helper.oClass = cl.loadClass(Global.oClassStr);

                    helper.atnClass = cl.loadClass(Global.atnClassStr);
                    helper.sendImgProxyUIClass = cl.loadClass(Global.sendImgProxyUIClassStr);

                    helper.axClass = cl.loadClass(Global.axClassStr);
                    helper.snshClass = cl.loadClass(Global.snshClassStr);

                    /*
                    XposedHelpers.findAndHookMethod("com.tencent.mm.ui.chatting.d.a", cl, "aF", Class.class, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            XposedBridge.log("Hooked aF");
                            Object obj = param.getResult();
                            if (obj!=null){
                                XposedBridge.log(obj.toString());
                            }else{
                                XposedBridge.log("null");
                            }
                        }
                    });
                    */

                    XposedHelpers.findAndHookMethod("com.tencent.mm.ui.LauncherUI", cl,"onCreate", Bundle.class, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) {
                            //XposedBridge.log("Hooked onCreate");
                            activity = (Activity) param.thisObject;
                            HookReceiver receiver = new HookReceiver(helper,activity);
                            IntentFilter filter = new IntentFilter();
                            filter.addAction("dev.jyer.wechat.sendAutoMsg");
                            activity.registerReceiver(receiver,filter);
                        }
                    });

                    //以下4个Hook是为了让使文本消息发送器得以以反射调用的方式成功调用，而不至于出Exception

                    //不能unhook,每次发送文本消息都要调用;其他可考虑unhook
                    XposedHelpers.findAndHookMethod(helper.paramaClass,"qp", boolean.class, new XC_MethodReplacement() {
                        @Override
                        protected Object replaceHookedMethod(MethodHookParam param) {
                            //XposedBridge.log("Hooked qp");
                            return null;
                        }
                    });

                    final Class bClass = cl.loadClass("com.tencent.mm.sdk.b");
                    XposedHelpers.findAndHookMethod(bClass, "dnK", new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            //XposedBridge.log("Hooked dnk()");
                            Object obj = param.thisObject;
                            Class<?> superClass = bClass.getSuperclass();
                            Method method = superClass.getMethod("getLooper");
                            Object looperObj = method.invoke(obj);
                            param.setResult(looperObj);
                        }
                    });

                    XposedHelpers.findAndHookMethod("com.tencent.mm.ui.v", cl,"hu", Context.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) {
                            //XposedBridge.log("Hooked hu()");
                            if (param.args.length<1||param.args[0]==null){
                                param.setResult(null);
                            }
                        }
                    });

                    final Class<?> mmFragmentClass = cl.loadClass("com.tencent.mm.ui.MMFragment");
                    XposedHelpers.findAndHookMethod(mmFragmentClass,"getMMResources", new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            //XposedBridge.log("Hooked getMMResources()");
                            Object obj = param.thisObject;
                            Method method = mmFragmentClass.getDeclaredMethod("thisActivity");
                            Object activityObj = method.invoke(obj);
                            if (activityObj==null){
                                param.setResult(activity.getResources());
                            }
                        }
                    });
                }
            });
        }
    }
}
