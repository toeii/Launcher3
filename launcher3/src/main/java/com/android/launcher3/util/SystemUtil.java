package com.android.launcher3.util;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;

import java.util.Locale;

/**
 * @author Toeii
 * @create 2020/5/28
 * @Describe
 */
public class SystemUtil {

    public static final String PHONE_XIAOMI = "xiaomi";
    public static final String PHONE_HUAWEI = "huawei";
    public static final String PHONE_MEIZU = "meizu";
    public static final String PHONE_SONY = "sony";
    public static final String PHONE_SAMSUNG = "samsung";
    public static final String PHONE_LG = "lg";
    public static final String PHONE_HTC = "htc";
    public static final String PHONE_NOVA = "nova";
    public static final String PHONE_OPPO = "oppo";
    public static final String PHONE_VIVO = "vivo";
    public static final String PHONE_LeMobile = "LeMobile";
    public static final String PHONE_LENOVO = "lenovo";

    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取当前系统上的语言列表(Locale列表)
     *
     * @return  语言列表
     */
    public static Locale[] getSystemLanguageList() {
        return Locale.getAvailableLocales();
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return  系统版本号
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return  手机型号
     */
    public static String getSystemModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return  手机厂商
     */
    public static String getDeviceBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)
     *
     * @return  手机IMEI
     */
    public static String getIMEI(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Activity.TELEPHONY_SERVICE);
        if (tm != null) {
            return tm.getDeviceId();
        }
        return null;
    }


}
