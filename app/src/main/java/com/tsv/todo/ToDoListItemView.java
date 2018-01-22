package com.tsv.todo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

public class ToDoListItemView extends TextView {

    private Paint marginPaint;
    private Paint linePaint;
    private int paperColor;
    private float margin;

    public ToDoListItemView(Context context, AttributeSet attributeSet, int ds) {
        super(context, attributeSet, ds);
        init();
    }

    public ToDoListItemView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public ToDoListItemView(Context context) {
        super(context);
        init();
    }

    private void init() {
        Resources resources = getResources();

        // Кисти.
        marginPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        marginPaint.setColor(resources.getColor(R.color.notepad_margin));
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(resources.getColor(R.color.notepad_lines));

        paperColor = resources.getColor(R.color.notepad_paper);
        margin = resources.getDimension(R.dimen.notepad_margin);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawColor(paperColor);

        //canvas.drawLine(0, 0, getMeasuredHeight(), 0, linePaint);
        //canvas.drawLine(0, getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight(), linePaint);

        canvas.drawLine(margin, 0, margin, getMeasuredHeight(), marginPaint);

        canvas.save();
        canvas.translate(margin, 0);

        super.onDraw(canvas);
        canvas.restore();
    }
}