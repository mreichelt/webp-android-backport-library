#include <jni.h>
#include <time.h>
#include <android/log.h>
#include <android/bitmap.h>

#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#include <webp/decode.h>

#define  LOG_TAG    "libwebpbackport"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)


JNIEXPORT jboolean JNICALL Java_de_marcreichelt_webp_1backport_WebPBackport_getInfo(
        JNIEnv * env,
        jobject obj,
        jbyteArray encoded,
        jintArray width,
        jintArray height
) {
    jbyte* encoded_array;
    jsize encoded_length;
    int ret, tmpWidth, tmpHeight;
    jint elements[] = {0};

    encoded_array = (*env)->GetByteArrayElements(env, encoded, NULL);
    encoded_length = (*env)->GetArrayLength(env, encoded);

    ret = WebPGetInfo(encoded_array, encoded_length, &tmpWidth, &tmpHeight);

    (*env)->ReleaseByteArrayElements(env, encoded, encoded_array, JNI_ABORT);

    elements[0] = tmpWidth;
    (*env)->SetIntArrayRegion(env, width, 0, 1, elements);
    elements[0] = tmpHeight;
    (*env)->SetIntArrayRegion(env, height, 0, 1, elements);

    return ret ? JNI_TRUE : JNI_FALSE;
}

jboolean decodeRGBAIntoInternal(JNIEnv * env, AndroidBitmapInfo info, jbyteArray encoded, jobject bitmap) {
    size_t output_buffer_size;
    uint8_t* result;
    void *pixels;
    int ret;
    jbyte* encoded_array;
    jsize encoded_length;
    int width = 0;
    int height = 0;

    output_buffer_size = info.height * info.stride;
    encoded_array = (*env)->GetByteArrayElements(env, encoded, NULL);
    encoded_length = (*env)->GetArrayLength(env, encoded);
    ret = WebPGetInfo(encoded_array, encoded_length, &width, &height);

    if (!ret) {
        LOGE("unable to get webp info");
        return JNI_FALSE;
    }
    if (info.width != width || info.height != height) {
        LOGE("webp size %dx%d does not match bitmap size %dx%d", width, height, info.width, info.height);
        return JNI_FALSE;
    }

    if ((ret = AndroidBitmap_lockPixels(env, bitmap, &pixels)) < 0) {
        LOGE("AndroidBitmap_lockPixels() failed ! error=%d", ret);
        return JNI_FALSE;
    }

    result = WebPDecodeRGBAInto(encoded_array, encoded_length, (uint8_t*) pixels, output_buffer_size, info.stride);

    AndroidBitmap_unlockPixels(env, bitmap);

    (*env)->ReleaseByteArrayElements(env, encoded, encoded_array, JNI_ABORT);

    return result ? JNI_TRUE : JNI_FALSE;
}



JNIEXPORT jboolean JNICALL Java_de_marcreichelt_webp_1backport_WebPBackport_decodeRGBAInto(
        JNIEnv * env,
        jobject obj,
        jobject bitmap,
        jbyteArray encoded
) {
    AndroidBitmapInfo info;
    int ret;

    if ((ret = AndroidBitmap_getInfo(env, bitmap, &info)) < 0) {
        LOGE("AndroidBitmap_getInfo() failed ! error=%d", ret);
        return JNI_FALSE;
    }

    if (info.format != ANDROID_BITMAP_FORMAT_RGBA_8888) {
        LOGE("Bitmap format is not RGBA_8888 !");
        return JNI_FALSE;
    }

    return decodeRGBAIntoInternal(env, info, encoded, bitmap);
}
