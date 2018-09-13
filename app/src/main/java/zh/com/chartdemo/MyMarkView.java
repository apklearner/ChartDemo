package zh.com.chartdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.widget.TextView;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

/**
 * Created by ly on 2018/9/13.
 */

public class MyMarkView extends MarkerView{

    private TextView tvContent;

    public MyMarkView(Context context, int layoutResource) {
        super(context, layoutResource);
        tvContent = findViewById(R.id.tvContent);
        initPaint();
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        String msg = "" + Utils.formatNumber(e.getY(), 0, true);
        if (msg.contains(".")) {
            String splits[] = msg.split("[.]");
            int length = splits[1].length();
            msg = (int) (Double.parseDouble(msg) * Math.pow(10, length)) + "";
        }

        tvContent.setText(msg);

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }

    private MPPointF mOffset2 = new MPPointF();

    @Override
    public MPPointF getOffsetForDrawingAtPoint(float posX, float posY) {

        MPPointF offset = getOffset();
        mOffset2.x = offset.x;
        mOffset2.y = offset.y;

        Chart chart = getChartView();

        width = getWidth();
        height = getHeight();


//        Log.e("1234", "angleHeight  " + angleHight);
//        Log.e("1234", "offValue  " + offValue);
//        Log.e("1234", "posX - width - offsetX - chartWidth  " + posX + "   " + width + "  " + mOffset2.x + "   " + chart.getWidth());
//        Log.e("1234", "posY - height - offsetY - chartHeight  " + posY + "   " + height + "  " + mOffset2.y + "   " + chart.getHeight());

        mXType = XType.CENTER;    //表格可能预留了markview的边距 实际可能只有三种
        mYType = YType.TOP;
        if (posX + mOffset2.x <= 0) {   //左
            mOffset2.x = 0;
            mXType = XType.LEFT;
        } else if (chart != null && posX + width + mOffset2.x >= chart.getWidth()) { //右
            mOffset2.x = -width;
            mXType = XType.RIGHT;
        }


        //TODO 有默认上边距 ,实际一直走的3
        if (posY + mOffset2.y - angleHight - offValue <= 0) {    //向下
            mOffset2.y = offValue + angleHight;
            mYType = YType.BOTTOM;
        } else {
            mOffset2.y = mOffset2.y - angleHight - offValue;
        }

//        Log.e("1234", "x - y " + mXType + "  " + mYType);
        return mOffset2;
    }


    @Override
    public void draw(Canvas canvas, float posX, float posY) {
//        super.draw(canvas, posX, posY);

        MPPointF offset = getOffsetForDrawingAtPoint(posX, posY);

        int saveId = canvas.save();
        // translate to the correct position and draw


        canvas.translate(posX + offset.x, posY + offset.y);
        redraw(canvas);
        draw(canvas);

        canvas.restoreToCount(saveId);

    }


    private Paint bgPaint;
    private Paint pathPaint;
    private int width;
    private int height;
    private Path path = new Path();
    private int angleWidth = 15;
    private int angleHight = 10;
    private XType mXType = XType.CENTER;
    private YType mYType = YType.TOP;
    private int txtColor;
    private int lineColor;
    private int offValue;

    public void configData(int txtColor, int lineColor, int offValue) {
        this.txtColor = txtColor;
        this.lineColor = lineColor;
        this.offValue = (int) (getContext().getResources().getDisplayMetrics().density * offValue);
        tvContent.setTextColor(txtColor);
        pathPaint.setColor(lineColor);
    }

    public void configData(int offValue) {
        this.offValue = (int) (getContext().getResources().getDisplayMetrics().density * offValue);
    }


    //TODO 默认解决3种
    private enum XType {
        LEFT, CENTER, RIGHT
    }

    private enum YType {
        TOP, BOTTOM
    }

    private void initPaint() {
        txtColor = Color.parseColor("#db4131");
        lineColor = Color.parseColor("#db4131");
        tvContent.setTextColor(txtColor);
        offValue = (int) (getContext().getResources().getDisplayMetrics().density * 6);

        bgPaint = new Paint();
        bgPaint.setColor(Color.WHITE);
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setAntiAlias(true);


        pathPaint = new Paint();
        pathPaint.setColor(lineColor);
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setStrokeJoin(Paint.Join.ROUND);
        pathPaint.setStrokeCap(Paint.Cap.ROUND);
        pathPaint.setStrokeWidth(6);
        pathPaint.setAntiAlias(true);
        pathPaint.setDither(true);
    }

    public void redraw(Canvas canvas) {
        path.rewind();

        path.addRoundRect(new RectF(0, 0, width, height), 5, 5, Path.Direction.CW);

        switch (mXType) {
            case CENTER:
                if (mYType == YType.TOP) {
                    path.moveTo(width / 2 + angleWidth / 2, height);
                    path.lineTo(width / 2, height + angleHight);
                    path.lineTo(width / 2 - angleWidth / 2, height);
                } else {
                    path.moveTo(width / 2 + angleWidth / 2, 0);
                    path.lineTo(width / 2, -angleHight);
                    path.lineTo(width / 2 - angleWidth / 2, 0);
                }

                break;
            case RIGHT:
                if (mYType == YType.TOP) {
                    path.moveTo(width, height / 2);
                    path.lineTo(width, height + angleHight);
                    path.lineTo(width - angleWidth / 2, height);
                } else {
                    int saveId = canvas.save();
                    canvas.rotate(180,width/2,height/2);
//                    path.moveTo(width, height / 2);
//                    path.lineTo(width, -angleHight);
//                    path.lineTo(width - angleWidth / 2, 3);

                    path.moveTo(angleWidth / 2, height);
                    path.lineTo(0, height + angleHight);
                    path.lineTo(0, height / 2);

                    canvas.drawPath(path, pathPaint);
                    canvas.drawPath(path, bgPaint);
                    canvas.restoreToCount(saveId);
                    return;
                }
                break;
            case LEFT:
                if (mYType == YType.TOP) {
                    path.moveTo(angleWidth / 2, height);
                    path.lineTo(0, height + angleHight);
                    path.lineTo(0, height / 2);
                } else {
                    int saveId = canvas.save();
                    canvas.rotate(180,width/2,height/2);

                    path.moveTo(width, height / 2);
                    path.lineTo(width, height + angleHight);
                    path.lineTo(width - angleWidth / 2, height);

                    canvas.drawPath(path, pathPaint);
                    canvas.drawPath(path, bgPaint);
                    canvas.restoreToCount(saveId);
                    return;
                }
                break;
        }

        canvas.drawPath(path, pathPaint);
        canvas.drawPath(path, bgPaint);

    }


}
