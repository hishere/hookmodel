package app.nooneb.hookmodel;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView; // Java

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        TextView textView = findViewById(R.id.t1); // Java
        textView.append("版本1.8，移除140157,140186,140187,140188四个卡死技能，现在分配6个外功和4个内功和暴击95，触发时机：onResume（切后台再返回前台），目标：Save0存档\n");  // 追加文本并换行
        textView.append("版本1.9，移除鸟兽类技能（因为没有特效），但保留鸟兽内功，增加4格移动\n");     // 继续追加
        textView.append("移除技能：[110010,110021,140101,140188,140187,140186,140157],[110031,110032,120040,120041,120042,120043,120044,120051,120052,120066,120067,120068,120069,120070,120071,120056,120057,120058,130012,140064,140065,140066,140095,140096,140097,140098,140099,140131,140132,110012,110013,140190,140059,140062]\n");     // 继续追加
        textView.append("移除内功：60026,70045,70046,87010,87011,87012,87013,87014,87037,87038,87039,87040,87041,87042,87043,60064,70008,70026,70052,70053,70054,70055,70056,70057,70006,70038,70051,70058,70059,70060,70061,70062,70063,70064,70065,70073,70074,700758,7004,87019,87021,87031,87032,87033,87034,87035,87045,87047,87048,87049,87050,87051,70066,70067,70068,87036\n");
        textView.append("版本1.94最终版，扩展hook范围，只要包名含xkqz关键字可hook，但以com.sy.xkqz.tap为准，其他包不一定能用，内功6个，移除一些基础性技能\n");

        textView.append("版本2.0，随机队友\n");
    }
}