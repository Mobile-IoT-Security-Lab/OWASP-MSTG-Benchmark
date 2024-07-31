#include <jni.h>
#include <iostream>

extern "C"
JNIEXPORT jfloat JNICALL
Java_com_example_mastg_1test0040_MainActivity_sumFloats(JNIEnv *env, jobject thiz, jfloat a,
                                                        jfloat b) {
    // Perform the sum of the two float numbers
    jfloat sum = a + b;

    // Return the result as a float
    return sum;
}