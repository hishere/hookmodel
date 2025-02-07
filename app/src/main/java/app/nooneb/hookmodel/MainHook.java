package app.nooneb.hookmodel;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

//        if (lpparam.packageName.contains("com.eg")){
//
//        }
        XposedHelpers.findAndHookMethod(Arrays.class, "asList", Object[].class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                super.beforeHookedMethod(param);
                Object[] objs = (Object[]) param.args[0];
                XposedBridge.log("ob------------------start--");

                if (objs[0] instanceof String){
                    if (objs[0].toString().equals("com.tencent.mm")){
                        ArrayList<String> a=new ArrayList<>();
                        for (Object obj : objs) {
                            a.add(obj.toString());
                        }
                        a.add("com.duowan.kiwi");
                        a.add("com.bb.bb");
                        a.add("com.cc.cc");
                        a.add("com.dd.dd");
                        a.add("com.ee.ee");
                        a.add("com.ff.ff");

                        param.args[0]=a.toArray();
                    }
                }
//                for (int i = 0; i < objs.length; i++) {
//                    if (objs[0] instanceof String){
////                        XposedBridge.log("objjj:"+objs[i].toString());
//                    }
//                }
                XposedBridge.log("ob-------------------end-");

            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Object[] arg = (Object[]) param.args[0];
                XposedBridge.log("aaa"+Arrays.toString(arg));
            }
        });
    }
}
