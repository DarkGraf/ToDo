package com.tsv.todo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

public class ToDoListItemView extends TextView {

    private Paint marginPaint;
    private int paperColor;
    private int paperIsDoneColor;
    private float margin;

    private boolean isDone;

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

        paperColor = resources.getColor(R.color.notepad_paper);
        paperIsDoneColor = resources.getColor(R.color.notepad_paper_is_done);
        margin = resources.getDimension(R.dimen.notepad_margin);

        isDone = false;
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawColor(isDone ? paperIsDoneColor : paperColor);

        canvas.drawLine(margin, 0, margin, getMeasuredHeight(), marginPaint);

        canvas.save();
        canvas.translate(margin, 0);

        super.onDraw(canvas);
        canvas.restore();
    }

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }
}