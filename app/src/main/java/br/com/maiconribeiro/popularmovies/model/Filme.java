package br.com.maiconribeiro.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maiconwillianribeiro on 15/09/16.
 */
public class Filme implements Parcelable {

    private String idFilme;
    private String titulo;
    private String pathImagemPoster;
    private String sinopse;
    private String dataLancamento;
    private String notaMedia;
    private String numeroVotos;
    private String homepage;
    private String duracao;
    private List<Video> videos;


    public Filme() {
    }

    public Filme(String idFilme, String titulo, String pathImagemPoster,
                 String sinopse, String dataLancamento, String notaMedia,
                 String numeroVotos, String homepage, String duracao,
                 List<Video> videos) {
        this.idFilme = idFilme;
        this.titulo = titulo;
        this.pathImagemPoster = pathImagemPoster;
        this.sinopse = sinopse;
        this.dataLancamento = dataLancamento;
        this.notaMedia = notaMedia;
        this.numeroVotos = numeroVotos;
        this.homepage = homepage;
        this.duracao = duracao;
        this.videos = videos;
    }

    public String getIdFilme() {
        return idFilme;
    }

    public void setIdFilme(String idFilme) {
        this.idFilme = idFilme;
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

    public String getNumeroVotos() {
        return numeroVotos;
    }

    public void setNumeroVotos(String numeroVotos) {
        this.numeroVotos = numeroVotos;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getDuracao() {
        return duracao;
    }

    public void setDuracao(String duracao) {
        this.duracao = duracao;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    public static final String PARCELABLE_KEY = "filme";

    public static final Parcelable.Creator<Filme> CREATOR
            = new Parcelable.Creator<Filme>() {
        public Filme createFromParcel(Parcel in) {
            return new Filme(in);
        }

        public Filme[] newArray(int size) {
            return new Filme[size];
        }
    };

    private Filme(Parcel in) {
        idFilme = in.readString();
        titulo = in.readString();
        pathImagemPoster = in.readString();
        sinopse = in.readString();
        dataLancamento = in.readString();
        notaMedia = in.readString();
        numeroVotos = in.readString();
        homepage = in.readString();
        duracao = in.readString();
        videos = new ArrayList<>();
        in.readTypedList(videos, Video.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idFilme);
        dest.writeString(titulo);
        dest.writeString(pathImagemPoster);
        dest.writeString(sinopse);
        dest.writeString(dataLancamento);
        dest.writeString(notaMedia);
        dest.writeString(numeroVotos);
        dest.writeString(homepage);
        dest.writeString(duracao);
        dest.writeList(videos);
    }
}
