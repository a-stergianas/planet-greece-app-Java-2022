package com.example.planetgreece;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.planetgreece.common.Helper;
import com.example.planetgreece.common.Results;
import com.example.planetgreece.db.DatabaseHelper;
import com.example.planetgreece.db.model.User;
import com.example.planetgreece.fragment.Login.LoginTabFragment;

public class ChangePasswordActivity extends AppCompatActivity {
    private DatabaseHelper db;

    private EditText etOldPassword;
    private EditText etNewPassword;
    private EditText etConfirmPassword;
    private Button btnSaveChanges;
    private Button btnDiscardChanges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        db = DatabaseHelper.getInstance(getApplicationContext());

        Intent intent = getIntent();
        User user = db.getUser(((User) intent.getSerializableExtra(LoginTabFragment.USER_OBJECT)).getId());

        etOldPassword = findViewById(R.id.etOldPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etRepeatNewPassword);

        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        btnDiscardChanges = findViewById(R.id.btnDiscardChanges);

        btnSaveChanges.setOnClickListener(v -> {
            String oldPassword = etOldPassword.getText().toString();
            String newPassword = etNewPassword.getText().toString();
            String confirmPassword = etConfirmPassword.getText().toString();

            String userPassword = db.getUserPassword(user.getId());

            // Check if the old password is correct
            if(!userPassword.equals(Helper.encryptPassword(oldPassword, user.getSalt()))) {
                Toast.makeText(ChangePasswordActivity.this, "Invalid current password", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if the new password and the confirm password are the same
            if(!newPassword.equals(confirmPassword)) {
                Toast.makeText(ChangePasswordActivity.this, "New password and confirm password do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Update the user password
            if (db.changePassword(user.getId(), newPassword)) {
                Toast.makeText(ChangePasswordActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();

                setResult(Results.ChangedPassword.ordinal());

                finish();
            } else {
                Toast.makeText(ChangePasswordActivity.this, "Password change failed", Toast.LENGTH_SHORT).show();
            }
        });

        btnDiscardChanges.setOnClickListener(v -> showAlertDialog());
    }

    private void showAlertDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Discard changes")
                .setMessage("Are you sure you want to discard the changes?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}