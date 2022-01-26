package com.netease.nersa;


import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * author: wb.linjunyi
 * date:   On 2021/08/03.
 */
public class NERsaPrivate implements INERsa {
    private static final String TAG = NERsaPrivate.class.getSimpleName();
    private static final String DEFAULT_FILE_DIR_NAME = "/nersaPrivate";

    static {
        System.loadLibrary("nersa");
    }

    private String dirPath;

    private String privateCertName;
    private Context context;

    public NERsaPrivate(Context context, String privateCertName) {
        if (context == null) {
            return;
        }

        this.context = context.getApplicationContext();
        this.privateCertName = privateCertName + ".dat";
    }

    private void checkIfNeedSaveFile(InputStream inputStream, String fileName, Context context, boolean isUpdate) {
        if (context == null || inputStream == null || TextUtils.isEmpty(fileName)) {
            return;
        }

        FileOutputStream fos = null;
        try {
            String rootDir = context.getApplicationContext().getFilesDir().getAbsolutePath() + DEFAULT_FILE_DIR_NAME;
            dirPath = rootDir;
            File dir = new File(rootDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File(rootDir, fileName);
            if (file.exists()) {
                if (!isUpdate) {
                    return;
                }

                file.delete();
            }

            file.createNewFile();

            int index;
            fos = new FileOutputStream(file);
            byte[] buffer = new byte[1024 * 10];
            while ((index = inputStream.read(buffer)) != -1) {
                fos.write(buffer, 0, index);
                fos.flush();
            }
            inputStream.close();
            fos.close();
        } catch (Exception e) {
            Log.e(TAG, "checkIfNeedSaveFile: " + e.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.e(TAG, "checkIfNeedSaveFile: " + e.getMessage());
                }
            }

            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    Log.e(TAG, "checkIfNeedSaveFile: " + e.getMessage());
                }
            }
        }
    }

    public String rsaPrivateEncryptHexFile(String data) {
        return rsaPrivateEncryptHexFile(data, getPrivateKeyFileName());
    }

    public String rsaPrivateDecryptHexFile(String data) {
        return rsaPrivateDecryptHexFile(data, getPrivateKeyFileName());
    }

    private String getPrivateKeyFileName() {
        return dirPath + "/" + privateCertName;
    }

    private static native String rsaPrivateEncryptHexFile(String data, String path);

    private static native String rsaPrivateDecryptHexFile(String data, String path);

    @Override
    public void setCert(byte[] data, boolean isUpdate) {
        InputStream inputStream = new ByteArrayInputStream(data);
        checkIfNeedSaveFile(inputStream, privateCertName, context, isUpdate);
    }

    @Override
    public void setCert(InputStream inputStream, boolean isUpdate) {
        checkIfNeedSaveFile(inputStream, privateCertName, context, isUpdate);
    }

    @Override
    public String encrypt(String data) {
        return rsaPrivateEncryptHexFile(data);
    }

    @Override
    public String decrypt(String data) {
        return rsaPrivateDecryptHexFile(data);
    }
}
