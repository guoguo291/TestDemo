package com.guoj.bsslocation.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by guoj on 2019/4/22.
 */

public class GetLBSInfoByBaseStationUtil {
    private static final String TAG = "GetLBSInfoByBaseStation";
    private Context mContext;
    TelephonyManager mTelephonyManager;

    public GetLBSInfoByBaseStationUtil(Context context) {
        this.mContext = context;
    }

    /**
     * 获取 基站 信息
     * @return
     */
    public String getBaseStationInformation(Activity activity) {
        if (mTelephonyManager == null) {
            mTelephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        }
        // 返回值MCC + MNC （注意：电信的mnc 对应的是 sid）
        String operator = mTelephonyManager.getNetworkOperator();
        List<String> mnc_cdma=Arrays.asList("03","05","11");
        List<String> mnc_gsm=Arrays.asList("00","01","02","04","06","07","09");
        String mcc = "-1";
        String mnc ="-1";
        if (operator != null && operator.length() > 3) {
            mcc = operator.substring(0, 3);
            mnc =operator.substring(3,5);
        }

        // 获取邻区基站信息
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},0);

           return "无权限";
        }
        List<NeighboringCellInfo> infos = mTelephonyManager.getNeighboringCellInfo();
        StringBuffer sb = new StringBuffer("总数 : " + infos.size() + "\n");

        for (NeighboringCellInfo info1 : infos) { // 根据邻区总数进行循环
            sb.append(" LAC : " + info1.getLac()); // 取出当前邻区的LAC
            sb.append("\n CID : " + info1.getCid()); // 取出当前邻区的CID
            sb.append("\n BSSS : " + (-113 + 2 * info1.getRssi()) + "\n"); // 获取邻区基站信号强度
        }


        int type = mTelephonyManager.getNetworkType();

        Toast.makeText(mContext,"type:= "+type, Toast.LENGTH_LONG).show();
        //需要判断网络类型，因为获取数据的方法不一样
//        if(type == TelephonyManager.NETWORK_TYPE_CDMA        // 电信cdma网
//                || type == TelephonyManager.NETWORK_TYPE_1xRTT
//                || type == TelephonyManager.NETWORK_TYPE_EVDO_0
//                || type == TelephonyManager.NETWORK_TYPE_EVDO_A
//                || type == TelephonyManager.NETWORK_TYPE_EVDO_B
//                ){
            if(mnc_cdma.contains(mnc)) {
                CdmaCellLocation cdma = (CdmaCellLocation) mTelephonyManager.getCellLocation();
                if (cdma != null) {
                    sb.append(" MCC = " + mcc + "\n");
                    sb.append(" MNC = " + mnc + "\n");
                    sb.append("\n cdma.getBaseStationLatitude()" + cdma.getBaseStationLatitude() / 14400 + "\n"
                            + "cdma.getBaseStationLongitude() " + cdma.getBaseStationLongitude() / 14400 + "\n"
                            + "cdma.getBaseStationId()(cid)  " + cdma.getBaseStationId()
                            + "\n  cdma.getNetworkId()(lac)   " + cdma.getNetworkId()
                            + "\n  cdma.getSystemId()(mnc)   " + cdma.getSystemId());
                } else {
                    sb.append("can not get the CdmaCellLocation");
                }

//        }else if(type == TelephonyManager.NETWORK_TYPE_GPRS         // 移动和联通GSM网
//                || type == TelephonyManager.NETWORK_TYPE_EDGE
//                || type == TelephonyManager.NETWORK_TYPE_HSDPA
//                || type == TelephonyManager.NETWORK_TYPE_UMTS
//                || type == TelephonyManager.NETWORK_TYPE_LTE){
            }else if(mnc_gsm.contains(mnc)){
            GsmCellLocation gsm = (GsmCellLocation) mTelephonyManager.getCellLocation();
            if(gsm!=null){
                sb.append(" MCC = " + mcc+"\n" );
                sb.append(" MNC = " + mnc +"\n");
                sb.append("  gsm.getCid()(cid)   "+gsm.getCid()+"  \n "//移动联通 cid
                        +"gsm.getLac()(lac) "+gsm.getLac()+"  \n "             //移动联通 lac
                        +"gsm.getPsc()  "+gsm.getPsc());
            }else{
                sb.append("can not get the GsmCellLocation");
            }
        }else if(type == TelephonyManager.NETWORK_TYPE_UNKNOWN){
            Toast.makeText(mContext,"电话卡不可用！",Toast.LENGTH_LONG).show();
        }

        Log.d("spp","mTelephonyManager.getNetworkType(); "+mTelephonyManager.getNetworkType());
        Log.i(TAG, " 获取邻区基站信息:" + sb.toString());
        return sb.toString();
    }
}
