package com.aliazaz.aliSecretCameraPicture.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.aliazaz.aliSecretCameraPicture.R;
import com.aliazaz.aliSecretCameraPicture.listeners.OnPictureCapturedListener;
import com.aliazaz.aliSecretCameraPicture.services.PictureService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity implements OnPictureCapturedListener, ActivityCompat.OnRequestPermissionsResultCallback {


    public static final int MY_PERMISSIONS_REQUEST_ACCESS_CODE = 1;

    private Boolean exit = false;

    Vibrator vib;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPref = getSharedPreferences("images",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        String dt = sharedPref.getString("dt",new SimpleDateFormat("dd-MM-yy").format(new Date()).toString());

        if(dt != new SimpleDateFormat("dd-MM-yy").format(new Date()).toString()){
            editor.putString("dt",new SimpleDateFormat("dd-MM-yy").format(new Date()).toString());
            editor.putInt("name",1);
        }

        checkPermissions();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Button btn = (Button) findViewById(R.id.startCaptureBtn);
        btn.setOnClickListener(v ->
                new PictureService().startCapturing(this, this, sharedPref.getInt("name",0))
        );

        vib = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);

    }

    private void showToast(final String text) {
        runOnUiThread(() ->
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show()
        );
    }

    @Override
    public void onDoneCapturingAllPhotos(TreeMap<String, byte[]> picturesTaken) {
        if (picturesTaken != null && !picturesTaken.isEmpty()) {
            showToast("Done capturing all photos!");
            return;
        }
        showToast("No camera detected!");
    }

    @Override
    public void onCaptureDone(String pictureUrl, byte[] pictureData) {
        if (pictureData != null && pictureUrl != null) {
            showToast("Picture saved");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
            if (exit) {
                showToast("Capturing");
                vib.vibrate(500);

                new PictureService().startCapturing(this,this,sharedPref.getInt("name",1));

            } else {
                exit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exit = false;
                    }
                }, 1 * 1000);

            }
        }
        else if (keyCode == KeyEvent.KEYCODE_BACK){
            super.onBackPressed();
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_CODE: {
                if (!(grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    checkPermissions();
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermissions() {
        final String[] requiredPermissions = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
        };
        final List<String> neededPermissions = new ArrayList<>();
        for (final String p : requiredPermissions) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    p) != PackageManager.PERMISSION_GRANTED) {
                neededPermissions.add(p);
            }
        }
        if (!neededPermissions.isEmpty()) {
            requestPermissions(neededPermissions.toArray(new String[]{}),
                    MY_PERMISSIONS_REQUEST_ACCESS_CODE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {

            startActivity(new Intent(this,SettingActivity.class));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

