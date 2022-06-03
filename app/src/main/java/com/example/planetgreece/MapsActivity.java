package com.example.planetgreece;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.planetgreece.db.DatabaseHelper;
import com.example.planetgreece.db.model.Mark;
import com.example.planetgreece.db.model.User;
import com.example.planetgreece.fragment.Login.LoginTabFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback , GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {
    private DatabaseHelper db;

    private User user;

    private Marker currentMarker;
    private Mark currentMarkerModel;

    boolean isPermissionGranted;
    private FusedLocationProviderClient mLocationClient;
    private GoogleMap mGoogleMap;
    private int GPS_REQUEST_CODE = 9001;
    private ArrayList<LatLng> locations;

    private Dialog dialog;
    private Dialog editDialog;
    private TextView inputText;
    private CheckBox infCheckBox;
    private CheckBox impCheckBox;
    private CheckBox danCheckBox;
    private ImageButton btnBack;

    @SuppressLint({"UseCompatLoadingForDrawables", "VisibleForTests"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        db = DatabaseHelper.getInstance(getApplicationContext());

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra(LoginTabFragment.USER_OBJECT);

        locations = new ArrayList<>();
        dialog = new Dialog(MapsActivity.this);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        editDialog = new Dialog(MapsActivity.this);
        editDialog.setContentView(R.layout.update_custom_dialog);
        editDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
        editDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        editDialog.setCancelable(false);
        editDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        btnBack = findViewById(R.id.btnLogout);
        btnBack.setOnClickListener(v -> finish());

        Button okay = dialog.findViewById(R.id.btn_okay);
        Button cancel = dialog.findViewById(R.id.btn_cancel);

        Button update = editDialog.findViewById(R.id.btn_update);
        Button delete = editDialog.findViewById(R.id.btn_delete);
        Button editCancel = editDialog.findViewById(R.id.btn_edit_cancel);

        editCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDialog.dismiss();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Marker marker = getCurrentMarker();
                LatLng latLng = marker.getPosition();
                initFields(editDialog);

                String message = inputText.getText().toString();

//                marker.remove();
                deleteMarker(marker);
                if (infCheckBox.isChecked()) {
                    String type = infCheckBox.getText().toString();
                    updateMarker(latLng, message, type);
                    addMarkerToDb(message, type, latLng);
                    inputText.setText("");
                    infCheckBox.setChecked(false);
                    editDialog.dismiss();
                } else if (impCheckBox.isChecked()) {
                    String type = impCheckBox.getText().toString();
                    updateMarker(latLng, message, type);
                    addMarkerToDb(message, type, latLng);
                    impCheckBox.setChecked(false);
                    inputText.setText("");
                    editDialog.dismiss();
                } else if (danCheckBox.isChecked()) {
                    String type = danCheckBox.getText().toString();
                    updateMarker(latLng, message, type);
                    addMarkerToDb(message, type, latLng);
                    inputText.setText("");
                    danCheckBox.setChecked(false);
                    editDialog.dismiss();
                } else {
                    Toast.makeText(MapsActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Marker marker = getCurrentMarker();
//                marker.remove();
                deleteMarker(marker);
                editDialog.dismiss();
            }
        });

        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                View view = (View) v.getParent();
//
//                inputText = view.findViewById(R.id.inputText);
//                infCheckBox = view.findViewById(R.id.infCheckBox);
//                impCheckBox = view.findViewById(R.id.impCheckBox);
//                danCheckBox = view.findViewById(R.id.danCheckBox);
                initFields(dialog);

                String message = inputText.getText().toString();
                LatLng point = locations.get(locations.size() - 1);


                if (infCheckBox.isChecked()) {
                    String type = infCheckBox.getText().toString();
                    createMarker(message, type);
                    addMarkerToDb(message, type, point);
                    inputText.setText("");;
                    infCheckBox.setChecked(false);
                    dialog.dismiss();
                } else if (impCheckBox.isChecked()) {
                    String type = impCheckBox.getText().toString();
                    createMarker(message, type);
                    addMarkerToDb(message, type, point);
                    impCheckBox.setChecked(false);
                    inputText.setText("");;
                    dialog.dismiss();
                } else if (danCheckBox.isChecked()) {
                    String type = danCheckBox.getText().toString();
                    createMarker(message, type);
                    addMarkerToDb(message, type, point);
                    inputText.setText("");;
                    danCheckBox.setChecked(false);
                    dialog.dismiss();
                } else {
                    Toast.makeText(MapsActivity.this, "Please fill all the fields.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initFields(dialog);

                disableAllCheckBox();

                inputText.setText("");
                int index = locations.size() - 1;
                locations.remove(index);
                dialog.dismiss();
            }
        });

        checkMyPermission();
        initMap();
        mLocationClient = new FusedLocationProviderClient(this);
    }

    private void deleteMarker(Marker marker) {
        Mark mark = db.getMark(marker.getPosition().latitude, marker.getPosition().longitude, marker.getSnippet(), marker.getTitle());
        if (mark == null)
            throw new NullPointerException("Marker is null");
        db.deleteMark(mark.getId());

        marker.remove();
    }

    private void setCurrentMarker(Marker marker) {
        Mark mark = db.getMark(marker.getPosition().latitude, marker.getPosition().longitude, marker.getSnippet(), marker.getTitle());
        if (mark == null)
            throw new NullPointerException("Marker is null");

        currentMarkerModel = mark;
        currentMarker = marker;
    }

    private Marker getCurrentMarker() {
        return currentMarker;
    }

    private Mark getCurrentMarkerModel() {
        return currentMarkerModel;
    }

    private void updateMarker(LatLng point, String message, String type) {
        if (type.equals("Danger")) {
            mGoogleMap.addMarker(
                    new MarkerOptions()
                            .position(point)
                            .title(type)
                            .snippet(message)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            );
        }

        if (type.equals("Information")) {
            mGoogleMap.addMarker(
                    new MarkerOptions()
                            .position(point)
                            .title(type)
                            .snippet(message)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            );
        }

        if(type.equals("Important")) {
            mGoogleMap.addMarker(
                    new MarkerOptions()
                            .position(point)
                            .title(type)
                            .snippet(message)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
            );
        }
    }

    private void disableAllCheckBox(){
        infCheckBox.setChecked(false);
        impCheckBox.setChecked(false);
        danCheckBox.setChecked(false);
    }

    private void initFields(Dialog dialog){
        inputText = dialog.findViewById(R.id.inputText);
        infCheckBox = dialog.findViewById(R.id.infCheckBox);
        impCheckBox = dialog.findViewById(R.id.impCheckBox);
        danCheckBox = dialog.findViewById(R.id.danCheckBox);
    }

    private void createMarker(String message, String type) {
        if (type.equals("Danger")) {
            mGoogleMap.addMarker(
                    new MarkerOptions()
                            .position(locations.get(locations.size() - 1))
                            .title(type)
                            .snippet(message)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            );
        }

        if (type.equals("Information")) {
            mGoogleMap.addMarker(
                    new MarkerOptions()
                            .position(locations.get(locations.size() - 1))
                            .title(type)
                            .snippet(message)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            );
        }

        if(type.equals("Important")) {
            mGoogleMap.addMarker(
                    new MarkerOptions()
                            .position(locations.get(locations.size() - 1))
                            .title(type)
                            .snippet(message)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
            );
        }
    }

    private void addMarkerToDb(String message, String type, LatLng point) {
        Mark mark = new Mark();
        mark.setUserId(user.getId());
        mark.setType(type);
        mark.setMessage(message);
        mark.setLatitude(point.latitude);
        mark.setLongitude(point.longitude);

        db.createMark(mark);
    }

    @SuppressLint("NonConstantResourceId")
    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        View parent = (View) view.getParent();
        switch(view.getId()) {
            case R.id.infCheckBox:
                if (checked) {
                    impCheckBox = parent.findViewById(R.id.impCheckBox);
                    danCheckBox = parent.findViewById(R.id.danCheckBox);
                    impCheckBox.setChecked(false);
                    danCheckBox.setChecked(false);
                    break;
                }
            case R.id.impCheckBox:
                if (checked) {
                    infCheckBox = parent.findViewById(R.id.infCheckBox);
                    danCheckBox = parent.findViewById(R.id.danCheckBox);
                    infCheckBox.setChecked(false);
                    danCheckBox.setChecked(false);
                    break;
                }
            case R.id.danCheckBox:
                if (checked) {
                    impCheckBox = parent.findViewById(R.id.impCheckBox);
                    infCheckBox = parent.findViewById(R.id.infCheckBox);
                    impCheckBox.setChecked(false);
                    infCheckBox.setChecked(false);
                    break;
                }
        }
    }

    private void initMap() {
        if (isPermissionGranted) {
            if (isGpsEnable()) {
                SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
                supportMapFragment.getMapAsync(this);
            }
        }
    }

    private boolean isGpsEnable(){
//        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//        boolean providerEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        if(providerEnable){
//           return true;
//        }else{
//            AlertDialog alertDialog = new AlertDialog.Builder(this)
//                    .setTitle("GPS Permission")
//                    .setMessage("GPS is required for this app to work. Please enable GPS")
//                    .setPositiveButton("Yes",((dialogInterface, i) -> {
//                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                        startActivityForResult(intent,GPS_REQUEST_CODE);
//                    }))
//                    .setCancelable(false)
//                    .show();
//        }
        return true;
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        mLocationClient.getLastLocation().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Location location = task.getResult();
                gotoLocation(location.getLatitude(),location.getLongitude());
            }
        });
    }

    private void gotoLocation(double latitude, double longitude) {
        LatLng latLng = new LatLng(latitude,longitude);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,15);
        mGoogleMap.moveCamera(cameraUpdate);
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    private void checkMyPermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                isPermissionGranted = true;
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package",getPackageName(),"");
                intent.setData(uri);
                startActivity(intent);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMyLocationEnabled(true);

        LatLng sydney = new LatLng(40.629269, 22.947412);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12));

        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng point) {
                locations.add(point);
                dialog.show();
            }
        });

        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(@NonNull Marker marker) {
                Mark mark = db.getMark(marker.getPosition().latitude, marker.getPosition().longitude, marker.getSnippet(), marker.getTitle());
                if (mark == null || (mark.getUserId() != user.getId() && !user.getIsAdmin())) {
                    return;
                }

                initFields(editDialog);

                String type = marker.getTitle();

                if (type.equals("Information")) {
                    infCheckBox.setChecked(true);
                    impCheckBox.setChecked(false);
                    danCheckBox.setChecked(false);
                } else if (type.equals("Danger")) {
                    infCheckBox.setChecked(false);
                    impCheckBox.setChecked(false);
                    danCheckBox.setChecked(true);
                } else if (type.equals("Important")) {
                    infCheckBox.setChecked(false);
                    impCheckBox.setChecked(true);
                    danCheckBox.setChecked(false);
                }

                inputText.setText(marker.getSnippet());
                setCurrentMarker(marker);
                editDialog.show();
            }
        });

        // Java get current date
        Date currentDate = new Date();

        // Add all marks
        ArrayList<Mark> marks = (ArrayList<Mark>) db.getMarks();

        for (Mark mark : marks) {
            // Delete marks after 1 day
            Date markFinishDate = null;
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());

                Calendar c = Calendar.getInstance();
                c.setTime(Objects.requireNonNull(dateFormat.parse(mark.getCreatedAt())));
                c.add(Calendar.DATE, 1);
                markFinishDate = c.getTime();

                if (markFinishDate.before(currentDate)) {
                    db.deleteMark(mark.getId());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            locations.add(mark.getLatLng());
            createMarker(mark.getMessage(), mark.getType());
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GPS_REQUEST_CODE){
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            boolean providerEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (providerEnable) {
                Toast.makeText(this, "GPS is enabled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "GPS in not enabled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}