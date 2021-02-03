//package com.kop.daegudot.KakaoMap;
//
//import android.content.Context;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.ItemTouchHelper;
//import androidx.recyclerview.widget.RecyclerView;
//
/** Recyclerview drag to move & swipe to delete
 * use when it need
 */
//public class ScheduleItemTouchHelperCallback extends ItemTouchHelper.Callback {
//  //  Context mContext;
//    ScheduleBSAdapter mAdapter;
//
//    ScheduleItemTouchHelperCallback(ScheduleBSAdapter adapter) {
//   //     mContext = context;
//        mAdapter = adapter;
//    }
//
//    public interface OnItemMoveListener {
//        boolean onItemMove(int fromPosition, int toPosition);
//        void onItemSwipe(int position);
//    }
//
//    private OnItemMoveListener mItemMoveListener;
//    public void ScheduleItemTouchHelper(OnItemMoveListener listener) {
//        mItemMoveListener = listener;
//    }
//
//    @Override
//    public int getMovementFlags(@NonNull RecyclerView recyclerView,
//                                @NonNull RecyclerView.ViewHolder viewHolder) {
//        int dragFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
//        int swipeFlags = ItemTouchHelper.DOWN;
//        return makeMovementFlags(dragFlags, swipeFlags);
//    }
//
//    @Override
//    public boolean onMove(@NonNull RecyclerView recyclerView,
//                          @NonNull RecyclerView.ViewHolder viewHolder,
//                          @NonNull RecyclerView.ViewHolder target) {
//        mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
//        return true;
////        return false;
//    }
//
//    @Override
//    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//        mAdapter.onItemSwipe(viewHolder.getAdapterPosition());
//    }
//}
