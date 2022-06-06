package com.example.planetgreece.fragment.Main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import androidx.fragment.app.Fragment;
import com.example.planetgreece.R;
import com.example.planetgreece.db.DatabaseHelper;
import com.example.planetgreece.db.model.User;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    private DatabaseHelper db;

    private static final String ARG_USER = "param1";

    private User mUser;

    Switch switchArktouros;
    Switch switchWWF;
    Switch switchGreenpeace;
    Switch switchOrnithologiki;
    Switch switchMEDASSET;
    Switch switchEEPF;
    Switch switchKallisto;
    Switch switchArchelon;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param user User object.
     * @return A new instance of fragment ProfileFragment.
     */
    public static SettingsFragment newInstance(User user) {
        SettingsFragment fragment = new SettingsFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        mUser = db.getUser(mUser.getId());

        switchArktouros = view.findViewById(R.id.switchArktouros);
        switchWWF = view.findViewById(R.id.switchWWF);
        switchGreenpeace = view.findViewById(R.id.switchGreenpeace);
        switchOrnithologiki = view.findViewById(R.id.switchOrnithologiki);
        switchMEDASSET = view.findViewById(R.id.switchMEDASSET);
        switchEEPF = view.findViewById(R.id.switchEEPF);
        switchKallisto = view.findViewById(R.id.switchKallisto);
        switchArchelon = view.findViewById(R.id.switchArchelon);

        ArrayList<String> preferredSites = (ArrayList<String>) db.getPreferredSites(mUser.getId());

        if (preferredSites != null) {
            switchArktouros.setChecked(preferredSites.contains("Αρκτούρος"));
            switchWWF.setChecked(preferredSites.contains("WWF"));
            switchGreenpeace.setChecked(preferredSites.contains("Greenpeace"));
            switchOrnithologiki.setChecked(preferredSites.contains("Ορνιθολογική"));
            switchMEDASSET.setChecked(preferredSites.contains("MEDASSET"));
            switchEEPF.setChecked(preferredSites.contains("Ε.Ε.Π.Φ."));
            switchKallisto.setChecked(preferredSites.contains("Καλλιστώ"));
            switchArchelon.setChecked(preferredSites.contains("ΑΡΧΕΛΩΝ"));
        }

        switchArktouros.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                db.addToPreferredSites(mUser.getId(), "Αρκτούρος");
            } else {
                db.removeFromPreferredSites(mUser.getId(), "Αρκτούρος");
            }
        });

        switchWWF.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                db.addToPreferredSites(mUser.getId(), "WWF");
            } else {
                db.removeFromPreferredSites(mUser.getId(), "WWF");
            }
        });

        switchGreenpeace.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                db.addToPreferredSites(mUser.getId(), "Greenpeace");
            } else {
                db.removeFromPreferredSites(mUser.getId(), "Greenpeace");
            }
        });

        switchOrnithologiki.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                db.addToPreferredSites(mUser.getId(), "Ορνιθολογική");
            } else {
                db.removeFromPreferredSites(mUser.getId(), "Ορνιθολογική");
            }
        });

        switchMEDASSET.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                db.addToPreferredSites(mUser.getId(), "MEDASSET");
            } else {
                db.removeFromPreferredSites(mUser.getId(), "MEDASSET");
            }
        });

        switchEEPF.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                db.addToPreferredSites(mUser.getId(), "Ε.Ε.Π.Φ.");
            } else {
                db.removeFromPreferredSites(mUser.getId(), "Ε.Ε.Π.Φ.");
            }
        });

        switchKallisto.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                db.addToPreferredSites(mUser.getId(), "Καλλιστώ");
            } else {
                db.removeFromPreferredSites(mUser.getId(), "Καλλιστώ");
            }
        });

        switchArchelon.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                db.addToPreferredSites(mUser.getId(), "ΑΡΧΕΛΩΝ");
            } else {
                db.removeFromPreferredSites(mUser.getId(), "ΑΡΧΕΛΩΝ");
            }
        });

        return view;
    }
}