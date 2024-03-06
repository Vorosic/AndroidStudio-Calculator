package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class MainActivity extends AppCompatActivity {

    private int[] tombolNumeric = {R.id.nol, R.id.satu, R.id.dua, R.id.tiga, R.id.empat, R.id.lima, R.id.enam, R.id.tuju, R.id.delapan, R.id.sembilan};

    private int[] tombolOperator = {R.id.bagi, R.id.kali, R.id.tambah, R.id.kurang, R.id.titik};


    private TextView workings;

    private TextView hasil;

    private boolean angkaTerakhir;

    private boolean kaloError;

    private boolean setelahTitik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        this.workings = (TextView) findViewById(R.id.workings);
        this.hasil = (TextView) findViewById(R.id.hasil);
        setNumericPadaSaatDiKlik();
        setOperatorPadaSaatDiKlik();
    }

    private void setNumericPadaSaatDiKlik() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button tombol = (Button) v;
                if (kaloError) {
                    workings.setText(tombol.getText());
                    kaloError = false;
                } else {
                    // Check if current content is "0", replace it with the clicked numeric button
                    if (workings.getText().toString().equals("0")) {
                        workings.setText(tombol.getText());
                    } else {
                        workings.append(tombol.getText());
                    }
                }
                angkaTerakhir = true;
            }
        };

        for (int id : tombolNumeric) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    private void setOperatorPadaSaatDiKlik() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button tombol = (Button) v;
                if (angkaTerakhir && !kaloError) {
                    workings.append(tombol.getText());
                    angkaTerakhir = false;
                    setelahTitik = false;
                }
            }
        };
        for (int id : tombolOperator) {
            findViewById(id).setOnClickListener(listener);
        }

        // Fungsionalitas Tombol Bagi "/"
        findViewById(R.id.bagi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (angkaTerakhir && !kaloError) {
                    workings.append("/");
                    angkaTerakhir = false;
                    setelahTitik = false;
                }
            }
        });

        // Fungsionalitas Tombol Kali "*"
        findViewById(R.id.kali).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (angkaTerakhir && !kaloError) {
                    workings.append("*");
                    angkaTerakhir = false;
                    setelahTitik = false;
                }
            }
        });

        // Fungsionalitas Tombol Titik "."
        findViewById(R.id.titik).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (angkaTerakhir && !kaloError && !setelahTitik) {
                    workings.append(".");
                    angkaTerakhir = false;
                    setelahTitik = true;
                }
            }
        });

        // Fungsionalitas Tombol Clear "C"
        findViewById(R.id.clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workings.setText(" ");
                angkaTerakhir = false;
                kaloError = false;
                setelahTitik = false;
            }
        });

        // Fungsionalitas Tombol Samadengan "="
        findViewById(R.id.samadengan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEqual();
            }

            private void onEqual() {
                if (angkaTerakhir && !kaloError) {
                    String txt = workings.getText().toString();
                    Expression expression = new ExpressionBuilder(txt).build();
                    try {
                        double result = expression.evaluate();
                        // Mengatur format tampilan angka agar tidak berakhir dengan .0
                        if (result == (long) result) {
                            hasil.setText(String.format("%d", (long) result));
                        } else {
                            hasil.setText(String.format("%s", result));
                        }
                        setelahTitik = true;
                    } catch (ArithmeticException ex) {
                        hasil.setText("ERROR mas");
                        kaloError = true;
                        angkaTerakhir = false;
                    }
                }
            }

        });
    }
}