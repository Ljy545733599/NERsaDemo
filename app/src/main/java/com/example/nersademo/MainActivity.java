package com.example.nersademo;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.netease.nersa.NERsaPrivate;
import com.netease.nersa.NERsaPublic;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCPjb50AZWT6ivg7pbGh018s8Q35go5lIT27k6eftXPBRUl8LJ0LVSOlcgfmM0xA8r9ShwvAx8qzVJjw/pHafp0rHkgKYRILB7LNbDVUZSBk9SX53qzUg39zAWRi7aRjIA9WM4aS8eTFUrrUNB1soXAiRerAEfxe/pPrC7y90BDIQIDAQAB";
    private static final String PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAIvvYTmBt1ZQAp5vvUgVY+J5Q/f8DWqoBzx9NKq8NS+MGp08dfTyLCxVZOkCma/qHjpwNWSww1CyLiCVkJca65hgFIOc0RCa8urKcqzx837TbqSadQeVQCSwCCQkfoGUR0yBW/5U1Rnz4KBKL3SSsfRwjXmAOW5zsI0c1ffwKLLTAgMBAAECgYBBVlwHBdIcsu8GGxY9+f7RV92shDufeSUn0S4uKgKLWNd0Yy6QWCLX8GdVhKlNwfqrPokvonkPzKJNBIimhSNLKT0NwBDJXPL9EMdO9Ipw+zNS7Fepb9BdUO5De1Kc5m+692/1qAWbEkimmkCkvHPHRyMiGnQcQCPLGY7AvQMlwQJBAMk42E0dIjK1n5GyZcXKIwmqnbnhKA+roMCwoQUyVBXyL2pNRtnzFUWQRpKvGOX0zuJj37y8q8FdPraSvbehz/UCQQCyB3LZHlXDW+4X2lC2BZKfOvlLLCc0yGieD6lTLstePDRjVL9x0f8gdkqRF0oY0szZ56qEDfA9M2vet7rkXaKnAkEAoZ1LdRwqJGplwCJ1xVdlrtTfG7UwdO2XDkyO91qF2J6M4KcyecjYU+feFuAVclSeeYxX3gH3PTYEMaVIRR3spQJAOfgq8X/rNT3N/X5dIagW4jp1yoCKE6neRvmajTeDiR/Zfy6tWAalKJDcHvqSahraEWpaH73aKFOCIHd7J7lFqQJBAKHOtFCK0kVJv1oc/NYPVvvy6DWM9W/lbtz6PD37FMLkBKfrvYiVQVJ0wP1oW1ngh0AdKsN/m7SauaeZKrSN3lI=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String test = "这是一条加密的内容";

        InputStream publicIs = null;
        InputStream privateIs = null;
        try {
            publicIs = getAssets().open("key_public.dat");
            privateIs = getAssets().open("key_private.dat");
        } catch (IOException e) {
            e.printStackTrace();
        }

        NERsaPrivate neRsaPrivate = new NERsaPrivate(this, "nersa_dmeo_key_private.dat");
        neRsaPrivate.setCert(privateIs, false);

        NERsaPublic neRsaPublic = new NERsaPublic(this, "nersa_dmeo_key_public.dat");
        neRsaPublic.setCert(publicIs, false);

        String encryptedPrivate = neRsaPrivate.rsaPrivateEncryptHexFile(test);
        Log.e("test", "neRsaPrivate encrypted:" + encryptedPrivate);

        String decryptedPrivate = neRsaPublic.rsaPublicDecryptHexFile(encryptedPrivate);
        Log.e("test", "neRsaPublic decrypted: " + decryptedPrivate);

        String encryptedPublic = neRsaPublic.rsaPublicEncryptHexFile(test);
        Log.e("test", "neRsaPublic encrypted:" + encryptedPublic);

        String decryptedPublic = neRsaPrivate.rsaPrivateDecryptHexFile(encryptedPublic);
        Log.e("test", "neRsaPrivate decrypted: " + decryptedPublic);
    }
}