/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connect4;

import java.util.*;

/**
 *
 * @author Ahmed Osama
 */

class pair{
    int first, second;
    pair(int fr, int sc){
        first = fr;
        second = sc;
    }
}

public class AI_Agent extends Agent {

    int[][] board;
    int[] colC;
    int depth, player, opp;

    pair DFS(int ro, int co, int Rmv, int Cmv, int nm, boolean spAct, int spCon) {
        pair ret = new pair(0, 0);
        if (ro == 6 || ro < 0 || co == 7 || co < 0) {
            return ret;
        }
        if ((board[ro][co] != nm && board[ro][co] != 0) ||
            (spAct && board[ro][co] != 0) ||
            (board[ro][co] == 0 && spCon <= 0))
            return ret;
        if (board[ro][co] == 0){
            ret = DFS(ro + Rmv, co + Cmv, Rmv, Cmv, nm, true, spCon - 1);
            ret.second++;
            return ret;
        }
        ret = DFS(ro + Rmv, co + Cmv, Rmv, Cmv, nm, spAct, spCon);
        ret.first++;
        return ret;
    }

    int[] dirR = {1, 0, 1, -1};
    int[] dirC = {0, 1, 1, 1};

    boolean checkForWin() {
        int count = 0;
        for (int i = 0; i < 6 && count < 4; i++) {
            for (int e = 0; e < 7 && count < 4; e++) {
                for (int d = 0; board[i][e] != 0 && d < 4 && count < 4; d++) {
                    count = DFS(i, e, dirR[d], dirC[d], board[i][e], false, 0).first;
                }
            }
        }
        return count >= 4;
    }

    ArrayList<Integer> bestRank(int turn, int curDepth, long time) {
        int bestCol = -1;
        ArrayList<Integer> curBest = new ArrayList<Integer>();
        if (checkForWin()) {
            int val = 300; //TODO: Depth-shifting
            if (turn == player) {
                val *= -1; //Negamax
            }
            curBest.add(val);
            return curBest;
        }
        boolean tie = true;
        for (int i = 0; i < 7 && tie; i++) {
            tie = (colC[i] > 5);
        }
        if (tie) {
            int val = 0; //TODO: Depth-shifting
            if (turn != player) {
                val *= -1; //Negamax
            }
            curBest.add(val);
            return curBest;
        }
        curBest.add(turn == player ? -1000000 : 1000000);
        long nw = System.currentTimeMillis();
        long diff = nw - time;
        for (int i = 0; i < 7; i++) {
            if (colC[i] <= 5) { //if there is a space to play
                int r = 5 - colC[i];
                ArrayList<Integer> tmp = new ArrayList<Integer>();
                if (curDepth >= depth && diff >= 1000) { //Run the heuristic function
                    for (int e = 0; e < 4; e++) {
                        int pl = player, op = opp;
                        if(turn != player){
                            pl = opp;
                            op = player;
                        }
                        int rank, count, spaces;
                        pair ret = DFS(r + dirR[e], i + dirC[e], dirR[e], dirC[e], pl, false, 2);
                        count = ret.first;
                        spaces = ret.second;
                        ret = DFS(r + dirR[e] * -1, i + dirC[e] * -1, dirR[e] * -1, dirC[e] * -1, pl, false, 2);
                        count += ret.first;
                        spaces += ret.second;
                        if(spaces + count < 3)////
                            count = 0;////
                        if (count >= 3) {
                            count *= 100;//////////
                        } else if (count == 2) {
                            count *= 2;///////////
                        }
                        rank = count;
                        ret = DFS(r + dirR[e], i + dirC[e], dirR[e], dirC[e], op, false, 2);
                        count = ret.first;
                        spaces = ret.second;
                        ret = DFS(r + dirR[e] * -1, i + dirC[e] * -1, dirR[e] * -1, dirC[e] * -1, op, false, 2);
                        count += ret.first;
                        spaces += ret.second;
                        if(spaces + count < 3)////
                            count = 0;////
                        if (count >= 3) {
                            count *= 100;//////////
                        } else if (count == 2) {
                            count *= 2;///////////
                        }
                        rank += count; //TODO: Depth-shifting
                        tmp.add(rank);
                    }
                    if(turn != player) {
                        for (int it = 0; it < tmp.size(); it++) {
                            int val = tmp.get(it);
                            val *= -1;
                            tmp.set(it, val);
                        }
                        Collections.sort(tmp);
                    }
                    else
                        Collections.sort(tmp, Collections.reverseOrder());
                } else {
                    board[r][i] = turn;
                    colC[i]++;
                    tmp = bestRank((turn == player ? opp : player), curDepth + 1, time); //Backtracking
                    board[r][i] = 0;
                    colC[i]--;
                }
                for (int e = 0;;) {
                    if (tmp.get(e) > curBest.get(e)) {
                        if(turn == player){
                            curBest = tmp;
                            bestCol = i;
                        }
                        break;
                    } else if (tmp.get(e) < curBest.get(e)) {
                        if(turn != player){
                            curBest = tmp;
                            bestCol = i;
                        }
                        break;
                    }
                    e++;
                    if (e == curBest.size()) {
                        curBest = tmp;
                        bestCol = i;
                        break;
                    } else if (e == tmp.size()) {
                        break;
                    }
                }
            }
        }
        if(curDepth == 0){
            curBest.clear();
            curBest.add(bestCol);
        }
        return curBest;
    }

    int Play(int[][] brd, int[] colc, int op, int dep) {
        board = brd;
        colC = colc;
        depth = dep;
        player = id;
        opp = op;
        long time = System.currentTimeMillis();
        ArrayList<Integer> ret = bestRank(player, 0, time);
        return ret.get(0);
    }
}
