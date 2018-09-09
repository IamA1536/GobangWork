package com.AWork.Fuction.IsWin;

import com.TeamWork.Data.Global.Data;

/**
 * 这是用于判断胜负的方法类
 * @author A
 * @version 1.1
 * @since 10.0
 */
public class IsWin implements Data {

    /**
     * 静态方法，用于判断胜负
     * @param n 连子的数目
     * @param piece 传入的棋子二维数组
     * @return 布尔值，是否分出胜负
     */

    public static boolean Checkborad(int n, int[][] piece) {
        boolean result = false;
        int c;
        for (int i = 0; i < RC; i++)
            for (int j = 0; j < RC; j++)
                if (piece[i][j] != 0) {
                    for (c = 1; c <= n; c++)
                        if (i + c < RC)
                            if (piece[i + c][j] != piece[i][j])
                                break;
                    if (c == n)
                        result = true;
                    for (c = 1; c <= n; c++)
                        if (j + c < 15)
                            if (piece[i][j + c] != piece[i][j])
                                break;
                    if (c == n)
                        result = true;
                    for (c = 1; c <= n; c++)
                        if ((i + c < RC) && (j + c < RC))
                            if (piece[i + c][j + c] != piece[i][j])
                                break;
                    if (c == n)
                        result = true;
                    for (c = 1; c <= n; c++)
                        if ((i - c > 0) && (j + c < RC))
                            if (piece[i - c][j + c] != piece[i][j])
                                break;
                    if (c == n)
                        result = true;
                }
        return result;
    }
}
