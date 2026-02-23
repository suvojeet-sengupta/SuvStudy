package com.suvojeet.suvstudy.di

import androidx.room.Room
import com.suvojeet.suvstudy.data.local.AppDatabase
import com.suvojeet.suvstudy.data.repository.FocusSessionRepository
import com.suvojeet.suvstudy.data.repository.GoalRepository
import com.suvojeet.suvstudy.data.repository.StudyTaskRepository
import com.suvojeet.suvstudy.data.repository.SubjectRepository
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Singleton Managers
    single { com.suvojeet.suvstudy.domain.manager.TimerManager() }
    single { com.suvojeet.suvstudy.domain.manager.ReminderManager(androidContext()) }
    single { com.suvojeet.suvstudy.domain.manager.SettingsManager(androidContext()) }

    // Database
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        )
        .fallbackToDestructiveMigration()
        .build()
    }

    // DAOs
    single { get<AppDatabase>().subjectDao }
    single { get<AppDatabase>().studyTaskDao }
    single { get<AppDatabase>().focusDao }
    single { get<AppDatabase>().goalDao }

    // Repositories
    single { SubjectRepository(get()) }
    single { StudyTaskRepository(get()) }
    single { FocusSessionRepository(get()) }
    single { GoalRepository(get()) }

    // ViewModels
    viewModel { com.suvojeet.suvstudy.ui.viewmodel.HomeViewModel(get(), get(), get(), get(), get()) }
    viewModel { com.suvojeet.suvstudy.ui.viewmodel.SubjectsViewModel(get()) }
    viewModel { com.suvojeet.suvstudy.ui.viewmodel.InsightsViewModel(get(), get()) }
    viewModel { com.suvojeet.suvstudy.ui.viewmodel.GoalsViewModel(get()) }
    viewModel { com.suvojeet.suvstudy.ui.viewmodel.SettingsViewModel(get()) }
    viewModel { com.suvojeet.suvstudy.ui.viewmodel.CalendarViewModel(get()) }
}

