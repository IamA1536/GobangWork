package com.TeamWork.Data.Global;

/**
 * 这是五子棋的基本数据
 * @author TeamWork
 * @version 1.0
 */

public interface Data {

    int RC = 15;
    int CENTER = 8;
    int size = 50;
    int radius = 25;
    int blackPiece = 1;
    int whitePiece = 2;
    int emptyPiece = 0;
    int borderPiece = -1;

    int Easy = 1;
    int Hard = 2;

    int Gaming = 0;
    int Tied = 3;
}
