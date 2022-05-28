package com.aariyan.platformreturns;

import static com.aariyan.platformreturns.Constant.Constant.DEFAULT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aariyan.platformreturns.Adapter.UserAdapter;
import com.aariyan.platformreturns.Constant.Constant;
import com.aariyan.platformreturns.Database.SP;
import com.aariyan.platformreturns.Interface.UserList;
import com.aariyan.platformreturns.Interface.UserListClick;
import com.aariyan.platformreturns.Model.UserModel;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements UserListClick {

    //Instance variable of warning message:
    private TextView warningMessage;
    //Instance of RecyclerView variable:
    private RecyclerView recyclerView;

    //List for populating user data:
    List<UserModel> list = new ArrayList<>();

    private TextView userMessage;
    private EditText passwordField;
    private TextView logInBtn;

    private View bottomSheet, ipBottomSheet;
    BottomSheetBehavior behavior, ipBehavior;

    private FloatingActionButton closeApp;

    private EditText ipField;
    private TextView saveBtn, exitBtn;

    private CoordinatorLayout snackBarLayout;

    private RequestQueue requestQueue;

    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestQueue = Volley.newRequestQueue(this);
        snackBarLayout = findViewById(R.id.coordinatorLayout);
        progressBar = findViewById(R.id.progressbar);
        Constant.BASE_URL = getURL();
        getSavedData();

        //Instantiating the UI element:
        initUI();


        //Loading data from API:
        //loadData();
    }

    private void getSavedData() {
        android.content.SharedPreferences sharedPreferences = getSharedPreferences(Constant.IP_MODE_KEY, Context.MODE_PRIVATE);
        String result = sharedPreferences.getString(Constant.IP_URL, DEFAULT);
        if (result.equals("")) {
            progressBar.setVisibility(View.GONE);
            //Toast.makeText(MainActivity.this, "IP is not set.", Toast.LENGTH_SHORT).show();
            Snackbar.make(snackBarLayout, "IP is not set!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Configure", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showAlertDialog();
                        }
                    }).show();
        } else {
            loadData();
        }
    }

    private void loadData() {
        progressBar.setVisibility(View.VISIBLE);
        String appendedUrl = Constant.BASE_URL + "GetUsers/";
        AsyncOperations operations = new AsyncOperations(appendedUrl);
        operations.execute();
//        Networking networking = new Networking();
//        networking.getUserList(new UserList() {
//            @Override
//            public void gotUserList(List<UserModel> listOfUsers) {
//                Snackbar.make(snackBarLayout, "" + listOfUsers.size() + " Users available!", Snackbar.LENGTH_SHORT).show();
//                UserAdapter userAdapter = new UserAdapter(MainActivity.this, listOfUsers, MainActivity.this);
//                recyclerView.setAdapter(userAdapter);
//                userAdapter.notifyDataSetChanged();
//                progressBar.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void error(String error) {
//                Snackbar.make(snackBarLayout, "" + error, Snackbar.LENGTH_SHORT).show();
//                progressBar.setVisibility(View.GONE);
//            }
//        });
    }

    private void initUI() {
        //instantiating the warning text View:
        warningMessage = findViewById(R.id.warningMessage);
        //Instantiating the recyclerView:
        recyclerView = findViewById(R.id.userRecyclerView);
        // setting the layout as view purpose
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        userMessage = findViewById(R.id.userMessage);
        passwordField = findViewById(R.id.passwordField);
        logInBtn = findViewById(R.id.logInBtn);

        bottomSheet = findViewById(R.id.bottomSheet);
        behavior = BottomSheetBehavior.from(bottomSheet);

        closeApp = findViewById(R.id.closeTheApp);

        ipField = findViewById(R.id.ipField);
        saveBtn = findViewById(R.id.saveBtn);
        exitBtn = findViewById(R.id.exitBtn);

        ipBottomSheet = findViewById(R.id.bottomSheetForIp);
        ipBehavior = BottomSheetBehavior.from(ipBottomSheet);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ipField.getText().toString().endsWith("/")) {
                    ipBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    SP SP = new SP(MainActivity.this);
                    SP.saveURL(Constant.IP_MODE_KEY, ipField.getText().toString());

                    Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Constant.BASE_URL = getURL();
                            finish();
                            startActivity(getIntent());

                            // this basically provides animation
                            overridePendingTransition(0, 0);
//                            startActivity(new Intent(MainActivity.this, MainActivity.class)
//                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                            finish();
                            loadData();
                            Log.d("BASE_URL_CHECKING", "run: " + Constant.BASE_URL);
                        }
                    }, 2000);


                } else {
                    Toast.makeText(MainActivity.this, "Ip should end with a / (Forward slash)", Toast.LENGTH_SHORT).show();
                }

            }
        });

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ipBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        closeApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog();
            }
        });
    }


    public String getURL() {
        SP sharedPreferences = new SP(MainActivity.this);
        //Constant.BASE_URL = sharedPreferences.getBaseUrl("root");
        return sharedPreferences.getURL(Constant.IP_MODE_KEY, Constant.IP_URL);
    }

    private void showAlertDialog() {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.app_close_dialog, null);
        TextView setUps = view.findViewById(R.id.setUps);
        TextView yes = view.findViewById(R.id.yes);
        TextView no = view.findViewById(R.id.no);

        setUps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                //saving the value on shared preference:
                ipBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                SP SP = new SP(MainActivity.this);
                String appendedUrl = SP.getURL(Constant.IP_MODE_KEY, Constant.IP_URL);

                ipField.setText(appendedUrl, TextView.BufferType.EDITABLE);
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                System.exit(0);
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.setContentView(view);
        dialog.show();
    }

//    private void loadData() {
//        progressBar.setVisibility(View.VISIBLE);
//
//        SP SP = new SP(MainActivity.this);
//
//        String appendedUrl = SP.getURL(Constant.IP_MODE_KEY, Constant.IP_URL) + "users.php";
//
//        JsonArrayRequest array = new JsonArrayRequest(appendedUrl,
//                this::parseJson,
//                e -> {
//                    warningMessage.setVisibility(View.VISIBLE);
//                    warningMessage.setText("Error: " + e.getMessage());
//                    progressBar.setVisibility(View.GONE);
//                });
//
//        requestQueue.add(array);
//    }
//
//    private void parseJson(JSONArray array) {
//
//        try {
//
//            //Taking the root data as JSON Array:
//            //JSONArray array = new JSONArray(response.body().string());
//            //checking to know, if the data is not available:
//            //Clearing the list if any data already in:
//            list.clear();
//            if (array.length() > 0) {
//                //Making the warning text Disable:
//                warningMessage.setVisibility(View.GONE);
//                recyclerView.setVisibility(View.VISIBLE);
//                //Traversing through all the array element:
//                for (int i = 0; i < array.length(); i++) {
//                    JSONObject single = array.getJSONObject(i);
//                    //taking particular element:
//                    String UserName = single.getString("UserName");
//                    int TabletUser = single.getInt("TabletUser");
//                    int UserID = single.getInt("UserID");
//                    int PinCode = single.getInt("PinCode");
//                    String strQRCode = single.getString("strQRCode");
//                    int GroupId = single.getInt("GroupId");
//
//                    UserModel model = new UserModel(UserName, TabletUser, UserID, PinCode, strQRCode, GroupId);
//                    list.add(model);
//                }
//
//                UserAdapter adapter = new UserAdapter(MainActivity.this, list, MainActivity.this);
//                recyclerView.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
//                progressBar.setVisibility(View.GONE);
//
//            } else {
//                //If any error happen make it visible and show a warning message:
//                warningMessage.setVisibility(View.VISIBLE);
//                warningMessage.setText("No data found!");
//                progressBar.setVisibility(View.GONE);
//            }
//
//        } catch (Exception e) {
//            //If any error happen make it visible and show a warning message:
//            warningMessage.setVisibility(View.VISIBLE);
//            warningMessage.setText("Error: " + e.getMessage());
//            progressBar.setVisibility(View.GONE);
//        }
//    }

    @Override
    public void clickOnUser(String name, String pinCode, int userId) {
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        userMessage.setText(String.format("Enter pin for %s", name));

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(passwordField.getText().toString().trim())) {
                    passwordField.setError("Enter password");
                    passwordField.requestFocus();
                    return;
                }
                String pin = passwordField.getText().toString();
                if (pin.equals(pinCode)) {
                    Intent intent = new Intent(MainActivity.this, Home.class);
                    intent.putExtra("name", name);
                    intent.putExtra("id", userId);
                    startActivity(intent);
                } else {
                    passwordField.setError("Wrong Credential");
                }
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
    }

    private class AsyncOperations extends AsyncTask<Void, Void, List<UserModel>> {
        List<UserModel> listOfUser = new ArrayList<>();
        String baseUrl;

        public AsyncOperations(String baseUrl) {
            this.baseUrl = baseUrl;
            Log.d("BASE_URL", "AsyncOperations: " + baseUrl);
        }

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This will normally run on a background thread. But to better
         * support testing frameworks, it is recommended that this also tolerates
         * direct execution on the foreground thread, as part of the {@link #execute} call.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param voids The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected synchronized List<UserModel> doInBackground(Void... voids) {

            parseJson(new UserList() {
                @Override
                public void gotUserList(List<UserModel> listOfUsers) {
                    Snackbar.make(snackBarLayout, "" + listOfUsers.size() + " Users available!", Snackbar.LENGTH_SHORT).show();
                    UserAdapter userAdapter = new UserAdapter(MainActivity.this, listOfUsers, MainActivity.this);
                    recyclerView.setAdapter(userAdapter);
                    userAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void error(String error) {
                    Snackbar.make(snackBarLayout, "" + error, Snackbar.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            });
            return listOfUser;
        }

        @Override
        protected synchronized void onPostExecute(List<UserModel> listOfUsers) {
            super.onPostExecute(listOfUsers);

        }

        private void parseJson(UserList userListInterface) {

            List<UserModel> innerList = new ArrayList<>();

            JsonArrayRequest response = new JsonArrayRequest(baseUrl,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray array) {
                            try {
                                innerList.clear();
                                if (array.length() > 0) {
                                    //Making the warning text Disable:
                                    warningMessage.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                    //Traversing through all the array element:
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject single = array.getJSONObject(i);
                                        //taking particular element:
                                        String UserName = single.getString("UserName");
                                        int TabletUser = single.getInt("TabletUser");
                                        int UserID = single.getInt("UserID");
                                        String PinCode = single.getString("PinCode");


                                        UserModel model = new UserModel(UserName, TabletUser, UserID, PinCode);
                                        //list.add(model);
                                        innerList.add(model);
                                    }
                                    userListInterface.gotUserList(innerList);
                                }

                            } catch (Exception e) {
                                userListInterface.error(e.getMessage());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            userListInterface.error(error.getMessage());
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", Constant.TOKENS);
                    return params;
                }
                //e -> userListInterface.error(e.getMessage()));
            };
            requestQueue.add(response);

        }


    }
}