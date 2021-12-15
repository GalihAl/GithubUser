package com.galih.latihandicoding.submission1.githubuser.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.galih.latihandicoding.submission1.githubuser.R
import com.galih.latihandicoding.submission1.githubuser.adapter.ViewPageDetailAdaptor
import com.galih.latihandicoding.submission1.githubuser.data.local.FavoriteUser
import com.galih.latihandicoding.submission1.githubuser.data.model.GithubUser
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val person = intent.getParcelableExtra<GithubUser>(EXTRA_PERSON) as GithubUser
        Log.d("person value", "$person")
        setupTabLayout(person)
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
    private fun setupTabLayout(githubUser: GithubUser) {
        val sectionsPagerAdapter = ViewPageDetailAdaptor(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        sectionsPagerAdapter.setUser(githubUser)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
    }

    companion object {
        const val EXTRA_PERSON = "extra_person"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.string_detail,
            R.string.string_follower,
            R.string.string_following
        )
    }
}

