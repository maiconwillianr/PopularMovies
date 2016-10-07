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
import br.com.maiconribeiro.popularmovies.model.Video;

/**
 * Created by maiconribeiro on 06/10/16.
 */
public class BuscarVideosFilmeService {

    private final String LOG_TAG = BuscarVideosFilmeService.class.getSimpleName();

    private AsyncTaskDelegate delegate = null;
    private Context context;

    public BuscarVideosFilmeService(AsyncTaskDelegate responder, Context context) {
        this.delegate = responder;
        this.context = context;
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

