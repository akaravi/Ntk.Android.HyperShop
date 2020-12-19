package ntk.android.hyper.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ntk.android.base.appclass.AboutUsClass;
import ntk.android.base.utill.prefrense.Preferences;
import ntk.android.hyper.BuildConfig;
import ntk.android.hyper.R;
import ntk.android.hyper.library.about.AboutPage;
import ntk.android.hyper.library.about.Element;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        AboutUsClass about = Preferences.with(this).appVariableInfo().aboutUs();
        if (about != null) {
            View aboutPage = new AboutPage(AboutUsActivity.this)
                    .isRTL(true)
                    .setImage(R.mipmap.ic_launcher)
                    .setDescription(about.AboutUsDescription)
                    .addGroup("آدرس : ")
                    .addItem(new Element().setTitle(about.AboutUsAddress))
                    .addGroup("تماس با ما")
                    .addPhone(about.AboutUsTel, "َشماره تلفن تماس")
                    .addPhone(about.AboutUsFax, "َشماره فکس")
                    .addEmail(about.AboutUsEmail, "ایمیل :")
//                    .addPhone(about., about.Item.TitleMobileNo)
//                    .addPhone(about.Item.OfficeNo, about.Item.TitleOfficeNo)
//                    .addPhone(about.Item.Tel1, about.Item.TitleTel1)
//                                    .addInstagram(about.Instagram, about.Item.TitleInstagram)
//                                    .addWebsite(about.Item.WebUrl, about.Item.TitleWebUrl)
//                                    .addTelegram(about.Item.Telegram, about.Item.TitleTelegram)
                    .addItem(new Element().setTitle(" " + BuildConfig.VERSION_NAME + " ver"))
                    .create();
            setContentView(aboutPage);
        }
    }
}
