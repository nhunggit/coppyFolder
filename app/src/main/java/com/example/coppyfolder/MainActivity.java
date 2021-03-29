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

//public class MainActivity extends AppCompatActivity  {
    public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    String path = null;
    private static final int REQUEST_CODE = 0;
    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;
    int mode = 2;
    int state= 0;
    private final String ACTION_START_ACTIVITY="android.intent.action.startInAppActivity";

    //Bkav Nhugnltk
    private static final String SET_PROPERTIES="bkav.intent.action.SET_PROPERTIES";
    private static final String SET_EVENT="bkav.intent.action.SET_EVENT";
    private static final String PROPERTIES_NAME = "properties_name";
    private static final String EVENT_NAME = "properties_name";

    //Bkav Nhungltk:
    private static final String ACTION_OPEN_GALLERY="android.intent.action.OPEN_GALLERY";
//    CallBackPass callBackPass=new CallBackPass() {
//        @Override
//        public void setPass(String pass) {
//        }
//    };

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("nhungltk", "onCreate: ");
        setContentView(R.layout.activity_main);

        //Bkav Nhungltk
        Intent intent1=new Intent(SET_PROPERTIES);
        intent1.setComponent(new ComponentName("bkav.android.staticsticalbphone","bkav.android.staticsticalbphone.ReportDataReceiver"));
        intent1.putExtra(PROPERTIES_NAME,"CoppyFolder");
        sendBroadcast(intent1);

        Intent intent2=new Intent(SET_EVENT);
        intent2.setComponent(new ComponentName("bkav.android.staticsticalbphone","bkav.android.staticsticalbphone.ReportDataReceiver"));
        intent2.putExtra(EVENT_NAME,"onCreate");
        sendBroadcast(intent2);

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
                //try {
                Intent intent= new Intent();
                intent.setAction(ACTION_START_ACTIVITY);
                intent.setComponent(new ComponentName("com.example.coppyfolder","com.example.coppyfolder.SercureReceive"));
                sendBroadcast(intent);
                    Log.d("nhungltk", "onAuthenticationSucceeded: "+state);
                    //if(state==1) {
//                        checkPermission();
//                        showFileChooser();
////                        String fileEncrypt = Environment.getExternalStorageDirectory() + "/" + "nhung" + "/" + "nhung6.txt";
////                        FileInputStream fileInputStream = new FileInputStream(fileEncrypt);
////                        String fileout = Environment.getExternalStorageDirectory() + "/" + "nhung" + "/" + "nhunge3";
////                        FileOutputStream fileOutputStream = new FileOutputStream(fileout);
//                        FileInputStream fileInputStream = new FileInputStream(path);
//                        FileOutputStream fileOutputStream = new FileOutputStream(path + "e");
//                        //luu ten file ma hoa va giai ma vao db
//                        encrypt(fileInputStream, fileOutputStream);
                    //}else if(state==0){
//                        Log.d("nhungltk", "onAuthenticationSucceeded: ");
//                        checkPermission();
//                        FileInputStream fis = new FileInputStream(Environment.getExternalStorageDirectory() + "/" + "nhung" + "/" + "nhunge3");
//                        FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory() +"/" + "nhung" + "/" + "nhungd.txt                                                                 ");
//                        decrypt(fis,fos);
                   //}
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (NoSuchPaddingException e) {
//                    e.printStackTrace();
//                } catch (NoSuchAlgorithmException e) {
//                    e.printStackTrace();
//                } catch (InvalidKeyException e) {
//                    e.printStackTrace();
//                }
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
        SQLiteDatabase.loadLibs(this);

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
//            if(v.getId()==R.id.encrypt){
//                state=1;
//                try {
//                    authenticationKey(getCipher().ENCRYPT_MODE);
//                } catch (NoSuchPaddingException e) {
//                    e.printStackTrace();
//                } catch (NoSuchAlgorithmException e) {
//                    e.printStackTrace();
//                }
//            }else if(v.getId()==R.id.decypt){
//                state =0;
//                try {
//                    authenticationKey(getCipher().ENCRYPT_MODE);
//                } catch (NoSuchPaddingException e) {
//                    e.printStackTrace();
//                } catch (NoSuchAlgorithmException e) {
//                    e.printStackTrace();
//                }
//                //nhung.getInstance(this).insert();
//            }
            //else if(v.getId()==)
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

    interface CallBackPass{
        public void setPass(String pass);
    }

    static void encrypt(FileInputStream fis, FileOutputStream fos) throws IOException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException {

        // Length is 16 byte
        SecretKeySpec sks = new SecretKeySpec("MyDifficultPassw".getBytes(),
                "AES");
        // Create cipher
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, sks);
        // Wrap the output stream
        CipherOutputStream cos = new CipherOutputStream(fos, cipher);
        // Write bytes
        int b;
        byte[] d = new byte[8];
        while ((b = fis.read(d)) != -1) {
            cos.write(d, 0, b);
        }
        // Flush and close streams.
        cos.flush();
        cos.close();
        fis.close();
    }

    static void decrypt(FileInputStream fis, FileOutputStream fos) throws IOException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException {
        SecretKeySpec sks = new SecretKeySpec("MyDifficultPassw".getBytes(),
                "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, sks);
        CipherInputStream cis = new CipherInputStream(fis, cipher);
        int b;
        byte[] d = new byte[8];
        while ((b = cis.read(d)) != -1) {
            fos.write(d, 0, b);
        }
        fos.flush();
        fos.close();
        cis.close();
    }

    private static final int FILE_SELECT_CODE = 0;

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    Log.d("nhungltk", "File Uri: " + uri.toString());
                    // Get the path
                    try {
                        path = getPath(this, uri);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    Log.d("nhungltk", "File Path: " + path);
                    // Get the file instance
                    // File file = new File(path);
                    // Initiate the upload
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    @Override
    protected void onStart() {
        try {
            authenticationKey(getCipher().ENCRYPT_MODE);
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void chooseFile() throws FileNotFoundException {
        showFileChooser();
//                        String fileEncrypt = Environment.getExternalStorageDirectory() + "/" + "nhung" + "/" + "nhung6.txt";
//                        FileInputStream fileInputStream = new FileInputStream(fileEncrypt);
//                        String fileout = Environment.getExternalStorageDirectory() + "/" + "nhung" + "/" + "nhunge3";
//                        FileOutputStream fileOutputStream = new FileOutputStream(fileout);
        FileInputStream fileInputStream = new FileInputStream(path);
        FileOutputStream fileOutputStream = new FileOutputStream(path + "e");
        //luu ten file ma hoa va giai ma vao db
//        encrypt(fileInputStream, fileOutputStream);
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
