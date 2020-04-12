package codestromer.com.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class addPathshala extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private Button btnsave;
    private EditText editTextName;
    private EditText editTextLatitude;
    private EditText editTextLongitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pathshala);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        editTextName=findViewById(R.id.editTextName);
        editTextLatitude=findViewById(R.id.editTextLatitude);
        editTextLongitude=findViewById(R.id.editTextLongitude);
        btnsave=findViewById(R.id.btnsave);
    }
    private void saveUserInformation(){
        String name =editTextName.getText().toString().trim();
        double latitude= Double.parseDouble(editTextLatitude.getText().toString().trim());
        double longitude= Double.parseDouble(editTextLongitude.getText().toString().trim());
        UserInformation userInformation=new UserInformation(name,latitude,longitude);

        DatabaseReference postsRef = mDatabase;
        postsRef.push().setValue(userInformation);
        Toast.makeText(this,"Saved",Toast.LENGTH_LONG).show();
    }

    public void addPathshala(View view){
            saveUserInformation();
            editTextName.getText().clear();
            editTextLatitude.getText().clear();
            editTextLongitude.getText().clear();
    }
}
