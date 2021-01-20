/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.allinone.vivu;

/**
 *
 * @author Vostro 3450
 */
public class Scene {
    public int idX;
    public int idY;
    //public Couple<Integer, Integer> luPos;//Left Up
    //public Couple<Integer, Integer> ldPos;//Left Down
    //public Couple<Integer, Integer> ruPos;//Right Up
    //public Couple<Integer, Integer> rdPos;//Right Down

//    public Scene(int i, int j, Couple<Integer, Integer> lu, 
//            Couple<Integer, Integer> ld, Couple<Integer, Integer> ru,
//            Couple<Integer, Integer> rd) {
//        idX = i;
//        idY = j;
//        luPos = lu;
//        ldPos = ld;
//        ruPos = ru;
//        rdPos = rd;
//    }
    public Scene(int i, int j) {
        idX = i;
        idY = j;
    }
    public boolean isSame(Scene other){
        return ((this.idX == other.idX) && (this.idY == other.idY));
    }
//    public boolean isIn(Couple<Integer, Integer> pos){
//        int x = pos.e1;
//        int y = pos.e2;
//        return ((x >= luPos.e1) && (x <= ruPos.e1)
//                && (y >= ldPos.e2) && (y <= luPos.e2));
//    }
}
