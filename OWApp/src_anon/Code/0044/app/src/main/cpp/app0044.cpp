#include <jni.h>
#include <iostream>
extern "C"
JNIEXPORT jfloat JNICALL
Java_com_example_app0044_MainActivity_sumFloats(JNIEnv *env, jobject thiz, jfloat a,
                                                        jfloat b) {
    jfloat sum = a + b;

    return sum;
}