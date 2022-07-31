package com.example.shoppingapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.shoppingapp.data.local.dao.ProductDao
import com.example.shoppingapp.data.local.entity.ProductEntity

@Database(
    entities = [ProductEntity::class],
    version = 1
)
abstract class ProductDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao

    companion object {
        @Volatile
        private var instance: ProductDatabase? = null

        fun create(context: Context): ProductDatabase {
            if (instance == null) {
                synchronized(this) {
                    instance = buildDatabase(context)
                }
            }
            return instance as ProductDatabase
        }

        private fun buildDatabase(context: Context): ProductDatabase {
            return Room.databaseBuilder(
                context.applicationContext, ProductDatabase::class.java, "product_database"
            ).build()
        }
    }
}
