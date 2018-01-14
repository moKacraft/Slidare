package epitech.eip.slidare;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import com.github.kittinunf.fuel.core.Request;
import com.github.kittinunf.fuel.core.Response;
import com.mvc.imagepicker.ImagePicker;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ferrei_e on 13/02/2017.
 */

public class SettingsActivity extends AppCompatActivity {

    static final String TAG = "SettingsActivity";

    private static final int CAMERA_REQUEST = 1888;

    private String mToken;
    private String mUrlPicture;

    private TextView mName;
    private TextView mUserEmail;

    private TextView mLibrary;
    private ImageView mUserImage;
    private ImageButton mPhotoButton;

    private EditText mNewUsername;
    private EditText mNewPassword;
    private EditText mCurrentPassword;
    private EditText mConfirmPassword;

    private String mUsername;
    private String mPassword;
    private String mBodyUpdatePicture;
    private String mBodyUpdateUsername;
    private String mBodyUpdatePassword;

    private ImageView mHomeView;
    private ImageView mGroupView;

    private Button mSave;
    private TextView mLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Log.d(TAG, "----------> onCreate");

        Intent intent = getIntent();
        mToken = intent.getStringExtra("token");
        mUrlPicture = intent.getStringExtra("fbUrl");

        mName = (TextView) findViewById(R.id.profil_name);
        mUserEmail = (TextView) findViewById(R.id.profil_email);

        mLibrary = (TextView) findViewById(R.id.library);
        mPhotoButton = (ImageButton) findViewById(R.id.camera);
        Picasso.with(getApplicationContext()).load(R.drawable.camera).fit().into(mPhotoButton);
        mUserImage = (ImageView) findViewById(R.id.user_image);

        mNewUsername = (EditText) findViewById(R.id.username_setting);
        mCurrentPassword = (EditText) findViewById(R.id.password_current);
        mNewPassword = (EditText) findViewById(R.id.password_new);
        mConfirmPassword = (EditText) findViewById(R.id.password_confirm);

        mSave = (Button) findViewById(R.id.save);
        mLogout = (TextView) findViewById(R.id.logout);

        mHomeView = (ImageView) findViewById(R.id.ico_home);
        mGroupView = (ImageView) findViewById(R.id.ico_group);

        View.OnClickListener mPhotoButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Log.d(TAG, "----------> PHOTO BUTTON");

            try{
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            catch(Exception error) {
                Log.d(TAG, "EXCEPTION ERROR : " + error);
            }
            }
        };

        View.OnClickListener mHomeViewListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            Intent intent = new Intent(SettingsActivity.this, HomeActivity.class);
            intent.putExtra("token", mToken);
            startActivity(intent);
            finish();
            }
        };

        View.OnClickListener mGroupViewListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            Intent intent = new Intent(SettingsActivity.this, ContactActivity.class);
            intent.putExtra("token", mToken);
            startActivity(intent);
            finish();
            }
        };

        View.OnClickListener mSaveListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            String username = mNewUsername.getText().toString();
            String currentPassword = mCurrentPassword.getText().toString();
            String newPassword = mNewPassword.getText().toString();
            String confirmPassword = mConfirmPassword.getText().toString();

            if (mUsername.compareTo(username) != 0){

                mBodyUpdateUsername = "{ \"username\": \"" + username + "\" }";
                try {
                    updateUserName(mBodyUpdateUsername, mToken);
                }
                catch (Exception error) {
                    Log.d(TAG, "EXCEPTION ERROR : " + error);
                }
            }

            if (mPassword.compareTo(currentPassword) == 0) {
                if (newPassword.compareTo("") != 0 && confirmPassword.compareTo("") != 0) {
                    if (newPassword.compareTo(currentPassword) != 0){
                        if (newPassword.compareTo(confirmPassword) != 0)
                            Toast.makeText(SettingsActivity.this, "New and confirmed password must be identicals.", Toast.LENGTH_SHORT).show();
                        else {
                            mBodyUpdatePassword = "{ \"old_password\": \"" + currentPassword + "\",\"new_password\": \"" + newPassword + "\" }";
                            try {
                                updateUserPassword(mBodyUpdatePassword, mToken);
                            }
                            catch (Exception error) {
                                Log.d(TAG, "EXCEPTION ERROR : " + error);
                            }
                        }
                    } else {
                        Toast.makeText(SettingsActivity.this, "Current and new password identicals.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (newPassword.compareTo("") == 0 && confirmPassword.compareTo("") != 0)
                        Toast.makeText(SettingsActivity.this, "You must confirm your new password.", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                if (currentPassword.compareTo("") != 0)
                    Toast.makeText(SettingsActivity.this, "Current Password incorrect.", Toast.LENGTH_SHORT).show();
            }
            }
        };

        View.OnClickListener mLogoutListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            SharedPreferences settings = getSharedPreferences("USERDATA", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.remove("userToken");
            editor.remove("userId");
            editor.remove("fbUrlImage");
            editor.commit();

            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            }
        };

        View.OnClickListener mSearchLibraryListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            ImagePicker.pickImage(SettingsActivity.this, "Select your image:");
            }
        };

        mHomeView.setOnClickListener(mHomeViewListener);
        mGroupView.setOnClickListener(mGroupViewListener);
        mPhotoButton.setOnClickListener(mPhotoButtonListener);
        mLibrary.setOnClickListener(mSearchLibraryListener);
        mSave.setOnClickListener(mSaveListener);
        mLogout.setOnClickListener(mLogoutListener);

        try {
            fetchUser(mToken);
        }
        catch (Exception error){
            Log.d(TAG, "EXCEPTION ERROR : " + error);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            String url;
            if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                mUserImage.setImageBitmap(photo);
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), photo, "profile_picture_url", null);
                Cursor cursor = getContentResolver().query(Uri.parse(path), null, null, null, null);
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                url = cursor.getString(idx);
            }
            else {
                url = ImagePicker.getImagePathFromResult(this, requestCode, resultCode, data);
            }
            mBodyUpdatePicture = "{ \"profile_picture_url\": \"" + url + "\" }";
            updateUserPicture(mBodyUpdatePicture, mToken);
            Picasso.with(getApplicationContext()).load(new File(url)).fit().into(mUserImage);
        }
        catch (Exception e) {
            Log.d(TAG, "EXCEPTION ERROR : " + e);
        }
    }

    public void fetchUser(String token) throws Exception {

        Map<String, Object> header = new HashMap<>();
        header.put("Authorization", "Bearer "+token);

        Fuel.get("http://34.238.153.180:50000/fetchUser").header(header).responseString(new Handler<String>() {
            @Override
            public void success(@NotNull Request request, @NotNull Response response, String s) {
                Log.d("fetchUser SUCCESS : ",response.toString());

                try {
                    JSONObject data = new JSONObject(new String(response.getData()));
                    mName.setText(data.getString("username"));
                    mUserEmail.setText(data.getString("email"));
                    mUsername = data.getString("username");
                    mNewUsername.setText(mUsername);

                    // Au cas où, la connexion se fait via l'api FB, ces valeurs peuvent être nulles.
                    try {
                        mPassword = data.getString("password");
                        mUrlPicture = data.getString("profile_picture_url");
                        Log.d(TAG, "Url picture : " + mUrlPicture);
                        Picasso.with(getApplicationContext()).load(new File(mUrlPicture)).fit().into(mUserImage);
                    }
                    catch (Exception error){
                        Log.d("No profile picture.", error.toString());
                    }

                    if (mPassword == null) {
                        mCurrentPassword.setVisibility(View.INVISIBLE);
                        mNewPassword.setVisibility(View.INVISIBLE);
                    } else {
                        mCurrentPassword.setText("");
                        mNewPassword.setText("");
                        mConfirmPassword.setText("");
                    }
                } catch (Throwable tx) {
                    tx.printStackTrace();
                }
            }

            @Override
            public void failure(@NotNull Request request, @NotNull Response response, @NotNull FuelError fuelError) {
                Log.d("fetchUser FAILURE : ",response.toString());
            }
        });
    }

    public void updateUserPicture(String body, String token) throws Exception {

        Map<String, Object> header = new HashMap<>();
        header.put("Authorization", "Bearer "+token);

        Fuel.post("http://34.238.153.180:50000/updateUserPicture").header(header).body(body.getBytes()).responseString(new Handler<String>() {
            @Override
            public void success(@NotNull Request request, @NotNull Response response, String s) {

                Log.d("upUserPic SUCCESS : ",response.toString());
                Toast.makeText(SettingsActivity.this, "Picture successfully updated.", Toast.LENGTH_SHORT).show();

                try {
                    fetchUser(mToken);
                }
                catch (Exception error){
                    Log.d(TAG, "EXCEPTION ERROR : " + error);
                }
            }

            @Override
            public void failure(@NotNull Request request, @NotNull Response response, @NotNull FuelError fuelError) {

                Log.d("upUserPic FAILURE : ",response.toString());
                Toast.makeText(SettingsActivity.this, "Picture update failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateUserName(String body, String token) throws Exception {

        Map<String, Object> header = new HashMap<>();
        header.put("Authorization", "Bearer "+token);

        Fuel.post("http://34.238.153.180:50000/updateUserName").header(header).body(body.getBytes()).responseString(new Handler<String>() {
            @Override
            public void success(@NotNull Request request, @NotNull Response response, String s) {

                Log.d("upUserName SUCCESS : ",response.toString());
                Toast.makeText(SettingsActivity.this, "Username successfully updated.", Toast.LENGTH_SHORT).show();

                try {
                    fetchUser(mToken);
                }
                catch (Exception error){
                    Log.d(TAG, "EXCEPTION ERROR : " + error);
                }
            }

            @Override
            public void failure(@NotNull Request request, @NotNull Response response, @NotNull FuelError fuelError) {

                Log.d("upUserName FAILURE : ",response.toString());
                Toast.makeText(SettingsActivity.this, "Username updated failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateUserPassword(String body, String token) throws Exception {

        Map<String, Object> header = new HashMap<>();
        header.put("Authorization", "Bearer "+token);

        Fuel.post("http://34.238.153.180:50000/updateUserPassword").header(header).body(body.getBytes()).responseString(new Handler<String>() {
            @Override
            public void success(@NotNull Request request, @NotNull Response response, String s) {

                Log.d("upUserPass SUCCESS : ",response.toString());
                Toast.makeText(SettingsActivity.this, "Password successfully updated.", Toast.LENGTH_SHORT).show();
                try {
                    fetchUser(mToken);
                }
                catch (Exception error){
                    Log.d(TAG, "EXCEPTION ERROR : " + error);
                }
            }

            @Override
            public void failure(@NotNull Request request, @NotNull Response response, @NotNull FuelError fuelError) {

                Log.d("upUserPass FAILURE : ",response.toString());
                Toast.makeText(SettingsActivity.this, "Wrong password provided.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
