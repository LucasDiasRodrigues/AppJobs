package com.teamappjobs.appjobs.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import com.squareup.picasso.Picasso;
import com.teamappjobs.appjobs.R;
import com.teamappjobs.appjobs.asyncTask.CadastraVitrineTask;
import com.teamappjobs.appjobs.asyncTask.EditaVitrineTask;
import com.teamappjobs.appjobs.asyncTask.ListaCategoriasTask;
import com.teamappjobs.appjobs.modelo.Categoria;
import com.teamappjobs.appjobs.modelo.SubCategoria;
import com.teamappjobs.appjobs.modelo.Vitrine;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CadastroVitrineActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {


    //Variaveis para obter Localização
    LatLng mLatLng;
    Boolean apiConnect;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    private ProgressDialog progress;
    private final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 123;

    //para a selecao da imagem de perfil
    private String localArquivoFoto;
    private final int IMG_CAM = 1;
    private final int IMG_SDCARD = 2;
    Bitmap imagemfotoReduzida;
    Bitmap imagemfoto;
    String imagemDecodificada = "";
    ImageView imagemPerfil;

    List<Categoria> categorias;
    List<SubCategoria> subCategorias;
    CoordinatorLayout coordinatorLayout;
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
    DateFormat brDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    DateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private EditText nomeVitrine;
    private EditText descricaoVitrine;
    private EditText qtdUnidades;
    private EditText etTags;
    private EditText link1;
    private EditText link2;
    private EditText link3;
    private EditText link4;
    private EditText link5;

    private LinearLayout linearLayoutLink5;
    private LinearLayout linearLayoutLink4;
    private LinearLayout linearLayoutLink3;
    private LinearLayout linearLayoutLink2;

    private ImageView btnAddLink;
    private ImageView btnClearLink1;
    private ImageView btnClearLink2;
    private ImageView btnClearLink3;
    private ImageView btnClearLink4;
    private ImageView btnClearLink5;

    private EditText txtValorInicial;
    private EditText txtValorFinal;
    private RadioGroup rbParametro;
    private RadioButton rbParamatroHora;
    private RadioButton rbParamatroUnidade;
    private Spinner spCategoria;
    private Spinner spSubCategoria;

    private FloatingActionButton btnCamera;
    private SharedPreferences prefs;
    private Boolean editar;
    private Vitrine vitrine;
    private Integer cod_vitrine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_vitrine);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        prefs = this.getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.cadastroVitrine);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        editar = intent.getBooleanExtra("editar", false);
        vitrine = (Vitrine) intent.getSerializableExtra("vitrine");

        //Checa a permissão para location
        checkPermission();
        VerificaGPS();
        //Pega Localização
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
                    .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
                    .addApi(LocationServices.API)
                    .build();
        }

        //Configuracao do objeto de monitoramento de localizacao
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000); //1 segundo
        mLocationRequest.setFastestInterval(500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator);
        nomeVitrine = (EditText) findViewById(R.id.editTextNome);
        descricaoVitrine = (EditText) findViewById(R.id.editTextDescricao);
        qtdUnidades = (EditText) findViewById(R.id.editTextQtdUnidades);
        etTags = (EditText) findViewById(R.id.editTextTags);
        txtValorInicial = (EditText) findViewById(R.id.valorInicial);
        txtValorFinal = (EditText) findViewById(R.id.valorFinal);
        rbParamatroHora = (RadioButton) findViewById(R.id.radiobuttonhora);
        rbParamatroUnidade = (RadioButton) findViewById(R.id.radiobuttonUnidade);

        //Links
        link1 = (EditText) findViewById(R.id.editTextLink);
        link2 = (EditText) findViewById(R.id.editTextLink2);
        link3 = (EditText) findViewById(R.id.editTextLink3);
        link4 = (EditText) findViewById(R.id.editTextLink4);
        link5 = (EditText) findViewById(R.id.editTextLink5);
        linearLayoutLink2 = (LinearLayout) findViewById(R.id.linearLayoutlink2);
        linearLayoutLink3 = (LinearLayout) findViewById(R.id.linearLayoutLink3);
        linearLayoutLink4 = (LinearLayout) findViewById(R.id.linearLayoutLink4);
        linearLayoutLink5 = (LinearLayout) findViewById(R.id.linearLayoutLink5);
        btnAddLink = (ImageView) findViewById(R.id.btnAddLink);
        btnClearLink1 = (ImageView) findViewById(R.id.btnClearLink1);
        btnClearLink2 = (ImageView) findViewById(R.id.btnClearLink2);
        btnClearLink3 = (ImageView) findViewById(R.id.btnClearLink3);
        btnClearLink4 = (ImageView) findViewById(R.id.btnClearLink4);
        btnClearLink5 = (ImageView) findViewById(R.id.btnClearLink5);

        btnAddLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linearLayoutLink2.getVisibility() != View.VISIBLE) {
                    linearLayoutLink2.setVisibility(View.VISIBLE);
                    link2.setText(link1.getText().toString());
                    link1.setText("");
                    link1.requestFocus();
                } else if (linearLayoutLink3.getVisibility() != View.VISIBLE) {
                    linearLayoutLink3.setVisibility(View.VISIBLE);
                    link3.setText(link2.getText().toString());
                    link2.setText(link1.getText().toString());
                    link1.setText("");
                    link1.requestFocus();
                } else if (linearLayoutLink4.getVisibility() != View.VISIBLE) {
                    linearLayoutLink4.setVisibility(View.VISIBLE);
                    link4.setText(link3.getText().toString());
                    link3.setText(link2.getText().toString());
                    link2.setText(link1.getText().toString());
                    link1.setText("");
                    link1.requestFocus();
                } else if (linearLayoutLink5.getVisibility() != View.VISIBLE) {
                    linearLayoutLink5.setVisibility(View.VISIBLE);
                    link5.setText(link4.getText().toString());
                    link4.setText(link3.getText().toString());
                    link3.setText(link2.getText().toString());
                    link2.setText(link1.getText().toString());
                    link1.setText("");
                    link1.requestFocus();
                    btnAddLink.setVisibility(View.GONE);
                    btnClearLink1.setVisibility(View.VISIBLE);
                }
            }
        });
        btnClearLink1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                link1.setText(link2.getText().toString());
                link2.setText(link3.getText().toString());
                link3.setText(link4.getText().toString());
                link4.setText(link5.getText().toString());
                link5.setText("");
                linearLayoutLink5.setVisibility(View.GONE);
                btnAddLink.setVisibility(View.VISIBLE);
                btnClearLink1.setVisibility(View.GONE);

            }
        });
        btnClearLink2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                link2.setText(link3.getText().toString());
                link3.setText(link4.getText().toString());
                link4.setText(link5.getText().toString());
                link5.setText("");
                btnAddLink.setVisibility(View.VISIBLE);
                btnClearLink1.setVisibility(View.GONE);

                if (linearLayoutLink5.getVisibility() == View.VISIBLE) {
                    linearLayoutLink5.setVisibility(View.GONE);
                } else if (linearLayoutLink4.getVisibility() == View.VISIBLE) {
                    linearLayoutLink4.setVisibility(View.GONE);
                } else if (linearLayoutLink3.getVisibility() == View.VISIBLE) {
                    linearLayoutLink3.setVisibility(View.GONE);
                } else if (linearLayoutLink2.getVisibility() == View.VISIBLE) {
                    linearLayoutLink2.setVisibility(View.GONE);
                }
            }
        });
        btnClearLink3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                link3.setText(link4.getText().toString());
                link4.setText(link5.getText().toString());
                link5.setText("");
                btnAddLink.setVisibility(View.VISIBLE);
                btnClearLink1.setVisibility(View.GONE);

                if (linearLayoutLink5.getVisibility() == View.VISIBLE) {
                    linearLayoutLink5.setVisibility(View.GONE);
                } else if (linearLayoutLink4.getVisibility() == View.VISIBLE) {
                    linearLayoutLink4.setVisibility(View.GONE);
                } else if (linearLayoutLink3.getVisibility() == View.VISIBLE) {
                    linearLayoutLink3.setVisibility(View.GONE);
                }
            }
        });
        btnClearLink4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                link4.setText(link5.getText().toString());
                link5.setText("");
                btnAddLink.setVisibility(View.VISIBLE);
                btnClearLink1.setVisibility(View.GONE);

                if (linearLayoutLink5.getVisibility() == View.VISIBLE) {
                    linearLayoutLink5.setVisibility(View.GONE);
                } else if (linearLayoutLink4.getVisibility() == View.VISIBLE) {
                    linearLayoutLink4.setVisibility(View.GONE);
                }

            }
        });
        btnClearLink5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                link5.setText("");
                linearLayoutLink5.setVisibility(View.GONE);
                btnAddLink.setVisibility(View.VISIBLE);
                btnClearLink1.setVisibility(View.GONE);
            }
        });

        rbParametro = (RadioGroup) findViewById(R.id.radiogroupParametro);
        rbParametro.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radiobuttonUnidade:
                        qtdUnidades.setVisibility(View.VISIBLE);
                        break;
                    default:
                        qtdUnidades.setVisibility(View.GONE);
                }
            }
        });

        spCategoria = (Spinner) findViewById(R.id.spinnerCategoria);
        spSubCategoria = (Spinner) findViewById(R.id.spinnerSubCategoria);
        spCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Categoria categoria = categorias.get(position);
                AtualizaListaSubCategoria(categoria);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ListaCategorias();

        btnCamera = (FloatingActionButton) findViewById(R.id.btnCamera);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EscolherImagem();
            }
        });

        imagemPerfil = (ImageView) findViewById(R.id.fotoVitrine);

        if (editar) {
            getSupportActionBar().setTitle("Editar vitrine");
            PreencheCampos();
        }

    }

    private void PreencheCampos() {
        Picasso.with(this).load(getResources().getString(R.string.imageserver) + vitrine.getFoto()).into(imagemPerfil);

        cod_vitrine = vitrine.getCodVitrine();
        nomeVitrine.setText(vitrine.getNome());
        descricaoVitrine.setText(vitrine.getDescricao());
        String[] auxFaixaPreco = vitrine.getFaixaPreco().split("-");
        txtValorInicial.setText(auxFaixaPreco[0]); //Fazer split
        if (auxFaixaPreco.length > 1)
            txtValorFinal.setText(auxFaixaPreco[1]);

        if (vitrine.getParametroPreco().equals("hora")) {
            rbParamatroHora.setChecked(true);

        } else if (vitrine.getParametroPreco().equals("unidade")) {
            rbParamatroUnidade.setChecked(true);
            qtdUnidades.setText(Integer.toString(vitrine.getUnidades()));
            qtdUnidades.setVisibility(View.VISIBLE);
        }
    }

    private void EscolherImagem() {

        final CharSequence[] options = {getText(R.string.tirarfoto), getText(R.string.escolherdagaleria), getText(R.string.cancelar)};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.escolhafoto);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals(getText(R.string.tirarfoto))) {
                    localArquivoFoto = getExternalFilesDir(null) + "/" + System.currentTimeMillis() + ".jpg";
                    Intent irParaCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    irParaCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(localArquivoFoto)));
                    irParaCamera.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                    startActivityForResult(irParaCamera, IMG_CAM);
                } else if (options[item].equals(getText(R.string.escolherdagaleria))) {
                    Intent irParaGaleria = new Intent(Intent.ACTION_GET_CONTENT);
                    irParaGaleria.setType("image/*");
                    startActivityForResult(irParaGaleria, IMG_SDCARD);

                } else if (options[item].equals(getText(R.string.cancelar))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMG_CAM && resultCode == RESULT_OK) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(localArquivoFoto, options);
            if (options.outHeight >= 4000 || options.outWidth >= 4000) {
                options.inSampleSize = 4;
            } else if (options.outHeight >= 2000 || options.outWidth >= 2000) {
                options.inSampleSize = 2;
            }
            options.inJustDecodeBounds = false;
            imagemfoto = BitmapFactory.decodeFile(localArquivoFoto, options);

            //Diminuir foto proporcionalmente para o view
            int scaleFactor = Math.min(imagemfoto.getWidth() / imagemPerfil.getWidth(),
                    imagemfoto.getHeight() / imagemPerfil.getHeight());
            imagemfotoReduzida = Bitmap.createScaledBitmap(imagemfoto, imagemfoto.getWidth() / scaleFactor,
                    imagemfoto.getHeight() / scaleFactor, true);
            imagemPerfil.setImageBitmap(imagemfotoReduzida);
            imagemPerfil.setTag(localArquivoFoto);

        } else if (data != null && requestCode == IMG_SDCARD && resultCode == RESULT_OK) {
            Uri img = data.getData();

            InputStream inputStream;
            try {
                inputStream = getContentResolver().openInputStream(img);
                InputStream inputStream2 = this.getContentResolver().openInputStream(img);
                //Imagem original
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                imagemfoto = BitmapFactory.decodeStream(inputStream, null, options);
                if (options.outHeight >= 4000 || options.outWidth >= 4000) {
                    options.inSampleSize = 4;
                } else if (options.outHeight >= 2000 || options.outWidth >= 2000) {
                    options.inSampleSize = 2;
                }
                options.inJustDecodeBounds = false;
                imagemfoto = BitmapFactory.decodeStream(inputStream2, null, options);
                Log.i("tamanho original", String.valueOf(imagemfoto.getHeight()) + String.valueOf(imagemfoto.getWidth()));

                //Diminuir foto proporcionalmente para o view
                int scaleFactor = Math.min(imagemfoto.getWidth() / imagemPerfil.getWidth(),
                        imagemfoto.getHeight() / imagemPerfil.getHeight());
                if (scaleFactor <= 0)
                    scaleFactor = 1;
                imagemfotoReduzida = Bitmap.createScaledBitmap(imagemfoto, imagemfoto.getWidth() / scaleFactor,
                        imagemfoto.getHeight() / scaleFactor, true);
                imagemPerfil.setImageBitmap(imagemfotoReduzida);
                imagemPerfil.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imagemPerfil.setTag(localArquivoFoto);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Erro ao carregar a imagem", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void ListaCategorias() {
        ListaCategoriasTask task = new ListaCategoriasTask(this);
        task.execute();
    }

    public void AtualizaListaCategoria(List<Categoria> categorias) {
        this.categorias = categorias;

        ArrayAdapter<Categoria> spinnerCategoriaAdapter = new ArrayAdapter<Categoria>(this, android.R.layout.simple_list_item_1, android.R.id.text1, categorias);
        spCategoria.setAdapter(spinnerCategoriaAdapter);

        if (editar) {
            String auxCategoria[] = vitrine.getDescCategoria().split(" - ");
            for (int i = 0; i < spCategoria.getAdapter().getCount(); i++) {
                if (spCategoria.getAdapter().getItem(i).toString().contains(auxCategoria[0])) {
                    spCategoria.setSelection(i);
                    Categoria categoria = categorias.get(i);
                    AtualizaListaSubCategoria(categoria);
                }
            }
        }
    }

    public void AtualizaListaSubCategoria(Categoria categoria) {
        subCategorias = new ArrayList<>();
        subCategorias = categoria.getSubCategorias();

        ArrayAdapter<SubCategoria> spinnerCategoriaAdapter = new ArrayAdapter<SubCategoria>(this, android.R.layout.simple_list_item_1, android.R.id.text1, subCategorias);
        spSubCategoria.setAdapter(spinnerCategoriaAdapter);

        if (editar) {
            String auxCategoria[] = vitrine.getDescCategoria().split(" - ");
            for (int i = 0; i < spSubCategoria.getAdapter().getCount(); i++) {
                if (spSubCategoria.getAdapter().getItem(i).toString().contains(auxCategoria[1])) {
                    spSubCategoria.setSelection(i);
                }
            }
        }
    }

    public Boolean validaCampos() {

        Boolean retorno = true;

        if (nomeVitrine.getText().toString().equals("")) {
            nomeVitrine.setError(getResources().getString(R.string.preenchacampo));
            nomeVitrine.setFocusable(true);
            retorno = false;

        }
        if (descricaoVitrine.getText().toString().equals("")) {
            descricaoVitrine.setError(getResources().getString(R.string.preenchacampo));
            descricaoVitrine.setFocusable(true);
            retorno = false;

        }

        if (!(spSubCategoria.getSelectedItem() instanceof SubCategoria)) {
            spSubCategoria.requestFocus();
            Snackbar.make(coordinatorLayout, R.string.subcatnaoselecionada, Snackbar.LENGTH_SHORT).show();
            retorno = false;
        }
        return retorno;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cadastro_vitrine, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.avancar:

                if (validaCampos()) {

                    //Faixa de preço
                    String faixaPreco = txtValorInicial.getText().toString() + "-" + txtValorFinal.getText().toString();

                    //ParametroPreco
                    String paramPreco;
                    int unidade = 0;
                    switch (rbParametro.getCheckedRadioButtonId()) {
                        case R.id.radiobuttonhora:
                            paramPreco = "hora";
                            break;
                        case R.id.radiobuttonUnidade:
                            paramPreco = "unidade";
                            if (qtdUnidades.getText().toString().equals("")) {
                                unidade = 1;
                            } else {
                                unidade = Integer.parseInt(qtdUnidades.getText().toString());
                            }
                            break;
                        default:
                            paramPreco = "";
                            unidade = 0;
                            break;
                    }

                    //SubCategoria
                    SubCategoria subCategoria = (SubCategoria) spSubCategoria.getSelectedItem();

                    //Tags
                    String listTags[];
                    List<String> mListTags = new ArrayList<>();
                    if (!etTags.getText().toString().equals("")) {
                        listTags = etTags.getText().toString().split(Pattern.quote(","));
                        for (int i = 0; i < listTags.length; i++) {
                            mListTags.add(listTags[i]);
                        }
                    }

                    //Links
                    List<String> mListLinks = new ArrayList<>();
                    if (!link1.getText().toString().equals("")) {
                        mListLinks.add(link1.getText().toString());
                    }
                    if (!link2.getText().toString().equals("")) {
                        mListLinks.add(link2.getText().toString());
                    }
                    if (!link3.getText().toString().equals("")) {
                        mListLinks.add(link3.getText().toString());
                    }
                    if (!link4.getText().toString().equals("")) {
                        mListLinks.add(link4.getText().toString());
                    }
                    if (!link5.getText().toString().equals("")) {
                        mListLinks.add(link5.getText().toString());
                    }

                    Vitrine vitrine = new Vitrine();

                    vitrine.setNome(nomeVitrine.getText().toString());
                    vitrine.setDescricao(descricaoVitrine.getText().toString());
                    vitrine.setFaixaPreco(faixaPreco);
                    vitrine.setParametroPreco(paramPreco);
                    vitrine.setUnidades(unidade);
                    vitrine.setSubCategoria(subCategoria.getCodSubCat());
                    vitrine.setTags(mListTags);
                    vitrine.setLinks(mListLinks);
                    vitrine.setEmailAnunciante(prefs.getString("email", ""));
                    String localizacao = mLatLng.latitude + "," + mLatLng.longitude;
                    vitrine.setLocalizacao(localizacao);

                    if (editar) {
                        vitrine.setCodVitrine(cod_vitrine);
                        EditaVitrineTask task = new EditaVitrineTask(this, vitrine, imagemfoto);
                        task.execute();
                    } else {
                        CadastraVitrineTask task = new CadastraVitrineTask(this, vitrine, imagemfoto);
                        task.execute();
                    }


                }


                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onErroCadastro() {
        Snackbar.make(coordinatorLayout, R.string.opserro, Snackbar.LENGTH_INDEFINITE).show();
    }


    public void VerificaGPS() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            // Solicita ao usu�rio para ligar o GPS
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
            alertDialogBuilder.setMessage(R.string.gpsDesligado)
                    .setCancelable(false).setPositiveButton(
                    "Sim",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Intent para entrar nas configura��es de localiza��o
                            Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(callGPSSettingIntent);
                        }
                    });
            alertDialogBuilder.setNegativeButton("Não",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            android.app.AlertDialog alert = alertDialogBuilder.create();
            alert.show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        apiConnect = true;

        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
            progress = ProgressDialog.show(this, "Aguarde...", getResources().getString(R.string.obtendoLocalizacao), true, true);
            progress.setCancelable(false);
        } else {
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Falha na localização", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        apiConnect = false;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        apiConnect = false;
    }

    protected void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        mLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        if (location.getAccuracy() <= 100) {
            stopLocationUpdates();
            progress.dismiss();
            Log.i("localizacao", mLatLng.toString());

            final Geocoder geocoder = new Geocoder(this);
            final String zip = "06703-450";
            try {
                List<Address> addresses = geocoder.getFromLocationName(zip, 1);
                if (addresses != null && !addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    // Use the address as needed
                    String message = String.format("Latitude: %f, Longitude: %f",
                            address.getLatitude(), address.getLongitude());
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                    Log.i("VIA CEP: ", message);
                } else {
                    // Display appropriate message when Geocoder services are not available
                    Toast.makeText(this, "Unable to geocode zipcode", Toast.LENGTH_LONG).show();
                }
            } catch (IOException e) {
                // handle exception
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates();
                } else {
                    new android.app.AlertDialog.Builder(this).setTitle(getResources().getString(R.string.permissaoNegada))
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
                                    Intent intent = new Intent(CadastroVitrineActivity.this, MainActivity.class);
                                    CadastroVitrineActivity.this.startActivity(intent);
                                }
                            })
                            .setCancelable(false)
                            .show();
                }
            }
        }
    }

    public void checkPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);
        }
    }

}
