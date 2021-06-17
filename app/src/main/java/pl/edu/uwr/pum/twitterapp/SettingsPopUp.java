package pl.edu.uwr.pum.twitterapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingsPopUp extends Activity {

    EditText changeNameEditText, changeBioEditText;
    Button changeButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_pop_up);

        changeBioEditText = findViewById(R.id.changeBioEditText);
        changeNameEditText = findViewById(R.id.changeNameEditText);
        changeButton = findViewById(R.id.editButton);

        String currentBio = getIntent().getStringExtra("currentBio");
        String currentName = getIntent().getStringExtra("currentName");

        changeNameEditText.setText(currentName);
        changeBioEditText.setText(currentBio);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("newBio", changeBioEditText.getText().toString());
                intent.putExtra("newName", changeNameEditText.getText().toString());
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });

        getWindow().setLayout((int)(width*.8), (int)(height*.8));
    }

}

