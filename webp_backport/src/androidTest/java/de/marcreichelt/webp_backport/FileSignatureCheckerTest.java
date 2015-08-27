package de.marcreichelt.webp_backport;

import android.test.AndroidTestCase;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;

import de.marcreichelt.webp_backport.test.R;

public class FileSignatureCheckerTest extends AndroidTestCase {

    static {
        WebPBackport.loadLibrary();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        assertTrue("native WebP library must be loaded for tests to be able to run", WebPBackport.librarySuccessfullyLoaded);
    }

    public void testByteArrayContains() throws Exception {
        assertTrue(FileSignatureChecker.byteArrayContainsAt(new byte[]{1, 2, 3, 4}, new byte[]{1, 2, 3, 4}, 0));
        assertTrue(FileSignatureChecker.byteArrayContainsAt(new byte[]{1, 2, 3, 4}, new byte[]{}, 0));
        assertTrue(FileSignatureChecker.byteArrayContainsAt(new byte[]{1, 2, 3, 4}, new byte[]{2, 3, 4}, 1));
        assertTrue(FileSignatureChecker.byteArrayContainsAt(new byte[]{}, new byte[]{}, 0));


        assertFalse(FileSignatureChecker.byteArrayContainsAt(new byte[]{1, 2, 3, 4}, new byte[]{1, 2, 3, 4}, -1));
        assertFalse(FileSignatureChecker.byteArrayContainsAt(new byte[]{1, 2, 3, 4}, new byte[]{1, 2, 3, 4}, 999));
        assertFalse(FileSignatureChecker.byteArrayContainsAt(new byte[]{1, 2, 3, 4}, new byte[]{1, 2, 3, 4}, 4));
        assertFalse(FileSignatureChecker.byteArrayContainsAt(new byte[]{}, new byte[]{2, 3, 4}, 1));
        assertFalse(FileSignatureChecker.byteArrayContainsAt(new byte[]{2, 3, 4}, new byte[]{2, 3, 4}, 1));
        assertFalse(FileSignatureChecker.byteArrayContainsAt(new byte[]{2, 3, 4}, new byte[]{2, 3, 4, 5}, 0));
        assertFalse(FileSignatureChecker.byteArrayContainsAt(new byte[]{2, 3, 3}, new byte[]{2, 3, 4}, 0));

        assertTrue(FileSignatureChecker.byteArrayContainsAt(new byte[]{1, 2, 3, 4}, new byte[]{4}, 3));
        assertFalse(FileSignatureChecker.byteArrayContainsAt(new byte[]{1, 2, 3, 4}, new byte[]{4}, 4));

    }

    public void testByteArrayStartsWith() throws Exception {
        assertTrue(FileSignatureChecker.byteArrayStartsWith(new byte[]{1, 2, 3, 4}, new byte[]{1, 2, 3, 4}));
        assertTrue(FileSignatureChecker.byteArrayStartsWith(new byte[]{1, 2, 3, 4}, new byte[]{1}));
        assertTrue(FileSignatureChecker.byteArrayStartsWith(new byte[]{1, 2, 3, 4}, new byte[]{}));

        assertTrue(FileSignatureChecker.byteArrayStartsWith(new byte[]{}, new byte[]{}));

        assertTrue(FileSignatureChecker.byteArrayStartsWith(new byte[]{1, 2, 3, 4}, new byte[]{1, 2, 3}));
        assertTrue(FileSignatureChecker.byteArrayStartsWith(new byte[]{1, 2, 3, 4}, new byte[]{1, 2}));
        assertTrue(FileSignatureChecker.byteArrayStartsWith(new byte[]{1, 2, 3, 4}, new byte[]{1}));
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
                loadFromResource(R.raw.test_lights_1280x853)));
    }

    public void testWebPLightIsRIFF() throws Exception {
        assertTrue(FileSignatureChecker.checkForRIFF(
                loadFromResource(R.raw.test_lights_1280x853)));
    }

    public void testWebPTransparentLosslessIsWebP() throws Exception {
        assertTrue(FileSignatureChecker.checkForWebP(
                loadFromResource(R.raw.test_transparent_lossless)));
    }

    public void testWebPTransparentLosslessIsRIFF() throws Exception {
        assertTrue(FileSignatureChecker.checkForRIFF(
                loadFromResource(R.raw.test_transparent_lossless)));
    }


    private byte[] loadFromResource(int resource) throws Exception {
        final InputStream in = getContext().getResources().openRawResource(resource);
        return IOUtils.toByteArray(in);
    }
}