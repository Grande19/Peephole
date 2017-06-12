package com.tfguniovi.grande.peephole;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class LocationService extends Service implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks{

    private static final int PETICION_PERMISO_LOCALIZACION = 101;
    private GoogleApiClient apiClient;
    double longitud,latitud;

    public LocationService() {
    }

    public void onCreate(){
        Log.d("INICIO" , "Empieza el Service");
        apiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);

        //lo que quieras que haga tu servicio
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null ;
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        //Se ha producido un error que no se puede resolver automáticamente
        //y la conexión con los Google Play Services no se ha establecido.

        Log.e("Error", "Error grave al conectar con Google Play Services");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //Conectado correctamente a Google Play Services

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            /*ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PETICION_PERMISO_LOCALIZACION);*/
        }
        else

        {

            Location gps =
                    LocationServices.FusedLocationApi.getLastLocation(apiClient);
            String pp = gps.toString();
            Log.e("Ubicacion",pp);

            updateUI(gps);
        }
    }

    private void updateUI(Location loc) {
        if (loc != null) {
            //lblLatitud.setText("Latitud: " + String.valueOf(loc.getLatitude()));
            //lblLongitud.setText("Longitud: " + String.valueOf(loc.getLongitude()));
            latitud = loc.getAltitude();
            longitud = loc.getLongitude();
            //volver();

        } else {
            //lblLatitud.setText("Latitud: (desconocida)");
            //lblLongitud.setText("Longitud: (desconocida)");
            latitud = 00000;
            longitud = 00000;
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        //Se ha interrumpido la conexión con Google Play Services

        Log.e("Conexion", "Se ha interrumpido la conexión con Google Play Services");
    }
}
