package com.example.tesis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText mEditTextName;
    private EditText mEditTextEmail;
    private EditText mEditTextPassword;
    private Button mButtonRegister;
    private Button mButtonSendToLogin;

    //Variables de los datos que se van a registrar

    private String name = "";
    private String email = "";
    private String password = "";

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mEditTextName = (EditText) findViewById(R.id.editTextName);
        mEditTextEmail = (EditText) findViewById(R.id.editTextEmail);
        mEditTextPassword = (EditText) findViewById(R.id.editTextPassword);
        mButtonRegister = (Button) findViewById(R.id.btnLogin);
        mButtonSendToLogin = (Button) findViewById(R.id.btnSendToLogin);

        mButtonRegister.setOnClickListener(new View.OnClickListener() { //ebentp para cuando se presione el botón
            @Override
            public void onClick(View v) {

                name = mEditTextName.getText().toString();
                email = mEditTextEmail.getText().toString();
                password = mEditTextPassword.getText().toString();

                if(!name.isEmpty() && !email.isEmpty() && !password.isEmpty()){

                    if(password.length() >=6 ){

                        registerUser();
                    }
                    else{
                        Toast.makeText(MainActivity.this,"El password debe tener almenos 6 caracteres",Toast.LENGTH_SHORT).show();

                    }
                }
                else{

                    Toast.makeText(MainActivity.this,"Debe completar todos los campos",Toast.LENGTH_SHORT).show();

                }

            }
        });

        mButtonSendToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, LoginActivity.class));

            }
        });


    }

    private void registerUser(){

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    Map<String, Object> map = new HashMap<>();
                    map.put("name", name);
                    map.put("email", email);
                    map.put("password", password);

                    String id = mAuth.getCurrentUser().getUid();

                    mDatabase.child("Users").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if (task2.isSuccessful()){  // la tarea es crear datos en la base de datos
                                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                                finish();
                            }
                            else{
                                Toast.makeText(MainActivity.this,"No se pudieron crear los datos correctamente",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });        //nodo hijo que esta dentro de la base de datos // child nos pide un id y setValue nos pide un mapa de valores


                }
                else{

                    Toast.makeText(MainActivity.this,"No se pudo registrar el usuario",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null){        //si el usuario ya inició sesión enviarlo a profile activity

            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            finish();

        }

    }
}









