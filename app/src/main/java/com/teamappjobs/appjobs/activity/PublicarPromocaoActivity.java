package com.teamappjobs.appjobs.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.teamappjobs.appjobs.R;
import com.teamappjobs.appjobs.asyncTask.EditaPromocaoTask;
import com.teamappjobs.appjobs.asyncTask.PublicaPromocaoTask;
import com.teamappjobs.appjobs.modelo.Promocao;
import com.teamappjobs.appjobs.modelo.Vitrine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PublicarPromocaoActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView txtNome;
    private TextView txtDescricao;
    private TextView txtRegras;
    private static TextView txtDataInicio;
    private static TextView txtDataFim;
    private ImageView imageView;
    private ImageView imageViewButton;
    private FloatingActionButton fabCloseImagem;
    private Integer cod_promo;
    private String auxFotoPromo;

    public static String auxDataPicker = "";

    //para a selecao da imagem de perfil
    private String localArquivoFoto;
    private final int IMG_CAM = 1;
    private final int IMG_SDCARD = 2;
    Bitmap imagemfotoReduzida;
    Bitmap imagemfoto;
    String imagemDecodificada = "";

    CoordinatorLayout coordinatorLayout;

    Vitrine vitrine;
    Promocao promocao;
    private Boolean editar;

    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicar_promocao);

        //Recebendo os dados da vitrine
        Intent intent = getIntent();
        vitrine = (Vitrine) intent.getSerializableExtra("vitrine");
        promocao = (Promocao) intent.getSerializableExtra("promocao");
        editar = intent.getBooleanExtra("editar", false);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(vitrine.getNome());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator);

        txtNome = (TextView) findViewById(R.id.txtNome);
        txtDescricao = (TextView) findViewById(R.id.txtDescricao);
        txtRegras = (TextView) findViewById(R.id.txtRegras);
        txtDataInicio = (TextView) findViewById(R.id.txtDataInicio);
        txtDataInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auxDataPicker = "inicio";
                DialogFragment dataFragment = new DatePickerFragment();
                dataFragment.show(getSupportFragmentManager(), "dataNasc");
            }
        });
        txtDataFim = (TextView) findViewById(R.id.txtDataFim);
        txtDataFim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auxDataPicker = "fim";
                DialogFragment dataFragment = new DatePickerFragment();
                dataFragment.show(getSupportFragmentManager(), "dataNasc");
            }
        });


        imageView = (ImageView) findViewById(R.id.imageView);
        fabCloseImagem = (FloatingActionButton) findViewById(R.id.fabCloseImagem);
        fabCloseImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limparImagem();
            }
        });
        imageView.setVisibility(View.GONE);
        fabCloseImagem.hide();

        imageViewButton = (ImageView) findViewById(R.id.imageViewButton);
        imageViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EscolherImagem();
            }
        });

        if (editar) {
            getSupportActionBar().setTitle("Editar promoção");
            PreencheCampos();
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
                    imageView.setVisibility(View.VISIBLE);
                    Intent irParaCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    irParaCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(localArquivoFoto)));
                    irParaCamera.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                    startActivityForResult(irParaCamera, IMG_CAM);
                } else if (options[item].equals(getText(R.string.escolherdagaleria))) {
                    imageView.setVisibility(View.VISIBLE);
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

            File file = new File(localArquivoFoto);
            Picasso.with(this).load(file).into(imageView);
            imageView.setTag(localArquivoFoto);

            fabCloseImagem.show();


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

                //Diminuir foto proporcionalmente para o view
                int scaleFactor = Math.min(imagemfoto.getWidth() / imageView.getWidth(),
                        imagemfoto.getHeight() / imageView.getHeight());
                if (scaleFactor <= 0)
                    scaleFactor = 1;
                imagemfotoReduzida = Bitmap.createScaledBitmap(imagemfoto, imagemfoto.getWidth() / scaleFactor,
                        imagemfoto.getHeight() / scaleFactor, true);
                imageView.setImageBitmap(imagemfotoReduzida);
                imageView.setTag(localArquivoFoto);


                Picasso.with(this).load(img).into(imageView);
                imageView.setTag(localArquivoFoto);
                fabCloseImagem.show();


            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Erro ao carregar a imagem", Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_publica_promocoes, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.avancar:

                if (validaCampos()) {

                    Promocao promocao = new Promocao();

                    promocao.setCodVitrine(vitrine.getCodVitrine());
                    promocao.setNome(txtNome.getText().toString());
                    promocao.setDescricao(txtDescricao.getText().toString());
                    promocao.setRegras(txtRegras.getText().toString());
                    try {
                        promocao.setDataInicio(dateFormat.parse(txtDataInicio.getText().toString()));
                        promocao.setDataFim(dateFormat.parse(txtDataFim.getText().toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (editar) {
                        promocao.setCodPromocao(cod_promo);
                        promocao.setFoto(auxFotoPromo);
                        EditaPromocaoTask promocaoTask = new EditaPromocaoTask(this, promocao, imagemfoto);
                        promocaoTask.execute();
                    } else {
                        PublicaPromocaoTask promocaoTask = new PublicaPromocaoTask(this, promocao, imagemfoto);
                        promocaoTask.execute();
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

    public boolean validaCampos() {
        boolean validacaoOk = true;

        if (txtNome.getText().toString().equals("")) {
            txtNome.setError("Preencha este campo!");
            validacaoOk = false;
        }

        if (txtDescricao.getText().toString().equals("")) {
            txtDescricao.setError("Preencha este campo!");
            validacaoOk = false;
        }

        //Campo Data
        if (txtDataInicio.getText().toString().equals("")) {
            txtDataInicio.setError("Preencha este campo!");
            validacaoOk = false;
        }
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date auxDate = new Date();
        //teste
        Date auxDataAgora = new Date();
        String dataAgoraString = dateFormat.format(auxDataAgora);
        try {
            auxDate = dateFormat.parse(txtDataInicio.getText().toString());
            auxDataAgora = dateFormat.parse(dataAgoraString);

            if (auxDate.before(auxDataAgora)) {
                validacaoOk = false;
                txtDataInicio.setError("Data invalida!");
            }


        } catch (ParseException e) {
            e.printStackTrace();
            txtDataInicio.setError("Data invalida!");
            validacaoOk = false;
        }

        //Campo Data
        if (txtDataFim.getText().toString().equals("")) {
            txtDataFim.setError("Preencha este campo!");
            validacaoOk = false;
        }

        //teste
        auxDataAgora = new Date();
        dataAgoraString = dateFormat.format(auxDataAgora);
        try {
            Date auxDateInicio = auxDate;
            auxDate = dateFormat.parse(txtDataFim.getText().toString());
            auxDataAgora = dateFormat.parse(dataAgoraString);

            if (auxDate.before(auxDataAgora)) {
                validacaoOk = false;
                txtDataFim.setError("Data invalida!");
            }
            if (auxDate.before(auxDateInicio)) {
                validacaoOk = false;
                txtDataFim.setError("Data invalida!");
            }

        } catch (ParseException e) {
            e.printStackTrace();
            txtDataFim.setError("Data invalida!");
            validacaoOk = false;
        }

        return validacaoOk;
    }


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog picker = new DatePickerDialog(getActivity(), this, year, month, day);
            picker.getDatePicker().setSpinnersShown(true);
            picker.getDatePicker().setCalendarViewShown(false);
            return picker;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user

            String txtmes = "";
            String txtdia = "";

            month = month + 1;

            if (month < 10) {
                txtmes = "0" + String.valueOf(month);
            } else {
                txtmes = String.valueOf(month);
            }
            if (day < 10) {
                txtdia = "0" + String.valueOf(day);
            } else {
                txtdia = String.valueOf(day);
            }

            String aux = txtdia + "/" + txtmes + "/" + year;
            if (auxDataPicker.equals("inicio")) {
                txtDataInicio.setText(aux);
            } else if (auxDataPicker.equals("fim")) {
                txtDataFim.setText(aux);
            }

        }
    }

    public void limparImagem() {

        fabCloseImagem.hide();
        imagemfoto = null;
        imagemfotoReduzida = null;
        imageView.setImageBitmap(null);
        imageView.setVisibility(View.GONE);
        localArquivoFoto = "";
        auxFotoPromo = "";

    }

    public void PreencheCampos() {
        cod_promo = promocao.getCodPromocao();
        auxFotoPromo = promocao.getFoto();
        txtNome.setText(promocao.getNome());
        txtDataInicio.setText(dateFormat.format(promocao.getDataInicio()));
        txtDataFim.setText(dateFormat.format(promocao.getDataFim()));
        txtDescricao.setText(promocao.getDescricao());
        txtRegras.setText(promocao.getRegras());

        if (!promocao.getFoto().equals("")) {

            Picasso.with(this).load(getResources().getString(R.string.imageserver) + promocao.getFoto()).into(imageView);
            fabCloseImagem.show();
            imageView.setVisibility(View.VISIBLE);
        }

    }


}
