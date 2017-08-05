package com.kb2fty7.laboratory.viewmodel;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by KB2FTY7 on 8/5/2017.
 */

public class LocationLiveData extends LiveData<Location> implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private final static int UPDATE_INTERVAL = 1000;
    private GoogleApiClient googleApiClient;

    public LocationLiveData(Context context) {
        googleApiClient =
                new GoogleApiClient.Builder(context, this, this)
                        .addApi(LocationServices.API)
                        .build();
    }

    @Override
    protected void onActive() {
        googleApiClient.connect();
    }

    @Override
    protected void onInactive() {
        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    googleApiClient, this);
        }
        googleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
            LocationRequest locationRequest = new LocationRequest().setInterval(UPDATE_INTERVAL).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    googleApiClient, locationRequest, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        setValue(location);
    }

    @Override
    public void onConnectionSuspended(int cause) {
        setValue(null);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        setValue(null);
    }
}
