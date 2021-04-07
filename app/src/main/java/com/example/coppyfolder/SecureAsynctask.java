package com.example.coppyfolder;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SecureAsynctask extends AsyncTask<Void, List<Path>, Void> {
    public List<Path> list;
    Activity contextParent;

    public SecureAsynctask(Activity contextParent) {
        this.contextParent = contextParent;
    }

    @Override
    protected Void doInBackground(Void... voids) {
       //list= Database.getInstance(contextParent).getList();
        publishProgress(list);
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected void onProgressUpdate(List<Path> values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Toast.makeText(contextParent, "Okie, Finished", Toast.LENGTH_SHORT).show();
    }
}
