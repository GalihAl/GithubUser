package com.galih.latihandicoding.submission1.githubuser.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.galih.latihandicoding.submission1.githubuser.data.model.GithubUser
import com.galih.latihandicoding.submission1.githubuser.view.DetailFragment
import com.galih.latihandicoding.submission1.githubuser.view.FollowersFragment
import com.galih.latihandicoding.submission1.githubuser.view.FollowingFragment

class ViewPageDetailAdaptor(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    companion object {
        const val EXTRA_PERSON = "extra_person"
    }


    private lateinit var githubUser: GithubUser
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> {
                fragment = DetailFragment()
                val bundle = Bundle()
                bundle.putParcelable(EXTRA_PERSON, getUser())
                fragment.arguments = bundle
            }
            1 -> {
                fragment = FollowersFragment()
                val bundle = Bundle()
                bundle.putParcelable(EXTRA_PERSON, getUser())
                fragment.arguments = bundle
            }
            2 -> {
                fragment = FollowingFragment()
                val bundle = Bundle()
                bundle.putParcelable(EXTRA_PERSON, getUser())
                fragment.arguments = bundle
            }
        }
        return fragment as Fragment
    }

    fun setUser(githubUser: GithubUser) {
        this@ViewPageDetailAdaptor.githubUser = githubUser
    }

    private fun getUser(): GithubUser = githubUser
}