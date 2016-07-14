package com.mattikettu.pinkiponki.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.edmodo.cropper.CropImageView;
import com.mattikettu.pinkiponki.EditProfileActivity;
import com.mattikettu.pinkiponki.R;
import com.mattikettu.pinkiponki.util.Constants;
import com.mattikettu.pinkiponki.util.DateTimeUtil;
import com.mattikettu.pinkiponki.util.Injector;
import com.mattikettu.pinkiponki.util.ToastCreator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageCropperActivity extends AppCompatActivity {

    private static final String TAG = "IMAGECROPPERACTIVITY";
    private static final int originActivityEditProfile = 901;
    private static final int originActivityGame = 902;

    private int fromActivity = 999;

    private static final int aspectX = 7;
    private static final int aspectY = 4;

    private final int REQUEST_CODE_ASK_CAMERA = 666;

    private final int REQUEST_CAMERA = 601;
    private final int SELECT_FILE = 602;

    private File destination = null;
    private String picturename = null;

    @Inject
    protected DateTimeUtil DTU;
    @Inject
    protected ToastCreator toastCreator;

    @BindView(R.id.imagecropper_crop_image_view)
    CropImageView imagecropper_crop_image_view;

    @BindView(R.id.imagecropper_output_image_view)
    ImageView imagecropper_output_image_view;

    @BindView(R.id.imagecropper_camera_button)
    Button imagecropper_camera_button;

    @BindView(R.id.imagecropper_gallery_button)
    Button imagecropper_gallery_button;

    @BindView(R.id.imagecropper_crop_button)
    Button imagecropper_crop_button;

    private byte[] bitmapByteArray = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_cropper);

        Injector.inject(this);
        ButterKnife.bind(this);

        fromActivity = (int) getIntent().getIntExtra("originActivity", 777);

        imagecropper_crop_image_view.setAspectRatio(aspectX, aspectY);
        imagecropper_crop_image_view.setFixedAspectRatio(true);
        imagecropper_crop_image_view.setGuidelines(0);

        //Buttons
        imagecropper_camera_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCameraOnClick();
            }
        });
        imagecropper_gallery_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendGalleryOnClick();
            }
        });
        imagecropper_crop_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCropOnClick();
            }
        });
        FloatingActionButton imagecropper_fab = (FloatingActionButton) findViewById(R.id.imagecropper_fab);
        imagecropper_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendImageBack();
            }
        });
    }



    // Handle the buttons
    private void sendGalleryOnClick(){
        Log.d(TAG, "Sending to gallery");
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_CAMERA);
        }

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select file"), SELECT_FILE);
    }

    private void sendCameraOnClick(){
        Log.d(TAG, "Sending to camera");
        if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_ASK_CAMERA);
        }

        String fileName = "pinkiponki_" + DTU.getCurrentDateTime()  + ".jpg";
        destination = new File(Constants.picturepath, fileName);
        Uri uri = Uri.fromFile(destination);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        picturename = fileName;
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void sendCropOnClick(){
        final Bitmap croppedBM = imagecropper_crop_image_view.getCroppedImage();
        imagecropper_output_image_view.setImageBitmap(croppedBM);
        toastCreator.showToastLong("Updated image!");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        croppedBM.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        bitmapByteArray = baos.toByteArray();
    }

    private void sendImageBack(){
        if(bitmapByteArray!=null){
            switch (fromActivity){
                case originActivityEditProfile:
                    Log.d(TAG, "Length bitmap array: " + bitmapByteArray.length);
                    Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                    intent.putExtra("image", bitmapByteArray);
                    intent.putExtra("imagename", picturename);
                    startActivity(intent);
                    this.finish();
                    break;
                case originActivityGame:
                    this.finish();
                    break;
                default:
                    toastCreator.showToastLong("Can't figure out where I came from :///");
                    break;
            }
        }else{
            sendCropOnClick();
            sendImageBack();
        }
    }


    //Image selection
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == Activity.RESULT_OK){
            imagecropper_crop_image_view.setGuidelines(1);
            if(requestCode == REQUEST_CAMERA){
                onCaptureImageResult();
            }else if(requestCode == SELECT_FILE){
                onSelectFromGalleryResult(data);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void onCaptureImageResult(){
        Bitmap bm = null;
        if(destination.exists()){
            try {
                bm = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(destination));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Scale if size is too big!
        float originalHeight = bm.getHeight();
        float originalWidth = bm.getWidth();
        if(originalHeight > 2048 || originalWidth > 2048){
            Log.d(TAG, "Scaling start!");
            //scaling gogo!
            if(originalHeight > originalWidth){
                float factor = 2048 / originalHeight;
                bm = Bitmap.createScaledBitmap(bm, (int) (factor*originalWidth), 2048, false);
            }else{
                float factor = 2048 / originalWidth;
                bm = Bitmap.createScaledBitmap(bm, 2048, (int) (factor*originalHeight), false);
            }
        }

        imagecropper_crop_image_view.setImageBitmap(bm);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data){
        Bitmap bm = null;
        if(data != null){
            try {
                bm = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        imagecropper_crop_image_view.setImageBitmap(bm);
        if(picturename==null){
            picturename = "pinkiponki_" + DTU.getCurrentDateTime()  + ".jpg";
        }
    }
}
