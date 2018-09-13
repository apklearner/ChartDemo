package zh.com.chartdemo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ly on 2018/9/10.
 */

public class CubicLineChart extends LineChart {

    private ChartBuilder builder;

    public CubicLineChart(Context context) {
        this(context, null);
    }

    public CubicLineChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CubicLineChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initConfig();
    }


    protected void initConfig() {

        builder = new ChartBuilder();
        builder.lineColor(Color.parseColor("#db4131"))
                .circleColor(Color.parseColor("#db4131"))
                .gradientColor(Color.parseColor("#fffdd3bb"), Color.parseColor("#00fdd3bb"))
                .lineWidth(2)
                .circleRadius(4)
                .holeRadius(2)
                .mode(LineDataSet.Mode.HORIZONTAL_BEZIER);

        setDrawBorders(false);
        setDragEnabled(false);
        setScaleEnabled(false);
//        offsetLeftAndRight((int) (20 * getResources().getDisplayMetrics().density));
//        offsetTopAndBottom((int) (15 * getResources().getDisplayMetrics().density));
        getDescription().setEnabled(false);
        getLegend().setEnabled(false);
        getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        getXAxis().setDrawGridLines(false);
        getXAxis().setDrawAxisLine(false);
//        getXAxis().setLabelCount(days.size(), true);
//        getXAxis().setAxisMinimum(0);
        getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return datas.get((int) value).day;
            }
        });
        getAxisLeft().setAxisMinimum(0);
        getAxisLeft().setSpaceTop(1);
        getAxisLeft().setEnabled(false);
        getAxisRight().setEnabled(false);

        MyMarkView markerView = new MyMarkView(getContext(), R.layout.layout_view_marker);
        markerView.setChartView(this);
        markerView.configData(builder.lineWidth + builder.circleRadius);
        setMarker(markerView);
    }

    public void refresh() {

        getXAxis().setLabelCount(datas.size(), true);
        getXAxis().setAxisMinimum(0);

        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            entries.add(new Entry(i, this.datas.get(i).value));
        }

        LineDataSet dataSet = new LineDataSet(entries, "DATA");
        dataSet.setDrawHighlightIndicators(false);
        dataSet.setDrawValues(false);
        dataSet.setCircleColor(builder.circleColor);
        dataSet.setColor(builder.lineColor);
        dataSet.setLineWidth(builder.lineWidth);
        dataSet.setCircleRadius(builder.circleRadius);
        dataSet.setCircleHoleRadius(builder.holeRadiues);
        dataSet.setFillDrawable(builder.gradientDrawable);
        dataSet.setMode(builder.mode);
        dataSet.setDrawFilled(true);
        dataSet.setDrawCircleHole(true);
        LineData data = new LineData(dataSet);
        setData(data);
        notifyDataSetChanged();
        animateY(1500, Easing.EasingOption.Linear);
    }

//    private List<String> days = new ArrayList<>();
//    private List<Integer> values = new ArrayList<>();

    private List<DataBean> datas = new ArrayList<>();

    public void workWith(List<DataBean> datas) {
        this.datas = datas;
        refresh();
    }

    public ChartBuilder getBuilder() {
        return builder;
    }

    public static class ChartBuilder {
        public int lineColor;
        public int circleColor;
        public int lineWidth;
        public int circleRadius;
        public int holeRadiues;
        public GradientDrawable gradientDrawable;
        public LineDataSet.Mode mode;


        public ChartBuilder lineColor(int color) {
            this.lineColor = color;
            return this;
        }

        public ChartBuilder lineWidth(int width) {
            this.lineWidth = width;
            return this;
        }

        public ChartBuilder gradientColor(int startColor, int endColor) {
            this.gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{startColor, endColor});
            return this;
        }

        public ChartBuilder circleColor(int color) {
            this.circleColor = color;
            return this;
        }

        public ChartBuilder circleRadius(int radius) {
            this.circleRadius = radius;
            return this;
        }

        public ChartBuilder holeRadius(int radius) {
            this.holeRadiues = radius;
            return this;
        }

        public ChartBuilder mode(LineDataSet.Mode mode) {
            this.mode = mode;
            return this;
        }

    }


}
