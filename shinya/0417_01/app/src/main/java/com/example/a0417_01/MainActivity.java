package com.example.a0417_01;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.*;

public class MainActivity extends AppCompatActivity {

    class GachaItem {
        String name;
        int weight;
        int rarity;

        GachaItem(String name, int weight, int rarity) {
            this.name = name;
            this.weight = weight;
            this.rarity = rarity;
        }
    }

    private List<GachaItem> gachaPool;
    private List<String> historyList = new ArrayList<>();
    private Set<String> ownedCharacters = new HashSet<>();
    private TextView textResult, textEffect, textPoint, textSpecialPoint;
    private Button btnGacha, btnGacha10, btnHistory, btnExchange;
    private RelativeLayout rootLayout;
    private Random random = new Random();
    private Handler handler = new Handler();

    private int point = 10000;
    private int specialPoint = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textResult = findViewById(R.id.textResult);
        textEffect = findViewById(R.id.textEffect);
        textPoint = findViewById(R.id.textPoint);
        textSpecialPoint = findViewById(R.id.textSpecialPoint);
        btnGacha = findViewById(R.id.btnGacha);
        btnGacha10 = findViewById(R.id.btnGacha10);
        btnHistory = findViewById(R.id.btnHistory);
        btnExchange = findViewById(R.id.btnExchange);
        rootLayout = findViewById(R.id.rootLayout);

        initializeGachaPool();
        updatePointDisplay();
        updateSpecialPointDisplay();

        btnGacha.setOnClickListener(view -> {
            if (point >= 10) {
                point -= 10;
                updatePointDisplay();
                GachaItem result = drawGacha();
                textResult.setText("結果： " + result.name);
                historyList.add(result.name);
                boolean isDuplicate = !ownedCharacters.add(result.name);
                if (isDuplicate) {
                    specialPoint++;
                    showToast("被り！特殊Pt +1");
                    updateSpecialPointDisplay();
                }
                if (result.rarity >= 4) showSpecialEffect(result.rarity);
                else resetEffect();
            } else {
                showToast("ポイントが足りません！");
            }
        });

        btnGacha10.setOnClickListener(view -> {
            if (point >= 100) {
                point -= 100;
                updatePointDisplay();
                StringBuilder results = new StringBuilder("10連結果：\n");
                boolean rareHit = false;
                for (int i = 0; i < 10; i++) {
                    GachaItem result = drawGacha();
                    results.append(result.name).append("\n");
                    historyList.add(result.name);
                    boolean isDuplicate = !ownedCharacters.add(result.name);
                    if (isDuplicate) {
                        specialPoint++;
                        updateSpecialPointDisplay();
                    }
                    if (result.rarity >= 4) rareHit = true;
                }
                textResult.setText(results.toString());
                if (rareHit) showSpecialEffect(5);
                else resetEffect();
            } else {
                showToast("ポイントが足りません！");
            }
        });

        btnHistory.setOnClickListener(view -> {
            if (historyList.isEmpty()) {
                showToast("履歴がまだありません！");
            } else {
                StringBuilder sb = new StringBuilder("ガチャ履歴：\n");
                for (String item : historyList) {
                    sb.append(item).append("\n");
                }
                textResult.setText(sb.toString());
                resetEffect();
            }
        });

        btnExchange.setOnClickListener(v -> showExchangeDialog());
    }

    private void initializeGachaPool() {
        gachaPool = new ArrayList<>();
        gachaPool.add(new GachaItem("★5：フェニックス", 3, 5));
        gachaPool.add(new GachaItem("★5：神龍", 2, 5));
        gachaPool.add(new GachaItem("★4：魔法使いエルフ", 7, 4));
        gachaPool.add(new GachaItem("★4：忍者マスター", 8, 4));
        gachaPool.add(new GachaItem("★3：剣士アーサー", 15, 3));
        gachaPool.add(new GachaItem("★3：アーチャー", 15, 3));
        gachaPool.add(new GachaItem("★2：スライム", 25, 2));
        gachaPool.add(new GachaItem("★2：ゴブリン", 25, 2));
    }

    private GachaItem drawGacha() {
        int totalWeight = 0;
        for (GachaItem item : gachaPool) {
            totalWeight += item.weight;
        }
        int rand = random.nextInt(totalWeight);
        int cumulative = 0;
        for (GachaItem item : gachaPool) {
            cumulative += item.weight;
            if (rand < cumulative) return item;
        }
        return null;
    }

    private void showSpecialEffect(int rarity) {
        String effectText = (rarity == 5) ? "\uD83C\uDF08\uD83C\uDF1F超激レア降臨!!\uD83C\uDF1F\uD83C\uDF08" : "\u2728\u2728激レアゲット！\u2728\u2728";
        textEffect.setText(effectText);
        textEffect.setVisibility(View.VISIBLE);
        rootLayout.setBackgroundColor(Color.parseColor("#FFEB3B"));
        handler.postDelayed(this::resetEffect, 2500);
    }

    private void resetEffect() {
        textEffect.setVisibility(View.GONE);
        rootLayout.setBackgroundColor(Color.WHITE);
    }

    private void updatePointDisplay() {
        textPoint.setText("所持ポイント：" + point);
    }

    private void updateSpecialPointDisplay() {
        textSpecialPoint.setText("特殊Pt：" + specialPoint);
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void showExchangeDialog() {
        String[] items = {
                "\uD83C\uDF08 虹のかけら（5Pt）",
                "\uD83D\uDD2E 魔法石（10Pt）",
                "\uD83E\uDDEA 経験値ポーション（8Pt）",
                "\uD83D\uDDE1️ 英雄の剣（12Pt）",
                "\uD83C\uDFC6 勝利のメダル（15Pt）"
        };
        int[] costs = {5, 10, 8, 12, 15};
        new AlertDialog.Builder(this)
                .setTitle("交換するアイテムを選んでください")
                .setItems(items, (dialog, which) -> {
                    if (specialPoint >= costs[which]) {
                        specialPoint -= costs[which];
                        updateSpecialPointDisplay();
                        showToast("「" + items[which] + "」と交換しました！");
                    } else {
                        showToast("ポイントが足りません！");
                    }
                })
                .setNegativeButton("キャンセル", null)
                .show();
    }
}
