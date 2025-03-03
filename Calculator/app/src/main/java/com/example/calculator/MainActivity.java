package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private TextView display;

    private Button btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9;
    private Button btnAdd, btnSubtract, btnMultiply, btnDivide;
    private Button btnEquals, btnDecimal, btnC, btnSqrt, btnPower, btnFactorial;

    private double firstNumber = 0;
    private double secondNumber = 0;
    private String operation = "";
    private boolean isOperationClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        display = findViewById(R.id.display);


        btn0 = findViewById(R.id.btn0);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);
        btn7 = findViewById(R.id.btn7);
        btn8 = findViewById(R.id.btn8);
        btn9 = findViewById(R.id.btn9);


        btnAdd = findViewById(R.id.btnAdd);
        btnSubtract = findViewById(R.id.btnSubtract);
        btnMultiply = findViewById(R.id.btnMultiply);
        btnDivide = findViewById(R.id.btnDivide);


        btnEquals = findViewById(R.id.btnEquals);
        btnDecimal = findViewById(R.id.btnDecimal);
        btnC = findViewById(R.id.btnC);
        btnSqrt = findViewById(R.id.btnSqrt);
        btnPower = findViewById(R.id.btnPower);
        btnFactorial = findViewById(R.id.btnFactorial);


        View.OnClickListener numberListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                if (isOperationClicked) {
                    display.setText(button.getText());
                    isOperationClicked = false;
                } else {
                    if (display.getText().toString().equals("0")) {
                        display.setText(button.getText());
                    } else {
                        display.setText(display.getText().toString() + button.getText());
                    }
                }
            }
        };

        btn0.setOnClickListener(numberListener);
        btn1.setOnClickListener(numberListener);
        btn2.setOnClickListener(numberListener);
        btn3.setOnClickListener(numberListener);
        btn4.setOnClickListener(numberListener);
        btn5.setOnClickListener(numberListener);
        btn6.setOnClickListener(numberListener);
        btn7.setOnClickListener(numberListener);
        btn8.setOnClickListener(numberListener);
        btn9.setOnClickListener(numberListener);


        btnDecimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!display.getText().toString().contains(".")) {
                    display.setText(display.getText().toString() + ".");
                }
            }
        });


        btnC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display.setText("0");
                firstNumber = 0;
                secondNumber = 0;
                operation = "";
                isOperationClicked = false;
            }
        });


        View.OnClickListener operationListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                firstNumber = Double.parseDouble(display.getText().toString());
                operation = button.getText().toString();
                isOperationClicked = true;
            }
        };

        btnAdd.setOnClickListener(operationListener);
        btnSubtract.setOnClickListener(operationListener);
        btnMultiply.setOnClickListener(operationListener);
        btnDivide.setOnClickListener(operationListener);
        btnPower.setOnClickListener(operationListener);


        btnEquals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secondNumber = Double.parseDouble(display.getText().toString());
                double result = 0;
                DecimalFormat df = new DecimalFormat("#.########");

                switch (operation) {
                    case "+":
                        result = firstNumber + secondNumber;
                        break;
                    case "-":
                        result = firstNumber - secondNumber;
                        break;
                    case "*":
                        result = firstNumber * secondNumber;
                        break;
                    case "/":
                        if (secondNumber != 0) {
                            result = firstNumber / secondNumber;
                        } else {
                            display.setText("Error");
                            return;
                        }
                        break;
                    case "x^y":
                        result = Math.pow(firstNumber, secondNumber);
                        break;
                    default:

                        result = secondNumber;
                        break;
                }


                if (result == (long) result) {
                    display.setText(String.valueOf((long) result));
                } else {
                    display.setText(df.format(result));
                }


                isOperationClicked = true;
            }
        });


        btnSqrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double number = Double.parseDouble(display.getText().toString());
                if (number >= 0) {
                    double result = Math.sqrt(number);
                    DecimalFormat df = new DecimalFormat("#.########");

                    if (result == (long) result) {
                        display.setText(String.valueOf((long) result));
                    } else {
                        display.setText(df.format(result));
                    }
                } else {
                    display.setText("Error");
                }
                isOperationClicked = true;
            }
        });


        btnFactorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String displayText = display.getText().toString();
                if (displayText.contains(".")) {
                    display.setText("Error");
                    return;
                }

                long number = Long.parseLong(displayText);
                if (number < 0) {
                    display.setText("Error");
                } else if (number <= 20) {
                    long result = factorial(number);
                    display.setText(String.valueOf(result));
                } else {
                    display.setText("Çok büyük");
                }
                isOperationClicked = true;
            }
        });
    }

    private long factorial(long n) {
        if (n == 0 || n == 1) {
            return 1;
        }
        long result = 1;
        for (long i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }
}