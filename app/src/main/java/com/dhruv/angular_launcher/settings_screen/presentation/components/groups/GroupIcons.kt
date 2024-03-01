package com.dhruv.angular_launcher.settings_screen.presentation.components.groups

import com.dhruv.angular_launcher.R

interface GroupIcons {
    companion object {
        val connection: Int
            get() = R.drawable.group_social
        val controller: Int
            get() = R.drawable.group_gaming
        val dumbbell: Int
            get() = R.drawable.group_dumbell
        val microsoft: Int
            get() = R.drawable.group_microsoft
        val stream: Int
            get() = R.drawable.group_stream
        val study: Int
            get() = R.drawable.group_study
        val travel: Int
            get() = R.drawable.group_travel
        val wallet: Int
            get() = R.drawable.group_wallet
        val work: Int
            get() = R.drawable.group_work
        val writing: Int
            get() = R.drawable.group_writing
        val music: Int
            get() = R.drawable.group_music

        val allGroupIcons = listOf(connection, controller, dumbbell, microsoft, stream, study, travel, wallet, work, writing, music)
    }
}