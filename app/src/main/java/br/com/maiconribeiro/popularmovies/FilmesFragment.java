package br.com.maiconribeiro.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.joda.time.LocalDate;

import java.util.ArrayList;

import br.com.maiconribeiro.popularmovies.adapters.DataAdapter;
import br.com.maiconribeiro.popularmovies.helpers.Util;
import br.com.maiconribeiro.popularmovies.interfaces.AsyncTaskDelegate;
import br.com.maiconribeiro.popularmovies.listener.EndlessRecyclerViewScrollListener;
import br.com.maiconribeiro.popularmovies.model.Filme;
import br.com.maiconribeiro.popularmovies.sync.FilmesService;


public class FilmesFragment extends Fragment implements AsyncTaskDelegate {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String filtroPref;
    private DataAdapter adapter;
    private RecyclerView recyclerView;
    private Context context;

    private ArrayList<Filme> todosFilmes;

    public FilmesFragment() {
    }

    public static FilmesFragment newInstance(String param1, String param2) {
        FilmesFragment fragment = new FilmesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {

        this.obterPreferenciasUsuario();

        super.onResume();
    }

    @Override
    public void processFinish(Object output) {

        if (output != null) {

            if (adapter == null) {
                todosFilmes = (ArrayList<Filme>) output;
                adapter = new DataAdapter(context, todosFilmes);
                recyclerView.setAdapter(adapter);
            } else {
                todosFilmes.addAll((ArrayList<Filme>) output);
                adapter.notifyItemRangeInserted(adapter.getItemCount(), todosFilmes.size() - 1);
            }

        } else {
            Toast.makeText(context, R.string.connection_error, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_filmes, container, false);

        todosFilmes = new ArrayList<>();

        this.obterPreferenciasUsuario();

        this.listarFilmes(String.valueOf(1), filtroPref);

        this.initViews(rootView);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void initViews(View rootView) {

        recyclerView = (RecyclerView) rootView.findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new DataAdapter(context, todosFilmes));

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        // Add the scroll listener
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {

                listarFilmes(String.valueOf(page), filtroPref);

            }
        });

    }

    //Cria a grid de filmes através do webservice
    private void listarFilmes(String page, String filtroPesquisa) {

        //Pega a data atual para utilizar na pesquisa
        final LocalDate localDate = new LocalDate();

        FilmesService filmesService = new FilmesService(this, context);

        //Verifica se a conexao com a internet
        if (Util.checkConnection(context)) {
            //Lista de Filmes utilizando espaço de tempo de um mês
            filmesService.buscarFilmes(page, localDate.minusMonths(1).toString(), localDate.toString(), filtroPesquisa);
        } else {
            //findViewById(R.id.rootLayout).setBackgroundResource(R.drawable.connection_error);
            Toast.makeText(context, R.string.connection_error, Toast.LENGTH_LONG).show();
        }

    }

    public void obterPreferenciasUsuario() {

        //Preferencias selecionadas pelo usuário
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);

        filtroPref = settings.getString(getString(R.string.pref_ordenacao_key), getString(R.string.pref_ordenacao_default));
        if (filtroPref == null || filtroPref.equals("")) {
            //Obtém a ordenacao deafult
            filtroPref = getString(R.string.pref_ordenacao_default);
        }
    }
}
