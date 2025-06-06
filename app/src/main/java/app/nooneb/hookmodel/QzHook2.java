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

public class QzHook2 implements IXposedHookLoadPackage {

    private static final String TARGET_PACKAGE = "com.sy.xkqz.tap";
    private static final String TAR_KW="xkqz";
    private static final String TAG = "ResumeHook";
    private static final String TARGET_FILE = "/storage/emulated/0/Android/data/com.sy.xkqz.tap/filesConfig/SaveData/Save0.Save";
    
    // 预设可选队友ID池（示例）
    private static final List<Integer> PRESET_TEAMMATE_IDS = Arrays.asList(100001,100002,100003,100004,100005,100006,100007,100008,100009,100010,100011,100012,100013,100014,100015,100016,100017,100018,100019,100020,100021,100022,100023,100024,100025,100026,100027,100028,100029,100030,100031,100032,100033,100034,100035,100036,100037,100038,100039,100040,100041,100042,100043,100044,100045,100046,100047,100048,100049,100050,100051,100052,100053,100054,100055,100056,100057,100058,100059,100060,100061,100062,100063,100064,100065,100066,100067,100068,100069,100070,100071,100072,100073,100074,100075,100076,100077,100078,100079,100080,100081,100082,100083,100084,100085,100086,100087,100088,100089,100090,100091,100092,100093,100094,100095,100096,100097,100098,100099,100100,100101,100102,100103,100104,100105,100106,100107,100108,100109,100110,100111,100112,100113,100114,100115,100116,100117,100118,100119,100120,100121,100122,100123,100124,100125,100126,100127,100128,100129,100130,100131,100132,100133,100134,100135,100136,100137,100138,100139,100140,100141,100142,100143,100144,100145,100146,100147,100148,100149,100150,100151,100152,100153,100154,100155,100156,100157,100158,100159,100160,100161,100162,100163,100164,100166,100167,100168,100169,100170,100173,100174,100189,100191,100194,100195,100196,100197,100198,100199,100201,100206,100209,100211,100212,100225,100231,100233,100293,100244,100245,100246,100297,100298,100299,100312,100314,100315,100401,100403,100412,100415,100418,100419,100422,110001,110015,199999,200000,200001,200002,200003,200004,200005,200006,200007,200008,200009,200010,200011,200012,200013,200014,200015,200016,200017,200018,200019,200020,200021,200022,200023,200024,200025,200026,200027,200028,200029,200030,200031,200032,200033,200034,200035,200036,200037,200038,200039,200040,200041,200042,200043,200044,200045,200046,200047,200048,200049,200050,200051,210001,210002,210003,210004,210005,210006,210007,210008,210009,210048,210049,210050,210051,210052,210053,210054,210055,210056,210057,210058,210059,210060,210061,210062,210063,210064,210065,210066,210067,210068,210069,210070,210071,210072,210073,210074,210075,210076,210077,210078,210079,210080,210081,210082,210083,210084,210085,210086,210087,210088,210089,210090,210091,210092,210093,210094,210095,210096,210097,210098,210099,210100,210101,210102,210103,210104,210105,210106,210107,210108,210109,210110,210111,210112,210113,210114,210115,210116,210117,210118,210119,210120,210121,210122,210123,210124,210125,210126,210127,210128,210129,210130,210131,210132,210133,210134,210135,210136,210137,210138,210139,210140,210141,210142,210143,210144,210145,210146,210147,210148,210149,210150,210151,210152,210153,210154,210155,210156,210157,210158,210159,210160,210161,210162,210163,210164,210165,210166,210167,210168,210169,210170,210171,210172,210173,210174,210175,210176,210177,210178,210179,210180,210181,210182,210183,210184,210185,220000,220001,300001,300002,300003,990023,990024,990025,990026,990027,990028,990029,990030,990031,500001,500002,500003,500004,500005,500006,500007,500008,500009,500010,500011,500012,500014,500015,500016,500017,500018,500019,500020,500021,500022,500023,500024,500025,500026,500027,500028,500029,500030,500031,500032,500033,500034,500035,500036,500037,500038,500039,500040,500041,500042,500043,500044,500045,500046,500047,500048,500049,500050,500051,500052,500053,500054,500055,500056,500057,500058,500059,500060,500061,500062,500063,500064,500065,500066,500067,500068,500069,500070,500071,500072,500073,500074,500075,500076,500077,500078,500079,500080,500081,500082,500083,500084,500085,500086,500087,500088,500089,500090,500091,500092,500093,500094,500095,500096,500097,500098,500099,500100,500101,500102,500103,500104,500105,500106,500107,500108,500109,500110,500111,500112,500113,500114,500115,500116,600001,600002,600003,600004,600005,600006,600007,600008,600009,600010,600011,600012,600013,600014,600015,600016,600017,600018,600019,600020,600021,600022,600023,600024,600025,600026,600027,600028,600029,600030,600031,600032,600033,600034,600035,600036,600037,600038,600039,600040,600041,600042,600043,600044,600045,600046,600047,600048,600049,600050,600051,600052,600053,600054,600055,600056,600057,600058,600059,600060,600061,600062,600063,600064,600065,600066,600067,600068,600069,600070,600071,600072,600073,600074,600075,600076,600077,600078,600079,600080,600081,600082,600083,600084,600085,910012,600086,600087,600088,600089,600090,600091,600092,600093,600094,600095,600096,600097,600098,600099,600100,600101,600102,610158,610159,610160,610161,610162,610163,610164,610165,610166,610167,610168,610169,610170,610171,610172,610173,610174,610175,800015,800016,800035,800036,800037,800054,800057,800059,800066,800075,800089,800091,800092,800094,800098,800104,800105,800106,820005,820013,820075,890023,890024,890025,890026,890013,890014,610176,610177,610178,610179,700024,700025,700027); 



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

            // 获取队友ID列表（改用List保持顺序）
            List<Integer> teammateIds = new ArrayList<>();
            ArrayNode teammateList = (ArrayNode) data.get("m_TeammateList");
            for (JsonNode node : teammateList) {
                teammateIds.add(node.asInt());
            }

            //在写入存档前
            // 在写入存档前的最后一步操作 m_TeammateList
           
            int currentCount = teammateList.size();
            
            // 1. 保留原有队友ID
            List<Integer> newTeammateIds = new ArrayList<>();
            for (JsonNode node : teammateList) {
                newTeammateIds.add(node.asInt());
            }
            
            // 2. 随机补充新队友（直到9人）
            if (newTeammateIds.size() < 9) {
                // 创建可用的预设ID池（排除已有ID）
                List<Integer> availableIds = new ArrayList<>(PRESET_TEAMMATE_IDS);
                availableIds.removeAll(newTeammateIds); // 移除已有ID
                
                // 随机打乱可用ID池[1,3](@ref)
                Collections.shuffle(availableIds); 
                
                // 补充所需数量的随机队友
                int needed = 9 - newTeammateIds.size();
                for (int i = 0; i < needed && !availableIds.isEmpty(); i++) {
                    newTeammateIds.add(availableIds.remove(0)); // 取打乱后的第一个ID
                }
            }
            
            // 3. 重建队友列表数组
            ArrayNode newTeammateArray = mapper.createArrayNode();
            for (int id : newTeammateIds) {
                newTeammateArray.add(id);
            }
            ((ObjectNode) data).set("m_TeammateList", newTeammateArray);
            
            // 4. 覆盖原始存档（原有逻辑保持不变）
            mapper.writeValue(saveFile, data);

            
            
            XposedBridge.log("----------Skill randomization complete! File overwritten: " + taf);
            
        } catch (Exception e) {
            XposedBridge.log("Skill randomization failed: " + e.getMessage());
        }
    }
}
