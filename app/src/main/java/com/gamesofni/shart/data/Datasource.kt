package com.gamesofni.shart.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gamesofni.shart.R
import kotlinx.coroutines.CoroutineScope
import androidx.sqlite.db.SupportSQLiteDatabase

import kotlinx.coroutines.launch


@Database(entities = [Model3d::class, ShartObject::class], version = 1, exportSchema = true)
abstract class ShartRoomDatabase : RoomDatabase() {
    abstract fun model3dDao(): Model3dDao
    abstract fun shartObjectDao(): ShartObjectDao

    companion object {
        @Volatile
        private var INSTANCE: ShartRoomDatabase? = null

        fun getDatabase(context: Context,
                        scope: CoroutineScope
        ): ShartRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ShartRoomDatabase::class.java,
                    "shart_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(ShartDatabaseCallback(scope))
                    .allowMainThreadQueries()
                    .build()

                INSTANCE = instance

                return instance
            }
        }
    }

    private class ShartDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
//        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.model3dDao(), database.shartObjectDao())
                }
            }
        }

        suspend fun populateDatabase(model3dDao: Model3dDao, shartObjectDao: ShartObjectDao) {
            // Delete all content here.
            model3dDao.deleteAll()

            // Add sample words.

            model3dDao.insertAll(
                Model3d(0,
                "Sonic",
                R.drawable.sonic_preview,
                "/assets/models/sonic/sonic.obj",
                ""),

                Model3d(1,
                    "Rafaj Mulberry Warlock", R.drawable.rafaj_preview,
                    "/assets/models/rafaj_the_mulberry_warlock/rafaj.obj",
                    ""
                ),

                Model3d(2,
                    "Tibetan fox",
                    R.drawable.tibetan_fox_preview,
                    "/assets/models/Tibetan_Hill_Fox/tibetan_fox2" +
                            ".obj",
                    ""
                ),

                Model3d(3,
                    "Low poly fox", R.drawable.low_fox_preview, "/assets/models/low_fox/low_fox.obj",
                    ""
                ),

                Model3d(4,
                    "Cyberpunk Apartment", R.drawable.apt_preview, "/assets/models/cyberpunk/apt.obj" +
                            ".obj",
                    ""
                ),

                Model3d(5,
                    "Lighthouse",
                    R.drawable.lighthouse_preview,
                    "/assets/models/lighthouse/lighthouse_v.obj",
                    ""
                ),

                Model3d(6,
                    "King sitting on the rock", R.drawable.king_sitting_preview,
                    "/assets/models/the-owl-house-king/king.obj",
                    ""
                ),

                Model3d(7,
                    "King standing", R.drawable.king_standing_preview, "/assets/models/king/king2" +
                            ".obj",
                    ""
                ),

                Model3d(8,
                    "Owl Albert", R.drawable.owl_albert_preview,
                    "/assets/models/owl_house_albert_hilt/owl_staff_sm.obj",
                    ""
                ),
            )

        }
    }

//    private val sRoomDatabaseCallback: Callback = object : Callback() {
//        override fun onOpen(db: SupportSQLiteDatabase) {
//            super.onOpen(db)
//            PopulateDbAsync(INSTANCE).execute()
//        }
//    }

}

//private class PopulateDbAsync internal constructor(db: ShartRoomDatabase?) :
//    AsyncTask<Void?, Void?, Void?>() {
//    private val mDao: ShartObjectDao
//    var words = arrayOf("dolphin", "crocodile", "cobra")
//    override fun doInBackground(vararg params: Void?): Void? {
//        // Start the app with a clean database every time.
//        // Not needed if you only populate the database
//        // when it is first created
////        mDao.deleteAll()
//        for (i in 0..words.size - 1) {
//            val word = ShartObject(0, 0, 0, 0, 0, false, null)
//            mDao.insert(word)
//        }
//        return null
//    }
//
//    init {
//        mDao = db.shartObjectDao()
//    }
//}

class Datasource {
    private val list3dModels = listOf(
        Model3d(0,
            "Sonic", R.drawable.sonic_preview, "/assets/models/sonic/sonic.obj",
            ""),

        Model3d(1,
            "Rafaj Mulberry Warlock", R.drawable.rafaj_preview,
            "/assets/models/rafaj_the_mulberry_warlock/rafaj.obj",
            ""
        ),

        Model3d(2,
            "Tibetan fox",
            R.drawable.tibetan_fox_preview,
            "/assets/models/Tibetan_Hill_Fox/tibetan_fox2" +
                    ".obj",
            ""
        ),

        Model3d(3,
            "Low poly fox", R.drawable.low_fox_preview, "/assets/models/low_fox/low_fox.obj",
        ""
            ),

        Model3d(4,
            "Cyberpunk Apartment", R.drawable.apt_preview, "/assets/models/cyberpunk/apt.obj" +
                    ".obj",
        ""
        ),

        Model3d(5,
            "Lighthouse",
            R.drawable.lighthouse_preview,
            "/assets/models/lighthouse/lighthouse_v.obj",
            ""
        ),

        Model3d(6,
            "King sitting on the rock", R.drawable.king_sitting_preview,
            "/assets/models/the-owl-house-king/king.obj",
            ""
        ),

        Model3d(7,
            "King standing", R.drawable.king_standing_preview, "/assets/models/king/king2" +
                ".obj",
        ""
            ),

        Model3d(8,
            "Owl Albert", R.drawable.owl_albert_preview,
            "/assets/models/owl_house_albert_hilt/owl_staff_sm.obj",
            ""
        ),

    )

    fun loadModels(): List<Model3d> {
        return list3dModels
    }

    fun getByName(name: String): Model3d {
        return list3dModels.first { model -> model.name == name }
    }

}

