package com.samplural.dailyplanner.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "todos")
data class Todo(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val title: String,
    val time: String,
    val date: String,
)

