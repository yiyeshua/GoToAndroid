package com.pili.gotome;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import com.pili.basemodel.base.BaseApplication;
import com.pili.basemodel.cache.ACache;
import com.pili.basemodel.manager.AppCrashHandler;
import com.pili.basemodel.utils.AppUtils;
import com.pili.basemodel.utils.SPUtils;

/**
 * Created by Administrator on 2017/7/26.
 */

public class App extends BaseApplication {
    @Override
    public void onCreate() {
        AppUtils.init(this);
        ACache.get(this);
        SPUtils.setApplication(this);
        //初始化异常处理机制，收集错误日志并保存在sd卡的crashLog目录下
        initCrashHandler(this);
        super.onCreate();
    }



    /**
     * 初始化crash的控制器
     * @author cheng.tian
     */
    public static void initCrashHandler(final Context context) {
        // 初始化CrashHandler
        AppCrashHandler.getInstance().init(context);
        // 设置成当出现crash的时候不自动退出
        AppCrashHandler.getInstance().setAutomaticExit(false);
        // 设置是否保存crash日志
        AppCrashHandler.getInstance().setCanSaveLog(true);
        // 添加crash后的自定义动作
        AppCrashHandler.getInstance().setCrashListener(new AppCrashHandler.CrashListener() {

            @Override
            public void onCrashAction() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        /**如果线程中使用Looper.prepare()和Looper.loop()创建了消息队列就可以让消息处理在该线程中完成。*/
                        Looper.prepare();
                        Toast.makeText(context, "程序奔溃，请处理！", Toast.LENGTH_LONG)
                                .show();
                        Looper.loop();
                        /**
                         * 让Looper开始工作，从消息队列里取消息，处理消息。
                         * 注意：写在Looper.loop()之后的代码不会被执行，这个函数内部应该是一个循环，当调用mHandler.getLooper().quit()后，
                         * loop才会中止，其后的代码才能得以运行。
                         */
                    }
                }).start();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                AppCrashHandler.getInstance().exitApp();
            }
        });
    }
}
