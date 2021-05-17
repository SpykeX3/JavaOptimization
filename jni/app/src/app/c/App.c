#include <jni.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>


#define BUFFER_SIZE 4096

JNIEXPORT jobject JNICALL Java_jni_App_getCpuInfo
  (JNIEnv *javaEnv,  jclass j_class) {

    jclass map = (*javaEnv)->FindClass(javaEnv, "java/util/HashMap");
    jsize map_len = 1;
    jmethodID init = (*javaEnv)->GetMethodID(javaEnv, map, "<init>", "(I)V");
    jobject hashMap = (*javaEnv)->NewObject(javaEnv, map, init, map_len);
    jmethodID put = (*javaEnv)->GetMethodID(javaEnv, map, "put",
                "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");

    char buf[BUFFER_SIZE];
    FILE* pipe = popen("sysctl -a | grep machdep.cpu", "r");
    if (!pipe) {
        perror ("IO error");
        return NULL;
    }

    while (fgets (buf, BUFFER_SIZE, pipe)) {
        char *keyString = strtok(buf, ": ");
        char *valueString = strtok(NULL, ": ");
        valueString[strcspn(valueString, "\n")] = 0;
        
        jstring key = (*javaEnv)->NewStringUTF(javaEnv, keyString);
        jstring value = (*javaEnv)->NewStringUTF(javaEnv, valueString);

        (*javaEnv)->CallObjectMethod(javaEnv, hashMap, put, key, value);

        (*javaEnv)->DeleteLocalRef(javaEnv, key);
        (*javaEnv)->DeleteLocalRef(javaEnv, value);
    }
    (*javaEnv)->DeleteLocalRef(javaEnv, map);
    fclose(pipe);
    return hashMap;
}