package com.galih.latihandicoding.submission1.githubuser.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.galih.latihandicoding.submission1.githubuser.R
import com.galih.latihandicoding.submission1.githubuser.adapter.UserFollowersAdapter
import com.galih.latihandicoding.submission1.githubuser.data.model.GithubUser
import com.galih.latihandicoding.submission1.githubuser.data.model.UserFollowers
import com.galih.latihandicoding.submission1.githubuser.viewModel.FollowersViewModel
import kotlinx.android.synthetic.main.fragment_followers.*

class FollowersFragment : Fragment() {


    private val listData: ArrayList<UserFollowers> = ArrayList()
    private lateinit var adapters: UserFollowersAdapter
    private lateinit var followersViewModel: FollowersViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_followers, container, false)
    }

    @SuppressLint("LongLogTag")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        followersViewModel = ViewModelProvider(
            this, ViewModelProvider.NewInstanceFactory()
        ).get(FollowersViewModel::class.java)
        config()
        showLoading(true)
        val dataUser =  arguments?.getParcelable<GithubUser>(DetailActivity.EXTRA_PERSON) as GithubUser
        activity?.let {
            followersViewModel.getDataGit(it.applicationContext, dataUser.userName.toString())
            Log.d("getDataGit", "${dataUser.name}")
            Log.d("getDataGit Followers","${dataUser.followers}")
            Log.d("getDataGit Followers","${dataUser.following}")
            if (dataUser.followers == 0) {
                showLoading(false)
                recycler_view_followers.visibility = View.GONE
                tv_no_followers.visibility = View.VISIBLE
            }
            else {
                followersViewModel.getListFollower().observe(requireActivity(), {
                    if (it != null) {
                        adapters.setData(it)
                        showLoading(false)
                        Log.d("getListFollower", "$it")
                    }
                })
            }
        }

    }

    private fun config() {
        recycler_view_followers.layoutManager = LinearLayoutManager(activity)
        adapters = UserFollowersAdapter(listData)
        recycler_view_followers.adapter = adapters
        recycler_view_followers.setHasFixedSize(true)
    }

    private fun showLoading(state: Boolean) {
        if (state) progressbarFollowers.visibility = View.VISIBLE
        else progressbarFollowers.visibility = View.GONE
    }
    companion object {
        val TAG = FollowersFragment::class.java.simpleName
    }
}