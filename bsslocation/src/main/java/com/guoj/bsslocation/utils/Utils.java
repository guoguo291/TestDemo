package com.guoj.bsslocation.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * Created by guoj on 2019/4/23.
 */

public class Utils {
   public static  String getWifiInfo(Context context){

       WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
       WifiInfo info = wifi.getConnectionInfo();

       String maxText = info.getMacAddress();
       String status = "";
       if (wifi.getWifiState() == WifiManager.WIFI_STATE_ENABLED)
       {
           status = "WIFI_STATE_ENABLED";
       }else{
           return "WIFI不可用";
       }
       String ssid = info.getSSID();
       int networkID = info.getNetworkId();
       int speed = info.getLinkSpeed();
       String bssid = info.getBSSID();
       return "mac：" + maxText + "\n\r"
               + "wifi status :" + status + "\n\r"
               + "ssid :" + ssid + "\n\r"
               + "net work id :" + networkID + "\n\r"
               + "connection speed:" + speed + "\n\r"
               + "BSSID:" + bssid + "\n\r"
               ;
   }
}
