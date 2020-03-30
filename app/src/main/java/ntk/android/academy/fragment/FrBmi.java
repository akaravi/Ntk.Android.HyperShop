package ntk.android.academy.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ntk.android.academy.R;
import ntk.android.academy.utill.FontManager;

public class FrBmi extends Fragment {

    @BindView(R.id.LblResultFrBmi)
    TextView Lbl;

    @BindView(R.id.imgFrBmi)
    ImageView Img;

    @BindView(R.id.BtnBmiFrBmi)
    Button Btn;

    @BindViews({R.id.TxtLengthFrBmi, R.id.TxtWeightFrBmi})
    List<EditText> Txts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_bmi, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        Lbl.setTypeface(FontManager.GetTypeface(getContext() , FontManager.IranSansBold));
        Btn.setTypeface(FontManager.GetTypeface(getContext() , FontManager.IranSans));
        Txts.get(0).setTypeface(FontManager.GetTypeface(getContext() , FontManager.IranSans));
        Txts.get(1).setTypeface(FontManager.GetTypeface(getContext() , FontManager.IranSans));
    }

    @Override
    public void onResume() {
        super.onResume();
        Lbl.setText("");
        Txts.get(0).setText("");
        Txts.get(1).setText("");
        Img.setImageDrawable(null);
    }

    @SuppressLint("SetTextI18n")
    @OnClick(R.id.BtnBmiFrBmi)
    public void ClickBtn() {
        if(Txts.get(0).getText().toString().isEmpty()) {
            YoYo.with(Techniques.Tada).duration(700).playOn(Txts.get(0));
        }else if(Txts.get(1).getText().toString().isEmpty()) {
            YoYo.with(Techniques.Tada).duration(700).playOn(Txts.get(1));
        }else{
            double L = Double.parseDouble(Txts.get(0).getText().toString());
            double W = Double.parseDouble(Txts.get(1).getText().toString());
            double LM = ((L/100) * (L/100));

            double BMI = (W)/((LM));
            if (BMI<18.5) {
                Lbl.setText("کمبود وزن " + "حداقل وزن شما: " + ((int)(LM * 18.5)) + " حداکثر وزن شما: " + ((int)(LM * 24.9)));
                Lbl.setTextColor(Color.parseColor("#93b4d7"));
                Img.setImageResource(R.drawable.bmi_1);
            }else if (BMI>=18.5 && BMI<=24.9) {
                Lbl.setText( "وزن نرمال " + "حداقل وزن شما: " + ((int)(LM * 18.5)) + " حداکثر وزن شما: " + ((int)(LM * 24.9)));
                Lbl.setTextColor(Color.parseColor("#8fc69f"));
                Img.setImageResource(R.drawable.bmi_2);
            }else if (BMI>=25 && BMI<=29.9) {
                Lbl.setText("اضافه وزن " + "حداقل وزن شما: " + ((int)(LM * 18.5)) + " حداکثر وزن شما: " + ((int)(LM * 24.9)));
                Lbl.setTextColor(Color.parseColor("#f9d648"));
                Img.setImageResource(R.drawable.bmi_3);
            }else if (BMI>=30 && BMI<=34.9) {
                Lbl.setText("چاق " + "حداقل وزن شما: " + ((int)(LM * 18.5)) + " حداکثر وزن شما: " + ((int)(LM * 24.9)));
                Lbl.setTextColor(Color.parseColor("#e4985e"));
                Img.setImageResource(R.drawable.bmi_4);
            }else if (BMI>=35) {
                Lbl.setText("خیلی چاق " + "حداقل وزن شما: " + ((int)(LM * 18.5)) + " حداکثر وزن شما: " + ((int)(LM * 24.9)));
                Lbl.setTextColor(Color.parseColor("#d65c5b"));
                Img.setImageResource(R.drawable.bmi_5);
            }

        }
    }
}
