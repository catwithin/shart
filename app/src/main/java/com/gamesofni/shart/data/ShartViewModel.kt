package com.gamesofni.shart.data

import android.content.ClipData
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.first

class ShartViewModel (
        private val model3dDao: Model3dDao,
        private val shartObjectDao: ShartObjectDao,
    ) : ViewModel() {


    // Cache all items form the database using LiveData.
    public val all3dModels: List<Model3d> = model3dDao.getItems()
//    public val all3dModels: LiveData<List<Model3d>> = model3dDao.getItems().asLiveData()
//    val allShartObjects: LiveData<List<ShartObject>> = shartObjectDao.getItems().asLiveData()
    val allShartObjects: List<ShartObject> = shartObjectDao.getItems()

//    fun retrieveModel(id: Int): LiveData<Model3d> {
    fun retrieveModel(id: Int): Model3d {
        return model3dDao.getItem(id)
//        return model3dDao.getItem(id).asLiveData()
    }
    suspend fun getModelByAugImgId(augImgId: Int): Model3d {
        return model3dDao.getItem(shartObjectDao.getByAugImgId(augImgId).modelId)
    }
    suspend fun addShart(shart: ShartObject) {
        shartObjectDao.insert(shart)
    }
//    fun addNewShartObject(itemName: String, itemPrice: String, itemCount: String) {
//        val newShart = getNewShartEntry(itemName, itemPrice, itemCount)
//        insertItem(newItem)
//    }
//    private fun getNewShartEntry(itemName: String, itemPrice: String, itemCount: String): ClipData
//    .Item {
//        return ClipData.Item(
//            itemName = itemName,
//            itemPrice = itemPrice.toDouble(),
//            quantityInStock = itemCount.toInt()
//        )
//    }
}


/**
 * Factory class to instantiate the [ViewModel] instance.
 */
class ShartViewModelFactory(
        private val model3dDao: Model3dDao,
        private val shartObjectDao: ShartObjectDao,
    ) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShartViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ShartViewModel(model3dDao, shartObjectDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
