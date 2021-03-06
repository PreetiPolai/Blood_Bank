package com.android.BloodBank.UI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.android.BloodBank.Model.UserProfileData;
import com.android.BloodBank.databinding.ActivityCreateprofileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class CreateProfileActivity extends AppCompatActivity {

    //user details
    private String UserName;
    private String UserPhoneNo;
    private String UserAge;
    private String UserWeight;
    private String UserBloodGroup;
    private String UserLastBloodDonationDate;
    private String UserImage;
    private String Status;
    private HashMap<String,String> UserAddress = new HashMap<>();

    //for browsing image we take this object
    private Uri filepath;
    //we will show the image on bitmap
    private Bitmap bitmap;

    //bit
    private ActivityCreateprofileBinding binding;
    private DatePickerDialog.OnDateSetListener dateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateprofileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //remove status and action bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        //manage permission for accessing files for the files
        //on clicking the Browse button
        binding.userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //first manage permission using dexter
                Dexter.withActivity(CreateProfileActivity.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                //If permission is granted we need to take them to search for image
                                Intent intent = new Intent(Intent.ACTION_PICK);
                               intent.setType("image/*");
                               startActivityForResult(Intent.createChooser(intent,"Select Image"),1);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                //ask again for permission in subsequent opening, if not granted in first run
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();

            }
        });

        //set the calender
        setDate();

        //set spinner
        setBloodGroup();

        //add data to cloud store
        binding.submitProfileDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImageToStorage();

            }
        });


    }


    //we need to collect the selected image


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK){
            //if result is okay find the path
            filepath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                binding.userImage.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void getEditTextData() {
        UserName = binding.userName.getEditText().getText().toString();
        UserPhoneNo = binding.userMobileNumber.getEditText().getText().toString();
        UserAge = binding.userAge.getEditText().getText().toString();
        UserWeight = binding.userWeight.getEditText().getText().toString();
        UserAddress.put("Street",binding.userStreet.getEditText().getText().toString());
        UserAddress.put("City",binding.userCity.getEditText().getText().toString());
        UserAddress.put("District",binding.userDistrict.getEditText().getText().toString());
        UserAddress.put("State",binding.userState.getEditText().getText().toString());
        UserAddress.put("PinCode",binding.userPincode.getEditText().getText().toString());
        Log.d("Details",binding.userStreet.getEditText().getText().toString() + " ******* " +binding.userName.getEditText().getText().toString());
    }


    private void setDate(){
        binding.userLastBloodDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                //by default we want the date should be current date so we set this
                DatePickerDialog dialog =  new DatePickerDialog(CreateProfileActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateSetListener, year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });


        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                ++month;
                String date =  day+ " - " + month + " - " + year;
                UserLastBloodDonationDate = date;
                binding.userLastBloodDonation.setText(date);
            }
        };
    }


    private void setBloodGroup(){

        // Spinner Drop down elements
        //A+, A-, B+, B-, AB+, AB-, O+, and O-.
        List<String> BloodGroup = new ArrayList<String>();
        BloodGroup.add("A+");
        BloodGroup.add("A-");
        BloodGroup.add("B+");
        BloodGroup.add("B-");
        BloodGroup.add("AB+");
        BloodGroup.add("AB-");
        BloodGroup.add("O+");
        BloodGroup.add("O-");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, BloodGroup);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        binding.bloodGroupMenu.setAdapter(dataAdapter);

        binding.bloodGroupMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                // On selecting a spinner item
                //store the value of the blood group
                String item = parent.getItemAtPosition(position).toString();
                UserBloodGroup = item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void setCloudFirebase() {

        //set edit text data
        getEditTextData();

        // Add a new document with a generated ID
        String ID = null;
        if(FirebaseAuth.getInstance().getCurrentUser() != null)
            ID = FirebaseAuth.getInstance().getCurrentUser().getUid();



        FirebaseFirestore db = FirebaseFirestore.getInstance();
        UserProfileData userProfileData = new UserProfileData(UserName,UserPhoneNo,UserAge,UserWeight,UserBloodGroup,UserLastBloodDonationDate,UserAddress,UserImage,ID);



        //pass the value to the realtime firebase
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Users").child(ID);
        HashMap<String,String>  hashMap = new HashMap<>();
        hashMap.put("id", ID);
        hashMap.put("name",  UserName );



        //add values to realtimeFirebase
        database.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                             @Override
                                                             public void onComplete(@NonNull Task<Void> task) {
                                                             }
                                                         }
            );




        //Adding the user phone no and type to the fireStore database
        DocumentReference d = db.collection("Users").document(ID);
        d.set(userProfileData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("TAG", "Successful adding document");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error adding document", e);
                    }
                });

    }

    private void uploadImageToStorage(){
        //create firebase storage object
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("File Uploader");
        dialog.show();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference uploader = storage.getReference("Image" + new Random().nextInt(50));

        uploader.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    //get url of the image
                    uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override

                        public void onSuccess(Uri uri) {
                            dialog.dismiss();

                            //uri is the url of the image
                            UserImage = uri.toString();

                            //set cloudFirebase
                            setCloudFirebase();

                            Toast.makeText(CreateProfileActivity.this,"Profile created",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(CreateProfileActivity.this, HomePageActivity.class));
                            finish();
                        }
                    });

                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                        float percent = (100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                        dialog.setMessage("Uploaded :" + (int)percent + " %");
                    }
                });
    }



    private class GeoCoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }

        }
    }

}