package com.firstapp.ma3;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {
    SignInButton signIn;
    GoogleSignInClient googleAuthSignIn;
    FirebaseAuth firebaseAuth;
    public void openGarden(){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button skipButton = (Button) findViewById(R.id.skip_button);
        skipButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openGarden();
            }
        });
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signIn = findViewById(R.id.bt_sign_in);
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleAuthSignIn = GoogleSignIn.getClient(this, googleSignInOptions);
        signIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                resultLauncher.launch(new Intent(googleAuthSignIn.getSignInIntent()));
            }
        });
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null)
        {
            startActivity(new Intent(MainActivity.this,ProfileActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }
    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == 100) {
                        Intent data = result.getData();
                        Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
                        try {
                            GoogleSignInAccount acc = signInAccountTask.getResult(ApiException.class);
                            assert acc != null;
                            firebaseAuthWithGoogle(acc.getIdToken());

                        } catch (ApiException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
    );




    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential  cred = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(cred).addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){
            @Override
            public void onComplete(@NonNull Task<AuthResult> task){
                if(task.isSuccessful()) {
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                    finish();
                    Toast.makeText(MainActivity.this, "Sucessful login", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }
}
