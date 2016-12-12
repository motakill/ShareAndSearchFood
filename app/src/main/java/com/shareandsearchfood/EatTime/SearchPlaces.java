package com.shareandsearchfood.EatTime;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.shareandsearchfood.shareandsearchfood.CustomOnItemSelectedListener;
import com.shareandsearchfood.shareandsearchfood.NavBar;
import com.shareandsearchfood.shareandsearchfood.R;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class SearchPlaces extends NavBar implements LocationListener {

    //nossa key
    private static final String GOOGLE_API_KEY = "AIzaSyAIt0L2w8BSHyHlO3dHslJDTRfowUPmvXk";

    //private static final String GOOGLE_API_KEY = "AIzaSyATuUiZUkEc_UgHuqsBJa1oqaODI-3mLs0";
    GoogleMap googleMap;
    EditText placeText;
    double latitude = 0;
    double longitude = 0;
    private int PROXIMITY_RADIUS = 1000;
    private Spinner aux_radius;

    ArrayList<String> lista = new ArrayList<String>();
    Boolean search_results = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_places);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //show error dialog if GoolglePlayServices not available
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        //filtro das pesquisas
        addItensToList(lista);

        placeText = (EditText) findViewById(R.id.placeText_mota);
        Button btnFind = (Button) findViewById(R.id.btnFind_mota);
        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap_mota);
        googleMap = fragment.getMap();
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
        googleMap.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        if (location != null) {
            onLocationChanged(location);
        }
        locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);

        btnFind.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = placeText.getText().toString();
                for (String nome : lista) {
                    if ((type.toLowerCase()).equals(nome.toLowerCase())){
                        search_results = true;
                        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                        googlePlacesUrl.append("location=" + latitude + "," + longitude);
                        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
                        googlePlacesUrl.append("&types=" + type);
                        googlePlacesUrl.append("&sensor=true");
                        googlePlacesUrl.append("&key=" + GOOGLE_API_KEY);

                        GooglePlacesReadTask googlePlacesReadTask = new GooglePlacesReadTask();
                        Object[] toPass = new Object[2];
                        toPass[0] = googleMap;
                        toPass[1] = googlePlacesUrl.toString();
                        googlePlacesReadTask.execute(toPass);
                    }
                }
                if (search_results == false)
                    Toast.makeText(SearchPlaces.this,"That search is invalid", Toast.LENGTH_LONG).show();
            }
        });

        Button btnRestaurant = (Button) findViewById(R.id.btnRestaurant);
        btnRestaurant.setOnClickListener(new View.OnClickListener() {
            String Restaurant = "restaurant";
            @Override
            public void onClick(View v) {
                Log.d("onClick", "Button is Clicked");
                googleMap.clear();
                String url = getUrl(latitude, longitude, Restaurant);
                Object[] DataTransfer = new Object[2];
                DataTransfer[0] = googleMap;
                DataTransfer[1] = url;
                Log.d("onClick", url);
                GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
                getNearbyPlacesData.execute(DataTransfer);
                Toast.makeText(SearchPlaces.this,"Nearby Restaurants", Toast.LENGTH_LONG).show();
            }
        });
        Button btnCafe = (Button) findViewById(R.id.btnCafe);
        btnCafe.setOnClickListener(new View.OnClickListener() {
            String Cafe = "cafe";
            @Override
            public void onClick(View v) {
                Log.d("onClick", "Button is Clicked");
                googleMap.clear();
                String url = getUrl(latitude, longitude, Cafe);
                Object[] DataTransfer = new Object[2];
                DataTransfer[0] = googleMap;
                DataTransfer[1] = url;
                Log.d("onClick", url);
                GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
                getNearbyPlacesData.execute(DataTransfer);
                Toast.makeText(SearchPlaces.this,"Nearby Cofees", Toast.LENGTH_LONG).show();
            }
        });

        Button btnBar = (Button) findViewById(R.id.btnBar);
        btnBar.setOnClickListener(new View.OnClickListener() {
            String Bar = "bar";
            @Override
            public void onClick(View v) {
                Log.d("onClick", "Button is Clicked");
                googleMap.clear();
                String url = getUrl(latitude, longitude, Bar);
                Object[] DataTransfer = new Object[2];
                DataTransfer[0] = googleMap;
                DataTransfer[1] = url;
                Log.d("onClick", url);
                GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
                getNearbyPlacesData.execute(DataTransfer);
                Toast.makeText(SearchPlaces.this,"Nearby Bars", Toast.LENGTH_LONG).show();
            }
        });

        aux_radius = (Spinner) findViewById(R.id.spinner_radius);
        aux_radius.setOnItemSelectedListener(new CustomOnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if(pos > 0) {
                    PROXIMITY_RADIUS = Integer.parseInt(parent.getItemAtPosition(pos).toString()) * 1000;
                    /*Toast.makeText(parent.getContext(),
                            "OnItemSelectedListener : " + PROXIMITY_RADIUS,
                            Toast.LENGTH_SHORT).show();
                            */
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }

        });
    }

    private String getUrl(double latitude, double longitude, String nearbyPlace) {

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyAIt0L2w8BSHyHlO3dHslJDTRfowUPmvXk");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    private void addItensToList (ArrayList<String> lista){
        lista.add("cafe");
        lista.add("casino");
        lista.add("food");
        lista.add("hospital");
        lista.add("police");
        lista.add("restaurant");
        lista.add("bar");
        lista.add("pharmacy");
        lista.add("university");
        lista.add("library");
        lista.add("airport");
        lista.add("school");
        lista.add("train_station");
        lista.add("taxi_stand");
        lista.add("subway_station");
    }
    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }
}
