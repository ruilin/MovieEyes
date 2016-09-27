LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := movieeyes
LOCAL_SRC_FILES := jni_main.c

include $(BUILD_SHARED_LIBRARY)
