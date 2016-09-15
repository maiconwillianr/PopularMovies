package br.com.maiconribeiro.popularmovies;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import br.com.maiconribeiro.popularmovies.adapters.DataAdapter;
import br.com.maiconribeiro.popularmovies.listener.EndlessRecyclerViewScrollListener;
import br.com.maiconribeiro.popularmovies.model.Filme;
import br.com.maiconribeiro.popularmovies.sync.BuscarFilmesService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        this.initViews();
    }

    private void initViews() {

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        //Lista de Filmes
        final ArrayList<Filme> todosFilmes = criarGridFilmes(String.valueOf(1));

        final DataAdapter adapter = new DataAdapter(getApplicationContext(), todosFilmes);
        recyclerView.setAdapter(adapter);

        // Add the scroll listener
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {

                List<Filme> maisFilmes = criarGridFilmes(String.valueOf(page));
                todosFilmes.addAll(maisFilmes);
                Handler handler = new Handler();
                final Runnable r = new Runnable() {
                    public void run() {
                        int curSize = adapter.getItemCount();
                        adapter.notifyItemRangeInserted(curSize, todosFilmes.size() - 1);
                    }
                };
                handler.post(r);
            }
        });

    }

    private ArrayList<Filme> criarGridFilmes(String page) {

        ArrayList<Filme> filmes = new ArrayList();

        BuscarFilmesService buscarFilmesService = new BuscarFilmesService();
        try {
            filmes.addAll(buscarFilmesService.execute(page).get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return filmes;
    }
}
