package com.kop.daegudot.MorePage.MyWishlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kop.daegudot.Login.KakaoLogin.GlobalApplication;
import com.kop.daegudot.MorePage.MyWishlist.Database.Wishlist;
import com.kop.daegudot.R;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * 찜 한 장소를 띄움
 */
public class MyWishlistActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "MyWishListActivity";
    private Context mContext;
    private ArrayList<Wishlist> mWishList;
    private RecyclerView mRecyclerView;
    private WishlistAdapter mWishlistAdapter;
    
    WishlistDBHandler wishlistHandler;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wishlist);
        mContext = this;

        Log.d("rx test", "onCreate");
        wishlistHandler = new WishlistDBHandler(mContext);
        wishlistHandler.selectAllWishlist();

        TextView title = findViewById(R.id.title);
        title.setText("찜 목록");
        ImageButton backbtn = findViewById(R.id.backBtn);
        backbtn.setOnClickListener(this);
        
        mRecyclerView = findViewById(R.id.mywish_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    }
    
    
    
    public void updateUI(ArrayList<Wishlist> wishlists) {
        mWishList = wishlists;
        
        mWishlistAdapter = new WishlistAdapter(mContext, mWishList);
        mRecyclerView.setAdapter(mWishlistAdapter);
    }
    
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backBtn) {
            finish();
        }
    }
}
