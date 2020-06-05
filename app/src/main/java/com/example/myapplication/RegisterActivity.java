package com.example.myapplication;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.example.myapplication.sql.SqlUtils;
import com.example.myapplication.utils.Utils;


/**
 * sign up page
 */
public class RegisterActivity extends BaseActivity {
  EditText mPhone;
  EditText mPass;
  EditText mAgainPass;
  private SqlUtils sql;

  @Override
  int getLayoutID() {
    return R.layout.activity_register;
  }

  @Override
  protected void initView() {
    mPhone = findViewById(R.id.Activity_Regi_Phone);
    mPass = findViewById(R.id.Activity_Regi_Pass);
    mAgainPass = findViewById(R.id.Activity_Regi_AgainPass);
    sql = new SqlUtils(mActivity);
    findViewById(R.id.Activity_Regi_regi).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String num = mPhone.getText().toString().trim();
        String pass = mPass.getText().toString().trim();
        String againpass = mAgainPass.getText().toString().trim();
        if (TextUtils.isEmpty(num) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(againpass)) {
          Utils.showToast(Utils.getString(R.string.empty_info));
          return;
        }

        if (!pass.equals(againpass)) {
          Utils.showToast(Utils.getString(R.string.pass_not));
          return;
        }
        Register(num, pass);
      }
    });
  }

  //get register
  public void Register(String num, String pass) {
    boolean b = sql.AddUser(num, pass);
    if (b) {
      Utils.showToast(Utils.getString(R.string.register_success));
      mActivity.finish();
    }
  }

}
