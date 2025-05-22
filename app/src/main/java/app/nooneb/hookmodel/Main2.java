package cn.kaicity.apps.wifikeylook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.*;
import java.net.URLDecoder;
import java.util.*;
import java.util.regex.*;

public class Main2 extends Activity {
    private static final String TAG = "Main2";
    private ListView listView;

    // 配置文件路径（根据系统版本自动选择）
    private String getConfigPath() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) ?
            "/data/misc/apexdata/com.android.wifi/WifiConfigStore.xml" :
            "/data/misc/wifi/wpa_supplicant.conf";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        listView = findViewById(R.id.listView);

        try {
            String configContent = RootUtils.readRootFile(getConfigPath());
            if (configContent.isEmpty()) {
                showErrorToast("读取配置文件失败，请检查Root权限");
                return;
            }

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                parseXmlConfig(new StringReader(configContent));
            } else {
                parseConfConfig(configContent);
            }
        } catch (Exception e) {
            Log.e(TAG, "初始化失败", e);
            showErrorToast("初始化失败: " + e.getMessage());
        }
    }

    /**
     * 解析旧版conf格式的WiFi配置
     * 实现原理参考搜索结果[6][8]的配置文件解析方法
     */
    private void parseConfConfig(String config) {
        List<Map<String, String>> wifiList = new ArrayList<>();
        
        // 使用正则匹配network块（网页6提到的配置块结构）
        Matcher networkMatcher = Pattern.compile("network=\\{([^\\}]+)\\}", Pattern.DOTALL)
            .matcher(config);

        while (networkMatcher.find()) {
            Map<String, String> wifiInfo = new HashMap<>();
            String networkBlock = networkMatcher.group(1);

            // 解析SSID（支持带引号和十六进制格式）
            Matcher ssidMatcher = Pattern.compile("ssid=\"([^\"]+)\"").matcher(networkBlock);
            if (ssidMatcher.find()) {
                wifiInfo.put("name", "名称：" + ssidMatcher.group(1));
            } else {
                Matcher hexSsidMatcher = Pattern.compile("ssid=([a-fA-F0-9]+)").matcher(networkBlock);
                if (hexSsidMatcher.find()) {
                    wifiInfo.put("name", "名称：" + decodeHexSsid(hexSsidMatcher.group(1)));
                }
            }

            // 解析PSK密码（网页7提到的关键字段）
            Matcher pskMatcher = Pattern.compile("psk=\"([^\"]+)\"").matcher(networkBlock);
            if (pskMatcher.find()) {
                String password = pskMatcher.group(1);
                wifiInfo.put("key", "密码：" + password);
                wifiInfo.put("truekey", password);
            }

            if (!wifiInfo.isEmpty()) {
                wifiList.add(wifiInfo);
            }
        }
        updateListView(wifiList);
    }

    /**
     * 解析新版XML格式的WiFi配置
     * 实现参考搜索结果[6]的XML解析方法
     */
    private void parseXmlConfig(Reader reader) throws XmlPullParserException, IOException {
        List<Map<String, String>> wifiList = new ArrayList<>();
        XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
        parser.setInput(reader);

        Map<String, String> currentWifi = null;
        int eventType = parser.getEventType();
        
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                if ("WifiConfiguration".equals(parser.getName())) {
                    currentWifi = new HashMap<>();
                } else if ("string".equals(parser.getName())) {
                    String nameAttr = parser.getAttributeValue(null, "name");
                    if ("SSID".equals(nameAttr)) {
                        parser.next();
                        String ssid = parser.getText().replace("\"", "");
                        currentWifi.put("name", "名称：" + ssid);
                    } else if ("PreSharedKey".equals(nameAttr)) {
                        parser.next();
                        String password = parser.getText().replace("\"", "");
                        currentWifi.put("key", "密码：" + password);
                        currentWifi.put("truekey", password);
                    }
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                if ("WifiConfiguration".equals(parser.getName()) && currentWifi != null) {
                    wifiList.add(currentWifi);
                    currentWifi = null;
                }
            }
            eventType = parser.next();
        }
        updateListView(wifiList);
    }

    /**
     * 解码十六进制格式的SSID
     * 参考搜索结果[6]的SSID解码方法
     */
    private String decodeHexSsid(String hexSsid) {
        try {
            String decoded = URLDecoder.decode(hexSsid, "UTF-8");
            return decoded.substring(5, decoded.length() - 2);
        } catch (Exception e) {
            Log.w(TAG, "SSID解码失败", e);
            return hexSsid;
        }
    }

    private void updateListView(List<Map<String, String>> wifiList) {
        if (wifiList.isEmpty()) {
            new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("没有找到已保存的WiFi配置")
                .setPositiveButton("确定", null)
                .show();
            return;
        }

        SimpleAdapter adapter = new SimpleAdapter(this, wifiList,
                R.layout.list_item, new String[]{"name", "key"},
                new int[]{R.id.ssid, R.id.password});

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListenerImpl(this, wifiList));
    }

    /**
     * 复制WiFi信息功能实现
     * 参考搜索结果[1][2]的密码复制交互设计
     */
    public void copyWifi(Map<String, String> wifiInfo) {
        new AlertDialog.Builder(this)
            .setTitle("密码操作")
            .setItems(new String[]{"复制密码", "复制名称", "复制全部"}, (dialog, which) -> {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                switch (which) {
                    case 0: // 复制密码
                        clipboard.setPrimaryClip(
                            ClipData.newPlainText("password", wifiInfo.get("truekey")));
                        break;
                    case 1: // 复制名称
                        String name = wifiInfo.get("name").replace("名称：", "");
                        clipboard.setPrimaryClip(ClipData.newPlainText("ssid", name));
                        break;
                    case 2: // 复制全部
                        String allInfo = wifiInfo.get("name") + "\n" + wifiInfo.get("key");
                        clipboard.setPrimaryClip(ClipData.newPlainText("wifi_info", allInfo));
                        break;
                }
                Toast.makeText(this, "已复制到剪贴板", Toast.LENGTH_SHORT).show();
            })
            .show();
    }

    // 内部类实现列表点击监听
    private static class OnItemClickListenerImpl implements AdapterView.OnItemClickListener {
        private final WeakReference<Main2> activityRef;
        private final List<Map<String, String>> wifiList;

        public OnItemClickListenerImpl(Main2 activity, List<Map<String, String>> list) {
            this.activityRef = new WeakReference<>(activity);
            this.wifiList = list;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Main2 activity = activityRef.get();
            if (activity != null && position < wifiList.size()) {
                activity.copyWifi(wifiList.get(position));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, "关于");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 0) {
            showAboutDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAboutDialog() {
        new AlertDialog.Builder(this)
            .setTitle("关于")
            .setMessage("本软件需要Root权限访问系统WiFi配置文件\n路径：" + getConfigPath())
            .setPositiveButton("确定", null)
            .show();
    }

    private void showErrorToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
