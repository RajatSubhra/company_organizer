package io.tutorial.turntotech.infoOrganizerSample.Models;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Rajat on 4/26/17.
 */


@DatabaseTable(tableName = "Company")
public class Company {


//    private static long nextID;
//
//    private static long getID() {
//        long newNumber = nextID;
//        newNumber++;
//        return newNumber;
//    }
    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField
    private String comapanyName;

    @DatabaseField
    private String logoURL;

    @DatabaseField
    private String stockTicker;

    @DatabaseField
    private double stockPrice;


    // One-to-many
    @ForeignCollectionField(columnName = "products", eager = true)
    private ForeignCollection<Product> products;


    public Company(){
        //this.id = getID();
        //products = new ArrayList<Product>();
    }

    public Company(String cName,String sTicker, String logoURL) {
        //this.id = getID();
        this.comapanyName = cName;
        this.stockTicker = sTicker;
        this.logoURL = logoURL;
        //products = new ArrayList<Product>();
    }

    public String getComapanyName() {
        return comapanyName;
    }

    public void setComapanyName(String comapanyName) {
        this.comapanyName = comapanyName;
    }

    public String getStockTicker() {
        return stockTicker;
    }

    public void setStockTicker(String stockTicker) {
        this.stockTicker = stockTicker;
    }

    public double getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(double stockPrice) {
        this.stockPrice = stockPrice;
    }

    public String getLogoURL() {
        return logoURL;
    }

    public void setLogoURL(String logoURL) {
        this.logoURL = logoURL;
    }

    public long getId() {
        return id;
    }

    public ForeignCollection<Product> getProducts() {
        return products;
    }

    public void addProduct(Product product){
        this.products.add(product);
    }

    public boolean deleteProduct(Product product){
        return this.products.remove(product);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
