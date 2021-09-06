package com.neppplus.colosseum_20210903

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.neppplus.colosseum_20210903.adapters.TopicAdapter
import com.neppplus.colosseum_20210903.datas.TopicData
import com.neppplus.colosseum_20210903.utils.ServerUtil
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : BaseActivity() {

    val mTopicList = ArrayList<TopicData>()
    lateinit var mTopicAdapter: TopicAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        topicListView.setOnItemClickListener { adapterView, view, position, l ->

            val clickedTopic = mTopicList[position]

            val myIntent = Intent(mContext, ViewTopicDetailActivity::class.java)
            myIntent.putExtra("topic", clickedTopic)
            startActivity(myIntent)

        }

    }

    override fun setValues() {

        getMainDataFromServer()

        mTopicAdapter = TopicAdapter(mContext, R.layout.topic_list_item, mTopicList)
        topicListView.adapter = mTopicAdapter

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


//                    mTopicList에 하나씩 추가. => 어댑터의 목록 구성 변수에 변화.
                    mTopicList.add(tempTopicData)

                }


//                로그인한 사용자 닉네임 가져오기

                val userObj = dataObj.getJSONObject("user")
                val nickname = userObj.getString("nick_name")


//                목록의 변화 -> 리스트뷰가 인지. -> 새로고침 공지. -> 리스트뷰 변경 -> 백그라운드에서 UI 변경
                runOnUiThread {
                    mTopicAdapter.notifyDataSetChanged()

                    Toast.makeText(mContext, "${nickname}님 환영합니다.!", Toast.LENGTH_SHORT).show()

                }


            }

        })

    }


}