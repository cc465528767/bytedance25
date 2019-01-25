package com.bytedance.camera.demo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.bytedance.camera.demo.utils.Utils;

import java.io.File;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class TakePictureActivity extends AppCompatActivity {
    private File image;
    private ImageView imageView;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private static final int REQUEST_EXTERNAL_STORAGE = 101;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_picture);
        imageView = findViewById(R.id.img);
        findViewById(R.id.btn_picture).setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(TakePictureActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(TakePictureActivity.this,
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                //todo 在这里申请相机、存储的权限
                    //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},REQUEST_IMAGE_CAPTURE);
                  ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_EXTERNAL_STORAGE);


            } else {
                takePicture();
            }
        });

    }

    private void takePicture() {
        //todo 打开相机

        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        image=Utils.getOutputMediaFile(MEDIA_TYPE_IMAGE);
        if(image!=null){
            Uri uri=
                    FileProvider.getUriForFile(this,"com.bytedance.camera.demo",image);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);

    }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            setPic();
        }
    }

    private void setPic() {
        //todo 根据imageView裁剪

        //todo 根据缩放比例读取文件，生成Bitmap

        //todo 如果存在预览方向改变，进行图片旋转

        //todo 如果存在预览方向改变，进行图片旋转
        int targetw=imageView.getWidth();
        int targeth=imageView.getHeight();
        BitmapFactory.Options bmOption= new BitmapFactory.Options();
        bmOption.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(image.getAbsolutePath(),bmOption);
        int photow=bmOption.outWidth;
        int photoh=bmOption.outHeight;
        int scaleFactor=Math.min(photow/targetw,photoh/targeth);
        bmOption.inJustDecodeBounds=false;
        bmOption.inSampleSize=scaleFactor;
        bmOption.inPurgeable=true;

        Bitmap bitmap=BitmapFactory.decodeFile(image.getAbsolutePath(),bmOption);
        imageView.setImageBitmap(bitmap);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                //todo 判断权限是否已经授予
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePicture();
                } else {
                     ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},REQUEST_IMAGE_CAPTURE);

                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
            }break;


            }
        }
    }


