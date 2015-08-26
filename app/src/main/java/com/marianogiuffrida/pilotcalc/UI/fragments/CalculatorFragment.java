package com.marianogiuffrida.pilotcalc.UI.fragments;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.marianogiuffrida.helpers.FragmentUtils;
import com.marianogiuffrida.helpers.StringUtils;
import com.marianogiuffrida.pilotcalc.R;
import com.marianogiuffrida.pilotcalc.UI.notification.IProvideResult;
import com.marianogiuffrida.pilotcalc.model.Calculator.ShuntingYardEvaluator;

import java.io.IOException;
import java.text.NumberFormat;


public class CalculatorFragment extends Fragment {

    public static final String INPUT_TEXT = "inputText";
    public static final String RESULT_TEXT = "resultText";
    public static final String CONSECUTIVE_OPS = "consecutiveOps";
    public static final String LAST_PRESSED = "lastPressed";
    private IProvideResult callBack;

    private enum ButtonType {
        NUMBER, ADD, SUB, MULTI, DIV
    }

    private View rootView;
    private TextView inputText;
    private TextView resultText;

    private ButtonType lastPressed = ButtonType.NUMBER;
    private int consecutiveOperatorCount;
    private ColorStateList defaultFontColor;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_calculator, container, false);
        inputText = (TextView) rootView.findViewById(R.id.CalculatorInputDisplay);
        inputText.setMovementMethod(ScrollingMovementMethod.getInstance());
        resultText = (TextView) rootView.findViewById(R.id.CalculatorResultDisplay);
        defaultFontColor = inputText.getTextColors();

        setupUiListeners();
        tryRehydrateSavedState(savedInstanceState);
        callBack = FragmentUtils.getParent(this, IProvideResult.class);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putCharSequence(INPUT_TEXT, inputText.getText());
        savedInstanceState.putCharSequence(RESULT_TEXT, resultText.getText());
        savedInstanceState.putInt(CONSECUTIVE_OPS, consecutiveOperatorCount);
        savedInstanceState.putSerializable(LAST_PRESSED, lastPressed);
    }

    private void setupUiListeners() {
        Button button0 = (Button) rootView.findViewById(R.id.button0);
        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleNumberClick((Button) v);
            }
        });

        Button button1 = (Button) rootView.findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleNumberClick((Button) v);
            }
        });

        Button button2 = (Button) rootView.findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleNumberClick((Button) v);
            }
        });

        Button button3 = (Button) rootView.findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleNumberClick((Button) v);
            }
        });

        Button button4 = (Button) rootView.findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleNumberClick((Button) v);
            }
        });

        Button button5 = (Button) rootView.findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleNumberClick((Button) v);
            }
        });

        Button button6 = (Button) rootView.findViewById(R.id.button6);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleNumberClick((Button) v);
            }
        });

        Button button7 = (Button) rootView.findViewById(R.id.button7);
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleNumberClick((Button) v);
            }
        });

        Button button8 = (Button) rootView.findViewById(R.id.button8);
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleNumberClick((Button) v);
            }
        });

        Button button9 = (Button) rootView.findViewById(R.id.button9);
        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleNumberClick((Button) v);
            }
        });

        Button commaButton = (Button) rootView.findViewById(R.id.commaButton);
        commaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleNumberClick((Button) v);
            }
        });

        Button addButton = (Button) rootView.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOperationClick((Button) v);
            }
        });

        Button subButton = (Button) rootView.findViewById(R.id.subButton);
        subButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOperationClick((Button) v);
            }
        });

        Button multiButton = (Button) rootView.findViewById(R.id.multiButton);
        multiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOperationClick((Button) v);
            }
        });

        Button divButton = (Button) rootView.findViewById(R.id.divideButton);
        divButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOperationClick((Button) v);
            }
        });

        Button clearButton = (Button) rootView.findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteLastInputCharacter();
            }
        });

        clearButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                clearAll();
                return true;
            }
        });

        Button equButton = (Button) rootView.findViewById(R.id.equalButton);
        equButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleEqualButton();
            }
        });
    }

    private void tryRehydrateSavedState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            inputText.setText(savedInstanceState.getCharSequence(INPUT_TEXT));
            resultText.setText(savedInstanceState.getCharSequence(RESULT_TEXT));
            lastPressed = (ButtonType) savedInstanceState.getSerializable(LAST_PRESSED);
            consecutiveOperatorCount = savedInstanceState.getInt(CONSECUTIVE_OPS);
        }
    }

    private void handleNumberClick(Button button) {
        lastPressed = ButtonType.NUMBER;
        consecutiveOperatorCount = 0;
        inputText.setText(TextUtils.concat(inputText.getText(), button.getText()));
        updateResult();
    }

    private void handleOperationClick(Button button) {
        if (consecutiveOperatorCount == 2) return;
        String buttonText = button.getText().toString();
        if (buttonText.equals("-")) {
            switch (lastPressed) {
                case NUMBER:
                case MULTI:
                case DIV:
                    appendToInputText(buttonText);
                    break;
                default:
                    substituteOperation(inputText, buttonText);
                    break;
            }
        } else {
            switch (lastPressed) {
                case NUMBER:
                    appendToInputText(buttonText);
                    break;
                default:
                    substituteOperation(inputText, buttonText);
                    break;
            }
        }
        setLastPressed(buttonText);
        consecutiveOperatorCount++;
    }

    private void handleEqualButton() {
        if (updateResult()) {
            String result = resultText.getText().toString().replace(",", "");
            inputText.setText(result);
            resultText.setText("");
            inputText.startAnimation(AnimationUtils.loadAnimation(rootView.getContext(), R.anim.calculator_input_text_translatein));
            if (callBack != null) callBack.onNewResult(result);
        }
    }

    private boolean updateResult() {
        resultText.setTextColor(defaultFontColor);
        String resultString = "";
        Boolean success = true;
        try {
            String inputValue = inputText.getText().toString();
            if (!StringUtils.isNullOrEmpty(inputValue)) {
                NumberFormat format = NumberFormat.getInstance();
                format.setMaximumFractionDigits(2);
                resultString = format.format(calculate());
            }
        } catch (IllegalArgumentException e) {
            resultString = "Error";
            resultText.setTextColor(getResources().getColor(R.color.calcError));
            success = false;
        } catch (IOException e) {
            e.printStackTrace();
            success = false;
        }
        resultText.setText(resultString);
        return success;
    }

    private Double calculate() throws IOException, IllegalArgumentException {
        return ShuntingYardEvaluator.calculate(inputText.getText().toString());
    }

    private void deleteLastInputCharacter() {
        CharSequence input = inputText.getText();
        if (input.length() > 0) {
            inputText.setText(TextUtils.substring(input, 0, input.length() - 1));
            if (lastPressed != ButtonType.NUMBER) /*IS AN OPERATION*/ {
                consecutiveOperatorCount--;
            }
            updateResult();
        }
    }

    private void clearAll() {
        resultText.setText("");
        inputText.setText("");
        resultText.startAnimation(AnimationUtils.loadAnimation(rootView.getContext(), R.anim.abc_fade_out));
        inputText.startAnimation(AnimationUtils.loadAnimation(rootView.getContext(), R.anim.abc_fade_out));
        consecutiveOperatorCount = 0;
    }

    private void appendToInputText(String buttonText) {
        inputText.setText(inputText.getText() + buttonText);
    }

    private void substituteOperation(TextView tv, String s) {
        tv.setText(
                TextUtils.concat(
                        TextUtils.substring(tv.getText(), 0, tv.length() - 1),
                        s));
        consecutiveOperatorCount--;
    }

    private void setLastPressed(String buttonText) {
        switch (buttonText) {
            case "+":
                lastPressed = ButtonType.ADD;
                break;
            case "-":
                lastPressed = ButtonType.SUB;
                break;
            case "*":
                lastPressed = ButtonType.MULTI;
                break;
            default:
                lastPressed = ButtonType.DIV;
        }
    }
}
