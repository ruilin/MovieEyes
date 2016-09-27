//
// Created by ruilin on 16/9/27.
//

#include "jni_main.h"


JNIEXPORT jstring JNICALL
Java_ruilin_com_movieeyes_Jni_LibJni_getHost(JNIEnv *env, jobject instance) {
    return (*env)->NewStringUTF(env, "http://www.quzhuanpan.com");
}