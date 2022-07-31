package com.example.shoppingapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.shoppingapp.data.local.entity.ProductEntity

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)

    @Query("UPDATE product_table SET category = :category, description= :description, image= :image, price= :price, title= :title WHERE id =:id")
    fun updateProduct(id: Int, category: String, description: String, image: String, price: String, title: String)

    @Query("DELETE FROM product_table WHERE id IN(:productIds)")
    fun deleteProducts(productIds: List<Int>)

    @Query("SELECT * FROM product_table")
    fun getProducts(): List<ProductEntity>

    @Query("SELECT * FROM product_table WHERE id = :id")
    fun getProductById(id: Int): ProductEntity
}
