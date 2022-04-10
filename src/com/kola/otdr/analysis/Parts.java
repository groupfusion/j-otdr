package com.kola.otdr.analysis;

/**
 * @Title: Parts.java
 * @Description:
 * @Package: com.kola.otdr.analysis
 * @author: xingyun－zhanghuijun
 * @date: 2022-03-30 15:28
 */
public class Parts {
    public static int LENGTH_SHORT = 2;
    public static int LENGTH_LONG = 4;
    /**
     * 读取整型数
     *
     * @param b      字节内容
     * @param offset 起始位置偏移量
     * @param length 整型数字节长度 {@link #LENGTH_SHORT,#LENGTH_LONG}
     * @return "小端" 整型数值
     */
    public static int readInt(byte[] b, int offset, int length) {
        byte[] bytes = new byte[LENGTH_LONG];
        System.arraycopy(b, offset, bytes, 0, length);
        return bytesToInt(bytes);
    }

    /**
     * byte[]转int
     * <p>由低位到高位</P>
     *
     * @param bytes 字节数组
     * @return 整型int
     */
    public static int bytesToInt(byte[] bytes) {
        int value = 0;
        for (int i = 0; i < bytes.length; i++) {
            int shift = i * 8;
            value += (bytes[i] & 0x000000FF) << shift;
        }
        return value;
    }

    /**
     * 读取以0为结束符的字符串
     *
     * @param b      字节内容
     * @param offset 起始位置偏移量
     * @return UTF-8字符串
     */
    public static String readStringSpaceZero(byte[] b, int offset) {
        int length = 0;
        while (length + offset <= b.length && b[length + offset] != 0) {
            length++;
        }
        return readString(b, offset, length);
    }

    /**
     * 读取字符串
     *
     * @param b      字节内容
     * @param offset 起始位置偏移量
     * @param length 读取长度
     * @return UTF-8字符串
     */
    public static String readString(byte[] b, int offset, int length) {
        byte[] bytes = new byte[length];
        System.arraycopy(b, offset, bytes, 0, length);
        return new String(bytes);
    }
}
