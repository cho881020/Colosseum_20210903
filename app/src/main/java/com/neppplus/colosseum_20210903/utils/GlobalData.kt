package com.neppplus.colosseum_20210903.utils

import com.neppplus.colosseum_20210903.datas.UserData

class GlobalData {

    companion object {

//        로그인한 사용자를 담아둘 변수.

//        앱이 처음 켜졌을때는 로그인한사용자가 없다.

        var loginUser : UserData? = null

    }

}