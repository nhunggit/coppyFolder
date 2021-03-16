package com.example.coppyfolder;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.concurrent.Executor;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class MainActivity extends AppCompatActivity  {
   // public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static final String BOS_SERVICE = "bos";
    private static final String OLD_PATH_1 = "BkavData" + "/" + "Quay màn hình";
    String[] a = {"nhung", "nhung1"};
    private static final String[] oldPath = {"BkavData" + "/" + "Quay màn hình", "BkavData" + "/" + "ChimLac" + "/" + "Logs", "BkavData" + "/" + "ScreenRecord", "BkavData" + "/" + "Logs"};
    private static final String[] standardPath = {"Movies" + "/" + "Quay màn hình", "Android" + "/" + "data" + "android.bkav.bchrome" + "/" + "Log", "Movies" + "/" + "ScreenRecord", "Android" + "/" + "Logs"};
    private static final int REQUEST_CODE = 0;
    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;
    int mode = 0;

    //Bkav Nhugnltk
    private static final String SET_PROPERTIES="bkav.intent.action.SET_PROPERTIES";
    private static final String SET_EVENT="bkav.intent.action.SET_EVENT";
    private static final String PROPERTIES_NAME = "properties_name";
    private static final String EVENT_NAME = "properties_name";
//    CallBackPass callBackPass=new CallBackPass() {
//        @Override
//        public void setPass(String pass) {
//        }
//    };

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("nhungltk", "onCreate: ");

        //Bkav Nhungltk
        Intent intent1=new Intent(SET_PROPERTIES);
        intent1.setComponent(new ComponentName("bkav.android.staticsticalbphone","bkav.android.staticsticalbphone.ReportDataReceiver"));
        intent1.putExtra(PROPERTIES_NAME,"CoppyFolder");
        sendBroadcast(intent1);

        Intent intent2=new Intent(SET_EVENT);
        intent2.setComponent(new ComponentName("bkav.android.staticsticalbphone","bkav.android.staticsticalbphone.ReportDataReceiver"));
        intent2.putExtra(EVENT_NAME,"onCreate");
        sendBroadcast(intent2);

//        Button login=findViewById(R.id.encrypt);
//        Button decrpyt=findViewById(R.id.decypt);
//        Executor executor = ContextCompat.getMainExecutor(this);
//         biometricPrompt = new BiometricPrompt(MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
//            @Override
//            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
//                super.onAuthenticationError(errorCode, errString);
//                Toast.makeText(getApplicationContext(), "Authentication error: " + errString, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
//                super.onAuthenticationSucceeded(result);
//                Toast.makeText(getApplicationContext(), "Authentication succeeded", Toast.LENGTH_SHORT).show();
//                try {
//                    byte[] encryptedInfo = result.getCryptoObject().getCipher().doFinal("nhung cute".getBytes(Charset.defaultCharset()));
//                    checkPermission();
//                    String fileEncrypt = Environment.getExternalStorageDirectory() + "/" + "nhung" + "/" + "nhung1.txt";
//                    FileInputStream fileInputStream= new FileInputStream(fileEncrypt);
//                    String fileout= Environment.getExternalStorageDirectory() + "/" + "nhung" + "/" + "nhunge";
//                    FileOutputStream fileOutputStream= new FileOutputStream(fileout);
//                    CipherOutputStream cos = new CipherOutputStream(fileOutputStream, result.getCryptoObject().getCipher());
//                    callBackPass.setPass(result.getCryptoObject().getCipher().toString());
//                     //Write bytes
//                    int b;
//                    byte[] d = new byte[8];
//                    while((b = fileInputStream.read(d)) != -1) {
//                        cos.write(d, 0, b);
//                    }
//                    fileOutputStream.flush();
//                    fileOutputStream.close();
//                    fileInputStream.close();
//                } catch (BadPaddingException e) {
//                    e.printStackTrace();
//                } catch (IllegalBlockSizeException e) {
//                    e.printStackTrace();
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            public void onAuthenticationFailed() {
//                super.onAuthenticationFailed();
//                Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            try {
//                generateSecretKey(new KeyGenParameterSpec.Builder("keyname", KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT).setBlockModes(KeyProperties.BLOCK_MODE_CBC).setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7).setUserAuthenticationRequired(true).setInvalidatedByBiometricEnrollment(true).build());
//            } catch (NoSuchProviderException e) {
//                e.printStackTrace();
//            } catch (NoSuchAlgorithmException e) {
//                e.printStackTrace();
//            } catch (InvalidAlgorithmParameterException e) {
//                e.printStackTrace();
//            }
//        }
//
//
//        promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("Biometric login for my app").setDescription("Login in using your biometric credential").setNegativeButtonText("Use account password").build();
//        login.setOnClickListener(this);
//        SQLiteDatabase.loadLibs(this);
//
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    private void generateSecretKey(KeyGenParameterSpec keyGenParameterSpec) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
//        KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
//        keyGenerator.init(keyGenParameterSpec);
//        keyGenerator.generateKey();
//    }
//
//    private SecretKey getSecretKey() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableKeyException {
//        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
//        keyStore.load(null);
//        return ((SecretKey) keyStore.getKey("keyname", null));
//    }
//
//    private Cipher getCipher() throws NoSuchPaddingException, NoSuchAlgorithmException {
//        return Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
//    }
//
//    String[] REQUESTED_PERMISSION = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
//
//    //int REQUEST_CODE;
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    public void checkPermission() {
//        if (ContextCompat.checkSelfPermission(
//                getApplicationContext(), String.valueOf(REQUESTED_PERMISSION)) !=
//                PackageManager.PERMISSION_GRANTED) {
//            // You can use the API that requires the permission.
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    String.valueOf(REQUESTED_PERMISSION))) {
//                // In an educational UI, explain to the user why your app requires this
//                // permission for a specific feature to behave as expected. In this UI,
//                // include a "cancel" or "no thanks" button that allows the user to
//                // continue using your app without granting the permission.
//                //showInContextUI(...);
//            } else {
//                // You can directly ask for the permission.
//                ActivityCompat.requestPermissions(this, REQUESTED_PERMISSION, REQUEST_CODE);
//            }
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case REQUEST_CODE: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                } else {
//                }
//                return;
//            }
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//            if(v.getId()==R.id.encrypt){
//                mode =1;
//                try {
//                    authenticationKey(getCipher().ENCRYPT_MODE);
//                } catch (NoSuchPaddingException e) {
//                    e.printStackTrace();
//                } catch (NoSuchAlgorithmException e) {
//                    e.printStackTrace();
//                }
//            }else if(v.getId()==R.id.decypt){
//                nhung.getInstance(this).insert();
//            }
//            //else if(v.getId()==)
//    }
//    public void authenticationKey(int mode){
//        Cipher cipher = null;
//        try {
//            cipher = getCipher();
//        } catch (NoSuchPaddingException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        SecretKey secretKey = null;
//        try {
//            secretKey = getSecretKey();
//        } catch (KeyStoreException e) {
//            e.printStackTrace();
//        } catch (CertificateException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (UnrecoverableKeyException e) {
//            e.printStackTrace();
//        }
//        try {
//            cipher.init(mode,secretKey);
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//        }
//        biometricPrompt.authenticate(promptInfo, new BiometricPrompt.CryptoObject(cipher));
//    }
//
//    interface CallBackPass{
//        public void setPass(String pass);
//    }
//
////    public void encryptDataBase(String passphrase) throws IOException {
////
////        File originalFile = context.getDatabasePath(DB_NAME);
////
////        File newFile = File.createTempFile("sqlcipherutils", "tmp", context.getCacheDir());
////
////        SQLiteDatabase existing_db = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, "", null, SQLiteDatabase.OPEN_READWRITE);
////
////        existing_db.rawExecSQL("ATTACH DATABASE '" + newFile.getPath() + "' AS encrypted KEY '" + passphrase + "';");
////        existing_db.rawExecSQL("SELECT sqlcipher_export('encrypted');");
////        existing_db.rawExecSQL("DETACH DATABASE encrypted;");
////
////        existing_db.close();
////
////        originalFile.delete();
////
////        newFile.renameTo(originalFile);
////
////    }
//
////    private void InitializeSQLCipher() {
////        SQLiteDatabase.loadLibs(this);
////        File databaseFile = getDatabasePath("demo.db");
////        databaseFile.mkdirs();
////        databaseFile.delete();
////        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(databaseFile, "test123", null);
////        database.execSQL("create table t1(a, b)");
////        database.execSQL("insert into t1(a, b) values(?, ?)", new Object[]{"one for the money",
////                "two for the show"});
////    }
//
//}

//        String fileEncrypt = Environment.getExternalStorageDirectory() + "/" + "Movies" + "/" + "Quay màn hình" + "/" + "Quay màn hình" + "/" + "ScreenRecord_2021-03-01-15-45-39.mp4";
//        try {
//            FileOutputStream fileOutputStream = new FileOutputStream(fileEncrypt);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        try {
            upgradeToVersion2();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Bkav Nhungltk: OTA -end


    //Bkav Nhungltk: OTA -start
    //copy thu muc tu vi tri nay sang vi tri khac
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void upgradeToVersion2() throws IOException {
        //thuc hien viec upgradeToVersion2
        for (int i = 0; i < oldPath.length; i++) {
            String pathSource = Environment.getExternalStorageDirectory() + "/" + oldPath[i];
            String pathDes = Environment.getExternalStorageDirectory() + "/" + standardPath[i];
            File source = new File(pathSource);
            File des = new File(pathDes);
            if (source.exists()) {
                try {
                    coppyFolder(pathSource, pathDes);
                    deleteDirectory(source.getParentFile());
                    Log.d("nhungltk", "upgradeToVersion2: "+source.getParentFile());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        //Bkav NhungLTk: xoa thu muc BkavData
//        String source = Environment.getExternalStorageDirectory() + "/" + "BkavData";
//        File delete = new File(source);
//        deleteDirectory(delete);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void coppyFolder(String pathSource, String pathDestiation) throws FileNotFoundException {
        try {
            File src = new File(pathSource);
            File dst = new File(pathDestiation);
            Log.d("nhungltk", "coppyFolder: " + src);
            if (src.isDirectory()) {
                String files[] = src.list();
                if (files == null) {
                    Log.d("nhungltk", "coppyFolder: " + src.listFiles());
                } else{
                    int filesLength = files.length;
                for (int i = 0; i < filesLength; i++) {
                    String src1 = (new File(src, files[i])).getPath();
                    String dst1 = (new File(dst, files[i])).getPath();
                    coppyFolder(src1, dst1);
                }
            }
            } else {
                Log.d("nhungltk", "coppyFolder: des " + dst);
                copyFile(src, dst);
                deleteDirectory(src);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();
        if (!destFile.exists()) {
            destFile.getParentFile().mkdirs();
        }

        String sourcePath = sourceFile.getPath();
        InputStream source = new FileInputStream(sourcePath);
        String destinationPath = destFile.getPath();
        OutputStream destination = new FileOutputStream(destinationPath);
        try {
            FileUtils.copy(source, destination);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void deleteDirectory(File delete) {
        if (delete.exists()) {
            File[] files = delete.listFiles();
            if (files == null) {
            }
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
                files[i].delete();
            }
        }
        delete.delete();
    }


}
