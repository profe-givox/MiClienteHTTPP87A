package net.ivanvega.miclientehttpp87a;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity  implements DownloadCallback {



    // Keep a reference to the NetworkFragment, which owns the AsyncTask object
    // that is used to execute network ops.
    private NetworkFragment networkFragment;

    // Boolean telling us whether a download is in progress, so we don't trigger overlapping
    // downloads with consecutive button clicks.
    private boolean downloading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        networkFragment = NetworkFragment.getInstance(getSupportFragmentManager(), "https://www.google.com");


    }

    private void startDownload() {
        if (!downloading && networkFragment != null) {
            // Execute the async download.
            Toast.makeText(this,
                    "Inicio", Toast.LENGTH_SHORT).show();
            networkFragment.startDownload();
            downloading = true;
        }
    }

    @Override
    public void updateFromDownload(Object result) {
        Toast.makeText(this,
                "aCTUALIZACION: "+result, Toast.LENGTH_SHORT).show();
        Log.i("LARESPUESTA", result.toString());
    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    @Override
    public void onProgressUpdate(int progressCode, int percentComplete) {
        switch(progressCode) {
            // You can add UI behavior for progress updates here.
            case Progress.ERROR:
//                ...
                break;
            case Progress.CONNECT_SUCCESS:
//                ...
                break;
            case Progress.GET_INPUT_STREAM_SUCCESS:
//                ...
                break;
            case Progress.PROCESS_INPUT_STREAM_IN_PROGRESS:
//                ...
                break;
            case Progress.PROCESS_INPUT_STREAM_SUCCESS:
//                ...
                break;
        }

    }

    @Override
    public void finishDownloading() {
        downloading = false;
        if (networkFragment != null) {
            networkFragment.cancelDownload();
        }

    }

    public void click(View view) {

        startDownload();
    }
}
