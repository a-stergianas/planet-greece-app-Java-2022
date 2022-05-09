package com.example.planetgreece;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.fragment.app.Fragment;

public class SignupTabFragment extends Fragment {

    EditText firstname,lastname,email_signup, password_signup;
    Button signup;
    float v=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_tab_fragment, container, false);

        firstname = root.findViewById(R.id.etFirstNameSignup);
        lastname = root.findViewById(R.id.etLastNameSignup);
        email_signup = root.findViewById(R.id.etEmailSignup);
        password_signup = root.findViewById(R.id.etPasswordSignup);
        signup = root.findViewById(R.id.btnSignup);

        firstname.setTranslationY(800);
        lastname.setTranslationY(800);
        email_signup.setTranslationY(800);
        password_signup.setTranslationY(800);
        signup.setTranslationY(800);

        firstname.setAlpha(v);
        lastname.setAlpha(v);
        email_signup.setAlpha(v);
        password_signup.setAlpha(v);
        signup.setAlpha(v);

        firstname.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(300).start();
        lastname.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(500).start();
        email_signup.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(700).start();
        password_signup.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(500).start();
        signup.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(700).start();

        return root;
    }
}
