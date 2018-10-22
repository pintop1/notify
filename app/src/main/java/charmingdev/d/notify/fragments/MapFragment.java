package charmingdev.d.notify.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import charmingdev.d.notify.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment {

    private final static String TAG = "mainApp";
    private GoogleMap mMap;

    private SupportMapFragment mapFragment;

    private CameraPosition mCameraPosition;

    // The entry points to the Places API.
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(6.5244, 3.3792);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    private Location mLastKnownLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    // Used for selecting the current place.
    private static final int M_MAX_ENTRIES = 5;
    private String[] mLikelyPlaceNames;
    private String[] mLikelyPlaceAddresses;
    private String[] mLikelyPlaceAttributions;
    private LatLng[] mLikelyPlaceLatLngs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        if (mapFragment == null) {

            mapFragment = SupportMapFragment.newInstance();
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    LatLng latLng = new LatLng(6.5244, 3.3792);
                    BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.marker_image_new);
                    googleMap.addMarker(new MarkerOptions().position(latLng)
                            .title("Fire outburst near railway station")
                            .icon(icon));
                    googleMap.addMarker(new MarkerOptions().position(new LatLng(46.722095,-98.556524))
                            .title("Fire outburst near a supermarket")
                            .icon(icon));
                    googleMap.addMarker(new MarkerOptions().position(new LatLng(6.535159,3.380598))
                            .title("Fire outburst near a supermarket")
                            .icon(icon));
                    googleMap.addMarker(new MarkerOptions().position(new LatLng(31.088604,-7.101638))
                            .title("Fire outburst near a supermarket")
                            .icon(icon));
                    googleMap.addMarker(new MarkerOptions().position(new LatLng(24.444571,54.378083))
                            .title("Fire outburst near a supermarket")
                            .icon(icon));
                    googleMap.addMarker(new MarkerOptions().position(new LatLng(7.636052,4.760881))
                            .title("Fire outburst near a supermarket")
                            .icon(icon));
                    googleMap.addMarker(new MarkerOptions().position(new LatLng(9.078768,7.418584))
                            .title("Fire outburst near a supermarket")
                            .icon(icon));
                    googleMap.addMarker(new MarkerOptions().position(new LatLng(6.827707,3.915446))
                            .title("Fire outburst near a supermarket")
                            .icon(icon));
                    googleMap.addMarker(new MarkerOptions().position(new LatLng(35.743725,139.747109))
                            .title("Fire outburst near a supermarket")
                            .icon(icon));
                    googleMap.addMarker(new MarkerOptions().position(new LatLng(-31.344703,26.191592))
                            .title("Fire outburst near a supermarket")
                            .icon(icon));
                    googleMap.addMarker(new MarkerOptions().position(new LatLng(-8.969054,-54.778459))
                            .title("Fire outburst near a supermarket")
                            .icon(icon));
                    googleMap.addMarker(new MarkerOptions().position(new LatLng(40.793595,-10.225689))
                            .title("Fire outburst near a supermarket")
                            .icon(icon));
                    googleMap.addMarker(new MarkerOptions().position(new LatLng(0,0))
                            .title("Fire outburst near a supermarket")
                            .icon(icon));
                    googleMap.addMarker(new MarkerOptions().position(new LatLng(-1,-1))
                            .title("Fire outburst near a supermarket")
                            .icon(icon));
                    googleMap.addMarker(new MarkerOptions().position(new LatLng(1,1))
                            .title("Fire outburst near a supermarket")
                            .icon(icon));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                }
            });

        }

        // R.id.map is a FrameLayout, not a Fragment
        getChildFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();

        return rootView;
    }

}
