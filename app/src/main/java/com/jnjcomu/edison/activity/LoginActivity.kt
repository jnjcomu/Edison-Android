package com.jnjcomu.edison.activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.widget.EditText

import com.jnjcomu.edison.R
import com.jnjcomu.edison.factory.UserFactory
import com.jnjcomu.edison.model.Ticket
import com.jnjcomu.edison.storage.UserStorage
import kotlinx.android.synthetic.main.activity_login.*

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity(), Callback<Ticket> {

    protected var btnLogin: CardView = btn_login
    protected var edtIdField: EditText = edt_id_field
    protected var edtPwField: EditText = edt_pw_field

    protected var statusDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        statusDialog = ProgressDialog(this)
        statusDialog!!.setMessage("잠시만 기다려 주세요...")

        btnLogin.setOnClickListener{
            val userId = edtIdField.text.toString()
            val userPassword = edtPwField.text.toString()

            sendResult(true)
        }
    }

    protected fun sendResult(finishLogin: Boolean, message: String) {
        /**
         * TODO
         * 코틀린에서 UI Thread 사용법을 모르겠음
        */
        statusDialog!!.dismiss()

        if (finishLogin) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        if ("" != message) {
            AlertDialog.Builder(this)
                    .setMessage(message)
                    .show()
        }
    }

    protected fun sendResult(finishLogin: Boolean) {
        sendResult(finishLogin, "")
    }

    override fun onResponse(call: Call<Ticket>, response: Response<Ticket>) {
        if (response.isSuccessful) {
            val ticket = response.body()
            val user = UserFactory.extractUser(ticket!!)

            UserStorage.getInstance(this)
                    .saveUser(user)
                    .saveUserTicket(ticket)

            sendResult(true)
        } else {
            sendResult(false, "회원 정보가 유효하지 않습니다.")
        }
    }

    override fun onFailure(call: Call<Ticket>, t: Throwable) {
        sendResult(false, "알 수 없는 이유로 로그인에 실패했습니다.")
    }
}
