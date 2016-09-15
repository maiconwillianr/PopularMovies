package br.com.maiconribeiro.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import br.com.maiconribeiro.popularmovies.adapters.DataAdapter;
import br.com.maiconribeiro.popularmovies.model.Filme;
import br.com.maiconribeiro.popularmovies.sync.BuscarFilmesService;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        this.initViews();
    }

    private void initViews() {

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        DataAdapter adapter = new DataAdapter(getApplicationContext(), criarGridFilmes());
        recyclerView.setAdapter(adapter);

    }

    private ArrayList<Filme> criarGridFilmes() {

        ArrayList<Filme> filmes = new ArrayList();

        BuscarFilmesService buscarFilmesService = new BuscarFilmesService();
        try {
            filmes.addAll(buscarFilmesService.execute().get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return filmes;
    }
}
