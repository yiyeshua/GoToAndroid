package com.pili.gotome;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.pili.basemodel.base.BaseActivity;
import com.pili.basemodel.utils.LogUtil;

import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by llf on 2017/5/5.
 * 欢迎页
 */

public class Welcome2Activity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
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

        if (EasyPermissions.hasPermissions(Welcome2Activity.this, params)) {
            skip();
        } else {
            EasyPermissions.requestPermissions(Welcome2Activity.this, "应用需要权限才能安全运行", PERMISSION, params);
        }
    }

    private void skip() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
                LogUtil.d("最高可用内存:" + maxMemory);
                startThenKill(Main2Activity.class);
                Welcome2Activity.this.overridePendingTransition(R.anim.scale_in, R.anim.shrink_out);
            }
        }, 1000 * 2);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        skip();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        switch (requestCode) {
            case PERMISSION:
                //引导用户跳转到设置界面
                new AppSettingsDialog.Builder(Welcome2Activity.this, "希望您通过权限")
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
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
