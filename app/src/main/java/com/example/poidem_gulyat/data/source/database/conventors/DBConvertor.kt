package com.example.poidem_gulyat.data.source.database.conventors

import androidx.room.TypeConverter

class DBConvertor {
    class PrimitiveTypeConvertor {
        @TypeConverter
        fun booleanToInt(param: Boolean): Int = if (param) 1 else 0

        @TypeConverter
        fun intToBoolean(param: Int): Boolean = param == 1
    }

    class ClassTypeConventor{
        @TypeConverter
        fun booleanToInt(param: Boolean): Int = if (param) 1 else 0

        @TypeConverter
        fun intToBoolean(param: Int): Boolean = param == 1
    }
}