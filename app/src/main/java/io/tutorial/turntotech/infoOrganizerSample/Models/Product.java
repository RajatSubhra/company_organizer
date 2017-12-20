package io.tutorial.turntotech.infoOrganizerSample.Models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Rajat on 4/26/17.
 */


@DatabaseTable(tableName = "Product")
public class Product {

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField
    private String productName;

    @DatabaseField
    private String logoURL;

    @DatabaseField
    private String productURL;

    @DatabaseField(columnName = "company", foreign = true, foreignAutoRefresh = true)
    private Company company;

    public Product() {
        //this.id = getID();
    }
    public Product(String prodName,String productURL,String logoURL,Company company){
        //this.id = getID();
        this.productName = prodName;
        this.productURL = productURL;
        this.logoURL = logoURL;
        this.company = company;
    }

    public long getId() {
        return id;
    }

    public String getLogoURL() {
        return logoURL;
    }

    public Company getComapny() {
        return this.company;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductURL() {
        return productURL;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setLogoURL(String logoURL) {
        this.logoURL = logoURL;
    }

    public void setProductURL(String productURL) {
        this.productURL = productURL;
    }
}
