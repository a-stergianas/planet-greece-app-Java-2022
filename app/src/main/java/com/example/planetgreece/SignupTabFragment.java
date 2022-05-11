package com.example.planetgreece;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class SignupTabFragment extends Fragment {

    EditText firstname, lastname, email_signup, password_signup;
    Button signup;

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
                Toast.makeText(getContext(), "Signup button clicked", Toast.LENGTH_LONG).show();
            }
        });

        return root;
    }
}
