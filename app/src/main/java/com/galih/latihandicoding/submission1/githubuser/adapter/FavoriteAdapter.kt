package com.galih.latihandicoding.submission1.githubuser.adapter

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.galih.latihandicoding.submission1.githubuser.data.local.FavoriteUser
import com.galih.latihandicoding.submission1.githubuser.data.local.FavoriteUserDao
import com.galih.latihandicoding.submission1.githubuser.data.local.UserDatabase
import com.galih.latihandicoding.submission1.githubuser.databinding.ItemRowGithubUserWithFavoriteChecklistBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteAdapter(private var listFavoriteUser: ArrayList<FavoriteUser>) :
    RecyclerView.Adapter<FavoriteAdapter.ListViewHolder>() {

    private var userDao: FavoriteUserDao?
    private var userDb: UserDatabase?
    private val application = Application()
    init {
        userDb = UserDatabase.getDatabase(application)
        userDao = userDb?.favoriteUserDao()
    }

    fun addToFavorite(
        id: Int,
        name: String,
        username: String,
        followers: Int,
        following: Int,
        avatar: String,
        adress: String,
        company: String,
        repository: Int
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = FavoriteUser(
                id,
                name,
                username,
                followers,
                following,
                avatar,
                adress,
                company,
                repository
            )
            userDao?.addToFavorite(user)
        }
    }

    private suspend fun checkUser(id: Int) = userDao?.checkUser(id)

    fun removeFromFavorite(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            userDao?.removeFromFavorite(id)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(items: ArrayList<FavoriteUser>) {
        listFavoriteUser.clear()
        listFavoriteUser.addAll(items)
        notifyDataSetChanged()
    }


    private lateinit var onItemClickCallback: OnItemClickCallback
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(val binding: ItemRowGithubUserWithFavoriteChecklistBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ListViewHolder {
        return ListViewHolder(
            ItemRowGithubUserWithFavoriteChecklistBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val githubUser = listFavoriteUser[position]
        var checked = false
        val id = githubUser.id
        val name = githubUser.name.toString()
        val username = githubUser.userName.toString()
        val follower = githubUser.followers
        val following = githubUser.following
        val avatar = githubUser.avatar.toString()
        val address = githubUser.adress.toString()
        val company = githubUser.company.toString()
        val repository = githubUser.repository
        CoroutineScope(Dispatchers.IO).launch {
            val count = checkUser(id)
            withContext(Dispatchers.Main) {
                if (count != null) {
                    Log.d("value COUNT", "$count")
                    if (count > 0) {
                        holder.binding.favoriteCheckbox.isChecked = true
                        checked = true
                    } else {
                        holder.binding.favoriteCheckbox.isChecked = false
                        checked = false
                    }

                }
            }
        }
        Glide.with(holder.itemView.context)
            .load(githubUser.avatar)
            .apply(RequestOptions().override(200, 200))
            .into(holder.binding.ivPhoto)
        holder.binding.tvUserName.text = githubUser.userName
        holder.binding.tvFollowerInt.text = githubUser.followers.toString()
        holder.binding.tvFollowingInt.text = githubUser.following.toString()
        holder.binding.tvAdres.text = githubUser.adress
        holder.binding.tvCompany.text = githubUser.company
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listFavoriteUser[position]) }
        holder.binding.favoriteCheckbox.setOnClickListener {
            checked = !checked
            Log.d("checked", "$checked")
            if (checked) {
                addToFavorite(
                    id,
                    name,
                    username,
                    follower,
                    following,
                    avatar,
                    address,
                    company,
                    repository
                )
                Log.d("add to favorite", "username = $username")
                Toast.makeText(holder.itemView.context,"add $username to favorite", Toast.LENGTH_SHORT ).show()
            } else {
                removeFromFavorite(id)
                Log.d("remove from favorite", "username = $username")
                Toast.makeText(holder.itemView.context,"remove $username from favorite",Toast.LENGTH_SHORT ).show()
            }
            holder.binding.favoriteCheckbox.isChecked = checked
        }
    }

    override fun getItemCount(): Int = listFavoriteUser.size

    interface OnItemClickCallback {
        fun onItemClicked(favoriteUser: FavoriteUser)

    }
}

