package com.leven.booguubalancescale.common;

/**
 * 字符串转换工具类
 * Created by thinkpad on 2017/3/17.
 */

public class StringConverterUtil {


    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

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
}
