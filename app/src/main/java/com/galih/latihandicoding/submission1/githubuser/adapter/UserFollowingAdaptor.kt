package com.galih.latihandicoding.submission1.githubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.galih.latihandicoding.submission1.githubuser.data.model.UserFollowing
import com.galih.latihandicoding.submission1.githubuser.databinding.ItemRowGithubUserBinding

class UserFollowingAdaptor (private val listFollowingAdapter: ArrayList<UserFollowing>) :
    RecyclerView.Adapter<UserFollowingAdaptor.ListViewHolder>() {

    inner class ListViewHolder(val binding: ItemRowGithubUserBinding) : RecyclerView.ViewHolder(binding.root)

    fun setData(items: ArrayList<UserFollowing>) {
        listFollowingAdapter.clear()
        listFollowingAdapter.addAll(items)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(ItemRowGithubUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val githubUser = listFollowingAdapter[position]
        Glide.with(holder.itemView.context)
            .load(githubUser.avatar)
            .apply(RequestOptions().override(150, 150))
            .into(holder.binding.ivPhoto)
        holder.binding.tvUserName.text = githubUser.userName
        holder.binding.tvAdres.text = githubUser.adress
        holder.binding.tvCompany.text = githubUser.company
        holder.binding.tvFollowerInt.text = githubUser.followers.toString()
        holder.binding.tvFollowingInt.text = githubUser.following.toString()
    }

    override fun getItemCount(): Int {
        return listFollowingAdapter.size
    }


}