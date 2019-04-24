package com.guoj.bsslocation;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.guoj.bsslocation.utils.GetLBSInfoByBaseStationUtil;
import com.guoj.bsslocation.utils.Utils;
import com.guoj.testlibrary.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final int FLAG_REQUEST_PERMISSION = 0;
    private static final int FLAG_SETTINGS_PERMISSION = 1;
    @BindView(R.id.btn_get_lbs_info)
    Button btnGetLbsInfo;
    @BindView(R.id.btn_get_wifi_info)
    Button btnGetWifiInfo;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.tv_wifi_info)
    TextView tvWifiInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.btn_get_lbs_info, R.id.btn_get_wifi_info})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_get_lbs_info:
               getLBSInfoByBaseStation();
                break;
            case R.id.btn_get_wifi_info:
                String wifiInfo = Utils.getWifiInfo(MainActivity.this);
                if (wifiInfo != null) {
                    tvWifiInfo.setText(wifiInfo);
                } else {
                    tvWifiInfo.setText("WIFI信息获取异常");
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 用户在授权对话框中授予了权限，执行相关操作
                getLBSInfoByBaseStation();
            } else {
                // 没有授权
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        MainActivity.this, Manifest.permission.CALL_PHONE)) {
                    // 没有点选“不再提示”
                    showPermissionDialog(FLAG_REQUEST_PERMISSION);
                } else {
                    // 点选了“不再提示”
                    showPermissionDialog(FLAG_SETTINGS_PERMISSION);
                }
            }
        }
    }
    private void showPermissionDialog(final int flag) {
        new AlertDialog.Builder(this).setTitle("授权提示")
                .setMessage("需要授予拨打电话权限才能打电话。")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 用户取消了授权，整个路程结束，不执行任何操作。
                        Toast.makeText(MainActivity.this, "取消拨打电话", Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton("去授权", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (flag) {
                            case FLAG_REQUEST_PERMISSION:
                                // 用户之前没有点击“不再提示”，但是此处选在继续授权的分支
                                // 请求系统授权对话框
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.CALL_PHONE}, 0);
                                break;
                            case FLAG_SETTINGS_PERMISSION:
                                // 用户之前点击过“不再提示”，此处选择继续授权的分支
                                // 到APP的详情页手动授权。
                                startActivity(getAppDetailSettingIntent());
                                break;
                        }

                    }
                }).create().show();
    }
    private Intent getAppDetailSettingIntent() {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        localIntent.setData(Uri.fromParts("package", getPackageName(), null));
        return localIntent;
    }
    private void getLBSInfoByBaseStation() {
        GetLBSInfoByBaseStationUtil getLBSInfoByBaseStationUtil = new GetLBSInfoByBaseStationUtil(MainActivity.this);
        String bbsInfo = getLBSInfoByBaseStationUtil.getBaseStationInformation(this);
        if (bbsInfo != null) {
            textView.setText(bbsInfo);
        }else{
            textView.setText("基站信息获取异常");
        }
    }
}
