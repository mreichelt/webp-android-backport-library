package de.marcreichelt.webp_backport;

import android.test.AndroidTestCase;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;

import de.marcreichelt.webp_backport.test.R;

public class FileSignatureCheckerTest extends AndroidTestCase {

    public static final byte[] ARRAY_1234 = {1, 2, 3, 4};
    public static final byte[] ARRAY_EMPTY = {};

    static {
        WebPBackport.loadLibrary();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        assertTrue("native WebP library must be loaded for tests to be able to run", WebPBackport.librarySuccessfullyLoaded);
    }

    public void testByteArrayContains() throws Exception {
        assertTrue(FileSignatureChecker.containsAt(ARRAY_1234, ARRAY_1234, 0));
        assertTrue(FileSignatureChecker.containsAt(ARRAY_1234, ARRAY_EMPTY, 0));
        assertTrue(FileSignatureChecker.containsAt(ARRAY_1234, new byte[]{2, 3, 4}, 1));
        assertTrue(FileSignatureChecker.containsAt(ARRAY_EMPTY, ARRAY_EMPTY, 0));


        assertFalse(FileSignatureChecker.containsAt(ARRAY_1234, ARRAY_1234, -1));
        assertFalse(FileSignatureChecker.containsAt(ARRAY_1234, ARRAY_1234, 999));
        assertFalse(FileSignatureChecker.containsAt(ARRAY_1234, ARRAY_1234, 4));
        assertFalse(FileSignatureChecker.containsAt(ARRAY_EMPTY, new byte[]{2, 3, 4}, 1));
        assertFalse(FileSignatureChecker.containsAt(new byte[]{2, 3, 4}, new byte[]{2, 3, 4}, 1));
        assertFalse(FileSignatureChecker.containsAt(new byte[]{2, 3, 4}, new byte[]{2, 3, 4, 5}, 0));
        assertFalse(FileSignatureChecker.containsAt(new byte[]{2, 3, 3}, new byte[]{2, 3, 4}, 0));

        assertTrue(FileSignatureChecker.containsAt(ARRAY_1234, new byte[]{4}, 3));
        assertFalse(FileSignatureChecker.containsAt(ARRAY_1234, new byte[]{4}, 4));

    }

    public void testByteArrayStartsWith() throws Exception {
        assertTrue(FileSignatureChecker.startsWith(ARRAY_1234, ARRAY_1234));
        assertTrue(FileSignatureChecker.startsWith(ARRAY_1234, new byte[]{1}));
        assertTrue(FileSignatureChecker.startsWith(ARRAY_1234, ARRAY_EMPTY));

        assertTrue(FileSignatureChecker.startsWith(ARRAY_EMPTY, ARRAY_EMPTY));

        assertTrue(FileSignatureChecker.startsWith(ARRAY_1234, new byte[]{1, 2, 3}));
        assertTrue(FileSignatureChecker.startsWith(ARRAY_1234, new byte[]{1, 2}));
        assertTrue(FileSignatureChecker.startsWith(ARRAY_1234, new byte[]{1}));
    }

    public void testEmptyIsNoWebP() throws Exception {
        final byte[] encoded = new byte[0];
        assertFalse(FileSignatureChecker.checkForWebP(encoded));
    }

    public void testEmptyFileIsNoWebP() throws Exception {
        final byte[] encoded = loadFromResource(R.raw.test_empty);
        assertFalse(FileSignatureChecker.checkForWebP(encoded));
    }

    public void testWebPLightsIsWebP() throws Exception {
        assertTrue(FileSignatureChecker.checkForWebP(
                loadFromResource(R.raw.test_lights_1280x853_webp)));
    }

    public void testWebPLightIsRIFF() throws Exception {
        assertTrue(FileSignatureChecker.checkForRIFF(
                loadFromResource(R.raw.test_lights_1280x853_webp)));
    }

    public void testWebPTransparentLosslessIsWebP() throws Exception {
        assertTrue(FileSignatureChecker.checkForWebP(
                loadFromResource(R.raw.test_pixel_transparent_webp)));
    }

    public void testWebPTransparentLosslessIsRIFF() throws Exception {
        assertTrue(FileSignatureChecker.checkForRIFF(
                loadFromResource(R.raw.test_pixel_transparent_webp)));
    }


    private byte[] loadFromResource(int resource) throws Exception {
        final InputStream in = getContext().getResources().openRawResource(resource);
        return IOUtils.toByteArray(in);
    }
}