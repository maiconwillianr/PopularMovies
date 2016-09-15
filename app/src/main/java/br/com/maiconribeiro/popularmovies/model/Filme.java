package br.com.maiconribeiro.popularmovies.model;

import java.io.Serializable;

/**
 * Created by maiconwillianribeiro on 15/09/16.
 */
public class Filme implements Serializable {

    private String titulo;
    private String pathImagemPoster;

    public Filme(String titulo, String pathImagemPoster) {
        this.titulo = titulo;
        this.pathImagemPoster = pathImagemPoster;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getPathImagemPoster() {
        return pathImagemPoster;
    }

    public void setPathImagemPoster(String pathImagemPoster) {
        this.pathImagemPoster = pathImagemPoster;
    }
}
