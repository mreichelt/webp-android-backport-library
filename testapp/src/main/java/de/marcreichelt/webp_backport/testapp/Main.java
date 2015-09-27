package de.marcreichelt.webp_backport.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Main extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        findViewById(R.id.simple_decode).setOnClickListener(this);
        findViewById(R.id.glide).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Class<?> clazz;
        switch (v.getId()) {
            case R.id.simple_decode:
                clazz = SimpleDecodeDemo.class;
                break;

            case R.id.glide:
                clazz = GlideDemo.class;
                break;

            default:
                return;
        }

        startActivity(new Intent(this, clazz));
    }

}
