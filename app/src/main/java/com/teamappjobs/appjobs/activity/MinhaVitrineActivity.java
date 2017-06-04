package com.teamappjobs.appjobs.activity;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.teamappjobs.appjobs.R;
import com.teamappjobs.appjobs.adapter.PagerAdapterMinhaVitrine;
import com.teamappjobs.appjobs.asyncTask.AtivaVitrineTask;
import com.teamappjobs.appjobs.asyncTask.BuscaDetalhesVitrineTask;
import com.teamappjobs.appjobs.asyncTask.CadastraPortifolioTask;
import com.teamappjobs.appjobs.asyncTask.CancelaPromocaoTask;
import com.teamappjobs.appjobs.asyncTask.InativaVitrineTask;
import com.teamappjobs.appjobs.modelo.Vitrine;

import de.hdodenhof.circleimageview.CircleImageView;

public class MinhaVitrineActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

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
    private FloatingActionButton fabEditVitrine;
    private FloatingActionButton fabCadastroPortifolio;
    private FloatingActionButton fabCadastroPromocoes;
    public Vitrine vitrine;

    public Vitrine getVitrine() {
        return vitrine;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minha_vitrine);
        //Recebendo os dados da vitrine
        Intent intent = getIntent();
        vitrine = (Vitrine) intent.getSerializableExtra("vitrine");
        //Configurando a toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        AppBarLayout appbarLayout = (AppBarLayout) findViewById(R.id.materialup_appbar);
        appbarLayout.addOnOffsetChangedListener(this);
        mMaxScrollSize = appbarLayout.getTotalScrollRange();
        mProfileImage = (CircleImageView) findViewById(R.id.materialup_profile_image);
        fotoVitrine = (ImageView) findViewById(R.id.profile_backdrop);
        txtNomeVitrine = (TextView) findViewById(R.id.txtNomeVitrine);
        Picasso.with(this).load(getResources().getString(R.string.imageserver) + vitrine.getFoto()).into(fotoVitrine);
        txtNomeVitrine.setText(vitrine.getNome());
        txtCurtidas = (TextView) findViewById(R.id.txtCurtidas);
        txtSeguidores = (TextView) findViewById(R.id.txtSeguidores);
        mCoordinator = (CoordinatorLayout) findViewById(R.id.root_coordinator);

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
                changeFloatingButtons(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        fabCadastroPromocoes = (FloatingActionButton) findViewById(R.id.fabPromocoes);
        fabCadastroPromocoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPromocao = new Intent(MinhaVitrineActivity.this, PublicarPromocaoActivity.class);
                intentPromocao.putExtra("vitrine", vitrine);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intentPromocao, ActivityOptions.makeSceneTransitionAnimation(MinhaVitrineActivity.this).toBundle());
                } else {
                    startActivity(intentPromocao);
                }
            }
        });
        fabCadastroPortifolio = (FloatingActionButton) findViewById(R.id.fabPortifolio);
        fabCadastroPortifolio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentPortifolio = new Intent(MinhaVitrineActivity.this, CadastroPortifolioActivity.class);
                intentPortifolio.putExtra("vitrine", vitrine);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intentPortifolio, ActivityOptions.makeSceneTransitionAnimation(MinhaVitrineActivity.this).toBundle());
                } else {
                    startActivity(intentPortifolio);
                }
            }
        });
        fabEditVitrine = (FloatingActionButton) findViewById(R.id.fabEdit);
        fabEditVitrine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentEditar = new Intent(MinhaVitrineActivity.this, CadastroVitrineActivity.class);
                intentEditar.putExtra("vitrine", vitrine);
                intentEditar.putExtra("editar", true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intentEditar, ActivityOptions.makeSceneTransitionAnimation(MinhaVitrineActivity.this).toBundle());
                } else {
                    startActivity(intentEditar);
                }
            }
        });

        fabEditVitrine.show();
        fabCadastroPortifolio.hide();
        fabCadastroPromocoes.hide();

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

    public void changeFloatingButtons(int position) {

        switch (position) {
            case 0:
                fabEditVitrine.show();
                fabCadastroPortifolio.hide();
                fabCadastroPromocoes.hide();
                break;
            case 1:
                fabCadastroPortifolio.show();
                fabCadastroPromocoes.hide();
                fabEditVitrine.hide();
                break;
            case 2:
                fabCadastroPortifolio.hide();
                fabEditVitrine.hide();
                fabCadastroPromocoes.show();
                break;
            default:
                fabEditVitrine.show();
                fabCadastroPortifolio.hide();
                fabCadastroPromocoes.hide();
                break;
        }

    }

    public void buscaDetalhesVitrine() {
        BuscaDetalhesVitrineTask task = new BuscaDetalhesVitrineTask(this, vitrine);
        task.execute();
    }

    public void atualizaDetalhesVitrine(Bundle bundle) {

        if (bundle != null) {

            if (!(bundle.getString("foto", "").equals(""))) {
                Picasso.with(this).load(getResources().getString(R.string.imageservermini) + bundle.getString("foto")).into(mProfileImage);
            }
            String auxCurtidas = bundle.getInt("curtidas", 0) + " " + getResources().getString(R.string.curtidas);
            txtCurtidas.setText(auxCurtidas);
            String auxSeguidores = bundle.getInt("seguidores", 0) + " " + getResources().getString(R.string.seguidores);
            txtSeguidores.setText(auxSeguidores);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_minha_vitrine, menu);

        if (vitrine.getSituacao().equals("ativa")) {
            menu.removeItem(R.id.ativar);
        } else {
            menu.removeItem(R.id.inativar);
        }

        return true;
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
                                InativaVitrineTask task = new InativaVitrineTask(MinhaVitrineActivity.this, vitrine);
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
                                AtivaVitrineTask task = new AtivaVitrineTask(MinhaVitrineActivity.this, vitrine);
                                task.execute();
                            }
                        })
                        .setNegativeButton(R.string.nao, null).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
