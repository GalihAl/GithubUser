package com.galih.latihandicoding.submission1.githubuser.view

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.galih.latihandicoding.submission1.githubuser.data.model.GithubUser
import com.galih.latihandicoding.submission1.githubuser.adapter.GithubUserAdapter
import com.galih.latihandicoding.submission1.githubuser.viewModel.MainViewModel
import com.galih.latihandicoding.submission1.githubuser.R
import com.galih.latihandicoding.submission1.githubuser.SettingPreferences
import com.galih.latihandicoding.submission1.githubuser.ViewModelFactory
import com.galih.latihandicoding.submission1.githubuser.databinding.ActivityMainBinding
import com.galih.latihandicoding.submission1.githubuser.viewModel.ThemeViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_row_github_user.*
import kotlinx.android.synthetic.main.item_row_github_user.view.*
import kotlinx.android.synthetic.main.item_row_github_user_with_favorite_checklist.*
import kotlinx.android.synthetic.main.item_row_github_user_with_favorite_checklist.view.*


class MainActivity : AppCompatActivity() {
    private var userList: ArrayList<GithubUser> = arrayListOf()
    private lateinit var listAdapter: GithubUserAdapter
    private lateinit var mainViewModel: MainViewModel
    private lateinit var _binding: ActivityMainBinding
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    companion object {
        val TAG: String? = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        listAdapter = GithubUserAdapter(userList)
        runGetDataGit()
        configMainViewModel(listAdapter)

    }

    private fun runGetDataGit() {
        mainViewModel.getGitUser(applicationContext)
        showLoading(true)
    }

    private fun configMainViewModel(adapter: GithubUserAdapter) {
        mainViewModel.getListUsers().observe(this, { listUsers ->
            if (listUsers != null) {
                adapter.setData(listUsers)
                showLoading(false)
                showRecyclerList()
            }
        })
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isEmpty()) {
                    return true
                } else {
                    userList.clear()
                    mainViewModel.getDataGitSearch(query, applicationContext)
                    showLoading(true)
                }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        val btnSwitch = menu.findItem(R.id.theme_mode).actionView as Switch
        val pref = SettingPreferences.getInstance(dataStore)
        val themeViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            ThemeViewModel::class.java
        )
        themeViewModel.getThemeSettings().observe(this,
            { isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    btnSwitch.isChecked = true
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    btnSwitch.isChecked = false
                }
            })
        btnSwitch.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            themeViewModel.saveThemeSetting(isChecked)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.language) {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
        if (item.itemId == R.id.favorite) {
            val mIntent = Intent(this@MainActivity, FavoriteActivity::class.java).apply {
            }
            startActivity(mIntent)
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showRecyclerList() {
        _binding.rvGithubUser.layoutManager = LinearLayoutManager(this)
        _binding.rvGithubUser.adapter = listAdapter
        _binding.rvGithubUser.setHasFixedSize(true)
        listAdapter.setOnItemClickCallback(object : GithubUserAdapter.OnItemClickCallback {
            override fun onItemClicked(githubUser: GithubUser) {
                moveIntent(githubUser)
            }
        })
        listAdapter.notifyDataSetChanged()
    }

    private fun moveIntent(githubUser: GithubUser) {
        val githubUserMoveData = Intent(this@MainActivity, DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_PERSON, githubUser)
        }
        startActivity(githubUserMoveData)
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.getGitUser(applicationContext)
        showLoading(true)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.INVISIBLE
        }
    }
}