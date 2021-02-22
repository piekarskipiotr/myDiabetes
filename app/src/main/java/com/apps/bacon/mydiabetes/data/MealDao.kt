package com.apps.bacon.mydiabetes.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MealDao {
    @Query("SELECT * FROM meals")
    fun getAll(): LiveData<List<Meal>>

    @Query("SELECT * FROM meals WHERE :id == meal_id")
    fun getMeal(id: Int): Meal

    @Insert
    fun insert(meal: Meal)

    @Update
    fun update(meal: Meal)

    @Delete
    fun delete(meal: Meal)

}