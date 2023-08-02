package com.websarva.wings.android.jankensample2;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private int _cocktailId = -1;
    private String _cocktailName = "";
    //ヘルパーを保持するフィールド
    private DatabaseHelper _helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _helper = new DatabaseHelper(MainActivity.this);
    }

    public void onButtonClick(View view) {

        // ラジオグループのオブジェクトを取得
        RadioGroup rg = (RadioGroup) findViewById(R.id.rgJanken);
        // チェックされているラジオボタンの ID を取得
        int id = rg.getCheckedRadioButtonId();

        //ユーザのじゃんけんの手を格納
        int user = 0;

        if (id == R.id.rbGu) {
            user = 1;
        } else if (id == R.id.rbCyoki) {
            user = 2;
        } else if (id == R.id.rbPa) {
            user = 3;
        }

        //コンピュータのじゃんけんの手を格納
        int pc = (int) (Math.random() * 3) + 1;
        String pcTe = "";

        if (pc == 1) {
            pcTe = "グー";
        } else if (pc == 2) {
            pcTe = "チョキ";
        } else if (pc == 3) {
            pcTe = "パー";
        }

        //結果を格納
        String kekka;

        switch (user) {
            case 1:    //グー
                switch (pc) {
                    case 1:
                        kekka = "あいこ";
                        break;
                    case 2:
                        kekka = "勝ち";
                        break;
                    default:
                        kekka = "負け";
                        break;
                }
                break;
            case 2:    //チョキ
                switch (pc) {
                    case 1:
                        kekka = "負け";
                        break;
                    case 2:
                        kekka = "あいこ";
                        break;
                    default:
                        kekka = "勝ち";
                        break;
                }
                break;
            default:    //パー
                switch (pc) {
                    case 1:
                        kekka = "勝ち";
                        break;
                    case 2:
                        kekka = "負け";
                        break;
                    default:
                        kekka = "あいこ";
                        break;
                }
        }

        TextView tvComputer = findViewById(R.id.tvComputer);
        TextView tvSyouhai = findViewById(R.id.tvSyouhai);

        tvComputer.setText("コンピュータの手は「 " + pcTe + " 」です");
        tvSyouhai.setText("あなたの「" + kekka + "」です");

        inputSeiseki(kekka);

    }

    private void inputSeiseki(String kekka) {

        //DB接続オブジェクトの取得
        SQLiteDatabase db = _helper.getWritableDatabase();

        //ステートメントの準備
        String sqlInsert = "INSERT INTO jankenmemos(seiseki) VALUES(?)";
        SQLiteStatement stmt = db.compileStatement(sqlInsert);

        int syouhai;

        //バインド
        stmt.bindString(1, kekka);

        //ｓｑｌの実行
        stmt.executeInsert();

        //String sql = "SELECT COUNT(seiseki) as kachi FROM jankenmemos where seiseki = '勝ち'";
        String sql = "SELECT seiseki, COUNT(seiseki) as seisekiGroupCount FROM jankenmemos GROUP BY seiseki order by seiseki";
           //SQLの実行
        Cursor cursor = db.rawQuery(sql, null);

        String tuusanSeiseki = "";
        int syoubusu = 0;
        while (cursor.moveToNext()){
            int idSeiseki = cursor.getColumnIndex("seiseki");
            String seiseki = cursor.getString(idSeiseki);
            int idSeisekiGroupCount = cursor.getColumnIndex("seisekiGroupCount");
            int seisekiGroupCount = cursor.getInt(idSeisekiGroupCount);
            syoubusu += seisekiGroupCount;
            tuusanSeiseki += seiseki + seisekiGroupCount + "  ";
        }

        TextView tvSeiseki = findViewById(R.id.tvSeiseki);
        tvSeiseki.setText("通算成績: " + tuusanSeiseki + " :計" + syoubusu);




    }

    @Override
    protected void onDestroy() {
        _helper.close();
        super.onDestroy();
    }
}
