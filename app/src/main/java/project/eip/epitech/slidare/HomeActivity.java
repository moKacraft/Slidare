package project.eip.epitech.slidare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class HomeActivity extends AppCompatActivity {

    static final String TAG = "HomeActivity";
    private ImageView mHomeView;
    private ImageView mGroupView;
    private ImageView mProfilView;
    private String mToken = null;
    private String mUrlPicture = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Log.d(TAG, "----------> onCreate");

        Intent intent = getIntent();
        mToken = intent.getStringExtra("token");
        mUrlPicture = intent.getStringExtra("fbUrl");

        mHomeView = (ImageView) findViewById(R.id.ico_home);
        mGroupView = (ImageView) findViewById(R.id.ico_group);
        mProfilView = (ImageView) findViewById(R.id.ico_profil);

        View.OnClickListener mHomeViewListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "----------> HOME");
            }
        };

        View.OnClickListener mGroupViewListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "----------> GROUP");

                Intent intent = new Intent(HomeActivity.this, ContactActivity.class);
                intent.putExtra("token", mToken);
                startActivity(intent);
                finish();
            }
        };

        View.OnClickListener mProfilViewListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "----------> PROFIL");

                Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                intent.putExtra("token", mToken);
                intent.putExtra("fbUrl", mUrlPicture);
                startActivity(intent);
                finish();
            }
        };

        mHomeView.setOnClickListener(mHomeViewListener);
        mGroupView.setOnClickListener(mGroupViewListener);
        mProfilView.setOnClickListener(mProfilViewListener);
    }

}