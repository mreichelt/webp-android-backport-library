package de.marcreichelt.webp_backport.testapp;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

import de.marcreichelt.webp_backport.WebPBackport;


public class SimpleDecodeDemo extends AppCompatActivity {

    TextView status;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_decode_demo);

        status = (TextView) findViewById(R.id.status);
        image = (ImageView) findViewById(R.id.image);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new LoadWebPTask().execute(R.raw.test_lights_1280x853);
    }

    class LoadWebPTask extends AsyncTask<Integer, String, Bitmap> {

        @Override
        protected Bitmap doInBackground(Integer... params) {
            try {
                publishProgress(getString(R.string.loading_image));
                long start = SystemClock.elapsedRealtime();
                InputStream in = getResources().openRawResource(params[0]);
                byte[] encoded = IOUtils.toByteArray(in);
                long time = SystemClock.elapsedRealtime() - start;
                publishProgress(getString(R.string.status, time, WebPBackport.isLibraryUsed()));
                return WebPBackport.decode(encoded);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            image.setImageBitmap(bitmap);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            status.setText(values[0]);
        }

    }

}
