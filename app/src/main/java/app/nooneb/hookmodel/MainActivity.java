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
        
        textView.append("版本2.0，随机队友\n");
    }
}