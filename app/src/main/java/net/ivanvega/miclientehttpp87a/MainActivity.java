package net.ivanvega.miclientehttpp87a;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity  implements DownloadCallback {
    RequestQueue queue;
    public static final String TAG = "MyTag";
    StringRequest stringRequest;
    ImageView img;


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

        img = findViewById(R.id.img);
        networkFragment = NetworkFragment.getInstance(getSupportFragmentManager(), "https://www.google.com");

        // Set the tag on the request.


        // Instantiate the RequestQueue.
         queue = Volley.newRequestQueue(this);
        String url ="http://www.google.com";

        // Request a string response from the provided URL.
        stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //textView.setText("Response is: "+ response.substring(0,500));
                        Toast.makeText(MainActivity.this,
                                "Response is: "+ response.substring(0,500),
                                Toast.LENGTH_SHORT).show();
                        Log.i("VOLLEYOPS",
                                "Response is: "+ response.substring(0,500));
                    }
                }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                           // textView.setText("That didn't work!");
                        }
        });

        stringRequest.setTag(TAG);

        // Add the request to the RequestQueue.
        queue.add(stringRequest);



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

        switch (view.getId()){
            case R.id.btnHTTP:
                startDownload();
                break;


            case R.id.btnImage:
                volleyImage();
                break;

            case R.id.btnjson:
                traerJSON();
                break;


        }

    }

    private void traerJSON() {


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                "https://simplifiedcoding.net/demos/view-flipper/heroes.php",
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }
        );
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void volleyImage() {
        ImageRequest imageRequest = new ImageRequest("https://keepcoding.io/es/wp-content/uploads/sites/4/2018/10/android-3d-820x400.jpg",
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        img.setImageBitmap(response);
                    }
                }, 200, 200, ImageView.ScaleType.CENTER, Bitmap.Config.ALPHA_8,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
                );
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(imageRequest);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (queue != null) {
            queue.cancelAll(TAG);
        }
    }
}
