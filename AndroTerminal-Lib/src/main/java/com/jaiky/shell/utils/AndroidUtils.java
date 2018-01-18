package com.jaiky.shell.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Android相关的工具类.
 * @author Jaiky
 */
public final class AndroidUtils {

    private static final String LOG_TAG = AndroidUtils.class.getSimpleName();
    /**
     * 公式的系数，像素= dpi/160*dp.
     */
    private static final int DP_RATIO = 160;


    public static String getFileNameFromUrl(String url) {
        // 通过 ‘？’ 和 ‘/’ 判断文件名
        int index = url.lastIndexOf('?');
        String filename;
        if (index > 1) {
            filename = url.substring(url.lastIndexOf('/') + 1, index);
        } else {
            filename = url.substring(url.lastIndexOf('/') + 1);
        }

        if (filename == null || "".equals(filename.trim())) {// 如果获取不到文件名称
            filename = UUID.randomUUID() + ".apk";// 默认取一个文件名
        }
        return filename;
    }

    /**
     * Android大小单位转换工具类
     *
     * @author wader
     */

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue
     *            （DisplayMetrics类中属性density）
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     *            （DisplayMetrics类中属性density）
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        int i = (int) (dpValue * scale + 0.5f);
        // System.out.println("px = " + i);
        return i;
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().density;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 如果任务正在运行，则取消任务.
     *
     * @param task
     *            任务对象
     */
    @SuppressWarnings("rawtypes")
    public static void cancelTask(AsyncTask task) {
        if (task != null && task.getStatus() == AsyncTask.Status.RUNNING) {
            task.cancel(true);
        }
    }

    /**
     * 隐藏键盘
     *
     * @param activity
     */
    public static void hideKeyboard(Activity activity) {
        InputMethodManager im = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (im.isActive()) {
            View v = activity.getCurrentFocus();
            if (v != null) {
                im.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    public static void hideKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) view.getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    /**
     * 强制隐藏输入法键盘
     */
    public static void hideInput(Activity context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 隐藏键盘
     *
     */
    public static void showKeyboard(Context context, View v) {
        InputMethodManager im = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (v != null) {
            v.requestFocus();
            im.showSoftInput(v, InputMethodManager.SHOW_FORCED);
        }
    }

    /**
     * 关闭处理中的对话框.
     *
     * @param progressDialog
     *            处理对话框
     */
    public static void clossProgressDialog(ProgressDialog progressDialog) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    /**
     * 获取字符串数组.
     *
     * @param context
     *            上下文
     * @param resId
     *            资源id
     * @return 字符串数组
     */
    public static String[] getArrayStr(Context context, int resId) {
        if (context != null) {
            return context.getResources().getStringArray(resId);
        } else {
            throw new RuntimeException("Context must not be null.");
        }
    }

    /**
     * 获取颜色值.
     *
     * @param context
     *            上下文
     * @param resId
     *            资源id
     * @return 颜色值
     */
    public static int getColor(Context context, int resId) {
        if (context != null) {
            return context.getResources().getColor(resId);
        } else {
            throw new RuntimeException("Context must not be null.");
        }
    }

    /**
     * 获取手机显示数据.
     *
     * @param activity
     *            活动对象
     * @return 手机手机显示数据
     */
    public static DisplayMetrics getDisplayMetrics(Activity activity) {
        if (activity != null) {
            DisplayMetrics metric = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
            return metric;
        } else {
            throw new RuntimeException("Activity must not be null.");
        }
    }

    /**
     * 用来判断服务是否后台运行.
     *
     *            上下文
     * @param className
     *            判断的服务名字
     * @return true 在运行 false 不在运行
     */
    public static boolean isServiceRunning(Context mContext, String className) {
        boolean IsRunning = false;
        ActivityManager activityManager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(30);
        if (!(serviceList.size() > 0)) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                IsRunning = true;
                break;
            }
        }
        return IsRunning;
    }



    /**
     * 显示处理中的对话框.
     *
     * @param context
     *            上下文
     * @param message
     *            要显示的消息
     * @return 对话框
     */
    public static ProgressDialog showProgressDialog(Context context, String message) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(message);
        progressDialog.show();
        return progressDialog;
    }

    /**
     * 从TextView获取整型值，如果获取的数据不能转换成预期的数据，则反回默认值.
     *
     * @param textView
     *            要取值的那个TextView组件
     * @param defaultValue
     *            默认值
     * @return 返回整型值
     */
    public static Integer getIntFromTextView(TextView textView, Integer defaultValue) {
        try {
            String trim = textView.getText().toString().trim();
            return Integer.valueOf(trim);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * 判断任务是否空闲.
     *
     * @param asyncTask
     *            异步任务
     * @return true代表是空闲的，false代表不是空闲的.
     */
    public static boolean isTaskIdle(AsyncTask asyncTask) {
        if (asyncTask == null || asyncTask.getStatus() == AsyncTask.Status.FINISHED) {
            return true;
        }
        return false;
    }

    /**
     * 获取view中的tag对象，并转换成指定的Java对象类型.
     *
     * @param view
     * @param clazz
     * @return
     */
    public static <T> T getTag(View view, Class<T> clazz) {
        Object tag = view.getTag();
        return (T) tag;
    }

    /**
     * 判断指定的应用名称是否已创建快捷方式.
     *
     * @param context
     * @param title
     *            标题，应用名称
     * @return
     */
    public static boolean isShortcutCreated(Context context, String title) {
        Cursor cursor = null;
        try {
            boolean isInstallShortcut = false;
            final ContentResolver cr = context.getContentResolver();
            String authority = "com.android.launcher2.settings";
            int systemVersion = getSystemVersion();
            if (systemVersion < 8) {
                // 2.2之前
                authority = "com.android.launcher.settings";
            } else {
                // 2.2之后
                authority = "com.android.launcher2.settings";
            }
            String uriString = "content://" + authority + "/favorites?notify=true";
            final Uri CONTENT_URI = Uri.parse(uriString);
            String[] projection = new String[] { "title", "iconResource" };
            String selection = "title=?";
            String[] selectionArgs = new String[] { title };
            cursor = cr.query(CONTENT_URI, projection, selection, selectionArgs, null);
            if (cursor != null) {
                int count = cursor.getCount();
                if (count > 0) {
                    isInstallShortcut = true;
                }
            }
            return isInstallShortcut;
        } finally {
            closeCursor(cursor);
        }
    }

    public static void closeCursor(Cursor c) {
        if (c != null) {
            c.close();
        }
    }

    public static String getAndroidId(Context context) {
        String uuid = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return TextUtils.isEmpty(uuid) ? "" : uuid;
    }


    /**
     * 获取有线网络IP地址
     * @return
     */
    public static String getLocalIp() {
        try {
            // 获取本地设备的所有网络接口
            Enumeration<NetworkInterface> enumerationNi = NetworkInterface.getNetworkInterfaces();
            while (enumerationNi.hasMoreElements()) {
                NetworkInterface networkInterface = enumerationNi.nextElement();
                String interfaceName = networkInterface.getDisplayName();
//                Log.i("WIFIQQQ", "网络名字" + interfaceName);

                // 如果是有限网卡
                if (interfaceName.equals("eth0")) {
                    Enumeration<InetAddress> enumIpAddr = networkInterface.getInetAddresses();
                    while (enumIpAddr.hasMoreElements()) {
                        // 返回枚举集合中的下一个IP地址信息
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        // 不是回环地址，并且是ipv4的地址
                        if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
//                            Log.i("WIFIQQQ", inetAddress.getHostAddress() + "   ");
                            return inetAddress.getHostAddress();
                        }
                    }
                }
            }

        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "";

    }


    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_ETHERNET){  //使用的是

            } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return null;
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

    /**
     * 检查是否有sdcard
     *
     * @return
     */
    public static boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    /**
     * 获取系统版本.2.2对应是8.
     *
     * @return
     */
    public static int getSystemVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * 调用系统短信程序，发送短信.
     *
     * @param context
     * @param tel
     *            短信接收号码
     * @param smsBody
     *            短信内容
     */
    public static void sendMsg(Context context, String tel, String smsBody) {
        if (tel == null) {
            tel = "";
        }
        String uriString = "smsto:" + tel;// 联系人地址
        Uri smsToUri = Uri.parse(uriString);
        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        intent.putExtra("sms_body", smsBody);// 短信内容
        context.startActivity(intent);
    }

    /**
     * 获取当前app的版本名字.
     *
     * @param context
     * @return
     */
    public static String getAppVersionName(Context context) {
        PackageInfo info = getCurrentAppPackageInfo(context);
        String version = info.versionName;
        return version;
    }

    /**
     * 获取当前app的版本号.
     *
     * @param context
     * @return
     */
    public static int getAppVersionCode(Context context) {
        PackageInfo info = getCurrentAppPackageInfo(context);
        int versionCode = info.versionCode;
        return versionCode;
    }

    /**
     * 获取当前app包信息对象.
     *
     * @param context
     * @return
     * @throws NameNotFoundException
     */
    private static PackageInfo getCurrentAppPackageInfo(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            String packageName = context.getPackageName();
            PackageInfo info = manager.getPackageInfo(packageName, 0);
            return info;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 安装apk文件.
     *
     * @param context
     * @param apkFile
     */
    public static void installApk(Context context, File apkFile) {
        Intent intent = new Intent();
        // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        String type = "application/vnd.android.package-archive";
        intent.setDataAndType(Uri.fromFile(apkFile), type);
        context.startActivity(intent);
    }

    /**
     * 建立部分字符串不同颜色的字符串.
     *
     * @param text
     *            源字符串
     * @param start
     *            开始位置
     * @param end
     *            结束位置
     * @param color
     *            改变的颜色
     * @return SpannableStringBuilder对象，可以直接供给TextView
     */
    public static SpannableStringBuilder buildStringSpannable(String text, int start, int end,
                                                              int color) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(text.toString());// 用于可变字符串
        ForegroundColorSpan span = new ForegroundColorSpan(color);
        spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    /**
     * 建立部分字符串不同颜色的字符串.
     *
     * @param color
     *            改变的颜色
     * @return SpannableStringBuilder对象，可以直接供给TextView
     */
    public static SpannableStringBuilder buildStringsSpannable(String string, String[] key,
                                                               int color) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(string.toString());// 用于可变字符串
        ForegroundColorSpan span = new ForegroundColorSpan(color);
        int start = 0;
        int end = 0;
        for (String s : key) {
            start = string.indexOf(s);
            if (start != -1) {
                end = start + s.length();
            }
            spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannable;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static int getListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return 0;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        return totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
    }

    /**
     * 是否有网络连接
     *
     * @param context
     * @return
     */
    public static boolean isConnect(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                // 获取网络连接管理的对象
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    // 判断当前网络是否已经连接
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    public static long getMediaDuration(String filePath) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            String extractMetadata = retriever
                    .extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            if (!TextUtils.isEmpty(extractMetadata)) {
                return Long.parseLong(extractMetadata);
            } else {
                LogUtils.e("gameshow", "Extract metadata failed.");
            }
        } catch (IllegalArgumentException ex) {
            LogUtils.e("gameshow", "IllegalArgumentException = " + ex.getMessage());
        } catch (RuntimeException ex) {
            LogUtils.e("gameshow", "RuntimeException = " + ex.getMessage());
        } catch (Exception ex) {
            LogUtils.e("gameshow", "exception = " + ex.getMessage());
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                LogUtils.e("gameshow", "IllegalArgumentException = " + ex.getMessage());
            }
        }
        return 0;
    }

    /**
     * 复制到剪切板
     *
     * @param content
     * @param context
     */
    public static void copy(String content, Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }

    public static String secToString(float second) {
        long millis = (long) (second * 1000 + 0.5f);
        boolean negative = millis < 0;
        millis = Math.abs(millis);

        millis /= 1000;
        int sec = (int) (millis % 60);
        millis /= 60;
        int min = (int) (millis % 60);
        millis /= 60;
        int hours = (int) millis;

        String time;
        DecimalFormat format = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        format.applyPattern("00");

        if (millis > 0)
            time = (negative ? "-" : "") + hours + ":" + format.format(min) + ":"
                    + format.format(sec);
        else
            time = (negative ? "-" : "") + min + ":" + format.format(sec);

        return time;
    }

    public static int getTotalHeightofListView(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return 0;
        }
        View listItem = listAdapter.getView(0, null, listView);
        listItem.measure(0, 0);
        return listItem.getMeasuredHeight();

        // ListAdapter mAdapter = listView.getAdapter();
        // if (mAdapter == null) {
        // return;
        // }
        // int totalHeight = 0;
        // for (int i = 0; i < mAdapter.getCount(); i++) {
        // View mView = mAdapter.getView(i, null, listView);
        // mView.measure(
        // MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
        // MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        // //mView.measure(0, 0);
        // totalHeight += mView.getMeasuredHeight();
        // LogUtils.d("HEIGHT" + i, String.valueOf(totalHeight));
        // }
        // ViewGroup.LayoutParams params = listView.getLayoutParams();
        // params.height = totalHeight + (listView.getDividerHeight() * (mAdapter.getCount() - 1));
        // LogUtils.d("xx", "params.height=" + params.height);
        // listView.setLayoutParams(params);
        // listView.requestLayout();

    }

    /**
     * 安装APK文件
     */
    public static void installApk(Context context, String path) {
        String newPath = null;
        if(path.contains("file://")){
            newPath = path.substring(6);
        } else {
            newPath = path;
        }
        File apkfile = new File(newPath);
        if (!apkfile.exists()) {
            return;
        }
        // 通过Intent安装APK文件
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                "application/vnd.android.package-archive");
        context.startActivity(i);
    }

    public static int dp2pxConvertInt(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }

    /**
     * 方法1：通过getRunningTasks判断App是否位于前台，此方法在5.0以上失效
     *
     * @param context     上下文参数
     * @param packageName 需要检查是否位于栈顶的App的包名
     * @return
     */
    public static boolean getRunningTask(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        return !TextUtils.isEmpty(packageName) && packageName.equals(cn.getPackageName());
    }

    /**
     * 方法2：通过getRunningAppProcesses的IMPORTANCE_FOREGROUND属性判断是否位于前台，当service需要常驻后台时候，此方法失效,
     * 在小米 Note上此方法无效，在Nexus上正常
     *
     * @param context     上下文参数
     * @param packageName 需要检查是否位于栈顶的App的包名
     * @return
     */
    public static boolean getRunningAppProcesses(Context context, String packageName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断当前设备是手机还是平板，代码来自 Google I/O App for Android
     * @param context
     * @return 平板返回 True，手机返回 False
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * 获取手机相关信息 此处获取手机厂商、手机型
     *
     * @return
     */
    public static String getPhoneInfo() {
        // 获取手机厂商
        String phoneManufacturer = android.os.Build.MANUFACTURER;
        // 获取手机型号
        String phoneModel = android.os.Build.MODEL;
        return phoneManufacturer + "-" + phoneModel;
    }

    /**
     * 获取手机系统android版本号
     *
     * @return
     */
    public static String getAndroidVersion() {
        // 获取手机android版本号
        String AndroidVersion = android.os.Build.VERSION.RELEASE;
        return AndroidVersion;
    }
}
