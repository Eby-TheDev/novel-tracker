package com.example.noveltracker.di

import android.content.Context
import androidx.room.Room
import com.example.noveltracker.data.local.AppDatabase
import com.example.noveltracker.data.local.dao.GoalDao
import com.example.noveltracker.data.local.dao.TaskPlanDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "novel_tracker_db"
        )
        // .addMigrations(MIGRATION_1_2) // Add migrations here when upgrading version
        .build()
    }

    @Provides
    fun provideGoalDao(database: AppDatabase): GoalDao {
        return database.goalDao()
    }

    @Provides
    fun provideTaskPlanDao(database: AppDatabase): TaskPlanDao {
        return database.taskPlanDao()
    }
}
