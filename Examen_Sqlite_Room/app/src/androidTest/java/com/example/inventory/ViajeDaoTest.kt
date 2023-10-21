/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.inventory

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.inventory.data.InventoryDatabase
import com.example.inventory.data.Viaje
import com.example.inventory.data.ViajesDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class ViajeDaoTest {

    private lateinit var viajesDao: ViajesDao
    private lateinit var inventoryDatabase: InventoryDatabase
    private val viaje1 = Viaje(1, "Guatemala", "Pimocha", "Premium", 500)
    private val viaje2 = Viaje(2, "Colombia", "Australia", "Economica", 250)

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        inventoryDatabase = Room.inMemoryDatabaseBuilder(context, InventoryDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        viajesDao = inventoryDatabase.itemDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        inventoryDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsItemIntoDB() = runBlocking {
        addOneItemToDb()
        val allItems = viajesDao.getAllItems().first()
        assertEquals(allItems[0], viaje1)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllItems_returnsAllItemsFromDB() = runBlocking {
        addTwoItemsToDb()
        val allItems = viajesDao.getAllItems().first()
        assertEquals(allItems[0], viaje1)
        assertEquals(allItems[1], viaje2)
    }


    @Test
    @Throws(Exception::class)
    fun daoGetItem_returnsItemFromDB() = runBlocking {
        addOneItemToDb()
        val item = viajesDao.getItem(1)
        assertEquals(item.first(), viaje1)
    }

    @Test
    @Throws(Exception::class)
    fun daoDeleteItems_deletesAllItemsFromDB() = runBlocking {
        addTwoItemsToDb()
        viajesDao.delete(viaje1)
        viajesDao.delete(viaje2)
        val allItems = viajesDao.getAllItems().first()
        assertTrue(allItems.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun daoUpdateItems_updatesItemsInDB() = runBlocking {
        addTwoItemsToDb()
        viajesDao.update(Viaje(1, "Guatemala", "Pimocha", "Premium", 500))
        viajesDao.update(Viaje(2, "Colombia", "Australia", "Economica", 250))

        val allItems = viajesDao.getAllItems().first()
        assertEquals(allItems[0], Viaje(1, "Guatemala", "Pimocha", "Premium", 500))
        assertEquals(allItems[1], Viaje(2, "Colombia", "Australia", "Economica", 250))
    }

    private suspend fun addOneItemToDb() {
        viajesDao.insert(viaje1)
    }

    private suspend fun addTwoItemsToDb() {
        viajesDao.insert(viaje1)
        viajesDao.insert(viaje2)
    }
}
