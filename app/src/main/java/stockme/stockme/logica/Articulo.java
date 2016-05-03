package stockme.stockme.logica;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by JuanMiguel on 03/03/2016.
 */
public class Articulo implements Parcelable{
    private Integer id;
    private String nombre;
    private String marca;
    private String tipo;

    public static String ID = "Id";
    public static String NOMBRE = "Nombre";
    public static String MARCA = "Marca";
    public static String TIPO = "Tipo";

    public Articulo(){
        this.nombre = "";
        this.marca = "";
    }

    public Articulo(Integer id, String nombre, String marca, String tipo) {
        this.id = id;
        this.nombre = nombre;
        this.marca = marca;
        this.tipo = tipo;
    }

    protected Articulo(Parcel in) {
        nombre = in.readString();
        marca = in.readString();
        tipo = in.readString();
    }

    public static final Creator<Articulo> CREATOR = new Creator<Articulo>() {
        @Override
        public Articulo createFromParcel(Parcel in) {
            return new Articulo(in);
        }

        @Override
        public Articulo[] newArray(int size) {
            return new Articulo[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombre);
        dest.writeString(marca);
        dest.writeString(tipo);
    }
}
