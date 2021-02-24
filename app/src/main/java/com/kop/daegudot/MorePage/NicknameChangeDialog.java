package com.kop.daegudot.MorePage;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kop.daegudot.R;

public class NicknameChangeDialog {
    private Context mContext;
    private TextView currentNickname;
    private EditText newNickname;
    private Button changeBtn;
    private Button cancelBtn;
    boolean check = true;

    public NicknameChangeDialog(Context context){
        mContext = context;
    }

    public void callFunctionForNickname(){
        final Dialog dialog = new Dialog(mContext);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.nickname_change_dialog);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams)params);
        dialog.show();

        currentNickname = dialog.findViewById(R.id.current_nick);
        newNickname = dialog.findViewById(R.id.change_nick);
        changeBtn = dialog.findViewById(R.id.change_btn_nick);
        cancelBtn = dialog.findViewById(R.id.cancel_btn_nick);

        //TODO: 현재 내 정보로부터 별명 가져와서 설정하기
        currentNickname.setText("현재 나의 별명");

        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newNick = newNickname.getText().toString();
                if(newNick.getBytes().length > 0){
                    //TODO: 닉네임 중복검사 하기
                    if(check == true){ //중복 없음
                        //TODO: 변경된 닉네임 서버로 넘기기
                        Toast.makeText(mContext, "별명 변경이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                    else{ //중복 존재
                        Toast.makeText(mContext, "이미 별명이 존재합니다.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(mContext, "변경할 별명을 입력해주세요.", Toast.LENGTH_SHORT).show();
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
