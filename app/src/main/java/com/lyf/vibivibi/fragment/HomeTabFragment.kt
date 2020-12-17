package com.lyf.vibivibi.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lyf.vibi.common.ui.component.HiAbsListFragment
import com.lyf.vibi.ui.item.HiDataItem

class HomeTabFragment : HiAbsListFragment() {
    val dataItems = mutableListOf<HiDataItem<*, *>>()
    val recommendModel = RecommendModel(
        "你们说什么悄悄话，我也想听...",
        "https://i1.hdslb.com/bfs/archive/bd69a3bc979b44fb353017f57ef2f2b9cde8e04b.jpg",
        "4.9万", "895", "10:01", "观察者网"
    )

    companion object {
        fun newInstance(categoryId: String): HomeTabFragment {
            val args = Bundle()
            args.putString("categoryId", categoryId)
            val fragment =
                HomeTabFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun createLayoutManager(): RecyclerView.LayoutManager {
        return GridLayoutManager(context, 2)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataItems.add(RecommendItem(recommendModel))
        dataItems.add(RecommendItem(recommendModel))
        dataItems.add(RecommendItem(recommendModel))
        dataItems.add(RecommendItem(recommendModel))
        dataItems.add(RecommendItem(recommendModel))
        finishRefresh(dataItems)
    }
}