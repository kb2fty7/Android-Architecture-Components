package com.kb2fty7.laboratory.viewmodel;

import android.Manifest;
import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.kb2fty7.laboratory.viewmodel.Util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

enum Warning {
    PERMISSION,
    GPS_DISABLED
}

public class MainActivity extends LifecycleActivity {

    private static final int PERMISSION_LOCATION_REQUEST = 0001;
    private static final int PLACE_PICKER_REQUEST = 1;
    private static final int GPS_ENABLE_REQUEST = 2;
    @BindView(R.id.status)
    TextView statusView;
    @BindView(R.id.radius)
    EditText radiusEditText;
    @BindView(R.id.point)
    EditText pointEditText;
    @BindView(R.id.network_name)
    EditText networkEditText;
    @BindView(R.id.warning_container)
    ViewGroup warningContainer;
    @BindView(R.id.main_content)
    ViewGroup contentContainer;
    @BindView(R.id.permission)
    Button permissionButton;
    @BindView(R.id.gps)
    Button gpsButton;
    private DetectorViewModel viewModel;
    private LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        checkPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            init();
        } else {
            showWarningPage(Warning.PERMISSION);
        }
    }

    private void checkPermission() {
        if (PackageManager.PERMISSION_GRANTED == checkSelfPermission(
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            init();
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION_REQUEST);
        }
    }


    private void init() {
        viewModel = ViewModelProviders.of(this).get(DetectorViewModel.class);
        if (Utils.isGpsEnabled(this)) {
            hideWarningPage();
            checkingPosition();
            initInput();
        } else {
            showWarningPage(Warning.GPS_DISABLED);
        }
    }

    private void initInput() {
        radiusEditText.setText(String.valueOf(viewModel.getRadius()));
        latLng = viewModel.getPoint();
        if (latLng == null) {
            pointEditText.setText(getString(R.string.chose_point));
        } else {
            pointEditText.setText(latLng.toString());
        }
        networkEditText.setText(viewModel.getNetworkName());
    }

    @OnClick(R.id.get_point)
    void getPointClick(View view) {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(MainActivity.this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.save)
    void saveOnClick(View view) {
        if (!TextUtils.isEmpty(radiusEditText.getText())) {
            viewModel.saveRadius(Integer.parseInt(radiusEditText.getText().toString()));
        }
        viewModel.saveNetworkName(networkEditText.getText().toString());
    }

    @OnClick(R.id.permission)
    void permissionOnClick(View view) {
        checkPermission();
    }

    @OnClick(R.id.gps)
    void gpsOnClick(View view) {
        startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), GPS_ENABLE_REQUEST);
    }


    private void checkingPosition() {
        viewModel.getStatus().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String status) {
                updateUI(status);
            }
        });
    }

    private void updateUI(String status) {
        statusView.setText(status);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                updatePlace(place.getLatLng());
            }
        }
        if (requestCode == GPS_ENABLE_REQUEST) {
            init();
        }
    }

    private void updatePlace(LatLng latLng) {
        viewModel.savePoint(latLng);
        pointEditText.setText(latLng.toString());
    }

    private void showWarningPage(Warning warning) {
        warningContainer.setVisibility(View.VISIBLE);
        contentContainer.setVisibility(View.INVISIBLE);
        switch (warning) {
            case PERMISSION:
                gpsButton.setVisibility(View.INVISIBLE);
                permissionButton.setVisibility(View.VISIBLE);
                break;
            case GPS_DISABLED:
                gpsButton.setVisibility(View.VISIBLE);
                permissionButton.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private void hideWarningPage() {
        warningContainer.setVisibility(View.GONE);
        contentContainer.setVisibility(View.VISIBLE);
    }
}
