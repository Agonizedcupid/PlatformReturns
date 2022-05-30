package com.aariyan.platformreturns;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aariyan.platformreturns.Adapter.StoreAdapter;
import com.aariyan.platformreturns.Constant.Constant;
import com.aariyan.platformreturns.Interface.CustomerInterface;
import com.aariyan.platformreturns.Interface.RouteInterface;
import com.aariyan.platformreturns.Model.RouteModel;
import com.aariyan.platformreturns.Model.StoreModel;
import com.aariyan.platformreturns.Network.Networking;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.List;

public class Home extends AppCompatActivity {

    private TextView getCustomerBtn, dateSelector;
    private Spinner routeSelector;

    private int selectedRoute;
    private ProgressBar progressBar;
    private ConstraintLayout snackBarLayout;

    private RecyclerView storeRecyclerView;

    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    int day, month, year;
    String date = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Home");

        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        initUI();
    }

    @Override
    protected void onResume() {
        loadRoute();
        super.onResume();
    }

    private void initUI() {

        storeRecyclerView = findViewById(R.id.storeRecyclerView);
        storeRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        getCustomerBtn = findViewById(R.id.getCustomerBtn);
        dateSelector = findViewById(R.id.dateSelector);
        routeSelector = findViewById(R.id.routeSelector);
        snackBarLayout = findViewById(R.id.snackBarLayout);

        progressBar = findViewById(R.id.progressbar);

        dateSelector.setText(Constant.getDate());

        getCustomerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                loadCustomer();
            }
        });

        dateSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDate();
            }
        });
    }

    private void loadCustomer() {
        Networking networking = new Networking();
        networking.getStores(new CustomerInterface() {
            @Override
            public void onSuccess(List<StoreModel> listOfStore) {
                StoreAdapter adapter = new StoreAdapter(Home.this, listOfStore);
                storeRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                Snackbar.make(snackBarLayout, "Loading Customer, Please wait!", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String errorMessage) {
                Snackbar.make(snackBarLayout, "" + errorMessage, Snackbar.LENGTH_SHORT).show();
            }
        }, dateSelector.getText().toString(), selectedRoute);

    }

    private void loadRoute() {
        Snackbar.make(snackBarLayout, "Loading Routes, Please wait!", Snackbar.LENGTH_SHORT).show();
        dateSelector.setEnabled(false);
        getCustomerBtn.setEnabled(false);
        Networking networking = new Networking();
        networking.getRoutes(new RouteInterface() {
            @Override
            public void onSuccess(List<RouteModel> listOfRoutes) {
                routeSpinnerFunc(listOfRoutes);
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(Home.this, "" + errorMessage, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void selectDate() {
        datePickerDialog = new DatePickerDialog(Home.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                //Month
                int j = i1 + 1;

                //date = i + "-" + j + "-" + i2;
                //date = i2 + "-" + j + "-" + i;
                date = i + "-" + j + "-" + i2;
                //2022-1-15
                dateSelector.setText(date);

            }
            //}, day, month, year);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
//        new DatePickerDialog(AddTimeActivity.this, null, calendar.get(Calendar.YEAR),
//                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

        datePickerDialog.show();
    }

    private void routeSpinnerFunc(List<RouteModel> routeList) {

        //Spinner items
        ArrayAdapter<RouteModel> dataAdapter = new ArrayAdapter<RouteModel>(Home.this,
                android.R.layout.simple_spinner_item, routeList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        routeSelector.setAdapter(dataAdapter);
        routeSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                //selectedRoute = Integer.parseInt(adapterView.getItemAtPosition(position).toString());
                selectedRoute = routeList.get(position).getRouteid();
                progressBar.setVisibility(View.GONE);
                dateSelector.setEnabled(true);
                getCustomerBtn.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}