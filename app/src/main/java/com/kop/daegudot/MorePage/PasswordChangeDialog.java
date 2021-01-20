package com.kop.daegudot.MorePage;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kop.daegudot.R;

public class PasswordChangeDialog {
    private Context mContext;
    private EditText newPassword;
    private EditText newPasswordCheck;
    private Button changeBtn;
    private Button cancelBtn;

    public PasswordChangeDialog(Context context){
        mContext = context;
    }

    public void callFunctionForPassword(){
        final Dialog dialog = new Dialog(mContext);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.password_change_dialog);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        dialog.show();

        newPassword = dialog.findViewById(R.id.change_pwd1);
        newPasswordCheck = dialog.findViewById(R.id.change_pwd2);
        changeBtn = dialog.findViewById(R.id.change_btn);
        cancelBtn = dialog.findViewById(R.id.cancel_btn);

        changeBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String newPwd= newPassword.getText().toString();
                String checkPwd = newPasswordCheck.getText().toString();
                if(newPwd.getBytes().length > 0 && checkPwd.getBytes().length>0) {
                    if (newPwd.equals(checkPwd)) {
                        //TODO: 변경된 비밀번호 서버로 넘겨주기
                        Toast.makeText(mContext, "비밀번호 변경이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, "비밀번호가 일치하지 않습니다.\n다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(mContext, "변경할 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
