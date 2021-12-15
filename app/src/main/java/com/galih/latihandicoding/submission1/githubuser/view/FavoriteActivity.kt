package com.galih.latihandicoding.submission1.githubuser.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.galih.latihandicoding.submission1.githubuser.adapter.FavoriteAdapter
import com.galih.latihandicoding.submission1.githubuser.data.local.FavoriteUser
import com.galih.latihandicoding.submission1.githubuser.data.model.GithubUser
import com.galih.latihandicoding.submission1.githubuser.databinding.ActivityFavoriteBinding
import com.galih.latihandicoding.submission1.githubuser.viewModel.MainViewModel
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_row_github_user.view.*
import kotlinx.android.synthetic.main.item_row_github_user_with_favorite_checklist.view.*

class FavoriteActivity : AppCompatActivity() {

    private var list: ArrayList<FavoriteUser> = arrayListOf()
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var listAdapter: FavoriteAdapter
    private lateinit var viewModel: MainViewModel
    private var user = GithubUser()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        listAdapter = FavoriteAdapter(list)
        listAdapter.notifyDataSetChanged()
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        listAdapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback {
            override fun onItemClicked(favoriteUser: FavoriteUser) {
                moveIntent(favoriteUser)
            }
        })
        binding.apply {
            rvFavorite.setHasFixedSize(true)
            rvFavorite.layoutManager = LinearLayoutManager(this@FavoriteActivity)
            rvFavorite.adapter = listAdapter
        }
        showLoading(true)
        viewModel.getFavoriteUser()?.observe(this, {
            if (it != null) {
                val list = mapList(it)
                listAdapter.setData(list as ArrayList<FavoriteUser>)
                showLoading(false)
            }
        })
    }

    private fun mapList(users: List<FavoriteUser>): Any {
        val listUser = ArrayList<FavoriteUser>()
        for (user in users) {
            val userMapped = FavoriteUser(
                user.id,
                user.name,
                user.userName,
                user.followers,
                user.following,
                user.avatar,
                user.adress,
                user.company,
                user.repository
            )
            listUser.add(userMapped)
        }
        return listUser
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowTitleEnabled(false)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun moveIntent(favoriteUser: FavoriteUser) {
        val githubUserMoveData = Intent(this@FavoriteActivity, DetailActivity::class.java).apply {
            user.id = favoriteUser.id
            user.name = favoriteUser.name
            user.userName = favoriteUser.userName
            user.avatar = favoriteUser.avatar
            user.adress = favoriteUser.adress
            user.company = favoriteUser.company
            user.followers = favoriteUser.followers
            user.following = favoriteUser.following
            user.repository = favoriteUser.repository
            putExtra(DetailActivity.EXTRA_PERSON, user)
        }
        startActivity(githubUserMoveData)
    }


    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.INVISIBLE
        }
    }
}