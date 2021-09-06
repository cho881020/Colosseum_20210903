package com.neppplus.colosseum_20210903.datas

import java.io.Serializable

class TopicData(
    var id: Int,
    var title: String,
    var imageURL: String) : Serializable {

//    보조 생성자 추가.
    constructor() : this(0, "제목없음", "")

//    연습. id값만 받는 보조 생성자.
    constructor(id: Int) : this(id, "제목없음", "")

}