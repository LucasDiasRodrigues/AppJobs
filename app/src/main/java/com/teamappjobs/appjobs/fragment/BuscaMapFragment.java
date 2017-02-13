package com.teamappjobs.appjobs.fragment;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.teamappjobs.appjobs.R;
import com.teamappjobs.appjobs.activity.MainActivity;
import com.teamappjobs.appjobs.activity.VitrineActivity;
import com.teamappjobs.appjobs.modelo.Vitrine;
import com.teamappjobs.appjobs.util.BuscarEventBus;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuscaMapFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    protected GoogleMap mGoogleMap;
    private SupportMapFragment mapFragment;
    private ProgressBar progressBar;
    private ProgressDialog progress;
    boolean canDraw = false;
    LatLng mLatLng;

    Boolean apiConnect;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    //Permissao
    private final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 123;
    Boolean mapReady = false;
    Boolean GPSLigado = false;
    private Map<Marker, Vitrine> allMarkersMap = new HashMap<Marker, Vitrine>();
    Marker marker;
    Vitrine vitrineClicada;
    private DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    private List<Vitrine> vitrines = new ArrayList<Vitrine>();


    //Componentes do fragment
    private ImageView imageVitrine;
    private TextView txtNomeVitrine;
    private TextView txtCategoriaVitrine;
    private TextView txtDtCriacao;
    private LinearLayout layoutButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_busca_map, container, false);
        EventBus.getDefault().register(this);
        progressBar = (ProgressBar) fragment.findViewById(R.id.progressBar);

        //Conexao com a API de Localizacao do PlayServices
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        //Configuracao do objeto de monitoramento de localizacao
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000); //1 segundo
        mLocationRequest.setFastestInterval(500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        checkPermission();
        return fragment;
    }

    protected void configureMap() {
        if (mGoogleMap == null) {
            mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapa);
            // Recupera o objeto GoogleMap
/*
            mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

                @Override
                public boolean onMarkerClick(Marker marker) {
                    Log.i("CLICOU CARAAAAI",marker.getTitle());
                    return true;
                }
            });
*/
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.getUiSettings().setScrollGesturesEnabled(true);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);
        mGoogleMap.getUiSettings().setAllGesturesEnabled(true);


        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //    Toast.makeText(getActivity(), "Marker Clicked", Toast.LENGTH_SHORT).show();
                vitrineClicada = allMarkersMap.get(marker);
                abreDialog();
                return false;
            }
        });
        mapReady = true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onStart() {
        super.onStart();

        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        apiConnect = true;
        if (apiConnect) {

            if (GPSLigado) {
                configureMap();
                startLocationUpdates();
                //progress = ProgressDialog.show(getActivity(), "Aguarde...", getResources().getString(R.string.obtendoLocalizacao), true, true);
                //progress.setCancelable(false);
            }
        } else {
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        apiConnect = false;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        apiConnect = false;
    }

    @Override
    public void onLocationChanged(Location location) {
        mLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        if (location.getAccuracy() <= 100) {
            stopLocationUpdates();
           progressBar.setVisibility(View.GONE);
            atualizarMapa();
            adicionaMarkers();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent intent = getActivity().getIntent();
                    startActivity(intent);
                    getActivity().finish();

                    // mGoogleApiClient.connect();


                    //// GPS estah ligado
                    //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000, 100, this);


                } else {

                    new android.app.AlertDialog.Builder(getActivity()).setTitle(getResources().getString(R.string.permissaoNegada))
                            .setMessage(getString(R.string.permissaoNegadaMsg))
                            .setPositiveButton(getString(R.string.permitir), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    checkPermission();
                                }
                            })
                            .setNegativeButton(getString(R.string.negar), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    getActivity().startActivity(intent);

                                }
                            })
                            .setCancelable(false)
                            .show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void checkPermission() {

        // Here, thisActivity is the current activity

        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            //      if (ActivityCompat.shouldShowRequestPermissionRationale(this,
            //            android.Manifest.permission.ACCESS_FINE_LOCATION)) {

            // Show an expanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.


            //  } else {

            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
            //}

        }

    }

    private void atualizarMapa() {
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 10.0f));
        mGoogleMap.addMarker(new MarkerOptions()
                .position(new LatLng(mLatLng.latitude, mLatLng.longitude))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .zIndex(0)
                .title("Você está aqui"));


    }

    protected void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    private void verificaGPS() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            // Solicita ao usuario para ligar o GPS
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(getActivity());
            alertDialogBuilder.setMessage(R.string.gpsDesligado)
                    .setCancelable(false).setPositiveButton(
                    R.string.sim,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Intent para entrar nas configuracoes de localizacao
                            Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(callGPSSettingIntent);
                        }
                    });
            alertDialogBuilder.setNegativeButton(R.string.nao,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            android.app.AlertDialog alert = alertDialogBuilder.create();
            alert.show();
        } else {
            GPSLigado = true;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        verificaGPS();
    }

    @Override
    public void onPause() {
        super.onPause();
        verificaGPS();
    }


    //EventBus recebe as vitrines
    @Subscribe
    public void onEvent(BuscarEventBus event) {
        if (event.getVitrines().size() > 0) {
            vitrines = event.getVitrines();
            adicionaMarkers();
        }
    }

    public void adicionaMarkers() {
        if (mapReady && (!vitrines.isEmpty())) {
            mapReady = false;
            for (Vitrine vitrine : vitrines) {
                if (!vitrine.getLocalizacao().isEmpty()) {
                    String[] latLng = vitrine.getLocalizacao().split(",");
                    double latitude = Double.parseDouble(latLng[0]);
                    double longitude = Double.parseDouble(latLng[1]);
                    LatLng mCustomerLatLng = new LatLng(latitude, longitude);

                    marker = mGoogleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(latitude, longitude)));
                    allMarkersMap.put(marker, vitrine);
                }
            }

        }

    }

    private void abreDialog() {
        try {
            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.item_dialog_vitrine_map);
            dialog.setTitle("Detalhes");

            imageVitrine = (ImageView) dialog.findViewById(R.id.imageViewCapa);
            txtNomeVitrine = (TextView) dialog.findViewById(R.id.txtNomeVitrine);
            txtCategoriaVitrine = (TextView) dialog.findViewById(R.id.categoriaVitrine);
            txtDtCriacao = (TextView) dialog.findViewById(R.id.dtCriacaoVitrine);
            layoutButton = (LinearLayout) dialog.findViewById(R.id.layVitrine);


            layoutButton.setVisibility(View.VISIBLE);
            Picasso.with(getActivity()).load(getActivity().getResources().getString(R.string.imageserver) + vitrineClicada.getFoto()).into(imageVitrine);
            txtNomeVitrine.setText(vitrineClicada.getNome());
            txtCategoriaVitrine.setText(vitrineClicada.getDescCategoria());
            txtDtCriacao.setText(format.format(vitrineClicada.getDataCriacao()));
            Button btnVerVitrine = (Button) dialog.findViewById(R.id.btnVerMais);

            btnVerVitrine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Vitrine vitrineSelecionada = vitrineClicada;
                    Intent intent = new Intent(getActivity(), VitrineActivity.class);
                    intent.putExtra("vitrine", vitrineSelecionada);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptions options = ActivityOptions
                                .makeSceneTransitionAnimation(getActivity(), imageVitrine, "capaVitrine");
                        getActivity().startActivity(intent, options.toBundle());
                    } else {
                        getActivity().startActivity(intent);
                    }
                    dialog.dismiss();
                }
            });

            dialog.show();

        } catch (Exception ex) {

        }


    }
}
