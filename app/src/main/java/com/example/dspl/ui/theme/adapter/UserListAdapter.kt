package com.example.dspl.ui.theme.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dspl.R
import com.example.dspl.databinding.UserInfoItemBinding
import com.example.dspl.ui.theme.model.UserTasks


class UserListAdapter(val context: Context, var list:ArrayList<UserTasks>) : RecyclerView.Adapter<UserListAdapter.UserListAdapterViewHolder>() {
    var mContext: Context? = null
    var onItemClick: ((UserTasks) -> Unit)? = null
    class UserListAdapterViewHolder(val mBinding: UserInfoItemBinding): RecyclerView.ViewHolder(mBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListAdapterViewHolder {
        mContext = parent.context
        val mBinding = UserInfoItemBinding.inflate(LayoutInflater.from(mContext!!), parent, false)
        return UserListAdapterViewHolder(mBinding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: UserListAdapterViewHolder, position: Int) {
        var model = list[position]
       var  adapter = UserInfoAdapter(mContext!!, model.tasks)
        val manager = LinearLayoutManager(mContext)
        holder.mBinding.recyclerView.layoutManager = manager
        holder.mBinding.recyclerView.setAdapter(adapter)


        holder.mBinding.clSearch.setOnClickListener {
            onItemClick?.invoke( list[position])
        }
        setScaleAnimation(holder.itemView);

    }

    fun refreshList(list: ArrayList<UserTasks>) {
        this.list=list
        notifyDataSetChanged()
    }
    private fun setScaleAnimation(view: View) {
        val animation: Animation = AnimationUtils.loadAnimation(mContext, R.anim.scale_in_list)
        view.startAnimation(animation)
    }
}