package com.example.coppyfolder;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.sqlcipher.database.SQLiteDatabase;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class inAppActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private Button choosseFile;
    private static final int FILE_SELECT_CODE = 0;
    String path = null;
    public List<String> list;
    SecureAsynctask secureAsynctask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.in_app);

//        toolbar= (Toolbar)findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        choosseFile= (Button)findViewById(R.id.bt_choose_file);
        choosseFile.setOnClickListener(this);

        SQLiteDatabase.loadLibs(this);

//        secureAsynctask=new SecureAsynctask(this);
//        secureAsynctask.execute();
        RecyclerView recyclerView= (RecyclerView)findViewById(R.id.recycle_view);
        try {
            // Your code, where the exception was thrown
            list= Database.getInstance(getApplicationContext()).getList();
            SecureAdapter secureAdapter= new SecureAdapter(list);
            recyclerView.setAdapter(secureAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            Log.d("nhungltk", "onCreate: "+list.size());
        } catch (Exception ex) {
            // Here we are logging the exception to see why it happened.
            Log.e("my app", ex.toString());
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==(R.id.bt_choose_file)) {
            showFileChooser();
            Log.d("nhungltk", "onClick: "+list.size());
        }
    }
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
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("nhungltk", "onActivityResult: ");
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
                    // Get the file instance
                    // File file = new File(path);
                    // Initiate the upload
                    if (path != null) {
                        try {
                            FileInputStream fileInputStream = new FileInputStream(path);
                            FileOutputStream fileOutputStream = new FileOutputStream(path + "e");
                            Database.getInstance(inAppActivity.this).insertPath(path, path+"e");
                            encrypt(fileInputStream, fileOutputStream);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (InvalidKeyException e) {
                            e.printStackTrace();
                        } catch (NoSuchPaddingException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
        }
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
}
