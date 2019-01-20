package fodiee.thenick.com.zerseydemo.UI;

import android.content.Intent;
import android.content.pm.SigningInfo;
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

import fodiee.thenick.com.zerseydemo.R;


public class SignUpActivity extends AppCompatActivity {

    Button bRegister,bSignin;
    EditText email_et,pwd_et;

    ProgressBar signUpProgress;

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

        signUpProgress=findViewById(R.id.signUpProgress);

        getSupportActionBar().setTitle("Sign Up");

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signUpProgress.setVisibility(View.VISIBLE);

                if (email_et.getText().toString().trim().length()>0 && pwd_et.getText().toString().trim().length()>=6)
                auth.createUserWithEmailAndPassword(email_et.getText().toString().trim(),pwd_et.getText().toString().trim()).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(),"SignUpActivity successful",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else
                        {
                            //              Log.v("usr","pass");
                            Toast.makeText(getApplicationContext(),"Please try again",Toast.LENGTH_SHORT).show();
                        }
                        signUpProgress.setVisibility(View.GONE);
                    }
                });
                else{
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
