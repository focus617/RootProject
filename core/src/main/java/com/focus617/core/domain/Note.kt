package com.focus617.core.domain

import com.focus617.core.platform.base.BaseEntity
import java.io.Serializable

data class Note(
    val id: Long,
    val content: String,
    val timestamp: String
) : BaseEntity(), Serializable {
    companion object {
        val EMPTY = Note(
            1L, "", "2000-1-1 00:00:00"
        )

        val ExampleNote1 = Note(
            id = 100L,
            content = "传说，在那古老的星空深处，伫立着一道血与火侵染的红色之门。(abchij123)",
            timestamp = "2022-3-22 12:15:00"
        )

        val ExampleNote2 = Note(
            id = 101L,
            content = "传奇与神话，黑暗与光明，无尽传说皆在这古老的门户中流淌。(xyzhij987)",
            timestamp = "2022-3-22 12:16:50"
        )

        val ExampleNote3 = Note(
            id = 103L,
            content = "俯瞰星门，热血照耀天地，黑暗终将离去！(uvwhij456)",
            timestamp = "2022-3-22 12:17:30"
        )

        val ExampleNote4 = Note(
            id = 104L,
            content = "本是盛唐王朝以禁法培育的绝秘实验体，试图创造出能够征战星海任何角落的深空战士。(xyzhij456)",
            timestamp = "2022-3-23 09:37:40"
        )

        val ExampleNote5 = Note(
            id = 105L,
            content = "自此在这星际开拓的大时代中，他征战八方，踏尽星河，终为人类开启全新时代。(xyzhij456)",
            timestamp = "2022-3-23 10:25:40"
        )
    }
}
