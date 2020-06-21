package com.ubis.numberbaseballgame_200621

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.ubis.numberbaseballgame_200621.adapters.ChatAdapter
import com.ubis.numberbaseballgame_200621.datas.Chat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    val cpuNumList = ArrayList<Int>()

    val chatList = ArrayList<Chat>()
    lateinit var mChatAdapter : ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setValurs()
        setEvents()
    }

    override fun setEvents() {
        inputBtn.setOnClickListener {
            val inputnum = numberEdt.text.toString()
            //세자리 일때만

            if( inputnum.length != 3) {
                Toast.makeText(mContext, "세자리 숫자만 입력 가능합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            chatList.add(Chat("ME", inputnum))

            mChatAdapter.notifyDataSetChanged()

            numberEdt.setText("")

            //입력한 값을 답장한다
            checkUserInputStrikeAndBall(inputnum)
        }
    }

    fun checkUserInputStrikeAndBall(input : String) {
        val number = input.toInt()
        // 세자리 숫자를 세개의 배열로 분리
        val numArr = ArrayList<Int>()
        numArr.add(number/100) // 100의 자리
        numArr.add(number%100/10) // 10의 자리
        numArr.add(number%10) // 1의 자리
        // 찾은 스트라이크 볼 저장변수
        var strikCount = 0
        var ballCount = 0

        for( i in numArr.indices)   {
            for(j in cpuNumList.indices) {
                if(numArr[i] == cpuNumList[j]) { // 숫자가  컴퓨터 숫자와 같은가
                    if(i == j) { // 같다면
                        strikCount++
                    }
                    else { // 같은숫자 다른자리
                        ballCount++
                    }
                }
            }
        }

        //결과를 컴퓨터가 답장
        chatList.add(Chat("CPU", "${strikCount}S ${ballCount}B 입니다."))

        if(strikCount == 3) {
            chatList.add(Chat("CPU", "축하합니다!!"))

            Toast.makeText(mContext, "게임을 종료합니다.",Toast.LENGTH_SHORT).show()
            numberEdt.isEnabled
            inputBtn.isEnabled
        }




    }

    override fun setValurs() {

        makeQuestionNum()

        for (num in cpuNumList) {
            Log.d("문제 출제", num.toString())
        }

        chatList.add(Chat("CPU", "숫자 야구게임에 오신것을 환영합니다."))
        chatList.add(Chat("CPU", "세자리 숫자를 맞춰주세요."))
        chatList.add(Chat("CPU", "0은 포함되지 않으며, 중복된 숫자도 없습니다."))

        mChatAdapter = ChatAdapter(mContext, R.layout.activity_chat_list_item, chatList)
        chatListView.adapter = mChatAdapter


    }

    fun makeQuestionNum()   {

//        세자리 숫자를 만든다 => 한자리씩 배열에(cpuNumList) 저장. Ex 741 => 7,4,1

        for (i in 0..2) {
//            조건에 맞는 숫자가 나오면 배열에 대입.
//            조건에 안맞는 숫자가 나오면 다시 뽑자.
//            조건에 맞는 숫자가 뽑힐때 까지 계속 뽑자.

            while (true) {

//              1 <=  (Math.random()*9+1).toInt()  < 10
//                우리가 원하는 숫자 : 0 제외. 1~9 정수
                val randomNum = (Math.random()*9+1).toInt()

//                중복된 숫자면 안됨. => 문제 배열을 보고 같은 숫자가 있는지?
//                있다면 사용 불가 (중복)

//                일단 써도 된다고 했다가 => 검사결과 같은게 있다면 => 쓰면 안된다고.
                var duplCheckResult = true

                for (num in cpuNumList) {
                    if (num == randomNum) {
//                        문제에 같은 숫자가 있다! => 사용하면 안된다!
                        duplCheckResult = false
                    }
                }

//                1~9 사이의 랜덤숫자가 중복검사를 통과 했는지?
                if (duplCheckResult) {
//                    써도 되는 숫자니까 출제 숫자에 등록.
                    cpuNumList.add(randomNum)
//                    무한반복을 깨고 다음 숫자를 뽑으러 이동.
                    break
                }
            }

        }


    }
}
