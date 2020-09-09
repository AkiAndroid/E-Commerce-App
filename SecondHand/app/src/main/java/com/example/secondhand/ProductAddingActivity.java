package com.example.secondhand;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductAddingActivity extends AppCompatActivity {

    String Category,productname,modelname,description,Pricing,Duration,saveCurrentDate,saveCurrentTime;
    EditText product_name,model_name,Description,pricing,duration;
    ImageView add_image;
    Button publish_button;
    Uri Image_uri;
    DatabaseReference ProductRef;
    ProgressDialog loadingbar;
    String productRandomKey,downloadImageUri;
    StorageReference Product_Image_Ref;

    static final int gallerypick=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_adding);
//declaration of attributes
        loadingbar=new ProgressDialog(this);
        product_name=findViewById(R.id.Product_Name);
        model_name=findViewById(R.id.Model_name);
        Description=findViewById(R.id.Description);
        pricing=findViewById(R.id.price);
        duration=findViewById(R.id.duration);
        add_image=findViewById(R.id.upload_image);
        publish_button=findViewById(R.id.publish_button);

        ProductRef = FirebaseDatabase.getInstance().getReference().child("Products");
        Category =getIntent().getExtras().get("category").toString();
        Product_Image_Ref = FirebaseStorage.getInstance().getReference().child("Product Image");


        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //selecting images from gallery
                Gallery();
            }
        });

        publish_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //checking whether required details are entered or not
                ProductDetailsValidation();
            }
        });
    }

    private void Gallery(){
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,gallerypick);

    }

    private void ProductDetailsValidation()
    {
        productname=product_name.getText().toString();
        modelname=model_name.getText().toString();
        description=Description.getText().toString();
        Pricing=pricing.getText().toString();
        Duration=duration.getText().toString();

        if(TextUtils.isEmpty(productname)){
            product_name.setError("Enter Product Name");
        }
        else if (TextUtils.isEmpty(modelname)){
            model_name.setError("Enter Product Model");
        }
        else if(TextUtils.isEmpty(description)){
            Description.setError("Enter Description");
        }
        else if(TextUtils.isEmpty(Pricing)){
            pricing.setError("Enter Price");
        }
        else if (TextUtils.isEmpty(Duration)){
            duration.setError("Enter Duration");
        }
        else if (Image_uri==null){
            Toast.makeText(this, "Image is required", Toast.LENGTH_SHORT).show();
        }
        else{
            //storing image upload date and time
            StoreImageInfo();
        }

    }

    private void StoreImageInfo()
    {
        loadingbar.setTitle("Adding your product");
        loadingbar.setMessage("Just Have a sip of Coffee");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar.show();


        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate=new SimpleDateFormat("MMM,dd,yyyy");
        saveCurrentDate= currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime= currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;

        final StorageReference Filepath=Product_Image_Ref.child(Image_uri.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask=Filepath.putFile(Image_uri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProductAddingActivity.this, "Error: "+ e.toString(), Toast.LENGTH_SHORT).show();
                loadingbar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(ProductAddingActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful())
                        {
                         throw task.getException();
                        }

                        downloadImageUri=Filepath.getDownloadUrl().toString();
                        return Filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        if(task.isSuccessful()){

                            SaveProductInfotoDatabase();
                        }
                    }
                });
            }
        });

    }

    private void SaveProductInfotoDatabase(){

        HashMap<String,Object> productHashMap=new HashMap<>();
        productHashMap.put("pid",productRandomKey);
        productHashMap.put("Date",saveCurrentDate);
        productHashMap.put("Time",saveCurrentTime);
        productHashMap.put("Description",description);
        productHashMap.put("image",downloadImageUri);
        productHashMap.put("category",Category);
        productHashMap.put("Model Name",modelname);
        productHashMap.put("Price",Pricing);
        productHashMap.put("Duration",Duration);

        ProductRef.child(productRandomKey).updateChildren(productHashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful())
                        {
                            downloadImageUri=task.getResult().toString();

                            Intent intent=new Intent(getApplicationContext(),AdminCategoryActivity.class);
                            startActivity(intent);

                            Toast.makeText(ProductAddingActivity.this, "You have Added Your Product Sucessfully", Toast.LENGTH_SHORT).show();
                            loadingbar.dismiss();

                        }
                        else {
                            Toast.makeText(ProductAddingActivity.this, "Error: "+ task.getException().toString(), Toast.LENGTH_SHORT).show();
                            loadingbar.dismiss();
                        }
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==gallerypick && resultCode==RESULT_OK && data!=null){
            Image_uri = data.getData();
            add_image.setImageURI(Image_uri);
        }
    }
}