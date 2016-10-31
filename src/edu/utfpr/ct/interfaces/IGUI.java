/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.ct.interfaces;

/**
 *
 * @author henrique
 */
public interface IGUI {
    
    /**
     * Informa a interface que um jogo teve atualizações.
     * @param gameID ID do jogo que teve atualizações.
     */
    public void pushGameRoomUpdate(Integer gameID);
}
