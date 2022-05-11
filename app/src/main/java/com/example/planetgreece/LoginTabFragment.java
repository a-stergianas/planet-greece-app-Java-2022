package com.example.planetgreece;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class LoginTabFragment extends Fragment {

    EditText email_login, password_login;
    Button login;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment, container, false);

        email_login = root.findViewById(R.id.etEmailLogin);
        password_login = root.findViewById(R.id.etPasswordLogin);
        login = root.findViewById(R.id.btnLogin);

        email_login.setTranslationY(800);
        password_login.setTranslationY(800);
        login.setTranslationY(800);

        email_login.setAlpha(0f);
        password_login.setAlpha(0f);
        login.setAlpha(0f);

        email_login.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(300).start();
        password_login.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(500).start();
        login.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(700).start();

        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getContext(), "Login button clicked", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }
}
