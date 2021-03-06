package com.neppplus.colosseum_20210903

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.neppplus.colosseum_20210903.adapters.ChildReplyAdapter
import com.neppplus.colosseum_20210903.datas.ReplyData
import com.neppplus.colosseum_20210903.utils.GlobalData
import com.neppplus.colosseum_20210903.utils.ServerUtil
import kotlinx.android.synthetic.main.activity_view_reply_detail.*
import org.json.JSONObject

class ViewReplyDetailActivity : BaseActivity() {

    lateinit var mReplyData : ReplyData

    val mChildReplyList = ArrayList<ReplyData>()

    lateinit var mChildReplyAdapter : ChildReplyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_reply_detail)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

//        답글 삭제 테스트. -> 리스트뷰의 이벤트 처리 (LongClick)

        childReplyListView.setOnItemLongClickListener { adapterView, view, position, l ->

//            vaildation => 내가 적은 답글이 아니라면, 함수 강제 종료.
//            길게 누른 답글의 작성자가 => 나 인가?
//            답글.작성자.id(Int)  ==  "로그인한사람".id(Int)

            Log.d("댓글상세-로그인한사람?", GlobalData.loginUser!!.nickname)

            val clickedReply = mChildReplyList[position]

            if (GlobalData.loginUser!!.id != clickedReply.writer.id) {
                Toast.makeText(mContext, "자신이 적은 답글만 삭제할 수 있습니다.", Toast.LENGTH_SHORT).show()
                return@setOnItemLongClickListener true
            }

//            경고창 -> 정말 해당 답글을 삭제하시겠습니까.?

            val alert = AlertDialog.Builder(mContext)
            alert.setMessage("정말 해당 답글을 삭제하시겠습니까?")
            alert.setPositiveButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->

//                해당 답글 삭제 -> API 요청 + 새로고침

                ServerUtil.deleteRequestReply(mContext, clickedReply.id, object : ServerUtil.JsonResponseHandler {
                    override fun onResponse(jsonObj: JSONObject) {

                        runOnUiThread {
                            Toast.makeText(mContext, "답글을 삭제 했습니다.", Toast.LENGTH_SHORT).show()
                        }

//                        답글 목록 새로 불러오기
                        getChildRepliesFromServer()

                    }

                })


            })
            alert.setNegativeButton("취소", null)
            alert.show()

            return@setOnItemLongClickListener true
        }


        okBtn.setOnClickListener {

            val inputContent = contentEdt.text.toString()

            if (inputContent.length < 5) {
                Toast.makeText(mContext, "5글자 이상 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            ServerUtil.postRequestChildReply(mContext, inputContent, mReplyData.id, object : ServerUtil.JsonResponseHandler {
                override fun onResponse(jsonObj: JSONObject) {

//                    답글 목록 다시 불러오기.
                    getChildRepliesFromServer()

                    runOnUiThread {
                        contentEdt.setText("")

//                        도전 코드 (구글링) : 키보드 숨김처리
                        val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)

                    }

                }

            })


        }

    }

    override fun setValues() {
        mReplyData = intent.getSerializableExtra("replyData") as ReplyData

        sideAndNicknameTxt.text = "(${mReplyData.selectedSide.title}) ${mReplyData.writer.nickname}"


        replyContentTxt.text = mReplyData.content

        getChildRepliesFromServer()

        mChildReplyAdapter = ChildReplyAdapter(mContext, R.layout.child_reply_list_item, mChildReplyList)
        childReplyListView.adapter = mChildReplyAdapter

    }

    fun getChildRepliesFromServer() {

        ServerUtil.getRequestReplyDetail(mContext, mReplyData.id, object : ServerUtil.JsonResponseHandler {
            override fun onResponse(jsonObj: JSONObject) {

                val dataObj = jsonObj.getJSONObject("data")
                val replyObj = dataObj.getJSONObject("reply")

                val repliesArr = replyObj.getJSONArray("replies")

//                똑같은 댓글이 여러번 쌓이는걸 방지.
                mChildReplyList.clear()

                for ( i  in  0 until repliesArr.length() ) {

                    mChildReplyList.add( ReplyData.getReplyDataFromJson( repliesArr.getJSONObject(i) ) )

                }


                runOnUiThread {
                    mChildReplyAdapter.notifyDataSetChanged()

//                    리스트뷰의 최 하단 (마지막 아이템)으로 이동.
                    childReplyListView.smoothScrollToPosition(mChildReplyList.lastIndex)


                }

            }

        })

    }

}