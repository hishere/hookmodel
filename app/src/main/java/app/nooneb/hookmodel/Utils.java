package cn.kaicity.apps.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

public class Utils {
    
    public static String getPermission(String filePath) {
        Process process = null;
        DataOutputStream outputStream = null;
        StringBuilder result = new StringBuilder();
        
        try {
            // 获取root权限[3,4](@ref)
            process = Runtime.getRuntime().exec("su");
            outputStream = new DataOutputStream(process.getOutputStream());
            
            // 构建shell命令：读取指定文件[1,8](@ref)
            String command = "cat " + filePath + "\n";
            outputStream.writeBytes(command);
            outputStream.writeBytes("exit\n");
            outputStream.flush();
            
            // 读取命令输出流[7,8](@ref)
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }
            
            // 等待命令执行完成[4,5](@ref)
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                return "";  // 执行失败返回空
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            // 资源清理[6,8](@ref)
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (process != null) {
                    process.destroy();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result.toString();
    }
}
