package de.marcreichelt.webp_backport;

import android.test.AndroidTestCase;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;

public class FileSignatureCheckerTest extends AndroidTestCase {

    static {
        WebPBackport.loadLibrary();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        assertTrue("native WebP library must be loaded for tests to be able to run", WebPBackport.librarySuccessfullyLoaded);
    }

    public void testNullIsNoWebP() throws Exception {
        final byte[] encoded = null;
        assertFalse(FileSignatureChecker.checkForWebP(encoded));
    }

    public void testEmptyIsNoWebP() throws Exception {
        final byte[] encoded = new byte[0];
        assertFalse(FileSignatureChecker.checkForWebP(encoded));
    }

    public void testEmptyFileIsNoWebP() throws Exception {
        final byte[] encoded = loadFromResource(de.marcreichelt.webp_backport.test.R.raw.test_empty);
        assertFalse(FileSignatureChecker.checkForWebP(encoded)); // TODO really assertFalse ?
    }

    public void testWebPLightsIsWebP() throws Exception {
        assertTrue(FileSignatureChecker.checkForWebP(
                loadFromResource(de.marcreichelt.webp_backport.test.R.raw.test_lights_1280x853)));
    }

    public void testWebPTransparentLosslessIsWebP() throws Exception {
        assertTrue(FileSignatureChecker.checkForWebP(
                loadFromResource(de.marcreichelt.webp_backport.test.R.raw.test_transparent_lossless)));
    }

    private byte[] loadFromResource(int resource) throws Exception {
        final InputStream in = getContext().getResources().openRawResource(resource);
        return IOUtils.toByteArray(in);
    }
}