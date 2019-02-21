package fodiee.thenick.com.SellIt.UI;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

import fodiee.thenick.com.SellIt.Pojo.User;
import fodiee.thenick.com.SellIt.R;


public class SignUpActivity extends AppCompatActivity {

    Button bRegister,bSignin;
    EditText email_et,pwd_et,displayNmae_et;

    ProgressBar signUpProgress;

    FirebaseDatabase firebasedb;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth=FirebaseAuth.getInstance();

        bRegister=findViewById(R.id.register_btn);
        bSignin=findViewById(R.id.already_sign_in_button);
        email_et=findViewById(R.id.email_et);
        pwd_et=findViewById(R.id.password_et);

        firebasedb=FirebaseDatabase.getInstance();

        displayNmae_et=findViewById(R.id.displayNameEt);

        signUpProgress=findViewById(R.id.signUpProgress);

    //    getSupportActionBar().setTitle("Sign Up");

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signUpProgress.setVisibility(View.VISIBLE);

                if (email_et.getText().toString().trim().length()>0 && pwd_et.getText().toString().trim().length()>=6 && displayNmae_et.getText().toString().trim().length()>=1)
                auth.createUserWithEmailAndPassword(email_et.getText().toString().trim(),pwd_et.getText().toString().trim()).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            FirebaseUser user = auth.getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(displayNmae_et.getText().toString().trim()).build();

                            user.updateProfile(profileUpdates);

                            user.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("hi", "Email sent.");
                                                auth.signOut();
                                                finish();
                                                Toast.makeText(SignUpActivity.this, "Email regarding verification sent to your registered email address.Confirm the email and signin", Toast.LENGTH_SHORT).show();
                                        //        firebasedb.getReference().child("Users").child(email_et.getText().toString().trim()).setValue(new User(displayNmae_et.getText().toString().trim(),null));
                                            }
                                            else{
                                                Toast.makeText(SignUpActivity.this, "Problem in sending verification email", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

               //             Toast.makeText(getApplicationContext(),"SignUpActivity successful",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            //              Log.v("usr","pass");
                            Toast.makeText(getApplicationContext(),"Please try again",Toast.LENGTH_SHORT).show();
                        }
                        signUpProgress.setVisibility(View.GONE);
                    }
                });
                else if(displayNmae_et.getText().toString().trim().length()<1)
                {
                    Toast.makeText(SignUpActivity.this, "Provide valid display name", Toast.LENGTH_SHORT).show();
                    signUpProgress.setVisibility(View.GONE);

                }
                else{
                    signUpProgress.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Please enter valid email and password", Toast.LENGTH_SHORT).show();
                }


            }
        });


        bSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this,SignInActivity.class));
                finish();
            }
        });

    }
}
