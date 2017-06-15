package com.aliazaz.aliSecretCameraPicture.activities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.aliazaz.aliSecretCameraPicture.R;

public class SettingActivity extends AppCompatActivity {

    EditText numCap, capTime;
    Button save;
    ToggleButton cameraChanger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        numCap = (EditText) findViewById(R.id.numCap);
        capTime = (EditText) findViewById(R.id.capTime);
        save = (Button) findViewById(R.id.save);
        cameraChanger = (ToggleButton) findViewById(R.id.cameraChanger);

        SharedPreferences shared = getSharedPreferences("images", MODE_PRIVATE);
        SharedPreferences.Editor edit = shared.edit();

        capTime.setText(shared.getString("capTime", "1"));
        numCap.setText(shared.getString("numCap", "1"));
        cameraChanger.setChecked(shared.getString("cameraPos", "0").equals("0") ? true : false);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(SettingActivity.this, "Saving Section", Toast.LENGTH_SHORT).show();
                if (formValidation()) {

                    edit.putString("capTime", capTime.getText().toString());
                    edit.putString("numCap", numCap.getText().toString());
                    edit.putString("cameraPos", cameraChanger.isChecked() ? "0" : "1");

                    edit.commit();

                    finish();

                }
            }
        });
    }

    public Boolean formValidation() {

        if (capTime.getText().toString().isEmpty()) {
            Toast.makeText(this, "ERROR(empty): " + getString(R.string.time), Toast.LENGTH_SHORT).show();
            capTime.setError("This data is Required!");    // Set Error on last radio button
            return false;
        } else {
            capTime.setError(null);
        }

        if (Integer.parseInt(capTime.getText().toString()) < 1 || Integer.parseInt(capTime.getText().toString()) > 300) {
            Toast.makeText(this, "Invalid(data): " + getString(R.string.time), Toast.LENGTH_SHORT).show();
            capTime.setError("Invalid(data): Range 1 sec to 300sec(5min)");    // Set Error on last radio button
            return false;
        } else {
            capTime.setError(null);
        }

        if (numCap.getText().toString().isEmpty()) {
            Toast.makeText(this, "ERROR(empty): " + getString(R.string.numImg), Toast.LENGTH_SHORT).show();
            numCap.setError("This data is Required!");    // Set Error on last radio button
            return false;
        } else {
            numCap.setError(null);
        }
        if (Integer.parseInt(numCap.getText().toString()) < 1 || Integer.parseInt(numCap.getText().toString()) > 100) {
            Toast.makeText(this, "Invalid(data): " + getString(R.string.numImg), Toast.LENGTH_SHORT).show();
            numCap.setError("Invalid(data): Range 1 to 100");    // Set Error on last radio button
            return false;
        } else {
            numCap.setError(null);
        }

        if (Integer.parseInt(numCap.getText().toString()) > Integer.parseInt(capTime.getText().toString())) {
            Toast.makeText(this, "Invalid(data): " + getString(R.string.numImgCaption), Toast.LENGTH_SHORT).show();
            numCap.setError("Invalid(data): Range less then capturing time");    // Set Error on last radio button
            return false;
        } else {
            numCap.setError(null);
        }


        return true;
    }

}
