package com.exercicios.mynewcolors.model

import android.graphics.Color

class MyColor (val id: Int, var name: String, var red: Int, var green: Int, var blue: Int): java.io.Serializable {

    fun getHexCode(): String {
        return String.format("#%02x%02x%02x", red, green, blue)
    }

    fun getColor(): Int {
        return Color.rgb(red, green, blue)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MyColor

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }

}