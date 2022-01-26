package com.netease.nersa;

import java.io.InputStream;

/**
 * author: wb.linjunyi
 * date:   On 2021/10/08.
 */
public interface INERsa {
    void setCert(byte[] data, boolean isUpdate);

    void setCert(InputStream inputStream, boolean isUpdate);

    String encrypt(String data);

    String decrypt(String data);
}
