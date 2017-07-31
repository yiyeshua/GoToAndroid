package com.pili.gotome;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Handler;

import com.pili.basemodel.base.BaseActivity;
import com.pili.basemodel.utils.LogUtil;
import com.pili.basemodel.utils.SPUtils;
import com.pili.gotome.ui.splash.SplashActivity;

import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;

/**
 * Created by lhw on 2017/5/5.
 * 欢迎页
 */

public class WelcomeActivity extends BaseActivity{
    public static final int PERMISSION = 100;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initView() {
        /**
         * 6.0系统动态权限申请需要
         */
        String[] params = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE};
        requestPermissions(params, new PermissionListener() {
            @Override
            public void onGranted() {
                skip();
            }

            @Override
            public void onDenied(List<String> deniedPermissions) {
                //引导用户跳转到设置界面
                new AppSettingsDialog.Builder(WelcomeActivity.this, "希望您通过权限")
                        .setTitle("权限设置")
                        .setPositiveButton("设置")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .setRequestCode(PERMISSION)
                        .build()
                        .show();
            }
        });

    }

    private void skip() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
                LogUtil.d("最高可用内存:" + maxMemory);

                boolean aBoolean = SPUtils.getBoolean(WelcomeActivity.this, getResources().getString(R.string.iswelcome));
                if(!aBoolean){
                    startThenKill(SplashActivity.class);
                }else{
                    startThenKill(Main2Activity.class);
                }
                WelcomeActivity.this.overridePendingTransition(R.anim.scale_in, R.anim.shrink_out);
            }
        }, 1000 * 2);
    }

}
