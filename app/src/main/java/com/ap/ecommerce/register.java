package com.ap.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class register extends AppCompatActivity {
       private EditText name, email, password;
       private ProgressDialog loadingbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name = findViewById(R.id.input_name);
        email = findViewById(R.id.input_email);
        password = findViewById(R.id.input_password);
        loadingbar  = new ProgressDialog(this);




    }

    public void sign_up(View view) {
       String uname = name.getText().toString();
       String uemail = email.getText().toString();
       String upassword = password.getText().toString();
        if(uname.isEmpty() | uemail.isEmpty() | upassword.isEmpty()){
            Toast.makeText(this, "Do not leave any of the Fields Empty", Toast.LENGTH_LONG).show();
        }
        else {
            loadingbar.setTitle("Create Account");
            loadingbar.setMessage("We are checking the credientials");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();
            ValidateEmail(uname,uemail,upassword);

        }

    }

    private void ValidateEmail(final String uname, final String uemail, final String upassword) {
        final DatabaseReference rootref = FirebaseDatabase.getInstance().getReference();
        rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if((dataSnapshot.child("Users").child(uemail).exists())){
                    Toast.makeText(register.this, "Email already exists !!", Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();
                }else{
                    HashMap<String, Object> userData = new HashMap<>();
                    userData.put("name",uname);
                    userData.put("email",uemail);
                    userData.put("password",upassword);
                    rootref.child("Users").child(uemail).updateChildren(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(register.this, "Account Created Successfully!!", Toast.LENGTH_SHORT).show();
                                loadingbar.dismiss();
                                startActivity(new Intent(register.this,MainActivity.class));
                            }
                            else{
                                loadingbar.dismiss();
                                Toast.makeText(register.this, "Something Error Happened!!", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(register.this, "Somethig Happended!!", Toast.LENGTH_SHORT).show();

            }
        });

    }
}
