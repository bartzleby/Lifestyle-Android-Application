package com.lifestyle

import androidx.annotation.NonNull
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "user_table")
class UserData(
    fullName: String,
    city: String?,
    country: String?
) {

    // TODO: make proper primary key
    @field:ColumnInfo(name = "full_name")
    @field:PrimaryKey
    @NonNull
    var fullName: String = fullName

    @field:ColumnInfo(name = "location_city")
    var city: String? = city

    @field:ColumnInfo(name = "location_country")
    var country: String? = country

}
