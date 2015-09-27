package de.marcreichelt.webp_backport.testapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.resource.bitmap.ImageVideoBitmapDecoder;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class GlideDemo extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.glide_demo);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.addItemDecoration(new MarginDecoration(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(new GlideDemoAdapter(10000));
    }

    private class GlideDemoAdapter extends RecyclerView.Adapter<GlideDemoViewHolder> {

        private final int n;
        private final byte[] imageData;

        public GlideDemoAdapter(int n) {
            this.n = n;
            InputStream in = getResources().openRawResource(R.raw.test_lights_150x100);
            try {
                imageData = IOUtils.toByteArray(in);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public GlideDemoViewHolder onCreateViewHolder(ViewGroup parent, int position) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new GlideDemoViewHolder(inflater.inflate(R.layout.glide_demo_item, parent, false));
        }

        @Override
        public void onBindViewHolder(GlideDemoViewHolder viewHolder, int position) {
            Context context = getApplicationContext();
            Glide.with(context)
                    .load(imageData)
                    .placeholder(R.drawable.ic_launcher)
                    .decoder(new GlideWebPBackportDecoder(context, DecodeFormat.PREFER_ARGB_8888))
                    .into(viewHolder.image);
        }

        @Override
        public int getItemCount() {
            return n;
        }

    }

    private static class GlideDemoViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;

        public GlideDemoViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
        }

    }

}
