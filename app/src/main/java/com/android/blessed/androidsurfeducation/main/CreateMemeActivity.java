package com.android.blessed.androidsurfeducation.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.blessed.androidsurfeducation.R;
import com.android.blessed.androidsurfeducation.models.Meme;
import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.media.MediaRecorder.VideoSource.CAMERA;

public class CreateMemeActivity extends MvpAppCompatActivity implements CreateMemeView {
    @InjectPresenter
    CreateMemePresenter mCreateMemePresenter;

    // Тулбар
    private Toolbar mToolbar;
    private MenuItem mCreateMemeButton;

    // Элементы экрана
    private TextInputLayout mMemeTitle;
    private TextInputEditText mMemeTitleText;
    private TextInputLayout mMemeDescription;
    private TextInputEditText mMemeDescriptionText;

    private ImageButton mLoadImage;
    private ImageButton mDeleteImage;

    private ImageView mMemeImage;

    private ProgressBar mProgressBar;

    // Поля логики
    private boolean mMemeTitleIsReady = false;
    private boolean mMemeImageIsReady = false;

    // Поля, отвечающие за работу с камерой
    String currentPhotoPath;
    private static final int GALLERY = 0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_meme_menu, menu);

        mCreateMemeButton = menu.findItem(R.id.action_create);
        mCreateMemeButton.setOnMenuItemClickListener(menuItem -> false);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.action_create:
                Meme meme = new Meme();
                meme.setTitle(String.valueOf(mMemeTitleText.getText()));
                meme.setDescription(String.valueOf(mMemeDescriptionText.getText()));
                meme.setIsFavorite(true);
                meme.setCreatedDate((int) new Date().getTime());
                meme.setPhotoUtl(currentPhotoPath);
                mCreateMemePresenter.insertMemeIntoDatabase(meme);
                return true;
        }

        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meme_creation_activity);

        setToolbar();

        initializeFields();
        setClickListeners();
    }

    private void setDeleteMemeButtonVisibility(int parameter) {
        mDeleteImage.setVisibility(parameter);
    }

    private void isMemeReady() {
        if (mMemeTitleIsReady && mMemeImageIsReady) {
            mCreateMemeButton.setEnabled(true);
        } else {
            mCreateMemeButton.setEnabled(false);
        }
    }

    @Override
    public void preExecute() {
        showProgressBar();
    }

    @Override
    public void postExecute() {
        hideProgressBar();
    }

    @Override
    public void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void closeActivity() {
        finish();
    }

    @Override
    public void showDialog() {
        final String[] imageLoadingVariants = {"Из галереи", "Сделать фото"};

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {
                AlertDialog.Builder ImageDialog = new AlertDialog.Builder(CreateMemeActivity.this);
                ImageDialog.setTitle("Выберите способ загрузки")
                        .setItems(imageLoadingVariants, (dialog, which) -> {
                            switch (which) {
                                case 0:
                                    choosePhotoFromGallery();
                                    break;
                                case 1:
                                    takePhotoFromCamera();
                                    break;
                            }
                        });
                ImageDialog.show();
            }
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {}
        }).check();
    }

    // работа с загрузкой изображений

    private void choosePhotoFromGallery() {
        // Создаем файл для картинки
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {

        }
        // если с ним все ок, то идем дальше
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(this,
                    "com.android.blessed.androidsurfeducation",
                    photoFile);
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, photoURI);
            startActivityForResult(galleryIntent, GALLERY);
        }
    }

    private void takePhotoFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Создаем файл для картинки
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            // если с ним все ок, то идем дальше
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.android.blessed.androidsurfeducation",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic(Bitmap image) {
        MediaStore.Images.Media.insertImage(getContentResolver(), image, String.valueOf(mMemeTitleText.getText()), String.valueOf(mMemeDescriptionText.getText()));
    }

    private Bitmap getTransformedCameraImage() {
        int targetW = mMemeImage.getWidth();
        int targetH = mMemeImage.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

        // фото пришло повернутое -> поворачиваем обратно
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        // подгоняем под размер экрана
        return scaleImage(bitmap);
    }

    private Bitmap scaleImage(Bitmap image) {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int nh = (int) ( image.getHeight() * ((float) size.x / image.getWidth()) );
        return Bitmap.createScaledBitmap(image, size.x, nh, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    mMemeImage.setImageURI(selectedImage);
                    Glide.with(this)
                            .asBitmap()
                            .load(selectedImage)
                            .into(new CustomTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    mMemeImage.setImageBitmap(scaleImage(resource));
                                    try {
                                        resource.compress(Bitmap.CompressFormat.JPEG, 100,  new FileOutputStream(currentPhotoPath));
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    setDeleteMemeButtonVisibility(View.VISIBLE);
                                    mMemeImageIsReady = true;
                                    isMemeReady();
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {

                                }
                            });
                }

                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    Bitmap image = getTransformedCameraImage();
                    mMemeImage.setImageBitmap(image);
                    galleryAddPic(image);

                    setDeleteMemeButtonVisibility(View.VISIBLE);
                    mMemeImageIsReady = true;
                    isMemeReady();
                }
                break;
        }
    }

    // -----------------------------//

    private void initializeFields() {
        mMemeTitle = findViewById(R.id.your_meme_title);
        mMemeTitleText = findViewById(R.id.title);

        mMemeDescription = findViewById(R.id.your_meme_description);
        mMemeDescriptionText = findViewById(R.id.description);

        mMemeImage = findViewById(R.id.image_container);
        mMemeImage.setImageBitmap(null);

        mLoadImage = findViewById(R.id.add_image);
        mDeleteImage = findViewById(R.id.delete_image);
        mDeleteImage.setVisibility(View.INVISIBLE);

        mProgressBar = findViewById(R.id.progress_bar_creation);
    }

    private void setClickListeners() {
        mLoadImage.setOnClickListener(view -> showDialog());

        mDeleteImage.setOnClickListener(view -> {
            mMemeImage.setImageBitmap(null);
            setDeleteMemeButtonVisibility(View.INVISIBLE);
            mMemeImageIsReady = false;
            isMemeReady();
        });

        mMemeTitleText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int length = mMemeTitleText.getText().length();
                if (length != 0 && !mMemeTitleIsReady) {
                    mMemeTitleIsReady = true;
                    isMemeReady();
                } else if (length == 0) {
                    mMemeTitleIsReady = false;
                    isMemeReady();
                }
            }
        });

        mMemeTitleText.removeTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mMemeTitleText.getText().length() == 0 && mMemeTitleIsReady) {
                    mMemeTitleIsReady = false;
                    mCreateMemeButton.setEnabled(false);
                    isMemeReady();
                }
            }
        });
    }

    private void setToolbar() {
        mToolbar = findViewById(R.id.create_meme_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.ic_close_meme);
    }
}
