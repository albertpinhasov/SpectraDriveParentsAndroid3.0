package com.spectraparent.Helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DirectionHelper {

    private final GoogleMap mMap;
    IDirectionHelper callback;
    LatLngBounds.Builder builder = new LatLngBounds.Builder();
    private Context mContext;
    private ArrayList<Polyline> mPloylines = new ArrayList<>();

    public DirectionHelper(GoogleMap map, Context ctx) {
        mMap = map;
        mContext = ctx;
    }

    public void drawPath(String o, String destination, int lineColor, int o_icon, int d_icon) {
        // String url = getUrl(origin, dest);
        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + o + "&dropLatLng=" + destination + "&mode=driving&key=AIzaSyB7f3knafT2UsHH1nWywlo_3l7mO6es5Gs";
        Log.d("onMapClick", url.toString());
        FetchUrl FetchUrl = new FetchUrl(lineColor, o_icon, d_icon);

        // Start downloading json data from Google Directions API
        FetchUrl.execute(url);
        //move map camera
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
    }

    public void clear() {
        for (Polyline pl : mPloylines
        ) {
            pl.remove();
        }
        mPloylines.clear();
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int o_vectorResId) {
        int h = 50;
        int w = 50;
        Drawable vectorDrawable = ContextCompat.getDrawable(context, o_vectorResId);
        vectorDrawable.setBounds(0, 0, w, h);
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public interface IDirectionHelper {
        void onSuccess(PolylineOptions options);
    }

    private class FetchUrl extends AsyncTask<String, Void, String> {

        private final int mOIcon;
        private final int mDIcon;
        int mColor;

        public FetchUrl(int lineColor, int o_icon, int d_icon) {
            mColor = lineColor;
            mOIcon = o_icon;
            mDIcon = d_icon;
        }

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask(mColor, mOIcon, mDIcon);

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        private final int mOIcon;
        private final int mDIcon;
        int mColor;

        public ParserTask(int mColor, int o_icon, int d_icon) {
            this.mColor = mColor;
            mOIcon = o_icon;
            mDIcon = d_icon;
        }

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask", jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask", "Executing routes");
                Log.d("ParserTask", routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask", e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;
            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                    builder.include(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(7);
                lineOptions.color(mColor);

                Log.d("onPostExecute", "onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                mPloylines.add(mMap.addPolyline(lineOptions));
                final CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(builder.build(), 150);
                mMap.moveCamera(cu);

                if (mOIcon > 0) {
                    LatLng sydney = lineOptions.getPoints().get(0);//)// mRide.getChild().get(0).getPickup().getLat(), mRide.getChild().get(0).getPickup().getLon());

                    mMap.addMarker(new MarkerOptions().position(sydney)
                            .anchor(0.5F, 0.5F)

                            .icon(bitmapDescriptorFromVector(mContext, mOIcon))
                            .title(""));
                }
                if (mDIcon > 0) {
                    LatLng sydney = lineOptions.getPoints().get(lineOptions.getPoints().size() - 1);//)// mRide.getChild().get(0).getPickup().getLat(), mRide.getChild().get(0).getPickup().getLon());
                    mMap.addMarker(new MarkerOptions().position(sydney)
                            .anchor(0.5F, 0.5F)
                            .icon(bitmapDescriptorFromVector(mContext, mDIcon))
                            .title(""));
                }
            } else {
                Log.d("onPostExecute", "without Polylines drawn");
            }
        }

        class DataParser {

            List<List<HashMap<String, String>>> parse(JSONObject jObject) {

                List<List<HashMap<String, String>>> routes = new ArrayList<>();
                JSONArray jRoutes;
                JSONArray jLegs;
                JSONArray jSteps;

                try {

                    jRoutes = jObject.getJSONArray("routes");

                    /** Traversing all routes */
                    for (int i = 0; i < jRoutes.length(); i++) {
                        jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                        List path = new ArrayList<>();

                        /** Traversing all legs */
                        for (int j = 0; j < jLegs.length(); j++) {
                            jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");

                            /** Traversing all steps */
                            for (int k = 0; k < jSteps.length(); k++) {
                                String polyline = "";
                                polyline = (String) ((JSONObject) ((JSONObject) jSteps.get(k)).get("polyline")).get("points");
                                List<LatLng> list = decodePoly(polyline);

                                /** Traversing all points */
                                for (int l = 0; l < list.size(); l++) {
                                    HashMap<String, String> hm = new HashMap<>();
                                    hm.put("lat", Double.toString((list.get(l)).latitude));
                                    hm.put("lng", Double.toString((list.get(l)).longitude));
                                    path.add(hm);
                                }
                            }
                            routes.add(path);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                }


                return routes;
            }


            /**
             * Method to decode polyline points
             */
            private List<LatLng> decodePoly(String encoded) {

                List<LatLng> poly = new ArrayList<>();
                int index = 0, len = encoded.length();
                int lat = 0, lng = 0;

                while (index < len) {
                    int b, shift = 0, result = 0;
                    do {
                        b = encoded.charAt(index++) - 63;
                        result |= (b & 0x1f) << shift;
                        shift += 5;
                    } while (b >= 0x20);
                    int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                    lat += dlat;

                    shift = 0;
                    result = 0;
                    do {
                        b = encoded.charAt(index++) - 63;
                        result |= (b & 0x1f) << shift;
                        shift += 5;
                    } while (b >= 0x20);
                    int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                    lng += dlng;

                    LatLng p = new LatLng((((double) lat / 1E5)),
                            (((double) lng / 1E5)));
                    poly.add(p);
                }

                return poly;
            }
        }
    }


}
