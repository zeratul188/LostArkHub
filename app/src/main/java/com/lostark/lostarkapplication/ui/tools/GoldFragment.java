package com.lostark.lostarkapplication.ui.tools;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lostark.lostarkapplication.R;

public class GoldFragment extends Fragment {
    private EditText edtStore, edtPrice;
    private Button[] btnStore = new Button[3];
    private Button[] btnPrice = new Button[3];
    private Button btnDeleteStore, btnDeletePrice, btnMulPrice;
    private RadioGroup rgGroup;
    private RadioButton[] rdoGroup = new RadioButton[4];
    private LinearLayout layoutMax, layoutDiv;
    private TextView txtMax, txtSelect, txtDiv, txtResult, txtResultInfo;

    private int groups = 4;

    public GoldFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gold, container, false);

        edtStore = root.findViewById(R.id.edtStore);
        edtPrice = root.findViewById(R.id.edtPrice);
        layoutMax = root.findViewById(R.id.layoutMax);
        layoutDiv = root.findViewById(R.id.layoutDiv);
        txtMax = root.findViewById(R.id.txtMax);
        txtSelect = root.findViewById(R.id.txtSelect);
        txtDiv = root.findViewById(R.id.txtDiv);
        txtResult = root.findViewById(R.id.txtResult);
        txtResultInfo = root.findViewById(R.id.txtResultInfo);
        rgGroup = root.findViewById(R.id.rgGroup);
        btnDeletePrice = root.findViewById(R.id.btnDeletePrice);
        btnDeleteStore = root.findViewById(R.id.btnDeleteStore);
        btnMulPrice = root.findViewById(R.id.btnMulPrice);
        for (int i = 0; i < btnStore.length; i++) {
            btnStore[i] = root.findViewById(getActivity().getResources().getIdentifier("btnStore"+(i+1), "id", getActivity().getPackageName()));
            btnPrice[i] = root.findViewById(getActivity().getResources().getIdentifier("btnPrice"+(i+1), "id", getActivity().getPackageName()));
            final int index = i;
            btnStore[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int plus = 0;
                    switch (index) {
                        case 0:
                            plus = 10;
                            break;
                        case 1:
                            plus = 100;
                            break;
                        case 2:
                            plus = 1000;
                            break;
                    }
                    if (edtStore.getText().toString().equals("")) {
                        edtStore.setText(Integer.toString(plus));
                    } else {
                        int value = Integer.parseInt(edtStore.getText().toString());
                        value += plus;
                        edtStore.setText(Integer.toString(value));
                    }
                }
            });
            btnPrice[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int plus = 0;
                    switch (index) {
                        case 0:
                            plus = 10;
                            break;
                        case 1:
                            plus = 100;
                            break;
                        case 2:
                            plus = 1000;
                            break;
                    }
                    if (edtPrice.getText().toString().equals("")) {
                        edtPrice.setText(Integer.toString(plus));
                    } else {
                        int value = Integer.parseInt(edtPrice.getText().toString());
                        value += plus;
                        edtPrice.setText(Integer.toString(value));
                    }
                }
            });
        }
        for (int i = 0; i < rdoGroup.length; i++) {
            rdoGroup[i] = root.findViewById(getActivity().getResources().getIdentifier("rdoGroup"+(i+1), "id", getActivity().getPackageName()));
        }

        btnDeleteStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtStore.setText("");
            }
        });

        btnDeletePrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtPrice.setText("");
            }
        });

        btnMulPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        rgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rdoGroup1:
                        groups = 4;
                        layoutMax.setVisibility(View.VISIBLE);
                        if (edtPrice.getText().toString().equals("")) {
                            layoutDiv.setVisibility(View.GONE);
                        } else {
                            layoutDiv.setVisibility(View.VISIBLE);
                        }
                        break;
                    case R.id.rdoGroup2:
                        groups = 8;
                        layoutMax.setVisibility(View.VISIBLE);
                        if (edtPrice.getText().toString().equals("")) {
                            layoutDiv.setVisibility(View.GONE);
                        } else {
                            layoutDiv.setVisibility(View.VISIBLE);
                        }
                        break;
                    case R.id.rdoGroup3:
                        groups = 16;
                        layoutMax.setVisibility(View.VISIBLE);
                        if (edtPrice.getText().toString().equals("")) {
                            layoutDiv.setVisibility(View.GONE);
                        } else {
                            layoutDiv.setVisibility(View.VISIBLE);
                        }
                        break;
                    case R.id.rdoGroup4:
                        groups = -1;
                        layoutMax.setVisibility(View.GONE);
                        layoutDiv.setVisibility(View.GONE);
                        break;
                }
                asyncResult();
            }
        });

        edtStore.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                asyncResult();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                asyncResult();
                if (edtPrice.getText().toString().equals("") || groups == -1) {
                    layoutDiv.setVisibility(View.GONE);
                } else {
                    layoutDiv.setVisibility(View.VISIBLE);
                }
                if (edtPrice.getText().toString().equals("")) {
                    btnMulPrice.setEnabled(false);
                } else {
                    btnMulPrice.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnMulPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtPrice.getText().toString().equals("")) {
                    return;
                }
                int value = Integer.parseInt(edtPrice.getText().toString());
                value = (int)((double)value * 1.1);
                edtPrice.setText(Integer.toString(value));
            }
        });

        return root;
    }

    private void asyncResult() {
        int price, store;
        int max = 0, select = 0, div = 0, result = 0;
        if (edtStore.getText().toString().equals("")) {
            store = 0;
        } else {
            store = Integer.parseInt(edtStore.getText().toString());
        }
        if (edtPrice.getText().toString().equals("")) {
            price = 0;
        } else {
            price = Integer.parseInt(edtPrice.getText().toString());
        }
        if (groups == -1) {
            select = (int)((double)store * 0.95 / 1.1);
            result = (int)((double)store * 0.95) - select;
        } else {
            max = (int)((double)store * 0.95 * ((double)(groups-1) / (double)groups));
            select = (int)((double)max / 1.1);
            div = (int)((double)price / (double)(groups-1));
            txtMax.setText(Integer.toString(max));
            txtDiv.setText(Integer.toString(div));
            if (price == 0) {
                result = (int)((double)store * 0.95 - max);
            } else {
                result = (int)((double)store * 0.95 - (double)price - (double)price * 1 / (double)(groups-1));
            }
        }
        txtSelect.setText(Integer.toString(select));
        if (result > 0) {
            txtResult.setText(Integer.toString(result));
            txtResultInfo.setText("이득");
            txtResult.setTextColor(Color.parseColor("#7BAE5D"));
            txtResultInfo.setTextColor(Color.parseColor("#7BAE5D"));
        } else if (result == 0) {
            txtResult.setText(Integer.toString(result));
            txtResultInfo.setText("");
            txtResult.setTextColor(Color.parseColor("#AAAAAA"));
            txtResultInfo.setTextColor(Color.parseColor("#AAAAAA"));
        } else {
            result *= -1;
            txtResult.setText(Integer.toString(result));
            txtResultInfo.setText("손해");
            txtResult.setTextColor(Color.parseColor("#FF8888"));
            txtResultInfo.setTextColor(Color.parseColor("#FF8888"));
        }
    }
}
