package com.marianogiuffrida.pilotcalc;

import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.marianogiuffrida.pilotcalc.model.ShuntingYardEvaluator;

import java.io.IOException;


public class CalculatorFragment extends Fragment {

    private View rootView;
    private TextView inputText;
    private TextView resultText;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_calculator, container, false);
        inputText = (TextView) rootView.findViewById(R.id.CalculatorInputDisplay);
        resultText = (TextView) rootView.findViewById(R.id.CalculatorResultDisplay);
        setupUiListeners();

        return rootView;
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

        Button clearButton = (Button) rootView.findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleClearClick((Button) v);
            }
        });

        Button backButton = (Button) rootView.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBackButton((Button) v);
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
                try {
                    resultText.setText(calc().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Double calc() throws IOException {
        return ShuntingYardEvaluator.Evaluate(inputText.getText().toString());
    }

    private enum ButtonType {
        number, ops
    }

    private ButtonType lastPressed;

    private void handleBackButton(Button v) {
        CharSequence input = inputText.getText();
        if (input.length() > 0)
            inputText.setText(TextUtils.substring(input, 0, input.length() - 1));
    }

    private void handleNumberClick(Button button) {
        lastPressed = ButtonType.number;
        inputText.setText(TextUtils.concat(inputText.getText(), button.getText()));
    }

    private void handleClearClick(Button button) {
        inputText.setText("");
        resultText.setText("");
    }

    private void handleOperationClick(Button button) {
        switch (lastPressed) {
            case number:
                inputText.setText(TextUtils.concat(inputText.getText(), button.getText()));
                break;
            case ops:
                inputText.setText(
                        TextUtils.concat(
                                TextUtils.substring(inputText.getText(), 0, inputText.getText().length() - 1),
                                button.getText()));
                break;
        }
        lastPressed = ButtonType.ops;
    }


}
