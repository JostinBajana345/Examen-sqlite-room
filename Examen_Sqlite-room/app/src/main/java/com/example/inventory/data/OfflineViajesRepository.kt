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

package com.example.inventory.data

import kotlinx.coroutines.flow.Flow

class OfflineViajesRepository(private val viajesDao: ViajesDao) : ViajesRepository {
    override fun getAllItemsStream(): Flow<List<Viaje>> = viajesDao.getAllItems()

    override fun getItemStream(id: Int): Flow<Viaje?> = viajesDao.getItem(id)

    override suspend fun insertItem(viaje: Viaje) = viajesDao.insert(viaje)

    override suspend fun deleteItem(viaje: Viaje) = viajesDao.delete(viaje)

    override suspend fun updateItem(viaje: Viaje) = viajesDao.update(viaje)
}
