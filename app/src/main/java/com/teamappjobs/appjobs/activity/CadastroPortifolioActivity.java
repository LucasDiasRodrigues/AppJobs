package com.teamappjobs.appjobs.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.teamappjobs.appjobs.R;
import com.teamappjobs.appjobs.asyncTask.CadastraPortifolioTask;
import com.teamappjobs.appjobs.modelo.Portifolio;
import com.teamappjobs.appjobs.modelo.Vitrine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class CadastroPortifolioActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView imageView;
    private EditText txtLegenda;

    private Vitrine vitrine;

    //para a selecao da imagem
    private String localArquivoFoto;
    private final int IMG_CAM = 1;
    private final int IMG_SDCARD = 2;
    Bitmap imagemfotoReduzida;
    Bitmap imagemfoto;
    String imagemDecodificada = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_portifolio);

        //Configurando a toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Recebendo os dados da vitrine
        Intent intent = getIntent();
        vitrine = (Vitrine) intent.getSerializableExtra("vitrine");



        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EscolherImagem();
            }
        });
        txtLegenda = (EditText) findViewById(R.id.txtLegenda);
        EscolherImagem();

    }

    @Override
    protected void onResume() {
        super.onResume();

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
                    dialog.dismiss();
                } else if (options[item].equals(getText(R.string.escolherdagaleria))) {
                    Intent irParaGaleria = new Intent(Intent.ACTION_GET_CONTENT);
                    irParaGaleria.setType("image/*");
                    startActivityForResult(irParaGaleria, IMG_SDCARD);
                    dialog.dismiss();
                } else if (options[item].equals(getText(R.string.cancelar))) {
                    dialog.dismiss();
                    finish();

                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == IMG_CAM && resultCode == RESULT_OK) {

            imagemfoto = BitmapFactory.decodeFile(localArquivoFoto);
            imagemfotoReduzida = Bitmap.createScaledBitmap(imagemfoto, imageView.getWidth(), imageView.getHeight(), true);
            imageView.setImageBitmap(imagemfotoReduzida);
            imageView.setTag(localArquivoFoto);


        } else if (data != null && requestCode == IMG_SDCARD && resultCode == RESULT_OK) {
            Uri img = data.getData();

            InputStream inputStream;
            try {
                inputStream = getContentResolver().openInputStream(img);
                //Imagem original
                imagemfoto = BitmapFactory.decodeStream(inputStream);

                imagemfotoReduzida = Bitmap.createScaledBitmap(imagemfoto, imageView.getWidth(), imageView.getHeight(), true);
                imageView.setImageBitmap(imagemfotoReduzida);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setTag(localArquivoFoto);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Erro ao carregar a imagem", Toast.LENGTH_LONG).show();
            }


        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cadastro_portifolio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.avancar:

                Portifolio portifolio = new Portifolio();

                portifolio.setCodVitrine(vitrine.getCodVitrine());
                if (!txtLegenda.getText().toString().equals("")) {
                    portifolio.setDescricao(txtLegenda.getText().toString());
                } else {
                    portifolio.setDescricao("");
                }

                CadastraPortifolioTask task = new CadastraPortifolioTask(this,portifolio,imagemfoto);
                task.execute();

                break;
        }


        return super.onOptionsItemSelected(item);
    }


}
