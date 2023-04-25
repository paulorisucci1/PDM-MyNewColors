package com.exercicios.mynewcolors.model

class MyColorList() {

    private val colorList: MutableList<MyColor> = mutableListOf()

    fun save(newColor: MyColor) {
        if(colorAlreadyExist(newColor)) {
            val index = colorList.indexOf(newColor)
            this.colorList[index] = newColor
        } else {
            this.colorList.add(newColor)
        }
    }

    private fun colorAlreadyExist(newColor: MyColor): Boolean {
        return colorList.any { myColor: MyColor -> myColor == newColor }
    }

    fun getSize(): Int {
        return this.colorList.size
    }

    fun getColor(position: Int): MyColor {
        return this.colorList[position]
    }

    fun removeAt(index: Int) {
        this.colorList.removeAt(index)
    }

    fun getList(): MutableList<*> {
        return this.colorList
    }
}