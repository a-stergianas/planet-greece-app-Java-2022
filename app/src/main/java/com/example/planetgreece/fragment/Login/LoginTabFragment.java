package com.example.planetgreece.fragment.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.planetgreece.MainActivity;
import com.example.planetgreece.R;
import com.example.planetgreece.common.Helper;
import com.example.planetgreece.db.DatabaseHelper;
import com.example.planetgreece.db.model.User;

public class LoginTabFragment extends Fragment {
    public static final String USER_OBJECT = "com.example.planetgreece.USER_OBJECT";

    private DatabaseHelper db;

    private EditText email_login, password_login;
    private Button login;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = DatabaseHelper.getInstance(getContext());
    }

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

        login.setOnClickListener(v -> {
            String email = email_login.getText().toString();
            String password = password_login.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Helper.isEmailValid(email)) {
                Toast.makeText(getContext(), "Please enter a valid email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (db.checkLogin(email, password)) {
                User user = db.getUser(email);

                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(USER_OBJECT, user);

                startActivity(intent);
                getActivity().finish();
            } else {
                Toast.makeText(getContext(), "Invalid", Toast.LENGTH_SHORT).show();
            }

        });

        return root;
    }
}
