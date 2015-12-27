package de.marcreichelt.webp_backport;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
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
        WebPBackport.forceLoadLibrary();
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

    public void testNativeDecodeForComparisonIfPossible() throws Exception {
        // only run test case when the device supports it
        if (WebPBackport.isWebpSupportedNatively()) {
            byte[] encoded = loadFromResource(R.raw.test_lights_1280x853_webp);
            for (int i = 0; i < 77; i++) {
                Bitmap bitmap = WebPBackport.decodeViaSystem(encoded);
                assertImage(bitmap, 1280, 853);
            }
        }
    }

    public void testTransparentPixel() throws Exception {
        assertEqualBitmaps(R.raw.test_pixel_transparent_png, R.raw.test_pixel_transparent_webp);
    }

    public void testRedPixel() throws Exception {
        assertEqualBitmaps(R.raw.test_pixel_red_png, R.raw.test_pixel_red_webp);
    }

    public void testRedPixelWithTransparentBorder() throws Exception {
        assertEqualBitmaps(R.raw.test_red_pixel_with_transparent_border_png, R.raw.test_red_pixel_with_transparent_border_webp);
    }

    public void testLossyRedPixelWithTransparentBorder() throws Exception {
        assertEqualBitmaps(R.raw.test_red_pixel_with_transparent_border_lossy_png, R.raw.test_red_pixel_with_transparent_border_lossy_webp);
    }

    public void testComplexRoundImage() throws Exception {
        assertEqualBitmaps(R.raw.test_round_png, R.raw.test_round_webp);
    }

    public void testComplexSmallRoundImage() throws Exception {
        assertEqualBitmaps(R.raw.test_round_small_png, R.raw.test_round_small_webp);
    }

    public void testLine() throws Exception {
        assertEqualBitmaps(R.raw.test_line_png, R.raw.test_line_webp);
    }

    public void testSemiTransparentPixel() throws Exception {
        assertEqualBitmaps(R.raw.test_pixel_semi_transparent_png, R.raw.test_pixel_semi_transparent_webp);
    }

    public void testBigImage() throws Exception {
        assertEqualBitmaps(R.raw.test_lights_1280x853_png, R.raw.test_lights_1280x853_webp);
    }

    public void testGetSize() throws Exception {
        byte[] encoded = loadFromResource(R.raw.test_lights_1280x853_webp);
        Rect rect = WebPBackport.getSize(encoded);
        assertNotNull(rect);
        assertEquals(1280, rect.width());
        assertEquals(853, rect.height());
    }

    public void testGetSizeOnInvalidData() throws Exception {
        byte[] encoded = loadFromResource(R.raw.test_empty);
        Rect rect = WebPBackport.getSize(encoded);
        assertNull(rect);
    }

    public void testMaximumPossibleWebPFileThrowsOutOfMemoryError() throws Exception {
        byte[] encoded = loadFromResource(R.raw.test_maximum_16383px);
        try {
            WebPBackport.decodeViaLibrary(encoded);
            fail("OutOfMemoryError expected");
        } catch (OutOfMemoryError expected) {
            // we actually expect an out of memory error
        }
    }

    private byte[] loadFromResource(@RawRes int resource) throws Exception {
        InputStream in = getContext().getResources().openRawResource(resource);
        return IOUtils.toByteArray(in);
    }

    private Bitmap webpBitmapFromResource(@RawRes int resource) throws Exception {
        byte[] bytes = loadFromResource(resource);
        return WebPBackport.decodeViaLibrary(bytes);
    }

    private Bitmap normalBitmapFromResource(@RawRes int resource) throws Exception {
        byte[] bytes = loadFromResource(resource);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    private void assertEqualBitmaps(@RawRes int expectedResource, @RawRes int webpResource) throws Exception {
        Bitmap expected = normalBitmapFromResource(expectedResource);
        Bitmap actual = webpBitmapFromResource(webpResource);
        assertEqualBitmaps(expected, actual);
    }

    public void assertEqualBitmaps(Bitmap expected, Bitmap actual) {
        assertEquals(expected.getHeight(), actual.getHeight());
        assertEquals(expected.getWidth(), actual.getWidth());
        for (int y = 0; y < expected.getHeight(); y++) {
            for (int x = 0; x < expected.getWidth(); x++) {
                int expectedColor = expected.getPixel(x, y);
                int actualColor = actual.getPixel(x, y);
                String expectedHex = "#" + Integer.toHexString(expectedColor);
                String actualHex = "#" + Integer.toHexString(actualColor);
                String message = String.format("Pixels at (%s, %s) have different colors.", x, y);
                assertEquals(message, expectedHex, actualHex);
            }
        }
    }

    private void assertImage(Bitmap bitmap, int width, int height) {
        assertNotNull(bitmap);
        assertEquals(width, bitmap.getWidth());
        assertEquals(height, bitmap.getHeight());
    }

    void decodeAndAssertNormalImage(int times) throws Exception {
        byte[] encoded = loadFromResource(R.raw.test_lights_1280x853_webp);
        for (int i = 0; i < times; i++) {
            Bitmap bitmap = WebPBackport.decodeViaLibrary(encoded);
            assertImage(bitmap, 1280, 853);
        }
    }

}
