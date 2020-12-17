package com.lyf.vibivibi.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.lyf.vibi.ui.item.HiDataItem
import com.lyf.vibi.ui.item.HiViewHolder
import com.lyf.vibivibi.R
import com.lyf.vibivibi.databinding.ItemRecommendIndexItemBinding

class RecommendItem(val recommendModel: RecommendModel) :
    HiDataItem<RecommendModel, RecommendItem.RecommendHolder>(recommendModel) {
    override fun onBindData(holder: RecommendHolder, position: Int) {
        val context = holder.itemView.context
        holder.binding.data =  recommendModel
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecommendHolder? {
        val inflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ItemRecommendIndexItemBinding>(inflater, getItemLayoutRes(), parent, false)
        return RecommendHolder(binding.root, binding)
    }

    override fun getItemLayoutRes(): Int {
        return R.layout.item_recommend_index_item
    }

    override fun getSpanSize(): Int {
        return 2;
    }

    inner class RecommendHolder(view: View, val binding: ItemRecommendIndexItemBinding) :
        HiViewHolder(view) {
    }
}