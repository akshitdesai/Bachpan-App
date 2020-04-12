package codestromer.com.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class myinfo extends AppCompatActivity {
    TextView mname,mclg,mphone,mbpoints,resetTextLink;
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseFirestore mStore;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);
        mname = findViewById(R.id.dvname);
        mclg = findViewById(R.id.dvclg);
        mphone = findViewById(R.id.dvphone);
        mbpoints = findViewById(R.id.dvbpoints);

        resetTextLink = findViewById(R.id.resetPwd);

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();

        userId = mAuth.getCurrentUser().getUid();
        user = mAuth.getCurrentUser();

        DocumentReference documentReference = mStore.collection("users").document(userId);

        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                mphone.setText("Phone No. : " + documentSnapshot.getString("phone_no"));
                mname.setText("Name : " + documentSnapshot.getString("fname"));
                mclg.setText("College : " + documentSnapshot.getString("College"));
                mbpoints.setText("Bachpan Points : "+documentSnapshot.getString("BachpanPoints"));
            }
        });

        resetTextLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetPassword = new EditText(v.getContext());

                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Change Password?");
                passwordResetDialog.setMessage("Enter new Password > 6 character long");
                passwordResetDialog.setView(resetPassword);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //extract he email and send resent link
                        String newPwd = resetPassword.getText().toString();
                        user.updatePassword(newPwd).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(myinfo.this,"Password changed Successfully!",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(myinfo.this,"Password reset failed!!",Toast.LENGTH_SHORT).show();
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

    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }
}
