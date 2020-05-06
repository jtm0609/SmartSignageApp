LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE    := sample-ffmpeg
LOCAL_SRC_FILES := sample-ffmpeg.c
LOCAL_LDLIBS := -llog
LOCAL_SHARED_LIBRARIES := libavformat libavcodec libswscale libavutil libswresample libavfilter
include $(BUILD_SHARED_LIBRARY)
$(call import-add-path, C:\Users\jtm06\AndroidStudioProjects\graduation\SmartAdvertisingBoard\app)
$(call import-module, libs)