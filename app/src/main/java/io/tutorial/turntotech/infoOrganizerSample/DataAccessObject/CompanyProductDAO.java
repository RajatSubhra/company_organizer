package io.tutorial.turntotech.infoOrganizerSample.DataAccessObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.commons.validator.routines.UrlValidator;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;

import io.tutorial.turntotech.infoOrganizerSample.Models.Company;
import io.tutorial.turntotech.infoOrganizerSample.Models.Product;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by Rajat on 4/26/17.
 */

public class CompanyProductDAO {

    ArrayList<Company> companyArrayList;
    ArrayList<Product> currentCompanyProductList;
    Company currentCompamny;
    static Context context;
    ArrayList<Double> stockPrice;
    public boolean isFirstFetch;
    int count_networkCall = 0;
    public ICallback callback;


    private static CompanyProductDAO instance = null;
    private CompanyProductDAO() {
        // Exists only to defeat instantiation.
    }


    public boolean checkConnection(){

        boolean isInternetPresent = false;
        // 1. Create an instance of ConnectivityManager

        ConnectivityManager cmanager = (ConnectivityManager)context.getSystemService(CONNECTIVITY_SERVICE);

        // 2. The connectivityManager object's getActiveNetworkInfo() method
        // gives us info about the network we are on
        NetworkInfo networkInfoObj = cmanager.getActiveNetworkInfo();

        String networkType = null;

        // 3. If connected, we can get the type of the network
        if (networkInfoObj != null && networkInfoObj.isConnected()) {
            isInternetPresent = true;
        }
        return isInternetPresent;
    }

    private void createDummyCompanyAndProducts() throws SQLException {

        // Create Google
        Company googleCompany = new Company("Google","GOOG","https://cdn.pixabay.com/photo/2015/12/11/11/43/google-1088004_960_720.png");
        Product googleChromecast = new Product("Chrome Cast","https://www.google.com","https://www.google.com/chromecast/static/images/home/chromecast.png",googleCompany);
        Product googleHome = new Product("Google Home","https://www.google.com/","https://lh3.googleusercontent.com/Nu3a6F80WfixUqf_ec_vgXy_c0-0r4VLJRXjVFF_X_CIilEu8B9fT35qyTEj_PEsKw",googleCompany);
        Product googlePixel = new Product("Google Pixel","https://www.google.com/","https://www.androidcentral.com/sites/androidcentral.com/files/styles/large/public/topic_images/2016/img_preorder-form_pixel_en-1.png?itok=PwujPFqP",googleCompany);


        // Create Apple
        Company appleCompany = new Company("Apple","AAPL","https://upload.wikimedia.org/wikipedia/commons/thumb/f/fa/Apple_logo_black.svg/2000px-Apple_logo_black.svg.png");
        Product iPhone  = new Product("IPhone","https://www.apple.com/iphone/","http://www.pngpix.com/wp-content/uploads/2016/03/iphone-PNG-image.png",appleCompany);
        Product iPad = new Product("IPad","https://www.apple.com/ipad/","http://www.pngpix.com/wp-content/uploads/2016/04/Ipad-PNG-Image1.png",appleCompany);
        Product iPod = new Product("IPod","https://www.apple.com/ipod/","https://upload.wikimedia.org/wikipedia/commons/5/51/IPod_classic.png",appleCompany);



        // Create Samsung
        Company samsungCompany = new Company("Samsung","SSNLF","http://www.brycomm.com/wp-content/uploads/2011/08/Samsung-Logo.png");
        Product galaxyS = new Product("Samsung Galaxy S","http://www.samsung.com/us/mobile/phones/galaxy-s/s/_/n-10+11+hv1rp+zq1xa/","https://upload.wikimedia.org/wikipedia/commons/e/ee/Samsung_Galaxy_S_Duos_3_Black.png",samsungCompany);
        Product galaxyNote = new Product("Samsung Galaxy Note","http://www.samsung.com/us/mobile/phones/galaxy-note/s/_/n-10+11+hv1rp+zq1xb/","https://www.pcper.com/files/imagecache/article_max_width/news/2013-09-09/Samsung%20Galaxy%20Note%203.png",samsungCompany);
        Product samsungJ7 = new Product("Samsung J7","http://www.samsung.com/us/mobile/phones/all-other-phones/samsung-galaxy-j7-16gb-t-mobile-white-sm-j700tzwatmb/","http://forum.youmobile.org/downloads/catimgs/267.png",samsungCompany);


        instance.companyArrayList.add(googleCompany);
        instance.companyArrayList.add(appleCompany);
        instance.companyArrayList.add(samsungCompany);

        // ADD to DB
        DataBaseHelper.getInstance(context).getCompanyDao().create(appleCompany);
        DataBaseHelper.getInstance(context).getProductDao().create(iPhone);
        DataBaseHelper.getInstance(context).getProductDao().create(iPad);
        DataBaseHelper.getInstance(context).getProductDao().create(iPod);

        DataBaseHelper.getInstance(context).getCompanyDao().create(googleCompany);
        DataBaseHelper.getInstance(context).getProductDao().create(googleChromecast);
        DataBaseHelper.getInstance(context).getProductDao().create(googleHome);
        DataBaseHelper.getInstance(context).getProductDao().create(googlePixel);

        DataBaseHelper.getInstance(context).getCompanyDao().create(samsungCompany);
        DataBaseHelper.getInstance(context).getProductDao().create(galaxyS);
        DataBaseHelper.getInstance(context).getProductDao().create(galaxyNote);
        DataBaseHelper.getInstance(context).getProductDao().create(samsungJ7);
    }

    public static CompanyProductDAO getInstance(Context currentContext) throws SQLException {
        context = currentContext;

        if(instance == null) {
            instance = new CompanyProductDAO();
            instance.companyArrayList = new ArrayList<Company>();

            // Get all from db
            DataBaseHelper dbHelper = DataBaseHelper.getInstance(context);
            instance.companyArrayList = (ArrayList<Company>) dbHelper.getCompanyDao().queryForAll();
            System.out.println(instance.companyArrayList.size());
            if (instance.companyArrayList.size() == 0) {
                // Load Dummy
                instance.createDummyCompanyAndProducts();
            }
            instance.isFirstFetch = true;

        }

        else {

            instance.isFirstFetch = false;
            // free all memory

            instance.companyArrayList.clear();
            instance.currentCompamny = null;
            if(instance.currentCompanyProductList!=null)
                instance.currentCompanyProductList.clear();

            // Get all from db
            DataBaseHelper dbHelper = DataBaseHelper.getInstance(context);

            instance.companyArrayList = (ArrayList<Company>) dbHelper.getCompanyDao().queryForAll();
        }
        return instance;
    }


    public ArrayList<Product> getProductsforCompany(int pos)
    {
        currentCompamny = this.companyArrayList.get(pos);
        this.currentCompanyProductList = new ArrayList<Product>(currentCompamny.getProducts());

        return  currentCompanyProductList;
    }

    public ArrayList<Company> getCompanyList(){
        return companyArrayList;
    }

    public void createNewCompany(String cName,String sTicker, String logoURL) throws SQLException {
        Company company = new Company(cName,sTicker,logoURL);
        this.companyArrayList.add(company);


        // Store to DB
        DataBaseHelper.getInstance(context).getCompanyDao().create(company);
    }

    public void addProductToCompany(String prodName,String productURL,String logoURL) throws SQLException {
        // Check URL is valid or not if URL is not valid or null then set a dummy image icon
        UrlValidator urlValidator = new UrlValidator();
        if (!urlValidator.isValid(productURL)){
            productURL = "https://www.google.com/";
        }
        Product product = new Product(prodName, productURL, logoURL, currentCompamny);
        currentCompanyProductList.add(product);


        // add Product to DataBase
        DataBaseHelper.getInstance(context).getProductDao().create(product);
    }

    public void editCommpany(Company company, String newName,String newTicker, String newlogoURL) throws SQLException {
        company.setComapanyName(newName);
        company.setLogoURL(newlogoURL);
        company.setStockTicker(newTicker);

        // update company to DB
        DataBaseHelper.getInstance(context).getCompanyDao().update(company);
    }

    public void editProduct(Product product,String prodName,String productURL,String logoURL) throws SQLException {
        product.setProductName(prodName);
        product.setProductURL(productURL);
        product.setLogoURL(logoURL);

        // Update Product to DB
        DataBaseHelper.getInstance(context).getProductDao().update(product);
    }

    public void deleteCompany(Company company) throws SQLException {
        this.companyArrayList.remove(company);

        // Delete to DB
        DataBaseHelper.getInstance(context).getCompanyDao().delete(company);
    }

    public void deleteProduct(Product product) throws SQLException {

        // Delete to DB
        DataBaseHelper.getInstance(context).getProductDao().delete(product);

        // update arrayList
        this.currentCompanyProductList.remove(product);
    }


    public boolean checkDouble(String myString)
    {
        final String Digits     = "(\\p{Digit}+)";
        final String HexDigits  = "(\\p{XDigit}+)";
        // an exponent is 'e' or 'E' followed by an optionally
        // signed decimal integer.
        final String Exp        = "[eE][+-]?"+Digits;
        final String fpRegex    =
                ("[\\x00-\\x20]*"+ // Optional leading "whitespace"
                        "[+-]?(" +         // Optional sign character
                        "NaN|" +           // "NaN" string
                        "Infinity|" +      // "Infinity" string

                        // A decimal floating-point string representing a finite positive
                        // number without a leading sign has at most five basic pieces:
                        // Digits . Digits ExponentPart FloatTypeSuffix
                        //
                        // Since this method allows integer-only strings as input
                        // in addition to strings of floating-point literals, the
                        // two sub-patterns below are simplifications of the grammar
                        // productions from the Java Language Specification, 2nd
                        // edition, section 3.10.2.

                        // Digits ._opt Digits_opt ExponentPart_opt FloatTypeSuffix_opt
                        "((("+Digits+"(\\.)?("+Digits+"?)("+Exp+")?)|"+

                        // . Digits ExponentPart_opt FloatTypeSuffix_opt
                        "(\\.("+Digits+")("+Exp+")?)|"+

                        // Hexadecimal strings
                        "((" +
                        // 0[xX] HexDigits ._opt BinaryExponent FloatTypeSuffix_opt
                        "(0[xX]" + HexDigits + "(\\.)?)|" +

                        // 0[xX] HexDigits_opt . HexDigits BinaryExponent FloatTypeSuffix_opt
                        "(0[xX]" + HexDigits + "?(\\.)" + HexDigits + ")" +

                        ")[pP][+-]?" + Digits + "))" +
                        "[fFdD]?))" +
                        "[\\x00-\\x20]*");// Optional trailing "whitespace"

        return (Pattern.matches(fpRegex, myString));

    }

    public void fetchStockPriceCompany(final int company_num, final Context context){

        if(!checkConnection())
            return;
        if (companyArrayList.get(company_num)!=null){
            if (companyArrayList.get(company_num).getStockTicker().isEmpty() || companyArrayList.get(company_num).getStockTicker().equalsIgnoreCase("")){
                return;
            }else{
                String stockUrl = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol="+companyArrayList.get(company_num).getStockTicker()+"&interval=1min&apikey=JRU0T191Z90UXGJX";

                StringRequest stringRequest = new StringRequest(stockUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                count_networkCall = 0;
                                try {
                                    JSONObject jsonObject = new JSONObject(response).getJSONObject("Time Series (1min)");
                                    Iterator<?> keys = jsonObject.keys();

                                    if( keys.hasNext() ) {
                                        String key = (String)keys.next();
                                        if ( jsonObject.get(key) instanceof JSONObject ) {
                                            String stockPrice = jsonObject.getJSONObject(key).getString("1. open");
                                            Log.d("STOCK PRICE",stockPrice);

                                            if(companyArrayList.get(company_num)!=null) {
                                                companyArrayList.get(company_num).setStockPrice(Double.parseDouble(stockPrice));
                                                DataBaseHelper.getInstance(context).getCompanyDao().update(companyArrayList.get(company_num));
                                            }
                                            if(callback!=null) {
                                                callback.update(stockPrice);
                                            }
                                        }
                                    }


                                } catch (JSONException e) {
                                    Log.e("ERR","JSON Parsing Error");
                                    return;
                                    //e.printStackTrace();
                                } catch (SQLException e) {
                                    Log.e("ERR","DataBase Error");
                                    return;
                                }


                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //Log.e("Network Error - ",error.getLocalizedMessage());
                                NetworkResponse networkResponse = error.networkResponse;
                                if(count_networkCall>=5)
                                    return;
                                if (networkResponse == null && count_networkCall<=5){
                                    count_networkCall++;
                                    fetchStockPriceCompany(company_num,context);
                                }
                                else if((networkResponse.statusCode == 301 || networkResponse.statusCode == 302)) {
                                    Log.e("301/302 ERR",networkResponse.data.toString());
                                }


                            }
                        });

                RequestQueue requestQueue = Volley.newRequestQueue(context);

                requestQueue.add(stringRequest);


            }
        }



    }

    // return company counts for Expreeso Test
    public static int returnCompanyCount(){
        return instance.companyArrayList.size();
    }


}
