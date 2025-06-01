package app.nooneb.hookmodel;


import android.app.Activity;
import android.util.Log;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Xposed模块：Hook指定包名的Activity.onResume()并写入日志
 * 包名：com.sy.xkqz.tap
 */
public class QzHook implements IXposedHookLoadPackage {

    private static final String TARGET_PACKAGE = "com.sy.xkqz.tap"; // 目标包名
    private static final String TAG = "ResumeHook"; // 日志标签

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        // 仅处理目标包名
        if (lpparam.packageName.equals(TARGET_PACKAGE)) {
            
        

        // Hook android.app.Activity类的onResume方法
        XposedHelpers.findAndHookMethod(
            Activity.class, // 目标类
            "onResume",    // 目标方法
            new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Activity activity = (Activity) param.thisObject;
                    
                    // 写入日志：包含Activity类名和时间戳
                    String logMsg = String.format(
                        "----------onResume triggered in %s at %d",
                        activity.getClass().getSimpleName(),
                        System.currentTimeMillis()
                    );
                    // 替换 Log.d(TAG, logMsg) 为：
                    XposedBridge.log(logMsg); // 直接输出到Xposed日志系统[6,4](@ref)

                }
            }
        );
        
        }
    }
}
