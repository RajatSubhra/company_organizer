package io.tutorial.turntotech.infoOrganizerSample.ActivityAndFragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import io.tutorial.turntotech.infoOrganizerSample.Models.Company;
import io.tutorial.turntotech.infoOrganizerSample.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class CompanyFragment extends Fragment {

    private RecyclerView recycler_view;
    private ArrayList<Company> listOfComany;
    private  VerticalAdapter recyclerAdapter;
    ImageButton addButton;
    ImageButton backButton,deleteButton;
    CompanyProductDAO companyProductDAO;
    boolean deleteFlag;

    RecyclerItemClickListener recyclerItemClickListener;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_main, container, false);

        deleteFlag = false;


        // free memory
        listOfComany = null;
        recycler_view=  view.findViewById(R.id.vertical_recycler_view);
        try {
            companyProductDAO = CompanyProductDAO.getInstance(getContext());
        } catch (SQLException e) {
            e.printStackTrace();
        }


        listOfComany = companyProductDAO.getCompanyList();




        recyclerAdapter=new VerticalAdapter(listOfComany);


        LinearLayoutManager layoutmanager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recycler_view.setLayoutManager(layoutmanager);



        recycler_view.setAdapter(recyclerAdapter);

        recyclerItemClickListener =
                new RecyclerItemClickListener(getContext(), recycler_view ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        if (deleteFlag)
                            return;

                        ((StartActivity) getActivity()).setCurrentCompanyNo(position);

                        // Go to Child not Found Screen
                        Fragment productFragment = new ProductFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.mainLayout, productFragment);
                        fragmentTransaction.addToBackStack(null);

                        // Commit the transaction
                        fragmentTransaction.commit();

                    }

                    @Override public void onLongItemClick(View view, final int position) {
                        // Edit Company
                        final Dialog dialog = new Dialog(getContext());
                        dialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
                        dialog.setContentView(R.layout.add_company_layout);
                        dialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.editcompany);

                        dialog.setTitle("Edit "+ listOfComany.get(position).getComapanyName());


                        // set the custom dialog components - text, image and button
                        final EditText newCompanyName = (EditText) dialog.findViewById(R.id.editTextCompanyName);
                        final EditText newCompanyTicker = (EditText) dialog.findViewById(R.id.editTextCompanyTicker);
                        final EditText newCompanyLogoURL = (EditText) dialog.findViewById(R.id.editTextCompanyLogo);

                        Button editButton = (Button) dialog.findViewById(R.id.button2);
                        editButton.setText("Edit Company");
                        newCompanyName.setText(listOfComany.get(position).getComapanyName());
                        newCompanyTicker.setText(listOfComany.get(position).getStockTicker());
                        newCompanyLogoURL.setText(listOfComany.get(position).getLogoURL());


                        // if button is clicked, close the custom dialog
                        editButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.e("TTT","Edit Company Done");
                                try {
                                    companyProductDAO.editCommpany(listOfComany.get(position),newCompanyName.getText().toString(),newCompanyTicker.getText().toString(),newCompanyLogoURL.getText().toString());
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }

                                // Refresh RecyclerView
                                recyclerAdapter.notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        });

                        dialog.show();

                    }
                });


         recycler_view.addOnItemTouchListener(recyclerItemClickListener );



        // Action Bar Setup
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.toolbar);
        backButton = (ImageButton)activity.findViewById(R.id.imageButton);
        addButton = (ImageButton)activity.findViewById(R.id.imageButton2);
        deleteButton = (ImageButton)activity.findViewById(R.id.deleteButtonToolBar);
        backButton.setVisibility(View.INVISIBLE);



        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
                dialog.setContentView(R.layout.add_company_layout);
                dialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.addicon);

                dialog.setTitle("Add New Company");


                // set the custom dialog components - text, image and button
                final EditText newCompanyName = (EditText) dialog.findViewById(R.id.editTextCompanyName);
                final EditText newCompanyTicker = (EditText) dialog.findViewById(R.id.editTextCompanyTicker);
                final EditText newCompanyLogoURL = (EditText) dialog.findViewById(R.id.editTextCompanyLogo);

                Button addButton = (Button) dialog.findViewById(R.id.button2);
                // if button is clicked, close the custom dialog
                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("TTT","Add Company Done");

                        if(newCompanyName.getText().toString().isEmpty() || newCompanyName.getText().toString().equalsIgnoreCase("")){
                            Toast.makeText(getContext(),"At least Provide the name of company",Toast.LENGTH_LONG).show();
                        }else{
                            try {
                                companyProductDAO.createNewCompany(newCompanyName.getText().toString(),newCompanyTicker.getText().toString(),newCompanyLogoURL.getText().toString());
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                        // Refresh RecyclerView
                        recyclerAdapter.notifyDataSetChanged();
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
                    recycler_view.removeOnItemTouchListener(recyclerItemClickListener);
                }else{
                    recycler_view.addOnItemTouchListener(recyclerItemClickListener);
                }

                // Refresh RecyclerView
                recyclerAdapter.notifyDataSetChanged();
            }
        });




        return view;
    }


    // Custom Adapter Class For RecyclerView
    public class VerticalAdapter extends RecyclerView.Adapter<VerticalAdapter.MyViewHolder> {

        private ArrayList<Company> verticalList;


        public class MyViewHolder extends RecyclerView.ViewHolder  {
            public TextView txtView;
            public TextView detailTxtView;
            public ImageView companyLogoImageView;
            public ImageButton deleteCompanyButton;




            public MyViewHolder(View view) {
                super(view);
                txtView = (TextView) view.findViewById(R.id.txtView);
                detailTxtView = (TextView) view.findViewById(R.id.textView2);
                companyLogoImageView = (ImageView) view.findViewById(R.id.imageView);
                deleteCompanyButton = (ImageButton) view.findViewById(R.id.imageButton3);
            }


        }


        public VerticalAdapter(ArrayList<Company> verticalList) {
            this.verticalList = verticalList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.vertical_item_view, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            holder.txtView.setText(verticalList.get(position).getComapanyName());
            holder.detailTxtView.setText(verticalList.get(position).getStockTicker());

            if (deleteFlag){
                // Show delete Button
                holder.deleteCompanyButton.setVisibility(View.VISIBLE);
            }else{
                // hide delete button
                holder.deleteCompanyButton.setVisibility(View.INVISIBLE);
            }


            // Check URL is valid or not if URL is not valid or null then set a dummy image icon
            UrlValidator urlValidator = new UrlValidator();
            if (urlValidator.isValid(verticalList.get(position).getLogoURL()) &&  companyProductDAO.checkConnection()){
                Picasso.with(getContext())
                        .load(verticalList.get(position).getLogoURL())
                        .into(holder.companyLogoImageView);
            }else{
                holder.companyLogoImageView.setImageResource(R.drawable.new_company);

            }


            holder.deleteCompanyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("TTT","Delete Company in Recycle View Cell");
                    recycler_view.addOnItemTouchListener(recyclerItemClickListener);
                    // Delete from the list
                    deleteFlag = false;

                    new AlertDialog.Builder(getContext())
                            .setTitle("Delete entry")
                            .setMessage("Are you sure you want to delete this entry?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    //Delete Company

                                    try {
                                        companyProductDAO.deleteCompany(listOfComany.get(position));
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }

                                    // Refresh RecyclerView
                                    //recycler_view.removeViewAt(position);
                                    //recyclerAdapter.notifyItemRemoved(position);
                                    //recyclerAdapter.notifyItemRangeChanged(position, listOfComany.size());
                                    recyclerAdapter.notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    recyclerAdapter.notifyDataSetChanged();
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
