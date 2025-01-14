package com.example.catvsdogs;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int MY_CAMERA_REQUEST_CODE = 100;
    ImageView im0;
    ImageView im1;
    ImageView im2;
    ImageView im3;
    ImageView im4;
    ImageView im5;
    Button bSelFromGal;
    Button bSelFromCam;
    TextView tvTopHeading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageClassifier.init(getAssets(), "converted_model.tflite");
        bSelFromGal = findViewById(R.id.b_select_from_gal);
        bSelFromCam = findViewById(R.id.b_select_from_cam);
        tvTopHeading = findViewById(R.id.tv_top_heading);

        Typeface type = Typeface.createFromAsset(getAssets(), "Pacifico-Regular.ttf");
        bSelFromGal.setTypeface(type);
        bSelFromCam.setTypeface(type);
        tvTopHeading.setTypeface(type);

        bSelFromCam.setOnClickListener(this);
        bSelFromGal.setOnClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0: //select from camera
                if (resultCode == RESULT_OK) {
//                    Uri selectedImage = data.getData();
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    startActivity(new Intent(this, ResultActivity.class).putExtra("image-bitmap", bitmap));
                }
                break;
            case 1: //select from gallery
                if (resultCode == RESULT_OK) {
//                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    Uri imageUri = data.getData();
//                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    startActivity(new Intent(this, ResultActivity.class).putExtra("image-uri", imageUri));
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);//zero can be replaced with any action code (called requestCode)
            } else {
//                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
                Snackbar.make(findViewById(android.R.id.content), "Camera permission needed!", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onClick(View v) {

        Log.d("Namam", "onClick: "+ v.getId());
        if(v.getId() == R.id.b_select_from_cam){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
                Log.d("permission status", "onClick: denied");
            } else {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0); // zero can be replaced with any action code (called requestCode)
            }
        }
        if(v.getId() == R.id.b_select_from_gal){
            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto, 1);
        }

    }
}
