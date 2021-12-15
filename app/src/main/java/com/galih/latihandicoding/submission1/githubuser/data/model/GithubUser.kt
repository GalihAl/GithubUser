package com.galih.latihandicoding.submission1.githubuser.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GithubUser(
    var id: Int = 0,
    var name: String? = "",
    var userName: String? = "",
    var followers: Int = 0,
    var following: Int = 0,
    var avatar: String? = "",
    var adress: String? = "",
    var company: String? = "",
    var repository: Int = 0
) : Parcelable