package com.oztosia.capsharewardrobe.singletons

class ObservedPostsSetHolder {
    companion object Singleton {
        val postSet = mutableSetOf<String>()
    }
}