package com.mor.eye.repository.data

data class TabInfo(
        val tabList: List<TabList>,
        val defaultIdx: Long
)

data class TabList(
        val id: Long,
        val name: String,
        val apiUrl: String,
        val tabType: Long,
        val nameType: Long,
        val adTrack: Any? = null
)