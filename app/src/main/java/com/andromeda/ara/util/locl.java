/*
 * Copyright (c) 2019. Fulton Browne
 *  This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.andromeda.ara.util;


/**
 * Created by Chromicle(Ajay Prabhakar).
 */

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import static android.content.Context.LOCATION_SERVICE;

public class locl implements LocationListener {

    public static double latitude;
    Context ctx;
    Location location;
    LocationManager locationManager;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    public static double longitude;

    public locl(Context ctx) {
        this.ctx = ctx;
        try {
            locationManager = (LocationManager) ctx.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            Toast.makeText(ctx, "GPS Enable " + isGPSEnabled, Toast.LENGTH_LONG).show();
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            Toast.makeText(ctx, "Network Enable " + isNetworkEnabled, Toast.LENGTH_LONG).show();

            if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission
                    (ctx, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(ctx,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED) {
            }
            if (isGPSEnabled == true) {
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, 0, 0, this);
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
            if (isNetworkEnabled == true) {
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, 0, 0, this);
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Toast.makeText(ctx, "latitude: " + latitude + " longitude: " + longitude, Toast.LENGTH_LONG).show();


        } catch (Exception ex) {

            Toast.makeText(ctx, "Exception " + ex, Toast.LENGTH_LONG).show();

        }
    }

    @Nullable
    @Override
    public void onLocationChanged(Location loc) {
        loc.getLatitude();
        loc.getLongitude();
        latitude = loc.getLatitude();
        longitude = loc.getLongitude();
    }

    @Override
    public void onProviderDisabled(String provider) {
        //print "Currently GPS is Disabled";
    }

    @Override
    public void onProviderEnabled(String provider) {
        //print "GPS got Enabled";
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
}
