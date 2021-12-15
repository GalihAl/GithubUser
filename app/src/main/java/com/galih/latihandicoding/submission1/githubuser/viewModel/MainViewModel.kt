package com.galih.latihandicoding.submission1.githubuser.viewModel

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.galih.latihandicoding.submission1.githubuser.BuildConfig
import com.galih.latihandicoding.submission1.githubuser.R
import com.galih.latihandicoding.submission1.githubuser.data.local.FavoriteUser
import com.galih.latihandicoding.submission1.githubuser.data.local.FavoriteUserDao
import com.galih.latihandicoding.submission1.githubuser.data.local.UserDatabase
import com.galih.latihandicoding.submission1.githubuser.data.model.GithubUser
import com.galih.latihandicoding.submission1.githubuser.view.MainActivity
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class MainViewModel(application: Application): AndroidViewModel(application) {
    private val listUsersNonMutable = ArrayList<GithubUser>()
    private val listUserMutable = MutableLiveData<ArrayList<GithubUser>>()

    private var userDao: FavoriteUserDao?
    private var userDb: UserDatabase?
    init {
        userDb = UserDatabase.getDatabase(application)
        userDao = userDb?.favoriteUserDao()
    }
    fun getFavoriteUser(): LiveData<List<FavoriteUser>>? {
        return userDao?.getFavoriteUser()
    }
    fun getListUsers(): LiveData<ArrayList<GithubUser>> {
        return listUserMutable
    }

    fun getGitUser(context: Context){
        val client = AsyncHttpClient()
        client.addHeader("Autorization", BuildConfig.GITHUB_TOKEN)
        client.addHeader("User-Agent","request")
        val url = "https://api.github.com/users"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                Log.d(MainActivity.TAG, result)
                try {
                    val responseArray = JSONArray(result)
                    for (i in 0 until responseArray.length()) {
                        val item = responseArray.getJSONObject(i)
                        val username: String = item.getString("login")
                        getUserDetail(username, context)
                    }
                }catch (e: Exception) {
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


    fun getUserDetail(id: String, context: Context) {
        val client = AsyncHttpClient()
        client.addHeader("Authorization", BuildConfig.GITHUB_TOKEN)
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/$id"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                Log.d(MainActivity.TAG, result)
                try {
                    val jsonObject = JSONObject(result)
                    val id: Int = jsonObject.getInt("id")
                    val username: String? = jsonObject.getString("login")
                    val name : String? =jsonObject.getString("name")
                    val avatar: String? = jsonObject.getString("avatar_url")
                    val followers: Int = jsonObject.getInt("followers")
                    val following: Int = jsonObject.getInt("following")
                    var adress: String? = jsonObject.getString("location")
                    if (adress == "null") {
                        adress = context.getString(R.string.no_data)
                    }
                    var company: String? = jsonObject.getString("company")
                    if (company == "null") {
                        company = context.getString(R.string.no_data)
                    }
                    val repository: Int = jsonObject.getInt("public_repos")
                    val user = GithubUser()
                    user.name = name
                    user.userName = username
                    user.avatar = avatar
                    user.followers = followers
                    user.following = following
                    user.adress = adress
                    user.company = company
                    user.repository = repository
                    user.id = id
                    listUsersNonMutable.add(user)
                    listUserMutable.postValue(listUsersNonMutable)
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
    fun getDataGitSearch(query: String, context: Context) {
        val httpClient = AsyncHttpClient()
        httpClient.addHeader("Authorization", BuildConfig.GITHUB_TOKEN)
        httpClient.addHeader("User-Agent", "request")
        val urlClient = "https://api.github.com/search/users?q=$query"

        httpClient.get(urlClient, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {

                val result =  String(responseBody)
                Log.d(MainActivity.TAG, result)
                try {
                    listUsersNonMutable.clear()
                    val jsonArray = JSONObject(result)
                    val item = jsonArray.getJSONArray("items")
                    for (i in 0 until item.length()) {
                        val jsonObject = item.getJSONObject(i)
                        val username = jsonObject.getString("login")
                        getUserDetail(username, context)
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

}