package com.example.secretdiary

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.edit

class MainActivity : AppCompatActivity() {

    private val numberPicker1 : NumberPicker by lazy{
        findViewById<NumberPicker>(R.id.numberPicker1)
            .apply {
                minValue = 0
                maxValue = 9
            }
    }

    private val numberPicker2 : NumberPicker by lazy{
        findViewById<NumberPicker>(R.id.numberPicker2)
            .apply {
                minValue = 0
                maxValue = 9
            }
    }

    private val numberPicker3 : NumberPicker by lazy{
        findViewById<NumberPicker>(R.id.numberPicker3)
            .apply {
                minValue = 0
                maxValue = 9
            }
    }

    private val openButton : AppCompatButton by lazy{
        findViewById<AppCompatButton>(R.id.openButton)
    }

    private val changePasswordButton : AppCompatButton by lazy{
        findViewById<AppCompatButton>(R.id.changePasswordButton)
    }

    private var changePasswordMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numberPicker1
        numberPicker2
        numberPicker3

        openButton.setOnClickListener{

            if (changePasswordMode){
                Toast.makeText(this, "비밀번호 변경 중입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE) // preference파일을 다른 앱과 같이 사용할 수 있도록 공유하는 기능, MODE_PRIVATE은 공유하지 않게함.
            val passwordFromUser = "${numberPicker1.value}${numberPicker2.value}${numberPicker3.value}"

            if (passwordPreferences.getString("password", "000").equals(passwordFromUser)){
                startActivity(Intent(this, DiaryActivity::class.java))
            }else{
                showErrorAlertDialog()
            }
        }

        changePasswordButton.setOnClickListener{

            val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)
            // getSharedPreferences : preference파일을 다른 앱과 같이 사용할 수 있도록 공유하는 기능, MODE_PRIVATE은 공유하지 않게함.
            val passwordFromUser = "${numberPicker1.value}${numberPicker2.value}${numberPicker3.value}"
            // 유저에게 입력받은 비밀번호

            if(changePasswordMode){
                //번호를 저장하는 기능
                passwordPreferences.edit(true){

                    putString("password", passwordFromUser)
                }
                changePasswordMode = false
                changePasswordButton.setBackgroundColor(Color.BLACK)
                Toast.makeText(this,"비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show()
            }else{
                //changePasswordMode 가 활성화 :: 비밀번호가 맞는지 확인
                if (passwordPreferences.getString("password", "000").equals(passwordFromUser)){
                    // 패스워드 성공
                    changePasswordMode = true
                    Toast.makeText(this,"변경할 패스워드를 입력하세요.", Toast.LENGTH_SHORT).show()

                    changePasswordButton.setBackgroundColor(Color.RED)
                }else{
                    showErrorAlertDialog()
                }
            }
        }
    }
    private fun showErrorAlertDialog(){ // 비밀번호 실패 시 띄워지는 알림창
        AlertDialog.Builder(this)
            .setTitle("실패!!")
            .setMessage("비밀번호가 잘못되었습니다")
            .setPositiveButton("확인") { _,_ -> } // dialog, which 인자를 받지만, 지금은 확인버튼을 받고 다른 액션을 취할 필요가 없으므로 _로 생략
            .create()
            .show()
    }
}