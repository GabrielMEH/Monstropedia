package br.edu.ifsp.sbv.monstropedia.modelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class MonstroDAO extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "monstropedia.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_MONSTROS = "monstros";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_SIZE = "size";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_ALIGN = "align";
    private static final String COLUMN_AC = "ac";
    private static final String COLUMN_ACTYPE = "actype";
    private static final String COLUMN_HP = "hp";
    private static final String COLUMN_HPROLL = "hproll";
    private static final String COLUMN_SPEED = "speed";
    private static final String COLUMN_STR = "str";
    private static final String COLUMN_DEX = "dex";
    private static final String COLUMN_CON = "con";
    private static final String COLUMN_INTL = "intl";
    private static final String COLUMN_WIS = "wis";
    private static final String COLUMN_CHA = "cha";
    private static final String COLUMN_SAVING_THROW = "saving_throw";
    private static final String COLUMN_SKILL = "skill";
    private static final String COLUMN_DAMAGE_VULN = "damage_vuln";
    private static final String COLUMN_DAMAGE_RESI = "damage_resi";
    private static final String COLUMN_DAMAGE_IMUN = "damage_imun";
    private static final String COLUMN_CONDITION_IMUN = "condition_imun";
    private static final String COLUMN_SENSES = "senses";
    private static final String COLUMN_LANGUAGES = "languages";
    private static final String COLUMN_CR = "cr";
    private static final String COLUMN_XP = "xp";
    private static final String COLUMN_IMAGE = "image";

    public MonstroDAO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MONSTROS_TABLE = "CREATE TABLE " + TABLE_MONSTROS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_SIZE + " TEXT,"
                + COLUMN_TYPE + " TEXT,"
                + COLUMN_ALIGN + " TEXT,"
                + COLUMN_AC + " INTEGER,"
                + COLUMN_ACTYPE + " TEXT,"
                + COLUMN_HP + " INTEGER,"
                + COLUMN_HPROLL + " TEXT,"
                + COLUMN_SPEED + " TEXT,"
                + COLUMN_STR + " INTEGER,"
                + COLUMN_DEX + " INTEGER,"
                + COLUMN_CON + " INTEGER,"
                + COLUMN_INTL + " INTEGER,"
                + COLUMN_WIS + " INTEGER,"
                + COLUMN_CHA + " INTEGER,"
                + COLUMN_SAVING_THROW + " TEXT,"
                + COLUMN_SKILL + " TEXT,"
                + COLUMN_DAMAGE_VULN + " TEXT,"
                + COLUMN_DAMAGE_RESI + " TEXT,"
                + COLUMN_DAMAGE_IMUN + " TEXT,"
                + COLUMN_CONDITION_IMUN + " TEXT,"
                + COLUMN_SENSES + " TEXT,"
                + COLUMN_LANGUAGES + " TEXT,"
                + COLUMN_CR + " INTEGER,"
                + COLUMN_XP + " INTEGER,"
                + COLUMN_IMAGE + " TEXT"
                + ")";
        db.execSQL(CREATE_MONSTROS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MONSTROS);
        onCreate(db);
    }

    public void insert(Monstro monstro) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, monstro.getName());
        values.put(COLUMN_SIZE, monstro.getSize());
        values.put(COLUMN_TYPE, monstro.getType());
        values.put(COLUMN_ALIGN, monstro.getAlign());
        values.put(COLUMN_AC, monstro.getAC());
        values.put(COLUMN_ACTYPE, monstro.getACtype());
        values.put(COLUMN_HP, monstro.getHP());
        values.put(COLUMN_HPROLL, monstro.getHProll());
        values.put(COLUMN_SPEED, monstro.getSpeed());
        values.put(COLUMN_STR, monstro.getStr());
        values.put(COLUMN_DEX, monstro.getDex());
        values.put(COLUMN_CON, monstro.getCon());
        values.put(COLUMN_INTL, monstro.getIntl());
        values.put(COLUMN_WIS, monstro.getWis());
        values.put(COLUMN_CHA, monstro.getCha());
        values.put(COLUMN_SAVING_THROW, monstro.getSaving_throw());
        values.put(COLUMN_SKILL, monstro.getSkill());
        values.put(COLUMN_DAMAGE_VULN, monstro.getDamage_vuln());
        values.put(COLUMN_DAMAGE_RESI, monstro.getDamage_resi());
        values.put(COLUMN_DAMAGE_IMUN, monstro.getDamage_imun());
        values.put(COLUMN_CONDITION_IMUN, monstro.getCondition_immunities());
        values.put(COLUMN_SENSES, monstro.getSenses());
        values.put(COLUMN_LANGUAGES, monstro.getLanguages());
        values.put(COLUMN_CR, monstro.getCR());
        values.put(COLUMN_XP, monstro.getXP());
        values.put(COLUMN_IMAGE, monstro.getImage());

        db.insert(TABLE_MONSTROS, null, values);
        db.close();
    }

    public Monstro findByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MONSTROS, null, "LOWER(" + COLUMN_NAME + ")=?",
                new String[]{name.toLowerCase()}, null, null, null, null);

        Monstro monstro = null;
        if (cursor != null && cursor.moveToFirst()) {
            monstro = cursorToMonstro(cursor);
        }
        if (cursor != null) cursor.close();
        db.close();
        return monstro;
    }

    public List<Monstro> listAll() {
        List<Monstro> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MONSTROS, null, null, null, null, null, COLUMN_NAME + " ASC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                lista.add(cursorToMonstro(cursor));
            } while (cursor.moveToNext());
        }
        if (cursor != null) cursor.close();
        db.close();
        return lista;
    }

    public boolean deletar(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_MONSTROS, COLUMN_NAME + "=?", new String[]{name});
        db.close();
        return rowsAffected > 0;
    }

    private Monstro cursorToMonstro(Cursor cursor) {
        Monstro monstro = new Monstro();
        monstro.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
        monstro.setSize(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SIZE)));
        monstro.setType(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE)));
        monstro.setAlign(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ALIGN)));
        monstro.setAC(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AC)));
        monstro.setACtype(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ACTYPE)));
        monstro.setHP(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_HP)));
        monstro.setHProll(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HPROLL)));
        monstro.setSpeed(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SPEED)));
        monstro.setStr(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STR)));
        monstro.setDex(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DEX)));
        monstro.setCon(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CON)));
        monstro.setIntl(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_INTL)));
        monstro.setWis(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_WIS)));
        monstro.setCha(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CHA)));
        monstro.setSaving_throw(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SAVING_THROW)));
        monstro.setSkill(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SKILL)));
        monstro.setDamage_vuln(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DAMAGE_VULN)));
        monstro.setDamage_resi(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DAMAGE_RESI)));
        monstro.setDamage_imun(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DAMAGE_IMUN)));
        monstro.setCondition_immunities(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONDITION_IMUN)));
        monstro.setSenses(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SENSES)));
        monstro.setLanguages(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LANGUAGES)));
        monstro.setCR(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CR)));
        monstro.setXP(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_XP)));
        monstro.setImage(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE)));
        return monstro;
    }
}
