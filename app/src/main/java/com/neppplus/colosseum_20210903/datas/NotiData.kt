package com.neppplus.colosseum_20210903.datas

import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class NotiData(
    var id: Int,
    var title: String) {

//    생성자와 관계 없이 동작하는 멤버변수
//    val : Calendar의 내부 값만 변경. 변수 자체의 대입 X.
    val createdAt = Calendar.getInstance() // 현재 시간이 기본값.

    constructor() : this(0, "제목 없음")

    companion object {

        fun getNotiDataFromJson(json : JSONObject) : NotiData {

            val notiData = NotiData()

            notiData.id = json.getInt("id")
            notiData.title = json.getString("title")

//            1. 서버가 알려주는 시간을 String으로 단순히 받기부터.
            val createdAtString = json.getString("created_at")

//            2. 받아낸 String을 => Calendar의 time값으로 대입.  (SimpleDateFormat - parse 필요)

            val serverFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            notiData.createdAt.time =  serverFormat.parse(createdAtString)

            return notiData

        }

    }

}