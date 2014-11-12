# Only these platforms are needed. The new 64 bit ABIs run on newer Android versions anyway.
# We *could* add x86 here, but then again I don't know if there are enough Android devices out there
# with x86 and Android < 4.0. If you have any data on how many Android devices there are with x86
# that do not support WebP already, please send me a mail: mcreichelt@gmail.com

APP_ABI := armeabi armeabi-v7a
APP_PLATFORM := android-8
