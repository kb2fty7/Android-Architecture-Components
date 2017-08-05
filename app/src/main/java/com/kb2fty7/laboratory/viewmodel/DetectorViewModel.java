package com.kb2fty7.laboratory.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.location.Location;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by KB2FTY7 on 8/4/2017.
 */

public class DetectorViewModel extends AndroidViewModel {
    private IRepository repository;
    private LatLng point;
    private int radius;
    private LocationLiveData locationLiveData;
    private NetworkLiveData networkLiveData;
    private MediatorLiveData<Status> statusMediatorLiveData = new MediatorLiveData<>();
    private String networkName;
    private MutableLiveData<String> statusLiveData = new MutableLiveData<>();
    private float[] distance = new float[1];
    private Observer<Location> locationObserver = new Observer<Location>() {
        @Override
        public void onChanged(@Nullable Location location) {
            checkZone();
        }
    };
    private Observer<String> networkObserver = new Observer<String>() {
        @Override
        public void onChanged(@Nullable String s) {
            checkZone();
        }
    };
    private Observer<Status> mediatorStatusObserver = new Observer<Status>() {
        @Override
        public void onChanged(@Nullable Status status) {
            statusLiveData.setValue(status.toString());
        }
    };

    public DetectorViewModel(final Application application) {
        super(application);
        repository = Repository.getInstance(application.getApplicationContext());
        initVariables();
        locationLiveData = new LocationLiveData(application.getApplicationContext());
        networkLiveData = new NetworkLiveData(application.getApplicationContext());
        statusMediatorLiveData.addSource(locationLiveData, locationObserver);
        statusMediatorLiveData.addSource(networkLiveData, networkObserver);
        statusMediatorLiveData.observeForever(mediatorStatusObserver);
    }


    private void updateLocationService() {
        if (isRequestedWiFi()) {
            statusMediatorLiveData.removeSource(locationLiveData);
        } else if (!isRequestedWiFi() && !locationLiveData.hasActiveObservers()) {
            statusMediatorLiveData.addSource(locationLiveData, locationObserver);
        }
    }

    private void initVariables() {
        point = repository.getPoint();
        if (point.latitude < 0 && point.longitude < 0)
            point = null;
        radius = repository.getRadius();
        networkName = repository.getNetworkName();
    }

    private void checkZone() {
        updateLocationService();
        if (isRequestedWiFi() || isInRadius()) {
            statusMediatorLiveData.setValue(Status.INSIDE);
        } else {
            statusMediatorLiveData.setValue(Status.OUTSIDE);
        }
    }

    public LiveData<String> getStatus() {
        return statusLiveData;
    }

    public void savePoint(LatLng latLng) {
        repository.savePoint(latLng);
        point = latLng;
        checkZone();
    }

    public void saveRadius(int radius) {
        this.radius = radius;
        repository.saveRadius(radius);
        checkZone();
    }

    public void saveNetworkName(String networkName) {
        this.networkName = networkName;
        repository.saveNetworkName(networkName);
        checkZone();
    }

    public int getRadius() {
        return radius;
    }

    public LatLng getPoint() {
        return point;
    }

    public String getNetworkName() {
        return networkName;
    }

    public boolean isInRadius() {
        if (locationLiveData.getValue() != null && point != null) {
            Location.distanceBetween(locationLiveData.getValue().getLatitude(), locationLiveData.getValue().getLongitude(), point.latitude, point.longitude, distance);
            if (distance[0] <= radius)
                return true;
        }
        return false;
    }

    public boolean isRequestedWiFi() {
        if (networkLiveData.getValue() == null)
            return false;
        if (networkName.isEmpty())
            return false;
        String network = networkName.replace("\"", "").toLowerCase();
        String currentNetwork = networkLiveData.getValue().replace("\"", "").toLowerCase();
        return network.equals(currentNetwork);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        statusMediatorLiveData.removeSource(locationLiveData);
        statusMediatorLiveData.removeSource(networkLiveData);
        statusMediatorLiveData.removeObserver(mediatorStatusObserver);
    }
}
