//
// class corresponding to user database table
//
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
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @field:ColumnInfo(name = "full_name")
    var fullName: String = fullName

    @field:ColumnInfo(name = "location_city")
    var city: String? = city

    @field:ColumnInfo(name = "location_country")
    var country: String? = country

}
