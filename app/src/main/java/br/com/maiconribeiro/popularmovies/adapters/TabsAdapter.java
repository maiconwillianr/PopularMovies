package br.com.maiconribeiro.popularmovies.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import br.com.maiconribeiro.popularmovies.FilmesFragment;
import br.com.maiconribeiro.popularmovies.model.Genero;

/**
 * Created by maiconwillianribeiro on 15/10/16.
 */
public class TabsAdapter extends FragmentStatePagerAdapter {

    private int mNumOfTabs;

    private List<Genero> generos;

    public TabsAdapter(FragmentManager fm, int NumOfTabs, List<Genero> generos) {
        super(fm);
        this.generos = generos;
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        Genero genero = generos.get(position);

        FilmesFragment tab1 = FilmesFragment.newInstance(genero.getId());
        return tab1;
        /*
        switch (position) {
            case 0:
                FilmesFragment tab1 = new FilmesFragment();
                return tab1;
            case 1:
                FilmesFragment tab2 = new FilmesFragment();
                return tab2;
            case 2:
                FilmesFragment tab3 = new FilmesFragment();
                return tab3;
            default:
                return null;
        }
        */
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
