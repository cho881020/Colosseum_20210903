package com.neppplus.colosseum_20210903

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.neppplus.colosseum_20210903.adapters.NotiAdapter
import com.neppplus.colosseum_20210903.datas.NotiData
import com.neppplus.colosseum_20210903.utils.ServerUtil
import kotlinx.android.synthetic.main.activity_notification_list.*
import org.json.JSONObject

class NotificationListActivity : BaseActivity() {

//    알림 목록을 담을 ArrayList
    val mNotiList = ArrayList<NotiData>()

    lateinit var mNotiAdapter : NotiAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_list)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

    }

    override fun setValues() {
        getNotiListFromServer()

        mNotiAdapter = NotiAdapter(mContext, R.layout.notification_list_item, mNotiList)
        notiListView.adapter = mNotiAdapter

    }

    fun getNotiListFromServer() {

        ServerUtil.getRequestNotificationCountOrList(mContext, true, object : ServerUtil.JsonResponseHandler {
            override fun onResponse(jsonObj: JSONObject) {

                val dataObj = jsonObj.getJSONObject("data")
                val notificationsArr = dataObj.getJSONArray("notifications")

                for ( i   in  0 until  notificationsArr.length()) {
                    val notiObj = notificationsArr.getJSONObject(i)

                    val notiData = NotiData.getNotiDataFromJson( notiObj )

                    mNotiList.add(notiData)

                }

                runOnUiThread {
//                    어댑터가 새로고침.
                    mNotiAdapter.notifyDataSetChanged()
                }

//                응용문제 답안.
//                 알림 목록을 불러오면 -> 맨 위의 알림까지는 내가 읽었따고 서버에 전파.
                ServerUtil.postRequestNotificationRead(mContext, mNotiList[0].id, null)

            }

        })

    }

}