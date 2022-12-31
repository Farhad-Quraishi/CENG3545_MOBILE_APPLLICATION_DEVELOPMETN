package com.example.myapplication;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {
    private EditText username;
    private EditText name;
    private EditText email;
    private EditText password;
    private Button signup;
    private TextView loginUser;

    private DatabaseReference mRootRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        username = findViewById(R.id.username);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signup = findViewById(R.id.signup);
        loginUser = findViewById(R.id.login_user);

        mRootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        loginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this,LoginActivity.class));
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textUserName = username.getText().toString();
                String textName = name.getText().toString();
                String textEmail = email.getText().toString();
                String textPassword = password.getText().toString();
                if (TextUtils .isEmpty(textUserName) || TextUtils.isEmpty(textName) || TextUtils.isEmpty(textEmail)
                        || TextUtils.isEmpty(textPassword)){
                    Toast.makeText(SignupActivity.this, " Empty Activity", Toast.LENGTH_SHORT).show();

                }else if( password.length()<8 ){
                    Toast.makeText(SignupActivity.this, "pass length too short", Toast.LENGTH_SHORT).show();
                }else{
                    signupUser (textUserName,textName,textEmail,textPassword);
                }
            }
        });


    }

    private void signupUser(String username, String name, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                HashMap<String,Object> map= new HashMap<>();
                map.put("name", name);
                map.put("email", email);
                map.put("username", username);
                map.put("id", mAuth.getUid());

                mRootRef.child("User").child(mAuth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener
                        (new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                            Toast.makeText(SignupActivity.this,"Update the profile" + "for best expreice",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignupActivity.this,MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignupActivity.this, e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}