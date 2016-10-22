/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.ct.interfaces;

import edu.utfpr.ct.datamodel.Game;

/**
 *
 * @author henrique
 */
public interface ILogger {
    
    /**
     * Método que registra um novo jogo inicializado.
     * @param game 
     */
    public void logGameStart(Game game);
    
    /**
     * Método que registra um movimento de um jogador.
     * @param gameID ID do jogo em que a jogada ocorreu.
     * @param nodeID ID do nodo em que a jogada ocorreu.
     * @param playerName nome do jogador que executou a jogada.
     * @param week Semana/turno em que a jogada ocorreu
     * @param move Pedido/jogada realizada.
     */
    public void logPlayerMove(Integer gameID, Integer nodeID, String playerName, Integer week, Integer move);
    
    /**
     * Remove uma partida do log.
     * É executado quando um jogo é encerrado e os relatórios devidamente gerados.
     * Também é executado quando o usuário pede a remoção do jogo.
     * @param gameID ID do jogo a ser removido.
     */
    public void purgeGame(Integer gameID);
    
    /**
     * Retorna a lista de jogos não encerrados.
     * A lista possui apenas o ID e o nome do jogo.
     * @return Lista de jogos não encerrados.
     */
    public Game[] getUnfinishedGamesID();
    
    /**
     * Recupera todos os dados de um jogo.
     * Ainda é necessário executar a retrogeração para gerar os estoques e afins.
     * @param gameID ID do jogo a ser recuperado.
     * @return Dados do jogo.
     */
    public Game retrieveGameData(Integer gameID);
    
}
