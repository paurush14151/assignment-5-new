package com.example.paurush.assignment5;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Download extends AppCompatActivity {
    TextView display;
    FloatingActionButton fab_btn;
    String str;
    public String url_str = "https://iiitd.ac.in/about";
    Async_class as;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        display = (TextView) findViewById(R.id.display);
        fab_btn = (FloatingActionButton) findViewById(R.id.fab);
        fab_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager man = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info = man.getActiveNetworkInfo();
                if(info!=null && info.isConnected()){
                    as = new Async_class();
                    as.execute(url_str);
                    Snackbar.make(view, "Downloading data", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else {
                    display.setText("Network connectivity unavailable");
//                    display.loadData("Network connectivity unavailable", "text/html; charset=utf-8", "utf-8");
                }

            }
        });
    }
//    @Override
//    protected void onSaveInstanceState(Bundle savedInstanceState){
////        savedInstanceState.putString("DownloadedData", display.getText().toString());
//        super.onSaveInstanceState(savedInstanceState);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_download, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class Async_class extends AsyncTask<String,Void, String> {
        Download down;
        public String TAG = "data";

        protected void onPreExecute(){
            super.onPreExecute();
            display.setText("Retreiving data");
            //display.loadData("Retreiving data", "text/html; charset=utf-8", "utf-8");
        }

        @Override
        protected String doInBackground(String... string) {
            try {
                return fetch(string[0]);
            }
            catch (Exception e){
                e.printStackTrace();
                return "Error in URL!!";

            }
        }

        protected void onPostExecute(String data){
            //     Toast.makeText(Download.this,"Download complete.", Toast.LENGTH_SHORT).show();
            Log.d(TAG, data);
            display.setText(data);

            //display.loadData(data, "text/html; charset=utf-8", "utf-8");
        }

        private String fetch(String s) throws IOException {
            int size_partial = 600, size_whole = 5000,r;
            String partial_data, whole_data;
            InputStream stream = null;
//            display.setText(s);
            try {
                URL url = new URL(s);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setReadTimeout(10000);
                c.setConnectTimeout(15000);
                c.setRequestMethod("GET");
                c.setDoInput(true);
                c.connect();
                r = c.getResponseCode();
                stream = c.getInputStream();
                whole_data = readfile(stream,size_whole);
//                Log.d(TAG,"Downloaded data is: "+whole_data);
                partial_data = readfile(stream,size_partial);
                return whole_data;


            }
            finally {
                if (stream != null){
                    stream.close();
                }
            }
        }

        private String readfile(InputStream stream, int size) throws IOException{
            char[] store = new char[size];
            Reader r = new InputStreamReader(stream, "UTF-8");
            r.read(store);
            return new String(store);

        }

    }
}
