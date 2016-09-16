package br.com.maiconribeiro.popularmovies.model;

import java.io.Serializable;

/**
 * Created by maiconwillianribeiro on 15/09/16.
 */
public class Filme implements Serializable {

    private String titulo;
    private String pathImagemPoster;
    private String sinopse;
    private String dataLancamento;
    private String notaMedia;

    public Filme() {
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

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    public String getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(String dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public String getNotaMedia() {
        return notaMedia;
    }

    public void setNotaMedia(String notaMedia) {
        this.notaMedia = notaMedia;
    }
}
