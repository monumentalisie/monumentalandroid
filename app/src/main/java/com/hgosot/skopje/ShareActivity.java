package com.hgosot.skopje;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShareActivity extends BaseActivity {


    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 10;
    private static final String TAG ="share activity" ;
    @BindView(R.id.fl_container)
    FrameLayout flContainer;
    @BindView(R.id.iv_camera_picture)
    ImageView ivCameraPicture;
    @BindView(R.id.btn_share)
    Button btnShare;
    @BindView(R.id.btn_take_picture)
    Button btnCamera;

    private Uri photoURI;

    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(photoURI != null){
                    new ShareImageTask(ShareActivity.this).execute(createBitmapFromView(flContainer));
                }



            }
        });
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
// check sdk level
                if (checkPermissionForExtertalStorage()) {
                    openDeviceCamera();
                } else {

                    requestPermissionForExtertalStorage();


                }


            }
        });
    }


    public Bitmap createBitmapFromView(View view){

        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache().copy(Bitmap.Config.ARGB_8888,false);

        view.destroyDrawingCache();
        return bitmap;



    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case STORAGE_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openDeviceCamera();
                } else {
                    Toast.makeText(this, "Enable storage permission", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    public void requestPermissionForExtertalStorage() {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                STORAGE_PERMISSION_REQUEST_CODE);

    }


    private void openChooser(Uri uri) {

        if (!TextUtils.isEmpty(currentPhotoPath)) {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/*");

            share.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(share, "Share Image"));
        }


    }

    private void openDeviceCamera() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE
        );
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            // Error occurred while creating the File

        }
        if (photoFile != null) {

            photoURI = FileProvider.getUriForFile(this, "com.hgosot.skopje.provider", photoFile);
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(pictureIntent,
                    CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        }


    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE &&
                resultCode == RESULT_OK) {

            //  ivCameraPicture.setImageURI(Uri.parse(currentPhotoPath));

            Glide.with(this).load(photoURI).centerCrop().into(ivCameraPicture);


        }
    }

    public boolean checkPermissionForExtertalStorage() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED;
    }

    private void showErrorMsg() {
        Toast.makeText(this, "error_gallery", Toast.LENGTH_SHORT).show();
    }

    private static class ShareImageTask extends AsyncTask<Bitmap, Void, Uri> {

        private WeakReference<ShareActivity> weakReference;

        ShareImageTask(ShareActivity activity) {
            this.weakReference = new WeakReference<>(activity);
        }

        @Override
        protected Uri doInBackground(Bitmap... args) {
            try {
                ShareActivity activity = weakReference.get();
                if (activity != null) {
                    return saveCompositeImage(activity,args[0]);

                }
            } catch (Exception exception) {
                Log.e("zz", "Exception Loading GeoJSON: " + exception.toString());
            }
            return null;
        }

        public static  Uri saveCompositeImage(Activity activity,Bitmap image) {
            //TODO - Should be processed in another thread
            File imagesFolder = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            Uri uri = null;
            try {

                File file = new File(imagesFolder, "shared_image.png");

                FileOutputStream stream = new FileOutputStream(file);
                image.compress(Bitmap.CompressFormat.PNG, 90, stream);
                stream.flush();
                stream.close();
                uri = FileProvider.getUriForFile(activity, "com.hgosot.skopje.provider", file);

            } catch (IOException e) {
                Log.d(TAG, "IOException while trying to write file for sharing: " + e.getMessage());
            }
            return uri;
        }




        @Override
        protected void onPostExecute(@Nullable Uri uri) {
            super.onPostExecute(uri);
            ShareActivity activity = weakReference.get();
            if (activity != null && uri != null) {
                activity.openChooser(uri);
            }
        }
    }
}
