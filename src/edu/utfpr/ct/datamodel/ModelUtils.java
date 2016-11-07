/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.ct.datamodel;

/**
 *
 * @author henrique
 */
public class ModelUtils {
    public static int getActualNodePosition(Game game, int k){
//        return k * (game.deliveryDelay + 1);
        return k;
    }
}
