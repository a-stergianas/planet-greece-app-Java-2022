package com.example.planetgreece.fragment.Login;

import android.database.DatabaseUtils;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.planetgreece.LoginActivity;
import com.example.planetgreece.R;
import com.example.planetgreece.common.Helper;
import com.example.planetgreece.db.DatabaseHelper;
import com.example.planetgreece.db.model.User;

import java.util.Objects;

public class SignupTabFragment extends Fragment {

    DatabaseHelper db;

    EditText firstname, lastname, email_signup, password_signup;
    Button signup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = DatabaseHelper.getInstance(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_tab_fragment, container, false);

        firstname = root.findViewById(R.id.etFirstNameSignup);
        lastname = root.findViewById(R.id.etLastNameSignup);
        email_signup = root.findViewById(R.id.etEmailSignup);
        password_signup = root.findViewById(R.id.etPasswordSignup);
        signup = root.findViewById(R.id.btnSignup);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = Helper.capitalizeFirstLetter(firstname.getText().toString());
                String lastName = Helper.capitalizeFirstLetter(lastname.getText().toString());
                String email = email_signup.getText().toString();
                String password = password_signup.getText().toString();

//                Toast.makeText(getContext(), "Signup button clicked", Toast.LENGTH_LONG).show();
                if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!Helper.isEmailValid(email)) {
                    Toast.makeText(getContext(), "Please enter a valid email", Toast.LENGTH_LONG).show();
                    return;
                }

                User user = new User();
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setEmail(email);
                user.setSalt(Helper.generateRandomString(32));
                user.setPassword(Helper.encryptPassword(password, user.getSalt()));
                user.setIsAdmin(false);

                long id = db.createUser(user);
                if (id != -1) {
                    firstname.setText("");
                    lastname.setText("");
                    email_signup.setText("");
                    password_signup.setText("");

                    // Switch back to login tab in fragment
                    ((LoginActivity) requireActivity()).switchToLoginTab();

                    Toast.makeText(getContext(), "User created, you can now login.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Invalid", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }
}
