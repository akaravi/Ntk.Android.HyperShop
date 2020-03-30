package ntk.android.academy.activity;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ntk.android.academy.R;
import ntk.android.academy.utill.AppUtill;
import ntk.android.academy.utill.FontManager;

public class ActLogin extends AppCompatActivity {

    @BindView(R.id.progressActLogin)
    ProgressBar Loading;

    @BindView(R.id.txtUsernameActLogin)
    EditText TxtUsername;

    @BindView(R.id.txtPasswordActLogin)
    EditText TxtPassword;

    @BindViews({R.id.lblVerificationActLogin,
            R.id.lblForgotPassActLogin})
    List<TextView> Lbls;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        Loading.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
        Loading.setVisibility(View.GONE);
        TxtUsername.setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));
        TxtPassword.setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));
        Lbls.get(0).setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));
        Lbls.get(1).setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));
//
    }

    @OnClick(R.id.btnActRegister)
    public void ClickBtn() {
        if (TxtUsername.getText().toString().isEmpty()) {
            Toast.makeText(this, "نام کاربری را وارد نمایید", Toast.LENGTH_SHORT).show();
        } else {
            if (TxtPassword.getText().toString().isEmpty()) {
                Toast.makeText(this, "گذرواژه را وارد نمایید", Toast.LENGTH_SHORT).show();
            } else {
                if (AppUtill.isNetworkAvailable(this)) {
                } else {
                    Toast.makeText(this, "عدم دسترسی به اینترنت", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @OnClick(R.id.RowForgotPasswordActLogin)
    public void ClickNoPhone() {
    }
}
