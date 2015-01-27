package com.marianogiuffrida.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;

/**
 * Created by Mariano on 27/01/2015.
 */

public class RadioButtonsTable extends TableLayout implements View.OnClickListener {

    public interface OnCheckedChangeListener{
        public void onCheckChanged(View view, int checkedId);
    }

    private static final String TAG = "RadioButtonsTable";
    private RadioButton activeRadioButton;
    private OnCheckedChangeListener onCheckedChangeListener;

    public RadioButtonsTable(Context context) {
        super(context);
    }

    public RadioButtonsTable(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        onCheckedChangeListener = listener;
    }

    public void setActiveRadioButton(RadioButton rb){
        if (rb != null){
            rb.setChecked(true);
            activeRadioButton = rb;
        }
    }

    @Override
    public void onClick(View v) {
        final RadioButton rb = (RadioButton) v;
        if ( activeRadioButton != null ) {
            activeRadioButton.setChecked(false);
        }
        rb.setChecked(true);
        activeRadioButton = rb;
        if (onCheckedChangeListener != null){
            onCheckedChangeListener.onCheckChanged(this, activeRadioButton.getId());
        }
    }

        @Override
    public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        setChildrenOnClickListener((TableRow)child);
    }

    @Override
    public void addView(View child, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, params);
        setChildrenOnClickListener((TableRow)child);
    }

    public int getCheckedRadioButtonId() {
        if ( activeRadioButton != null ) {
            return activeRadioButton.getId();
        }
        return -1;
    }

    private void setChildrenOnClickListener(TableRow tr) {
        final int c = tr.getChildCount();
        for (int i=0; i < c; i++) {
            final View v = tr.getChildAt(i);
            if ( v instanceof RadioButton ) {
                v.setOnClickListener(this);
            }
        }
    }
}
