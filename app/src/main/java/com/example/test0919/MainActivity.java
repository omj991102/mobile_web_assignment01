package com.example.test0919;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends AppCompatActivity
{
    ImageView imgView;
    String imageUrl = "http://kiokahn.synology.me:30000/uploads/-/system/appearance/logo/1/Gazzi_Labs_CI_type_B_-_big_logo.png";
    Bitmap bmImg = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgView = findViewById(R.id.imgView);
    }

    public void onClickForLoad(View v)
    {
        new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    URL myFileUrl = new URL(imageUrl);
                    HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();

                    bmImg = BitmapFactory.decodeStream(is);
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {   imgView.setImageBitmap(bmImg); }
                    });
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        Toast.makeText(getApplicationContext(), "Load", Toast.LENGTH_LONG).show();
    }

    public void onClickForSave(View v)
    {
        saveBitmaptoJpeg(bmImg, "DCIM", "image");
        Toast.makeText(getApplicationContext(), "Save", Toast.LENGTH_LONG).show();
    }

    public static void saveBitmaptoJpeg(Bitmap bitmap, String folder, String name)
    {
        String ex_storage = Environment.getExternalStorageDirectory().getAbsolutePath();
        String foler_name = "/" + folder + "/";
        String file_name = name + ".jpg";
        String string_path = ex_storage + foler_name;
        Log.d("경로", string_path);
        File file_path;
        file_path = new File(string_path);

        if (!file_path.exists())
            file_path.mkdirs();
        try
        {
            FileOutputStream out = new FileOutputStream(string_path + file_name);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
        } catch (FileNotFoundException exception) {
            Log.e("FileNotFoundException", exception.getMessage());
        } catch (IOException exception) {
            Log.e("IOException", exception.getMessage());
        }
    }
}