package com.example.weather;

import android.content.Context;
import android.content.Intent;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
    private LottieAnimationView loading;
    TextView Login;
    private EditText Name,Email,Password;
    private String name,email,password;
    private AppCompatButton signUp;
    private static Animation shakeAnim;

    private FirebaseAuth mAuth;
    private ImageView gIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        viewInit();
        mAuth=FirebaseAuth.getInstance();
        gIcon=findViewById(R.id.googleIconBtn);

        shakeAnim= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.shake);
        signUp.setOnClickListener(v -> {
            name=Name.getText().toString();
            email=Email.getText().toString();
            password=Password.getText().toString();
            if(!name.isEmpty()){
                if(!email.isEmpty()){
                    if(!password.isEmpty() &&(password.length()>7&&password.length()<19)){
                        if(EmailVaild.isValidEmail(email)){
                            loading.setVisibility(View.VISIBLE);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    AuthenticationWithCreaditonal(name,email,password);
                                    loading.setVisibility(View.GONE);
                                }
                            },2000);

                        }
                        else{


//                             Toast.makeText(this, "Email not vaild", Toast.LENGTH_SHORT).show();
                            shakeAnimation(Email,shakeAnim,"Email not vaild",SignUpActivity.this);
                        }
                    }
                    else{
                        if(password.equals("")){
                            shakeAnimation(Password,shakeAnim,"Password is blank",getApplicationContext());

                        }else {
                            shakeAnimation(Password,shakeAnim,"password minimum character 8 to 16",getApplicationContext());}
                    }
                }
                else{
                    shakeAnimation(Email,shakeAnim,"Email is blank",getApplicationContext());
                }
            }
            else{
                shakeAnimation(Name,shakeAnim,"Name is blank",getApplicationContext());
            }
        });
        Login.setOnClickListener(v -> {
            startActivity(new Intent(this, SignInActivity.class));
        });
    }

    public void shakeAnimation(EditText getEdit, Animation Anim , String Error, Context context){

        getEdit.startAnimation(Anim);
        changeBorderColor(getEdit, context.getResources().getColor(R.color.red));
        getEdit.setError(Error);
        Anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                // Remove the red border after animation ends
                changeBorderColor(getEdit, context.getResources().getColor(R.color.original_edittext_color));
                // Revert the EditText to its normal state
                getEdit.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }
    private void viewInit(){
        loading=findViewById(R.id.lottieAnimationView);
        loading.setVisibility(View.GONE);
        Login=findViewById(R.id.login);
        Name=findViewById(R.id.name_sign_up);
        Email=findViewById(R.id.email_sign_up);
        Password=findViewById(R.id.password_sign_up);
        signUp=findViewById(R.id.SignUpBtn);
    }
    private void changeBorderColor(EditText editText, int color) {
        GradientDrawable drawable = (GradientDrawable) editText.getBackground();
        drawable.setStroke(2, color); // 2 is the border width
    }
    private void AuthenticationWithCreaditonal(String userName,String userEmail,String userPassword){
        mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user=mAuth.getCurrentUser();
                            if(user!=null){
                                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Intent i=new Intent(getApplicationContext(), EmailVerificationActivity.class);
                                            i.putExtra("name",userName);
                                            i.putExtra("email",userEmail);
                                            startActivity(i);
                                            finish();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("eeee",e.getLocalizedMessage());
                                    }
                                });
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                        }
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("eeee",e.getLocalizedMessage());
                    }
                });
    }
}