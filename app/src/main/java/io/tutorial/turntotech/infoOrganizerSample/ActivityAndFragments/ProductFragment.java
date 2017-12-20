package io.tutorial.turntotech.infoOrganizerSample.ActivityAndFragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.commons.validator.routines.UrlValidator;

import java.sql.SQLException;
import java.util.ArrayList;

import io.tutorial.turntotech.infoOrganizerSample.DataAccessObject.CompanyProductDAO;
import io.tutorial.turntotech.infoOrganizerSample.DataAccessObject.ICallback;
import io.tutorial.turntotech.infoOrganizerSample.Models.Product;
import io.tutorial.turntotech.infoOrganizerSample.R;

public class ProductFragment extends Fragment implements ICallback {

    private RecyclerView product_recycler_view;
    private ProductFragment.VerticalProductAdapter recyclerProductAdapter;
    ArrayList<Product> products;
    ImageButton addButton;
    ImageButton backButton,deleteButton;
    TextView toolbatTitle;
    CompanyProductDAO companyProductDAO;
    boolean deleteFlag;
    RecyclerItemClickListener recyclerItemClickListener;
    int companyNo;
    ActionBar actionBar;


    @Override
    public void onStart() {
        super.onStart();
        recyclerProductAdapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(
                R.layout.activity_main2, container, false);

        product_recycler_view= (RecyclerView) view.findViewById(R.id.vertical_recycler_view);

        // Get the Company to display correct Products
        companyNo = ((StartActivity) getActivity()).getCurrentCompanyNo();

        // free memory
        products = null;

        // Get Correct Products
        try {
            companyProductDAO = CompanyProductDAO.getInstance(getContext());
            companyProductDAO.callback = this;
            companyProductDAO.fetchStockPriceCompany(companyNo,getContext());

//            // Call that method in
//            //This network call will run every 60 seconds.
//            Timer t = new Timer();
//            t.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    Log.e("Call","NetWork Call");
//
//                }
//            }, 0, 60000);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        products = companyProductDAO.getProductsforCompany(companyNo);

        recyclerProductAdapter = new VerticalProductAdapter(products);


        LinearLayoutManager layoutmanager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        product_recycler_view.setLayoutManager(layoutmanager);


        product_recycler_view.setAdapter(recyclerProductAdapter);

        recyclerItemClickListener = new RecyclerItemClickListener(getContext(), product_recycler_view ,new RecyclerItemClickListener.OnItemClickListener() {

            @Override public void onItemClick(View view, int position) {


                ((StartActivity) getActivity()).setProductNo(position);

                if (deleteFlag)
                    return;

                // Go to Child not Found Screen
                Fragment webFragment = new WebFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.mainLayout, webFragment);
                fragmentTransaction.addToBackStack(null);

                // Commit the transaction
                fragmentTransaction.commit();

            }

            @Override public void onLongItemClick(View view, final int position) {

                // Edit Product
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
                dialog.setContentView(R.layout.add_product_layout);
                dialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.editproduct);

                dialog.setTitle("Edit Product - "+products.get(position).getProductName());


                // set the custom dialog components - text, image and button
                final EditText newProductName = (EditText) dialog.findViewById(R.id.editTextProductName);
                final EditText newProductURL = (EditText) dialog.findViewById(R.id.editTextProductURL);
                final EditText newProductLogoURL = (EditText) dialog.findViewById(R.id.editTextProductLogo);

                Button editButton = (Button) dialog.findViewById(R.id.productbutton2);
                editButton.setText("Edit Product");
                newProductName.setText(products.get(position).getProductName());
                newProductLogoURL.setText(products.get(position).getLogoURL());
                newProductURL.setText(products.get(position).getProductURL());


                // if button is clicked, close the custom dialog
                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("TTT","Edit Product Done");
                        try {
                            companyProductDAO.editProduct(products.get(position),newProductName.getText().toString(),newProductURL.getText().toString(),newProductLogoURL.getText().toString());

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        // Refresh RecyclerView
                        recyclerProductAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });

        product_recycler_view.addOnItemTouchListener(recyclerItemClickListener);
        // ActionBar SetUp
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        actionBar = activity.getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.toolbar);
        backButton = (ImageButton)activity.findViewById(R.id.imageButton);
        addButton = (ImageButton)activity.findViewById(R.id.imageButton2);
        deleteButton = (ImageButton)activity.findViewById(R.id.deleteButtonToolBar);
        toolbatTitle = (TextView)activity.findViewById(R.id.toolbarTitle);

        toolbatTitle.setText(companyProductDAO.getCompanyList().get(companyNo).getComapanyName()+": "+companyProductDAO.getCompanyList().get(companyNo).getStockPrice());
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
                dialog.setContentView(R.layout.add_product_layout);
                dialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.addproduct);

                dialog.setTitle("Add a new Product");


                // set the custom dialog components - text, image and button
                final EditText newProductName = (EditText) dialog.findViewById(R.id.editTextProductName);
                final EditText newProductURL = (EditText) dialog.findViewById(R.id.editTextProductURL);
                final EditText newProductLogoURL = (EditText) dialog.findViewById(R.id.editTextProductLogo);

                Button addButton = (Button) dialog.findViewById(R.id.productbutton2);
                // if button is clicked, close the custom dialog
                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("TTT","Add Product Done");
                        if(newProductName.getText().toString().isEmpty() || newProductName.getText().toString().equalsIgnoreCase("")){
                            Toast.makeText(getContext(),"At least Provide the name of Product",Toast.LENGTH_LONG).show();

                        }else{
                            try {
                                companyProductDAO.addProductToCompany(newProductName.getText().toString(),newProductURL.getText().toString(),newProductLogoURL.getText().toString());

                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        // Refresh RecyclerView
                        recyclerProductAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFlag = !deleteFlag;
                if(deleteFlag){
                    product_recycler_view.removeOnItemTouchListener(recyclerItemClickListener);
                }else{
                    product_recycler_view.addOnItemTouchListener(recyclerItemClickListener);
                }

                product_recycler_view.removeOnItemTouchListener(recyclerItemClickListener);



                // Refresh RecyclerView
                recyclerProductAdapter.notifyDataSetChanged();
            }
        });




        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        companyProductDAO.callback = null;
    }

    @Override
    public void update(Object object) {

        if(object!=null){
            toolbatTitle.setText(companyProductDAO.getCompanyList().get(companyNo).getComapanyName()+": "+(String)object);
        }

    }

    public class VerticalProductAdapter extends RecyclerView.Adapter<ProductFragment.VerticalProductAdapter.MyViewHolder> {

        private ArrayList<Product> verticalList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView textProductName;
            public ImageView productLogo;
            public Button deleteProductButton;

            public MyViewHolder(View view) {
                super(view);
                textProductName = (TextView) view.findViewById(R.id.textProductName);
                productLogo = (ImageView)view.findViewById(R.id.imageView);
                deleteProductButton = (Button) view.findViewById(R.id.button);

            }
        }


        public VerticalProductAdapter(ArrayList<Product> verticalList) {
            this.verticalList = verticalList;
        }

        @Override
        public ProductFragment.VerticalProductAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.vertical_product_item, parent, false);

            return new ProductFragment.VerticalProductAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int pos) {
            if (deleteFlag){
                // Show delete Button
                holder.deleteProductButton.setVisibility(View.VISIBLE);
            }else{
                // hide delete button
                holder.deleteProductButton.setVisibility(View.INVISIBLE);
            }
            holder.textProductName.setText(verticalList.get(pos).getProductName());


            // Check URL is valid or not if URL is not valid or null then set a dummy image icon
            UrlValidator urlValidator = new UrlValidator();
            if (urlValidator.isValid(verticalList.get(pos).getLogoURL()) && companyProductDAO.checkConnection()){
                Picasso.with(getContext())
                        .load(verticalList.get(pos).getLogoURL())
                        .into(holder.productLogo);
            }else{
                holder.productLogo.setImageResource(R.drawable.new_company);

            }

            holder.deleteProductButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("TTT","Delete Product in Recycle View Cell");
                    product_recycler_view.addOnItemTouchListener(recyclerItemClickListener);
                    // Delete from the list
                    deleteFlag = false;

                    new AlertDialog.Builder(getContext())
                            .setTitle("Delete entry")
                            .setMessage("Are you sure you want to delete this entry?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //Delete Product
                                    try {
                                        companyProductDAO.deleteProduct(products.get(pos));

                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }

                                    // Refresh RecyclerView
//                                    product_recycler_view.removeViewAt(pos);
//                                    recyclerProductAdapter.notifyItemRemoved(pos);
//                                    recyclerProductAdapter.notifyItemRangeChanged(pos, products.size());
                                    recyclerProductAdapter.notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    recyclerProductAdapter.notifyDataSetChanged();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();


                }
            });
        }

        @Override
        public int getItemCount() {
            return verticalList.size();
        }
    }
}