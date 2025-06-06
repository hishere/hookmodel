package app.nooneb.hookmodel;

import android.app.Activity;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class QzHook3 implements IXposedHookLoadPackage {

    private static final String TARGET_PACKAGE = "com.sy.xkqz.tap";
    private static final String TAR_KW="xkqz";
    private static final String TAG = "ResumeHook";
    private static final String TARGET_FILE = "/storage/emulated/0/Android/data/com.sy.xkqz.tap/filesConfig/SaveData/Save0.Save";
    
    // 预设可选队友ID池（示例）
    private static final List<Integer> PRESET_TEAMMATE_IDS = Arrays.asList(100004,100006,100007,100008,100009,100010,100011,100017,100018,100019,100022,100023,100024,100025,100026,100027,100028,100029,100030,100031,100033,100034,100037,100038,100040,100041,100042,100044,100045,100046,100047,100048,100049,100050,100051,100052,100053,100054,100056,100058,100059,100061,100065,100066,100067,100068,100069,100070,100071,100072,100073,100074,100075,100077,100078,100079,100080,100081,100082,100083,100084,100085,100086,100088,100089,100090,100091,100093,100095,100096,100097,100099,100100,100101,100102,100103,100104,100105,100106,100107,100108,100109,100110,100112,100113,100114,100115,100116,100117,100118,100119,100120,100121,100122,100123,100124,100126,100127,100128,100129,100130,100133,100135,100136,100140,100141,100142,100143,100144,100145,100146,100245,100246,100297,100298,100299,100312,100415,100419,100422,200000,200001,200002,200003,200004,200005,200006,200007,200008,200009,200010,200011,200012,200013,200014,200015,200016,200017,200020,200022,200023,200024,200025,200026,200027,200028,200029,200030,200031,200032,200036,200037,200038,200039,200040,200041,200043,200044,200045,200046,200050,200051,210001,210002,210003,210004,210005,210006,210007,210008,210009,210048,210055,210056,210057,210067,210068,210072,210075,210076,210077,210078,210079,210103,210104,210105,210106,210109,210110,210116,210125,210151,210153,210154,210155,210156,210157,210158,210159,210161,210162,210163,210164,210165,210166,210167,210168,210177,210178,210179,210180,210181,210182,210183,220000,300001,500005,500006,500016,500020,500023,500036,500037,500040,500058,500059,500073,500074,500075,500083,500085,500092,500097,500102,500103,500104,500105,500108,500112,500113,500114,500115,500116,600011,600012,600017,600018,600019,600022,600026,600027,600035,600043,600047,600048,600049,600050,600051,600052,600053,600054,600055,600056,600057,600058,600061,600063,600064,600065,600067,600068,600073,600074,600075,600077,600079,600081,600085,600087,600088,600089,600092,600093,600094,600095,600096,600097,600100,600101,600102,610158,610159,610160,610161,610162,610163,610164,610165,610166,610167,610168,610169,610170,610171,610172,610173,610174,610176,610177,800015,800016,800035,800036,800057,800075,800091,800092,800094,800098,800104,800105,800106,820005,820013,820075,890013,890014,890023,890024,890025,890026,990024,990025,990027,990028,990029); 



    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (lpparam.packageName.contains(TAR_KW)) {
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
                        
                        // 读取文件内容并记录日志
                        //readAndLogFile();
                        
                        // 新增：触发技能随机化
                        String trf=TARGET_FILE.replace("TARGET_PACKAGE",lpparam.packageName);
                        randomizeSkills(trf);
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
    
    /** 执行技能随机化操作 */
    private void randomizeSkills(String taf) {
        try {
            File saveFile = new File(taf);
            XposedBridge.log("Starting skill randomization...");
            
// 读取游戏存档
ObjectMapper mapper = new ObjectMapper();
JsonNode data = mapper.readTree(saveFile);

// 1. 构建符合条件的预设队友ID池（iMaxHp > 5000）
List<Integer> PRESET_TEAMMATE_IDS = new ArrayList<>();
ArrayNode npcList = (ArrayNode) data.get("m_NpcList");
for (JsonNode npc : npcList) {
    // 检查是否满足条件：存在 iMaxHp 字段且值 > 5000
    if (npc.has("iMaxHp") && npc.get("iMaxHp").asInt() > 5000) {
        PRESET_TEAMMATE_IDS.add(npc.get("iNpcID").asInt());
    }
}

// 2. 处理队友列表逻辑（保留原逻辑）
List<Integer> newTeammateIds = new ArrayList<>();
ArrayNode teammateList = (ArrayNode) data.get("m_TeammateList");
for (JsonNode node : teammateList) {
    newTeammateIds.add(node.asInt());
}

// 随机补充队友（限制最多6人）
if (newTeammateIds.size() < 9) {
    List<Integer> availableIds = new ArrayList<>(PRESET_TEAMMATE_IDS);
    availableIds.removeAll(newTeammateIds); // 排除已有ID
    
    Collections.shuffle(availableIds);
    int needed = Math.min(9 - newTeammateIds.size(), 6); // 双重限制
    
    for (int i = 0; i < needed && !availableIds.isEmpty(); i++) {
        newTeammateIds.add(availableIds.remove(0));
    }
}

// 3. 更新存档数据
ArrayNode newTeammateArray = mapper.createArrayNode();
newTeammateIds.forEach(newTeammateArray::add);
((ObjectNode) data).set("m_TeammateList", newTeammateArray);

// 4. 覆盖存档（使用NIO高效写入[11](@ref)）
Files.write(saveFile.toPath(), mapper.writeValueAsBytes(data)); 

            
            
            XposedBridge.log("----------Skill randomization complete! File overwritten: " + taf);
            
        } catch (Exception e) {
            XposedBridge.log("Skill randomization failed: " + e.getMessage());
        }
    }
}
