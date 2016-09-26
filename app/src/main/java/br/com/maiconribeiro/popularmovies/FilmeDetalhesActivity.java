package br.com.maiconribeiro.popularmovies;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import br.com.maiconribeiro.popularmovies.helpers.Util;
import br.com.maiconribeiro.popularmovies.model.Filme;
import br.com.maiconribeiro.popularmovies.model.Video;

public class FilmeDetalhesActivity extends AppCompatActivity {

    private Filme filme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_filme_detalhes);

        if (getIntent().getExtras() != null) {

            filme = getIntent().getExtras().getParcelable(Filme.PARCELABLE_KEY);

            //Verifica se a conexao com a internet
            if (Util.checkConnection(this)) {

                this.buscarVideos(filme.getIdFilme());
            }

            //Seta o titulo da ActionBar com o nome do filme escolhido
            getSupportActionBar().setTitle(filme.getTitulo());

            TextView labelTitulo = (TextView) findViewById(R.id.tituloDetalhe);
            labelTitulo.setText(filme.getTitulo());

            TextView dataLancamento = (TextView) findViewById(R.id.dataLancamento);
            if (filme.getDataLancamento() != null) {
                DateTime dt = DateTime.parse(filme.getDataLancamento(), DateTimeFormat.forPattern("yyyy-MM-dd"));
                dataLancamento.setText(dt.toString("dd-MM-yyyy"));
            } else {
                dataLancamento.setText("");
            }

            ImageView imageFilme = (ImageView) findViewById(R.id.imagemDetalhe);

            if (!"".equals(filme.getPathImagemPoster())) {
                Picasso.with(this).load(filme.getPathImagemPoster()).into(imageFilme);
            } else {
                Picasso.with(this).load(R.drawable.not_found).into(imageFilme);
            }

            TextView labelDuracao = (TextView) findViewById(R.id.duracao);
            labelDuracao.setText(filme.getDuracao());

            TextView labelNumeroVotos = (TextView) findViewById(R.id.numeroVotos);
            labelNumeroVotos.setText(filme.getNumeroVotos());

            Float notaMedia = Float.valueOf(filme.getNotaMedia());
            RatingBar rb = (RatingBar) findViewById(R.id.mediaVotos);
            rb.setIsIndicator(true);
            rb.setRating(notaMedia / 2);

            TextView labelSinopse = (TextView) findViewById(R.id.sinopse);
            labelSinopse.setText(filme.getSinopse());

        }

        //Adiciona o botão up navegation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void buscarVideos(String idFilme) {

        final String URL_BASE = "https://api.themoviedb.org/3/";
        final String TIPO_SERVICO = "movie";
        final String VIDEOS = "videos";
        final String MOVIE_ID = idFilme;
        final String API_KEY = "api_key";

        Uri builtUri = Uri.parse(URL_BASE).buildUpon()
                .appendPath(TIPO_SERVICO)
                .appendPath(MOVIE_ID)
                .appendPath(VIDEOS)
                .appendQueryParameter(API_KEY, BuildConfig.THE_MOVIE_DB_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url.toString(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        filme = parseJson(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
        queue.add(request);
    }

    private ArrayList<Video> parseVideoJson(JSONObject videosJson) {

        ArrayList<Video> videos = new ArrayList<>();

        final String RESULTS = "results";
        final String ID = "id";
        final String ISO_639_1 = "iso_639_1";
        final String ISO_3166_1 = "iso_3166_1";
        final String KEY = "key";
        final String NAME = "name";
        final String SITE = "site";
        final String SIZE = "size";
        final String TYPE = "type";

        try {

            JSONArray videosArray = videosJson.getJSONArray(RESULTS);

            if (videosArray.length() > 0) {

                for (int i = 0; i < videosArray.length(); i++) {

                    JSONObject v = videosArray.getJSONObject(i);

                    Video video = new Video();

                    video.setIdVideo(v.getString(ID));
                    video.setIso_639_1(v.getString(ISO_639_1));
                    video.setIso_3166_1(v.getString(ISO_3166_1));
                    video.setKey(v.getString(KEY));
                    video.setName(v.getString(NAME));
                    video.setSite(v.getString(SITE));
                    video.setSize(v.getString(SIZE));
                    video.setType(v.getString(TYPE));

                    videos.add(video);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return videos;
    }

    public Filme parseJson(JSONObject f) {

        Filme filme = new Filme();

        final String ID = "id";
        final String IMAGE_PATH = "http://image.tmdb.org/t/p/w185";
        final String TITLE = "title";
        final String POSTER_PATH = "poster_path";
        final String VOTE_AVAREGE = "vote_average";
        final String OVERVIEW = "overview";
        final String RELEASE_DATE = "release_date";
        final String VOTE_COUNT = "vote_count";
        final String RUNTIME = "runtime";

        try {

            filme.setIdFilme(f.getString(ID));
            filme.setTitulo(f.getString(TITLE));
            if (!"null".equals(f.getString(POSTER_PATH))) {
                filme.setPathImagemPoster(IMAGE_PATH + f.get(POSTER_PATH));
            } else {
                filme.setPathImagemPoster("");
            }
            filme.setNotaMedia(f.getString(VOTE_AVAREGE));
            filme.setSinopse(f.getString(OVERVIEW));
            filme.setDataLancamento(f.getString(RELEASE_DATE));
            filme.setNumeroVotos(f.getString(VOTE_COUNT));
            filme.setDuracao(f.getString(RUNTIME));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return filme;
    }
}
