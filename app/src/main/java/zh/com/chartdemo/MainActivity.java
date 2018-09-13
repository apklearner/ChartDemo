package zh.com.chartdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private CubicLineChart chartView;
    private Button resetBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRes();
    }

    private void initRes() {
        chartView = findViewById(R.id.chartview);
        resetBtn = findViewById(R.id.btn);

        resetBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn:
                List<DataBean> datas = DataBean.randomValues();
                chartView.workWith(datas);

                break;
        }
    }
}
