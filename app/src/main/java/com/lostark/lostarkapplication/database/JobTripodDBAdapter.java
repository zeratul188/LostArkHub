package com.lostark.lostarkapplication.database;

import android.app.Activity;

import java.io.IOException;
import java.io.InputStream;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class JobTripodDBAdapter {
    InputStream is = null;
    Workbook wb = null;

    public JobTripodDBAdapter(Activity activity, int position) {
        try {
            is = activity.getBaseContext().getResources().getAssets().open("jbs"+position+"_tripod.xls");
            wb = Workbook.getWorkbook(is);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
    }

    public int getSize() {
        if (wb != null) {
            Sheet sheet = wb.getSheet(0);
            if (sheet != null) {
                return sheet.getColumn(sheet.getColumns()-1).length;
            }
        }
        return 0;
    }

    public String[] readData(int number) {
        if (wb != null) {
            Sheet sheet = wb.getSheet(0);
            if (sheet != null) {
                int colTotal = sheet.getColumns();
                int rowIndexStart = 0;
                int rowTotal = sheet.getColumn(colTotal-1).length;

                String[] result = new String[colTotal];
                for (int col = 0; col < colTotal; col++) {
                    result[col] = sheet.getCell(col, number).getContents();
                }
                return result;
            }
        }

        return null;
    }

    public String[] readData(String name, int index) {
        if (wb != null) {
            Sheet sheet = wb.getSheet(0);
            if (sheet != null) {
                int colTotal = sheet.getColumns();
                int rowIndexStart = 0;
                int rowTotal = sheet.getColumn(colTotal-1).length;

                int now = 0;

                for (int row = rowIndexStart; row < rowTotal; row++) {
                    if (name.equals(sheet.getCell(0, row).getContents())) {
                        if (now == index) {
                            String[] result = new String[colTotal];
                            for (int col = 0; col < colTotal; col++) {
                                result[col] = sheet.getCell(col, row).getContents();
                            }
                            return result;
                        } else {
                            now++;
                        }
                    }
                }
            }
        }

        return null;
    }

}
