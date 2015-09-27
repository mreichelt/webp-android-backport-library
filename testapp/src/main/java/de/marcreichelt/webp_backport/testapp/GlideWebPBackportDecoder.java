package de.marcreichelt.webp_backport.testapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.model.ImageVideoWrapper;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.load.resource.bitmap.Downsampler;
import com.bumptech.glide.load.resource.gifbitmap.GifBitmapWrapper;
import com.bumptech.glide.load.resource.gifbitmap.GifBitmapWrapperResource;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import de.marcreichelt.webp_backport.WebPBackport;

/**
 * Bitmap decoder based on {@link com.bumptech.glide.load.resource.bitmap.StreamBitmapDecoder}, using WebP backport library if needed.
 */
public class GlideWebPBackportDecoder implements ResourceDecoder<ImageVideoWrapper, GifBitmapWrapper> {

    private static final String ID = "de.marcreichelt.webp_backport.GlideWebPBackportDecoder";
    private static final String TAG = GlideWebPBackportDecoder.class.getSimpleName();

    private final Downsampler downsampler;

    private BitmapPool bitmapPool;

    private DecodeFormat decodeFormat;

    private String id;

    @SuppressWarnings("unused")
    public GlideWebPBackportDecoder(Context context) {
        this(Glide.get(context).getBitmapPool());
    }

    public GlideWebPBackportDecoder(BitmapPool bitmapPool) {
        this(bitmapPool, DecodeFormat.DEFAULT);
    }

    public GlideWebPBackportDecoder(Context context, DecodeFormat decodeFormat) {
        this(Glide.get(context).getBitmapPool(), decodeFormat);
    }

    public GlideWebPBackportDecoder(BitmapPool bitmapPool, DecodeFormat decodeFormat) {
        this(Downsampler.AT_LEAST, bitmapPool, decodeFormat);
    }

    public GlideWebPBackportDecoder(Downsampler downsampler, BitmapPool bitmapPool, DecodeFormat decodeFormat) {
        this.downsampler = downsampler;
        this.bitmapPool = bitmapPool;
        this.decodeFormat = decodeFormat;
    }

    @Override
    public Resource<GifBitmapWrapper> decode(ImageVideoWrapper source, int width, int height) {
        final InputStream stream = getMarkSupportingStream(source.getStream());
        Bitmap bitmap = WebPBackport.decode(inputStreamToBytes(stream));
        BitmapResource bitmapResource = BitmapResource.obtain(bitmap, bitmapPool);
        return new GifBitmapWrapperResource(new GifBitmapWrapper(bitmapResource, null));
    }

    private InputStream getMarkSupportingStream(InputStream stream) {
        if (stream.markSupported()) {
            return stream;
        } else {
            return new BufferedInputStream(stream);
        }
    }

    @Override
    public String getId() {
        if (id == null) {
            id = ID + downsampler.getId() + decodeFormat.name();
        }
        return id;
    }

    private byte[] inputStreamToBytes(InputStream is) {
        final int bufferSize = 16 * 1024;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream(bufferSize);
        try {
            int nRead;
            byte[] data = new byte[bufferSize];
            while ((nRead = is.read(data)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
        } catch (IOException e) {
            Log.w(TAG, "Error reading data from stream", e);
            return null;
        }
        return buffer.toByteArray();
    }

}

