package fodiee.thenick.com.SellIt.UI;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

import fodiee.thenick.com.SellIt.Pojo.User;
import fodiee.thenick.com.SellIt.R;

public class NameDetailsEnter extends AppCompatActivity {

    Button bRegister;

    EditText userNameET;

    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_details_enter);


        bRegister=findViewById(R.id.registerMobileUser_btn);

        userNameET=findViewById(R.id.userName_et);

        auth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(userNameET.getText().toString().trim())) {

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(userNameET.getText().toString().trim())
                            .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                            .build();

                    auth.getCurrentUser().updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        firebaseDatabase.getReference().child("Users").child(auth.getCurrentUser().getPhoneNumber()).setValue(new User(userNameET.getText().toString(),null));
                                        Log.d("tag", "User profile updated.");
                                        Intent intent = new Intent(NameDetailsEnter.this, HomePage.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                }
                            });


                } else {

                    Toast.makeText(NameDetailsEnter.this, "Please provide your name", Toast.LENGTH_SHORT).show();

                }
            }
        });


    }
}
