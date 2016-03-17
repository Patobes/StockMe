package stockme.stockme.persistencia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import stockme.stockme.logica.Lista;
import stockme.stockme.logica.Articulo;
import stockme.stockme.util.Util;

/**
 * Created by JuanMiguel on 03/03/2016.
 */
public class BDHandler  extends SQLiteOpenHelper {
    private static int bdVersion = Util.getBD_VERSION();

    public BDHandler(Context context) {
        super(context, "Productos", null, bdVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        insertarIniciales(db);
    }

    private void insertarIniciales(SQLiteDatabase db) {
        //tabla de articulo
        String articulo = "CREATE TABLE `ARTICULO` (" +
            "`Id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "`Nombre` TEXT NOT NULL," +
            "`Marca` TEXT," +
            "`Supermercado` TEXT NOT NULL," +
            "`Precio` REAL," +
            "FOREIGN KEY(`Supermercado`) REFERENCES `SUPERMERCADO`(`Nombre`)" +
        ")";
        db.execSQL("DROP TABLE IF EXISTS 'ARTICULO';");
        db.execSQL(articulo);

        //tabla de supermercado
        String supermercado = "CREATE TABLE `SUPERMERCADO` (" +
            "`Nombre` TEXT NOT NULL," +
            "PRIMARY KEY(Nombre)" +
        ")";
        db.execSQL("DROP TABLE IF EXISTS 'SUPERMERCADO';");
        db.execSQL(supermercado);

        //tabla de stock
        String stock = "CREATE TABLE `STOCK` (" +
            "`Articulo` INTEGER NOT NULL," +
            "`Minimo` INTEGER NOT NULL DEFAULT 0," +
            "`Cantidad` INTEGER NOT NULL DEFAULT 0," +
            "PRIMARY KEY(Articulo)," +
            "FOREIGN KEY(`Articulo`) REFERENCES ARTICULO(Id)" +
        ")";
        db.execSQL("DROP TABLE IF EXISTS 'STOCK';");
        db.execSQL(stock);

        //tabla lista
        String lista = "CREATE TABLE `LISTA` (" +
            "`Nombre` TEXT NOT NULL," +
            "`FechaCreacion` TEXT NOT NULL," +
            "`FechaModificacion` TEXT NOT NULL," +
            "PRIMARY KEY(Nombre)" +
        ")";
        db.execSQL("DROP TABLE IF EXISTS 'LISTA';");
        db.execSQL(lista);

        //Lista_Articulo
        String listaArticulo = "CREATE TABLE `LISTA_ARTICULO` (" +
            "`Articulo` INTEGER NOT NULL," +
            "`Nombre` TEXT NOT NULL," +
            "`Cantidad` INTEGER NOT NULL DEFAULT 0," +
            "PRIMARY KEY(Articulo,Nombre)," +
            "FOREIGN KEY(`Articulo`) REFERENCES ARTICULO(Id)," +
            "FOREIGN KEY(`Nombre`) REFERENCES LISTA(Nombre)" +
        ")";
        db.execSQL("DROP TABLE IF EXISTS 'LISTA_ARTICULO';");
        db.execSQL(listaArticulo);

        //ahora se añaden los elementos iniciales
        db.execSQL("INSERT INTO `ARTICULO` VALUES(1, 'Leche', 'Hacendado', 'Mercadona', 0.60);");

        db.execSQL("INSERT INTO `SUPERMERCADO` VALUES('Día');");
        db.execSQL("INSERT INTO `SUPERMERCADO` VALUES('Mercadona');");
        db.execSQL("INSERT INTO `SUPERMERCADO` VALUES('Eroski');");
        db.execSQL("INSERT INTO `SUPERMERCADO` VALUES('Simply');");
        db.execSQL("INSERT INTO `SUPERMERCADO` VALUES('Carrefour');");

        db.execSQL("INSERT INTO 'LISTA' VALUES('LISTACHUNGA','HOY','MAÑANA')");
        db.execSQL("INSERT INTO 'LISTA' VALUES('LISTACHU','AYER','TOMORROW')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        insertarIniciales(db);
    }

    public SQLiteDatabase obtenerManejadorLectura()
    {
        return this.getReadableDatabase();
    }

    public SQLiteDatabase obtenerManejadorEscritura()
    {
        return this.getWritableDatabase();
    }

    public void abrir()
    {
        this.getWritableDatabase();
    }

    public void cerrar()
    {
        this.close();
    }

    public List<Lista> obtenerListas(){
        ArrayList<Lista> lista = new ArrayList();
        String query = "SELECT * FROM LISTA";

        // 2. get reference to writable DB
        SQLiteDatabase db = this.obtenerManejadorLectura();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Lista book = null;
        if (cursor.moveToFirst()) {
            do {
                book = new Lista();
                book.setNombre(cursor.getString(cursor.getColumnIndex(Lista.NOMBRE)));
                book.setFechaCreacion(cursor.getString(cursor.getColumnIndex(Lista.FECHA_CREACION)));
                book.setFechaModificacion(cursor.getString(cursor.getColumnIndex(Lista.FECHA_MODIFICACION)));
                lista.add(book);
            } while (cursor.moveToNext());
        }

        Log.d("getAllBooks()", lista.toString());
        db.close();
        return lista;
    }

    public boolean insertarLista(Lista lista){
        boolean ok = false;
        SQLiteDatabase db = null;
        try {
            db = this.obtenerManejadorEscritura();

            ContentValues values = new ContentValues();
            values.put(Lista.NOMBRE, lista.getNombre());
            values.put(Lista.FECHA_CREACION, lista.getFechaCreacion());
            values.put(Lista.FECHA_MODIFICACION, lista.getFechaModificacion());

            db.insert("LISTA", null, values);

//            db.close();
            ok = true;
        } catch (Exception e) {
            ok = false;
        } finally{
            if(db != null)
                db.close();
        }
        return ok;
    }

    public List<Articulo> obtenerArticulos(){
        ArrayList<Articulo> lista = new ArrayList();
        String query = "SELECT * FROM ARTICULO";

        // 2. get reference to writable DB
        SQLiteDatabase db = this.obtenerManejadorLectura();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Articulo book = null;
        if (cursor.moveToFirst()) {
            do {
                book = new Articulo();
                book.setId(cursor.getInt(cursor.getColumnIndex(Articulo.ID)));
                book.setNombre(cursor.getString(cursor.getColumnIndex(Articulo.NOMBRE)));
                book.setMarca(cursor.getString(cursor.getColumnIndex(Articulo.MARCA)));
                book.setSupermercado(cursor.getString(cursor.getColumnIndex(Articulo.SUPERMERCADO)));
                        book.setPrecio(cursor.getFloat(cursor.getColumnIndex(Articulo.PRECIO)));
                lista.add(book);
            } while (cursor.moveToNext());
        }

        Log.d("getAllBooks()", lista.toString());
        db.close();
        return lista;
    }

    public boolean insertarArticulo(Articulo articulo){
        boolean ok = false;
        SQLiteDatabase db = this.obtenerManejadorEscritura();
        ContentValues values = new ContentValues();

        values.put(Articulo.ID, articulo.getId());
        values.put(Articulo.NOMBRE, articulo.getNombre());
        values.put(Articulo.MARCA, articulo.getMarca());
        values.put(Articulo.SUPERMERCADO, articulo.getSupermercado());
        values.put(Articulo.PRECIO, articulo.getPrecio());

        long indent = -1;
        SQLiteDatabase dbRead = this.obtenerManejadorLectura();
        String query = "insert into ARTICULO where id = " + articulo.getId();
        Cursor cursor = db.rawQuery(query, null);
        if(!cursor.moveToFirst()){//no existe en la bd
            indent = db.insert("ARTICULO",
                    null,
                    values);
        }
        dbRead.close();

        if(indent != -1){
            articulo.setId((int) indent);
            ok = true;
        }

        db.close();
        return ok;
        /*
        * PreparedStatement prep = conn.prepareStatement("insert into participants values ($next_id,?,?);");
            prep.setString(2, "bla");
            prep.setString(3, "blub");
        * */
    }

    public void eliminarLista(Lista lista){

        SQLiteDatabase db = this.obtenerManejadorEscritura();
        db.delete("LISTA", Lista.NOMBRE + "=?", new String[]{lista.getNombre()});

        db.close();
    }
}
