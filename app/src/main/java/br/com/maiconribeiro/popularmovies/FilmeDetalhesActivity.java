package br.com.maiconribeiro.popularmovies;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import br.com.maiconribeiro.popularmovies.model.Filme;
import br.com.maiconribeiro.popularmovies.model.Video;

public class FilmeDetalhesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_filme_detalhes);

        if (getIntent().getExtras() != null) {

            final Filme filme = getIntent().getExtras().getParcelable(Filme.PARCELABLE_KEY);

            final String URL_BASE = "https://api.themoviedb.org/3/";
            final String TIPO_SERVICO = "movie";
            final String VIDEOS = "videos";
            final String MOVIE_ID = filme.getIdFilme();
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

            Log.i("url", url.toString());
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url.toString(),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            filme.setVideos(parseJson(response));
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });

            RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
            queue.add(request);

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

    public ArrayList<Video> parseJson(JSONObject videosJson) {

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
}
