package de.marcreichelt.webp_backport;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import com.google.webp.libwebp;

import java.nio.ByteBuffer;

/**
 * Helper for loading WebP images on old and new Android platforms. Loads a native decoding library
 * 'libwebp' when the current Android version does not support WebP.
 */
public class WebPBackport {

    private static final String TAG = WebPBackport.class.getSimpleName();
    static boolean librarySuccessfullyLoaded = false;

    static {
        if (!isIsWebpSupportedNatively()) {
            loadLibrary();
        }
    }

    static void loadLibrary() {
        try {
            System.loadLibrary("webp");
            librarySuccessfullyLoaded = true;
        } catch (Exception e) {
            Log.w(TAG, "failed to load webp library", e);
        }
    }

    static boolean isIsWebpSupportedNatively() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    }

    /**
     * Decodes a WebP image. This is done either by using the Android internal functionality or by using the
     * WebPBackport library. The WebPBackport library is used only when the current Android system does not support the
     * decoding by itself and the given encoded image has the necessary WebP file signature in the beginning of its
     * content.
     *
     * @param encoded The encoded WebP data (e.g. from a stream, resource, etc.).
     * @return The decoded image, or {@code null} if it could not be decoded.
     */
    public static Bitmap decode(byte[] encoded) {
        if (isLibraryUsed() && FileSignatureChecker.checkForWebP(encoded)) {
            return decodeViaLibrary(encoded);
        }
        return decodeViaSystem(encoded);
    }

    /**
     * Returns whether WebP images can be decoded. More exact: if WebP is supported by the platform
     * or by the included native library.
     */
    public static boolean isWebPSupported() {
        return isIsWebpSupportedNatively() || librarySuccessfullyLoaded;
    }

    /**
     * Is the native library used to decode WebP images?
     */
    public static boolean isLibraryUsed() {
        return librarySuccessfullyLoaded;
    }

    static Bitmap decodeViaLibrary(byte[] encoded) {
        int[] width = new int[]{0};
        int[] height = new int[]{0};
        byte[] decoded = libwebp.WebPDecodeARGB(encoded, encoded.length, width, height);
        if (width[0] == 0 || height[0] == 0 || decoded == null) {
            return null;
        }
        int[] pixels = new int[decoded.length / 4];
        ByteBuffer.wrap(decoded).asIntBuffer().get(pixels);
        //noinspection UnusedAssignment
        decoded = null;
        return Bitmap.createBitmap(pixels, width[0], height[0], Bitmap.Config.ARGB_8888);
    }

    static Bitmap decodeViaSystem(byte[] encoded) {
        return BitmapFactory.decodeByteArray(encoded, 0, encoded.length);
    }

}
