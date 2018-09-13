package zh.com.chartdemo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ly on 2018/9/13.
 */

public class DataBean {

    public String day;
    public int value;

//    public void resetData() {
//        days.clear();
//        values.clear();
//        int month = (int) (Math.random() * 12 + 1);
//        for (int i = 0; i < 7; i++) {
//            days.add(month + "月0" + i + "日");
//            values.add((int) (Math.random() *100));
//        }
//    }


    public static List<DataBean> randomValues() {
        List<DataBean> datas = new ArrayList<>();
        int month = (int) (Math.random() * 10 );
        for (int i = 0; i < 7; i++) {
            DataBean bean = new DataBean();
            bean.day = ("0"+month + "/0" + i + "");
            bean.value = ((int) (Math.random() * 1000));
            datas.add(bean);
        }
        return datas;
    }
}
