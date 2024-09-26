package com.example.dspl.ui.theme.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Task(
    val userId: Int,
    val id: Int? = -1,
    val title: String?  = "",
    val completed: Boolean? = false
)

// Define the data structure for UserTasks (userId with sublist of tasks)
data class UserTasks(
    val userId: Int,
    val tasks: ArrayList<Task>
)
