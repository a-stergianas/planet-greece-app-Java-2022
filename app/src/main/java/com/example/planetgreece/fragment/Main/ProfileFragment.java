package com.example.planetgreece.fragment.Main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import com.example.planetgreece.AddArticleActivity;
import com.example.planetgreece.ChangePasswordActivity;
import com.example.planetgreece.EditProfileActivity;
import com.example.planetgreece.LoginActivity;
import com.example.planetgreece.R;
import com.example.planetgreece.common.ImageSaver;
import com.example.planetgreece.common.Results;
import com.example.planetgreece.db.DatabaseHelper;
import com.example.planetgreece.db.model.User;
import com.example.planetgreece.fragment.Login.LoginTabFragment;
import com.mikhaellopez.circularimageview.CircularImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    private DatabaseHelper db;

    private static final String ARG_USER = "param1";

    private User mUser;

    private TextView btnLogout; // btnLogout is actually a TextView :D
    private TextView btnAddArticle;
    private TextView tvFullName;
    private TextView tvFirstName;
    private TextView tvLastName;
    private TextView tvEmail;
    private Button btnEditProfile;
    private Button btnChangePassword;
    private CircularImageView ivProfile;

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
        ivProfile = view.findViewById(R.id.ivProfile);

        mUser = db.getUser(mUser.getId());

        loadProfilePicture();

        btnLogout = view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            db.closeDb();

            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
            getActivity().finish();
        });

        btnAddArticle = view.findViewById(R.id.btnAddArticle);

        if (!mUser.getIsAdmin())
            btnAddArticle.setVisibility(View.GONE);
        else
            btnAddArticle.setVisibility(View.VISIBLE);

        btnAddArticle.setOnClickListener(v -> {
            if (!mUser.getIsAdmin()) {
                Toast.makeText(getContext(), "You are not an admin!", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(getContext(), AddArticleActivity.class);
            intent.putExtra(LoginTabFragment.USER_OBJECT, mUser);
            resultLauncher.launch(intent);
        });

        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        btnChangePassword = view.findViewById(R.id.btnChangePassword);

        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), EditProfileActivity.class);
            intent.putExtra(LoginTabFragment.USER_OBJECT, mUser);
//            startActivity(intent);
            resultLauncher.launch(intent);
        });

        btnChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ChangePasswordActivity.class);
            intent.putExtra(LoginTabFragment.USER_OBJECT, mUser);
            resultLauncher.launch(intent);
        });

        tvFullName.setText(mUser.getFirstName() + " " + mUser.getLastName());
        tvFirstName.setText(mUser.getFirstName());
        tvLastName.setText(mUser.getLastName());
        tvEmail.setText(mUser.getEmail());

        return view;
    }

    void loadProfilePicture() {
        Bitmap pfpPicture = new ImageSaver(getContext()).setFileName(mUser.getId() + ".png").load();
        if (pfpPicture != null)
            ivProfile.setImageBitmap(pfpPicture);
    }

    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Results.EditedProfile.ordinal()) {
                        System.out.println("Edited profile.");
//                        Intent data = result.getData();
                        mUser = db.getUser(mUser.getId());

                        tvFullName.setText(mUser.getFirstName() + " " + mUser.getLastName());
                        tvFirstName.setText(mUser.getFirstName());
                        tvLastName.setText(mUser.getLastName());
                        tvEmail.setText(mUser.getEmail());

                        loadProfilePicture();
                    }

                    if (result.getResultCode() == Results.ChangedPassword.ordinal()) {
                        System.out.println("Changed password.");
                    }
                }
            }
    );
}