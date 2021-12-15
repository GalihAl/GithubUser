package com.galih.latihandicoding.submission1.githubuser.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.galih.latihandicoding.submission1.githubuser.R
import com.galih.latihandicoding.submission1.githubuser.adapter.UserFollowingAdaptor
import com.galih.latihandicoding.submission1.githubuser.data.model.GithubUser
import com.galih.latihandicoding.submission1.githubuser.data.model.UserFollowing
import com.galih.latihandicoding.submission1.githubuser.viewModel.FollowingViewModel
import kotlinx.android.synthetic.main.fragment_followers.progressbarFollowers
import kotlinx.android.synthetic.main.fragment_following.*

class FollowingFragment : Fragment() {
    private val listData: ArrayList<UserFollowing> = ArrayList()
    private lateinit var adapters: UserFollowingAdaptor
    private lateinit var followingViewModel: FollowingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        followingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()
        ).get(FollowingViewModel::class.java)

        val dataUser =  arguments?.getParcelable<GithubUser>(DetailActivity.EXTRA_PERSON) as GithubUser
        config()
        showLoading(true)
        activity?.let {
            followingViewModel.getDataGit(it.applicationContext, dataUser.userName.toString())
            if (dataUser.following == 0) {
                showLoading(false)
                recycler_view_following.visibility = View.GONE
                tv_no_following.visibility = View.VISIBLE
            }
            else {
                followingViewModel.getListFollowing().observe(requireActivity(), { listFollowing ->
                    if (listFollowing != null) {
                        adapters.setData(listFollowing)
                        showLoading(false)
                    }
                })
            }
        }
    }
    private fun config() {
        recycler_view_following.layoutManager = LinearLayoutManager(activity)
        adapters = UserFollowingAdaptor(listData)
        recycler_view_following.adapter = adapters
        recycler_view_following.setHasFixedSize(true)
    }

    private fun showLoading(state: Boolean) {
        if (state) progressbarFollowers.visibility = View.VISIBLE
        else progressbarFollowers.visibility = View.INVISIBLE
    }
}