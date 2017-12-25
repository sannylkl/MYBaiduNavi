package com.example.administrator.mybaidunavi;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by 华罡 on 2017/12/25.
 */

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static ExceptionHandler instance = new ExceptionHandler();
    private Context context;
    private String infoPath = "/ErrorLog/";
    private Thread.UncaughtExceptionHandler defaultHandler;
    private Map<String, String> devInfos = new HashMap<String, String>();
    private DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());

    public static ExceptionHandler getInstance() {
        return instance;
    }

    public void setCustomCrashHanler(Context ctx) {
        context = ctx;
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * @param thread 发生异常的线程
     * @param ex     抛出的异常
     * @return void
     * @name uncaughtException(Thread thread, Throwable ex)
     * @description 当发生UncaughtException时会回调此函数
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        boolean isDone = doException(ex);
        if (!isDone && defaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            defaultHandler.uncaughtException(thread, ex);
        } else {
            // 如果自己处理了异常，则不会弹出错误对话框，则需要手动退出app
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {

            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    /**
     * @param ex 抛出的异常
     * @return 异常处理标志
     * @name doException(Throwable ex)
     * @description 处理异常
     */
    private boolean doException(Throwable ex) {
        if (ex == null) {
            return true;
        }

        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(context, "程序出现错误退出！", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();

        collectDeviceInfo(context);
        saveExceptionToFile(ex);
        return true;
    }


    /**
     * @param ctx
     * @return void
     * @name collectDeviceInfo(Context ctx)
     * @description 收集必须的设备信息
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                devInfos.put("versionName", pi.versionName);
                devInfos.put("versionCode", "" + pi.versionCode);
                devInfos.put("MODEL", "" + Build.MODEL);
                devInfos.put("SDK_INT", "" + Build.VERSION.SDK_INT);
                devInfos.put("PRODUCT", "" + Build.PRODUCT);
                devInfos.put("TIME", "" + getCurrentTime());
            }
        } catch (PackageManager.NameNotFoundException e) {

        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                devInfos.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
            }
        }
    }

    /**
     * @param ex 抛出的异常
     * @return void
     * @name saveExceptionToFile(Throwable ex)
     * @description 保存异常信息到文件中
     */
    private void saveExceptionToFile(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : devInfos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();

        String result = writer.toString();
        sb.append(result);
        try {
            String time = df.format(new Date());
            String fileName = time + ".txt";

            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = Environment.getExternalStorageDirectory() + infoPath;
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(path + fileName);
                fos.write(sb.toString().getBytes());
                fos.close();
            }
        } catch (Exception e) {

        }
    }

    /**
     * @param
     * @return 当前时间
     * @name getCurrentTime()
     * @description 获取当前时间
     */
    public static String getCurrentTime() {
        SimpleDateFormat sdf = null;
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String CurrentTime = sdf.format(new Date());
        return CurrentTime;
    }
}
