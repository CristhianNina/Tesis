package com.example.tesis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private Button mButtonSignOut;

    private FirebaseAuth mAuth;

    private TextView mTextViewName;
    private TextView mTextViewEmail;

    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mButtonSignOut = (Button) findViewById(R.id.btnSignOut);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mTextViewName = (TextView) findViewById(R.id.textViewName);
        mTextViewEmail = (TextView) findViewById(R.id.textViewEmail);


        mButtonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth.signOut();

                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                finish();

            }
        });

        getUserInfo();

    }

    private void getUserInfo(){

        String id = mAuth.getCurrentUser().getUid();       //para obtener el id del usuario
        mDatabase.child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){      //si el nodo existe...
                    String name = dataSnapshot.child("name").getValue().toString();     //obtiene los valores de "name" la BDD
                    String email = dataSnapshot.child("email").getValue().toString();   //obtiene los valores de "email" la BDD

                    mTextViewName.setText(name);        //pone los datos dentro de los textView
                    mTextViewEmail.setText(email);      //pone los datos dentro de los textView

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }




}