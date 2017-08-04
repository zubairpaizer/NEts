package com.fyp.faaiz.ets;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.fyp.faaiz.ets.auth.LoginActivity;
import com.fyp.faaiz.ets.model.Employee;
import com.fyp.faaiz.ets.model.MessageCode;
import com.fyp.faaiz.ets.session.Session;
import com.fyp.faaiz.ets.util.Parser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class ChangeUserDetails extends AppCompatActivity {

    StorageReference mStorage;
    StorageReference mRStorage;
    FirebaseStorage firebaseStorage;

    private static final int PICK_IMAGE_REQUEST = 1;
    public static final int CAPTURE_IMAGE = 2;

    private static final String KEY_IMAGE = "profile_image";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String PHONE_NUMBER = "phone_number";
    public static final String CNIC = "cnic";
    public static final String UUID = "uuid";
    public static final String ID = "id";


    Toolbar toolbar;
    Session _session;
    ImageView profile_lock_unlock_btn;
    boolean editingStatus = true;

    EditText profile_first_name;
    EditText profile_last_name;
    //EditText profile_email;
    EditText profile_phone;
    EditText profile_cnic;
    ImageView profile_camera;
    ImageView profile_avatar;
    private Bitmap bitmap;
    private Uri filePath;
    private String image = "";
    TextView profile_cnic_count;
    TextView profile_phone_number_count;
    String imgPath = "";
    String selectedImagePath = "";
    public static String URL_REQUEST = "";
    Button profile_update_button;
    private String email_filter_exp = "~#^|$%&*!+,':\";{}[]\\/()<?>";
    private String name_exp = "._-123456890@ ";
    private String phone_exp = "~#^|$%&*!,'.-_:\";{}[]\\/()<?> ";
    ProgressBar progressBar_update;
    public static int USER_ID = 0;
    public static String USER_UUID = "";

    public static String BASE_IMAGE = "https://firebasestorage.googleapis.com/v0/b/nets-8cb47.appspot.com/o/Photos%2F";

    private InputFilter email_filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && email_filter_exp.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    private InputFilter phone_filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && phone_exp.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    private InputFilter name_filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && name_exp.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_details);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        _session = new Session(this);
        String email = _session.getUserDetails().get(Session.KEY_EMAIL);
        toolbar.setTitle("Profile");
        toolbar.setSubtitle(email);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Session session = new Session(getApplicationContext());
        USER_ID = session.getId().get(Session.KEY_ID);
        URL_REQUEST = ApplicationState.REMOTE_BASE_URL + "/employee/" + USER_ID;
        Toast.makeText(this, URL_REQUEST, Toast.LENGTH_LONG).show();

        USER_UUID = session.getUserDetails().get(Session.UUID);
        BASE_IMAGE += USER_UUID + "?alt=media";
        Log.d("BASE", BASE_IMAGE);
        firebaseStorage = FirebaseStorage.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();

        profile_update_button = (Button) findViewById(R.id.profile_update_button);
        progressBar_update = (ProgressBar) findViewById(R.id.progressBar_update);
        profile_first_name = (EditText) findViewById(R.id.profile_first_name);
        profile_last_name = (EditText) findViewById(R.id.profile_last_name);
        //profile_email = (EditText) findViewById(R.id.profile_email);
        profile_phone = (EditText) findViewById(R.id.profile_phone_number);
        profile_cnic = (EditText) findViewById(R.id.profile_cnic);
        profile_camera = (ImageView) findViewById(R.id.profile_camera);
        profile_avatar = (ImageView) findViewById(R.id.profile_avatar);
        profile_phone_number_count = (TextView) findViewById(R.id.profile_phone_number_count);

        try {
            Glide.with(this).load(BASE_IMAGE).into(profile_avatar);
        }catch (Exception ex){
            ex.printStackTrace();
        }

        profile_first_name.setEnabled(false);
        profile_last_name.setEnabled(false);
        //profile_email.setEnabled(false);
        profile_phone.setEnabled(false);
        profile_cnic.setEnabled(false);
        profile_cnic_count = (TextView) findViewById(R.id.profile_cnic_count);
        profile_first_name.setFilters(new InputFilter[]{name_filter});
        profile_last_name.setFilters(new InputFilter[]{name_filter});
        //profile_email.setFilters(new InputFilter[]{email_filter});
        profile_phone.setFilters(new InputFilter[]{phone_filter});


        String first_name = session.getUserDetails().get(Session.FIRST_NAME) != null ? session.getUserDetails().get(Session.FIRST_NAME) : "";
        String last_name = session.getUserDetails().get(Session.LAST_NAME) != null ? session.getUserDetails().get(Session.LAST_NAME) : "";
        String cnic = session.getUserDetails().get(Session.CNIC) != null ? session.getUserDetails().get(Session.CNIC) : "";
        String phone = session.getUserDetails().get(Session.PHONE) != null ? session.getUserDetails().get(Session.PHONE) : "";

        Toast.makeText(this, session.getUserDetails().get(Session.FIRST_NAME), Toast.LENGTH_SHORT).show();

        profile_first_name.setText(first_name);
        profile_last_name.setText(last_name);
        profile_phone.setText(phone);
        profile_cnic.setText(cnic);


        profile_lock_unlock_btn = (ImageView) findViewById(R.id.profile_lock_unlock_btn);
        profile_lock_unlock_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editingStatus == true) {
                    editingStatus = false;
                    profile_lock_unlock_btn.setImageResource(R.drawable.profile_lock);
                    profile_first_name.setEnabled(false);
                    profile_last_name.setEnabled(false);
                    //profile_email.setEnabled(false);
                    profile_phone.setEnabled(false);
                    profile_cnic.setEnabled(false);
                } else {
                    editingStatus = true;
                    profile_lock_unlock_btn.setImageResource(R.drawable.profile_unlock);
                    profile_first_name.setEnabled(true);
                    profile_last_name.setEnabled(true);
                    //profile_email.setEnabled(true);
                    profile_phone.setEnabled(true);
                    profile_cnic.setEnabled(true);
                }
            }
        });

        profile_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        profile_cnic.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (profile_cnic.getText().length() >= 0) {
                    int count = 13;
                    profile_cnic_count.setText("Characters Left : " + (count - profile_cnic.getText().length()));
                    profile_cnic_count.setTextColor(Color.GREEN);

                    if (profile_cnic.getText().length() == 13) {
                        profile_cnic_count.setTextColor(Color.RED);
                    }

                    if (profile_cnic.getText().length() > 13) {
                        profile_cnic_count.setTextColor(Color.RED);
                    }
                }
            }
        });

        profile_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (profile_phone.getText().length() >= 0) {
                    int count = 13;
                    profile_phone_number_count.setText("Characters Left : " + (count - profile_phone.getText().length()));
                    profile_phone_number_count.setTextColor(Color.GREEN);
                    if (profile_phone.getText().length() == 13) {
                        profile_phone_number_count.setTextColor(Color.RED);
                    }
                    if (profile_phone.getText().length() > 13) {
                        profile_phone_number_count.setTextColor(Color.RED);
                    }
                }
            }
        });

        profile_update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
                StorageReference file = mStorage.child("Photos").child(USER_UUID);
                    if(filePath != null) {
                        file.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                hideProgressDialog();
                                Toast.makeText(ChangeUserDetails.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                updateProfile();
            }
        });

    }

    public Uri setImageUri() {
        // Store image in dcim
        File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "image" + new Date().getTime() + ".png");
        Uri imgUri = Uri.fromFile(file);
        this.imgPath = file.getAbsolutePath();
        return imgUri;
    }

    public String getImagePath() {
        return imgPath;
    }

    private void showFileChooser() {
        Toast.makeText(this, "You Clicked", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, ""), PICK_IMAGE_REQUEST);
    }

    private void cameraCapture() {
        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
        startActivityForResult(intent, CAPTURE_IMAGE);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_CANCELED) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                filePath = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    profile_avatar.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == CAPTURE_IMAGE) {
                selectedImagePath = getImagePath();
                profile_avatar.setImageBitmap(decodeFile(selectedImagePath));
                Toast.makeText(this, "Camera", Toast.LENGTH_SHORT).show();
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    public Bitmap decodeFile(String path) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, o);
            // The new size we want to scale to
            final int REQUIRED_SIZE = 70;
            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeFile(path, o2);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;

    }

    public String getAbsolutePath(Uri uri) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    public void showProgressDialog() {
        profile_update_button.setEnabled(false);
        profile_update_button.setText("");
        progressBar_update.setBackgroundColor(Color.GRAY);
        progressBar_update.setVisibility(View.VISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void hideProgressDialog() {
        profile_update_button.setEnabled(true);
        profile_update_button.setText("Update Profile");
        profile_update_button.setBackground(getDrawable(R.drawable.botton_border));
        progressBar_update.setVisibility(View.INVISIBLE);
    }

    private void updateProfile() {
        showProgressDialog();
        int id = _session.getId().get(Session.KEY_ID);
        String URL = ApplicationState.REMOTE_BASE_URL + "/employee/" + id;

        StringRequest request = new StringRequest(Request.Method.PATCH, URL,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onResponse(String response) {

                        if(response.contains("message") && response.contains("code")){
                            MessageCode m = Parser.messageCodes(response);
                            Toast.makeText(ChangeUserDetails.this, m.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }


                        if (response.contains("first_name")) {

                            _session.logoutUser();

                            List<Employee> parse = Parser.parse(response);

                            int local_id = parse.get(0).getId();

                            String local_full_name = parse.get(0).getFirst_name() + " " + parse.get(0).getLast_name();

                            String local_email = parse.get(0).getEmail();

                            String uuid = parse.get(0).getUuid();
                            String first_name_n = parse.get(0).getFirst_name();
                            String last_name = parse.get(0).getLast_name();
                            String cnic = parse.get(0).getCnic();
                            String phone = parse.get(0).getPhone_number();

                            _session.createLoginSession(local_id, local_full_name, local_email,"agent",uuid,first_name_n,last_name,cnic,phone);

                            Toast.makeText(ChangeUserDetails.this, response, Toast.LENGTH_SHORT).show();
                            hideProgressDialog();
                            startActivity(new Intent(getBaseContext(), ChangeUserDetails.class));
                            finish();

                        } else {
                            Toast.makeText(ChangeUserDetails.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        hideProgressDialog();
                        String message = null;
                        if (volleyError instanceof NetworkError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                            Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();
                        } else if (volleyError instanceof ServerError) {
                            message = "The server could not be found. Please try again after some time!!";
                            Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();
                        } else if (volleyError instanceof AuthFailureError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                            Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();

                        } else if (volleyError instanceof ParseError) {
                            message = "Parsing error! Please try again after some time!!";
                            Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();

                        } else if (volleyError instanceof NoConnectionError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                            Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();

                        } else if (volleyError instanceof TimeoutError) {
                            message = "Connection TimeOut! Please check your internet connection.";
                            Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();
                        }

                        NetworkResponse networkResponse = volleyError.networkResponse;
                        String errorMessage = "Unknown error";
                        if (networkResponse == null) {
                            if (volleyError.getClass().equals(TimeoutError.class)) {
                                errorMessage = "Request timeout";
                                Toast.makeText(getApplicationContext(), "" + errorMessage, Toast.LENGTH_SHORT).show();
                            } else if (volleyError.getClass().equals(NoConnectionError.class))
                                errorMessage = "Failed to connect server";
                            Toast.makeText(getApplicationContext(), "" + errorMessage, Toast.LENGTH_SHORT).show();
                        } else {
                            String result = new String(networkResponse.data);
                            try {
                                JSONObject response = new JSONObject(result);
                                String status = response.getString("status");
                                message = response.getString("message");

                                if (networkResponse.statusCode == 404) {
                                    errorMessage = "Resource not found";
                                    Toast.makeText(getApplicationContext(), "" + errorMessage, Toast.LENGTH_SHORT).show();

                                } else if (networkResponse.statusCode == 401) {
                                    errorMessage = message + " Please login again";
                                    Toast.makeText(getApplicationContext(), "" + errorMessage, Toast.LENGTH_SHORT).show();
                                } else if (networkResponse.statusCode == 400) {
                                    errorMessage = message + " Check your inputs";
                                    Toast.makeText(getApplicationContext(), "" + errorMessage, Toast.LENGTH_SHORT).show();
                                } else if (networkResponse.statusCode == 500) {
                                    errorMessage = message + " Something is getting wrong";
                                    Toast.makeText(getApplicationContext(), "" + errorMessage, Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        volleyError.printStackTrace();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                Map<String, String> params = new Hashtable();
                params.put(ID, String.valueOf(USER_ID));
                params.put(UUID, USER_UUID);
                params.put(KEY_IMAGE, image);
                params.put(FIRST_NAME, profile_first_name.getText().toString());
                params.put(LAST_NAME, profile_last_name.getText().toString());
                params.put(PHONE_NUMBER, profile_phone.getText().toString());
                params.put(CNIC, profile_cnic.getText().toString());
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplication());
        queue.add(request);
        //AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
