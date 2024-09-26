package com.example.dspl.ui.theme.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dspl.R
import com.example.dspl.databinding.UserInfoBinding
import com.example.dspl.ui.theme.model.Task
import com.example.dspl.ui.theme.model.UserTasks

class UserInfoAdapter(val context: Context, var list:ArrayList<Task>) : RecyclerView.Adapter<UserInfoAdapter.UserInfoAdapterViewHolder>() {
    var mContext: Context? = null

    class UserInfoAdapterViewHolder(val mBinding: UserInfoBinding): RecyclerView.ViewHolder(mBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserInfoAdapterViewHolder {
        mContext = parent.context
        val mBinding = UserInfoBinding.inflate(LayoutInflater.from(mContext!!), parent, false)
        return UserInfoAdapterViewHolder(mBinding)
    }

    override fun onBindViewHolder(holder: UserInfoAdapterViewHolder, position: Int) {
        var model = list[position]
        holder.mBinding.taskTitle.text="${position+1}.${model.title}"
        if (model.completed==true){
            holder.mBinding.taskCompletedStatus.setImageResource(R.drawable.ic_tick)
        }else{
            holder.mBinding.taskCompletedStatus.setImageResource(R.drawable.svgviewer_output__1_)
        }
        if (list.size-1==position){
            holder.mBinding.bgView.visibility=View.GONE
        }else{
            holder.mBinding.bgView.visibility=View.VISIBLE
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

}