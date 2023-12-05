package com.example.wym_002.db
import androidx.room.*
import androidx.room.Dao
import kotlinx.coroutines.flow.Flow


@Dao
interface Dao {
    @Insert
    fun insertItem(items: Items)

    @Insert(onConflict = OnConflictStrategy.REPLACE)    // добавляет если нет, изменяет если есть
    fun insertSpend(spends: Spends)

    @Query ("SELECT SUM(budget) FROM spends WHERE data = :selData")      // выводит бюджет по выбранной дате
    fun getBudgetData(selData: String): Int?

    @Query("SELECT * FROM items")
    fun getAllItems(): Flow<List<Items>>

    @Query("SELECT * FROM items WHERE data BETWEEN :dateFrom AND :dateTo") // выводит записи по выбранной дате
    fun getItemsByDate(dateFrom: String, dateTo: String): Flow<List<Items>>

    @Query("SELECT SUM(amount) FROM items WHERE data BETWEEN :dateFrom AND :dateTo") // выводит сумму трат по выбранной дате
    fun getSumByDate(dateFrom: String, dateTo: String): Int

    @Query("SELECT SUM(savings) FROM spends WHERE data = :selectedDate") // выводит сумму трат по выбранной дате
    fun getSavingByDate(selectedDate: String): Int

    @Query ("DELETE FROM spends")
    fun deleteFromSpends()

    @Query ("DELETE FROM items")
    fun deleteFromItems()

    @Transaction
    fun deleteAll(){                        // удаляет все данные из всех таблиц из бд
        deleteFromSpends()
        deleteFromItems()
    }

}