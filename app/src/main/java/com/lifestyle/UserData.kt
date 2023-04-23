package com.lifestyle

import androidx.annotation.NonNull
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "user_table")
class UserData(
    fullName: String,
    age: Int,
    city: String?,
    country: String?,
    height: Int,
    weight: Int,
    sex: String,
    activity: String
) {

    // TODO: make proper primary key
    @field:PrimaryKey(autoGenerate = true)
    var uID: Int = 0

    @field:ColumnInfo(name = "full_name")
    @NonNull
    var fullName: String = fullName

    @field:ColumnInfo(name = "age")
    var age: Int? = age

    @field:ColumnInfo(name = "location_city")
    var city: String? = city

    @field:ColumnInfo(name = "location_country")
    var country: String? = country

    @field:ColumnInfo(name = "height")
    var height: Int? = height

    @field:ColumnInfo(name = "weight")
    var weight: Int? = weight

    @field:ColumnInfo(name = "sex")
    var sex: String? = sex

    @field:ColumnInfo(name = "activity_level")
    var activity: String? = activity

}
