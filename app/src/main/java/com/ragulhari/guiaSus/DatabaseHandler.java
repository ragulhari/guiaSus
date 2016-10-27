package com.ragulhari.guiaSus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.ragulhari.guiaSus.listObjects.OpinionItem;

/**
* Classe criada com o objetivo de centralizar o uso do banco de dados interno pela aplicação.
* O banco de dados nesse aplicativo tem uma função específica de "cache", armazenando as avaliações já realizadas pelo usuário.
* O objetivo é evitar mais uma chamada de API caso o usuário acesse novamente o estabelecimento avaliado.
* */

class DatabaseHandler extends SQLiteOpenHelper {

    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String DATABASE_NAME = "GuiaSaude";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase db;

    public DatabaseHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }


    /**
     * Neste database, existe apenas uma tabela, para o registro das avaliações realizadas pelo usuário
     * @return String de criação da tabela
     */
    private String createTable()
    {
        return "CREATE TABLE " + Opinions.TABLE_NAME + " (" +
                        Opinions._ID + " INTEGER PRIMARY KEY," +
                        Opinions.CNES_ID + TEXT_TYPE + COMMA_SEP +
                        Opinions.USEREMAIL + TEXT_TYPE + COMMA_SEP +
                        Opinions.RATING + INT_TYPE + COMMA_SEP +
                        Opinions.OPINION_TEXT + TEXT_TYPE + ")";
    }

    private String dropTable()
    {
        return "DROP TABLE IF EXISTS " + Opinions.TABLE_NAME;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(createTable());
        db = sqLiteDatabase;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(dropTable());
        sqLiteDatabase.execSQL(createTable());
    }

    public void insertData(String pCnesId, String pUserEmail, int pRating, String pOpinionText){

        if (db == null){
            db = getWritableDatabase();
        }

        ContentValues objContent = new ContentValues();
        objContent.put(Opinions.CNES_ID, pCnesId);
        objContent.put(Opinions.USEREMAIL, pUserEmail);
        objContent.put(Opinions.RATING, pRating);
        objContent.put(Opinions.OPINION_TEXT, pOpinionText);

        db.insert(Opinions.TABLE_NAME,null,objContent);
    }

    /**
    * Neste aplicativo existe uma única consulta a banco de dados, que é verificar se uma opinião foi enviada para o aplicativo
    * e recuperar para exibição ao usuário (similar a um cache)
     * @param pCnesId código CNES do estabelecimento
     * @param pUserEmail E-mail do usuário logado no APP
     *
    * */
    public OpinionItem searchData(String pCnesId, String pUserEmail)
    {
        String[] args = new String[2];
        args[0] = pCnesId;
        args[1] = pUserEmail;

        if (db == null){
            db = getReadableDatabase();
        }

        String whereClause = Opinions.CNES_ID + " = ? AND " + Opinions.USEREMAIL + " = ?";
        Cursor c = db.query(Opinions.TABLE_NAME,null,whereClause,args,null,null,null);

        try {
            c.moveToFirst();
            return new OpinionItem(c.getString(c.getColumnIndexOrThrow(Opinions.CNES_ID)),
                    c.getString(c.getColumnIndexOrThrow(Opinions.USEREMAIL)),
                    c.getInt(c.getColumnIndexOrThrow(Opinions.RATING)),
                    c.getString(c.getColumnIndexOrThrow(Opinions.OPINION_TEXT)));

        }catch(Exception err){
            return null;
        }
        finally {
            c.close();
        }
    }

    /**
     * Classe descritora das colunas da tabela utilizada por esse projeto.
     */
    public static abstract class Opinions implements BaseColumns{

        public static final String TABLE_NAME = "Opinions";

        public static final String CNES_ID = "CnesId";
        public static final String RATING = "Rating";
        public static final String USEREMAIL = "UserEmail";
        public static final String OPINION_TEXT = "OpinionText";

    }


}
