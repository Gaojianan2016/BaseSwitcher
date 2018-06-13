# BaseSwitcher
自定义Switcher工具

- 依赖使用
```
allprojects {
    repositories {
        google()
        jcenter()
        maven { url 'https://jitpack.io' }
    }
}


dependencies {
    implementation 'com.github.Gaojianan2016:BaseSwitcher:1.0.0'
}
```

```
package com.gjn.baseswitcher;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.gjn.baseswitcherlibrary.TextSwitcherSet;
import com.gjn.baseswitcherlibrary.ViewSwitcherSet;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewSwitcherSet<String> utils;
    private List<String> strings;
    private TextSwitcherSet utils2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewSwitcher vs = findViewById(R.id.vs);
        TextSwitcher ts = findViewById(R.id.ts);

        strings = new ArrayList<>();

        utils = new ViewSwitcherSet<String>(this, vs, R.layout.item, null) {
            @Override
            protected void bindData(Activity activity, View view, int marker, String s) {
                TextView textView = view.findViewById(R.id.tv_item);
                textView.setText(s);
                Button button = view.findViewById(R.id.button);
                button.setText(String.valueOf(marker));
            }
        };
        utils.create();

        utils2 = new TextSwitcherSet(this, ts, null);
        utils2.create();

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utils.previousView();
                utils2.previousView();
            }
        });

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strings.clear();
                strings.add("111111111");
                strings.add("123");
                strings.add("1123123");
                strings.add("1231231");
                strings.add("11111123121111");
                utils.updataView(strings);
                utils2.updataView(strings);
            }
        });

        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utils.nextView();
                utils2.nextView();
            }
        });

        findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utils.setLoop(!utils.isLoop()).updataView();
                utils2.setLoop(!utils2.isLoop()).updataView();
            }
        });

        findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utils.setAnimation(!utils.isAnimation()).updataView();
                utils2.setAnimation(!utils2.isAnimation()).updataView();
            }
        });

        findViewById(R.id.button7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strings.add("新增加.........."+strings.size());
                utils.updataView();
                utils2.updataView();
            }
        });
    }
}

```
