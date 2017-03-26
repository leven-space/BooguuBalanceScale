package com.leven.booguubalancescale.common;

import android.util.Log;

import com.leven.booguubalancescale.BuildConfig;

import org.apache.commons.lang3.StringUtils;

/**
 * 字符串转换工具类
 * Created by thinkpad on 2017/3/17.
 */

public class StringConverterUtil {


    private static final String TAG = "StringConverterUtil";

    final private static char[] hexArray = "0123456789ABCDEF".toCharArray();

    private static char findHex(byte b) {
        int t = new Byte(b).intValue();
        t = t < 0 ? t + 16 : t;

        if ((0 <= t) && (t <= 9)) {
            return (char) (t + '0');
        }

        return (char) (t - 10 + 'A');
    }

    public static String ByteToString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(findHex((byte) ((bytes[i] & 0xf0) >> 4)));
            sb.append(findHex((byte) (bytes[i] & 0x0f)));

        }
        return sb.toString();
    }

    public static byte[] intToByteArray(int a) {
        return new byte[]{
                (byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }


    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * 将16进制的字符串转换为字节数组 *
     *
     * @param message * @return 字节数组
     */
    public static byte[] getHexBytes(String message) {
        int len = message.length() / 2;
        char[] chars = message.toCharArray();
        String[] hexStr = new String[len];
        byte[] bytes = new byte[len];
        for (int i = 0, j = 0; j < len; i += 2, j++) {
            hexStr[j] = "" + chars[i] + chars[i + 1];
            bytes[j] = (byte) Integer.parseInt(hexStr[j], 16);
        }
        return bytes;
    }

    public static Float hexToFloat(String hex) {
        Long i = Long.parseLong(hex, 16);
        Float f = Float.intBitsToFloat(i.intValue());
        // String hexString = Integer.toHexString(Float.floatToIntBits(f));
        if (BuildConfig.DEBUG) Log.d(TAG, "f:" + f);
        return f;
    }

    public static Short hexToShort(String hex) throws NumberFormatException {
        return Short.parseShort(hex, 16);
    }

    public static int hexToInteger(String hex) {
        return Integer.parseInt(hex, 16);
    }


    public String reverse(String s) {
        String low = StringUtils.substring(s, 0, 2);
        String hight = StringUtils.substring(s, 2, 4);
        Log.d(TAG, "reverse: " + s + "--" + hight + low);
        return hight + low;
    }


}
