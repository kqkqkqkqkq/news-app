package dev.k.database

import android.content.Context
import androidx.room.Database
import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteOpenHelper
import dev.k.database.dao.ArticleDao
import dev.k.database.models.ArticleDBO
import dev.k.database.utils.Convertors

@Database(entities = [ArticleDBO::class], version = 1)
@TypeConverters(Convertors::class)
abstract class NewsDatabase : RoomDatabase() {

    abstract fun articlesDao(): ArticleDao
}

fun NewsDatabase(applicationContext: Context): NewsDatabase
    = Room.databaseBuilder(
        checkNotNull(applicationContext.applicationContext),
        NewsDatabase::class.java,
        "news"
    ).build()