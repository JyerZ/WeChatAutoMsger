package dev.jyer.wechatautomsger;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class HookHelper{

    Class<?> paramaClass;
    Class paramBaseChattingUIFragmentClass;
    Class paramaeClass;
    Class paramafClass;
    Class<?> paramChatFooterClass;
    Class paramChattingUIFragmentClass;

    Class<?> adClass;

    Class<?> aiClass;
    Class<?> caClass;

    Class<?> vClass;

    Class<?> aaClass;
    Class cyClass;
    Class cqClass;

    Class hClass;
    Class tClass;
    Class oClass;

    Class<?> atnClass;
    Class<?> sendImgProxyUIClass;

    Class<?> axClass;
    Class<?> snshClass;

    private Object aInstance;

    private Method arqMethod;
    private Method afMethod;
    private Constructor<?> adConstructor;
    private Method aMethod;

    private Method atnaMethod;
    private Method srMethod;
    private Method gMethod;

    private Constructor<?> snshConstructor;
    private Field qFYField;
    private Field qFXField;
    private Field descField;

    private Method ymMethod;
    //private Method axAMethod;
    //private Method arMethod;
    private Method dfMethod;
    private Method dgMethod;
    private Method diMethod;
    private Method dhMethod;
    private Method commitMethod;

    private Object aiClassObj;

    private Object atnClassObj;

    private Object axObject;

    private boolean isAutoMsgInit;

    private volatile static HookHelper helper;

    private HookHelper(){

    }

    static HookHelper sharedInstance(){
        if(helper==null){
            synchronized (HookHelper.class) {
                if (helper == null) {
                    helper = new HookHelper();
                }
            }
        }
        return helper;
    }

    private XC_MethodHook.Unhook getAHooker(){
        //Hook是为了让使文本消息发送器得以以反射调用的方式成功调用，而不至于出Exception
        return XposedHelpers.findAndHookMethod(caClass, "a", helper.paramaClass, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                //XposedBridge.log("Hooked a()");
                Object obj = param.thisObject;
                Field cglField = caClass.getDeclaredField("cgL");
                cglField.setAccessible(true);
                cglField.set(obj,param.args[0]);
                param.setResult(null);
            }
        });
    }

    void sendAutoMsgInit(Activity activity){
        if (!isAutoMsgInit&&paramaClass!=null){
            try {

                XC_MethodHook.Unhook aHooker = getAHooker();

                Constructor<?> aConstructor = paramaClass.getConstructor(paramBaseChattingUIFragmentClass, paramaeClass, paramafClass);
                Object chattingUIFragment = paramChattingUIFragmentClass.newInstance();
                aInstance = aConstructor.newInstance(chattingUIFragment, chattingUIFragment ,chattingUIFragment);

                Method aMethod4a = paramaClass.getDeclaredMethod("a", Class.class, vClass);

                Constructor<?> aaConMethod = aaClass.getConstructor(paramaClass, ListView.class);
                //Constructor listViewCon = ListView.class.getConstructor(Context.class);
                //ListView listView = (ListView)listViewCon.newInstance(activity);//
                Object aaObj = aaConMethod.newInstance(aInstance,null);
                aMethod4a.invoke(aInstance, hClass, aaObj);

                aMethod4a.invoke(aInstance, tClass, cyClass.newInstance());

                Object cqClassObj = cqClass.newInstance();
                Field oorField = cqClass.getDeclaredField("oor");
                oorField.setAccessible(true);
                Constructor<?> chatFooterConstructor = paramChatFooterClass.getConstructor(Context.class, AttributeSet.class);
                Object chatFooter = chatFooterConstructor.newInstance(activity, null);
                oorField.set(cqClassObj,chatFooter);
                aMethod4a.invoke(aInstance, oClass, cqClassObj);

                adConstructor = adClass.getConstructor(String.class);
                afMethod = paramaClass.getMethod("af", adClass);

                aiClassObj = aiClass.newInstance();

                aMethod = aiClass.getMethod("a", paramaClass);
                arqMethod = aiClass.getMethod("arq", String.class);

                atnaMethod = atnClass.getDeclaredMethod("a", ArrayList.class, boolean.class, int.class, int.class, String.class);
                srMethod = atnClass.getMethod("sr",String.class);
                atnClassObj = atnClass.newInstance();

                gMethod = sendImgProxyUIClass.getDeclaredMethod("g",ArrayList.class,ArrayList.class);
                gMethod.setAccessible(true);

                snshConstructor = snshClass.getConstructor(String.class,int.class);
                qFYField = snshClass.getDeclaredField("qFY");
                qFXField = snshClass.getDeclaredField("qFX");
                descField = snshClass.getDeclaredField("desc");

                Constructor<?> axConstructor = axClass.getConstructor(int.class);
                axObject = axConstructor.newInstance(1);
                ymMethod = axClass.getDeclaredMethod("Ym",String.class);
                //axAMethod = axClass.getDeclaredMethod("a",aytClass);
                //arMethod = axClass.getDeclaredMethod("ar",LinkedList.class);
                dfMethod = axClass.getDeclaredMethod("Df",int.class);
                dgMethod = axClass.getDeclaredMethod("Dg",int.class);
                diMethod = axClass.getDeclaredMethod("Di",int.class);
                dhMethod = axClass.getDeclaredMethod("dh", List.class);
                commitMethod = axClass.getDeclaredMethod("commit");

                //及时unHook以免影响微信正常使用
                aHooker.unhook();
                isAutoMsgInit = true;
            }catch (Exception e){
                XposedBridge.log(e);
            }
        }
    }

    boolean sendAutoMsg(String wechatId, String content) {
        XC_MethodHook.Unhook aHooker = getAHooker();
        if (aInstance!=null) {
            try {
                Object ad = adConstructor.newInstance(wechatId);
                afMethod.invoke(aInstance, ad);
                aMethod.invoke(aiClassObj, aInstance);
                arqMethod.invoke(aiClassObj, content);
                //及时unHook以免影响微信正常使用
                aHooker.unhook();
                return true;
            } catch (Exception e) {
                XposedBridge.log(e);
            }
        } else {
            XposedBridge.log("sendAutoMsg: "+"NULL");
        }
        aHooker.unhook();
        return false;
    }

    boolean sendPictureAutoMsg(final String wechatId, final ArrayList picturePaths){
        if (atnClassObj!=null) {
            try {
                atnaMethod.invoke(atnClassObj,picturePaths,false,0,0,wechatId);
                ArrayList picIds = (ArrayList) srMethod.invoke(atnClassObj,wechatId);
                gMethod.invoke(null,picIds,picturePaths);
                return true;
            } catch (Exception e) {
                XposedBridge.log(e);
            }
        } else {
            XposedBridge.log("sendPicAutoMsg: "+"NULL");
        }
        return false;
    }

    boolean sendSnsAutoMsg(String content, ArrayList picturePaths){
        if (axObject!=null) {
            try {
                LinkedList<Object> list = new LinkedList<>();
                for (Object str : picturePaths) {
                    Object snshClassObj = snshConstructor.newInstance(str,2);
                    qFYField.set(snshClassObj,0);
                    qFXField.set(snshClassObj,0);
                    descField.set(snshClassObj,content);
                    list.add(snshClassObj);
                }
                ymMethod.invoke(axObject,content);
                //axAMethod.invoke(axObject,null);
                //arMethod.invoke(axObject,new LinkedList<>());
                dfMethod.invoke(axObject,0);
                dgMethod.invoke(axObject,0);
                diMethod.invoke(axObject,0);
                dhMethod.invoke(axObject,list);
                commitMethod.invoke(axObject);
                return true;
            }catch (Exception e){
                XposedBridge.log(e);
            }
        }else{
            XposedBridge.log("sendSnsAutoMsg: "+"NULL");
        }
        return false;
    }

}
