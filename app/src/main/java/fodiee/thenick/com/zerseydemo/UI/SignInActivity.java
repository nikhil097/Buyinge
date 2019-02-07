package fodiee.thenick.com.zerseydemo.UI;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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


public class SignInActivity extends AppCompatActivity {


    Button bSign,bRegister,bMobileSign;
    EditText email_tv,pwd_et;

    FirebaseAuth auth;

    ProgressBar signInProgess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        bSign=findViewById(R.id.sign_in_btn);
        bRegister=findViewById(R.id.already_sign_up_button);

        email_tv=findViewById(R.id.email_et_sign);
        pwd_et=findViewById(R.id.password_et_signin);
        bMobileSign=findViewById(R.id.mobileSignIn_btn);

        signInProgess=findViewById(R.id.signProgress);

        getSupportActionBar().setTitle("Sign in");

        auth =FirebaseAuth.getInstance();

        bSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signInProgess.setVisibility(View.VISIBLE);

                if(email_tv.getText().toString().trim().length()>0 && pwd_et.getText().toString().trim().length()>=6)
                auth.signInWithEmailAndPassword(email_tv.getText().toString().trim(),pwd_et.getText().toString().trim()).addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful())
                        {
                            if(auth.getCurrentUser().isEmailVerified())
                            {
                            Toast.makeText(getApplicationContext(),"SignIn Successful",Toast.LENGTH_SHORT).show();
                            finish();
                            }
                            else{
                                auth.signOut();
                                Toast.makeText(SignInActivity.this, "Verify your email first.", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else{
                            Toast.makeText(SignInActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                        }
                        signInProgess.setVisibility(View.GONE);

                    }
                });
                else{
                    Toast.makeText(getApplicationContext(),"Please enter valid username and password",Toast.LENGTH_SHORT).show();
                }
            }
        });

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this,SignUpActivity.class));
                finish();
            }
        });


        bMobileSign.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               startActivity(new Intent(SignInActivity.this,SigniN.class));
                                           }
                                       });




    }
}
