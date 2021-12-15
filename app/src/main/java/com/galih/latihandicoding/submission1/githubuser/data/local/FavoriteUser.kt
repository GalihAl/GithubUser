package com.galih.latihandicoding.submission1.githubuser.data.local

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.io.Serializable
@Entity(tableName = "favorite_user")
data class FavoriteUser(
    @PrimaryKey
    val id: Int = 0,
    var name: String? = "",
    var userName: String? = "",
    var followers: Int = 0,
    var following: Int = 0,
    var avatar: String? = "",
    var adress: String? = "",
    var company: String? = "",
    var repository: Int = 0
) : Serializable