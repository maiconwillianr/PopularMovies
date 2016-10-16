package br.com.maiconribeiro.popularmovies.sync;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import br.com.maiconribeiro.popularmovies.BuildConfig;
import br.com.maiconribeiro.popularmovies.VolleySingleton;
import br.com.maiconribeiro.popularmovies.helpers.Util;
import br.com.maiconribeiro.popularmovies.interfaces.AsyncTaskDelegate;
import br.com.maiconribeiro.popularmovies.model.Filme;
import br.com.maiconribeiro.popularmovies.model.Genero;
import br.com.maiconribeiro.popularmovies.model.Video;

/**
 * Created by maiconwillianribeiro on 16/10/16.
 */

public class FilmesService {

    private final String LOG_TAG = FilmesService.class.getSimpleName();

    private ArrayList<Filme> filmes;

    private AsyncTaskDelegate delegate = null;
    private Context context;
    private ArrayList<Genero> generos;

    public FilmesService(AsyncTaskDelegate responder, Context context) {
        this.delegate = responder;
        this.context = context;
    }

    public void buscarFilmes(String page, String dataInicio, String dataFim, String filtroPesquisa) {

        //Verifica se a conexao com a internet
        if (Util.checkConnection(context)) {

            final String URL_BASE = "https://api.themoviedb.org/3/";
            final String TIPO_SERVICO = "discover";
            final String TIPO_ESPETACULO = "movie";
            final String DATA_INICIO_PARAM = "primary_release_date.gte";
            final String DATA_INICIO_VALUE = dataInicio;
            final String DATA_FINAL_PARAM = "primary_release_date.lte";
            final String DATA_FINAL_VALUE = dataFim;
            final String SORT_BY_PARAM = "sort_by";
            final String SORT_BY_VALUE = filtroPesquisa;
            final String PAGE_PARAM = "page";
            final String API_KEY = "api_key";

            Uri builtUri = Uri.parse(URL_BASE).buildUpon()
                    .appendPath(TIPO_SERVICO)
                    .appendEncodedPath(TIPO_ESPETACULO)
                    .appendQueryParameter(DATA_INICIO_PARAM, DATA_INICIO_VALUE)
                    .appendQueryParameter(DATA_FINAL_PARAM, DATA_FINAL_VALUE)
                    .appendQueryParameter(DATA_FINAL_PARAM, DATA_FINAL_VALUE)
                    .appendQueryParameter(PAGE_PARAM, page)
                    .appendQueryParameter(SORT_BY_PARAM, SORT_BY_VALUE)
                    .appendQueryParameter(API_KEY, BuildConfig.THE_MOVIE_DB_API_KEY)
                    .build();

            URL url = null;
            try {
                url = new URL(builtUri.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            JsonObjectRequest request = new JsonObjectRequest(com.android.volley.Request.Method.GET, url.toString(),
                    new com.android.volley.Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject filmesJson) {

                            filmes = new ArrayList<>();

                            final String IMAGE_PATH = "http://image.tmdb.org/t/p/w185";
                            final String RESULTS = "results";
                            final String TITLE = "title";
                            final String POSTER_PATH = "poster_path";
                            final String VOTE_AVAREGE = "vote_average";
                            final String OVERVIEW = "overview";
                            final String RELEASE_DATE = "release_date";
                            final String VOTE_COUNT = "vote_count";
                            final String ID = "id";

                            try {

                                JSONArray filmesArray = filmesJson.getJSONArray(RESULTS);

                                if (filmesArray.length() > 0) {
                                    for (int i = 0; i < filmesArray.length(); i++) {
                                        JSONObject f = filmesArray.getJSONObject(i);
                                        Filme filme = new Filme();
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
                                        filmes.add(filme);
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (delegate != null) {
                                delegate.processFinish(filmes);
                            }

                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(LOG_TAG, error.getMessage());
                        }
                    });

            RequestQueue queue = VolleySingleton.getInstance(context).getRequestQueue();
            queue.add(request);
        }
    }

    public void buscarDetalhesFilmes(String idFilme) {

        //Verifica se a conexao com a internet
        if (Util.checkConnection(context)) {

            final String URL_BASE = "https://api.themoviedb.org/3/";
            final String TIPO_SERVICO = "movie";
            final String MOVIE_ID = idFilme;
            final String API_KEY = "api_key";

            Uri builtUri = Uri.parse(URL_BASE).buildUpon()
                    .appendPath(TIPO_SERVICO)
                    .appendPath(MOVIE_ID)
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
                        public void onResponse(JSONObject f) {

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

                                Filme filme = new Filme();

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

                                if (delegate != null) {
                                    delegate.processFinish(filme);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(LOG_TAG, error.getMessage());
                        }
                    });

            RequestQueue queue = VolleySingleton.getInstance(context).getRequestQueue();
            queue.add(request);
        }
    }

    public void buscarGeneros() {

        //Verifica se a conexao com a internet
        if (Util.checkConnection(context)) {

            final String URL_BASE = "https://api.themoviedb.org/3/";
            final String TIPO_SERVICO = "genre";
            final String TIPO_ESPETACULO = "movie";
            final String LIST = "list";
            final String API_KEY = "api_key";
            final String LANGUAGE_PARAM = "language";
            final String LANGUAGE_VALUE = "pt-Br";

            Uri builtUri = Uri.parse(URL_BASE).buildUpon()
                    .appendPath(TIPO_SERVICO)
                    .appendEncodedPath(TIPO_ESPETACULO)
                    .appendEncodedPath(LIST)
                    .appendQueryParameter(API_KEY, BuildConfig.THE_MOVIE_DB_API_KEY)
                    .appendQueryParameter(LANGUAGE_PARAM, LANGUAGE_VALUE)
                    .build();

            URL url = null;
            try {
                url = new URL(builtUri.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            JsonObjectRequest request = new JsonObjectRequest(com.android.volley.Request.Method.GET, url.toString(),
                    new com.android.volley.Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject generoJson) {

                            generos = new ArrayList<>();

                            final String GENRES = "genres";
                            final String ID = "id";
                            final String NAME = "name";

                            try {

                                JSONArray generosArray = generoJson.getJSONArray(GENRES);

                                if (generosArray.length() > 0) {
                                    for (int i = 0; i < generosArray.length(); i++) {

                                        JSONObject f = generosArray.getJSONObject(i);

                                        Genero genero = new Genero();
                                        genero.setId(f.getString(ID));
                                        genero.setName(f.getString(NAME));

                                        generos.add(genero);
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (delegate != null) {
                                delegate.processFinish(generos);
                            }

                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(LOG_TAG, error.getMessage());
                        }
                    });

            RequestQueue queue = VolleySingleton.getInstance(context).getRequestQueue();
            queue.add(request);

        }
    }

    public void buscarVideosFilme(String idFilme) {

        //Verifica se a conexao com a internet
        if (Util.checkConnection(context)) {

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
                        public void onResponse(JSONObject f) {

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

                                JSONArray videosArray = f.getJSONArray(RESULTS);

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

                                if (delegate != null) {
                                    delegate.processFinish(videos);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(LOG_TAG, error.getMessage());
                        }
                    });

            RequestQueue queue = VolleySingleton.getInstance(context).getRequestQueue();
            queue.add(request);
        }
    }
}
