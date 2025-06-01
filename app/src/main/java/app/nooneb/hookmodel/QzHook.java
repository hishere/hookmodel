package app.nooneb.hookmodel;

import android.app.Activity;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;

public class QzHook implements IXposedHookLoadPackage {

    private static final String TARGET_PACKAGE = "com.sy.xkqz.tap";
    private static final String TAG = "ResumeHook";
    // 添加文件路径常量
    private static final String TARGET_FILE = "/storage/emulated/0/Android/data/com.sy.xkqz.tap/filesConfig/SaveData/Save0.Save";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (lpparam.packageName.equals(TARGET_PACKAGE)) {
            
            XposedHelpers.findAndHookMethod(
                Activity.class,
                "onResume",
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        Activity activity = (Activity) param.thisObject;
                        
                        String logMsg = String.format(
                            "----------onResume triggered in %s at %d",
                            activity.getClass().getSimpleName(),
                            System.currentTimeMillis()
                        );
                        XposedBridge.log(logMsg);
                        
                        // 新增文件读取功能
                        readAndLogFile();
                    }
                }
            );
        }
    }

    /** 读取目标文件前100字符并输出日志 */
    private void readAndLogFile() {
        try {
            File file = new File(TARGET_FILE);
            
            if (!file.exists()) {
                XposedBridge.log("File not found: " + TARGET_FILE);
                return;
            }
            
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                char[] buffer = new char[100];
                int bytesRead = reader.read(buffer, 0, 100);
                
                if (bytesRead > 0) {
                    String content = new String(buffer, 0, bytesRead);
                    XposedBridge.log("File content (first 100 chars): " + content);
                } else {
                    XposedBridge.log("File is empty");
                }
            }
        } catch (Exception e) {
            XposedBridge.log("Error reading file: " + e.getMessage());
        }
    }
}
