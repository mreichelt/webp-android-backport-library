package de.marcreichelt.webp_backport;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.RawRes;
import android.test.AndroidTestCase;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;

import de.marcreichelt.webp_backport.test.R;

public class WebPBackportTest extends AndroidTestCase {

    /*
     * Image test_lights_1280x853 is public domain: http://www.pdpics.com/photo/7425-road-traffic-lights-cars/
     */

    static {
        WebPBackport.loadLibrary();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        assertTrue("native WebP library must be loaded for tests to be able to run", WebPBackport.librarySuccessfullyLoaded);
    }

    public void testLoadEmptyFileReturnsNull() throws Exception {
        assertNull(WebPBackport.decodeViaLibrary(new byte[0]));
    }

    public void testLoadTrashReturnsNull() throws Exception {
        assertNull(WebPBackport.decodeViaLibrary(new byte[]{1, 2, 3, 4, 5}));
    }

    public void testLoadNormalImage() throws Exception {
        decodeAndAssertNormalImage(1);
    }

    public void testGarbageCollectionCanFreeData() throws Exception {
        decodeAndAssertNormalImage(77);
    }

    public void testNativeDecodeForComparisonIfPossible() throws Exception {
        // only run test case when the device supports it
        if (WebPBackport.isIsWebpSupportedNatively()) {
            byte[] encoded = loadFromResource(R.raw.test_lights_1280x853);
            for (int i = 0; i < 77; i++) {
                Bitmap bitmap = WebPBackport.decodeViaSystem(encoded);
                assertImage(bitmap, 1280, 853);
            }
        }
    }

    public void testTransparencyAndLosslessyCompression() throws Exception {
        byte[] encoded = loadFromResource(R.raw.test_transparent_lossless);
        Bitmap bitmap = WebPBackport.decodeViaLibrary(encoded);
        assertImage(bitmap, 1, 1);
        int firstPixel = bitmap.getPixel(0, 0);
        assertEquals(Color.TRANSPARENT, firstPixel);
    }

    void decodeAndAssertNormalImage(int times) throws Exception {
        byte[] encoded = loadFromResource(R.raw.test_lights_1280x853);
        for (int i = 0; i < times; i++) {
            Bitmap bitmap = WebPBackport.decodeViaLibrary(encoded);
            assertImage(bitmap, 1280, 853);
        }
    }

    void assertImage(Bitmap bitmap, int width, int height) {
        assertNotNull(bitmap);
        assertEquals(width, bitmap.getWidth());
        assertEquals(height, bitmap.getHeight());
    }

    // TODO: currently, instead of throwing an OutOfMemoryError, loading the image crashes the whole VM.
    /*
    public void testMaximumPossibleWebPFileThrowsOutOfMemoryError() throws Exception {
        byte[] encoded = loadFromResource(R.raw.test_maximum_16383px);
        try {
            WebPBackport.decodeViaLibrary(encoded);
            fail("OutOfMemoryError expected");
        } catch (OutOfMemoryError expected) {
            // we actually expect an out of memory error
        }
    }
    */

    byte[] loadFromResource(@RawRes int resource) throws Exception {
        InputStream in = getContext().getResources().openRawResource(resource);
        return IOUtils.toByteArray(in);
    }

}