package com.neppplus.colosseum_20210903.datas

import org.json.JSONObject
import java.io.Serializable

class SideData(
    var id: Int,
    var topicId: Int,
    var title: String,
    var voteCount: Int) : Serializable {

    constructor() : this(0, 0, "미정", 0)

    companion object {

//        json을 넣으면 -> SideData로 변환해주는 기능.

        fun getSideDataFromJson(json : JSONObject) : SideData {
            val sideData = SideData()

            sideData.id = json.getInt("id")
            sideData.topicId = json.getInt("topic_id")
            sideData.title = json.getString("title")
            sideData.voteCount = json.getInt("vote_count")

            return sideData
        }

    }

}