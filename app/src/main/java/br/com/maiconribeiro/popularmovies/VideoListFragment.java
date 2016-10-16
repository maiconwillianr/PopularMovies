package br.com.maiconribeiro.popularmovies;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import br.com.maiconribeiro.popularmovies.adapters.VideoListAdapter;
import br.com.maiconribeiro.popularmovies.model.Video;

/***********************************************************************************
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2015 Scott Cooper
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 ***********************************************************************************/

public class VideoListFragment extends ListFragment {

    private ArrayList<Video> videos;
    private VideoListAdapter videoListAdapter;

    /**
     * Empty constructor
     */
    public VideoListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        this.videos = args.getParcelableArrayList("videos");
        videoListAdapter = new VideoListAdapter(getActivity(), videos);
        setListAdapter(videoListAdapter);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        final Context context = getActivity();
        final Video video = videos.get(position);


        //Opens in the the custom Lightbox activity
        final Intent lightboxIntent = new Intent(context, CustomLightboxActivity.class);
        lightboxIntent.putExtra(CustomLightboxActivity.KEY_VIDEO_ID, video.getKey());
        startActivity(lightboxIntent);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        videoListAdapter.releaseLoaders();
    }
}