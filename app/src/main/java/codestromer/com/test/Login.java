package codestromer.com.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class Login extends AppCompatActivity {

    EditText mEmail,mPass;
    Button mLoginBtn;
    FirebaseAuth fAuth;
    FirebaseFirestore mStore;
    ProgressBar progressBar;
    TextView forgotTextLink;
    String userId,madmin="true";
    boolean x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.demail2);
        mPass = findViewById(R.id.dpass);
        progressBar = findViewById(R.id.dprogressBar2);
        fAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        mLoginBtn = findViewById(R.id.dSubmitBtn);
        forgotTextLink = findViewById(R.id.forgotPwd);

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();
                String pass = mPass.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is Required");
                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    mPass.setError("Password is Requied");
                    return;
                }
                if (pass.length() < 6) {
                    mPass.setError("Password Must be greater than 5 characters");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                //authantication of user
                fAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            userId = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = mStore.collection("users").document(userId);
                            documentReference.addSnapshotListener(Login.this, new EventListener<DocumentSnapshot>() {
                                @Override
                                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                                    if (madmin.equals(documentSnapshot.getString("Admin")))
                                        x = true;
                                    else x = false;

                                    if(x){
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(Login.this, "Logged in as Admin", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    }
                                    else{
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(Login.this, "Logged in as Volunteer", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), Main2Activity.class));
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(Login.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
        forgotTextLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetMail = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password?");
                passwordResetDialog.setMessage("Enter your email to recive Reset Link.");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //extract he email and send resent link
                        String mail = resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Login.this, "Reset link is sent to your Email", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this,"Error ! "+e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //closing the dialog
                    }
                });
                passwordResetDialog.create().show();
            }
        });
    }
}