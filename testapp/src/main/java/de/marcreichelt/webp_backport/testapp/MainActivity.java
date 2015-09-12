package de.marcreichelt.webp_backport.testapp;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

import de.marcreichelt.webp_backport.WebPBackport;


public class MainActivity extends AppCompatActivity {

    TextView status;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        status = (TextView) findViewById(R.id.status);
        image = (ImageView) findViewById(R.id.image);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new LoadWebPTask().execute(R.raw.test_lights_1280x853);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
