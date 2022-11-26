package com.example.imagedownloader;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSONS_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };
    EditText txtURL;
    Button btnDownload;
    ImageView imgView;
    ProgressDialog PD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtURL = findViewById(R.id.txtURL);
        btnDownload = (Button) findViewById(R.id.btnDownload);
        imgView = (ImageView) findViewById(R.id.imgView);


        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permission = ActivityCompat.checkSelfPermission(
                        MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permission != PackageManager.PERMISSION_GRANTED) {


                    ActivityCompat.requestPermissions(MainActivity.this, PERMISSONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
                }
                ImageDownloader imageDownloader = new ImageDownloader();
                imageDownloader.execute(txtURL.getText().toString());
            }

        });


    }


    class ImageDownloader extends AsyncTask<String, Integer, Bitmap> {

        //This method is executed in the Background;
        @Override
        protected Bitmap doInBackground(String... strings) {

            String fileName = "temp.jpg";


            String imagePath = (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)).toString() + "/" + fileName;
            Log.i("imagePath", imagePath);
            Bitmap image = download(strings[0], imagePath);

            return image;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            float w = bitmap.getWidth();
            float h = bitmap.getHeight();

            int W = 400;
            int H = (int) ((h * W) / w);
            Bitmap scaledImg = Bitmap.createScaledBitmap(bitmap, W, H, false);
            PD.dismiss();
            imgView.setImageBitmap(scaledImg);
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            PD = new ProgressDialog(MainActivity.this);
            PD.setMax(100);
            PD.setIndeterminate(false);
            PD.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            PD.setTitle("Downloading");
            PD.setMessage("Please Wait");
            PD.show();

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            PD.setProgress(values[0]);
        }


        private Bitmap download(String pushedurl, String imagePath) {
            try {
                /*URL url = new URL(pushedurl);
                URLConnection connection = url.openConnection();

                connection.connect();
*///
                java.net.URL url = new java.net.URL(pushedurl);
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
                //
            }
        }

    }
}