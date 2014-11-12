package com.google.webp;

@SuppressWarnings("ALL")
public class libwebp {

    public static int WebPGetDecoderVersion() {
        return libwebpJNI.WebPGetDecoderVersion();
    }

    public static int WebPGetInfo(byte[] data, long data_size, int[] width, int[] height) {
        return libwebpJNI.WebPGetInfo(data, data_size, width, height);
    }

    public static byte[] WebPDecodeRGB(byte[] data, long data_size, int[] width, int[] height) {
        return libwebpJNI.WebPDecodeRGB(data, data_size, width, height);
    }

    public static byte[] WebPDecodeRGBA(byte[] data, long data_size, int[] width, int[] height) {
        return libwebpJNI.WebPDecodeRGBA(data, data_size, width, height);
    }

    public static byte[] WebPDecodeARGB(byte[] data, long data_size, int[] width, int[] height) {
        return libwebpJNI.WebPDecodeARGB(data, data_size, width, height);
    }

    public static byte[] WebPDecodeBGR(byte[] data, long data_size, int[] width, int[] height) {
        return libwebpJNI.WebPDecodeBGR(data, data_size, width, height);
    }

    public static byte[] WebPDecodeBGRA(byte[] data, long data_size, int[] width, int[] height) {
        return libwebpJNI.WebPDecodeBGRA(data, data_size, width, height);
    }

    private static final int UNUSED = 1;
    private static int outputSize[] = {0};

}
