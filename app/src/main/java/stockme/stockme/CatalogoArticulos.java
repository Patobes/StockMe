package stockme.stockme;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;

import stockme.stockme.logica.Articulo;
import stockme.stockme.util.OpcionesMenus;
import stockme.stockme.util.Preferencias;
import stockme.stockme.util.Util;

public class CatalogoArticulos extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        Fragment_listas.OnFragmentInteractionListener, Fragment_catalogo_todos.OnFragmentInteractionListener, Fragment_catalogo_tipos.OnFragmentInteractionListener {
    FragmentPagerAdapter adapterViewPager;
    private GridView articulos;
    private Button aniadir;
    private ImageButton btn_reset;

    private static NavigationView nav_menu;

    //TODO: corregir la *** navegación y comportamiento de la opción de búsqueda
    //si no se puede... a tomar por saco y borrarla
    private static String querySearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo_articulos);
        Preferencias.setPreferencia("anterior", "Artículos");

        //toolbar + navbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.setTitle("Catálogo");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        nav_menu = (NavigationView) findViewById(R.id.nav_view);
        nav_menu.setNavigationItemSelectedListener(this);

        querySearch = null;//para resetear la búsqueda al entrar en Artículos

        Intent i = getIntent();
        if (i != null){
            if (i.getExtras() != null){
                String viene = i.getExtras().getString("vieneDe");
                if(viene != null)
                    Util.vieneDe = viene;
            }
        }

        nav_menu.getMenu().getItem(2).setChecked(true);


        aniadir = (Button) findViewById(R.id.fragment_catalogo_btn_mas);

        aniadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ArticuloSimpleAdd.class);
                startActivityForResult(i,1);
            }
        });

        btn_reset = (ImageButton)findViewById(R.id.fragment_catalogo_btn_reset_search);
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CatalogoArticulos.this, CatalogoArticulos.class);
                i.setAction(Intent.ACTION_SEARCH);
                startActivity(i);
                finish();
            }
        });

        handleIntent(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return OpcionesMenus.onNavigationItemSelected(item, this);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        ViewPager vpPager = (ViewPager) findViewById(R.id.pager_catalogo);
        MyPagerAdapter adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        //Número de páginas
        private static int NUM_ITEMS = 2;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Devolver el total de páginas
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Crear el fragment a devolver en cada página
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    Fragment_catalogo_todos ft = new Fragment_catalogo_todos();
                    if(querySearch != null)
                        ft.setQuerySearch(querySearch);
                    else
                        ft.setQuerySearch(null);
                    return ft;
                case 1:
                    Fragment_catalogo_tipos fti = new Fragment_catalogo_tipos();
                    if(querySearch != null)
                        fti.setQuerySearch(querySearch);
                    else
                        fti.setQuerySearch(null);
                    return fti;
                default:
                    Fragment_catalogo_todos ftd = new Fragment_catalogo_todos();
                    if(querySearch != null)
                        ftd.setQuerySearch(querySearch);
                    else
                        ftd.setQuerySearch(null);
                    return ftd;
            }
        }

        // Nombre de cada página
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Todos";
                case 1:
                    return "Tipos";
                default:
                    return "";
            }
        }

    }

    @Override
    public void onArticuloSeleccionado(Articulo articulo) {

        if(Util.vieneDe.equals("articulosAdd")){
            Intent i = new Intent(this,ArticulosAdd.class);
            i.putExtra("Articulo",articulo);
            setResult(1,i);
            finish();
        }else{
            Util.mostrarToast(this, "No encuentro el articulosAdd");
        }
    }

    @Override
    public void onBackPressed() {
        OpcionesMenus.onBackPressed(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
        finish();
    }

    private void handleIntent(Intent intent) {
        String accion = intent.getAction();
        if (Intent.ACTION_SEARCH.equals(accion)) {
            querySearch = intent.getStringExtra(SearchManager.QUERY);
            if(querySearch != null && querySearch.isEmpty())
                querySearch = null;
        }
    }
}
