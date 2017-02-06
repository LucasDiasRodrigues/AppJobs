package com.teamappjobs.appjobs.activity;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.teamappjobs.appjobs.R;
import com.teamappjobs.appjobs.asyncTask.LogOutTask;
import com.teamappjobs.appjobs.fragment.BuscaFragment;
import com.teamappjobs.appjobs.fragment.ConfiguracoesFragment;
import com.teamappjobs.appjobs.fragment.HomeFragment;
import com.teamappjobs.appjobs.fragment.MinhasVitrinesFragment;
import com.teamappjobs.appjobs.fragment.VitrinesSigoFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Fabs
    private FloatingActionButton fabCadastrarVitrine;
    private FloatingActionButton fabExemplo;
    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Tratar usuario previamente logado
        Boolean logado = EstaLogado();
        if (!logado) {
                Intent it = new Intent(this, LoginActivity.class);
                startActivity(it);
                finish();
        }

        fabExemplo = (FloatingActionButton) findViewById(R.id.fab);
        fabExemplo.hide();


        fabCadastrarVitrine = (FloatingActionButton) findViewById(R.id.fabCadastroVitrine);
        fabCadastrarVitrine.hide();
        fabCadastrarVitrine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, CadastroVitrineActivity.class);
                intent.putExtra("editar",false);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent,
                            ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                } else {
                    startActivity(intent);
                }

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        HomeFragment fragmentHome = new HomeFragment();
        FragmentTransaction transactionHome = getSupportFragmentManager().beginTransaction();
        transactionHome.replace(R.id.frameLayout, fragmentHome);
        transactionHome.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Bundle args = new Bundle();
                args.putString("palavras",query);

                BuscaFragment fragmentHome = new BuscaFragment();
                fragmentHome.setArguments(args);
                FragmentTransaction transactionHome = getSupportFragmentManager().beginTransaction();
                transactionHome.replace(R.id.frameLayout, fragmentHome);
                transactionHome.commit();

                searchView.clearFocus();
                searchItem.collapseActionView();
                //Open and close the  keyboard
                // Hide the keyboard and give focus to the list

                /*
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                searchView.onActionViewCollapsed();
*/
                return true;
            }

            @Override
            public boolean onQueryTextChange(String searchQuery) {
                return true;
            }
        });
        return true;
    }

     @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.entrar) {
            Intent intent = new Intent(this, LoginActivity.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                startActivity(intent,
                        ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            } else {
                startActivity(intent);
            }

            return true;
        } else if (id == R.id.sair) {

            new AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage(R.string.desejasair)
                    .setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onLogout();
                        }
                    }).setNegativeButton(R.string.nao, null).show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            HomeFragment fragmentHome = new HomeFragment();
            FragmentTransaction transactionHome = getSupportFragmentManager().beginTransaction();
            transactionHome.replace(R.id.frameLayout, fragmentHome);
            transactionHome.commit();
            fabCadastrarVitrine.hide();
            fabExemplo.hide();


        } else if (id == R.id.minhasVitrines) {

            MinhasVitrinesFragment fragmentMinhasVitrines = new MinhasVitrinesFragment();
            android.support.v4.app.FragmentTransaction transactionMinhasVitrines = getSupportFragmentManager().beginTransaction();
            transactionMinhasVitrines.replace(R.id.frameLayout, fragmentMinhasVitrines);
            transactionMinhasVitrines.commit();
            fabCadastrarVitrine.show();
            fabExemplo.hide();

        } else if (id == R.id.vitrinesQueSigo) {
            VitrinesSigoFragment fragmentVitrinesSigo = new VitrinesSigoFragment();
            android.support.v4.app.FragmentTransaction transactionVitrines = getSupportFragmentManager().beginTransaction();
            transactionVitrines.replace(R.id.frameLayout, fragmentVitrinesSigo);
            transactionVitrines.commit();

            fabCadastrarVitrine.hide();
          //  fabExemplo.show();

        } else if (id == R.id.chat) {


        } else if (id == R.id.configuracoes) {
            ConfiguracoesFragment fragmentConfig = new ConfiguracoesFragment();
            FragmentTransaction transactionConfig = getSupportFragmentManager().beginTransaction();
            transactionConfig.replace(R.id.frameLayout, fragmentConfig);
            transactionConfig.commit();


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public boolean EstaLogado() {
        try {
            SharedPreferences prefs = getSharedPreferences("Configuracoes", MODE_PRIVATE);
            if (prefs != null) {
                boolean logado = prefs.getBoolean("logado", false);
                return logado;
            }

        } catch (NullPointerException exception) {

        }

        return false;
    }

    public void onLogout() {

        SharedPreferences prefs = getSharedPreferences("Configuracoes", MODE_PRIVATE);

        LogOutTask task = new LogOutTask(prefs.getString("email", ""), this);
        task.execute();

    }

}
