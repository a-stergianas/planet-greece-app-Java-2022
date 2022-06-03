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

import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback , GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {
    private FusedLocationProviderClient mLocationClient;
    boolean isPermissionGranted;
    GoogleMap mGoogleMap;
    private int GPS_REQUEST_CODE = 9001;
    private ImageButton btnBack;
    ArrayList<LatLng> locations;
    Dialog dialog;
    Dialog editDialog;
    TextView inputText;
    CheckBox infCheckBox;
    CheckBox impCheckBox;
    CheckBox danCheckBox;
    Marker currentMarker;

    @SuppressLint({"UseCompatLoadingForDrawables", "VisibleForTests"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
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
        Button okey = dialog.findViewById(R.id.btn_okey);
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
                Toast.makeText(MapsActivity.this, "UPDATE", Toast.LENGTH_SHORT).show();
                Marker marker = getCurrentMarker();
                LatLng latLng = marker.getPosition();
                initFields(editDialog);
                marker.remove();
                if(infCheckBox.isChecked()){
                    updateMarker(latLng,inputText.getText().toString(),infCheckBox.getText().toString());
                    inputText.setText("");;
                    infCheckBox.setChecked(false);
                    editDialog.dismiss();
                }else if(impCheckBox.isChecked()){
                    updateMarker(latLng,inputText.getText().toString(),impCheckBox.getText().toString());
                    impCheckBox.setChecked(false);
                    inputText.setText("");;
                    editDialog.dismiss();
                }else if(danCheckBox.isChecked()){
                    updateMarker(latLng,inputText.getText().toString(),danCheckBox.getText().toString());
                    inputText.setText("");;
                    danCheckBox.setChecked(false);
                    editDialog.dismiss();
                }else{
                    Toast.makeText(MapsActivity.this, "Please Fill all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Marker marker = getCurrentMarker();
                marker.remove();
                editDialog.dismiss();
            }
        });

        okey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initFields(dialog);
                if(infCheckBox.isChecked()){
                    createMarker(inputText.getText().toString(),infCheckBox.getText().toString());
                    inputText.setText("");;
                    infCheckBox.setChecked(false);
                    dialog.dismiss();
                }else if(impCheckBox.isChecked()){
                    createMarker(inputText.getText().toString(),impCheckBox.getText().toString());
                    impCheckBox.setChecked(false);
                    inputText.setText("");;
                    dialog.dismiss();
                }else if(danCheckBox.isChecked()){
                    createMarker(inputText.getText().toString(),danCheckBox.getText().toString());
                    inputText.setText("");;
                    danCheckBox.setChecked(false);
                    dialog.dismiss();
                }else{
                    Toast.makeText(MapsActivity.this, "Please Fill all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initFields(dialog);
                disableAllCheckBox();
                inputText.setText("");
                disableAllCheckBox();
                locations.remove(locations.size() - 1);
                dialog.dismiss();
            }
        });

        checkMyPermission();
        initMap();
        mLocationClient = new FusedLocationProviderClient(this);
    }

    public void deleteMarker(Marker marker){
        System.out.println(marker);
    }


    public void setCurrentMarker(Marker currentMarker){
        this.currentMarker = currentMarker;
    }

    public Marker getCurrentMarker(){
        return this.currentMarker;
    }

    public void updateMarker(LatLng latLng, String message, String type){
        if (type.equals("Danger")){
            mGoogleMap.addMarker(new MarkerOptions().position(latLng).title(type).snippet(message).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        }
        if (type.equals("Information")){
            mGoogleMap.addMarker(new MarkerOptions().position(latLng).title(type).snippet(message).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        }

        if(type.equals("Important")){
            mGoogleMap.addMarker(new MarkerOptions().position(latLng).title(type).snippet(message).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        }
    }

    public void createMarker(String message,String type){
        if (type.equals("Danger")){
            mGoogleMap.addMarker(new MarkerOptions().position(locations.get(locations.size() - 1)).title(type).snippet(message).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        }
        if (type.equals("Information")){
            mGoogleMap.addMarker(new MarkerOptions().position(locations.get(locations.size() - 1)).title(type).snippet(message).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        }

        if(type.equals("Important")){
            mGoogleMap.addMarker(new MarkerOptions().position(locations.get(locations.size() - 1)).title(type).snippet(message).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        }
    }


    public void disableAllCheckBox(){
        infCheckBox.setChecked(false);
        impCheckBox.setChecked(false);
        danCheckBox.setChecked(false);
    }

    public void initFields(Dialog dialog){
        inputText = (TextView) dialog.findViewById(R.id.inputText);
        infCheckBox = (CheckBox) dialog.findViewById(R.id.infCheckBox);
        impCheckBox = (CheckBox) dialog.findViewById(R.id.impCheckBox);
        danCheckBox = (CheckBox) dialog.findViewById(R.id.danCheckBox);

    }

    @SuppressLint("NonConstantResourceId")
    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        View parent = (View) view.getParent();
        switch(view.getId()) {
            case R.id.infCheckBox:
                if (checked){
                    impCheckBox = (CheckBox) parent.findViewById(R.id.impCheckBox);
                    danCheckBox = (CheckBox) parent.findViewById(R.id.danCheckBox);
                    impCheckBox.setChecked(false);
                    danCheckBox.setChecked(false);
                    break;
                }
            case R.id.impCheckBox:
                if (checked){
                    infCheckBox = (CheckBox) parent.findViewById(R.id.infCheckBox);
                    danCheckBox = (CheckBox) parent.findViewById(R.id.danCheckBox);
                    infCheckBox.setChecked(false);
                    danCheckBox.setChecked(false);
                    break;
                }
            case R.id.danCheckBox:
                if (checked){
                    impCheckBox = (CheckBox) parent.findViewById(R.id.impCheckBox);
                    infCheckBox = (CheckBox) parent.findViewById(R.id.infCheckBox);
                    impCheckBox.setChecked(false);
                    infCheckBox.setChecked(false);
                    break;
                }
        }
    }

    private void initMap() {
        if(isPermissionGranted){
            if (isGpsEnable()){
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
    private void getCurrentLocation(){
        mLocationClient.getLastLocation().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
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
                initFields(editDialog);
                if(marker.getTitle().equals("Information")){
                    infCheckBox.setChecked(true);
                    impCheckBox.setChecked(false);
                    danCheckBox.setChecked(false);
                }else if(marker.getTitle().equals("Danger")){
                    infCheckBox.setChecked(false);
                    impCheckBox.setChecked(false);
                    danCheckBox.setChecked(true);
                }else if(marker.getTitle().equals("Important")){
                    infCheckBox.setChecked(false);
                    impCheckBox.setChecked(true);
                    danCheckBox.setChecked(false);
                }
                inputText.setText(marker.getSnippet());
                setCurrentMarker(marker);
                editDialog.show();
            }
        });

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

            if (providerEnable){
                Toast.makeText(this, "GPS is enable", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "GPS in not enable", Toast.LENGTH_SHORT).show();
            }
        }
    }
}