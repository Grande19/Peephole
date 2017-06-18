package com.tfguniovi.grande.peephole;

/**
 * Alvaro Grande
 */

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class LocationService extends Service implements com.google.android.gms.location.LocationListener, GoogleApiClient.ConnectionCallbacks ,  GoogleApiClient.OnConnectionFailedListener{

    private GoogleApiClient mGoogleApiClient;
    double longitud,latitud,velocidad,precision;
    LocationManager mLocationManager;

    public LocationService() {
    }

    public void onCreate(){
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        Log.d("Service","Entra en el service");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                //.addApi(Wearable.API)  // used for data layer API
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //mLocationManager = (LocationManager) getApplicationContext().getSystemService(this.LOCATION_SERVICE);
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        if (!mGoogleApiClient.isConnected())
            mGoogleApiClient.connect();
        return START_STICKY;


        //return super.onStartCommand(intent, flags, startId);
        //return START_STICKY;



        //lo que quieras que haga tu servicio
    }



    @Override
    public void onConnectionSuspended(int i) {
        //Se ha interrumpido la conexión con Google Play Services

        Log.e("Interrupción", "Se ha interrumpido la conexión con Google Play Services");
    }

    public void onStart(Intent intent, int startId){
        System.out.println("El servicio a comenzado");
        this.stopSelf();
    }



    @Override
    public void onConnectionFailed (@NonNull ConnectionResult connectionResult){

    }


        @Override
    public void onLocationChanged(Location location) {
        velocidad=location.getSpeed();
        latitud= (float)location.getLatitude();
        longitud= (float)location.getLongitude();
        precision= location.getAccuracy();
        Log.d("GET" , "Location");

            Intent i = new Intent();
            i.setAction("android.intent.action.GPS");
            i.putExtra("latitud",latitud);
            i.putExtra("longitud" , longitud);

            //Se envian los valores de latitud y longitud a la MainActivity
            sendBroadcast(i);
            stopSelf();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d ("Depuración","Conectado");
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(300000000)
                .setFastestInterval(300000000);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi
                .requestLocationUpdates(mGoogleApiClient,locationRequest,this)
                .setResultCallback(new ResultCallback<Status>() {

                    @Override
                    public void onResult(Status status) {
                        if (status.getStatus().isSuccess()) {
                            if (Log.isLoggable("Depuración", Log.DEBUG)) {
                                Log.d("Depuración", "Successfully requested location updates");
                            }
                        } else {
                            Log.e("Depuración",
                                    "Failed in requesting location updates, "
                                            + "status code: "
                                            + status.getStatusCode()
                                            + ", message: "
                                            + status.getStatusMessage());
                        }
                    }
                });

    }






    @Override
    public void onDestroy(){
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
        super.onDestroy();
        //stopService()
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null ;
    }

}
