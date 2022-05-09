package com.example.planetgreece;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.fragment.app.Fragment;

public class LoginTabFragment extends Fragment {

    EditText email_login, password_login;
    Button login;
    float v = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment, container, false);

        email_login = root.findViewById(R.id.etEmailLogin);
        password_login = root.findViewById(R.id.etPasswordLogin);
        login = root.findViewById(R.id.btnLogin);

        email_login.setTranslationY(800);
        password_login.setTranslationY(800);
        login.setTranslationY(800);

        email_login.setAlpha(v);
        password_login.setAlpha(v);
        login.setAlpha(v);

        email_login.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(300).start();
        password_login.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(500).start();
        login.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(700).start();

        return root;
    }
}