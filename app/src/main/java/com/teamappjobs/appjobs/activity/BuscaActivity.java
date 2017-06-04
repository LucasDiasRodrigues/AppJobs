package com.teamappjobs.appjobs.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.teamappjobs.appjobs.R;
import com.teamappjobs.appjobs.adapter.ExpandableListViewCategoriasAdapter;
import com.teamappjobs.appjobs.adapter.PagerAdapterBuscar;
import com.teamappjobs.appjobs.asyncTask.ListaCategoriasTask;
import com.teamappjobs.appjobs.asyncTask.ListaResultadoBuscaTask;
import com.teamappjobs.appjobs.modelo.Categoria;
import com.teamappjobs.appjobs.modelo.SubCategoria;
import com.teamappjobs.appjobs.modelo.Vitrine;
import com.teamappjobs.appjobs.util.BuscarEventBus;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class BuscaActivity extends AppCompatActivity {

    //Tabs
    private Toolbar toolbar;
    private ViewPager mViewPager;
    private int numTabs = 2;
    private TabLayout mTabLayout;

    private SearchView searchView;

    //PagerAdapterBusca
    private PagerAdapterBuscar pagerAdapterBusca;
    private boolean isBuscaActive = false;

    private ExpandableListView listView;

    List<Categoria> categorias;
    List<SubCategoria> subCategorias;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busca);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Configurando as tabs e fragments
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);

        listView = (ExpandableListView) findViewById(R.id.expandableList);

        ligaTabsBusca(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        // Barra de perquisa
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ligaTabsBusca(true);
                Pesquisa(query);
                toolbar.setTitle(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String searchQuery) {
                return true;
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                ligaTabsBusca(false);
                return true;
            }
        });

        return true;
    }

    public void ligaTabsBusca(boolean liga) {
        if (liga) {
            //Configurando as tabs e fragments
            mTabLayout.setVisibility(View.VISIBLE);
            mViewPager.setVisibility(View.VISIBLE);
            pagerAdapterBusca = new PagerAdapterBuscar(getSupportFragmentManager(), this, numTabs);
            mViewPager.setAdapter(pagerAdapterBusca);
            mTabLayout.setupWithViewPager(mViewPager);
            isBuscaActive = true;
            listView.setVisibility(View.GONE);
        } else {
            mTabLayout.setVisibility(View.GONE);
            mViewPager.setVisibility(View.GONE);
            mViewPager.setAdapter(null);
            isBuscaActive = false;

            listView.setVisibility(View.VISIBLE);
            ListaCategorias();
        }
    }

    //Metodo para a busca
    public void Pesquisa(String query) {
        ListaResultadoBuscaTask task = new ListaResultadoBuscaTask(this, query);
        task.execute();
    }

    //Metodo para a busca
    public void RecebeLista(List<Vitrine> vitrines) {
        BuscarEventBus event = new BuscarEventBus();
        event.setVitrines(vitrines);
        EventBus.getDefault().post(event);
    }

    public void ListaCategorias() {
        ListaCategoriasTask task = new ListaCategoriasTask(this);
        task.execute();
    }

    public void AtualizaListaCategoria(final List<Categoria> categorias) {
        this.categorias = categorias;

        listView.setAdapter(new ExpandableListViewCategoriasAdapter(this,categorias));
        listView.setDividerHeight(0);
        listView.setHeaderDividersEnabled(false);
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                ListaResultadoBuscaTask task = new ListaResultadoBuscaTask(BuscaActivity.this,
                        categorias.get(groupPosition).getSubCategorias().get(childPosition).getCodSubCat());
                task.execute();
                ligaTabsBusca(true);
                return true;
            }
        });
    }
}
