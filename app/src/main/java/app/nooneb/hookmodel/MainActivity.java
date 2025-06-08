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
        
        textView.append("版本3.0，最终版，不再更新，随机技能6+内功4，血2w，内3000，暴击95，足以横行武侠界\n");
    }
}