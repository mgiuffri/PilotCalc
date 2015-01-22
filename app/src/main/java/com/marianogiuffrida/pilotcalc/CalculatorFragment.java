package com.marianogiuffrida.pilotcalc;

import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.marianogiuffrida.pilotcalc.model.ShuntingYardEvaluator;

import java.io.IOException;
import java.text.NumberFormat;


public class CalculatorFragment extends Fragment {

    public static final String INPUT_TEXT = "inputText";
    public static final String RESULT_TEXT = "resultText";
    public static final String CONSECUTIVE_OPS = "consecutiveOps";
    public static final String LAST_PRESSED = "lastPressed";

    private enum ButtonType {
        NUMBER, ADD, SUB, MULTI, DIV
    }

    private View rootView;
    private TextView inputText;
    private TextView resultText;

    private ButtonType lastPressed = ButtonType.NUMBER;
    private int consequetiveOperatorCount;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_calculator, container, false);
        inputText = (TextView) rootView.findViewById(R.id.CalculatorInputDisplay);
        inputText.setMovementMethod(ScrollingMovementMethod.getInstance());
        resultText = (TextView) rootView.findViewById(R.id.CalculatorResultDisplay);
        setupUiListeners();

        if (savedInstanceState != null) {
            inputText.setText(savedInstanceState.getCharSequence(INPUT_TEXT));
            resultText.setText(savedInstanceState.getCharSequence(RESULT_TEXT));
            lastPressed = (ButtonType) savedInstanceState.getSerializable(LAST_PRESSED);
            consequetiveOperatorCount = savedInstanceState.getInt(CONSECUTIVE_OPS);
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putCharSequence(INPUT_TEXT, inputText.getText());
        savedInstanceState.putCharSequence(RESULT_TEXT, resultText.getText());
        savedInstanceState.putInt(CONSECUTIVE_OPS, consequetiveOperatorCount);
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

        Button clearButton = (Button) rootView.findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBackButton((Button) v);
            }
        });

        clearButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                handleClearClick((Button) v);
                return true;
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

        Button equButton = (Button) rootView.findViewById(R.id.equalButton);
        equButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleEqualButton();
            }
        });
    }

    private void handleEqualButton() {
        try {
            if (ShuntingYardEvaluator.IsWellFormedExpression(inputText.getText().toString())) {
                NumberFormat format = NumberFormat.getInstance();
                format.setMaximumFractionDigits(2);
                resultText.setText(format.format(Calculate()));
            } else {
                resultText.setText("Error");
                resultText.setTextColor(getResources().getColor(R.color.calcError));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Double Calculate() throws IOException {
        return ShuntingYardEvaluator.Evaluate(inputText.getText().toString());
    }

    private void handleBackButton(Button v) {
        CharSequence input = inputText.getText();
        if (input.length() > 0)
            inputText.setText(TextUtils.substring(input, 0, input.length() - 1));
    }

    private void handleNumberClick(Button button) {
        lastPressed = ButtonType.NUMBER;
        consequetiveOperatorCount =0;
        inputText.setText(TextUtils.concat(inputText.getText(), button.getText()));
        handleEqualButton();
    }

    private void handleClearClick(Button button) {
        inputText.setText("");
        resultText.setText("");
        consequetiveOperatorCount=0;
    }

    private void handleOperationClick(Button button) {
        if (consequetiveOperatorCount==2) return;
        String buttonText = button.getText().toString();
        if (buttonText.equals("-")) {
            switch (lastPressed) {
                case NUMBER:
                case MULTI:
                case DIV:
                    inputText.append(buttonText);
                    break;
                default:
                    substituteLastCharacter(inputText, buttonText);
                    break;
            }
        } else {
            switch (lastPressed) {
                case NUMBER:
                    inputText.append(buttonText);
                    break;
                default:
                    substituteLastCharacter(inputText, buttonText);
                    break;
            }
        }
        setLastPressed(buttonText);
        consequetiveOperatorCount++;
    }

    private void substituteLastCharacter(TextView tv, String s) {
        tv.setText(
                TextUtils.concat(
                        TextUtils.substring(tv.getText(), 0, tv.length() - 1),
                        s));
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
