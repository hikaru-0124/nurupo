package com.example.a0430_01;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.*;

public class MainActivity extends AppCompatActivity {

    private List<String> choreList = Arrays.asList(
            "洗濯", "晩御飯を作る", "食器を洗う", "掃除機をかける", "トイレ掃除",
            "風呂掃除", "ゴミ出し", "買い物", "布団を干す", "アイロンがけ",
            "窓拭き", "ペットの世話", "家計簿管理", "書類整理", "植物の水やり"
    );
    private List<String> remainingChores;
    private List<String> selectedChores = new ArrayList<>();
    private int currentStep = 0;

    private TextView questionText;
    private Button choice1, choice2, choice3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionText = findViewById(R.id.question_text);
        choice1 = findViewById(R.id.choice1);
        choice2 = findViewById(R.id.choice2);
        choice3 = findViewById(R.id.choice3);

        remainingChores = new ArrayList<>(choreList);
        nextQuestion();
    }

    private void nextQuestion() {
        if (currentStep >= 5) {
            showResult();
            return;
        }

        currentStep++;
        questionText.setText("家事を選んでください（" + currentStep + " / 5）");

        // ランダムで3つ選択
        Collections.shuffle(remainingChores);
        final List<String> options = remainingChores.subList(0, 3);

        // ボタンに表示
        choice1.setText(options.get(0));
        choice2.setText(options.get(1));
        choice3.setText(options.get(2));

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button clicked = (Button) v;
                String selected = clicked.getText().toString();
                selectedChores.add(selected);
                remainingChores.remove(selected);
                nextQuestion();
            }
        };

        choice1.setOnClickListener(listener);
        choice2.setOnClickListener(listener);
        choice3.setOnClickListener(listener);
    }

    private void showResult() {
        StringBuilder result = new StringBuilder("あなたが選んだ家事:\n");
        for (String chore : selectedChores) {
            result.append("・").append(chore).append("\n");
        }

        questionText.setText(result.toString());
        choice1.setVisibility(View.GONE);
        choice2.setVisibility(View.GONE);
        choice3.setVisibility(View.GONE);
    }
}
