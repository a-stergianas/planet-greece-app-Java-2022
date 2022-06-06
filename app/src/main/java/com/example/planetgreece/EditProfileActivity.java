package com.example.planetgreece;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.planetgreece.common.Helper;
import com.example.planetgreece.common.ImageSaver;
import com.example.planetgreece.common.Results;
import com.example.planetgreece.db.DatabaseHelper;
import com.example.planetgreece.db.model.User;
import com.example.planetgreece.fragment.Login.LoginTabFragment;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.IOException;

public class EditProfileActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private User user;

    private EditText etFirstName;
    private EditText etLastName;
    private EditText etEmail;
    private EditText etPassword;
    private Button btnSaveChanges;
    private Button btnDiscardChanges;
    private Button btnChangePhoto;
    private CircularImageView ivProfile;

    private Bitmap changedImageBitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        db = DatabaseHelper.getInstance(getApplicationContext());

        Intent intent = getIntent();
        user = db.getUser(((User) intent.getSerializableExtra(LoginTabFragment.USER_OBJECT)).getId());

        etFirstName = findViewById(R.id.tvFirstname);
        etLastName = findViewById(R.id.tvLastname);
        etEmail = findViewById(R.id.tvEmail);
        etPassword = findViewById(R.id.etPassword);
        ivProfile = findViewById(R.id.ivProfile);

        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        btnDiscardChanges = findViewById(R.id.btnDiscardChanges);
        btnChangePhoto = findViewById(R.id.btnChangePhoto);

        etFirstName.setText(user.getFirstName());
        etLastName.setText(user.getLastName());
        etEmail.setText(user.getEmail());

        loadProfilePicture();

        btnChangePhoto.setOnClickListener(v -> {
            pfpChooser();
        });

        btnSaveChanges.setOnClickListener(v -> {
            String firstName = Helper.capitalizeFirstLetter(etFirstName.getText().toString());
            String lastName = Helper.capitalizeFirstLetter(etLastName.getText().toString());
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            if(firstName == null
                    || lastName == null
                    || firstName.isEmpty()
                    || lastName.isEmpty()
                    || email.isEmpty()) {
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
                Toast.makeText(EditProfileActivity.this, "Invalid current password.", Toast.LENGTH_SHORT).show();
                return;
            }

            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);

            db.updateUser(user);

            if (changedImageBitmap != null)
                new ImageSaver(getApplicationContext())
                    .setFileName(user.getId() + ".png")
                    .save(changedImageBitmap);

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

    void loadProfilePicture() {
        Bitmap pfpPicture = new ImageSaver(getApplicationContext()).setFileName(user.getId() + ".png").load();
        if (pfpPicture != null)
            ivProfile.setImageBitmap(pfpPicture);
    }

    void pfpChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        resultLauncher.launch(intent);
//        startActivityForResult(intent, Results.ProfilePictureChooser);
    }

    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        System.out.println("PFP changed.");
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            Uri selectedImageUri = data.getData();
                            try {
                                changedImageBitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), selectedImageUri);
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                                return;
                            }
                            ivProfile.setImageBitmap(changedImageBitmap);
                        }
                    }
                }
            }
    );
}