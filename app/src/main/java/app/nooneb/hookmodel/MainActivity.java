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
        textView.append("版本1.8，移除140157,140186,140187,140188四个卡死技能，现在分配6个外功和3个内功和暴击95，触发时机：onResume（切后台再返回前台），目标：Save0存档\n");  // 追加文本并换行
        textView.append("版本1.9，移除鸟兽类技能（因为没有特效），但保留鸟兽内功，增加4格移动，精简安装包大小\n");     // 继续追加
        textView.append("移除技能：[140188,140187,140186,140157],[110031,110032,120040,120041,120042,120043,120044,120051,120052,120066,120067,120068,120069,120070,120071,120056,120057,120058,140065,140066,140095,140096,140097,140098,140099,140131,140132]\n");     // 继续追加

    }
}