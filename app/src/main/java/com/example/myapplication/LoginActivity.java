package com.example.myapplication;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.sql.SqlUtils;
import com.example.myapplication.utils.SpUtils;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.Utils;


/**
 * login page
 */

public class LoginActivity extends BaseActivity {
  EditText mNum;
  EditText mPass;
  Button   mLogin;
  CheckBox compatCheckBox;
  private SqlUtils sql;

  @Override
  int getLayoutID() {
    return R.layout.activity_login;
  }

  @Override
  protected void initView() {
    mNum = findViewById(R.id.Activity_Login_Number);
    mPass = findViewById(R.id.Activity_Login_Password);
    mLogin = findViewById(R.id.Activity_Login_Login);
    compatCheckBox = findViewById(R.id.Activity_Login_Chk);
    mNum = findViewById(R.id.Activity_Login_Number);
    mNum = findViewById(R.id.Activity_Login_Number);
    sql = new SqlUtils(mActivity);
    compatCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SpUtils.putBoolean("chk", isChecked);
      }
    });
    if (SpUtils.getBoolean("chk", true)) {
      mNum.setText(SpUtils.getString(StringUtils.PHONE, ""));
      mPass.setText(SpUtils.getString(StringUtils.PASSWORD, ""));
    }
    mLogin.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String num = mNum.getText().toString().trim();
        String pass = mPass.getText().toString().trim();
        if (TextUtils.isEmpty(num) || TextUtils.isEmpty(pass)) {
          Toast.makeText(LoginActivity.this, Utils.getString(R.string.empty_input), Toast.LENGTH_SHORT).show();
          return;
        }
        Login(num, pass);
      }
    });
    findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
      }
    });
  }

  //get login
  public void Login(final String num, final String pass) {
    if (!sql.findPass(num).equals(pass)) {
      Utils.showToast(Utils.getString(R.string.pass_error));
      return;
    }
    SpUtils.putString(StringUtils.PHONE, num);
    SpUtils.putString(StringUtils.PASSWORD, pass);
    Utils.showToast(Utils.getString(R.string.login_success));
    startActivity(new Intent(this, MainActivity.class));
    mActivity.finish();
  }


}
