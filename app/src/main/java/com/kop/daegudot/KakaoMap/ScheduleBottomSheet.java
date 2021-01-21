//package com.kop.daegudot.KakaoMap;
//
//import android.content.Context;
//import android.graphics.Rect;
//import android.util.TypedValue;
//import android.view.View;
//import android.widget.Button;
//
//import androidx.coordinatorlayout.widget.CoordinatorLayout;
//import androidx.recyclerview.widget.ItemTouchHelper;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.kop.daegudot.MySchedule.MainScheduleInfo;
//import com.kop.daegudot.MySchedule.SubScheduleInfo;
//import com.kop.daegudot.R;
//
//import java.util.ArrayList;
//
/** previous version
 *
 */
//
//public class ScheduleBottomSheet {
//    private Context mContext;
//    RecyclerView mRecyclerView;
//    ArrayList<SubScheduleInfo> mSubScheduleList;
//    MainScheduleInfo mMainSchedule;
//
//    ItemTouchHelper mItemTouchHelper;
//    float oldX;
//
//    ScheduleBottomSheet(Context context,
//                        MainScheduleInfo mainSchedule, ArrayList<SubScheduleInfo> subscheduleList) {
//        mContext = context;
//        mMainSchedule = mainSchedule;
//        mSubScheduleList = subscheduleList;
//
//        ViewScheduleBottomSheet();
//    }
//
//    public void ViewScheduleBottomSheet() {
//        mRecyclerView = ((MapMainActivity) mContext).findViewById(R.id.BSScheduleList);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
//        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//
//        // TODO: get list of subschedule address name
//
//        mRecyclerView.setLayoutManager(layoutManager);
//
//        ScheduleBSAdapter adapter = new ScheduleBSAdapter(mSubScheduleList.get(0).getPlaceName(), mContext);
//
//
////        ScheduleItemTouchHelperCallback mCallback = new ScheduleItemTouchHelperCallback(adapter);
////        mItemTouchHelper = new ItemTouchHelper(mCallback);
////        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
//
//        mRecyclerView.setAdapter(adapter);
//
//        float width = (float) 320 / mSubScheduleList.get(0).getPlaceName().size() - 1;
//        mRecyclerView.addItemDecoration(new RecyclerViewDecoration(dpToPx(mContext, width)));
//    }
//
//    // change dp to pixel
//    public int dpToPx(Context context, float dp) {
//        return (int) TypedValue.applyDimension(
//                TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
//    }
//}
//
//// RecyclerView 사이 간격 조절
//class RecyclerViewDecoration extends RecyclerView.ItemDecoration {
//    private final int divWidth;
//
//    public RecyclerViewDecoration(int divWidth) {
//        this.divWidth = divWidth;
//    }
//
//    @Override
//    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//        super.getItemOffsets(outRect, view, parent, state);
//        int position = parent.getChildAdapterPosition(view);
//
//        if (position != parent.getAdapter().getItemCount() -1) {
//            outRect.right = divWidth;
//        }
//    }
//}