package com.neppplus.colosseum_20210903

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.neppplus.colosseum_20210903.datas.TopicData
import com.neppplus.colosseum_20210903.utils.ServerUtil
import org.json.JSONObject

class MainActivity : BaseActivity() {

    val mTopicList = ArrayList<TopicData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

    }

    override fun setValues() {

        getMainDataFromServer()

    }

//    서버에서, 메인화면에 보여줄 정보 받아오기

    fun getMainDataFromServer() {

        ServerUtil.getRequestMainData(mContext, object : ServerUtil.JsonResponseHandler {
            override fun onResponse(jsonObj: JSONObject) {

//                응답 - jsonObj 분석 (파싱) => 토론 주제들을 서버가 내려줌.

                val dataObj = jsonObj.getJSONObject("data")
                val topicsArr = dataObj.getJSONArray("topics")

//                서버가 내려주는 토론주제들 (JSONObject 목록) => TopicData로 변환해서 ArrayList에 추가. (반복문 활용)

//                topicArr에 4개 : 0 ~ 4직전 까지 반복.

                for (i  in   0 until  topicsArr.length()) {

//                    순서에 맞는 { }를 통째로 받아내기. => 토론 주제 하나하나에 대한 정보를 담게됨.
                    val topicObj = topicsArr.getJSONObject(i)

//                    TopicData를 만들어서 -> 멤버변수들에 -> topicObj에서 파싱한 데이터를 대입.
                    val tempTopicData = TopicData()
                    tempTopicData.id = topicObj.getInt("id")
                    tempTopicData.title = topicObj.getString("title")
                    tempTopicData.imageURL = topicObj.getString("img_url")

//                    mTopicList에 하나씩 추가.
                    mTopicList.add(tempTopicData)

                }

            }

        })

    }


}