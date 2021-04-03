package com.example.coppyfolder;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.sqlcipher.database.SQLiteDatabase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class inAppActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private Button choosseFile;
    private static final int FILE_SELECT_CODE = 0;
    String path = null;
    public List<Path> list;
    SecureAdapter secureAdapter;
    process process;
    Database database;
    private String folderSecure= Environment.getExternalStorageDirectory() +
            File.separator + ".Secure";
    private String folderEncrypt= folderSecure + File.separator+".Encrypt";
    private String folderDecrypt= folderSecure + File.separator+".Decrypt";
    private File encryptFolder;
    private File decryptFolder;
    callbackListener callbackListener= new callbackListener() {
        @Override
        public void callback(int posistion) {
            FragmentManager manager = getSupportFragmentManager();
            fragmentExternalView fragmentExternalView= new fragmentExternalView(posistion,list);
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.fragment, fragmentExternalView).addToBackStack(null);
            transaction.commit();
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.in_app);

//        toolbar= (Toolbar)findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        process = new process();

        choosseFile= (Button)findViewById(R.id.bt_choose_file);
        choosseFile.setOnClickListener(this);

        SQLiteDatabase.loadLibs(this);
        database=Database.getInstance(this);
        list=Database.getInstance(this).getList();
        Log.d("nhungltk", "onCreate: "+list);

        secureAdapter= new SecureAdapter(list,callbackListener);

        FragmentManager manager = getSupportFragmentManager();
        AllFileFragment fragment= new AllFileFragment(secureAdapter, callbackListener);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment, fragment);
        transaction.commit();

        File folder = new File(folderSecure);
        boolean success = true;
        if (!folder.exists()) {
            success=folder.mkdirs();
        }
        if(success){
            encryptFolder = new File(folderEncrypt);
            if(!encryptFolder.exists()){
                encryptFolder.mkdirs();
            }
            decryptFolder= new File(folderDecrypt);
            if(!decryptFolder.exists()){
                decryptFolder.mkdirs();
            }
        }

        try {
            process.decryptFolder(folderEncrypt,folderDecrypt);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==(R.id.bt_choose_file)) {
            showFileChooser();
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

    @RequiresApi(api = Build.VERSION_CODES.Q)
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
                        path = process.getPath(this, uri);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    // Get the file instance
                    // File file = new File(path);
                    // Initiate the upload
                    if (path != null) {
                        try {
                            File origal= new File(path);
                            String name =origal.getName();
                            String encrypt= folderEncrypt + File.separator + name;
                            String decrypt= folderDecrypt + File.separator + name;
                            FileInputStream fileInputStream = new FileInputStream(path);
                            FileOutputStream fileOutputStream = new FileOutputStream(folderEncrypt+File.separator+name);

                            /*Bkav Nhungltk: ma hoa file
                            copy file vao .Decrypt de view trong app
                            xoa file o vi tri cu
                            insert thong tin file vao database.
                            */
                            process.encrypt(fileInputStream, fileOutputStream);
                            process.copyFile(new File(path),new File(decrypt));
                            database.insertPath(encrypt, path, decrypt);
                            process.encrypt(fileInputStream, fileOutputStream);
                            list.add(0, new Path(encrypt,path,decrypt));
                            origal.delete();

                            secureAdapter.notifyItemInserted(0);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("nhungltk", "onDestroy: parent");
        process.deleteDirectory(decryptFolder);
    }

    interface callbackListener{
        void callback(int posistion);
    }
}
