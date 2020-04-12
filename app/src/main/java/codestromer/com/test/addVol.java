package codestromer.com.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.core.Tag;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class addVol extends AppCompatActivity {

    public static final String TAG = "tag";
    EditText mname,memail,mclg,mphone,menroll;
    CheckBox isAdmin;
    Button mAddbtn;
    ProgressBar mprogressBar;
    FirebaseFirestore mStore;
    String userID,admin,email,name,clg,enroll,phone;
    FirebaseAuth mAuth1,mAuth2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vol);
        mname = findViewById(R.id.dname);
        memail = findViewById(R.id.demail2);
        menroll = findViewById(R.id.denroll);
        mclg = findViewById(R.id.dclg);
        mphone = findViewById(R.id.dPhone);
        isAdmin = findViewById(R.id.dIsAdmin);
        mAddbtn =  findViewById(R.id.dSubmitBtn);
        mprogressBar = findViewById(R.id.dprogressBar2);
        mAuth1 = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        mAddbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = memail.getText().toString();
                name = mname.getText().toString();
                enroll = menroll.getText().toString();
                clg = mclg.getText().toString();
                phone = mphone.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    memail.setError("Email is Required");
                    return;
                }
                if (TextUtils.isEmpty(name)) {
                    mname.setError("Email is Required");
                    return;
                }
                if (TextUtils.isEmpty(clg)) {
                    mclg.setError("College is Required");
                    return;
                }
                if (TextUtils.isEmpty(enroll)) {
                    menroll.setError("Enrollment No. is Required");
                    return;
                }
                if (TextUtils.isEmpty(phone)) {
                    mphone.setError("Phone no. is Required");
                    return;
                }
                if (enroll.length()<6) {
                    menroll.setError("Enter full enrollment Number");
                    return;
                }
                if(isAdmin.isChecked()){
                    admin = "true";
                }else{
                    admin = "false";
                }
                mprogressBar.setVisibility(View.VISIBLE);
                FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                        .setDatabaseUrl("[https://test-c1793.firebaseio.com/")
                        .setApiKey("AIzaSyDEfcys6clCG3m9HrQx3Q-rk6IspbiVOl4")
                        .setApplicationId("test-c1793").build();

                try { FirebaseApp myApp = FirebaseApp.initializeApp(getApplicationContext(), firebaseOptions, "test");
                    mAuth2 = FirebaseAuth.getInstance(myApp);
                } catch (IllegalStateException e){
                    mAuth2 = FirebaseAuth.getInstance(FirebaseApp.getInstance("test"));
                }

                //authantication of user
                mAuth2.createUserWithEmailAndPassword(email,enroll).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mprogressBar.setVisibility(View.GONE);
                            userID = mAuth2.getCurrentUser().getUid();
                            Log.d(TAG," created acc : "+userID);
                            DocumentReference documentReference = mStore.collection("users").document(userID);
                            Map<String,String> user = new HashMap<>();
                            user.put("fname",name);
                            user.put("mail",email);
                            user.put("phone_no",phone);
                            user.put("College",clg);
                            user.put("Enroll",enroll);
                            user.put("Admin",admin);
                            user.put("BachpanPoints","0");
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG,"onSuccess: user Profile is created for "+userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG,"onFailure: "+e.toString());
                                }
                            });
                            mAuth2.signOut();
                            Toast.makeText(addVol.this, "Registred Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            Toast.makeText(addVol.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            mprogressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }
}