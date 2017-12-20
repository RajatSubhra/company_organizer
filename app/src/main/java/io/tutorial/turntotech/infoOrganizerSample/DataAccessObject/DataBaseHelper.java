package io.tutorial.turntotech.infoOrganizerSample.DataAccessObject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import io.tutorial.turntotech.infoOrganizerSample.Models.Company;
import io.tutorial.turntotech.infoOrganizerSample.Models.Product;

/**
 * Created by Raj on 30/04/17.
 */

public class DataBaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME    = "info.db";
    private static final int    DATABASE_VERSION = 5;

    private Dao<Company, Integer> mCompanyDao = null;
    private Dao<Product, Integer> mProductDao = null;
    private static DataBaseHelper sharedInstance;


    public static DataBaseHelper getInstance(Context context) throws SQLException {
        if(sharedInstance == null) {
            sharedInstance = new DataBaseHelper(context);
        }
        return sharedInstance;
    }
    private DataBaseHelper(Context context) throws SQLException {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mCompanyDao = getDao(Company.class);
        mProductDao = getDao(Product.class);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Company.class);
            TableUtils.createTable(connectionSource, Product.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Company.class, true);
            TableUtils.dropTable(connectionSource, Product.class, true);
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /* Company */
    public Dao<Company, Integer> getCompanyDao() throws SQLException {
        if (mCompanyDao == null) {
            mCompanyDao = getDao(Company.class);
        }
        return mCompanyDao;
    }

    /* Product */
    public Dao<Product, Integer> getProductDao() throws SQLException {
        if (mProductDao == null) {
            mProductDao = getDao(Product.class);
        }
        return mProductDao;
    }

    @Override
    public void close() {
        mCompanyDao = null;
        mProductDao = null;
        super.close();
    }

}
