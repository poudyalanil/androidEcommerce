package com.ap.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ap.ecommerce.Model.Users;

public class MainActivity extends AppCompatActivity {
    private EditText email,password;
    private Button sign_in,sign_up;
    private ProgressDialog loadingbar;
    private String parentDB = "Users";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = findViewById(R.id.email_login);
        password = findViewById(R.id.password_login);
        loadingbar = new ProgressDialog(this);


    }
    public void login(View view) {
       String uemail = email.getText().toString();
       String upassword = password.getText().toString();

        if(uemail.isEmpty() | upassword.isEmpty()){
            Toast.makeText(this, "Do not leave any of the Fields Empty", Toast.LENGTH_LONG).show();
        }
        else {
            loadingbar.setTitle("Loggin in");
            loadingbar.setMessage("We are checking the credientials");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();
            ValidateUser(uemail,upassword);
        }


    }

    private void ValidateUser(final String uemail, final String upassword) {
        final DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(parentDB).child(uemail).exists()){

                    Users udata = dataSnapshot.child(parentDB).child(uemail).getValue(Users.class);
                    if(udata.getEmail().equals(uemail)){
                        if(udata.getPassword().equals(upassword)){
                            Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            loadingbar.dismiss();
                            startActivity(new Intent(MainActivity.this,home.class));
                        }
                    }
                }
                if(dataSnapshot.child("admin").child(uemail).exists()){

                    Users udata = dataSnapshot.child(parentDB).child(uemail).getValue(Users.class);
                    if(udata.getEmail().equals(uemail)){
                        if(udata.getPassword().equals(upassword)){
                            Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            loadingbar.dismiss();
                            startActivity(new Intent(MainActivity.this,home.class));
                        }
                    }
                }
                else{
                    loadingbar.dismiss();
                    Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void go_to_register_intent(View view){
        startActivity(new Intent(MainActivity.this,register.class));
    }
}
