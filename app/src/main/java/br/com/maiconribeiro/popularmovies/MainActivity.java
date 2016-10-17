package br.com.maiconribeiro.popularmovies;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import br.com.maiconribeiro.popularmovies.adapters.TabsAdapter;
import br.com.maiconribeiro.popularmovies.interfaces.AsyncTaskDelegate;
import br.com.maiconribeiro.popularmovies.model.Genero;
import br.com.maiconribeiro.popularmovies.sync.FilmesService;

public class MainActivity extends AppCompatActivity implements AsyncTaskDelegate {

    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        FilmesService filmesService = new FilmesService(this, getApplicationContext());
        filmesService.buscarGeneros();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {

            Intent settingsIntent = new Intent(this, SettingsActivity.class);

            startActivity(settingsIntent);

            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void processFinish(Object output) {

        if (output != null) {

            List<Genero> generos = (ArrayList<Genero>) output;

            tabLayout.addTab(tabLayout.newTab().setText("TODOS"));
            for(Genero genero : generos){
                tabLayout.addTab(tabLayout.newTab().setText(genero.getName()));
            }
            generos.add(0, new Genero(null, "Todos"));

            final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
            final TabsAdapter adapter = new TabsAdapter
                    (getSupportFragmentManager(), tabLayout.getTabCount(), generos);
            viewPager.setAdapter(adapter);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });


        }
    }
}
