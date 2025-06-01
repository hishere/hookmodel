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

public class QzHook implements IXposedHookLoadPackage {

    private static final String TARGET_PACKAGE = "com.sy.xkqz.tap";
    private static final String TAG = "ResumeHook";
    private static final String TARGET_FILE = "/storage/emulated/0/Android/data/com.sy.xkqz.tap/filesConfig/SaveData/Save0.Save";
    
    // 技能池定义
    private static final List<Integer> SKILL_POOL = Arrays.asList(100001,100002,100003,100004,100005,100006,100007,100008,100009,100010,100011,100012,100013,100014,100015,100016,100017,100018,100019,100020,100021,100022,100023,100024,100025,100026,100027,100028,100029,100030,100031,100032,100033,100034,100035,100036,100037,100038,100039,100040,100041,100042,100043,100044,100045,100046,100047,100048,100049,100050,100051,100052,100053,100054,100055,100056,100057,100058,100059,100060,100061,100062,100063,100064,100065,100066,100067,100068,100069,100070,100071,100072,100073,100074,100075,100076,100077,100078,100079,100080,100081,100082,100083,100084,100085,100086,100087,100088,100089,100090,100091,100092,100093,100094,100095,100096,100097,100098,100099,100100,100101,100102,100103,100104,100105,100106,100107,100108,100109,100110,100111,100112,100113,100114,100115,100116,100117,100118,100119,100120,100121,100122,100123,100124,100125,100126,100127,100128,100129,100130,100131,100132,100133,100134,100135,100136,100137,100138,100139,100140,100141,100142,100143,100144,100145,100146,100147,100148,100149,100150,100151,100152,100153,100154,100155,100156,100157,100158,100159,100160,100161,100162,100163,100164,100165,100166,100167,100168,100169,100170,100171,100172,100173,100174,100175,100176,100177,100178,100179,100180,100181,100182,100183,100184,100185,100186,100187,100188,100189,100190,100191,100192,100193,100194,100195,100196,100197,100198,100199,100200,100201,100202,100203,100204,100205,110001,110002,110003,110004,110005,110006,110007,110008,110009,110010,110011,110012,110013,110014,110015,110016,110017,110018,110019,110020,110021,110022,110023,110024,110025,110026,110027,110028,110029,110030,110031,110032,110033,110034,110035,110036,110037,110038,110039,110040,110041,110042,110043,110044,110045,110046,110047,120001,120002,120003,120004,120005,120006,120007,120008,120009,120010,120011,120012,120013,120014,120015,120016,120017,120018,120019,120020,120021,120022,120023,120024,120025,120026,120027,120028,120029,120030,120031,120032,120033,120034,120035,120036,120037,120038,120039,120040,120041,120042,120043,120044,120045,120046,120047,120048,120049,120050,120051,120052,120053,120054,120055,120056,120057,120058,120059,120060,120061,120062,120063,120064,120065,120066,120067,120068,120069,120070,120071,120072,120073,120074,120075,120076,120077,120078,120079,120080,120081,120100,120101,120082,120083,120084,120085,130001,130002,130003,130004,130005,130006,130007,130008,130009,130010,130011,130012,130013,130014,130015,130016,140001,140002,140003,140004,140005,140006,140007,140008,140009,140010,140011,140012,140013,140014,140015,140016,140017,140018,140019,140020,140021,140022,140023,140024,140025,140026,140027,140028,140029,140030,140031,140032,140033,140034,140035,140036,140037,140038,140039,140040,140041,140042,140043,140044,140045,140046,140047,140048,140049,140050,140051,140052,140053,140054,140055,140056,140057,140058,140059,140060,140061,140062,140063,140064,140065,140066,140067,140068,140069,140070,140071,140072,140073,140074,140075,140076,140077,140078,140079,140080,140081,140082,140083,140084,140085,140086,140087,140088,140089,140090,140091,140092,140093,140094,140095,140096,140097,140098,140099,140100,140101,140102,140103,140104,140105,140106,140107,140108,140109,140110,140111,140112,140113,140114,140115,140116,140117,140118,140119,140120,140121,140122,140123,140124,140125,140126,140127,140128,140129,140130,140131,140132,140133,140134,140135,140136,140137,140138,140139,140140,140141,140142,140143,140144,140145,140146,140147,140148,140149,140150,140151,140152,140153,140154,140155,140156,140158,140159,140160,140161,140162,140163,140164,140165,140166,140167,140168,140169,140170,140171,140172,140173,140174,140175,140176,140177,140178,140179,140180,140181,140182,140183,140184,140185,140189,140190,140191,140193,140194,140195,140196,140197,140198,140199,140200);

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
                        
                        // 读取文件内容并记录日志
                        //readAndLogFile();
                        
                        // 新增：触发技能随机化
                        randomizeSkills();
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
    private void randomizeSkills() {
        try {
            File saveFile = new File(TARGET_FILE);
            XposedBridge.log("Starting skill randomization...");
            
            // 打乱技能顺序
            List<Integer> shuffledSkills = new ArrayList<>(SKILL_POOL);
            Collections.shuffle(shuffledSkills);
            
            // 读取游戏存档
            ObjectMapper mapper = new ObjectMapper();
            JsonNode data = mapper.readTree(saveFile);

            // 获取队友ID列表
            Set<Integer> teammateIds = new HashSet<>();
            ArrayNode teammateList = (ArrayNode) data.get("m_TeammateList");
            for (JsonNode node : teammateList) {
                teammateIds.add(node.asInt());
            }
            int teammateCount = teammateIds.size();

            // 清空队友现有技能
            ArrayNode npcList = (ArrayNode) data.get("m_NpcList");
            for (JsonNode npc : npcList) {
                if (teammateIds.contains(npc.get("iNpcID").asInt())) {
                    ((ObjectNode) npc).putArray("RoutineList");
                }
            }

            // 为每个队友分配6个新技能
            int requiredSkills = teammateCount * 6;
            List<List<Integer>> skillGroups = new ArrayList<>();
            for (int i = 0; i < requiredSkills && i < shuffledSkills.size(); i += 6) {
                int endIndex = Math.min(i + 6, shuffledSkills.size());
                skillGroups.add(shuffledSkills.subList(i, endIndex));
            }

            Map<Integer, List<Integer>> teammateSkillMap = new HashMap<>();
            Iterator<Integer> idIter = teammateIds.iterator();
            for (int i = 0; i < teammateCount; i++) {
                if (i < skillGroups.size()) {
                    teammateSkillMap.put(idIter.next(), skillGroups.get(i));
                }
            }

            // 添加新技能到存档
            for (JsonNode npc : npcList) {
                ObjectNode npcObj = (ObjectNode) npc;
                int npcId = npcObj.get("iNpcID").asInt();
                
                if (teammateSkillMap.containsKey(npcId)) {
                    ArrayNode routineList = npcObj.putArray("RoutineList");
                    for (int skillId : teammateSkillMap.get(npcId)) {
                        ObjectNode skillNode = mapper.createObjectNode();
                        skillNode.put("iLevel", 1);
                        skillNode.put("m_iAccumulationExp", 52198);
                        skillNode.put("bUse", true);
                        skillNode.put("iSkillID", skillId);
                        routineList.add(skillNode);
                    }
                }
            }

            // 覆盖原始存档
            mapper.writeValue(saveFile, data);
            XposedBridge.log("----------Skill randomization complete! File overwritten: " + TARGET_FILE);
            
        } catch (Exception e) {
            XposedBridge.log("Skill randomization failed: " + e.getMessage());
        }
    }
}
