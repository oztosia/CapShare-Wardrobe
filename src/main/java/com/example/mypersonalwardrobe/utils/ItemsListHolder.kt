package com.example.mypersonalwardrobe.utils

class ItemsListHolder {

    companion object ItemsListHolder {
        val list = mutableListOf<String>()

        fun addItems(vararg items: String) {
            list.addAll(items)
        }

        fun addItem(item: String) {
            list.add(item)
        }

        fun removeAll() {
            list.clear()
        }

        fun removeItem(item: String) = list.remove(item)

        fun getItems(): List<String> = list
    }
}