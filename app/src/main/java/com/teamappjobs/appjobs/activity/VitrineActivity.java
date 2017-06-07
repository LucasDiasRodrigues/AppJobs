package com.teamappjobs.appjobs.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.teamappjobs.appjobs.R;
import com.teamappjobs.appjobs.adapter.PagerAdapterMinhaVitrine;
import com.teamappjobs.appjobs.asyncTask.AtivaVitrineTask;
import com.teamappjobs.appjobs.asyncTask.BuscaDetalhesVitrineTask;
import com.teamappjobs.appjobs.asyncTask.CurtirVitrineTask;
import com.teamappjobs.appjobs.asyncTask.InativaVitrineTask;
import com.teamappjobs.appjobs.asyncTask.UsuarioConsultaCurtidaVitrineTask;
import com.teamappjobs.appjobs.modelo.Vitrine;

import de.hdodenhof.circleimageview.CircleImageView;

public class VitrineActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    //Animate
    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;
    private boolean mIsAvatarShown = true;

    private ImageView mProfileImage;
    private TextView txtCurtidas;
    private TextView txtSeguidores;
    private int mMaxScrollSize;
    private ImageView fotoVitrine;
    private TextView txtNomeVitrine;

    private ViewPager mViewPager;
    private int numTabs = 3;
    private TabLayout mTabLayout;
    private Toolbar toolbar;

    private CoordinatorLayout mCoordinator;
    private ImageView imageViewToolbar;

    private Vitrine vitrine;
    private Boolean VarBtnClickCurtir;
    private Boolean VarOnOpenCurtir=false; // Só executa a task se o click vier do botão
    private Boolean VarBtnClickSeguir;
    private Boolean VarOnOpenSeguir=false;

    public Vitrine getVitrine() {
        return vitrine;
    }

    private FloatingActionButton btnCurtir;
    private Button btnSeguir;
   private SharedPreferences prefs ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vitrine);
        //Recebendo os dados da vitrine
        prefs=  this.getSharedPreferences("Configuracoes", Context.MODE_PRIVATE);
        Intent intent = getIntent();
        vitrine = (Vitrine) intent.getSerializableExtra("vitrine");

        btnCurtir = (FloatingActionButton) findViewById(R.id.btnCurtir);
        btnSeguir = (Button)findViewById(R.id.btnSeguir);
        btnSeguir.setOnClickListener(btnSeguirOnClickListener);

        try {
            //Se a vitrine for de quem está acessando, oculta os botões.
            if (vitrine.getEmailAnunciante().equals(prefs.getString("email", ""))) {
                btnSeguir.setVisibility(View.GONE);
                btnSeguir.setVisibility(View.GONE);
            } else {
                verificaCurtidaVitrine();
            }
        }
        catch (Exception ex){
            Log.i("Erro emailanunc:", ex.toString());
            btnSeguir.setVisibility(View.GONE);
            btnSeguir.setVisibility(View.GONE);
        }
        AppBarLayout appbarLayout = (AppBarLayout) findViewById(R.id.materialup_appbar);
        appbarLayout.addOnOffsetChangedListener(this);
        mMaxScrollSize = appbarLayout.getTotalScrollRange();
        mProfileImage = (CircleImageView) findViewById(R.id.materialup_profile_image);
        fotoVitrine = (ImageView) findViewById(R.id.profile_backdrop);
        txtNomeVitrine = (TextView) findViewById(R.id.txtNomeVitrine);

        Picasso.with(this).load(getResources().getString(R.string.imageserver) + vitrine.getFoto()).into(fotoVitrine);

        txtCurtidas = (TextView) findViewById(R.id.txtCurtidas);
        txtSeguidores = (TextView) findViewById(R.id.txtSeguidores);
        mCoordinator = (CoordinatorLayout) findViewById(R.id.root_coordinator);


        btnCurtir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(verificaUsuarioLogado()){
                VarOnOpenCurtir=true;
                controlaBotaoCurtida();
                buscaDetalhesVitrine();
            }
                else
                {
                    // Mandar usuário para a tela de login/Cadastro
                    Intent it = new Intent(VitrineActivity.this,LoginActivity.class);
                    startActivity(it);


                }

            }
        });

        //Configurando as tabs e fragments
        PagerAdapterMinhaVitrine pagerAdapter = new PagerAdapterMinhaVitrine(getSupportFragmentManager(), this, numTabs);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {



            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        mProfileImage.setAlpha(0f);
    }

    @Override
    protected void onResume() {
        super.onResume();
        buscaDetalhesVitrine();
        mProfileImage.animate().setStartDelay(800).alpha(1);
    }

    @Override
    public void onBackPressed() {
        mIsAvatarShown = false;
        mProfileImage.animate().setStartDelay(0).alpha(0);
        super.onBackPressed();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();

        int percentage = (Math.abs(verticalOffset)) * 100 / mMaxScrollSize;

        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
            mIsAvatarShown = false;
            mProfileImage.animate().setStartDelay(0).scaleY(0).scaleX(0).setDuration(200).start();
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
            mIsAvatarShown = true;

            mProfileImage.animate().setStartDelay(0)
                    .scaleY(1).scaleX(1)
                    .start();
        }
    }

    public void buscaDetalhesVitrine() {
        BuscaDetalhesVitrineTask task = new BuscaDetalhesVitrineTask(this, vitrine);
        task.execute();
    }

    public void atualizaDetalhesVitrine(Bundle bundle) {

        if (bundle != null) {

            if (!(bundle.getString("foto","").equals(""))) {
                Picasso.with(this).load(getResources().getString(R.string.imageservermini) + bundle.getString("foto")).into(mProfileImage);
            }
            txtNomeVitrine.setText(vitrine.getNome());
            String auxCurtidas = bundle.getInt("curtidas", 0) + " " + getResources().getString(R.string.curtidas);
            txtCurtidas.setText(auxCurtidas);
            String auxSeguidores = bundle.getInt("seguidores", 0) + " " + getResources().getString(R.string.seguidores);
            txtSeguidores.setText(auxSeguidores);
        }

    }

    public void  atualizaCurtidaAtual(Bundle bundle){
        if(bundle != null){
           if(bundle.getString("RespostaCurte").equals("curtiu")){

               VarBtnClickCurtir=true;
               controlaBotaoCurtida();
               btnCurtir.setVisibility(View.VISIBLE);
           }
           else if  (bundle.getString("RespostaCurte").equals("ncurtiu")){
               VarBtnClickCurtir=false;
               controlaBotaoCurtida();
               btnCurtir.setVisibility(View.VISIBLE);
           }

            if(bundle.getString("RespostaSegue").equals("curtiu")){
                VarBtnClickSeguir=true;
                controlaBotaoSeguir();
                btnSeguir.setVisibility(View.VISIBLE);
            }
            else if  (bundle.getString("RespostaSegue").equals("ncurtiu")){
                VarBtnClickSeguir=false;
                controlaBotaoSeguir();
                btnSeguir.setVisibility(View.VISIBLE);
            }

        }
    }

    public  void  verificaCurtidaVitrine(){

        UsuarioConsultaCurtidaVitrineTask task = new UsuarioConsultaCurtidaVitrineTask(this, vitrine.getCodVitrine(),prefs.getString("email", ""));
        task.execute();
    }


    public void controlaBotaoCurtida(){
        if(VarBtnClickCurtir){
            if(VarOnOpenCurtir) {
                CurtirVitrineTask task = new CurtirVitrineTask(VitrineActivity.this,prefs.getString("email", ""), vitrine.getCodVitrine(), "curtir_vitrine-json");
                task.execute();
            }
            VarBtnClickCurtir=false;
            btnCurtir.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDark)));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                btnCurtir.setImageDrawable(getResources().getDrawable(R.drawable.ic_heart_broken_white_48dp, getApplicationContext().getTheme()));
            } else {
                btnCurtir.setImageDrawable(getResources().getDrawable(R.drawable.ic_heart_broken_white_48dp));
            }
        }
        else{
            if(VarOnOpenCurtir) {
                CurtirVitrineTask task = new CurtirVitrineTask(VitrineActivity.this, prefs.getString("email", ""), vitrine.getCodVitrine(), "descurtir_vitrine-json");
                task.execute();
            }
            VarBtnClickCurtir=true;
            btnCurtir.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                btnCurtir.setImageDrawable(getResources().getDrawable(R.drawable.ic_heart_white_48dp, getApplicationContext().getTheme()));
            } else {
                btnCurtir.setImageDrawable(getResources().getDrawable(R.drawable.ic_heart_white_48dp));
            }

        }
    }


    public void controlaBotaoSeguir(){
        if(VarBtnClickSeguir){
            if(VarOnOpenSeguir) {
                CurtirVitrineTask task = new CurtirVitrineTask(VitrineActivity.this, prefs.getString("email", ""), vitrine.getCodVitrine(), "seguir_vitrine-json");
                task.execute();
            }
            VarBtnClickSeguir=false;
        btnSeguir.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDark)));
        btnSeguir.setText(" Deixar de seguir");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
              //  btnSeguir.setImageDrawable(getResources().getDrawable(R.drawable.ic_heart_broken_white_48dp, getApplicationContext().getTheme()));
            } else {
                //btnSeguir.setImageDrawable(getResources().getDrawable(R.drawable.ic_heart_broken_white_48dp));
            }
            //Testar no Lollipop
            int imgResource = R.drawable.bell_off;
            btnSeguir.setCompoundDrawablesWithIntrinsicBounds(imgResource, 0, 0, 0);
        }
        else{
            if(VarOnOpenSeguir) {
                CurtirVitrineTask task = new CurtirVitrineTask(VitrineActivity.this, prefs.getString("email", ""), vitrine.getCodVitrine(), "deixar_de_seguir_vitrine-json");
                task.execute();
            }
            VarBtnClickSeguir=true;
            btnSeguir.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRed)));
            btnSeguir.setText(" Seguir");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
               // btnSeguir.setImageDrawable(getResources().getDrawable(R.drawable.ic_heart_white_48dp, getApplicationContext().getTheme()));

            } else {
                //btnSeguir.setImageDrawable(getResources().getDrawable(R.drawable.ic_heart_white_48dp));
            }
            //Testar no Lollipop
            int imgResource = R.drawable.bell;
            btnSeguir.setCompoundDrawablesWithIntrinsicBounds(imgResource, 0, 0, 0);
        }
    }

    public boolean verificaUsuarioLogado(){
        try {
        SharedPreferences prefs = this.getSharedPreferences("Configuracoes", this.MODE_PRIVATE);
        if (prefs.getBoolean("logado", false)) {
            return true;
        }
        else
        {
            return false;
        }
        } catch (NullPointerException exception) {
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.inativar:
                //Inativar
                new AlertDialog.Builder(this).setTitle(R.string.inativarVitrine)
                        .setMessage(R.string.desejaInativarVitrine)
                        .setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                InativaVitrineTask task = new InativaVitrineTask(VitrineActivity.this, vitrine);
                                task.execute();
                            }
                        })
                        .setNegativeButton(R.string.nao, null).show();
                return true;

            case R.id.ativar:
                //Inativar
                new AlertDialog.Builder(this).setTitle(R.string.ativarVitrine)
                        .setMessage(R.string.desejaAtivarVitrine)
                        .setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AtivaVitrineTask task = new AtivaVitrineTask(VitrineActivity.this, vitrine);
                                task.execute();
                            }
                        })
                        .setNegativeButton(R.string.nao, null).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private View.OnClickListener btnSeguirOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
         if(verificaUsuarioLogado()){
            VarOnOpenSeguir=true;
            controlaBotaoSeguir();
            buscaDetalhesVitrine();
        }
         else{
             // Mandar para a tela de login/Cadastro
             Intent it = new Intent(VitrineActivity.this,LoginActivity.class);
             startActivity(it);
         }

        }
    };
}
