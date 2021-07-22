package com.kop.daegudot.MorePage.MyInformation;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kop.daegudot.Network.More.MyInfo.NicknameUpdate;
import com.kop.daegudot.Network.More.MyInfo.PasswordUpdate;
import com.kop.daegudot.Network.RestApiService;
import com.kop.daegudot.Network.RestfulAdapter;
import com.kop.daegudot.R;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class PasswordChangeDialog {
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private Context mContext;
    private EditText newPassword;
    private EditText newPasswordCheck;
    private Button changeBtn;
    private Button cancelBtn;
    private String mToken;

    public PasswordChangeDialog(Context context, String token){
        mContext = context;
        mToken = token;
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
                        PasswordUpdate passwordUpdate = new PasswordUpdate();
                        passwordUpdate.password = newPwd;
                        updatePassword(passwordUpdate);
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

    //비밀번호 변경
    private void updatePassword(PasswordUpdate passwordUpdate) {
        RestfulAdapter restfulAdapter = RestfulAdapter.getInstance();
        RestApiService service =  restfulAdapter.getServiceApi(mToken);
        Observable<Long> observable = service.updateUserPassword(passwordUpdate);

        mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Long>() {
                    @Override
                    public void onNext(Long response) {
                        Toast.makeText(mContext, "비밀번호 변경이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        Log.d("UPDATE_PASSWORD", "UPDATED" + " " + response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("UPDATE_PASSWORD", e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d("UPDATE_PASSWORD", "complete");
                    }
                })
        );
    }
}
