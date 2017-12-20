package io.tutorial.turntotech.infoOrganizerSample.ActivityAndFragments;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.tutorial.turntotech.infoOrganizerSample.R;


public class StartActivity extends AppCompatActivity {

    static int companyNo;
    static int productNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        companyNo = 0;

        // for reducing overdraw
        getWindow().setBackgroundDrawable(null);

        FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainLayout, new CompanyFragment()).commit();
    }

    public int getCurrentCompanyNo()
    {
        return companyNo;
    }

    public void setCurrentCompanyNo(int pos)
    {
        companyNo = pos;
    }

    public int getProductNo()
    {
        return productNo;
    }

    public void setProductNo(int pos)
    {
        productNo = pos;
    }
}
