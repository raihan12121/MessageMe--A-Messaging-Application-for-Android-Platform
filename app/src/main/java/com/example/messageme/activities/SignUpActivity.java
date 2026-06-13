package com.example.messageme.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.messageme.R;
import com.example.messageme.databinding.ActivitySignUpBinding;
import com.example.messageme.utilities.Constants;
import com.example.messageme.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;


public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    private PreferenceManager preferenceManager;
    private String encodedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());

        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setListeners();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setListeners() {
        binding.textSignIn.setOnClickListener(v -> onBackPressed());
        binding.buttonSignUp.setOnClickListener(v -> {
            if (isValidSignUpDetails()) {
                signUp();
            }
        });
        binding.layoutImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });
    }




    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void signUp(){
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        user.put(Constants.KEY_NAME,binding.inputName.getText().toString());
        user.put(Constants.KEY_EMAIL,binding.inputEmail.getText().toString());
        user.put(Constants.KEY_PASSWORD,binding.inputPassword.getText().toString());
        user.put(Constants.KEY_IMAGE,encodedImage);
        database.collection(Constants.KEY_COLLECTION_USERS)
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    loading(false);
                    preferenceManager.putboolean(Constants.KEY_IS_SIGNED_IN,true);
                    preferenceManager.putString(Constants.KEY_USER_ID, documentReference.getId());
                    preferenceManager.putString(Constants.KEY_NAME,binding.inputName.getText().toString());
                    preferenceManager.putString(Constants.KEY_IMAGE, encodedImage);
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    intent.addFlags((Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    startActivity(intent);


                })
                .addOnFailureListener(exception -> {
                    loading(false);
                    showToast("Error: " + exception.getMessage());



                });


    }
    private String getEncodedImage(Bitmap bitmap){
        int previewWidth =150;
        int previewHeight =bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap,previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream =new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[]bytes= byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);

    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result ->{
                if(result.getResultCode()==RESULT_OK){
                    if(result.getData()!=null){
                        Uri imageUri = result.getData().getData();
                                try{
                                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
                                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                    binding.imageprofile.setImageBitmap(bitmap);
                                    binding.textAddImage.setVisibility(View.GONE);
                                    encodedImage = getEncodedImage(bitmap);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                    showToast("Image not found");
                                }

                    }
                }

            });


    private boolean isValidSignUpDetails(){
        if(encodedImage == null){
            showToast("Select profile image");
            return false;
        } else if(binding.inputName.getText().toString().trim().isEmpty()){
            showToast("Enter Name");
            return false;
        } else if(binding.inputEmail.getText().toString().trim().isEmpty()){
            showToast("Enter Email");
            return false;
        } else if(!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()){
            showToast("Enter valid Email");
            return false;
        } else if(binding.inputPassword.getText().toString().trim().isEmpty()){
            showToast("Enter your Password");
            return false;
        } else if(binding.inputConfirmedPassword.getText().toString().trim().isEmpty()){
            showToast("Confirm your password");
            return false;
        } else if(!binding.inputPassword.getText().toString().equals(binding.inputConfirmedPassword.getText().toString())) {
            showToast("Password and Confirm Password must match");
            return false;
        } else {
            return true;
        }






}
    private void loading(Boolean isLoading){
        if(isLoading){
            binding.buttonSignUp.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }else {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.buttonSignUp.setVisibility(View.VISIBLE);
        }
    }
}