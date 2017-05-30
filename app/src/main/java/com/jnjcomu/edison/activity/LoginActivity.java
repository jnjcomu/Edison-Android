package com.jnjcomu.edison.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.jnjcomu.edison.R;
import com.jnjcomu.edison.factory.UserFactory;
import com.jnjcomu.edison.model.Ticket;
import com.jnjcomu.edison.model.User;
import com.jnjcomu.edison.storage.UserStorage;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@EActivity(R.layout.activity_login)
public class LoginActivity extends AppCompatActivity implements Callback<Ticket> {

    @ViewById(R.id.edt_id_field)
    protected EditText edtIdField;

    @ViewById(R.id.edt_pw_field)
    protected EditText edtPwField;

    protected ProgressDialog statusDialog;

    @AfterViews
    protected void initUI() {
        statusDialog = new ProgressDialog(this);
        statusDialog.setMessage("잠시만 기다려 주세요...");
    }

    @Click(R.id.btn_login)
    protected void doTryLogin() {
        String userId = edtIdField.getText().toString();
        String userPassword = edtPwField.getText().toString();

        sendResult(true);
    }

    @UiThread
    protected void sendResult(boolean finishLogin, String message) {
        statusDialog.dismiss();

        if (finishLogin) {
            startActivity(new Intent(this, MainActivity_.class));
            finish();
        }

        if (!"".equals(message)) {
            new AlertDialog.Builder(this)
                    .setMessage(message)
                    .show();
        }
    }

    protected void sendResult(boolean finishLogin) {
        sendResult(finishLogin, "");
    }

    @Override
    public void onResponse(@NonNull Call<Ticket> call, @NonNull Response<Ticket> response) {
        if (response.isSuccessful()) {
            Ticket ticket = response.body();
            User user = UserFactory.extractUser(ticket);

            UserStorage.getInstance(this)
                    .saveUser(user)
                    .saveUserTicket(ticket);

            sendResult(true);
        } else {
            sendResult(false, "회원 정보가 유효하지 않습니다.");
        }
    }

    @Override
    public void onFailure(@NonNull Call<Ticket> call, @NonNull Throwable t) {
        sendResult(false, "알 수 없는 이유로 로그인에 실패했습니다.");
    }
}
