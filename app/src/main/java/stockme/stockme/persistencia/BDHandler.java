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
import stockme.stockme.logica.ListaArticulo;
import stockme.stockme.logica.Stock;
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
        db.execSQL("INSERT INTO `ARTICULO` VALUES(2, 'Pizza', 'Hacendado', 'Mercadona', 2.60);");
        db.execSQL("INSERT INTO `ARTICULO` VALUES(3, 'Ketchup', 'Heinz', 'Mercadona', 0.60);");

        db.execSQL("INSERT INTO `SUPERMERCADO` VALUES('Día');");
        db.execSQL("INSERT INTO `SUPERMERCADO` VALUES('Mercadona');");
        db.execSQL("INSERT INTO `SUPERMERCADO` VALUES('Eroski');");
        db.execSQL("INSERT INTO `SUPERMERCADO` VALUES('Simply');");
        db.execSQL("INSERT INTO `SUPERMERCADO` VALUES('Carrefour');");

        db.execSQL("INSERT INTO 'LISTA' VALUES('LISTACHUNGA','HOY','MAÑANA')");
        db.execSQL("INSERT INTO 'LISTA' VALUES('LISTACHU','AYER','TOMORROW')");

        db.execSQL("INSERT INTO 'LISTA_ARTICULO' VALUES(1,'LISTACHUNGA', 6)");
        db.execSQL("INSERT INTO 'LISTA_ARTICULO' VALUES(2,'LISTACHUNGA', 1)");

        db.execSQL("INSERT INTO 'STOCK' VALUES (1, 2, 4)");
        db.execSQL("INSERT INTO 'STOCK' VALUES (2, 3, 5)");
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

    //OPERACIONES
    public int numArticulosEnLista(String nombre){
        String query = "SELECT COUNT(*) FROM LISTA_ARTICULO WHERE " + ListaArticulo.NOMBRE + " = ?";
        SQLiteDatabase lectura = this.obtenerManejadorLectura();
        Cursor cursor = lectura.rawQuery(query, new String[]{nombre});
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    //LISTAS
    public List<Lista> obtenerListas(){
        //1. crear lista a devolver
        ArrayList<Lista> listas = new ArrayList();
        String query = "SELECT * FROM LISTA";

        // 2. obtener un manejador de bd
        SQLiteDatabase db = this.obtenerManejadorLectura();
        Cursor cursor = db.rawQuery(query, null);

        // 3. recorrer los elementos y añadirlos a la lista a devolver
        Lista lista = null;
        if (cursor.moveToFirst()) {
            do {
                lista = new Lista();
                lista.setNombre(cursor.getString(cursor.getColumnIndex(Lista.NOMBRE)));
                lista.setFechaCreacion(cursor.getString(cursor.getColumnIndex(Lista.FECHA_CREACION)));
                lista.setFechaModificacion(cursor.getString(cursor.getColumnIndex(Lista.FECHA_MODIFICACION)));
                listas.add(lista);
            } while (cursor.moveToNext());
        }

        Log.d("obtenerListas()", "size: " + listas.size());

        //4. cerrar la referencia a la bd y devolver la lista poblada
        db.close();
        return listas;
    }

    public boolean insertarLista(Lista lista){
        boolean ok = false;
        //hay que hacer una preconsulta ya que no hemos podido controlar la excepción de UNIQUE
        if(!estaLista(lista.getNombre())) {//no existe en la bd
            SQLiteDatabase db = this.obtenerManejadorEscritura();
            ContentValues values = new ContentValues();
            values.put(Lista.NOMBRE, lista.getNombre());
            values.put(Lista.FECHA_CREACION, lista.getFechaCreacion());
            values.put(Lista.FECHA_MODIFICACION, lista.getFechaModificacion());

            db.insert("LISTA", null, values);
            db.close();
            ok = true;
        }
        return ok;
    }

    public Lista obtenerLista(String nombre){
        Lista lista = null;
        String query = "SELECT * FROM LISTA WHERE " + Lista.NOMBRE + " = ?";
        SQLiteDatabase lectura = this.obtenerManejadorLectura();
        Cursor cursor = lectura.rawQuery(query, new String[]{nombre});
        if(cursor.moveToFirst()){
            lista = new Lista();
            lista.setNombre(cursor.getString(cursor.getColumnIndex(Lista.NOMBRE)));
            lista.setFechaCreacion(cursor.getString(cursor.getColumnIndex(Lista.FECHA_CREACION)));
            lista.setFechaModificacion(cursor.getString(cursor.getColumnIndex(Lista.FECHA_MODIFICACION)));
        }
        return lista;
    }

    public boolean estaLista(String nombre){
        String query = "SELECT * FROM LISTA WHERE " + Lista.NOMBRE + " = ?";
        SQLiteDatabase lectura = this.obtenerManejadorLectura();
        Cursor cursor = lectura.rawQuery(query, new String[]{nombre});
        boolean ok = cursor.moveToFirst();
        lectura.close();
        return ok;
    }

    //cambia los valores de la lista pasada por parámetro. El nombre debe ser el de la lista que se quiere modificar
    public boolean modificarLista(Lista lista){
        int filaActu = 0;
        if(estaLista(lista.getNombre())){
            //obtenemos acceso de escritura a la bd
            SQLiteDatabase db = this.obtenerManejadorEscritura();
            //creamos los valores a actualizar
            ContentValues valores = new ContentValues();
            valores.put(Lista.NOMBRE, lista.getNombre());
            valores.put(Lista.FECHA_CREACION, lista.getFechaCreacion());
            valores.put(Lista.FECHA_MODIFICACION, lista.getFechaModificacion());
            //actualizamos y recogemos el num de filas actualizadas (debería ser 1 claro)
            filaActu = db.update("LISTA", valores, Lista.NOMBRE + "=?", new String[]{lista.getNombre()});
        }
        return filaActu > 0;
    }

    public boolean eliminarLista(Lista lista){
        SQLiteDatabase db = this.obtenerManejadorEscritura();
        int filaBorrada = db.delete("LISTA", Lista.NOMBRE + "=?", new String[]{lista.getNombre()});
        db.close();
        return filaBorrada > 0;
    }

    //ARTICULOS
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

    public Articulo obtenerArticulo(int id){
        Articulo articulo = null;
        String query = "SELECT * FROM ARTICULO WHERE id = '"+id+"'";
        SQLiteDatabase lectura = this.obtenerManejadorLectura();
        Cursor cursor = lectura.rawQuery(query, null);
        if(cursor.moveToFirst()){
            articulo = new Articulo();
            articulo.setId(cursor.getInt(cursor.getColumnIndex(Articulo.ID)));
            articulo.setNombre(cursor.getString(cursor.getColumnIndex(Articulo.NOMBRE)));
            articulo.setMarca(cursor.getString(cursor.getColumnIndex(Articulo.MARCA)));
            articulo.setSupermercado(cursor.getString(cursor.getColumnIndex(Articulo.SUPERMERCADO)));
            articulo.setPrecio(cursor.getFloat(cursor.getColumnIndex(Articulo.PRECIO)));
        }
        lectura.close();
        return articulo;
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

    //LISTAARTICULO
    public void insertarArticuloEnLista(int articulo, Lista lista, int cantidad){ //Esto hay que mirarlo....

        SQLiteDatabase db = this.obtenerManejadorEscritura();
        String query = "INSERT INTO LISTA_ARTICULO VALUES("+articulo+", '"+lista.getNombre()+"', "+cantidad+")";

        db.execSQL(query);
        db.close();
    }

    public ArrayList<Articulo> obtenerArticulosEnLista(String nombre){
        ArrayList<Articulo> articulos = null;
        String query = "SELECT * FROM LISTA_ARTICULOS WHERE nombre = '"+nombre+"'";
        SQLiteDatabase lectura = this.obtenerManejadorLectura();
        Cursor cursor = lectura.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                articulos.add(obtenerArticulo(cursor.getInt(cursor.getColumnIndex(ListaArticulo.ARTICULO))));
            } while (cursor.moveToNext());
        }

        lectura.close();
        return articulos;
    }

    //STOCK
    public List<Stock> obtenerStock(){
        ArrayList<Stock> lista = new ArrayList();
        String query = "SELECT * FROM STOCK";

        // 2. get reference to writable DB
        SQLiteDatabase db = this.obtenerManejadorLectura();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Stock book = null;
        if (cursor.moveToFirst()) {
            do {
                book = new Stock();
                book.setArticulo(cursor.getInt(cursor.getColumnIndex(Stock.ARTICULO)));
                book.setCanitdad(cursor.getInt(cursor.getColumnIndex(Stock.CANTIDAD)));
                book.setMinimo(cursor.getInt(cursor.getColumnIndex(Stock.MINIMO)));
                lista.add(book);
            } while (cursor.moveToNext());
        }

        Log.d("getAllBooks()", lista.toString());
        db.close();
        return lista;
    }

}
