package com.example.planetgreece.fragment.Main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.planetgreece.LoginActivity;
import com.example.planetgreece.R;
import com.example.planetgreece.db.DatabaseHelper;
import com.example.planetgreece.db.model.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    DatabaseHelper db;

    private static final String ARG_USER = "param1";

    private User mUser;

    private TextView btnLogout; // btnLogout is actually a TextView :D
    private TextView tvFullName;
    private TextView tvFirstName;
    private TextView tvLastName;
    private TextView tvEmail;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param user User object.
     * @return A new instance of fragment ProfileFragment.
     */
    public static ProfileFragment newInstance(User user) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = DatabaseHelper.getInstance(getContext());

        if (getArguments() != null) {
            mUser = (User) getArguments().getSerializable(ARG_USER);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        tvFullName = view.findViewById(R.id.tvFullName);
        tvFirstName = view.findViewById(R.id.tvFirstname);
        tvLastName = view.findViewById(R.id.tvLastname);
        tvEmail = view.findViewById(R.id.tvEmail);

        btnLogout = view.findViewById(R.id.btnBack);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.closeDb();

                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(intent);
                getActivity().finish();
            }
        });

        tvFullName.setText(mUser.getFirstName() + " " + mUser.getLastName());
        tvFirstName.setText(mUser.getFirstName());
        tvLastName.setText(mUser.getLastName());
        tvEmail.setText(mUser.getEmail());

        return view;
    }
}