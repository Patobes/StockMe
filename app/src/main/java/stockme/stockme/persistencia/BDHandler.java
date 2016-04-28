package stockme.stockme.persistencia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import stockme.stockme.logica.ArticuloSupermercado;
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
         */
        String articulo = "CREATE TABLE `ARTICULO` (" +
                "`Id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "`Nombre` TEXT NOT NULL," +
                "`Marca` TEXT," +
                "`Tipo` TEXT DEFAULT 'Cualquiera'," +
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
                "FOREIGN KEY(`Articulo`) REFERENCES `ARTICULO`(`Id`)," +
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
                "`Minimo` INTEGER NOT NULL DEFAULT 0," +
                "`Cantidad` INTEGER NOT NULL DEFAULT 0," +
                "PRIMARY KEY(Articulo)," +
                "FOREIGN KEY(`Articulo`) REFERENCES `ARTICULO`(`Id`)" +
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
                "PRIMARY KEY(Nombre)," +
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
                "FOREIGN KEY(`Articulo`) REFERENCES `ARTICULO_SUPERMERCADO`(`Id`)," +
                "FOREIGN KEY(`Nombre`) REFERENCES `LISTA`(`Nombre`)" +
                ")";
        db.execSQL("DROP TABLE IF EXISTS 'LISTA_ARTICULO';");
        db.execSQL(listaArticulo);

        //ELEMENTOS INICIALES
        db.execSQL("INSERT INTO `ARTICULO` VALUES(1, 'Leche', 'Hacendado', 'Lácteos');");
        db.execSQL("INSERT INTO `ARTICULO` VALUES(2, 'Pizza', 'Hacendado', 'Congelados');");
        db.execSQL("INSERT INTO `ARTICULO` VALUES(3, 'Ketchup', 'Heinz', 'Salsas');");
        db.execSQL("INSERT INTO `ARTICULO` VALUES(4, 'Mostaza', 'Heinz', 'Salsas');");

        db.execSQL("INSERT INTO `SUPERMERCADO` VALUES('Cualquiera');");
        db.execSQL("INSERT INTO `SUPERMERCADO` VALUES('Día');");
        db.execSQL("INSERT INTO `SUPERMERCADO` VALUES('Mercadona');");
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

        db.execSQL("INSERT INTO 'STOCK' VALUES (1, 1, 4)");
        db.execSQL("INSERT INTO 'STOCK' VALUES (2, 1, 5)");
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


    //FUNCIONES EXTRAS
    public List<String> obtenerMarcas(){
        ArrayList<String> marcas = new ArrayList<>();

        String query = "SELECT DISTINCT " + Articulo.MARCA + " FROM ARTICULO";
        SQLiteDatabase db = this.obtenerManejadorLectura();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            do{
                String marca = cursor.getString(0);
                if(marca != null && !marca.isEmpty())
                    marcas.add(marca);
            }while(cursor.moveToNext());
        }
        db.close();

        return marcas;
    }


    /*
    LISTA
    LISTA_ARTICULO_SUPERMERCADO (LISTA, SUPER, ARTICULO_SUPERMERCADO)
    STOCK (ARTICULO)
     */

    //ARTICULO (ID, NOMBRE, MARCA, TIPO)

    //ARTICULO - GET

    //Obtiene todos los articulos de la BD, devuelve un List<Articulo>
    public List<Articulo> obtenerArticulos(){
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

    //Obtiene el articulo con el id indicado
    public Articulo obtenerArticulo(int id){
        Articulo articulo = null;
        String query = "SELECT * FROM ARTICULO WHERE " + Articulo.ID + " = ?";
        SQLiteDatabase lectura = this.obtenerManejadorLectura();

        Cursor cursor = lectura.rawQuery(query, new String[]{Integer.toString(id)});

        if(cursor.moveToFirst()){
            articulo = new Articulo(cursor.getInt(cursor.getColumnIndex(Articulo.ID)),
                    cursor.getString(cursor.getColumnIndex(Articulo.NOMBRE)),
                    cursor.getString(cursor.getColumnIndex(Articulo.MARCA)),
                    cursor.getString(cursor.getColumnIndex(Articulo.TIPO)));
        }
        lectura.close();
        return articulo;
    }

    public Articulo obtenerArticulo(String nombre, String marca){
        Articulo articulo = null;
        String query = "SELECT * FROM ARTICULO WHERE " + Articulo.NOMBRE + " = ? AND " + Articulo.MARCA + " = ?";
        SQLiteDatabase lectura = this.obtenerManejadorLectura();

        Cursor cursor = lectura.rawQuery(query, new String[]{nombre, marca});

        if(cursor.moveToFirst()){
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

    //Obtiene todos los articulos con un nombre determinado
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

    //Obtiene todos los articulos con una marca determinada
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

    //Obtiene todos los articulos con un tipo determinado
    public List<Articulo> obtenerArticulosPorTipo(String tipo){
        ArrayList<Articulo> articulos = new ArrayList<Articulo>();

        String query = "SELECT * FROM ARTICULO WHERE " + Articulo.TIPO + " = ?";
        SQLiteDatabase lectura = this.obtenerManejadorLectura();

        Cursor cursor = lectura.rawQuery(query, new String[]{tipo});

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

    //Comprueba si esta un articulo de id indicado
    public boolean estaArticulo(int id){
        return obtenerArticulo(id) != null;
    }

    public boolean estaArticulo(String nombre, String marca){
        return obtenerArticulo(nombre, marca) != null;
    }

    //ARTICULO - INSERT

    //devuelve el id del articulo insertado o -1 si no se ha podido insertar
    public Integer insertarArticulo(Articulo articulo){
        if(estaArticulo(articulo.getNombre(), articulo.getMarca()))
            return -1;
        else{
            SQLiteDatabase db = this.obtenerManejadorEscritura();
            ContentValues values = new ContentValues();

            values.put(Articulo.ID, articulo.getId());
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
        /*
        Integer id = articulo.getId();

        //si es null significa que no está creado
        if(id == null) {
            SQLiteDatabase db = this.obtenerManejadorEscritura();
            ContentValues values = new ContentValues();

            values.put(Articulo.ID, articulo.getId());
            values.put(Articulo.NOMBRE, articulo.getNombre());
            values.put(Articulo.MARCA, articulo.getMarca());
            values.put(Articulo.TIPO, articulo.getTipo());

            long ident = db.insert("ARTICULO", null, values);

            if (ident != -1) {
                id = (int) ident;
                articulo.setId(id);
            }

            db.close();
            return id;
        }else{
            return null;
        }*/
    }

    public Integer insertarArticulos(String nombre, String marca){
        if(estaArticulo(nombre, marca))
            return -1;
            //return null;
        else{
            SQLiteDatabase db = this.obtenerManejadorEscritura();
            ContentValues values = new ContentValues();

            //values.put(Articulo.ID, (Integer)null);
            values.put(Articulo.NOMBRE, nombre);
            values.put(Articulo.MARCA, marca);
            //values.put(Articulo.TIPO, "Cualquiera");

            long ident = db.insert("ARTICULO", null, values);

            db.close();
            return (int)ident;
        }
    }

    public Integer insertarArticulo(String nombre, String marca, String tipo){
        SQLiteDatabase db = this.obtenerManejadorEscritura();
        ContentValues values = new ContentValues();

        //values.put(Articulo.ID, (Integer)null);
        values.put(Articulo.NOMBRE, nombre);
        values.put(Articulo.MARCA, marca);
        values.put(Articulo.TIPO, tipo);

        long ident = db.insert("ARTICULO", null, values);

        db.close();
        return (int)ident;
    }

    //ARTICULO - UPDATE

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

            filaActu = db.update("ARTICULO", valores, Articulo.ID + "=?", new String[]{Integer.toString(articulo.getId())});
            db.close();
        }
        return filaActu > 0;
    }

    //ARTICULO - DELETE

    //Elimina el articulo indicado, debe existir previamente
    public boolean eliminarArticulo(Articulo articulo){
        int filaBorrada = 0;

        if(estaArticulo(articulo.getId())){
            SQLiteDatabase db = this.obtenerManejadorEscritura();
            filaBorrada = db.delete("ARTICULO", Articulo.ID + " = ?", new String[]{Integer.toString(articulo.getId())});
            db.close();
        }

        return filaBorrada > 0;
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

    //Este debería reemplazar al metodo de arriba, pero puede dar fallo otras clases que utilicen directamente
    //la lista de strings en vez de objetos supermercado
    /*
    public List<Supermercado> obtenerSupermercados(){
        ArrayList<Supermercado> supermercados = new ArrayList<Supermercado>();
        String query = "SELECT * FROM SUPERMERCADO";

        SQLiteDatabase db = this.obtenerManejadorLectura();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Supermercado supermercado = new Supermercado(cursor.getString(cursor.getColumnIndex(Supermercado.NOMBRE)));
                supermercados.add(supermercado);
            } while (cursor.moveToNext());
        }

        Log.d("obtenerSupermercados", "size: " + supermercados.size());

        db.close();
        return supermercados;
    }
    */

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

    //Cambia el nombre del objeto Supermercado por el nuevo nombre (el de tipo String)
    public boolean modificarSupermercado(Supermercado supermercado, String nombre){
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
        int filaBorrada = 0;

        if(estaSupermercado(supermercado.getNombre())){
            SQLiteDatabase db = this.obtenerManejadorEscritura();
            filaBorrada = db.delete("SUPERMERCADP", Supermercado.NOMBRE + "=?", new String[]{supermercado.getNombre()});
            db.close();
        }

        return filaBorrada > 0;
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
        if(precio2 < precio1)
            minimo = precio2; maximo = precio1;
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

            //values.put(Articulo.ID, (Integer)null);
            values.put(ArticuloSupermercado.ARTICULO, articulo);
            values.put(ArticuloSupermercado.SUPERMERCADO, supermercado);
            //values.put(Articulo.TIPO, "Cualquiera");

            long ident = db.insert("ARTICULO_SUPERMERCADO", null, values);

            db.close();
            return (int)ident;
        }
    }

    public Integer insertarArticuloSupermercado(int articulo, String supermercado, float precio){
        if(estaArticuloSupermercado(articulo, supermercado))
            return -1;
        else{
            SQLiteDatabase db = this.obtenerManejadorEscritura();
            ContentValues values = new ContentValues();

            //values.put(Articulo.ID, (Integer)null);
            values.put(ArticuloSupermercado.ARTICULO, articulo);
            values.put(ArticuloSupermercado.SUPERMERCADO, supermercado);
            values.put(ArticuloSupermercado.PRECIO, precio);

            long ident = db.insert("ARTICULO_SUPERMERCADO", null, values);

            db.close();
            return (int)ident;
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

    //-------------------------------------------------------------------------------------------

    //LISTA (NOMBRE, FECHACREACION, FECHAMODIFICACION, SUPERMERCADO - FK(SUPERMERCADO))

    //LISTA - GET

    //LISTA - EXIST

    //LISTA - INSERT

    //LISTA - UPDATE

    //LISTA - DELETE

    //-------------------------------------------------------------------------------------------

    //LISTA_ARTICULO (ARTICULO - FK(ARTICULO_SUPERMERCADO), NOMBRE - FK(LISTA), CANTIDAD)

    //LISTA_ARTICULO - GET

    //LISTA_ARTICULO - EXIST

    //LISTA_ARTICULO - INSERT

    //LISTA_ARTICULO - UPDATE

    //LISTA_ARTICULO - DELETE

    //-------------------------------------------------------------------------------------------

    //STOCK (ARTICULO - FK(ARTICULO), MINIMO, CANTIDAD)

    //STOCK - GET

    public List<Stock> obtenerStocks(){
        ArrayList<Stock> lista = new ArrayList<Stock>();
        String query = "SELECT * FROM STOCK";

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
        return obtenerStock(obtenerArticulo(nombre, marca).getId());
    }

    /*
    public List<Stock> obtenerStockPorMinimo[Menor, Mayor, Igual, Entre](int minimo)
    public List<Stock> obtenerStockPorCantidad[Menor, Mayor, Igual, Entre](int cantidad)
     */

    //STOCK - EXIST

    public boolean estaStock(int id){
        return obtenerStock(id) != null;
    }

    public boolean estaStock(String nombre, String marca){
        return obtenerStock(nombre, marca) != null;
    }

    //STOCK - INSERT

    //este esta mal
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

    //STOCK - UPDATE

    //revisar este
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

    //-------------------------------------------------------------------------------------------

    //LISTA - GET

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

    //LISTAS - EXIST

    //Comprueba si esta una lista de nombre indicado
    public boolean estaLista(String nombre){
        String query = "SELECT * FROM LISTA WHERE " + Lista.NOMBRE + " = ?";

        SQLiteDatabase lectura = this.obtenerManejadorLectura();
        Cursor cursor = lectura.rawQuery(query, new String[]{nombre});

        boolean ok = cursor.moveToFirst();
        lectura.close();
        return ok;
    }

    //LISTAS - INSERT

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

    //LISTAS - SET

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

    //LISTAS - DELETE

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

    //LISTAARTICULO

    //Obtiene todas las listas de la BD, devuelve un List<Lista>
    public List<ListaArticulo> obtenerListaArticulos(){
        //1. crear lista a devolver
        ArrayList<ListaArticulo> listas = new ArrayList();
        String query = "SELECT * FROM LISTA_ARTICULO";

        // 2. obtener un manejador de bd
        SQLiteDatabase db = this.obtenerManejadorLectura();
        Cursor cursor = db.rawQuery(query, null);

        // 3. recorrer los elementos y añadirlos a la lista a devolver
        ListaArticulo la = null;
        if (cursor.moveToFirst()) {
            do {
                la = new ListaArticulo();
                la.setArticulo(cursor.getInt(cursor.getColumnIndex(ListaArticulo.ARTICULO)));
                la.setNombre(cursor.getString(cursor.getColumnIndex(ListaArticulo.NOMBRE)));
                la.setCantidad(cursor.getInt(cursor.getColumnIndex(ListaArticulo.CANTIDAD)));
                listas.add(la);
            } while (cursor.moveToNext());
        }

        Log.d("obtenerListas()", "size: " + listas.size());

        //4. cerrar la referencia a la bd y devolver la lista poblada
        db.close();
        return listas;
    }

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
}
