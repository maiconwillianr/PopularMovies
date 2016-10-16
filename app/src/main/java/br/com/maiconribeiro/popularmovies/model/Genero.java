package br.com.maiconribeiro.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by maiconwillianribeiro on 09/10/16.
 */
public class Genero implements Parcelable {

    private String idGenero;
    private String id;
    private String name;

    public Genero() {
    }

    public String getIdGenero() {
        return idGenero;
    }

    public void setIdGenero(String idGenero) {
        this.idGenero = idGenero;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static final String PARCELABLE_KEY = "genero";

    public static final Parcelable.Creator<Genero> CREATOR
            = new Parcelable.Creator<Genero>() {
        public Genero createFromParcel(Parcel in) {
            return new Genero(in);
        }

        public Genero[] newArray(int size) {
            return new Genero[size];
        }
    };

    private Genero(Parcel in) {
        idGenero = in.readString();
        id = in.readString();
        name = in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idGenero);
        dest.writeString(id);
        dest.writeString(name);
    }
}
