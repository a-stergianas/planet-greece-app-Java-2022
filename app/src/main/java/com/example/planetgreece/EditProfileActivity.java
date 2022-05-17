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

public class EditProfileActivity extends AppCompatActivity {
    private DatabaseHelper db;

    private EditText etFirstName;
    private EditText etLastName;
    private EditText etEmail;
    private EditText etPassword;
    private Button btnSaveChanges;
    private Button btnDiscardChanges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        db = DatabaseHelper.getInstance(getApplicationContext());

        Intent intent = getIntent();
        User user = db.getUser(((User) intent.getSerializableExtra(LoginTabFragment.USER_OBJECT)).getId());

        etFirstName = findViewById(R.id.tvFirstname);
        etLastName = findViewById(R.id.tvLastname);
        etEmail = findViewById(R.id.tvEmail);
        etPassword = findViewById(R.id.etPassword);

        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        btnDiscardChanges = findViewById(R.id.btnDiscardChanges);

        etFirstName.setText(user.getFirstName());
        etLastName.setText(user.getLastName());
        etEmail.setText(user.getEmail());

        btnSaveChanges.setOnClickListener(v -> {
            String firstName = Helper.capitalizeFirstLetter(etFirstName.getText().toString());
            String lastName = Helper.capitalizeFirstLetter(etLastName.getText().toString());
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            if(firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
                Toast.makeText(EditProfileActivity.this, "All fields are required.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Helper.isEmailValid(email)) {
                Toast.makeText(EditProfileActivity.this, "Invalid email address.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!email.equals(user.getEmail()) && db.userExists(email)) {
                Toast.makeText(EditProfileActivity.this, "Email already exists.", Toast.LENGTH_SHORT).show();
                return;
            }

            String userPassword = db.getUserPassword(user.getId());

            // Check if the old password is correct
            if(!userPassword.equals(Helper.encryptPassword(password, user.getSalt()))) {
                Toast.makeText(EditProfileActivity.this, "Invalid current password", Toast.LENGTH_SHORT).show();
                return;
            }

            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);

            db.updateUser(user);

            Toast.makeText(EditProfileActivity.this, "Changes saved.", Toast.LENGTH_SHORT).show();

            setResult(Results.EditedProfile.ordinal());

            finish();
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