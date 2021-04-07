package com.example.coppyfolder;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import net.sqlcipher.database.SQLiteDatabase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidParameterSpecException;
import java.util.concurrent.Executor;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

    public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    String path = null;
    private static final int REQUEST_CODE = 0;
    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;
    private final String ACTION_START_ACTIVITY="android.intent.action.startInAppActivity";

    private Button login;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login= (Button)findViewById(R.id.login);
        login.setOnClickListener(this);
//        Intent intent = new Intent(Intent.ACTION_RUN);
//        intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.password.ChooseLockGeneric"));
//        startActivity(intent);

        Executor executor = ContextCompat.getMainExecutor(this);
         biometricPrompt = new BiometricPrompt(MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(), "Authentication error: " + errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(), "Authentication succeeded", Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(getApplicationContext(),inAppActivity.class);
                startActivity(intent);
            }

            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                generateSecretKey(new KeyGenParameterSpec.Builder("keyname", KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT).setBlockModes(KeyProperties.BLOCK_MODE_CBC).setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7).setUserAuthenticationRequired(true).setInvalidatedByBiometricEnrollment(true).build());
            } catch (NoSuchProviderException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            }
        }

        promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("Biometric login for my app").setDescription("Login in using your biometric credential").setNegativeButtonText("Use account password").build();

//        Intent activityIntent = activity.getIntent();
//        Intent intent = new Intent(activity, SetupChooseLockGeneric.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
//
//        // Copy the original extras into the new intent
//        copyBooleanExtra(activityIntent, intent,
//               EXTRA_KEY_HAS_CHALLENGE, false);
//        copyBooleanExtra(activityIntent, intent,
//                EXTRA_SHOW_OPTIONS_BUTTON, false);
//        intent.putExtra(PASSWORD_TYPE_KEY, selectedLockType.defaultQuality);
//        intent.putExtra(EXTRA_KEY_CHALLENGE,
//                activityIntent.getLongExtra(EXTRA_KEY_CHALLENGE, 0));
//        startActivity(intent);
//        finish();
        SQLiteDatabase.loadLibs(this);

    }
        private static void copyBooleanExtra(Intent from, Intent to, String name,
                                             boolean defaultValue) {
            to.putExtra(name, from.getBooleanExtra(name, defaultValue));
        }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void generateSecretKey(KeyGenParameterSpec keyGenParameterSpec) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        keyGenerator.init(keyGenParameterSpec);
        keyGenerator.generateKey();
    }

    private SecretKey getSecretKey() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableKeyException {
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);
        return ((SecretKey) keyStore.getKey("keyname", null));
    }

    private Cipher getCipher() throws NoSuchPaddingException, NoSuchAlgorithmException {
        return Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
    }

    String[] REQUESTED_PERMISSION = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    //int REQUEST_CODE;
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(
                getApplicationContext(), String.valueOf(REQUESTED_PERMISSION)) !=
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    String.valueOf(REQUESTED_PERMISSION))) {
                // In an educational UI, explain to the user why your app requires this
                // permission for a specific feature to behave as expected. In this UI,
                // include a "cancel" or "no thanks" button that allows the user to
                // continue using your app without granting the permission.
                //showInContextUI(...);
            } else {
                // You can directly ask for the permission.
                ActivityCompat.requestPermissions(this, REQUESTED_PERMISSION, REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
                return;
            }
        }
    }

    @Override
    public void onClick(View v) {
            if(v.getId()==R.id.login) {
                try {
                    authenticationKey(getCipher().ENCRYPT_MODE);
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
    }

    public void authenticationKey(int mode){
        Cipher cipher = null;
        try {
            cipher = getCipher();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        SecretKey secretKey = null;
        try {
            secretKey = getSecretKey();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        }
        try {
            cipher.init(mode,secretKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        biometricPrompt.authenticate(promptInfo, new BiometricPrompt.CryptoObject(cipher));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    //    public void encryptDataBase(String passphrase) throws IOException {
//
//        File originalFile = context.getDatabasePath(DB_NAME);
//
//        File newFile = File.createTempFile("sqlcipherutils", "tmp", context.getCacheDir());
//
//        SQLiteDatabase existing_db = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, "", null, SQLiteDatabase.OPEN_READWRITE);
//
//        existing_db.rawExecSQL("ATTACH DATABASE '" + newFile.getPath() + "' AS encrypted KEY '" + passphrase + "';");
//        existing_db.rawExecSQL("SELECT sqlcipher_export('encrypted');");
//        existing_db.rawExecSQL("DETACH DATABASE encrypted;");
//
//        existing_db.close();
//
//        originalFile.delete();
//
//        newFile.renameTo(originalFile);
//
//    }

//    private void InitializeSQLCipher() {
//        SQLiteDatabase.loadLibs(this);
//        File databaseFile = getDatabasePath("demo.db");
//        databaseFile.mkdirs();
//        databaseFile.delete();
//        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(databaseFile, "test123", null);
//        database.execSQL("create table t1(a, b)");
//        database.execSQL("insert into t1(a, b) values(?, ?)", new Object[]{"one for the money",
//                "two for the show"});
//    }

}
