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




    @Query("SELECT SUM(amount) FROM items WHERE data BETWEEN :dateFrom AND :dateTo") // выводит сумму трат по выбранной дате со всех карт
    fun getSumByDate(dateFrom: String, dateTo: String): Int

    @Query("SELECT SUM(amount) FROM items WHERE category = :catKey AND data BETWEEN :dateFrom AND :dateTo") // выводит сумму трат по выбранной категории
    fun getSumWithCategoryByDate(catKey: String, dateFrom: String, dateTo: String): Int

    @Query("SELECT SUM(savings) FROM spends WHERE data BETWEEN :dateFrom AND :dateTo") // выводит сумму сбережений по выбранной дате
    fun getSavingByDate(dateFrom: String, dateTo: String): Int




    @Query("SELECT SUM(amount) FROM items WHERE card = :selectedCard AND data BETWEEN :dateFrom AND :dateTo") // выводит сумму трат по выбранной дате с картой
    fun getSumByDateWithCards(dateFrom: String, dateTo: String, selectedCard: String): Int

    @Query("SELECT SUM(amount) FROM items") // выводит сумму трат за все время со всех карт
    fun getSumByAllDateWithAllCards(): Int

    @Query("SELECT SUM(amount) FROM items WHERE card = :selectedCard") // выводит сумму трат за все время с картой
    fun getSumByAllDateWithCards(selectedCard: String): Int



    @Query("SELECT * FROM items ORDER BY data DESC") // выводит все записи со всех карт
    fun getAllItemsWithAllCards(): Flow<List<Items>>

    @Query("SELECT * FROM items WHERE data BETWEEN :dateFrom AND :dateTo ORDER BY data DESC") // выводит записи по выбранной дате со всех карт
    fun getItemsByDateWithAllCards(dateFrom: String, dateTo: String): Flow<List<Items>>

    @Query("SELECT * FROM items WHERE card = :selectedCard AND data BETWEEN :dateFrom AND :dateTo ORDER BY data DESC") // выводит записи по выбранной дате с выбранной картой
    fun getItemsByDateWithSelectedCard(dateFrom: String, dateTo: String, selectedCard: String): Flow<List<Items>>

    @Query("SELECT * FROM items WHERE card = :selectedCard ORDER BY data DESC") // выводит записи за все время с выбранной картой
    fun getItemsByAllDateWithSelectedCard(selectedCard: String): Flow<List<Items>>


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