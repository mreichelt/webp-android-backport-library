// Copyright 2011 Google Inc.
//
// Use of this source code is governed by a BSD-style license
// that can be found in the COPYING file in the root of the source
// tree. An additional intellectual property rights grant can be found
// in the file PATENTS. All contributing project authors may
// be found in the AUTHORS file in the root of the source tree.
// -----------------------------------------------------------------------------
//
// libwebp swig interface definition
//
// Author: James Zern (jzern@google.com)

/*
  Go bindings:
  $ swig -go \
         -outdir . \
         -o libwebp_go_wrap.c libwebp.swig

  Java bindings:
  $ mkdir -p java/com/google/webp
  $ swig -java \
         -package com.google.webp \
         -outdir java/com/google/webp \
         -o libwebp_java_wrap.c libwebp.swig

  Python bindings:
  $ swig -python \
         -outdir . \
         -o libwebp_python_wrap.c libwebp.swig
*/

%module libwebp

%include "constraints.i"
%include "typemaps.i"

#ifdef SWIGJAVA
%include "arrays_java.i";
%include "enums.swg" /*NB: requires JDK-1.5+
                       See: http://www.swig.org/Doc1.3/Java.html#enumerations */

// map uint8_t* such that a byte[] is used
%{
#include "webp/types.h"
%}
// from arrays_java.i (signed char)
JAVA_ARRAYS_DECL(uint8_t, jbyte, Byte, Uint8)
JAVA_ARRAYS_IMPL(uint8_t, jbyte, Byte, Uint8)
JAVA_ARRAYS_TYPEMAPS(uint8_t, byte, jbyte, Uint8, "[B")
%apply uint8_t[] { uint8_t* }
#endif  /* SWIGJAVA */

//------------------------------------------------------------------------------
// Decoder specific

%apply int* OUTPUT { int* width, int* height }

int WebPGetDecoderVersion(void);
int WebPGetInfo(const uint8_t* data, size_t data_size,
                int* width, int* height);

#if defined(SWIGJAVA) || defined(SWIGPYTHON)

// free the buffer returned by these functions after copying into
// the native type
%newobject WebPDecodeRGB;
%newobject WebPDecodeRGBA;
%newobject WebPDecodeARGB;
%newobject WebPDecodeBGR;
%newobject WebPDecodeBGRA;
%typemap(newfree) uint8_t* "free($1);"

uint8_t* WebPDecodeRGB(const uint8_t* data, size_t data_size,
                       int* width, int* height);
uint8_t* WebPDecodeRGBA(const uint8_t* data, size_t data_size,
                        int* width, int* height);
uint8_t* WebPDecodeARGB(const uint8_t* data, size_t data_size,
                        int* width, int* height);
uint8_t* WebPDecodeBGR(const uint8_t* data, size_t data_size,
                       int* width, int* height);
uint8_t* WebPDecodeBGRA(const uint8_t* data, size_t data_size,
                        int* width, int* height);

#endif  /* SWIGJAVA || SWIGPYTHON */

//------------------------------------------------------------------------------
// Wrapper code additions

%{
#include "webp/decode.h"
%}

#ifdef SWIGJAVA
%{
#define FillMeInAsSizeCannotBeDeterminedAutomatically \
    (result ? (jint)ReturnedBufferSize(__FUNCTION__, arg3, arg4) : 0)
%}
#endif  /* SWIGJAVA */

#if defined(SWIGJAVA) || defined(SWIGPYTHON)
%{
static size_t ReturnedBufferSize(
    const char* function, int* width, int* height) {
  static const struct sizemap {
    const char* function;
    int size_multiplier;
  } size_map[] = {
#ifdef SWIGJAVA
    { "Java_com_google_webp_libwebpJNI_WebPDecodeRGB",  3 },
    { "Java_com_google_webp_libwebpJNI_WebPDecodeRGBA", 4 },
    { "Java_com_google_webp_libwebpJNI_WebPDecodeARGB", 4 },
    { "Java_com_google_webp_libwebpJNI_WebPDecodeBGR",  3 },
    { "Java_com_google_webp_libwebpJNI_WebPDecodeBGRA", 4 },
#endif
    { NULL, 0 }
  };
  const struct sizemap* p;
  size_t size = 0;

  for (p = size_map; p->function; ++p) {
    if (!strcmp(function, p->function)) {
      size = *width * *height * p->size_multiplier;
      break;
    }
  }

  return size;
}
%}

#endif  /* SWIGJAVA || SWIGPYTHON */

//------------------------------------------------------------------------------
// Language specific

#ifdef SWIGJAVA
%{
/* Work around broken gcj jni.h */
#ifdef __GCJ_JNI_H__
# undef JNIEXPORT
# define JNIEXPORT
# undef JNICALL
# define JNICALL
#endif
%}

%pragma(java) modulecode=%{
  private static final int UNUSED = 1;
  private static int outputSize[] = { 0 };
%}

#endif  /* SWIGJAVA */
