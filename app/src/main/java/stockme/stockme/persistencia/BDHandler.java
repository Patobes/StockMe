package stockme.stockme.persistencia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import stockme.stockme.logica.Articulo;
import stockme.stockme.logica.ArticuloSupermercado;
import stockme.stockme.logica.Lista;
import stockme.stockme.logica.ListaArticulo;
import stockme.stockme.logica.Stock;
import stockme.stockme.logica.Supermercado;
import stockme.stockme.util.Util;

public class BDHandler extends SQLiteOpenHelper {
    //private static int bdVersion = Util.getBD_VERSION();

    public BDHandler(Context context) {
        super(context, "Productos", null, Util.getBD_VERSION());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        insertarIniciales(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db){ db.execSQL("PRAGMA foreign_keys = ON;"); }

    private void insertarIniciales(SQLiteDatabase db) {
        /* TABLA ARTICULO
        ID - INT - PK
        NOMBRE - STRING
        MARCA - STRING
        TIPO - STRING
         */
        String articulo = "CREATE TABLE `ARTICULO` (" +
                "`Id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "`Nombre` TEXT NOT NULL," +
                "`Marca` TEXT DEFAULT 'Cualquiera'," +
                "`Tipo` TEXT DEFAULT 'Cualquiera'" +
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

        /* TABLA ARTICULO_SUPERMERCADO
        ID - INT - PK
        ARTICULO - INT - FK
        SUPERMERCADO - STRING - FK
        PRECIO - FLOAT
         */
        String articuloSupermercado = "CREATE TABLE `ARTICULO_SUPERMERCADO` (" +
                "`Id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "`Articulo` INT NOT NULL," +
                "`Supermercado` TEXT NOT NULL DEFAULT 'Cualquiera'," +
                "`Precio` REAL DEFAULT 0.0," +
                "FOREIGN KEY(`Articulo`) REFERENCES `ARTICULO`(`Id`) ON DELETE CASCADE," +
                "FOREIGN KEY(`Supermercado`) REFERENCES `SUPERMERCADO`(`Nombre`)" +
                ")";
        db.execSQL("DROP TABLE IF EXISTS 'ARTICULO_SUPERMERCADO';");
        db.execSQL(articuloSupermercado);

        /* TABLA STOCK
        ARTICULO - INT - PK - FK
        MINIMO - INT
        CANTIDAD - INT
         */
        String stock = "CREATE TABLE `STOCK` (" +
                "`Articulo` INTEGER NOT NULL," +
                "`Cantidad` INTEGER NOT NULL DEFAULT 1," +
                "`Minimo` INTEGER NOT NULL DEFAULT 0," +
                "PRIMARY KEY(Articulo)," +
                "FOREIGN KEY(`Articulo`) REFERENCES `ARTICULO`(`Id`) ON DELETE CASCADE" +
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
                "`Supermercado` TEXT NOT NULL DEFAULT 'Cualquiera'," +
                "PRIMARY KEY(Nombre)," +
                "FOREIGN KEY(`Supermercado`) REFERENCES SUPERMERCADO(Nombre)" +
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
                "FOREIGN KEY(`Articulo`) REFERENCES `ARTICULO_SUPERMERCADO`(`Id`) ON DELETE CASCADE," +
                "FOREIGN KEY(`Nombre`) REFERENCES `LISTA`(`Nombre`) ON DELETE CASCADE" +
                ")";
        db.execSQL("DROP TABLE IF EXISTS 'LISTA_ARTICULO';");
        db.execSQL(listaArticulo);

        //ELEMENTOS INICIALES
        db.execSQL("INSERT INTO `ARTICULO` VALUES(1, 'Leche', 'Hacendado', 'Lácteos');");
        db.execSQL("INSERT INTO `ARTICULO` VALUES(2, 'Pizza', 'Hacendado', 'Carnicería');");
        db.execSQL("INSERT INTO `ARTICULO` VALUES(3, 'Ketchup', 'Heinz', 'Salsas y especias');");
        db.execSQL("INSERT INTO `ARTICULO` VALUES(4, 'Mostaza', 'Heinz', 'Salsas y especias');");

        db.execSQL("INSERT INTO `SUPERMERCADO` VALUES('Cualquiera');");
        db.execSQL("INSERT INTO `SUPERMERCADO` VALUES('Día');");
        db.execSQL("INSERT INTO `SUPERMERCADO` VALUES('Mercadona');");
        db.execSQL("INSERT INTO `SUPERMERCADO` VALUES('SuperUltraHiperMercadona');");
        db.execSQL("INSERT INTO `SUPERMERCADO` VALUES('Eroski');");
        db.execSQL("INSERT INTO `SUPERMERCADO` VALUES('Simply');");
        db.execSQL("INSERT INTO `SUPERMERCADO` VALUES('Carrefour');");
        db.execSQL("INSERT INTO `SUPERMERCADO` VALUES('Supersol');");

        db.execSQL("INSERT INTO `ARTICULO_SUPERMERCADO` VALUES(1, 1, 'Mercadona', 0.60);");
        db.execSQL("INSERT INTO `ARTICULO_SUPERMERCADO` VALUES(2, 2, 'Mercadona', 2.60);");
        db.execSQL("INSERT INTO `ARTICULO_SUPERMERCADO` VALUES(3, 3, 'Mercadona', 0.60);");
        db.execSQL("INSERT INTO `ARTICULO_SUPERMERCADO` VALUES(4, 4, 'Mercadona', 0.60);");
        db.execSQL("INSERT INTO `ARTICULO_SUPERMERCADO` VALUES(5, 1, 'Eroski', 0.59);");
        db.execSQL("INSERT INTO `ARTICULO_SUPERMERCADO` VALUES(6, 2, 'Eroski', 2.59);");

        db.execSQL("INSERT INTO 'LISTA' VALUES('Lista prueba','18/04/2016','18/04/2016','Mercadona')");
        db.execSQL("INSERT INTO 'LISTA' VALUES('Lista prueba 2','18/04/2016','18/04/2016','Cualquiera')");

        db.execSQL("INSERT INTO 'LISTA_ARTICULO' VALUES(1,'Lista prueba', 6)");
        db.execSQL("INSERT INTO 'LISTA_ARTICULO' VALUES(2,'Lista prueba', 1)");
        db.execSQL("INSERT INTO 'LISTA_ARTICULO' VALUES(3,'Lista prueba', 3)");
        db.execSQL("INSERT INTO 'LISTA_ARTICULO' VALUES(4,'Lista prueba', 5)");

        db.execSQL("INSERT INTO 'STOCK' VALUES (1, 4, 1)");
        db.execSQL("INSERT INTO 'STOCK' VALUES (2, 5, 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        insertarIniciales(db);
    }

    public SQLiteDatabase obtenerManejadorLectura() {
        return this.getReadableDatabase();
    }

    public SQLiteDatabase obtenerManejadorEscritura() {
        return this.getWritableDatabase();
    }

    public void abrir() {
        this.getWritableDatabase();
    }

    public void cerrar() {
        this.close();
    }


    //FUNCIONES EXTRAS
    public List<String> obtenerMarcas() {
        ArrayList<String> marcas = new ArrayList<>();

        String query = "SELECT DISTINCT " + Articulo.MARCA + " FROM ARTICULO";
        SQLiteDatabase db = this.obtenerManejadorLectura();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                String marca = cursor.getString(0);
                if (marca != null && !marca.isEmpty())
                    marcas.add(marca);
            } while (cursor.moveToNext());
        }
        db.close();

        return marcas;
    }

    public float obtenerPrecioTotal(String lista) {
        float precio = 0.0f;

        String query = "select SUM(a.Precio * la.Cantidad) as 'Total' from ARTICULO_SUPERMERCADO as a " +
                " join LISTA_ARTICULO as la on a.id = la.articulo" +
                " where la.Nombre = ?;";
        SQLiteDatabase db = this.obtenerManejadorLectura();
        Cursor cursor = db.rawQuery(query, new String[]{lista});
        if (cursor.moveToFirst()) {
            precio = cursor.getFloat(0);
        }
        db.close();

        return precio;
    }

    //ARTICULO (ID, NOMBRE, MARCA, TIPO)

    //ARTICULO - GET

    public List<Articulo> obtenerArticulosConQuerySearch(String querySearch){
        ArrayList<Articulo> lista = new ArrayList<Articulo>();
        String query = "SELECT * FROM ARTICULO WHERE NOMBRE LIKE ?";

        SQLiteDatabase db = this.obtenerManejadorLectura();
        Cursor cursor = db.rawQuery(query, new String[]{"%" + querySearch + "%"});

        if (cursor.moveToFirst()) {
            do {
                Articulo art = new Articulo(cursor.getInt(cursor.getColumnIndex(Articulo.ID)),
                        cursor.getString(cursor.getColumnIndex(Articulo.NOMBRE)),
                        cursor.getString(cursor.getColumnIndex(Articulo.MARCA)),
                        cursor.getString(cursor.getColumnIndex(Articulo.TIPO)));

                lista.add(art);
            } while (cursor.moveToNext());
        }

        Log.d("obtenerArticulos()", lista.toString());
        db.close();
        return lista;
    }

    public List<Articulo> obtenerArticulos() {
        ArrayList<Articulo> lista = new ArrayList<Articulo>();
        String query = "SELECT * FROM ARTICULO";

        SQLiteDatabase db = this.obtenerManejadorLectura();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Articulo art = new Articulo(cursor.getInt(cursor.getColumnIndex(Articulo.ID)),
                        cursor.getString(cursor.getColumnIndex(Articulo.NOMBRE)),
                        cursor.getString(cursor.getColumnIndex(Articulo.MARCA)),
                        cursor.getString(cursor.getColumnIndex(Articulo.TIPO)));

                lista.add(art);
            } while (cursor.moveToNext());
        }

        Log.d("obtenerArticulos()", lista.toString());
        db.close();
        return lista;
    }

    public Articulo obtenerArticulo(int id) {
        Articulo articulo = null;
        String query = "SELECT * FROM ARTICULO WHERE " + Articulo.ID + " = ?";
        SQLiteDatabase lectura = this.obtenerManejadorLectura();

        Cursor cursor = lectura.rawQuery(query, new String[]{Integer.toString(id)});

        if (cursor.moveToFirst()) {
            articulo = new Articulo(cursor.getInt(cursor.getColumnIndex(Articulo.ID)),
                    cursor.getString(cursor.getColumnIndex(Articulo.NOMBRE)),
                    cursor.getString(cursor.getColumnIndex(Articulo.MARCA)),
                    cursor.getString(cursor.getColumnIndex(Articulo.TIPO)));
        }
        lectura.close();
        return articulo;
    }

    public Articulo obtenerArticulo(String nombre, String marca) {
        Articulo articulo = null;
        String query = "SELECT * FROM ARTICULO WHERE " + Articulo.NOMBRE + " = ? AND " + Articulo.MARCA + " = ?";
        SQLiteDatabase lectura = this.obtenerManejadorLectura();

        Cursor cursor = lectura.rawQuery(query, new String[]{nombre, marca});

        if (cursor.moveToFirst()) {
            articulo = new Articulo(cursor.getInt(cursor.getColumnIndex(Articulo.ID)),
                    cursor.getString(cursor.getColumnIndex(Articulo.NOMBRE)),
                    cursor.getString(cursor.getColumnIndex(Articulo.MARCA)),
                    cursor.getString(cursor.getColumnIndex(Articulo.TIPO)));
        }
        lectura.close();
        return articulo;
    }

    public Articulo obtenerArticulo(String nombre, String marca, String tipo){
        Articulo articulo = null;
        String query = "SELECT * FROM ARTICULO WHERE " + Articulo.NOMBRE + " = ? AND " + Articulo.MARCA + " = ? AND "
                + Articulo.TIPO + " = ?";
        SQLiteDatabase lectura = this.obtenerManejadorLectura();

        Cursor cursor = lectura.rawQuery(query, new String[]{nombre, marca, tipo});

        if(cursor.moveToFirst()){
            articulo = new Articulo(cursor.getInt(cursor.getColumnIndex(Articulo.ID)),
                    cursor.getString(cursor.getColumnIndex(Articulo.NOMBRE)),
                    cursor.getString(cursor.getColumnIndex(Articulo.MARCA)),
                    cursor.getString(cursor.getColumnIndex(Articulo.TIPO)));
        }
        lectura.close();
        return articulo;
    }

    public List<Articulo> obtenerArticulosPorNombre(String nombre){
        ArrayList<Articulo> articulos = new ArrayList<Articulo>();

        String query = "SELECT * FROM ARTICULO WHERE " + Articulo.NOMBRE + " = ?";
        SQLiteDatabase lectura = this.obtenerManejadorLectura();

        Cursor cursor = lectura.rawQuery(query, new String[]{nombre});

        if(cursor.moveToFirst()){
            do{
                Articulo articulo = new Articulo(cursor.getInt(cursor.getColumnIndex(Articulo.ID)),
                        cursor.getString(cursor.getColumnIndex(Articulo.NOMBRE)),
                        cursor.getString(cursor.getColumnIndex(Articulo.MARCA)),
                        cursor.getString(cursor.getColumnIndex(Articulo.TIPO)));

                articulos.add(articulo);
            }while(cursor.moveToNext());
        }
        lectura.close();
        return articulos;
    }

    public List<Articulo> obtenerArticulosPorMarca(String marca){
        ArrayList<Articulo> articulos = new ArrayList<Articulo>();

        String query = "SELECT * FROM ARTICULO WHERE " + Articulo.MARCA + " = ?";
        SQLiteDatabase lectura = this.obtenerManejadorLectura();

        Cursor cursor = lectura.rawQuery(query, new String[]{marca});

        if(cursor.moveToFirst()){
            do{
                Articulo articulo = new Articulo(cursor.getInt(cursor.getColumnIndex(Articulo.ID)),
                        cursor.getString(cursor.getColumnIndex(Articulo.NOMBRE)),
                        cursor.getString(cursor.getColumnIndex(Articulo.MARCA)),
                        cursor.getString(cursor.getColumnIndex(Articulo.TIPO)));

                articulos.add(articulo);
            }while(cursor.moveToNext());
        }
        lectura.close();
        return articulos;
    }

    public List<Articulo> obtenerArticulosPorTipoYQuerySearch(String tipo, String querySearch){
        ArrayList<Articulo> articulos = new ArrayList<Articulo>();

        String query = "SELECT * FROM ARTICULO WHERE " + Articulo.TIPO + " = ? AND NOMBRE LIKE ?";
        SQLiteDatabase lectura = this.obtenerManejadorLectura();

        Cursor cursor = lectura.rawQuery(query, new String[]{tipo, "%"+querySearch+"%"});

        if(cursor.moveToFirst()){
            do{
                Articulo articulo = new Articulo(cursor.getInt(cursor.getColumnIndex(Articulo.ID)),
                        cursor.getString(cursor.getColumnIndex(Articulo.NOMBRE)),
                        cursor.getString(cursor.getColumnIndex(Articulo.MARCA)),
                        cursor.getString(cursor.getColumnIndex(Articulo.TIPO)));

                articulos.add(articulo);
            }while(cursor.moveToNext());
        }
        lectura.close();
        return articulos;
    }

    public List<Articulo> obtenerArticulosPorTipo(String tipo){
        ArrayList<Articulo> articulos = new ArrayList<Articulo>();

        String query = "SELECT * FROM ARTICULO WHERE " + Articulo.TIPO + " = ?";
        SQLiteDatabase lectura = this.obtenerManejadorLectura();

        Cursor cursor = lectura.rawQuery(query, new String[]{tipo});

        if (cursor.moveToFirst()) {
            do {
                Articulo articulo = new Articulo(cursor.getInt(cursor.getColumnIndex(Articulo.ID)),
                        cursor.getString(cursor.getColumnIndex(Articulo.NOMBRE)),
                        cursor.getString(cursor.getColumnIndex(Articulo.MARCA)),
                        cursor.getString(cursor.getColumnIndex(Articulo.TIPO)));

                articulos.add(articulo);
            } while (cursor.moveToNext());
        }
        lectura.close();
        return articulos;
    }

    public List<Articulo> obtenerArticulosOrdenPorTipoYQuerySearch(String querySearch) {
        ArrayList<Articulo> articulos = new ArrayList<Articulo>();

        String query = "SELECT * FROM ARTICULO WHERE NOMBRE LIKE ? ORDER BY " + Articulo.TIPO;
        SQLiteDatabase lectura = this.obtenerManejadorLectura();

        Cursor cursor = lectura.rawQuery(query, new String[]{"%" + querySearch + "%"});

        if (cursor.moveToFirst()) {
            do {
                Articulo articulo = new Articulo(cursor.getInt(cursor.getColumnIndex(Articulo.ID)),
                        cursor.getString(cursor.getColumnIndex(Articulo.NOMBRE)),
                        cursor.getString(cursor.getColumnIndex(Articulo.MARCA)),
                        cursor.getString(cursor.getColumnIndex(Articulo.TIPO)));

                articulos.add(articulo);
            } while (cursor.moveToNext());
        }
        lectura.close();
        return articulos;
    }

    public List<Articulo> obtenerArticulosOrdenPorTipo() {
        ArrayList<Articulo> articulos = new ArrayList<Articulo>();

        String query = "SELECT * FROM ARTICULO ORDER BY " + Articulo.TIPO;
        SQLiteDatabase lectura = this.obtenerManejadorLectura();

        Cursor cursor = lectura.rawQuery(query, new String[]{});

        if(cursor.moveToFirst()){
            do{
                Articulo articulo = new Articulo(cursor.getInt(cursor.getColumnIndex(Articulo.ID)),
                        cursor.getString(cursor.getColumnIndex(Articulo.NOMBRE)),
                        cursor.getString(cursor.getColumnIndex(Articulo.MARCA)),
                        cursor.getString(cursor.getColumnIndex(Articulo.TIPO)));

                articulos.add(articulo);
            }while(cursor.moveToNext());
        }
        lectura.close();
        return articulos;
    }

    //ARTICULO - EXIST

    public boolean estaArticulo(int id){
        return obtenerArticulo(id) != null;
    }

    public boolean estaArticulo(String nombre, String marca){
        return obtenerArticulo(nombre, marca) != null;
    }

    //ARTICULO - INSERT -> Devuelve el id del articulo insertado o -1 si no se ha podido insertar

    public Integer insertarArticulo(Articulo articulo){
        if(estaArticulo(articulo.getNombre(), articulo.getMarca()))
            return -1;
        else{
            SQLiteDatabase db = this.obtenerManejadorEscritura();
            ContentValues values = new ContentValues();

            values.put(Articulo.NOMBRE, articulo.getNombre());
            values.put(Articulo.MARCA, articulo.getMarca());
            values.put(Articulo.TIPO, articulo.getTipo());

            long ident = db.insert("ARTICULO", null, values);

            if (ident != -1) {
                articulo.setId((int)ident);
            }

            db.close();
            return (int)ident;
        }
    }

    public Integer insertarArticulo(String nombre, String marca){
        if(estaArticulo(nombre, marca))
            return -1;
        else{
            SQLiteDatabase db = this.obtenerManejadorEscritura();
            ContentValues values = new ContentValues();

            values.put(Articulo.NOMBRE, nombre);
            values.put(Articulo.MARCA, marca);

            long ident = db.insert("ARTICULO", null, values);

            db.close();
            return (int)ident;
        }
    }

    public Integer insertarArticulo(String nombre, String marca, String tipo){
        SQLiteDatabase db = this.obtenerManejadorEscritura();
        ContentValues values = new ContentValues();

        values.put(Articulo.NOMBRE, nombre);
        values.put(Articulo.MARCA, marca);
        values.put(Articulo.TIPO, tipo);

        long ident = db.insert("ARTICULO", null, values);

        db.close();
        return (int)ident;
    }

    public boolean modificarArticulo(Articulo articulo){
        int filaActu = 0;
        if(estaArticulo(articulo.getId())){

            SQLiteDatabase db = this.obtenerManejadorEscritura();

            ContentValues valores = new ContentValues();
            valores.put(Articulo.NOMBRE, articulo.getNombre());
            valores.put(Articulo.MARCA, articulo.getMarca());
            valores.put(Articulo.TIPO, articulo.getTipo());

            filaActu = db.update("ARTICULO", valores, Articulo.ID + " = ?", new String[]{Integer.toString(articulo.getId())});
            db.close();
        }
        return filaActu > 0;
    }

    public boolean modificarArticuloNombre(Articulo articulo, String nombre){
        int filaActu = 0;
        if(estaArticulo(articulo.getId())){

            SQLiteDatabase db = this.obtenerManejadorEscritura();

            ContentValues valores = new ContentValues();
            valores.put(Articulo.NOMBRE, nombre);

            filaActu = db.update("ARTICULO", valores, Articulo.ID + " = ?", new String[]{Integer.toString(articulo.getId())});
            db.close();
        }
        return filaActu > 0;
    }

    public boolean modificarArticuloMarca(Articulo articulo, String marca){
        int filaActu = 0;
        if(estaArticulo(articulo.getId())){

            SQLiteDatabase db = this.obtenerManejadorEscritura();

            ContentValues valores = new ContentValues();
            valores.put(Articulo.MARCA, marca);

            filaActu = db.update("ARTICULO", valores, Articulo.ID + " = ?", new String[]{Integer.toString(articulo.getId())});
            db.close();
        }
        return filaActu > 0;
    }

    public boolean modificarArticuloTipo(Articulo articulo, String tipo){
        int filaActu = 0;
        if(estaArticulo(articulo.getId())){

            SQLiteDatabase db = this.obtenerManejadorEscritura();

            ContentValues valores = new ContentValues();
            valores.put(Articulo.TIPO, tipo);

            filaActu = db.update("ARTICULO", valores, Articulo.ID + " = ?", new String[]{Integer.toString(articulo.getId())});
            db.close();
        }
        return filaActu > 0;
    }

    //ARTICULO - DELETE

    public boolean eliminarArticulo(Articulo articulo){
        return eliminarArticulo(articulo.getNombre(), articulo.getMarca());
    }

    public boolean eliminarArticulo(String nombre, String marca){
        int filaBorrada = 0;

        if(estaArticulo(nombre, marca)){
            SQLiteDatabase db = this.obtenerManejadorEscritura();
            filaBorrada = db.delete("ARTICULO", Articulo.NOMBRE + " = ? AND " + Articulo.MARCA + " = ?", new String[]{nombre, marca});
            db.close();
        }
        return filaBorrada> 0;
    }

    //-------------------------------------------------------------------------------------------

    //SUPERMERCADO (NOMBRE)

    //SUPERMERCADO - GET

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

    public Supermercado obtenerSupermercado(String nombre){
        Supermercado supermercado = null;
        String query = "SELECT * FROM SUPERMERCADO WHERE " + Supermercado.NOMBRE + " = ?";

        SQLiteDatabase db = this.obtenerManejadorLectura();
        Cursor cursor = db.rawQuery(query, new String[]{nombre});

        if (cursor.moveToFirst()) {
            supermercado = new Supermercado(cursor.getString(cursor.getColumnIndex(Supermercado.NOMBRE)));
        }

        db.close();
        return supermercado;
    }

    //SUPERMERCADO - EXIST

    public boolean estaSupermercado(String nombre){
        return obtenerSupermercado(nombre) != null;
    }

    //SUPERMERCADO - INSERT

    public boolean insertarSupermercado(Supermercado supermercado) {
        return insertarSupermercado(supermercado.getNombre());
    }

    public boolean insertarSupermercado(String nombre){
        if (estaSupermercado(nombre))
            return false;
        else {
            SQLiteDatabase db = this.obtenerManejadorEscritura();
            ContentValues values = new ContentValues();

            values.put(Supermercado.NOMBRE, nombre);

            long ident = db.insert("SUPERMERCADO", null, values);

            db.close();
            return ident != -1;
        }
    }

    //SUPERMERCADO - SET

    public boolean modificarSupermercadoNombre(Supermercado supermercado, String nombre){
        int filaActu = 0;
        if(estaSupermercado(supermercado.getNombre())){

            SQLiteDatabase db = this.obtenerManejadorEscritura();

            ContentValues valores = new ContentValues();
            valores.put(Supermercado.NOMBRE, nombre);

            filaActu = db.update("SUPERMERCADO", valores, Supermercado.NOMBRE + "=?", new String[]{supermercado.getNombre()});
            db.close();
        }
        return filaActu > 0;
    }

    //SUPERMERCADO - DELETE

    public boolean eliminarSupermercado(Supermercado supermercado){
        return eliminarSupermercado(supermercado.getNombre());
    }

    public boolean eliminarSupermercado(String nombre){
        int filaBorrada = 0;

        if(estaSupermercado(nombre)){
            SQLiteDatabase db = this.obtenerManejadorEscritura();
            filaBorrada = db.delete("SUPERMERCADP", Supermercado.NOMBRE + "=?", new String[]{nombre});
            db.close();
        }
        return filaBorrada> 0;
    }

    //-------------------------------------------------------------------------------------------

    //ARTICULO_SUPERMERCADO (ID, ARTICULO - FK(ARTICULO), SUPERMERCADO - FK(SUPERMERCADO), PRECIO)

    ///Añadir metodos de busqueda por parametros de claves foraneas (int idArticulo --> getId(String nombre, String marca))

    //ARTICULO_SUPERMERCADO - GET

    public List<ArticuloSupermercado> obtenerArticulosSupermercado(){
        ArrayList<ArticuloSupermercado> lista = new ArrayList<ArticuloSupermercado>();
        String query = "SELECT * FROM ARTICULO_SUPERMERCADO";

        SQLiteDatabase db = this.obtenerManejadorLectura();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                ArticuloSupermercado art = new ArticuloSupermercado(cursor.getInt(cursor.getColumnIndex(ArticuloSupermercado.ID)),
                        cursor.getInt(cursor.getColumnIndex(ArticuloSupermercado.ARTICULO)),
                        cursor.getString(cursor.getColumnIndex(ArticuloSupermercado.SUPERMERCADO)),
                        cursor.getFloat(cursor.getColumnIndex(ArticuloSupermercado.PRECIO)));

                lista.add(art);
            } while (cursor.moveToNext());
        }

        Log.d("obtenerArticulos()", lista.toString());
        db.close();
        return lista;
    }

    public ArticuloSupermercado obtenerArticuloSupermercado(int id){
        ArticuloSupermercado articulo = null;
        String query = "SELECT * FROM ARTICULO_SUPERMERCADO WHERE " + ArticuloSupermercado.ID + " = ?";
        SQLiteDatabase lectura = this.obtenerManejadorLectura();

        Cursor cursor = lectura.rawQuery(query, new String[]{Integer.toString(id)});

        if(cursor.moveToFirst()){
            //Poner parametros dentro del constructor?
            articulo = new ArticuloSupermercado(cursor.getInt(cursor.getColumnIndex(ArticuloSupermercado.ID)),
                    cursor.getInt(cursor.getColumnIndex(ArticuloSupermercado.ARTICULO)),
                    cursor.getString(cursor.getColumnIndex(ArticuloSupermercado.SUPERMERCADO)),
                    cursor.getFloat(cursor.getColumnIndex(ArticuloSupermercado.PRECIO)));
        }
        lectura.close();
        return articulo;
    }

    public ArticuloSupermercado obtenerArticuloSupermercado(int idArticulo, String supermercado){
        ArticuloSupermercado articulo = null;
        String query = "SELECT * FROM ARTICULO_SUPERMERCADO WHERE " + ArticuloSupermercado.ARTICULO + " = ? AND " + ArticuloSupermercado.SUPERMERCADO + " = ?";
        SQLiteDatabase lectura = this.obtenerManejadorLectura();

        Cursor cursor = lectura.rawQuery(query, new String[]{Integer.toString(idArticulo), supermercado});

        if(cursor.moveToFirst()){
            articulo = new ArticuloSupermercado(cursor.getInt(cursor.getColumnIndex(ArticuloSupermercado.ID)),
                    cursor.getInt(cursor.getColumnIndex(ArticuloSupermercado.ARTICULO)),
                    cursor.getString(cursor.getColumnIndex(ArticuloSupermercado.SUPERMERCADO)),
                    cursor.getFloat(cursor.getColumnIndex(ArticuloSupermercado.PRECIO)));
        }
        lectura.close();
        return articulo;
    }

    public ArticuloSupermercado obtenerArticuloSupermercado(int idArticulo, String supermercado, float precio){
        ArticuloSupermercado articulo = null;
        String query = "SELECT * FROM ARTICULO_SUPERMERCADO WHERE " + ArticuloSupermercado.ARTICULO + " = ? AND " + ArticuloSupermercado.SUPERMERCADO + " = ? AND "
                + ArticuloSupermercado.PRECIO + " = ?";
        SQLiteDatabase lectura = this.obtenerManejadorLectura();

        Cursor cursor = lectura.rawQuery(query, new String[]{Integer.toString(idArticulo), supermercado, Float.toString(precio)});

        if(cursor.moveToFirst()){
            articulo = new ArticuloSupermercado(cursor.getInt(cursor.getColumnIndex(ArticuloSupermercado.ID)),
                    cursor.getInt(cursor.getColumnIndex(ArticuloSupermercado.ARTICULO)),
                    cursor.getString(cursor.getColumnIndex(ArticuloSupermercado.SUPERMERCADO)),
                    cursor.getFloat(cursor.getColumnIndex(ArticuloSupermercado.PRECIO)));
        }
        lectura.close();
        return articulo;
    }

    public ArticuloSupermercado obtenerArticuloSupermercado(String nombre, String marca, String supermercado){
        Articulo art = obtenerArticulo(nombre, marca);
        if(art != null)
            return obtenerArticuloSupermercado(art.getId(), supermercado);
        else
            return null;
    }

    public ArticuloSupermercado obtenerArticuloSupermercado(String nombre, String marca, String tipo, String supermercado){
        Articulo art = obtenerArticulo(nombre, marca, tipo);
        if(art != null)
            return obtenerArticuloSupermercado(art.getId(), supermercado);
        else
            return null;
    }

    public List<ArticuloSupermercado> obtenerArticulosSupermercadoPorArticulo(int articulo){
        ArrayList<ArticuloSupermercado> lista = new ArrayList<ArticuloSupermercado>();
        String query = "SELECT * FROM ARTICULO_SUPERMERCADO WHERE " + ArticuloSupermercado.ARTICULO + " = ?";

        SQLiteDatabase db = this.obtenerManejadorLectura();
        Cursor cursor = db.rawQuery(query, new String[]{Integer.toString(articulo)});

        if (cursor.moveToFirst()) {
            do {
                ArticuloSupermercado art = new ArticuloSupermercado(cursor.getInt(cursor.getColumnIndex(ArticuloSupermercado.ID)),
                        cursor.getInt(cursor.getColumnIndex(ArticuloSupermercado.ARTICULO)),
                        cursor.getString(cursor.getColumnIndex(ArticuloSupermercado.SUPERMERCADO)),
                        cursor.getFloat(cursor.getColumnIndex(ArticuloSupermercado.PRECIO)));

                lista.add(art);
            } while (cursor.moveToNext());
        }
        db.close();
        return lista;
    }

    public List<ArticuloSupermercado> obtenerArticulosSupermercadoPorArticulo(String nombre, String marca){
        Articulo art = obtenerArticulo(nombre, marca);
        if(art != null)
            return obtenerArticulosSupermercadoPorArticulo(art.getId());
        else
            return null;
    }

    public List<ArticuloSupermercado> obtenerArticulosSupermercadoPorSupermercado(String supermercado){
        ArrayList<ArticuloSupermercado> lista = new ArrayList<ArticuloSupermercado>();
        String query = "SELECT * FROM ARTICULO_SUPERMERCADO WHERE " + ArticuloSupermercado.SUPERMERCADO + " = ?";

        SQLiteDatabase db = this.obtenerManejadorLectura();
        Cursor cursor = db.rawQuery(query, new String[]{supermercado});

        if (cursor.moveToFirst()) {
            do {
                ArticuloSupermercado art = new ArticuloSupermercado(cursor.getInt(cursor.getColumnIndex(ArticuloSupermercado.ID)),
                        cursor.getInt(cursor.getColumnIndex(ArticuloSupermercado.ARTICULO)),
                        cursor.getString(cursor.getColumnIndex(ArticuloSupermercado.SUPERMERCADO)),
                        cursor.getFloat(cursor.getColumnIndex(ArticuloSupermercado.PRECIO)));

                lista.add(art);
            } while (cursor.moveToNext());
        }
        db.close();
        return lista;
    }

    public List<ArticuloSupermercado> obtenerArticulosSupermercadoPorPrecioIgual(float precio){
        ArrayList<ArticuloSupermercado> lista = new ArrayList<ArticuloSupermercado>();
        String query = "SELECT * FROM ARTICULO_SUPERMERCADO WHERE " + ArticuloSupermercado.PRECIO + " = ?";

        SQLiteDatabase db = this.obtenerManejadorLectura();
        Cursor cursor = db.rawQuery(query, new String[]{Float.toString(precio)});

        if (cursor.moveToFirst()) {
            do {
                ArticuloSupermercado art = new ArticuloSupermercado(cursor.getInt(cursor.getColumnIndex(ArticuloSupermercado.ID)),
                        cursor.getInt(cursor.getColumnIndex(ArticuloSupermercado.ARTICULO)),
                        cursor.getString(cursor.getColumnIndex(ArticuloSupermercado.SUPERMERCADO)),
                        cursor.getFloat(cursor.getColumnIndex(ArticuloSupermercado.PRECIO)));

                lista.add(art);
            } while (cursor.moveToNext());
        }
        db.close();
        return lista;
    }

    public List<ArticuloSupermercado> obtenerArticulosSupermercadoPorPrecioMenor(float precio){
        ArrayList<ArticuloSupermercado> lista = new ArrayList<ArticuloSupermercado>();
        String query = "SELECT * FROM ARTICULO_SUPERMERCADO WHERE " + ArticuloSupermercado.PRECIO + " < ?";

        SQLiteDatabase db = this.obtenerManejadorLectura();
        Cursor cursor = db.rawQuery(query, new String[]{Float.toString(precio)});

        if (cursor.moveToFirst()) {
            do {
                ArticuloSupermercado art = new ArticuloSupermercado(cursor.getInt(cursor.getColumnIndex(ArticuloSupermercado.ID)),
                        cursor.getInt(cursor.getColumnIndex(ArticuloSupermercado.ARTICULO)),
                        cursor.getString(cursor.getColumnIndex(ArticuloSupermercado.SUPERMERCADO)),
                        cursor.getFloat(cursor.getColumnIndex(ArticuloSupermercado.PRECIO)));

                lista.add(art);
            } while (cursor.moveToNext());
        }
        db.close();
        return lista;
    }

    public List<ArticuloSupermercado> obtenerArticulosSupermercadoPorPrecioMayor(float precio){
        ArrayList<ArticuloSupermercado> lista = new ArrayList<ArticuloSupermercado>();
        String query = "SELECT * FROM ARTICULO_SUPERMERCADO WHERE " + ArticuloSupermercado.PRECIO + " > ?";

        SQLiteDatabase db = this.obtenerManejadorLectura();
        Cursor cursor = db.rawQuery(query, new String[]{Float.toString(precio)});

        if (cursor.moveToFirst()) {
            do {
                ArticuloSupermercado art = new ArticuloSupermercado(cursor.getInt(cursor.getColumnIndex(ArticuloSupermercado.ID)),
                        cursor.getInt(cursor.getColumnIndex(ArticuloSupermercado.ARTICULO)),
                        cursor.getString(cursor.getColumnIndex(ArticuloSupermercado.SUPERMERCADO)),
                        cursor.getFloat(cursor.getColumnIndex(ArticuloSupermercado.PRECIO)));

                lista.add(art);
            } while (cursor.moveToNext());
        }
        db.close();
        return lista;
    }

    public List<ArticuloSupermercado> obtenerArticulosSupermercadoPorPrecioEntre(float precio1, float precio2){
        ArrayList<ArticuloSupermercado> lista = new ArrayList<ArticuloSupermercado>();
        String query = "SELECT * FROM ARTICULO_SUPERMERCADO WHERE " + ArticuloSupermercado.PRECIO + " > ? AND " + ArticuloSupermercado.PRECIO + " < ?";

        float minimo = precio1, maximo = precio2;
        if(precio2 < precio1) {
            minimo = precio2;
            maximo = precio1;
        }
        SQLiteDatabase db = this.obtenerManejadorLectura();
        Cursor cursor = db.rawQuery(query, new String[]{Float.toString(minimo), Float.toString(maximo)});

        if (cursor.moveToFirst()) {
            do {
                ArticuloSupermercado art = new ArticuloSupermercado(cursor.getInt(cursor.getColumnIndex(ArticuloSupermercado.ID)),
                        cursor.getInt(cursor.getColumnIndex(ArticuloSupermercado.ARTICULO)),
                        cursor.getString(cursor.getColumnIndex(ArticuloSupermercado.SUPERMERCADO)),
                        cursor.getFloat(cursor.getColumnIndex(ArticuloSupermercado.PRECIO)));

                lista.add(art);
            } while (cursor.moveToNext());
        }
        db.close();
        return lista;
    }

    //ARTICULO_SUPERMERCADO - EXIST

    public boolean estaArticuloSupermercado(int id){
        return obtenerArticuloSupermercado(id) != null;
    }

    public boolean estaArticuloSupermercado(int articulo, String supermercado){
        return obtenerArticuloSupermercado(articulo, supermercado) != null;
    }

    public boolean estaArticuloSupermercado(String nombre, String marca, String supermercado){
        return obtenerArticuloSupermercado(nombre, marca, supermercado) != null;
    }

    //ARTICULO_SUPERMERCADO - INSERT

    public Integer insertarArticuloSupermercado(ArticuloSupermercado articuloSupermercado){
        if(estaArticuloSupermercado(articuloSupermercado.getArticulo(), articuloSupermercado.getSupermercado()))
            return -1;
        else{
            SQLiteDatabase db = this.obtenerManejadorEscritura();
            ContentValues values = new ContentValues();

            values.put(ArticuloSupermercado.ID, articuloSupermercado.getId());
            values.put(ArticuloSupermercado.ARTICULO, articuloSupermercado.getArticulo());
            values.put(ArticuloSupermercado.SUPERMERCADO, articuloSupermercado.getSupermercado());
            values.put(ArticuloSupermercado.PRECIO, articuloSupermercado.getPrecio());

            long ident = db.insert("ARTICULO_SUPERMERCADO", null, values);

            if (ident != -1) {
                articuloSupermercado.setId((int)ident);
            }

            db.close();
            return (int)ident;
        }
    }

    public Integer insertarArticuloSupermercado(int articulo, String supermercado){
        if(estaArticuloSupermercado(articulo, supermercado))
            return -1;
        else{
            SQLiteDatabase db = this.obtenerManejadorEscritura();
            ContentValues values = new ContentValues();

            values.put(ArticuloSupermercado.ARTICULO, articulo);
            values.put(ArticuloSupermercado.SUPERMERCADO, supermercado);

            long ident = db.insert("ARTICULO_SUPERMERCADO", null, values);

            db.close();
            return (int)ident;
        }
    }

    public Integer insertarArticuloSupermercado(String nombre, String marca, String supermercado){
        Articulo art = obtenerArticulo(nombre, marca);
        if(art == null){
            int id = insertarArticulo(nombre, marca);
            if (id != -1)
                return insertarArticuloSupermercado(id, supermercado);
            else
                return -1;
        }
        else{
            return insertarArticuloSupermercado(art.getId(), supermercado);
        }
    }

    public Integer insertarArticuloSupermercado(String nombre, String marca, String tipo, String supermercado){
        Articulo art = obtenerArticulo(nombre, marca, tipo);
        if(art == null){
            int id = insertarArticulo(nombre, marca, tipo);
            if (id != -1)
                return insertarArticuloSupermercado(id, supermercado);
            else
                return -1;
        } else {
            return insertarArticuloSupermercado(art.getId(), supermercado);
        }
    }

    public Integer insertarArticuloSupermercado(int articulo, String supermercado, float precio){
        if(estaArticuloSupermercado(articulo, supermercado))
            return -1;
        else{
            SQLiteDatabase db = this.obtenerManejadorEscritura();
            ContentValues values = new ContentValues();

            values.put(ArticuloSupermercado.ARTICULO, articulo);
            values.put(ArticuloSupermercado.SUPERMERCADO, supermercado);
            values.put(ArticuloSupermercado.PRECIO, precio);

            long ident = db.insert("ARTICULO_SUPERMERCADO", null, values);

            db.close();
            return (int)ident;
        }
    }

    public Integer insertarArticuloSupermercado(String nombre, String marca, String supermercado, float precio){
        Articulo art = obtenerArticulo(nombre, marca);
        if(art == null){
            int id = insertarArticulo(nombre, marca);
            if (id != -1)
                return insertarArticuloSupermercado(id, supermercado, precio);
            else
                return -1;
        } else {
            return insertarArticuloSupermercado(art.getId(), supermercado, precio);
        }
    }

    public Integer insertarArticuloSupermercado(String nombre, String marca, String tipo, String supermercado, float precio){
        Articulo art = obtenerArticulo(nombre, marca, tipo);
        if(art == null){
            int id = insertarArticulo(nombre, marca, tipo);
            if (id != -1)
                return insertarArticuloSupermercado(id, supermercado, precio);
            else
                return -1;
        } else {
            return insertarArticuloSupermercado(art.getId(), supermercado, precio);
        }
    }

    //ARTICULO_SUPERMERCADO - UPDATE

    public boolean modificarArticuloSupermercado(ArticuloSupermercado articuloSupermercado){
        int filaActu = 0;
        if(estaArticuloSupermercado(articuloSupermercado.getId())){

            SQLiteDatabase db = this.obtenerManejadorEscritura();

            ContentValues valores = new ContentValues();
            valores.put(ArticuloSupermercado.ID, articuloSupermercado.getId());
            valores.put(ArticuloSupermercado.ARTICULO, articuloSupermercado.getArticulo());
            valores.put(ArticuloSupermercado.SUPERMERCADO, articuloSupermercado.getSupermercado());
            valores.put(ArticuloSupermercado.PRECIO, articuloSupermercado.getPrecio());

            filaActu = db.update("ARTICULO_SUPERMERCADO", valores, ArticuloSupermercado.ID + " = ?", new String[]{Integer.toString(articuloSupermercado.getId())});
            db.close();
        }
        return filaActu > 0;
    }

    public boolean modificarArticuloSupermercadoArticulo(ArticuloSupermercado articuloSupermercado, int articulo){
        int filaActu = 0;
        if(estaArticuloSupermercado(articuloSupermercado.getId())){

            SQLiteDatabase db = this.obtenerManejadorEscritura();

            ContentValues valores = new ContentValues();
            valores.put(ArticuloSupermercado.ARTICULO, articulo);

            filaActu = db.update("ARTICULO_SUPERMERCADO", valores, ArticuloSupermercado.ID + " = ?", new String[]{Integer.toString(articuloSupermercado.getId())});
            db.close();
        }
        return filaActu > 0;
    }

    public boolean modificarArticuloSupermercadoSupermercado(ArticuloSupermercado articuloSupermercado, String supermercado){
        int filaActu = 0;
        if(estaArticuloSupermercado(articuloSupermercado.getId())){

            SQLiteDatabase db = this.obtenerManejadorEscritura();

            ContentValues valores = new ContentValues();
            valores.put(ArticuloSupermercado.SUPERMERCADO, supermercado);

            filaActu = db.update("ARTICULO_SUPERMERCADO", valores, ArticuloSupermercado.ID + " = ?", new String[]{Integer.toString(articuloSupermercado.getId())});
            db.close();
        }
        return filaActu > 0;
    }

    public boolean modificarArticuloSupermercadoPrecio(ArticuloSupermercado articuloSupermercado, float precio){
        int filaActu = 0;
        if(estaArticuloSupermercado(articuloSupermercado.getId())){

            SQLiteDatabase db = this.obtenerManejadorEscritura();

            ContentValues valores = new ContentValues();
            valores.put(ArticuloSupermercado.PRECIO, precio);

            filaActu = db.update("ARTICULO_SUPERMERCADO", valores, ArticuloSupermercado.ID + " = ?", new String[]{Integer.toString(articuloSupermercado.getId())});
            db.close();
        }
        return filaActu > 0;
    }

    //ARTICULO_SUPERMERCADO - DELETE

    public boolean eliminarArticuloSupermercado(ArticuloSupermercado articuloSupermercado){
        int filaBorrada = 0;

        if(estaArticuloSupermercado(articuloSupermercado.getId())){
            SQLiteDatabase db = this.obtenerManejadorEscritura();
            filaBorrada = db.delete("ARTICULO_SUPERMERCADO", ArticuloSupermercado.ID + " = ?", new String[]{Integer.toString(articuloSupermercado.getId())});
            db.close();
        }

        return filaBorrada > 0;
    }

    public boolean eliminarArticuloSupermercado(int articulo, String supermercado){
        int filaBorrada = 0;

        if(estaArticuloSupermercado(articulo, supermercado)){
            SQLiteDatabase db = this.obtenerManejadorEscritura();
            filaBorrada = db.delete("ARTICULO_SUPERMERCADO", ArticuloSupermercado.ARTICULO + " = ? AND " + ArticuloSupermercado.SUPERMERCADO + " = ?",
                    new String[]{Integer.toString(articulo), supermercado});
            db.close();
        }

        return filaBorrada > 0;
    }

    public boolean eliminarArticuloSupermercado(String nombre, String marca, String supermercado){
        int filaBorrada = 0;

        if(estaArticuloSupermercado(nombre, marca, supermercado)){
            Articulo articulo = this.obtenerArticulo(nombre, marca);
            SQLiteDatabase db = this.obtenerManejadorEscritura();
            filaBorrada = db.delete("ARTICULO_SUPERMERCADO", ArticuloSupermercado.ARTICULO + " = ? AND " + ArticuloSupermercado.SUPERMERCADO + " = ?",
                    new String[]{Integer.toString(articulo.getId()), supermercado});
            db.close();
        }

        return filaBorrada > 0;
    }

    //-------------------------------------------------------------------------------------------

    //LISTA (NOMBRE, FECHACREACION, FECHAMODIFICACION, SUPERMERCADO - FK(SUPERMERCADO))

    //LISTA - GET

    public List<Lista> obtenerListasOrdenadas(String orderBy){
        ArrayList<Lista> listas = new ArrayList<Lista>();
        String query = "SELECT * FROM LISTA";

        String []args = null;

        if(orderBy != null) {
            args = new String[]{orderBy};
            query += " ORDER BY ?, Nombre ASC";
        }

        SQLiteDatabase db = this.obtenerManejadorLectura();
        Cursor cursor = db.rawQuery(query, args);

        if (cursor.moveToFirst()) {
            do {
                Lista lista = new Lista(cursor.getString(cursor.getColumnIndex(Lista.NOMBRE)),
                        cursor.getString(cursor.getColumnIndex(Lista.FECHA_CREACION)),
                        cursor.getString(cursor.getColumnIndex(Lista.FECHA_MODIFICACION)),
                        cursor.getString(cursor.getColumnIndex(Lista.SUPERMERCADO)));

                listas.add(lista);
            } while (cursor.moveToNext());
        }

        Log.d("obtenerListas()", "size: " + listas.size());

        db.close();
        return listas;
    }

    public List<Lista> obtenerListas(){
        return obtenerListasOrdenadas(null);
    }

    public Lista obtenerLista(String nombre){
        Lista lista = null;
        String query = "SELECT * FROM LISTA WHERE " + Lista.NOMBRE + " = ?";

        SQLiteDatabase lectura = this.obtenerManejadorLectura();
        Cursor cursor = lectura.rawQuery(query, new String[]{nombre});

        if(cursor.moveToFirst()){
            lista = new Lista(cursor.getString(cursor.getColumnIndex(Lista.NOMBRE)),
                    cursor.getString(cursor.getColumnIndex(Lista.FECHA_CREACION)),
                    cursor.getString(cursor.getColumnIndex(Lista.FECHA_MODIFICACION)),
                    cursor.getString(cursor.getColumnIndex(Lista.SUPERMERCADO)));
        }
        lectura.close();
        return lista;
    }

    public List<Lista> obtenerListasPorSupermercado(String supermercado){
        ArrayList<Lista> listas = new ArrayList<Lista>();
        String query = "SELECT * FROM LISTA WHERE " + Lista.SUPERMERCADO + " = ?";

        SQLiteDatabase db = this.obtenerManejadorLectura();
        Cursor cursor = db.rawQuery(query, new String[]{supermercado});

        if (cursor.moveToFirst()) {
            do {
                Lista lista = new Lista(cursor.getString(cursor.getColumnIndex(Lista.NOMBRE)),
                        cursor.getString(cursor.getColumnIndex(Lista.FECHA_CREACION)),
                        cursor.getString(cursor.getColumnIndex(Lista.FECHA_MODIFICACION)),
                        cursor.getString(cursor.getColumnIndex(Lista.SUPERMERCADO)));

                listas.add(lista);
            } while (cursor.moveToNext());
        }
        db.close();
        return listas;
    }

    //LISTA - EXIST

    public boolean estaLista(String nombre){
        return obtenerLista(nombre) != null;
    }

    //LISTA - INSERT

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

    public boolean insertarLista(String nombre){
        String fecha = Util.diaMesAnyo.format(new Date());
        boolean ok = false;

        if(!estaLista(nombre)) {

            SQLiteDatabase db = this.obtenerManejadorEscritura();
            ContentValues values = new ContentValues();

            values.put(Lista.NOMBRE, nombre);
            values.put(Lista.FECHA_CREACION, fecha);
            values.put(Lista.FECHA_MODIFICACION, fecha);

            db.insert("LISTA", null, values);
            db.close();
            ok = true;
        }
        return ok;
    }

    public boolean insertarLista(String nombre, String supermercado){
        String fecha = Util.diaMesAnyo.format(new Date());
        boolean ok = false;

        if(!estaLista(nombre)) {

            SQLiteDatabase db = this.obtenerManejadorEscritura();
            ContentValues values = new ContentValues();

            values.put(Lista.NOMBRE, nombre);
            values.put(Lista.FECHA_CREACION, fecha);
            values.put(Lista.FECHA_MODIFICACION, fecha);
            values.put(Lista.SUPERMERCADO, supermercado);

            db.insert("LISTA", null, values);
            db.close();
            ok = true;
        }
        return ok;
    }

    //LISTA - UPDATE

    public boolean modificarLista(Lista lista){
        int filaActu = 0;
        if(estaLista(lista.getNombre())){

            SQLiteDatabase db = this.obtenerManejadorEscritura();

            ContentValues valores = new ContentValues();
            valores.put(Lista.NOMBRE, lista.getNombre());
            valores.put(Lista.FECHA_CREACION, lista.getFechaCreacion());
            valores.put(Lista.FECHA_MODIFICACION, lista.getFechaModificacion());
            valores.put(Lista.SUPERMERCADO, lista.getSupermercado());

            filaActu = db.update("LISTA", valores, Lista.NOMBRE + " = ?", new String[]{lista.getNombre()});
            db.close();
        }
        return filaActu > 0;
    }

    public boolean modificarListaNombre(Lista lista, String nombre){
        int filaActu = 0;
        if(estaLista(lista.getNombre())){

            SQLiteDatabase db = this.obtenerManejadorEscritura();

            ContentValues valores = new ContentValues();
            valores.put(Lista.NOMBRE, nombre);

            filaActu = db.update("LISTA", valores, Lista.NOMBRE + " = ?", new String[]{lista.getNombre()});
            db.close();
        }
        return filaActu > 0;
    }

    public boolean modificarListaFechaCreacion(Lista lista, String fechaCreacion){
        int filaActu = 0;
        if(estaLista(lista.getNombre())){

            SQLiteDatabase db = this.obtenerManejadorEscritura();

            ContentValues valores = new ContentValues();
            valores.put(Lista.FECHA_CREACION, fechaCreacion);

            filaActu = db.update("LISTA", valores, Lista.NOMBRE + " = ?", new String[]{lista.getNombre()});
            db.close();
        }
        return filaActu > 0;
    }

    public boolean modificarListaFechaModificacion(Lista lista, String fechaModificacion){
        int filaActu = 0;
        if(estaLista(lista.getNombre())){

            SQLiteDatabase db = this.obtenerManejadorEscritura();

            ContentValues valores = new ContentValues();
            valores.put(Lista.FECHA_MODIFICACION, fechaModificacion);

            filaActu = db.update("LISTA", valores, Lista.NOMBRE + " = ?", new String[]{lista.getNombre()});
            db.close();
        }
        return filaActu > 0;
    }

    public boolean modificarListaSupermercado(Lista lista, String supermercado){
        int filaActu = 0;
        if(estaLista(lista.getNombre())){

            SQLiteDatabase db = this.obtenerManejadorEscritura();

            ContentValues valores = new ContentValues();
            valores.put(Lista.SUPERMERCADO, supermercado);

            filaActu = db.update("LISTA", valores, Lista.NOMBRE + " = ?", new String[]{lista.getNombre()});
            db.close();
        }
        return filaActu > 0;
    }

    //LISTA - DELETE

    public boolean eliminarLista(Lista lista){
        int filaBorrada = 0;

        if(estaLista(lista.getNombre())){
            SQLiteDatabase db = this.obtenerManejadorEscritura();
            filaBorrada = db.delete("LISTA", Lista.NOMBRE + " = ?", new String[]{lista.getNombre()});
            db.close();
        }

        return filaBorrada > 0;
    }

    public boolean eliminarLista(String nombre){
        int filaBorrada = 0;

        if(estaLista(nombre)){
            SQLiteDatabase db = this.obtenerManejadorEscritura();
            filaBorrada = db.delete("LISTA", Lista.NOMBRE + " = ?", new String[]{nombre});
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
            db.delete("LISTA", Lista.NOMBRE + " = ?", new String[]{lista.getNombre()});
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

    //-------------------------------------------------------------------------------------------

    //LISTA_ARTICULO (ARTICULO - FK(ARTICULO_SUPERMERCADO), NOMBRE - FK(LISTA), CANTIDAD)

    //LISTA_ARTICULO - GET

    public List<ListaArticulo> obtenerListasArticulos(){
        ArrayList<ListaArticulo> listas = new ArrayList<ListaArticulo>();
        String query = "SELECT * FROM LISTA_ARTICULO";

        SQLiteDatabase db = this.obtenerManejadorLectura();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                ListaArticulo la = new ListaArticulo(cursor.getInt(cursor.getColumnIndex(ListaArticulo.ARTICULO)),
                        cursor.getString(cursor.getColumnIndex(ListaArticulo.NOMBRE)),
                        cursor.getInt(cursor.getColumnIndex(ListaArticulo.CANTIDAD)));

                listas.add(la);
            } while (cursor.moveToNext());
        }

        Log.d("obtenerListas()", "size: " + listas.size());

        db.close();
        return listas;
    }

    public ListaArticulo obtenerListaArticulo(int idArticulo, String nombreLista){
        ListaArticulo la = null;

        String query = "SELECT * FROM LISTA_ARTICULO WHERE " + ListaArticulo.ARTICULO + " = ? AND "
                + ListaArticulo.NOMBRE + " = ?";
        SQLiteDatabase db = this.obtenerManejadorLectura();
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(idArticulo), nombreLista});
        if(cursor.moveToFirst()){
            la = new ListaArticulo(cursor.getInt(cursor.getColumnIndex(ListaArticulo.ARTICULO)),
                    cursor.getString(cursor.getColumnIndex(ListaArticulo.NOMBRE)),
                    cursor.getInt(cursor.getColumnIndex(ListaArticulo.CANTIDAD)));
        }
        db.close();

        return la;
    }

    public List<ArticuloSupermercado> obtenerArticulosEnLista(Lista lista){
        return obtenerArticulosEnLista(lista.getNombre());
    }

    public List<ArticuloSupermercado> obtenerArticulosEnLista(String nombre){
        ArrayList<ArticuloSupermercado> articulos = new ArrayList();
        String query = "SELECT * FROM LISTA_ARTICULO WHERE " + ListaArticulo.NOMBRE + " = ?";

        SQLiteDatabase db = this.obtenerManejadorLectura();
        Cursor cursor = db.rawQuery(query, new String[]{nombre});

        if (cursor.moveToFirst()) {
            do {
                ArticuloSupermercado art = obtenerArticuloSupermercado(cursor.getInt(cursor.getColumnIndex(ListaArticulo.ARTICULO)));
                articulos.add(art);
            } while (cursor.moveToNext());
        }

        Log.d("obtenerArticulosEnLista", articulos.toString());
        db.close();
        return articulos;
    }

    public List<ArticuloSupermercado> obtenerArticulosEnLista(Lista lista, String orderBy) {
        String nombre = lista.getNombre();
        ArrayList<ArticuloSupermercado> articulos = new ArrayList();
        String query = "SELECT * FROM LISTA_ARTICULO AS LA JOIN ARTICULO_SUPERMERCADO AS AR ON LA.Articulo = AR.Id " +
                "JOIN ARTICULO AS A ON AR.Articulo = A.ID WHERE LA.NOMBRE = ? ORDER BY A.NOMBRE " + orderBy;

        SQLiteDatabase db = this.obtenerManejadorLectura();
        Cursor cursor = db.rawQuery(query, new String[]{nombre});

        if (cursor.moveToFirst()) {
            do {
                ArticuloSupermercado art = obtenerArticuloSupermercado(cursor.getInt(cursor.getColumnIndex(ListaArticulo.ARTICULO)));
                articulos.add(art);
            } while (cursor.moveToNext());
        }

        Log.d("obtenerArticulosEnLista", articulos.toString());
        db.close();
        return articulos;
    }

    public int obtenerCantidadArticuloEnLista(int articulo, Lista lista){
        return obtenerCantidadArticuloEnLista(articulo, lista.getNombre());
    }

    public int obtenerCantidadArticuloEnLista(int articulo, String nombre){
        int cantidad = -1;
        String query = "SELECT CANTIDAD FROM LISTA_ARTICULO WHERE " + ListaArticulo.NOMBRE + " = ? AND " + ListaArticulo.ARTICULO + " = ?";

        SQLiteDatabase lectura = this.obtenerManejadorLectura();
        Cursor cursor = lectura.rawQuery(query, new String[]{nombre,Integer.toString(articulo)});

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

    //LISTA_ARTICULO - EXIST

    public boolean estaArticuloEnLista(int articulo, String lista){
        String query = "SELECT * FROM LISTA_ARTICULO WHERE " + ListaArticulo.ARTICULO + " = ? AND " + ListaArticulo.NOMBRE + " = ?";

        SQLiteDatabase lectura = this.obtenerManejadorLectura();
        Cursor cursor = lectura.rawQuery(query, new String[]{Integer.toString(articulo), lista});

        boolean ok = cursor.moveToFirst();
        lectura.close();

        return ok;
    }

    //LISTA_ARTICULO - INSERT

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

    public boolean insertarArticuloEnLista(int articulo, String lista, int cantidad){
        boolean ok = false;

        if(!estaArticuloEnLista(articulo, lista)) {

            SQLiteDatabase db = this.obtenerManejadorEscritura();
            ContentValues values = new ContentValues();

            values.put(ListaArticulo.ARTICULO, articulo);
            values.put(ListaArticulo.NOMBRE, lista);
            values.put(ListaArticulo.CANTIDAD, cantidad);

            db.insert("LISTA_ARTICULO", null, values);
            db.close();
            ok = true;
        }
        return ok;
    }

    //LISTA_ARTICULO - UPDATE

    public boolean modificarArticuloEnLista(ListaArticulo listaArticulo){
        int filaActu = 0;
        if(estaArticuloEnLista(listaArticulo.getArticulo(), listaArticulo.getNombre())) {

            SQLiteDatabase db = this.obtenerManejadorEscritura();

            ContentValues valores = new ContentValues();
            valores.put(ListaArticulo.ARTICULO, listaArticulo.getArticulo());
            valores.put(ListaArticulo.NOMBRE, listaArticulo.getNombre());
            valores.put(ListaArticulo.CANTIDAD, listaArticulo.getCantidad());

            filaActu = db.update("LISTA_ARTICULO", valores, ListaArticulo.ARTICULO + " = ? AND " + ListaArticulo.NOMBRE + " = ?", new String[]{Integer.toString(listaArticulo.getArticulo()), listaArticulo.getNombre()});
            db.close();
        }
        return filaActu > 0;
    }

    public boolean modificarArticuloEnListaCantidad(ListaArticulo listaArticulo, int cantidad){
        int filaActu = 0;
        if(estaArticuloEnLista(listaArticulo.getArticulo(), listaArticulo.getNombre())) {

            SQLiteDatabase db = this.obtenerManejadorEscritura();

            ContentValues valores = new ContentValues();
            valores.put(ListaArticulo.CANTIDAD, cantidad);

            filaActu = db.update("LISTA_ARTICULO", valores, ListaArticulo.ARTICULO + " = ? AND " + ListaArticulo.NOMBRE + " = ?", new String[]{Integer.toString(listaArticulo.getArticulo()), listaArticulo.getNombre()});
            db.close();
        }
        return filaActu > 0;
    }

    //LISTA_ARTICULO - DELETE

    public boolean eliminarArticuloEnLista(ListaArticulo listaArticulo){
        int filaBorrada = 0;

        if(estaArticuloEnLista(listaArticulo.getArticulo(), listaArticulo.getNombre())) {
            SQLiteDatabase db = this.obtenerManejadorEscritura();
            filaBorrada = db.delete("LISTA_ARTICULO", ListaArticulo.ARTICULO + "=? AND " + ListaArticulo.NOMBRE + "=?", new String[]{Integer.toString(listaArticulo.getArticulo()), listaArticulo.getNombre()});
            db.close();
        }

        return filaBorrada > 0;
    }

    public boolean eliminarArticuloEnLista(int articulo, String lista){
        int filaBorrada = 0;

        if(estaArticuloEnLista(articulo, lista)) {
            SQLiteDatabase db = this.obtenerManejadorEscritura();
            filaBorrada = db.delete("LISTA_ARTICULO", ListaArticulo.ARTICULO + " = ? AND " + ListaArticulo.NOMBRE + " = ?", new String[]{Integer.toString(articulo), lista});
            db.close();
        }

        return filaBorrada > 0;
    }

    //-------------------------------------------------------------------------------------------

    //STOCK (ARTICULO - FK(ARTICULO), MINIMO, CANTIDAD)

    //STOCK - GET

    public List<Stock> obtenerStocks(){
        ArrayList<Stock> lista = new ArrayList<Stock>();
        String query = "SELECT * FROM STOCK AS S JOIN ARTICULO AS A ON S.Articulo = A.Id ORDER BY A.Nombre";

        SQLiteDatabase db = this.obtenerManejadorLectura();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Stock stock = new Stock(cursor.getInt(cursor.getColumnIndex(Stock.ARTICULO)),
                        cursor.getInt(cursor.getColumnIndex(Stock.CANTIDAD)),
                        cursor.getInt(cursor.getColumnIndex(Stock.MINIMO)));

                lista.add(stock);
            } while (cursor.moveToNext());
        }

        Log.d("obtenerStocks", lista.toString());
        db.close();
        return lista;
    }

    public Stock obtenerStock(int articulo){
        Stock stock = null;
        String query = "SELECT * FROM STOCK WHERE " + Stock.ARTICULO + " = ?";
        SQLiteDatabase lectura = this.obtenerManejadorLectura();

        Cursor cursor = lectura.rawQuery(query, new String[]{Integer.toString(articulo)});

        if(cursor.moveToFirst()){
            stock = new Stock(cursor.getInt(cursor.getColumnIndex(Stock.ARTICULO)),
                    cursor.getInt(cursor.getColumnIndex(Stock.CANTIDAD)),
                    cursor.getInt(cursor.getColumnIndex(Stock.MINIMO)));
        }
        lectura.close();
        return stock;
    }

    public Stock obtenerStock(String nombre, String marca){
        Articulo art = obtenerArticulo(nombre, marca);
        if(art != null)
            return obtenerStock(art.getId());
        else
            return null;
    }

    public Stock obtenerStock(Articulo articulo){
        return obtenerStock(articulo.getId());
    }

    //STOCK - EXIST

    public boolean estaStock(int id){
        return obtenerStock(id) != null;
    }

    public boolean estaStock(String nombre, String marca){
        Articulo art = obtenerArticulo(nombre, marca);
        if(art != null)
            return estaStock(art.getId());
        else
            return false;
    }

    public boolean estaStockEnListaCompra(Stock stock){
        return estaStockEnListaCompra(stock.getArticulo());
    }

    public boolean estaStockEnListaCompra(int articulo){
        List<Lista> listas = obtenerListas();

        for (Lista lista : listas) {

            for (ArticuloSupermercado art : obtenerArticulosEnLista(lista)) {
                if (art.getArticulo() == articulo)
                    return true;
            }

        }

        return false;
    }

    //STOCK - INSERT

    public boolean insertarStock(Stock stock){
        boolean ok = false;

        if(!estaStock(stock.getArticulo())) {

            SQLiteDatabase db = this.obtenerManejadorEscritura();
            ContentValues values = new ContentValues();

            values.put(Stock.ARTICULO, stock.getArticulo());
            values.put(Stock.CANTIDAD, stock.getCantidad());
            values.put(Stock.MINIMO, stock.getMinimo());

            db.insert("STOCK", null, values);
            db.close();
            ok = true;
        }
        return ok;
    }

    public boolean insertarStock(int articulo, int cantidad, int minimo){
        if(!estaStock(articulo)) {

            SQLiteDatabase db = this.obtenerManejadorEscritura();
            ContentValues values = new ContentValues();

            values.put(Stock.ARTICULO, articulo);
            values.put(Stock.CANTIDAD, cantidad);
            values.put(Stock.MINIMO, minimo);

            db.insert("STOCK", null, values);
            db.close();
            return true;
        }
        else
            return false;
    }

    public boolean insertarStock(String nombre, String marca, int cantidad, int minimo){
        Articulo art = obtenerArticulo(nombre, marca);
        if(art != null) {
            if (!estaStock(art.getId())) {

                SQLiteDatabase db = this.obtenerManejadorEscritura();
                ContentValues values = new ContentValues();

                values.put(Stock.ARTICULO, art.getId());
                values.put(Stock.CANTIDAD, cantidad);
                values.put(Stock.MINIMO, minimo);

                db.insert("STOCK", null, values);
                db.close();
                return true;
            } else
                return false;
        } else {
            int id = insertarArticulo(nombre, marca);
            if(id != -1)
                return insertarStock(id, cantidad, minimo);
            else
                return false;

        }
    }

    public boolean insertarStock(String nombre, String marca, String tipo, int cantidad, int minimo){
        Articulo art = obtenerArticulo(nombre, marca, tipo);
        if(art != null) {
            if (!estaStock(art.getId())) {

                SQLiteDatabase db = this.obtenerManejadorEscritura();
                ContentValues values = new ContentValues();

                values.put(Stock.ARTICULO, art.getId());
                values.put(Stock.CANTIDAD, cantidad);
                values.put(Stock.MINIMO, minimo);

                db.insert("STOCK", null, values);
                db.close();
                return true;
            } else
                return false;
        } else {
            int id = insertarArticulo(nombre, marca, tipo);
            if(id != -1)
                return insertarStock(id, cantidad, minimo);
            else
                return false;

        }
    }

    //STOCK - UPDATE

    public boolean modificarStock(Stock stock){
        int filaActu = 0;
        if(estaStock(stock.getArticulo())){

            SQLiteDatabase db = this.obtenerManejadorEscritura();

            ContentValues valores = new ContentValues();
            valores.put(Stock.ARTICULO, stock.getArticulo());
            valores.put(Stock.CANTIDAD, stock.getCantidad());
            valores.put(Stock.MINIMO, stock.getMinimo());

            filaActu = db.update("STOCK", valores, Stock.ARTICULO + "=?", new String[]{Integer.toString(stock.getArticulo())});
            db.close();
        }
        return filaActu > 0;
    }

    public boolean modificarStockCantidad(Stock stock, int cantidad){
        int filaActu = 0;
        if(estaStock(stock.getArticulo())){

            SQLiteDatabase db = this.obtenerManejadorEscritura();

            ContentValues valores = new ContentValues();
            valores.put(Stock.CANTIDAD, cantidad);

            filaActu = db.update("STOCK", valores, Stock.ARTICULO + "=?", new String[]{Integer.toString(stock.getArticulo())});
            db.close();
        }
        return filaActu > 0;
    }

    public boolean modificarStockMinimo(Stock stock, int minimo){
        int filaActu = 0;
        if(estaStock(stock.getArticulo())){

            SQLiteDatabase db = this.obtenerManejadorEscritura();

            ContentValues valores = new ContentValues();
            valores.put(Stock.MINIMO, minimo);

            filaActu = db.update("STOCK", valores, Stock.ARTICULO + "=?", new String[]{Integer.toString(stock.getArticulo())});
            db.close();
        }
        return filaActu > 0;
    }

    //STOCK - DELETE

    public boolean eliminarStock(Stock stock){
        int filaBorrada = 0;

        if(estaStock(stock.getArticulo())){
            SQLiteDatabase db = this.obtenerManejadorEscritura();
            filaBorrada = db.delete("STOCK", Stock.ARTICULO + " = ?", new String[]{Integer.toString(stock.getArticulo())});
            db.close();
        }

        return filaBorrada > 0;
    }

    public boolean eliminarStock(int id){
        int filaBorrada = 0;

        if(estaStock(id)){
            SQLiteDatabase db = this.obtenerManejadorEscritura();
            filaBorrada = db.delete("STOCK", Stock.ARTICULO + " = ?", new String[]{Integer.toString(id)});
            db.close();
        }

        return filaBorrada > 0;
    }

    public boolean eliminarStock(String nombre, String marca){
        int filaBorrada = 0;

        if(estaStock(nombre, marca)){
            SQLiteDatabase db = this.obtenerManejadorEscritura();
            filaBorrada = db.delete("STOCK", Stock.ARTICULO + " = ?", new String[]{Integer.toString(obtenerArticulo(nombre, marca).getId())});
            db.close();
        }

        return filaBorrada > 0;
    }

}
