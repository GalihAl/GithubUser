package com.galih.latihandicoding.submission1.githubuser.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.galih.latihandicoding.submission1.githubuser.BuildConfig
import com.galih.latihandicoding.submission1.githubuser.R
import com.galih.latihandicoding.submission1.githubuser.data.model.UserFollowers
import com.galih.latihandicoding.submission1.githubuser.view.FollowersFragment
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class FollowersViewModel : ViewModel() {
    private val listFollowerNonMutable = ArrayList<UserFollowers>()
    private val listFollowerMutable = MutableLiveData<ArrayList<UserFollowers>>()

    fun getListFollower(): LiveData<ArrayList<UserFollowers>> {
        return listFollowerMutable
    }

    fun getDataGit(context: Context, id: String) {
        val httpClient = AsyncHttpClient()
        httpClient.addHeader("Authorization", BuildConfig.GITHUB_TOKEN)
        httpClient.addHeader("User-Agent", "request")
        val urlClient = "https://api.github.com/users/$id/followers"
        httpClient.get(urlClient, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                Log.d(FollowersFragment.TAG, result)
                try {
                    val jsonArray = JSONArray(result)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val usernameLogin: String = jsonObject.getString("login")
                        getDataGitDetail(usernameLogin, context)
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun getDataGitDetail(usernameLogin: String, context: Context) {
        val httpClient = AsyncHttpClient()
        httpClient.addHeader("Authorization", BuildConfig.GITHUB_TOKEN)
        httpClient.addHeader("User-Agent", "request")
        val urlClient = "https://api.github.com/users/$usernameLogin"
        httpClient.get(urlClient, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                Log.d(FollowersFragment.TAG, result)
                try {
                    val jsonObject = JSONObject(result)
                    val usersData = UserFollowers()
                    usersData.userName = jsonObject.getString("name")
                    if (usersData.userName == "null") {
                        usersData.userName = context.getString(R.string.no_data)
                    }
                    usersData.avatar = jsonObject.getString("avatar_url")
                    usersData.company = jsonObject.getString("company")
                    if (usersData.company == "null") {
                        usersData.company = context.getString(R.string.no_data)
                    }
                    usersData.adress = jsonObject.getString("location")
                    if (usersData.adress == "null") {
                        usersData.adress = context.getString(R.string.no_data)
                    }
                    usersData.followers = jsonObject.getInt("followers")
                    usersData.following = jsonObject.getInt("following")
                    listFollowerNonMutable.add(usersData)
                    listFollowerMutable.postValue(listFollowerNonMutable)
                } catch (e: Exception) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
}