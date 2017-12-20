package io.tutorial.turntotech.infoOrganizerSample.ActivityAndFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.sql.SQLException;

import io.tutorial.turntotech.infoOrganizerSample.DataAccessObject.CompanyProductDAO;
import io.tutorial.turntotech.infoOrganizerSample.Models.Product;
import io.tutorial.turntotech.infoOrganizerSample.R;


public class WebFragment extends Fragment {

    ImageButton addButton;
    ImageButton backButton,deleteButton;
    int productNo;
    int companyNo;
    CompanyProductDAO companyProductDAO;
    Product currentProduct;
    WebView productWebView;
    ImageView errorImage;


    public WebFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(
                R.layout.fragment_web, container, false);

        // Action Bar Setup
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.toolbar);
        backButton = (ImageButton)activity.findViewById(R.id.imageButton);
        addButton = (ImageButton)activity.findViewById(R.id.imageButton2);
        deleteButton = (ImageButton)activity.findViewById(R.id.deleteButtonToolBar);
        errorImage = (ImageView) view.findViewById(R.id.errorImageView);

        addButton.setVisibility(View.INVISIBLE);
        deleteButton.setVisibility(View.INVISIBLE);
        errorImage.setVisibility(View.INVISIBLE);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        productWebView = (WebView) view.findViewById(R.id.productWebView);


        try {
            companyProductDAO = CompanyProductDAO.getInstance(getContext());
        } catch (SQLException e) {
            e.printStackTrace();
        }


        companyNo = ((StartActivity) getActivity()).getCurrentCompanyNo();
        productNo = ((StartActivity) getActivity()).getProductNo();


        if(currentProduct!=null){
            currentProduct = null;
        }


        currentProduct = companyProductDAO.getProductsforCompany(companyNo).get(productNo);

        if(companyProductDAO.checkConnection())
            productWebView.loadUrl(currentProduct.getProductURL());
        else{
            // Show a Toast message

            Toast.makeText(getContext(),"No Internet..",Toast.LENGTH_LONG).show();
            errorImage.setVisibility(View.VISIBLE);
            errorImage.bringToFront();
        }

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
