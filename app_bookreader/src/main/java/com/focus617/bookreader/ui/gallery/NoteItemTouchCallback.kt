package com.focus617.bookreader.ui.gallery

import android.R.attr
import android.graphics.Color
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber
import java.util.*


class NoteItemTouchCallback(private val adapter: NoteListAdapter) :
    ItemTouchHelper.Callback() {

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlag: Int
        val swipeFlag: Int

        if (recyclerView.layoutManager is GridLayoutManager) {
            dragFlag =
                ItemTouchHelper.DOWN or ItemTouchHelper.UP or ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
            swipeFlag = 0
        } else {
            dragFlag = ItemTouchHelper.DOWN or ItemTouchHelper.UP
            swipeFlag = ItemTouchHelper.END
        }
        return makeMovementFlags(dragFlag, swipeFlag)

    }

    override fun onMove(
        recyclerView: RecyclerView,
        source: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        if (source.itemViewType != target.itemViewType) {
            return false
        }

        val fromPosition: Int = source.adapterPosition
        val toPosition: Int = target.adapterPosition

        // Notify the adapter of the move
        Timber.d("onItemMove(fromPosition: $fromPosition, toPosition: $toPosition)")

        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition - 1) {
                Collections.swap(adapter.currentList, i, i + 1)
            }
        } else if (fromPosition > toPosition) {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(adapter.currentList, i, i - 1)
            }
        }
        adapter.notifyItemMoved(fromPosition, toPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        if (attr.direction == ItemTouchHelper.END) {
            adapter.currentList.removeAt(position)
            adapter.notifyItemRemoved(position)
        }
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)

        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            viewHolder?.itemView?.setBackgroundColor(Color.GRAY)
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)

        viewHolder.itemView.setBackgroundColor(0)
    }
}