package com.kop.daegudot.MorePage;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kop.daegudot.Network.More.MyInfo.NicknameUpdate;
import com.kop.daegudot.Network.RestApiService;
import com.kop.daegudot.Network.RestfulAdapter;
import com.kop.daegudot.Network.User.UserResponse;
import com.kop.daegudot.R;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class NicknameChangeDialog {
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private Context mContext;
    private TextView currentNickname;
    private EditText newNickname;
    private Button changeBtn;
    private Button cancelBtn;
    private String mToken;
    private String mNickname;

    public NicknameChangeDialog(Context context, String token, String nickname){
        mContext = context;
        mToken = token;
        mNickname = nickname;
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
        currentNickname.setText(mNickname);

        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newNick = newNickname.getText().toString();
                if(newNick.getBytes().length > 0){
                    //TODO: 닉네임 중복검사 하기
                    selectNickname(newNick);
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

    //닉네임 변경
    private void updateNickname(NicknameUpdate nicknameUpdate) {
        RestfulAdapter restfulAdapter = RestfulAdapter.getInstance();
        RestApiService service =  restfulAdapter.getServiceApi(mToken);
        Observable<Long> observable = service.updateUserNickname(nicknameUpdate);

        mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Long>() {
                    @Override
                    public void onNext(Long response) {
                        Toast.makeText(mContext, "별명 변경이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        Log.d("UPDATE_NICKNAME", "UPDATED" + " " + response);
                        
                        ((MyInformationActivity)mContext).updateNickNameUI(nicknameUpdate.nickname);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("UPDATE_NICKNAME", e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d("UPDATE_NICKNAME", "complete");
                    }
                })
        );
    }

    //닉네임 중복 검사
    private void selectNickname(String nickname) {
        RestfulAdapter restfulAdapter = RestfulAdapter.getInstance();
        RestApiService service =  restfulAdapter.getServiceApi(null);
        Observable<UserResponse> observable = service.checkNickDup(nickname);

        mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<UserResponse>() {
                    @Override
                    public void onNext(UserResponse response) {
                        Toast.makeText(mContext, "중복된 별명 입니다.", Toast.LENGTH_SHORT).show();
                        Log.d("NICKNAME_DUP", "DUPLICATE NICKNAME" + " " + nickname);
                    }

                    @Override
                    public void onError(Throwable e) {
                        NicknameUpdate nicknameUpdate = new NicknameUpdate();
                        nicknameUpdate.nickname = nickname;
                        updateNickname(nicknameUpdate);

                        Toast.makeText(mContext, "별명이 변경되었습니다.", Toast.LENGTH_SHORT).show();
                        Log.d("NICKNAME_DUP", e.getMessage() + " " + nickname);
                    }

                    @Override
                    public void onComplete() {
                        Log.d("NICKNAME_DUP", "complete");
                    }
                })
        );
    }

}
