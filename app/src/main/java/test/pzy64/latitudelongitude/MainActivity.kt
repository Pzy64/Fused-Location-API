package test.pzy64.latitudelongitude

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            getLocationPermission()
        }
        else
        {
            obtainCurrentLocation()
        }


    }


    private fun getLocationPermission() {
        /**
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                1001)
        } else {

            /**
             * CALL LOCATION FUNCTION ...
             * Location permission already granted before..
             */
            obtainCurrentLocation()
        }
    }


    /**
     * Handles the result of the request for location permissions.
     */
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            1001 -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    /** NO LOCATION Obtained*/
                } else {
                    /**
                     * CALL LOCATION FUNCTION ...
                     * Location permission granted by user
                     */
                    obtainCurrentLocation()
                }
            }
        }
    }




    private fun obtainCurrentLocation() {

        val locationCallback = object : LocationCallback() {
            override fun onLocationAvailability(p0: LocationAvailability?) {
                super.onLocationAvailability(p0)
            }

            override fun onLocationResult(p0: LocationResult?) {
                super.onLocationResult(p0)
                /**
                 *
                 * RECIEVE LOCTION HERE!!!!
                 *
                 * */
                latlong.text = "${p0!!.lastLocation.latitude}, ${p0!!.lastLocation.longitude}"
            }
        }

        val connectionCallback = object : GoogleApiClient.ConnectionCallbacks {

            override fun onConnected(p0: Bundle?) {

                val locationRequest = LocationRequest()

                locationRequest.interval = 1000
                locationRequest.fastestInterval = 1000

                /**
                 *  SET LOCATION PRIORITY...
                 *  LocationRequest.PRIORITY_HIGH_ACCURACY
                 *  LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
                 *  LocationRequest.PRIORITY_LOW_POWER
                 *  more..
                 */
                locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY

                if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    LocationServices.getFusedLocationProviderClient(this@MainActivity)
                        .requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
                }
            }

            override fun onConnectionSuspended(p0: Int) {
            }

        }


        val apiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(connectionCallback)
            .addApi(LocationServices.API)
            .build()

        apiClient.connect()
    }
}
