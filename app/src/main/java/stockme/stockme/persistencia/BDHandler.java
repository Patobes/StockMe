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
import stockme.stockme.logica.Supermercado;
import stockme.stockme.util.Util;

public class BDHandler  extends SQLiteOpenHelper {
    //private static int bdVersion = Util.getBD_VERSION();

    public BDHandler(Context context) {
        super(context, "Productos", null, Util.getBD_VERSION());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        insertarIniciales(db);
    }

    //TODO: añadir valores por defecto
    private void insertarIniciales(SQLiteDatabase db) {
        /* TABLA ARTICULO
        ID - INT - PK
        NOMBRE - STRING
        MARCA - STRING
        TIPO - STRING
        SUPERMERCADO - STRING - FK
        PRECIO - FLOAT
         */
        String articulo = "CREATE TABLE `ARTICULO` (" +
            "`Id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "`Nombre` TEXT NOT NULL," +
            "`Marca` TEXT," +
            "`Tipo` TEXT," +
            "`Supermercado` TEXT NOT NULL," +
            "`Precio` REAL," +
            "FOREIGN KEY(`Supermercado`) REFERENCES `SUPERMERCADO`(`Nombre`)" +
        ")";
        db.execSQL("DROP TABLE IF EXISTS 'ARTICULO';");
        db.execSQL(articulo);

        /* TABLA SUPERMERCADO
        NOMBRE - STRING - PK
         */
        String supermercado = "CREATE TABLE `SUPERMERCADO` (" +
            "`Nombre` TEXT NOT NULL," +
            "PRIMARY KEY(Nombre)" +
        ")";
        db.execSQL("DROP TABLE IF EXISTS 'SUPERMERCADO';");
        db.execSQL(supermercado);

        /* TABLA STOCK
        ARTICULO - INT - PK - FK
        MINIMO - INT
        CANTIDAD - INT
         */
        String stock = "CREATE TABLE `STOCK` (" +
            "`Articulo` INTEGER NOT NULL," +
            "`Minimo` INTEGER NOT NULL DEFAULT 0," +
            "`Cantidad` INTEGER NOT NULL DEFAULT 0," +
            "PRIMARY KEY(Articulo)," +
            "FOREIGN KEY(`Articulo`) REFERENCES ARTICULO(Id)" +
        ")";
        db.execSQL("DROP TABLE IF EXISTS 'STOCK';");
        db.execSQL(stock);

        /* TABLA LISTA
        NOMBRE - STRING - PK
        FECHACREACION - STRING
        FECHAMODIFICACION - STRING
        SUPERMERCADO - STRING - FK
         */
        String lista = "CREATE TABLE `LISTA` (" +
            "`Nombre` TEXT NOT NULL," +
            "`FechaCreacion` TEXT NOT NULL," +
            "`FechaModificacion` TEXT NOT NULL," +
            "`Supermercado` TEXT NOT NULL," +
            "PRIMARY KEY(Nombre)" +
            "FOREIGN KEY(`Supermercado`) REFERENCES ARTICULO(Nombre)" +
        ")";
        db.execSQL("DROP TABLE IF EXISTS 'LISTA';");
        db.execSQL(lista);

        /* TABLA LISTA_ARTICULO
        ARTICULO - INT - PK - FK
        NOMBRE - STRING - PK - FK
        CANTIDAD - INT
         */
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

        //ELEMENTOS INICIALES
        db.execSQL("INSERT INTO `ARTICULO` VALUES(1, 'Leche', 'Hacendado', 'Lácteos','Mercadona', 0.60);");
        db.execSQL("INSERT INTO `ARTICULO` VALUES(2, 'Pizza', 'Hacendado', 'Congelados','Mercadona', 2.60);");
        db.execSQL("INSERT INTO `ARTICULO` VALUES(3, 'Ketchup', 'Heinz', 'Salsas','Mercadona', 0.60);");
        db.execSQL("INSERT INTO `ARTICULO` VALUES(4, 'Mostaza', 'Heinz', 'Salsas','Mercadona', 0.60);");

        db.execSQL("INSERT INTO `SUPERMERCADO` VALUES('Cualquiera');");
        db.execSQL("INSERT INTO `SUPERMERCADO` VALUES('Día');");
        db.execSQL("INSERT INTO `SUPERMERCADO` VALUES('Mercadona');");
        db.execSQL("INSERT INTO `SUPERMERCADO` VALUES('Eroski');");
        db.execSQL("INSERT INTO `SUPERMERCADO` VALUES('Simply');");
        db.execSQL("INSERT INTO `SUPERMERCADO` VALUES('Carrefour');");
        db.execSQL("INSERT INTO `SUPERMERCADO` VALUES('Supersol');");

        db.execSQL("INSERT INTO 'LISTA' VALUES('Lista prueba','18/04/2016','18/04/2016','Día')");
        db.execSQL("INSERT INTO 'LISTA' VALUES('Lista prueba 2','18/04/2016','18/04/2016','Cualquiera')");

        db.execSQL("INSERT INTO 'LISTA_ARTICULO' VALUES(1,'Lista prueba', 6)");
        db.execSQL("INSERT INTO 'LISTA_ARTICULO' VALUES(2,'Lista prueba', 1)");
        db.execSQL("INSERT INTO 'LISTA_ARTICULO' VALUES(3,'Lista prueba', 3)");
        db.execSQL("INSERT INTO 'LISTA_ARTICULO' VALUES(4,'Lista prueba', 5)");

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



    //LISTAS

    //Obtiene todas las listas de la BD, devuelve un List<Lista>
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
                lista.setSupermercado(cursor.getString(cursor.getColumnIndex(Lista.SUPERMERCADO)));
                listas.add(lista);
            } while (cursor.moveToNext());
        }

        Log.d("obtenerListas()", "size: " + listas.size());

        //4. cerrar la referencia a la bd y devolver la lista poblada
        db.close();
        return listas;
    }

    //Inserta una lista en la BD, devuelve el éxito o el fracaso de la operación
    public boolean insertarLista(Lista lista){
        boolean ok = false;

        if(!estaLista(lista.getNombre())) {

            SQLiteDatabase db = this.obtenerManejadorEscritura();
            ContentValues values = new ContentValues();

            values.put(Lista.NOMBRE, lista.getNombre());
            values.put(Lista.FECHA_CREACION, lista.getFechaCreacion());
            values.put(Lista.FECHA_MODIFICACION, lista.getFechaModificacion());
            values.put(Lista.SUPERMERCADO, lista.getSupermercado());

            db.insert("LISTA", null, values);
            db.close();
            ok = true;
        }
        return ok;
    }

    //Obtiene una lista con el nombre indicado
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
            lista.setSupermercado(cursor.getString(cursor.getColumnIndex(Lista.SUPERMERCADO)));
        }
        lectura.close();
        return lista;
    }

    //Comprueba si esta una lista de nombre indicado
    public boolean estaLista(String nombre){
        String query = "SELECT * FROM LISTA WHERE " + Lista.NOMBRE + " = ?";

        SQLiteDatabase lectura = this.obtenerManejadorLectura();
        Cursor cursor = lectura.rawQuery(query, new String[]{nombre});

        boolean ok = cursor.moveToFirst();
        lectura.close();
        return ok;
    }

    //Cambia los valores de la lista indicada, debe existir previamente
    public boolean modificarLista(Lista lista){
        int filaActu = 0;
        if(estaLista(lista.getNombre())){

            SQLiteDatabase db = this.obtenerManejadorEscritura();

            ContentValues valores = new ContentValues();
            valores.put(Lista.NOMBRE, lista.getNombre());
            valores.put(Lista.FECHA_CREACION, lista.getFechaCreacion());
            valores.put(Lista.FECHA_MODIFICACION, lista.getFechaModificacion());
            valores.put(Lista.SUPERMERCADO, lista.getSupermercado());

            filaActu = db.update("LISTA", valores, Lista.NOMBRE + "=?", new String[]{lista.getNombre()});
            db.close();
        }
        return filaActu > 0;
    }

    //Elimina la lista indicada, debe existir previamente
    public boolean eliminarLista(Lista lista){
        int filaBorrada = 0;

        if(estaLista(lista.getNombre())){
            SQLiteDatabase db = this.obtenerManejadorEscritura();
            filaBorrada = db.delete("LISTA", Lista.NOMBRE + "=?", new String[]{lista.getNombre()});
            db.close();
        }

        return filaBorrada > 0;
    }

    //borrar lista y sus artículos
    public boolean eliminarListaCascade(Lista lista){
        boolean ok = false;
        SQLiteDatabase db = this.obtenerManejadorEscritura();
        db.beginTransaction();
        try {
            //borrar la lista
            db.delete("LISTA", Lista.NOMBRE + "=?", new String[]{lista.getNombre()});
            db.delete("LISTA_ARTICULO", ListaArticulo.NOMBRE + " = ?", new String[]{lista.getNombre()});

            ok = true;
            db.setTransactionSuccessful();
        } catch (Exception e){
            ok = false;
        } finally {
                db.endTransaction();
                db.close();
        }
        return ok;
    }

    //ARTICULOS

    //Obtiene todos los articulos de la BD, devuelve un List<Articulo>
    public List<Articulo> obtenerArticulos(){
        ArrayList<Articulo> lista = new ArrayList();
        String query = "SELECT * FROM ARTICULO";

        SQLiteDatabase db = this.obtenerManejadorLectura();
        Cursor cursor = db.rawQuery(query, null);

        Articulo art = null;
        if (cursor.moveToFirst()) {
            do {
                art = new Articulo();
                art.setId(cursor.getInt(cursor.getColumnIndex(Articulo.ID)));
                art.setNombre(cursor.getString(cursor.getColumnIndex(Articulo.NOMBRE)));
                art.setMarca(cursor.getString(cursor.getColumnIndex(Articulo.MARCA)));
                art.setTipo(cursor.getString(cursor.getColumnIndex(Articulo.TIPO)));
                art.setSupermercado(cursor.getString(cursor.getColumnIndex(Articulo.SUPERMERCADO)));
                art.setPrecio(cursor.getFloat(cursor.getColumnIndex(Articulo.PRECIO)));

                lista.add(art);
            } while (cursor.moveToNext());
        }

        Log.d("obtenerArticulos()", lista.toString());
        db.close();
        return lista;
    }

    //Inserta un articulo en la BD, devuelve el id nuevo o null si ya existe
    public Integer insertarArticulo(Articulo articulo){
        Integer id = articulo.getId();

        //si es null significa que no está creado
        if(id == null) {
            SQLiteDatabase db = this.obtenerManejadorEscritura();
            ContentValues values = new ContentValues();

            values.put(Articulo.ID, articulo.getId());
            values.put(Articulo.NOMBRE, articulo.getNombre());
            values.put(Articulo.MARCA, articulo.getMarca());
            values.put(Articulo.TIPO, articulo.getTipo());
            values.put(Articulo.SUPERMERCADO, articulo.getSupermercado());
            values.put(Articulo.PRECIO, articulo.getPrecio());

            long ident = db.insert("ARTICULO", null, values);

            if (ident != -1) {
                id = (int) ident;
                articulo.setId(id);
            }

            db.close();
            return id;
        }else{
            return null;
        }
    }
//    public boolean insertarArticulo(Articulo articulo){
//
//        boolean ok = false;
//
//        if(!estaArticulo(articulo.getId())) {
//
//            SQLiteDatabase db = this.obtenerManejadorEscritura();
//            ContentValues values = new ContentValues();
//
//            values.put(Articulo.ID, articulo.getId());
//            values.put(Articulo.NOMBRE, articulo.getNombre());
//            values.put(Articulo.MARCA, articulo.getMarca());
//            values.put(Articulo.TIPO, articulo.getTipo());
//            values.put(Articulo.SUPERMERCADO, articulo.getSupermercado());
//            values.put(Articulo.PRECIO, articulo.getPrecio());
//
//            db.insert("ARTICULO", null, values);
//            db.close();
//            ok = true;
//        }
//        return ok;
//
//    }

    //Obtiene el articulo con el id indicado
    public Articulo obtenerArticulo(int id){

        Articulo articulo = null;
        String query = "SELECT * FROM ARTICULO WHERE "+Articulo.ID + " = ?";
        SQLiteDatabase lectura = this.obtenerManejadorLectura();

        Cursor cursor = lectura.rawQuery(query, new String[]{Integer.toString(id)});

        if(cursor.moveToFirst()){
            articulo = new Articulo();
            articulo.setId(cursor.getInt(cursor.getColumnIndex(Articulo.ID)));
            articulo.setNombre(cursor.getString(cursor.getColumnIndex(Articulo.NOMBRE)));
            articulo.setMarca(cursor.getString(cursor.getColumnIndex(Articulo.MARCA)));
            articulo.setTipo(cursor.getString(cursor.getColumnIndex(Articulo.TIPO)));
            articulo.setSupermercado(cursor.getString(cursor.getColumnIndex(Articulo.SUPERMERCADO)));
            articulo.setPrecio(cursor.getFloat(cursor.getColumnIndex(Articulo.PRECIO)));
        }
        lectura.close();
        return articulo;
    }

    public Articulo obtenerArticulo(String nombre, String marca, String supermercado){
        Articulo articulo = null;
        String query = "SELECT * FROM ARTICULO WHERE " + Articulo.NOMBRE + " = ? AND " + Articulo.MARCA + " = ? AND " + Articulo.SUPERMERCADO + " = ?";
        SQLiteDatabase lectura = this.obtenerManejadorLectura();

        Cursor cursor = lectura.rawQuery(query, new String[]{nombre, marca, supermercado});

        if(cursor.moveToFirst()){
            articulo = new Articulo();
            articulo.setId(cursor.getInt(cursor.getColumnIndex(Articulo.ID)));
            articulo.setNombre(cursor.getString(cursor.getColumnIndex(Articulo.NOMBRE)));
            articulo.setMarca(cursor.getString(cursor.getColumnIndex(Articulo.MARCA)));
            articulo.setTipo(cursor.getString(cursor.getColumnIndex(Articulo.TIPO)));
            articulo.setSupermercado(cursor.getString(cursor.getColumnIndex(Articulo.SUPERMERCADO)));
            articulo.setPrecio(cursor.getFloat(cursor.getColumnIndex(Articulo.PRECIO)));
        }
        lectura.close();
        return articulo;
    }

    //devuelve null si no encuentra el artículo
    public Articulo obtenerArticulo(String nombre, String marca, String tipo, String supermercado){
        Articulo articulo = null;
        String query = "SELECT * FROM ARTICULO WHERE " + Articulo.NOMBRE + " = ? AND " + Articulo.MARCA + " = ? AND "
                + Articulo.TIPO + " = ? AND " + Articulo.SUPERMERCADO + " = ?";
        SQLiteDatabase lectura = this.obtenerManejadorLectura();

        Cursor cursor = lectura.rawQuery(query, new String[]{nombre, marca, tipo, supermercado});

        if(cursor.moveToFirst()){
            articulo = new Articulo();
            articulo.setId(cursor.getInt(cursor.getColumnIndex(Articulo.ID)));
            articulo.setNombre(cursor.getString(cursor.getColumnIndex(Articulo.NOMBRE)));
            articulo.setMarca(cursor.getString(cursor.getColumnIndex(Articulo.MARCA)));
            articulo.setTipo(cursor.getString(cursor.getColumnIndex(Articulo.TIPO)));
            articulo.setSupermercado(cursor.getString(cursor.getColumnIndex(Articulo.SUPERMERCADO)));
            articulo.setPrecio(cursor.getFloat(cursor.getColumnIndex(Articulo.PRECIO)));
        }
        lectura.close();
        return articulo;
    }

    //Comprueba si esta un articulo de id indicado
    public boolean estaArticulo(int id){
        String query = "SELECT * FROM ARTICULO WHERE " + Articulo.ID + " = ?";

        SQLiteDatabase lectura = this.obtenerManejadorLectura();
        Cursor cursor = lectura.rawQuery(query, new String[]{Integer.toString(id)});

        boolean ok = cursor.moveToFirst();
        lectura.close();

        return ok;
    }

    public boolean estaArticulo(String nombre, String marca, String supermercado){
        String query = "SELECT * FROM ARTICULO WHERE " + Articulo.NOMBRE + " = ? AND " + Articulo.MARCA + " = ? AND " + Articulo.SUPERMERCADO + " = ?";

        SQLiteDatabase lectura = this.obtenerManejadorLectura();
        Cursor cursor = lectura.rawQuery(query, new String[]{nombre, marca, supermercado});

        boolean ok = cursor.moveToFirst();
        lectura.close();

        return ok;
    }

    //Cambia los valores del articulo indicado, debe existir previamente
    public boolean modificarArticulo(Articulo articulo){
        int filaActu = 0;
        if(estaArticulo(articulo.getId())){

            SQLiteDatabase db = this.obtenerManejadorEscritura();

            ContentValues valores = new ContentValues();
            valores.put(Articulo.ID, articulo.getId());
            valores.put(Articulo.NOMBRE, articulo.getNombre());
            valores.put(Articulo.MARCA, articulo.getMarca());
            valores.put(Articulo.TIPO, articulo.getTipo());
            valores.put(Articulo.SUPERMERCADO, articulo.getSupermercado());
            valores.put(Articulo.PRECIO, articulo.getPrecio());

            filaActu = db.update("ARTICULO", valores, Articulo.ID + "=?", new String[]{Integer.toString(articulo.getId())});
            db.close();
        }
        return filaActu > 0;
    }

    //Elimina el articulo indicado, debe existir previamente
    public boolean eliminarArticulo(Articulo articulo){
        int filaBorrada = 0;

        if(estaArticulo(articulo.getId())){
            SQLiteDatabase db = this.obtenerManejadorEscritura();
            filaBorrada = db.delete("ARTICULO", Articulo.ID + "=?", new String[]{Integer.toString(articulo.getId())});
            db.close();
        }

        return filaBorrada > 0;
    }

    //LISTAARTICULO

    //Obtiene una lista de los articulos en una lista
    public List<Articulo> obtenerArticulosEnLista(Lista lista){

        ArrayList<Articulo> articulos = new ArrayList();
        String query = "SELECT * FROM LISTA_ARTICULO WHERE " + ListaArticulo.NOMBRE + " =?";

        SQLiteDatabase db = this.obtenerManejadorLectura();
        Cursor cursor = db.rawQuery(query, new String[]{lista.getNombre()});

        Articulo art = null;
        if (cursor.moveToFirst()) {
            do {
                art = obtenerArticulo(cursor.getInt(cursor.getColumnIndex(ListaArticulo.ARTICULO)));
                articulos.add(art);
            } while (cursor.moveToNext());
        }

        Log.d("obtenerArticulosEnLista", articulos.toString());
        db.close();
        return articulos;
    }

    public ListaArticulo obtenerListaArticulo(int idArticulo, String nombreLista){
        ListaArticulo la = null;

        String query = "SELECT * FROM LISTA_ARTICULO WHERE " + ListaArticulo.ARTICULO + " = ? AND "
                + ListaArticulo.NOMBRE + " = ?";
        SQLiteDatabase db = this.obtenerManejadorLectura();
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(idArticulo), nombreLista});
        if(cursor.moveToFirst()){
            la = new ListaArticulo();
            la.setArticulo(cursor.getInt(cursor.getColumnIndex(ListaArticulo.ARTICULO)));
            la.setNombre(cursor.getString(cursor.getColumnIndex(ListaArticulo.NOMBRE)));
            la.setCantidad(cursor.getInt(cursor.getColumnIndex(ListaArticulo.CANTIDAD)));
        }
        db.close();

        return la;
    }

    //Inserta en la BD la cantidad del articulo en la lista indicados
    public boolean insertarArticuloEnLista(ListaArticulo listaArticulo){

        boolean ok = false;

        if(!estaArticuloEnLista(listaArticulo.getArticulo(), listaArticulo.getNombre())) {

            SQLiteDatabase db = this.obtenerManejadorEscritura();
            ContentValues values = new ContentValues();

            values.put(ListaArticulo.ARTICULO, listaArticulo.getArticulo());
            values.put(ListaArticulo.NOMBRE, listaArticulo.getNombre());
            values.put(ListaArticulo.CANTIDAD, listaArticulo.getCantidad());

            db.insert("LISTA_ARTICULO", null, values);
            db.close();
            ok = true;
        }
        return ok;
    }

    //Dice si un articulo está o no en una lista
    public boolean estaArticuloEnLista(int articulo, String lista){
        String query = "SELECT * FROM LISTA_ARTICULO WHERE " + ListaArticulo.ARTICULO + " = ? AND " + ListaArticulo.NOMBRE + " = ?";

        SQLiteDatabase lectura = this.obtenerManejadorLectura();
        Cursor cursor = lectura.rawQuery(query, new String[]{Integer.toString(articulo), lista});

        boolean ok = cursor.moveToFirst();
        lectura.close();

        return ok;
    }

    //Modifica un articulo en una lista, significativo uso para la cantidad
    public boolean modificarArticuloEnLista(ListaArticulo listaArticulo){
        int filaActu = 0;
        if(estaArticuloEnLista(listaArticulo.getArticulo(), listaArticulo.getNombre())) {

            SQLiteDatabase db = this.obtenerManejadorEscritura();

            ContentValues valores = new ContentValues();
            valores.put(ListaArticulo.ARTICULO, listaArticulo.getArticulo());
            valores.put(ListaArticulo.NOMBRE, listaArticulo.getNombre());
            valores.put(ListaArticulo.CANTIDAD, listaArticulo.getCantidad());

            filaActu = db.update("LISTA_ARTICULO", valores, ListaArticulo.ARTICULO + "=? AND " + ListaArticulo.NOMBRE + "=?", new String[]{Integer.toString(listaArticulo.getArticulo()), listaArticulo.getNombre()});
            db.close();
        }
        return filaActu > 0;
    }

    //Elimina el articulo de la lista
    public boolean eliminarArticuloEnLista(ListaArticulo listaArticulo){
        int filaBorrada = 0;

        if(estaArticuloEnLista(listaArticulo.getArticulo(), listaArticulo.getNombre())) {
            SQLiteDatabase db = this.obtenerManejadorEscritura();
            filaBorrada = db.delete("LISTA_ARTICULO", ListaArticulo.ARTICULO + "=? AND " + ListaArticulo.NOMBRE + "=?", new String[]{Integer.toString(listaArticulo.getArticulo()), listaArticulo.getNombre()});
            db.close();
        }

        return filaBorrada > 0;
    }

    //Obtiene la cantidad de un articulo en una lista
    public int obtenerCantidadArticuloEnLista (int articulo, Lista lista){

        int cantidad = -1;
        String query = "SELECT CANTIDAD FROM LISTA_ARTICULO WHERE " + ListaArticulo.NOMBRE + " = ? AND " + ListaArticulo.ARTICULO + " = ?";

        SQLiteDatabase lectura = this.obtenerManejadorLectura();
        Cursor cursor = lectura.rawQuery(query, new String[]{lista.getNombre(),Integer.toString(articulo)});

        if (cursor.moveToFirst()) {
            cantidad = cursor.getInt(cursor.getColumnIndex(ListaArticulo.CANTIDAD));
        }

        lectura.close();
        return cantidad;
    }

    public int numArticulosEnLista(String nombre){
        String query = "SELECT COUNT(*) FROM LISTA_ARTICULO WHERE " + ListaArticulo.NOMBRE + " = ?";

        SQLiteDatabase lectura = this.obtenerManejadorLectura();
        Cursor cursor = lectura.rawQuery(query, new String[]{nombre});

        cursor.moveToFirst();
        lectura.close();
        return cursor.getInt(0);
    }

    //STOCK

    //Obtiene un List<Stock> con todos los Stock
    public List<Stock> obtenerStocks(){
        ArrayList<Stock> lista = new ArrayList();
        String query = "SELECT * FROM STOCK";

        SQLiteDatabase db = this.obtenerManejadorLectura();
        Cursor cursor = db.rawQuery(query, null);

        Stock stock = null;
        if (cursor.moveToFirst()) {
            do {
                stock = new Stock();
                stock.setArticulo(cursor.getInt(cursor.getColumnIndex(Stock.ARTICULO)));
                stock.setCanitdad(cursor.getInt(cursor.getColumnIndex(Stock.CANTIDAD)));
                stock.setMinimo(cursor.getInt(cursor.getColumnIndex(Stock.MINIMO)));
                lista.add(stock);
            } while (cursor.moveToNext());
        }

        Log.d("obtenerStocks", lista.toString());
        db.close();
        return lista;
    }

    //Inserta un Stock en la BD, devuelve exito o fracaso
    public boolean insertarStock(Stock stock){

        boolean ok = false;

        if(!estaStock(stock.getArticulo())) {

            SQLiteDatabase db = this.obtenerManejadorEscritura();
            ContentValues values = new ContentValues();

            values.put(Stock.ARTICULO, stock.getArticulo());
            values.put(Stock.CANTIDAD, stock.getCanitdad());
            values.put(Stock.MINIMO, stock.getMinimo());

            db.insert("STOCK", null, values);
            db.close();
            ok = true;
        }
        return ok;

    }

    //Obtiene el Stock del articulo indicado
    public Stock obtenerStocks(Articulo articulo){

        Stock stock = null;
        String query = "SELECT * FROM STOCK WHERE "+ Stock.ARTICULO + " = ?";
        SQLiteDatabase lectura = this.obtenerManejadorLectura();

        Cursor cursor = lectura.rawQuery(query, new String[]{Integer.toString(articulo.getId())});

        if(cursor.moveToFirst()){

            stock = new Stock();
            stock.setArticulo(cursor.getInt(cursor.getColumnIndex(Stock.ARTICULO)));
            stock.setCanitdad(cursor.getInt(cursor.getColumnIndex(Stock.CANTIDAD)));
            stock.setMinimo(cursor.getInt(cursor.getColumnIndex(Stock.MINIMO)));
        }
        lectura.close();
        return stock;
    }

    //Comprueba si esta en stock un id de articulo indicado
    public boolean estaStock(int id){
        String query = "SELECT * FROM STOCK WHERE " + Stock.ARTICULO + " = ?";

        SQLiteDatabase lectura = this.obtenerManejadorLectura();
        Cursor cursor = lectura.rawQuery(query, new String[]{Integer.toString(id)});

        boolean ok = cursor.moveToFirst();
        lectura.close();

        return ok;
    }

    public boolean estaStock(String nombre, String marca, String supermercado){
        Articulo articulo = this.obtenerArticulo(nombre, marca, supermercado);
        if(articulo != null)
            return estaStock(articulo.getId());
        else
            return false;
    }

    //Cambia los valores del stock indicado, debe existir previamente
    public boolean modificarStock(Stock stock){
        int filaActu = 0;
        if(estaStock(stock.getArticulo())){

            SQLiteDatabase db = this.obtenerManejadorEscritura();

            ContentValues valores = new ContentValues();
            valores.put(Stock.ARTICULO, stock.getArticulo());
            valores.put(Stock.CANTIDAD, stock.getCanitdad());
            valores.put(Stock.MINIMO, stock.getMinimo());

            filaActu = db.update("STOCK", valores, Stock.ARTICULO + "=?", new String[]{Integer.toString(stock.getArticulo())});
            db.close();
        }
        return filaActu > 0;
    }

    //Elimina el Stock indicado, debe existir previamente
    public boolean eliminarStock(Stock stock){
        int filaBorrada = 0;

        if(estaStock(stock.getArticulo())){
            SQLiteDatabase db = this.obtenerManejadorEscritura();
            filaBorrada = db.delete("STOCK", Stock.ARTICULO + "=?", new String[]{Integer.toString(stock.getArticulo())});
            db.close();
        }

        return filaBorrada > 0;
    }

    //SUPERMERCADO

    //Obtiene todos los supermercados y devuelve un list de STRINGS
    public List<String> obtenerSupermercados(){

        ArrayList<String> supermercados = new ArrayList();
        String query = "SELECT * FROM SUPERMERCADO";

        SQLiteDatabase db = this.obtenerManejadorLectura();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                supermercados.add(cursor.getString(cursor.getColumnIndex(Supermercado.NOMBRE)));
            } while (cursor.moveToNext());
        }

        Log.d("obtenerSupermercados", "size: " + supermercados.size());

        db.close();
        return supermercados;

    }


}
