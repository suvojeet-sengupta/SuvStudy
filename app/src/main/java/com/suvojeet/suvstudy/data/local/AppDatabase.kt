package com.suvojeet.suvstudy.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.suvojeet.suvstudy.data.local.dao.FocusDao
import com.suvojeet.suvstudy.data.local.dao.GoalDao
import com.suvojeet.suvstudy.data.local.dao.StudyTaskDao
import com.suvojeet.suvstudy.data.local.dao.SubjectDao
import com.suvojeet.suvstudy.data.local.entity.FocusSessionEntity
import com.suvojeet.suvstudy.data.local.entity.GoalEntity
import com.suvojeet.suvstudy.data.local.entity.StudyTaskEntity
import com.suvojeet.suvstudy.data.local.entity.SubjectEntity

@Database(
    entities = [
        SubjectEntity::class,
        StudyTaskEntity::class,
        FocusSessionEntity::class,
        GoalEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract val subjectDao: SubjectDao
    abstract val studyTaskDao: StudyTaskDao
    abstract val focusDao: FocusDao
    abstract val goalDao: GoalDao
    
    companion object {
        const val DATABASE_NAME = "suvstudy_db"
    }
}
