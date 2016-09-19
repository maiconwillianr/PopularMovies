package br.com.maiconribeiro.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import br.com.maiconribeiro.popularmovies.adapters.DataAdapter;
import br.com.maiconribeiro.popularmovies.helpers.Util;
import br.com.maiconribeiro.popularmovies.interfaces.AsyncTaskDelegate;
import br.com.maiconribeiro.popularmovies.listener.EndlessRecyclerViewScrollListener;
import br.com.maiconribeiro.popularmovies.model.Filme;
import br.com.maiconribeiro.popularmovies.sync.BuscarFilmesService;

public class MainActivity extends AppCompatActivity implements AsyncTaskDelegate {


    private String filtroPref;
    private DataAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        this.initViews();
    }

    @Override
    protected void onResume() {

        this.obterPreferenciasUsuario();

        this.initViews();

        super.onResume();
    }

    @Override
    public void processFinish(Object output) {

        if (output != null) {

            ArrayList<Filme> filmes = (ArrayList<Filme>) output;

            adapter.notifyItemRangeInserted(adapter.getItemCount(), filmes.size() - 1);

        } else {
            Toast.makeText(this, R.string.connection_error, Toast.LENGTH_LONG).show();
        }

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


    private void initViews() {

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        this.obterPreferenciasUsuario();

        final ArrayList<Filme> todosFilmes = this.listarFilmes(String.valueOf(1), filtroPref);

        adapter = new DataAdapter(getApplicationContext(), todosFilmes);
        recyclerView.setAdapter(adapter);

        // Add the scroll listener
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {

                List<Filme> maisFilmes = listarFilmes(String.valueOf(page), filtroPref);
                todosFilmes.addAll(maisFilmes);
            }
        });

    }

    //Cria a grid de filmes através do webservice
    private ArrayList<Filme> listarFilmes(String page, String filtroPesquisa) {

        //Pega a data atual para utilizar na pesquisa
        final LocalDate localDate = new LocalDate();

        BuscarFilmesService buscarFilmesService = new BuscarFilmesService(this);

        try {

            //Verifica se a conexao com a internet
            if (Util.checkConnection(this)) {
                //Lista de Filmes utilizando espaço de tempo de um mês
                return buscarFilmesService.execute(page, localDate.minusMonths(1).toString(), localDate.toString(), filtroPesquisa).get();
            } else {
                //findViewById(R.id.rootLayout).setBackgroundResource(R.drawable.connection_error);
                Toast.makeText(this, R.string.connection_error, Toast.LENGTH_LONG).show();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void obterPreferenciasUsuario() {

        //Preferencias selecionadas pelo usuário
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);

        filtroPref = settings.getString(getString(R.string.pref_ordenacao_key), getString(R.string.pref_ordenacao_default));
        if (filtroPref == null || filtroPref.equals("")) {
            //Obtém a ordenacao deafult
            filtroPref = getString(R.string.pref_ordenacao_default);
        }
    }
}
