package com.google.webp;

@SuppressWarnings("ALL")
public class libwebpJNI {

    public final static native int WebPGetDecoderVersion();

    public final static native int WebPGetInfo(byte[] jarg1, long jarg2, int[] jarg3, int[] jarg4);

    public final static native byte[] WebPDecodeRGB(byte[] jarg1, long jarg2, int[] jarg3, int[] jarg4);

    public final static native byte[] WebPDecodeRGBA(byte[] jarg1, long jarg2, int[] jarg3, int[] jarg4);

    public final static native byte[] WebPDecodeARGB(byte[] jarg1, long jarg2, int[] jarg3, int[] jarg4);

    public final static native byte[] WebPDecodeBGR(byte[] jarg1, long jarg2, int[] jarg3, int[] jarg4);

    public final static native byte[] WebPDecodeBGRA(byte[] jarg1, long jarg2, int[] jarg3, int[] jarg4);
    
}
