package com.gamesofni.shart

import android.app.Application
import com.gamesofni.shart.data.ShartRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class ShartApplication : Application() {
    // No need to cancel this scope as it'll be torn down with the process
    val applicationScope = CoroutineScope(SupervisorJob())

    val database: ShartRoomDatabase by lazy { ShartRoomDatabase.getDatabase(this, applicationScope) }
}
