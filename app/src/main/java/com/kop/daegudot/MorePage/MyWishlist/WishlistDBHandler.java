package com.kop.daegudot.MorePage.MyWishlist;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.kop.daegudot.Login.KakaoLogin.GlobalApplication;
import com.kop.daegudot.MorePage.MyWishlist.Database.Wishlist;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * 찜 목록 조회, 삭제
 */
public class WishlistDBHandler {
    private final static String TAG = "WishlistDBHandler";
    
    private Context mContext;
    private ArrayList<Wishlist> mWishList;
    
    /* Rx java - local */
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    
    public WishlistDBHandler(Context context) {
        mContext = context;
    }
    
    public void insertWishlist(Wishlist wishlist) {
        insertWishlistRx(wishlist);
    }
    
    public void selectAllWishlist() {
        selectAllWishlistsRx();
    }
    
    public void deleteWishlists(Wishlist wishlist) {
        deleteWishlistRx(wishlist);
    }
    
    public void getWishlistByPlaceId(long id) {
    
    }
    
    private void insertWishlistRx(Wishlist wishlist) {
        Log.d("Rx " + TAG, "insert!");
        
        Completable completable = GlobalApplication.db.wishlistDao().insertWishlist(wishlist);
        
        mCompositeDisposable.add(completable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableCompletableObserver() {
                            @Override
                            public void onComplete() {
                                Log.d("Rx " + TAG, "complete");
                                Toast.makeText(mContext, "찜 목록에 추가되었습니다", Toast.LENGTH_SHORT).show();
                            }
                            
                            @Override
                            public void onError(Throwable e) {
                                Log.d("Rx " + TAG, e.getMessage());
                                Toast.makeText(mContext, "실패하였습니다", Toast.LENGTH_SHORT).show();
                            }
                        })
                );
        
    }
    
    private void selectAllWishlistsRx(){
        Log.d("rx test", "selectallwishlistrx");
        
        Flowable<List<Wishlist>> flowable =
                GlobalApplication.db.wishlistDao().selectAllWishlists();
        
        mCompositeDisposable.add(flowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(wishlists -> {
                    mWishList = new ArrayList<>();
                    mWishList.addAll(wishlists);
                    Log.d("RX " + TAG, "wish size: " + wishlists.size());
                    ((MyWishlistActivity) mContext).updateUI(mWishList);
                })
        );
        
        // Todo: 왜 onComplete가 불리지 않을까?
//        Observable<List<Wishlist>> observable =
//                GlobalApplication.db.wishlistDao().selectAllWishlists();
//
//        mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new DisposableObserver<List<Wishlist>>() {
//                    @Override
//                    public void onNext(List<Wishlist> response) {
//                        Log.d("RX " + TAG, "Next");
//                        mWishList.addAll(response);
//                        Log.d("RX " + TAG, "size: " + mWishList.size());
//                        ((MyWishlistActivity) mContext).updateUI(mWishList);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.d("RX " + TAG, e.getMessage());
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        Log.d("RX " + TAG, "complete");
//                        ((MyWishlistActivity) mContext).updateUI(mWishList);
//                    }
//                })
//        );
//
    }
    
    private void deleteWishlistRx(Wishlist wishlist) {
        Log.d("Rx " + TAG, "delete!");
    
        Completable completable = GlobalApplication.db.wishlistDao().deleteWishlist(wishlist);
    
        mCompositeDisposable.add(completable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableCompletableObserver() {
                            @Override
                            public void onComplete() {
                                Log.d("Rx " + TAG, "complete");
                                Toast.makeText(mContext, "삭제되었습니다", Toast.LENGTH_SHORT).show();
                            }
                        
                            @Override
                            public void onError(Throwable e) {
                                Log.d("Rx " + TAG, e.getMessage());
                                Toast.makeText(mContext, "실패하였습니다", Toast.LENGTH_SHORT).show();
                            }
                        })
                );
    }
    
    private void getWishlistByPlaceIdRx(long placeId) {
        Observable<Wishlist> observable =
                GlobalApplication.db.wishlistDao().selectWishlistByPlaceId(placeId);
    
        mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Wishlist>() {
                    @Override
                    public void onNext(Wishlist response) {
                        Log.d("RX " + TAG, "Next");
                        Toast.makeText(mContext, "response: " + response.attractName, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("RX " + TAG, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d("RX " + TAG, "complete");
                        ((MyWishlistActivity) mContext).updateUI(mWishList);
                    }
                })
        );
    }
}
