package com.example.dropittest.bags

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*
import javax.inject.Inject

class BagsViewModel @Inject constructor() : ViewModel() {

    private val _selectedBagList by lazy { MutableLiveData<MutableList<String>>() }
    val selectedBagList: LiveData<MutableList<String>> = _selectedBagList
    private val selectedBags = mutableListOf<String>()
    private val _randomBagList by lazy { MutableLiveData<MutableList<String>>() }
    val randomBagList: LiveData<MutableList<String>> = _randomBagList
    private var randomBags = mutableListOf<String>()
    var deleteIndex: Int? = null
    var bagView: View? = null

    fun getRandomBagNames(): MutableList<String> {
        val list = mutableListOf<String>()
        for (i in 0 until 15) {
            val randomString =
                UUID.randomUUID().toString().substring(0, 6).toUpperCase(Locale.getDefault())
            list.add(randomString)
        }
        randomBags = list
        return list
    }

    fun addSelectedBag(bagName: String) {
        selectedBags.add(bagName)
        _selectedBagList.value = selectedBags
    }

    fun deleteSelectedBag() {
        deleteIndex?.let { index ->
            selectedBags.removeAt(index)
            _selectedBagList.value = selectedBags
        }
    }

    fun addRandomBag(bagName: String) {
        randomBags.add(bagName)
        _randomBagList.value = randomBags
    }

    fun deleteRandomBag(index: Int) {
        randomBags.removeAt(index)
        _randomBagList.value = randomBags
    }
}