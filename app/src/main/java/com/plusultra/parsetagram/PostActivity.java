package com.plusultra.parsetagram;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.plusultra.parsetagram.model.Post;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class PostActivity extends AppCompatActivity {
    private EditText descriptionIn;
    private Button createButton, postButton;
    public Uri file;
    public File photoFile;
    public final String APP_TAG = "Xstagram";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    public String photoFileName = "photo.jpg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        descriptionIn = findViewById(R.id.etDescription);
        createButton = findViewById(R.id.btnCreate);
        postButton = findViewById(R.id.btnPost);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            createButton.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLaunchCamera(view);
            }
        });
    }

    public void onLaunchCamera(View view) {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference to access to future access
        photoFile = getPhotoFileUri(photoFileName);

//        // wrap File object into a content provider
//        // required for API >= 24
//        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
//        Uri fileProvider = FileProvider.getUriForFile(PostActivity.this, "com.codepath.fileprovider", photoFile);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bundle extras = data.getExtras();
                Bitmap takenImage = (Bitmap) extras.get("data");
                // RESIZE BITMAP, see section below
                // turn the image into a format recognized by ParseFile
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                takenImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] image = stream.toByteArray();
                final ParseFile parseFile = new ParseFile(image);
                // Load the taken image into a preview
                ImageView ivPicture = findViewById(R.id.ivPicture);;
                ivPicture.setImageBitmap(takenImage);

                postButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        createPost(descriptionIn.getText().toString(), parseFile, ParseUser.getCurrentUser());
                        final Intent intent = new Intent(PostActivity.this, TimelineActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(APP_TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    private void createPost(String description, ParseFile imageFile, ParseUser user){
        // final Intent intent = new Intent(this, TimelineActivity.class);
        final Post newPost = new Post();
        newPost.setDescription(description);
        newPost.setImage(imageFile);
        newPost.setUser(user);

        newPost.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){
//                    startActivity(intent);
//                    finish();
                    Log.d("PostActivity", "Create post success");
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}