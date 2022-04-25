package com.lostark.lostarkapplication.ui.tools;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lostark.lostarkapplication.CustomToast;
import com.lostark.lostarkapplication.R;

public class BingoFragment extends Fragment {
    private ImageView[][] imgBingo = new ImageView[5][5];
    private TextView txtInfo, txtBomb;
    private Button btnRandom, btnUndo, btnPass, btnInfinity, btnReset;

    private CustomToast customToast;

    private int[][] bingo = new int[5][5];
    private int count = 0, start = 0;

    public BingoFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bingo, container, false);

        customToast = new CustomToast(getActivity());

        txtInfo = root.findViewById(R.id.txtInfo);
        txtBomb = root.findViewById(R.id.txtBomb);
        btnRandom = root.findViewById(R.id.btnRandom);
        btnUndo = root.findViewById(R.id.btnUndo);
        btnPass = root.findViewById(R.id.btnPass);
        btnInfinity = root.findViewById(R.id.btnInfinity);
        btnReset = root.findViewById(R.id.btnReset);
        for (int i = 0; i < imgBingo.length; i++) {
            for (int j = 0; j < imgBingo[i].length; j++) {
                bingo[i][j] = 0;
                imgBingo[i][j] = root.findViewById(getActivity().getResources().getIdentifier("imgBingo"+i+j, "id", getActivity().getPackageName()));
                final int first = i;
                final int second = j;
                imgBingo[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (start < 2) {
                            bingo[first][second] = 1;
                            changeBingo(imgBingo[first][second], 1, false);
                            start++;
                            if (start == 1) txtInfo.setText("두번째 지점을 선택하세요");
                            else txtInfo.setText("");
                            return;
                        }
                        if (first-1 >= 0) {
                            if (bingo[first-1][second] != 2) {
                                if (bingo[first-1][second] == 1) {
                                    bingo[first-1][second] = 0;
                                    changeBingo(imgBingo[first-1][second], 0, false);
                                } else {
                                    bingo[first-1][second] = 1;
                                    changeBingo(imgBingo[first-1][second], 1, false);
                                }
                            }
                        }
                        if (first+1 < 5) {
                            if (bingo[first+1][second] != 2) {
                                if (bingo[first+1][second] == 1) {
                                    bingo[first+1][second] = 0;
                                    changeBingo(imgBingo[first+1][second], 0, false);
                                } else {
                                    bingo[first+1][second] = 1;
                                    changeBingo(imgBingo[first+1][second], 1, false);
                                }
                            }
                        }
                        if (second-1 >= 0) {
                            if (bingo[first][second-1] != 2) {
                                if (bingo[first][second-1] == 1) {
                                    bingo[first][second-1] = 0;
                                    changeBingo(imgBingo[first][second-1], 0, false);
                                } else {
                                    bingo[first][second-1] = 1;
                                    changeBingo(imgBingo[first][second-1], 1, false);
                                }
                            }
                        }
                        if (second+1 < 5) {
                            if (bingo[first][second+1] != 2) {
                                if (bingo[first][second+1] == 1) {
                                    bingo[first][second+1] = 0;
                                    changeBingo(imgBingo[first][second+1], 0, false);
                                } else {
                                    bingo[first][second+1] = 1;
                                    changeBingo(imgBingo[first][second+1], 1, false);
                                }
                            }
                        }
                        if (bingo[first][second] != 2) {
                            if (bingo[first][second] == 1) {
                                bingo[first][second] = 0;
                                changeBingo(imgBingo[first][second], 0, false);
                            } else {
                                bingo[first][second] = 1;
                                changeBingo(imgBingo[first][second], 1, false);
                            }
                        }
                        count++;
                        if (count%3 == 2) {
                            txtInfo.setText("다음 폭탄 때 빙고 완성 못할 시 이난나 사용 권장");
                        } else {
                            txtInfo.setText("");
                        }
                        txtBomb.setText(Integer.toString(count));
                        asyncBingo();
                    }
                });
            }
        }

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
                customToast.createToast("초기화되었습니다.", Toast.LENGTH_SHORT);
                customToast.show();
            }
        });

        return root;
    }

    public void reset() {
        for (int i = 0; i < bingo.length; i++) {
            for (int j = 0; j < bingo[i].length; j++) {
                bingo[i][j] = 0;
                changeBingo(imgBingo[i][j], 0, false);
            }
        }
        count = 0;
        start = 0;
        txtInfo.setText("첫번째 지점을 선택하세요");
        txtBomb.setText("0");
    }

    public void refresh() {
        for (int i = 0; i < bingo.length; i++) {
            for (int j = 0; j < bingo[i].length; j++) {
                bingo[i][j] = 0;
            }
        }
        count = 0;
        start = 0;
    }

    private void asyncBingo() {
        for (int i = 0; i < bingo.length; i++) {
            int cnt = 0;
            for (int j = 0; j < bingo[i].length; j++) {
                if (bingo[i][j] >= 1) {
                    cnt++;
                }
            }
            if (cnt == 5) {
                for (int j = 0; j < bingo[i].length; j++) {
                    bingo[i][j] = 2;
                    changeBingo(imgBingo[i][j], 2, true);
                }
            }
        }
        for (int i = 0; i < bingo.length; i++) {
            int cnt = 0;
            for (int j = 0; j < bingo[i].length; j++) {
                if (bingo[j][i] >= 1) {
                    cnt++;
                }
            }
            if (cnt == 5) {
                for (int j = 0; j < bingo[i].length; j++) {
                    bingo[j][i] = 2;
                    changeBingo(imgBingo[j][i], 2, true);
                }
            }
        }
        int one_line = 0, two_line = 0;
        for (int i = 0; i < bingo.length; i++) {
            if (bingo[i][i] >= 1) {
                one_line++;
            }
            if (bingo[i][4-i] >= 1) {
                two_line++;
            }
        }
        for (int i = 0; i < bingo.length; i++) {
            if (one_line == 5) {
                bingo[i][i] = 2;
                changeBingo(imgBingo[i][i], 2, true);
            }
            if (two_line == 5) {
                bingo[i][4-i] = 2;
                changeBingo(imgBingo[i][4-i], 2, true);
            }
        }
    }

    private void changeBingo(ImageView imgView, int statue, boolean isBingo) {
        if (isBingo) {
            imgView.setImageResource(getActivity().getResources().getIdentifier("bingo_two_pick_block", "drawable", getActivity().getPackageName()));
        } else {
            if (statue == 1) {
                imgView.setImageResource(getActivity().getResources().getIdentifier("bingo_one_pick_block", "drawable", getActivity().getPackageName()));
            } else {
                imgView.setImageResource(getActivity().getResources().getIdentifier("bingo_disable_block", "drawable", getActivity().getPackageName()));
            }
        }
    }
}
