# Only these platforms are needed. The new 64 bit ABIs run on newer Android versions anyway.
APP_ABI := armeabi armeabi-v7a x86
APP_CPPFLAGS := -fno-rtti -fno-exceptions
APP_PLATFORM := android-8

ifndef WEBP_BACKPORT_DEBUG_NATIVE
# Force release compilation in release optimizations, even if application is debuggable by manifest
APP_OPTIM := release
endif
