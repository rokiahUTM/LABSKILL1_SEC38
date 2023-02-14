package com.example.labskill1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    Button loginBtn,cancelBtn;
    EditText usernameET,passwordET;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    TextView tx1, registerTV, forgotPwdTV;
    int counter = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pref = getSharedPreferences("login_details",MODE_PRIVATE);

        usernameET = (EditText)findViewById(R.id.usernameET);
        passwordET = (EditText)findViewById(R.id.passwordET);
        loginBtn = (Button)findViewById(R.id.loginBtn);
        cancelBtn = (Button)findViewById(R.id.cancelLoginBtn);
        tx1 = (TextView)findViewById(R.id.textView3);
        registerTV = (TextView) findViewById(R.id.registerTV);
        forgotPwdTV = (TextView) findViewById(R.id.forgotPwdTV);

        tx1.setVisibility(View.GONE);
        if(pref.contains("username"))
            loginBtn.setEnabled(true);


        loginBtn.setOnClickListener(new ProcessButtonClick());
        cancelBtn.setOnClickListener(new ProcessButtonClick());
        registerTV.setOnClickListener(new ProcessButtonClick());

    }

    public void processlogin(){
        String username = pref.getString("username","admin@123");
        String password = pref.getString("password","admin@123");

        if(usernameET.getText().toString().equals(username) &&
                passwordET.getText().toString().equals(password)) {
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(getApplicationContext(), "Wrong Credentials",Toast.LENGTH_SHORT).show();

            tx1.setVisibility(View.VISIBLE);
            tx1.setBackgroundColor(Color.RED);
            counter--;
            tx1.setText(Integer.toString(counter));

            if (counter == 0) {
                loginBtn.setEnabled(false);
            }
        }
    }

    public void registerUser(){
        final AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.register_dialog,null);

        final EditText newUsernameET = (EditText)mView.findViewById(R.id.newUsernameET);
        final EditText newPasswordET = (EditText)mView.findViewById(R.id.newPasswordET);
        final EditText newEmailET = (EditText)mView.findViewById(R.id.newEmailET);
        Button btn_cancel = (Button)mView.findViewById(R.id.btn_cancel);
        Button btn_okay = (Button)mView.findViewById(R.id.btn_okay);

        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);

        btn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = newUsernameET.getText().toString();
                String password = newPasswordET.getText().toString();
                String email = newEmailET.getText().toString();
                if(!username.isEmpty()&&!password.isEmpty()&&!email.isEmpty()){
                    editor = pref.edit();
                    editor.putString("username",username);
                    editor.putString("password",password);
                    editor.putString("email",email);
                    editor.commit();
                    loginBtn.setEnabled(true);
                }
                alertDialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }


    class ProcessButtonClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.loginBtn:       processlogin();
                                          break;
                case R.id.cancelLoginBtn: finish();
                                          break;
                case R.id.registerTV:  registerUser();
                                       break;
            }
        }
    }
}