package project.eip.epitech.slidare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import com.github.kittinunf.fuel.core.Request;
import com.github.kittinunf.fuel.core.Response;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class SignupActivity extends AppCompatActivity {

    static final String TAG = "SignupActivity";

    private EditText mFirstNameEditText = null;
    private EditText mLastNameEditText = null;
    private EditText mEmailEditText = null;
    private EditText mPasswordEditText = null;
    private EditText mPasswordConfirmEditText = null;
    private TextView mCancelText;
    private Button mSubmitButton = null;
    private String mBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Log.d(TAG, "----------> onCreate");

        mFirstNameEditText = (EditText) findViewById(R.id.firstname);
        mLastNameEditText = (EditText) findViewById(R.id.lastname);
        mEmailEditText = (EditText) findViewById(R.id.email);
        mPasswordEditText = (EditText) findViewById(R.id.password);
        mPasswordConfirmEditText = (EditText) findViewById(R.id.password_confirm);
        mSubmitButton = (Button) findViewById(R.id.submit);
        mCancelText = (TextView) findViewById(R.id.cancel);

        View.OnClickListener mSubmitListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Firstname : ", "value = " + mFirstNameEditText.getText().toString());
                Log.d("Lastname : ", "value = " + mLastNameEditText.getText().toString());
                Log.d("Email : ", "value = " + mEmailEditText.getText().toString());
                Log.d("Password : ", "value = " + mPasswordEditText.getText().toString());
                Log.d("Password Confirm : ", "value = " + mPasswordConfirmEditText.getText().toString());

                String firstname = mFirstNameEditText.getText().toString();
                String lastname = mLastNameEditText.getText().toString();
                String email = mEmailEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();

                if (password.compareTo(mPasswordConfirmEditText.getText().toString()) != 0)
                    Toast.makeText(SignupActivity.this, "Passwords must be identical.", Toast.LENGTH_SHORT).show();
                else {
                    mBody = "{ \"first_name\": \"" + firstname + "\",\"last_name\": \"" + lastname + "\",\"email\": \"" + email + "\",\"password\": \"" + password + "\" }";

                    try {
                        createUser(mBody);

                        /*Intent intent = new Intent(SignupActivity.this, SessionActivity.class);
                        intent.putExtra("token", "");
                        startActivity(intent);
                        finish();*/

                    } catch (Exception error) {
                        Log.d(TAG, "EXCEPTION ERROR = " + error);
                    }
                }
            }
        };

        View.OnClickListener mCancelTextListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        };

        mSubmitButton.setOnClickListener(mSubmitListener);
        mCancelText.setOnClickListener(mCancelTextListener);
    }

    public void createUser(String body) throws Exception {

        Fuel.post("http://54.224.110.79:50000/createUser").body(body.getBytes()).responseString(new Handler<String>() {
            @Override
            public void success(@NotNull Request request, @NotNull Response response, String s) {
                Log.d("SUCCESS create User : ",response.toString());
                try {
                    loginUser(mBody);
                }
                catch (Exception error) {
                    Log.d(TAG, "EXCEPTION ERROR = " + error);
                }
            }

            @Override
            public void failure(@NotNull Request request, @NotNull Response response, @NotNull FuelError fuelError) {
                Log.d("FAILURE create User : ",response.toString());
            }
        });
    }

    public void loginUser(String body) throws Exception {

        Fuel.post("http://54.224.110.79:50000/loginUser").body(body.getBytes()).responseString(new Handler<String>() {
            @Override
            public void success(@NotNull Request request, @NotNull Response response, String s) {
                Log.d("SUCCESS login User : ",response.toString());

                try {
                    JSONObject data = new JSONObject(new String(response.getData()));

                    Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
                    intent.putExtra("token", data.getString("token"));
                    startActivity(intent);
                    finish();
                }
                catch (Throwable tx) {
                    tx.printStackTrace();
                }
            }

            @Override
            public void failure(@NotNull Request request, @NotNull Response response, @NotNull FuelError fuelError) {
                Log.d("FAILURE login User : ",response.toString());
            }
        });
    }
}