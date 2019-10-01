package com.spectraparent.Helpers

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.AsyncTask
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.Log
import android.view.animation.LinearInterpolator
import android.widget.ProgressBar
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import com.logicbeanzs.uberpolylineanimation.MapAnimator
import com.spectraparent.Activities.MapsActivity
import com.spectraparent.android.BuildConfig
import com.spectraparent.android.R
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.*


public class LiveTrackingUtils(val mContext: Context) {

    var google_map_api_key = "AIzaSyDWGG7aJX2eNg1v6QhzXR2STixx7bBzjig"
    lateinit var googleMap: GoogleMap
    private var driverMarker: Marker? = null
    private var newLocation: java.util.LinkedHashMap<*, *>? = null
    lateinit var driverLatlng: LatLng
    lateinit var markerName: Marker
    lateinit var removemarker: Marker
    var distance: String = ""
    private var emission = 0
    var duration: String = ""
    lateinit var dropLatLng: LatLng
    var latLngList = ArrayList<LatLng>()

    lateinit var latLngPathList: List<LatLng>
    private var v: Float = 0.toFloat()
    lateinit var directionHelper: DirectionHelper

    fun mapPathDraw(googleMap: GoogleMap, newLoc: LinkedHashMap<*, *>?, mapFragment: Context, progressBar: ProgressBar, isTrackingStarted: Boolean, directionHelper: DirectionHelper) {

        newLocation = newLoc
        this.googleMap = googleMap
        this.directionHelper = directionHelper;
        /* try {
             // Customise the styling of the base map using a JSON object defined
             // in a raw resource file.
 //            var success: Boolean = this.googleMap.setMapStyle(
 //                    MapStyleOptions.loadRawResourceStyle(
 //                            mContext, R.raw.style))
            // this.googleMap.uiSettings.isMapToolbarEnabled = false

         } catch (e: Resources.NotFoundException) {

         }*/
        var tomarkerPin = BitmapDescriptorFactory.fromResource(R.drawable.destination)
        var tomarkerCircle = BitmapDescriptorFactory.fromResource(R.drawable.car2)
        driverLatlng = LatLng(newLoc!!.get("driver_lat").toString().toDouble(), newLoc!!.get("driver_lon").toString().toDouble())

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(driverLatlng.latitude, driverLatlng.longitude), 15.5f), 4000, null)

        dropLatLng = LatLng(newLoc!!.get("drop_lat").toString().toDouble(), newLoc!!.get("drop_long").toString().toDouble())
        googleMap.addMarker(MarkerOptions().position(dropLatLng).icon(tomarkerPin))
        if (!isTrackingStarted) {
            markerName = googleMap.addMarker(MarkerOptions().position(driverLatlng).icon(tomarkerCircle))

        }
//        else {
//          markerName = googleMap.addMarker(MarkerOptions().position(dropLatLng).flat(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.car_icon)))
//        }

        val URL = getDirectionURL(driverLatlng, dropLatLng)
        GetDirection(URL, mapFragment, progressBar).execute()


    }


//    fun mapPathDrawForTrackOnly(googleMap: GoogleMap, newLoc: LinkedHashMap<*, *>?, mapFragment: Context, progressBar: ProgressBarHandler, isTrackingStarted: Boolean) {
//        Log.e("map", "mappathdrawCalled")
//        newLocation = newLoc
//        this.googleMap = googleMap
//        try {
//            // Customise the styling of the base map using a JSON object defined
//            // in a raw resource file.
//            var success: Boolean = this.googleMap.setMapStyle(
//                    MapStyleOptions.loadRawResourceStyle(
//                            mContext, R.raw.style))
//            this.googleMap.uiSettings.isMapToolbarEnabled = false
//
//        } catch (e: Resources.NotFoundException) {
//
//        }
//        // var tomarkerPin = BitmapDescriptorFactory.fromResource(R.drawable.pin)
//        // var tomarkerCircle = BitmapDescriptorFactory.fromResource(R.drawable.location_icon)
//        driverLatlng = LatLng(newLoc!!.get("latitudeValue").toString().toDouble(), newLoc!!.get("lngValue").toString().toDouble())
//        // googleMap.addMarker(MarkerOptions().position(driverLatlng).icon(tomarkerCircle))
//        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(newLoc!!.get("lat2").toString().toDouble(), newLoc!!.get("lng2").toString().toDouble()), 15.5f), 4000, null)
//        dropLatLng = LatLng(newLoc!!.get("lat2").toString().toDouble(), newLoc!!.get("lng2").toString().toDouble())
//
//        markerName = googleMap.addMarker(MarkerOptions().position(dropLatLng).flat(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.car_icon)))
//    }


    fun mapPathDrawWithoutmarker(newLoc: LinkedHashMap<*, *>?, mapFragment: Context, progressBar: ProgressBar) {

        //   newLocation = newLoc

        this.googleMap = googleMap

        driverLatlng = LatLng(newLoc!!.get("driver_lat").toString().toDouble(), newLoc!!.get("driver_lon").toString().toDouble())
        val destination = LatLng(newLoc!!.get("drop_lat").toString().toDouble(), newLoc!!.get("drop_long").toString().toDouble())
        val URL = getDirectionURL(driverLatlng, destination)
        GetDirection(URL, mapFragment, progressBar).execute()


    }


    /*   fun removeMarker() {
           removemarker.remove()
           markerName = googleMap.addMarker(MarkerOptions().position(dropLatLng).flat(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.car_icon)))

       }*/

    public fun updateUI(newLoc: java.util.LinkedHashMap<*, *>?, context: Context) {
        val oldLocation = LatLng((newLoc!!["drop_lat"]).toString().toDouble(), (newLoc!!["drop_long"]).toString().toDouble())

        val newLocation = LatLng((newLoc!!["driver_lat"]).toString().toDouble(), (newLoc!!["driver_lon"]).toString().toDouble())
        if (latLngList.size < 2) {
            latLngList.add(LatLng((newLoc!!["driver_lat"]).toString().toDouble(), (newLoc!!["driver_lon"]).toString().toDouble()))
        } else {
            latLngList.removeAt(0)
            latLngList.add(LatLng((newLoc!!["driver_lat"]).toString().toDouble(), (newLoc!!["driver_lon"]).toString().toDouble()))
        }
        if (latLngList.size > 1) {

            animateCarOnMap(latLngList, oldLocation, context)
        }
        if (driverMarker != null) {
        }

    }


    private fun animateCarOnMap(latLngs: List<LatLng>, destinationLatLng: LatLng, context: Context) {
        val builder = LatLngBounds.Builder()
        emission++
        for (latLng in latLngs) {
            builder.include(latLng)
        }

        Log.e("emission", emission.toString())
        val bounds = builder.build()
        val mCameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 2)
        /* if (emission == 1) {
             markerName = googleMap.addMarker(MarkerOptions().position(latLngs[0])
                     .flat(true)
                     .icon(BitmapDescriptorFactory.fromResource(R.drawable.car2)))
         }*/



        markerName.position = latLngs[0]
        val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
        valueAnimator.duration = 1500
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.addUpdateListener { valueAnimator ->
            v = valueAnimator.animatedFraction
            val lng = v * latLngs[1].longitude + (1 - v) * latLngs[0].longitude
            val lat = v * latLngs[1].latitude + (1 - v) * latLngs[0].latitude
            val newPos = LatLng(lat, lng)
/*
            if(latLngPathList.contains(newPos)){
                Log.e("deleteChild","deleteChild")
                latLngPathList.dropLastWhile { latLngPathList.contains(newPos) }
        }
           */
            //   checkDistance(newPos, destinationLatLng,context)
            markerName.position = newPos
            markerName.setAnchor(0.5f, 0.5f)
            markerName.rotation = getBearing(latLngs[0], newPos)
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.Builder().target(newPos)
                    .zoom(15.5f).build()))
        }
        valueAnimator.start()
    }

//       fun updatePath(newLoc: LinkedHashMap<*, *>?, mapFragment: Context, progressBar: ProgressBarHandler) {
//        Log.e("map", "updatePath")
//        driverLatlng = LatLng(newLoc!!.get("latitudeValue").toString().toDouble(), newLoc!!.get("lngValue").toString().toDouble())
//
//        val dropLatLng = LatLng(newLoc!!.get("lat2").toString().toDouble(), newLoc!!.get("lng2").toString().toDouble())
//        val URL = getDirectionURL(driverLatlng, dropLatLng)
//        //GetDirection(URL, mapFragment, progressBar).execute()
//        updateUI(newLoc, mapFragment)
//    }


    private fun getBearing(begin: LatLng, end: LatLng): Float {
        val lat = Math.abs(begin.latitude - end.latitude)
        val lng = Math.abs(begin.longitude - end.longitude)

        if (begin.latitude < end.latitude && begin.longitude < end.longitude)
            return Math.toDegrees(Math.atan(lng / lat)).toFloat()
        else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
            return (90 - Math.toDegrees(Math.atan(lng / lat)) + 90).toFloat()
        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
            return (Math.toDegrees(Math.atan(lng / lat)) + 180).toFloat()
        else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
            return (90 - Math.toDegrees(Math.atan(lng / lat)) + 270).toFloat()
        return -1f
    }

    fun getLocationName(latitude: Double, longitude: Double): List<Address>? {
        var geocoder: Geocoder

        geocoder = Geocoder(mContext, Locale.getDefault());

        var addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        var address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        var city = addresses.get(0).getLocality();
        var state = addresses.get(0).getAdminArea();
        var country = addresses.get(0).getCountryName();
        var postalCode = addresses.get(0).getPostalCode();
        var knownName = addresses.get(0).getFeatureName();
        return addresses
    }

    @SuppressLint("ResourceType")
    fun drwaPath(polyLineList: List<LatLng>, context: Context) {
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                MapAnimator.getInstance().setPrimaryLineColor(context.getColor(R.color.colorPrimary))
                MapAnimator.getInstance().setSecondaryLineColor(context.getColor(R.color.colorPrimary))
            }
            MapAnimator.getInstance().animateRoute(googleMap, polyLineList)


        } catch (e: Exception) {
            Log.d("--->>>", "plyline inbound exception")
        }


        if (newLocation != null)
            updateUI(newLocation, context)
    }

    private fun getDirectionURL(origin: LatLng, dest: LatLng): String {
        return "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.latitude},${origin.longitude}&destination=${dest.latitude},${dest.longitude}&sensor=true&mode=driving&key=${google_map_api_key}"
    }

    private inner class GetDirection(val url: String, val context: Context, val progressbar: ProgressBar) : AsyncTask<Void, Void, List<LatLng>>() {

        override fun onPreExecute() {
            super.onPreExecute()
            if (context is MapsActivity) {
                context.startStopProgressBar(1)
            }

        }

        override fun doInBackground(vararg params: Void?): List<LatLng> {
            val client = OkHttpClient()
            val path = ArrayList<LatLng>()
            try {
                val request = Request.Builder().url(url).build()

                val response = client.newCall(request).execute()
                val data = response.body()!!.string()
                val result = ArrayList<List<LatLng>>()
                val respObj = Gson().fromJson(data, GoogleMapDTO::class.java)
                val status = JSONObject(data).getString("status")
                if (status == "OK") {
                    distance = respObj.routes[0].legs[0].distance.text
                    duration = respObj.routes[0].legs[0].duration.text
                    for (i in 0..(respObj.routes[0].legs[0].steps.size - 1)) {
                        val startLatLng = LatLng(respObj.routes[0].legs[0].steps[i].start_location.lat.toDouble()
                                , respObj.routes[0].legs[0].steps[i].start_location.lng.toDouble())
                        path.add(startLatLng)
                        val endLatLng = LatLng(respObj.routes[0].legs[0].steps[i].end_location.lat.toDouble()
                                , respObj.routes[0].legs[0].steps[i].end_location.lng.toDouble())
                        path.addAll(decodePolyline(respObj.routes[0].legs[0].steps[i].polyline.points))
                    }
                    result.add(path)

                } else {
                    return emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()

            }
            return path
        }

        override fun onPostExecute(result: List<LatLng>) {
            //  mProgress.hideProgress()

            latLngPathList = result
            if (context is MapsActivity) {
                context.startStopProgressBar(0)
            }

            if (context is MapsActivity) {
                context.setDistanceAndTime(distance, duration)

            }
            if (result.isNotEmpty()) {
                drwaPath(result, context)
            } else {
                if (context is MapsActivity) {
                    context.startStopProgressBar(2)
                }

            }


            /*val lineoption = PolylineOptions()
            for (i in result.indices){
                lineoption.addAll(result[i])
                lineoption.width(10f)
                lineoption.color(Color.BLUE)
                lineoption.geodesic(true)
            }
            googleMap.addPolyline(lineoption)*/
        }
    }

    fun decodePolyline(encoded: String): List<LatLng> {

        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val latLng = LatLng((lat.toDouble() / 1E5), (lng.toDouble() / 1E5))
            poly.add(latLng)
        }

        return poly
    }

    interface ProgressBarHandlerTracking {
        fun startStopProgressBar(start: Int)
    }


    /*
    private fun animateCar(dropLatLng: LatLng) {
        val startPosition = driverMarker!!.getPosition()
        val endPosition = LatLng(dropLatLng.latitude, dropLatLng.longitude)
        val latLngInterpolator = MapFragment.LatLngInterpolator.LinearFixed()
        val valueAnimator = ValueAnimator.ofFloat(0F, 1F)
        valueAnimator.duration = 5000 // duration 5 seconds
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.addUpdateListener { animation ->
            try {
                val v = animation.animatedFraction
                val newPosition = latLngInterpolator.interpolate(v, startPosition, endPosition)
                driverMarker!!.setPosition(newPosition)
            } catch (ex: Exception) {
            }
        }
        valueAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
            }
        })
        valueAnimator.start()
    }
*/
    /*

    private fun rotateMarker(marker: Marker, toRotation: Float) {
        if (!isMarkerRotating) {
            val handler = Handler()
            val start = SystemClock.uptimeMillis()
            val startRotation = marker.rotation
            val duration: Long = 2000

            val interpolator = LinearInterpolator()

            handler.post(object : Runnable {
                override fun run() {
                    isMarkerRotating = true

                    val elapsed = SystemClock.uptimeMillis() - start
                    val t = interpolator.getInterpolation(elapsed.toFloat() / duration)

                    val rot = t * toRotation + (1 - t) * startRotation

                    val bearing = if (-rot > 180) rot / 2 else rot

                    marker.rotation = bearing

                    if (t < 1.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16)
                    } else {
                        isMarkerRotating = false
                    }
                }
            })
        }
    }
*/

/*
    private fun bearingBetweenLocations(latLng1: LatLng, latLng2: LatLng): Double {

        val PI = 3.14159
        val lat1 = latLng1.latitude * PI / 180
        val long1 = latLng1.longitude * PI / 180
        val lat2 = latLng2.latitude * PI / 180
        val long2 = latLng2.longitude * PI / 180

        val dLon = long2 - long1

        val y = Math.sin(dLon) * Math.cos(lat2)
        val x = Math.cos(lat1) * Math.sin(lat2) - (Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon))

        var brng = Math.atan2(y, x)

        brng = Math.toDegrees(brng)
        brng = (brng + 360) % 360

        return brng
    }*/


}
