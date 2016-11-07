package com.shareandsearchfood.login;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.internal.Utility;
import com.shareandsearchfood.shareandsearchfood.MenuActivity;
import com.shareandsearchfood.shareandsearchfood.MyProfile;
import com.shareandsearchfood.shareandsearchfood.R;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;

import static android.Manifest.permission.READ_CONTACTS;

public class RegistActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_READ_CONTACTS = 0;
    private UserLoginTask mAuthTask = null;

    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };


    //funciona
    private static final int PICK_IMAGE = 100;
    private ImageView imageView;
    private AutoCompleteTextView mEmailView;
    private AutoCompleteTextView mUserNameView;
    private EditText mPasswordView;
    private Session session;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        imageView = (ImageView) findViewById(R.id.display_personal_photo);
        session = new Session(this);

        Button click = (Button)findViewById(R.id.camera);

        Button pickImageButton = (Button) findViewById(R.id.mypersonal_photo_button);
        pickImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        mUserNameView = (AutoCompleteTextView) findViewById(R.id.user);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptRegister();
                    return true;
                }
                return false;
            }
        });

        Button submitRegisterButton = (Button) findViewById(R.id.Submit_button);
        submitRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });
    }

    private void attemptRegister() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String user = mUserNameView.getText().toString();
        ImageView photo = imageView;

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        } else if (user.isEmpty()){
            mUserNameView.setError(getString(R.string.error_invalid_username));
            focusView = mUserNameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            //MOTA Sign In SSFood
            DaoSession daoSession = ((App) getApplication()).getDaoSession();
            UserDao userDao = daoSession.getUserDao();
            session.setEmail(email);

            QueryBuilder qb = userDao.queryBuilder();
            qb.where(UserDao.Properties.Email.eq(email));
            long userCount = qb.list().size();

            if (userCount == 0) {
                if (password != null) {
                    userDao.insert(new User(null, user, email, password,photo.toString(), 0));
                    Intent intent = new Intent(this, MyProfile.class);
                    startActivity(intent);

                    //  showProgress(true);
                    mAuthTask = new RegistActivity.UserLoginTask(email, password, user, photo);
                    mAuthTask.execute((Void) null);
                }
            }
            else
                mEmailView.setError(getString(R.string.already_register));

        }
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mUsername;
        private final String mPassword;
        private ImageView mImageView;


        UserLoginTask(String email, String password, String username, ImageView ImageView) {
            mEmail = email;
            mPassword = password;
            mUsername = username;
            mImageView = ImageView;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            // showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            //    showProgress(false);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private void openGallery() {
        Intent gallery =
                new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    public void signIn(View view){
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);

    }



    public void dispatchTakePictureIntent(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }


    }
// tirar foto
    /**  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
    Bundle extras = data.getExtras();
    Bitmap imageBitmap = (Bitmap) extras.get("data");
    result.setImageBitmap(imageBitmap);
    }
    }
     */
// carregar foto
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            Uri imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }
}
