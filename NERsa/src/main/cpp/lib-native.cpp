#include <jni.h>
#include "base64.h"
#include "encrypt/Rsa.h"

//
// Created by wb.linjunyi on 2021/8/3.
//

extern "C" {

char *jstringToChar(JNIEnv *env, jstring jstr) {
    char *rtn = nullptr;
    jclass clsstring = env->FindClass("java/lang/String");
    jstring strencode = env->NewStringUTF("UTF-8");
    jmethodID mid = env->GetMethodID(clsstring, "getBytes", "(Ljava/lang/String;)[B");
    auto barr = (jbyteArray) env->CallObjectMethod(jstr, mid, strencode);
    jsize alen = env->GetArrayLength(barr);
    jbyte *ba = env->GetByteArrayElements(barr, JNI_FALSE);
    if (alen > 0) {
        rtn = (char *) malloc(alen + 1);
        memcpy(rtn, ba, alen);
        rtn[alen] = 0;
    }
    env->ReleaseByteArrayElements(barr, ba, 0);
    return rtn;
}


jstring rsaPublicEncryptHexFile(JNIEnv *env, jclass clazz,
                                jstring data, jstring path) {
    if (data == nullptr || path == nullptr) {
        return env->NewStringUTF("");
    }

    char *data_char = jstringToChar(env, data);
    char *path_char = jstringToChar(env, path);
    if (data_char == nullptr || path_char == nullptr) {
        return env->NewStringUTF("");
    }
    std::string data_str = data_char;
    std::string path_str = path_char;
    if (data_str.empty() || path_str.empty()) {
        return env->NewStringUTF("");
    }
    std::string encrypted;
    Rsa::rsa_public_encrypt_hex_file(data_str, path_str, encrypted);
    if (encrypted.empty()) {
        return env->NewStringUTF("");
    }

    std::string base64 = base::encode64(encrypted);
    return env->NewStringUTF(base64.c_str());
}

jstring rsaPublicDecryptHexFile(JNIEnv *env, jclass clazz,
                                jstring data, jstring path) {
    if (data == nullptr || path == nullptr) {
        return env->NewStringUTF("");
    }

    char *data_char = jstringToChar(env, data);
    char *path_char = jstringToChar(env, path);
    if (data_char == nullptr || path_char == nullptr) {
        return env->NewStringUTF("");
    }
    std::string data_str = data_char;
    std::string path_str = path_char;
    if (data_str.empty() || path_str.empty()) {
        return env->NewStringUTF("");
    }

    std::string base64 = base::decode64(data_char);
    std::string encrypted;
    Rsa::rsa_public_decrypt_hex_file(base64, path_str, encrypted);
    if (encrypted.empty()) {
        return env->NewStringUTF("");
    }

    return env->NewStringUTF(encrypted.c_str());
}

jstring rsaPrivateEncryptHexFile(JNIEnv *env, jclass clazz,
                                 jstring data, jstring path) {
    if (data == nullptr || path == nullptr) {
        return env->NewStringUTF("");
    }

    char *data_char = jstringToChar(env, data);
    char *path_char = jstringToChar(env, path);
    if (data_char == nullptr || path_char == nullptr) {
        return env->NewStringUTF("");
    }
    std::string data_str = data_char;
    std::string path_str = path_char;
    if (data_str.empty() || path_str.empty()) {
        return env->NewStringUTF("");
    }
    std::string encrypted;
    Rsa::rsa_private_encrypt_hex_file(data_str, path_str, encrypted);
    if (encrypted.empty()) {
        return env->NewStringUTF("");
    }

    std::string base64 = base::encode64(encrypted);
    return env->NewStringUTF(base64.c_str());
}

jstring rsaPrivateDecryptHexFile(JNIEnv *env, jclass clazz,
                                 jstring data, jstring path) {
    if (data == nullptr || path == nullptr) {
        return env->NewStringUTF("");
    }

    char *data_char = jstringToChar(env, data);
    char *path_char = jstringToChar(env, path);
    if (data_char == nullptr || path_char == nullptr) {
        return env->NewStringUTF("");
    }
    std::string data_str = data_char;
    std::string path_str = path_char;
    if (data_str.empty() || path_str.empty()) {
        return env->NewStringUTF("");
    }

    std::string base64 = base::decode64(data_char);
    std::string encrypted;
    Rsa::rsa_private_decrypt_hex_file(base64, path_str, encrypted);
    if (encrypted.empty()) {
        return env->NewStringUTF("");
    }

    return env->NewStringUTF(encrypted.c_str());
}

jint RegisterNativesPublic(JNIEnv *env) {
    jclass clazzPublic = env->FindClass("com/netease/nersa/NERsaPublic");
    if (clazzPublic == nullptr) {
        return JNI_ERR;
    }
    JNINativeMethod methods_NERsaPublic[] = {
            {"rsaPublicEncryptHexFile", "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;", (void *) rsaPublicEncryptHexFile},
            {"rsaPublicDecryptHexFile", "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;", (void *) rsaPublicDecryptHexFile}
    };


    return env->RegisterNatives(clazzPublic, methods_NERsaPublic,
                                       sizeof(methods_NERsaPublic) /
                                       sizeof(methods_NERsaPublic[0]));
}

jint RegisterNativesPrivate(JNIEnv *env) {
    jclass clazzPrivate = env->FindClass("com/netease/nersa/NERsaPrivate");
    if (clazzPrivate == nullptr) {
        return JNI_ERR;
    }


    JNINativeMethod methods_NERsaPrivate[] = {
            {"rsaPrivateEncryptHexFile", "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;", (void *) rsaPrivateEncryptHexFile},
            {"rsaPrivateDecryptHexFile", "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;", (void *) rsaPrivateDecryptHexFile}
    };

    return env->RegisterNatives(clazzPrivate, methods_NERsaPrivate,
                                       sizeof(methods_NERsaPrivate) /
                                       sizeof(methods_NERsaPrivate[0]));
}

JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env = nullptr;
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR;
    }

    if (RegisterNativesPublic(env) < 0) {
        return -1;
    }

    if (RegisterNativesPrivate(env) < 0) {
        return -1;
    }

    return JNI_VERSION_1_6;
}

}