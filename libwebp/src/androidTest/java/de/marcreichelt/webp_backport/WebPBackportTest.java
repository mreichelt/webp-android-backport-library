package de.marcreichelt.webp_backport;

import android.graphics.Bitmap;
import android.support.annotation.RawRes;
import android.test.AndroidTestCase;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;

import de.marcreichelt.webp_backport.test.R;

public class WebPBackportTest extends AndroidTestCase {

    static {
        WebPBackport.loadLibrary();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        assertTrue("native WebP library must be loaded for tests to be able to run", WebPBackport.librarySuccessfullyLoaded);
    }

    public void testLoadEmptyFileReturnsNull() throws Exception {
        assertTrue(WebPBackport.isLibraryUsed());
        assertNull(WebPBackport.decodeViaLibrary(new byte[0]));
    }

    public void testLoadTrashReturnsNull() throws Exception {
        assertTrue(WebPBackport.isLibraryUsed());
        assertNull(WebPBackport.decodeViaLibrary(new byte[]{1, 2, 3, 4, 5}));
    }

    public void testLoadNormalImage() throws Exception {
        decodeAndAssertNormalImage(1);
    }

    public void testGarbageCollectionCanFreeData() throws Exception {
        decodeAndAssertNormalImage(77);
    }

    void decodeAndAssertNormalImage(int times) throws Exception {
        // image is public domain from http://www.pdpics.com/photo/7425-road-traffic-lights-cars/
        byte[] encoded = loadFromResource(R.raw.test_lights_1280x853);
        for (int i = 0; i < times; i++) {
            Bitmap decoded = WebPBackport.decodeViaLibrary(encoded);
            assertNotNull(decoded);
            assertEquals(1280, decoded.getWidth());
            assertEquals(853, decoded.getHeight());
        }
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