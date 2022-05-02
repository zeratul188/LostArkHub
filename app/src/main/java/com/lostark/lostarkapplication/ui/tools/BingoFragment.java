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

import java.util.ArrayList;

public class BingoFragment extends Fragment {
    private ImageView[][] imgBingo = new ImageView[5][5];
    private TextView txtInfo, txtBomb, txtPass;
    private Button btnRandom, btnUndo, btnPass, btnInfinity, btnReset, btnHell;

    int[][] out_lines = {{0, 0}, {0, 1}, {0, 2}, {0, 3}, {0, 4}, {4, 0}, {4, 1}, {4, 2}, {4, 3}, {4, 4}, {1, 4}, {2, 4}, {3, 4}, {1, 0}, {2, 0}, {3, 0}};
    int[][] in_lines = {{1, 1}, {2, 1}, {3, 1}, {1, 3}, {2, 3}, {3, 3}, {2, 1}, {2, 3}};

    private CustomToast customToast;
    private ArrayList<int[][]> historys;
    private ArrayList<Integer> now_historys;

    private int[][] bingo = new int[5][5];
    private int count = 0, start = 0, now = 0;
    private boolean isEnd = false, isPass = false, isInfinity = false, isHell = false;

    public BingoFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bingo, container, false);

        customToast = new CustomToast(getActivity());
        historys = new ArrayList<>();
        now_historys = new ArrayList<>();

        txtInfo = root.findViewById(R.id.txtInfo);
        txtBomb = root.findViewById(R.id.txtBomb);
        btnRandom = root.findViewById(R.id.btnRandom);
        btnUndo = root.findViewById(R.id.btnUndo);
        btnPass = root.findViewById(R.id.btnPass);
        btnInfinity = root.findViewById(R.id.btnInfinity);
        btnReset = root.findViewById(R.id.btnReset);
        txtPass = root.findViewById(R.id.txtPass);
        btnHell = root.findViewById(R.id.btnHell);
        for (int i = 0; i < imgBingo.length; i++) {
            for (int j = 0; j < imgBingo[i].length; j++) {
                bingo[i][j] = 0;
                imgBingo[i][j] = root.findViewById(getActivity().getResources().getIdentifier("imgBingo"+i+j, "id", getActivity().getPackageName()));
                final int first = i;
                final int second = j;
                imgBingo[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isEnd) return;
                        if (start < 2) {
                            if (start == 0 && isHell) {
                                bingo[first][second] = 3;
                            } else {
                                bingo[first][second] = 1;
                            }
                            changeBingo(imgBingo[first][second], bingo[first][second], false);
                            start++;
                            if (start == 1) txtInfo.setText("두번째 지점을 선택하세요");
                            else txtInfo.setText("");
                            return;
                        }
                        int[][] clone_arr = new int[5][5];
                        for (int i = 0; i < clone_arr.length; i++) {
                            for (int j = 0; j < clone_arr[i].length; j++) {
                                clone_arr[i][j] = bingo[i][j];
                            }
                        }
                        historys.add(0, clone_arr);
                        now_historys.add(0, now);
                        if (first-1 >= 0) {
                            if (bingo[first-1][second] != 2) {
                                if (bingo[first-1][second] == 3) {
                                    onHell();
                                } else if (bingo[first-1][second] == 1) {
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
                                if (bingo[first+1][second] == 3) {
                                    onHell();
                                } else if (bingo[first+1][second] == 1) {
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
                                if (bingo[first][second-1] == 3) {
                                    onHell();
                                } else if (bingo[first][second-1] == 1) {
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
                                if (bingo[first][second+1] == 3) {
                                    onHell();
                                } else if (bingo[first][second+1] == 1) {
                                    bingo[first][second+1] = 0;
                                    changeBingo(imgBingo[first][second+1], 0, false);
                                } else {
                                    bingo[first][second+1] = 1;
                                    changeBingo(imgBingo[first][second+1], 1, false);
                                }
                            }
                        }
                        if (bingo[first][second] != 2) {
                            if (bingo[first][second] == 3) {
                                onHell();
                            } else if (bingo[first][second] == 1) {
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
                        } else if (!isEnd) {
                            txtInfo.setText("");
                        }
                        txtBomb.setText(Integer.toString(count));
                        if (asyncBingo()) {
                            customToast.createToast("빙고완성", Toast.LENGTH_SHORT);
                            customToast.show();
                        } else {
                            if (count%3 == 0 && !isInfinity && !isPass) {
                                txtInfo.setText("빙고를 완성하지 못하여 종료");
                                customToast.createToast("3번째 폭탄에 빙고를 완성하지 못하여 종료됩니다.", Toast.LENGTH_SHORT);
                                customToast.show();
                                isEnd = true;
                            }
                        }
                        if (isPass) {
                            isPass = false;
                        }
                        txtPass.setText(Integer.toString(now/5));
                    }
                });
            }
        }

        btnUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (historys.isEmpty()) {
                    customToast.createToast("이전 기록이 없습니다.", Toast.LENGTH_SHORT);
                    customToast.show();
                    return;
                }
                bingo = historys.get(0).clone();
                now = now_historys.get(0);
                historys.remove(0);
                now_historys.remove(0);
                for (int i = 0; i < bingo.length; i++) {
                    for (int j = 0; j < bingo[i].length; j++) {
                        changeBingo(imgBingo[i][j], bingo[i][j], false);
                    }
                }
                count--;
                txtBomb.setText(Integer.toString(count));
                isEnd = false;
                if (count%3 == 2) {
                    txtInfo.setText("다음 폭탄 때 빙고 완성 못할 시 이난나 사용 권장");
                } else {
                    txtInfo.setText("");
                }
                txtPass.setText(Integer.toString(now/5));
            }
        });

        btnRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (start >= 2) {
                    customToast.createToast("이미 첫 배치를 완료하였습니다.", Toast.LENGTH_SHORT);
                    customToast.show();
                } else if (start == 1) {
                    customToast.createToast("이미 첫번째 칸에 배치하였습니다. 랜덤 배치하기 위해서 초기화를 해주십시오.", Toast.LENGTH_SHORT);
                    customToast.show();
                } else {
                    int out_ran = (int)(Math.random()*123456)%out_lines.length;
                    int in_ran = (int)(Math.random()*123456)%in_lines.length;
                    int[] first = out_lines[out_ran];
                    int[] second = in_lines[in_ran];
                    if (isHell) {
                        int ransu = (int)(Math.random()*123456)%2;
                        if (ransu == 0) {
                            changeBingo(imgBingo[first[0]][first[1]], 3, false);
                            changeBingo(imgBingo[second[0]][second[1]], 1, false);
                            bingo[first[0]][first[1]] = 3;
                            bingo[second[0]][second[1]] = 1;
                        } else {
                            changeBingo(imgBingo[first[0]][first[1]], 1, false);
                            changeBingo(imgBingo[second[0]][second[1]], 3, false);
                            bingo[first[0]][first[1]] = 1;
                            bingo[second[0]][second[1]] = 3;
                        }
                    } else {
                        bingo[first[0]][first[1]] = 1;
                        bingo[second[0]][second[1]] = 1;
                        changeBingo(imgBingo[first[0]][first[1]], 1, false);
                        changeBingo(imgBingo[second[0]][second[1]], 1, false);
                    }
                    start = 2;
                    txtInfo.setText("");
                    customToast.createToast("2개를 자동으로 배치하였습니다.", Toast.LENGTH_SHORT);
                    customToast.show();
                }
            }
        });

        btnHell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isHell) {
                    isHell = false;
                    btnHell.setText("헬 모드 켜기");
                    reset();
                } else {
                    isHell = true;
                    btnHell.setText("헬 모드 끄기");
                }
            }
        });

        btnInfinity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInfinity) {
                    btnInfinity.setText("제한없이 진행 켜기");
                    isInfinity = false;
                } else {
                    btnInfinity.setText("제한없이 진행 끄기");
                    isInfinity = true;
                }
            }
        });

        btnPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPass) {
                    customToast.createToast("이미 니난나를 사용하였습니다.", Toast.LENGTH_SHORT);
                    customToast.show();
                    return;
                }
                isPass = true;
                txtInfo.setText("이난나를 사용하였습니다.");
            }
        });

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
        now = 0;
        txtInfo.setText("첫번째 지점을 선택하세요");
        txtBomb.setText("0");
        txtPass.setText("0");
        isEnd = false;
        isPass = false;
        historys.clear();
        now_historys.clear();
    }

    private boolean asyncBingo() {
        boolean isBingo = false;
        int bg = 0;
        for (int i = 0; i < bingo.length; i++) {
            int cnt = 0;
            for (int j = 0; j < bingo[i].length; j++) {
                if (bingo[i][j] == 1 || bingo[i][j] == 2) {
                    cnt++;
                }
            }
            if (cnt == 5) {
                for (int j = 0; j < bingo[i].length; j++) {
                    bingo[i][j] = 2;
                    changeBingo(imgBingo[i][j], 2, true);
                    bg++;
                }
            }
        }
        for (int i = 0; i < bingo.length; i++) {
            int cnt = 0;
            for (int j = 0; j < bingo[i].length; j++) {
                if (bingo[j][i] == 1 || bingo[j][i] == 2) {
                    cnt++;
                }
            }
            if (cnt == 5) {
                for (int j = 0; j < bingo[i].length; j++) {
                    bingo[j][i] = 2;
                    changeBingo(imgBingo[j][i], 2, true);
                    bg++;
                }
            }
        }
        int one_line = 0, two_line = 0;
        for (int i = 0; i < bingo.length; i++) {
            if (bingo[i][i] == 1 || bingo[i][i] == 2) {
                one_line++;
            }
            if (bingo[i][4-i] == 1 || bingo[i][4-i] == 2) {
                two_line++;
            }
        }
        for (int i = 0; i < bingo.length; i++) {
            if (one_line == 5) {
                bingo[i][i] = 2;
                changeBingo(imgBingo[i][i], 2, true);
                bg++;
            }
            if (two_line == 5) {
                bingo[i][4-i] = 2;
                changeBingo(imgBingo[i][4-i], 2, true);
                bg++;
            }
        }
        isBingo = bg > now;
        now = bg;
        return isBingo;
    }

    private void changeBingo(ImageView imgView, int statue, boolean isBingo) {
        if (isBingo || statue == 2) {
            imgView.setImageResource(getActivity().getResources().getIdentifier("bingo_two_pick_block", "drawable", getActivity().getPackageName()));
        } else {
            if (statue == 1) {
                imgView.setImageResource(getActivity().getResources().getIdentifier("bingo_one_pick_block", "drawable", getActivity().getPackageName()));
            } else if (statue == 3) {
                imgView.setImageResource(getActivity().getResources().getIdentifier("bingo_hell_block", "drawable", getActivity().getPackageName()));
            } else {
                imgView.setImageResource(getActivity().getResources().getIdentifier("bingo_disable_block", "drawable", getActivity().getPackageName()));
            }
        }
    }

    private void onHell() {
        txtInfo.setText("특수 블럭에 해골이 생겨서 빙고를 실패하였습니다.");
        isEnd = true;
    }
}
