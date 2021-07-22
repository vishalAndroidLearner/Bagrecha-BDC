package com.bagrechatech.bagrechabdc;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yalantis.ucrop.UCrop;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HomeFragment extends Fragment {

    private static final int RC_CAMERA = 100;
    private static boolean calledAlready = false;

    private Spinner stationSpinner;
    private EditText customerNameEt;
    private EditText customerContactNoEt;
    private EditText customerAddressEt;
    private EditText customerRemarksEt;
    private String cardImageUrl;
    private ImageView cardImageView;
    private ProgressBar progressBar;
    private ImageButton captureImageButton;
    private Button sendButton;

    public static ArrayList<String> stationsList;
    public static ArrayAdapter stationListAdapter;

    //Photo Capturing
    private Uri imageUri;
    private String currentPhotoPath;

    //Firebase Variables
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference customersReference;
    private DatabaseReference stationsReference;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        if (!calledAlready)
        {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            calledAlready = true;
        }

        stationSpinner = view.findViewById(R.id.station_spinner);
        customerNameEt = view.findViewById(R.id.customerNameEt);
        customerContactNoEt = view.findViewById(R.id.customerContactNoEt);
        customerAddressEt = view.findViewById(R.id.customerAddressEt);
        customerRemarksEt = view.findViewById(R.id.customerRemarksEt);
        cardImageView = view.findViewById(R.id.cardImageView);
        progressBar = view.findViewById(R.id.progressBar);
//        captureImageButton = view.findViewById(R.id.captureImageButton);
        sendButton = view.findViewById(R.id.sendButton);
        

        // Initialize firebase variables
        firebaseDatabase = FirebaseDatabase.getInstance();
        stationsReference = firebaseDatabase.getReference().child("Stations");
        customersReference = firebaseDatabase.getReference().child("Customers");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference().child("Card Images");

        stationsList = new ArrayList<>();
        stationListAdapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_dropdown_item,stationsList);
        stationSpinner.setAdapter(stationListAdapter);
        stationListAdapter.notifyDataSetChanged();

        stationsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                stationsList.clear();

                for (DataSnapshot childSnapshot :
                        snapshot.getChildren()) {
                    stationsList.add(childSnapshot.getValue(String.class));
                }

                stationListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        cardImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        imageUri = Uri.fromFile(photoFile);
                        Uri photoURI = FileProvider.getUriForFile(getActivity(),
                                "com.bagrechatech.bagrechabdc",
                                photoFile);

                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
                        startActivityForResult(takePictureIntent, RC_CAMERA);
                    }
                }

            }
        });
        
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String stationName = stationSpinner.getSelectedItem().toString();
                String customerName = customerNameEt.getText().toString();
                String customerContactNo = customerContactNoEt.getText().toString();
                String customerAddress = customerAddressEt.getText().toString();
                String customerRemarks = customerRemarksEt.getText().toString();
                if(!stationName.equals("---------- SELECT STATION ----------") && !customerName.equals("") && !customerContactNo.equals("") && !customerAddress.equals("") && !cardImageUrl.equals("")){

                    CustomerModel customer = new CustomerModel(stationName,customerName,customerContactNo,customerAddress,customerRemarks,cardImageUrl);
                    customersReference.push().setValue(customer);

                }else{
                    Toast.makeText(getActivity(), "Please fill all the Required fields.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_CAMERA && resultCode == Activity.RESULT_OK){
            if(imageUri != null){
                startCrop(imageUri);
            }
        }else if(requestCode == UCrop.REQUEST_CROP && resultCode == Activity.RESULT_OK){
            Uri imageUriResultCrop = UCrop.getOutput(data);
            if(imageUriResultCrop != null){
                progressBar.setVisibility(View.VISIBLE);
                StorageReference photoRef = storageReference.child(imageUriResultCrop.getLastPathSegment());
                UploadTask uploadTask = photoRef.putFile(imageUriResultCrop);

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                cardImageUrl = uri.toString();
                                Glide.with(getContext())
                                        .load(uri)
                                        .listener(new RequestListener<Drawable>() {
                                            @Override
                                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                progressBar.setVisibility(View.INVISIBLE);
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                progressBar.setVisibility(View.INVISIBLE);
                                                return false;
                                            }
                                        })
                                        .into(cardImageView);
                            }
                        });
                    }
                });
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void startCrop(Uri uri){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String destinationFileName = "JPEG_" + timeStamp;
        //String destinationFileName = SAMPLE_CROPPED_IMAGE_NAME;
        destinationFileName = destinationFileName + ".jpeg";

        UCrop uCrop = UCrop.of(uri,Uri.fromFile(new File(getContext().getCacheDir(),destinationFileName)));
        uCrop.withAspectRatio(16,9);
        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(100);
        options.setMaxBitmapSize(5000);
        uCrop.withOptions(options);
        uCrop.withMaxResultSize(2500,2500);
//        uCrop.start(getActivity());
        uCrop.start(getContext(),this);

    }
}