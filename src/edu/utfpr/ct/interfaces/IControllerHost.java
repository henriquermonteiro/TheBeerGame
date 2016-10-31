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
public interface IControllerHost {
    
    /**
     * Cria um novo jogo a partir dos parâmetros passados.
     * @param game parâmetros do jogo a ser criado.
     * @return true se criou o jogo com sucesso. false do contrário.
     */
    public boolean createGame(Game game);
    
    /**
     * Faz a requisição para restaurar um jogo.
     * Ainda é necessário um requisição para retomada das jogadas.
     * @param gameID ID do jogo a ser restaurado.
     * @return true se restaurou o jogo com sucesso. false do contrário.
     */
    public boolean restoreGame(Integer gameID);
    
    /**
     * Faz a requisição para retomar um jogo.
     * O serviço web então passa a ouvir as jogadas realizadas.
     * @param gameID ID do jogo retomado.
     * @return true se o jogo foi retomado com sucesso. false do contrário.
     */
    public boolean startGame(Integer gameID);
    
    /**
     * Faz uma requisição para o jogo parar.
     * Os dados devem estar devidamente salvos no log, e o serviço web deve parar de receber
     * jogadas e encaminhar os jogadores a uma tela de notificação.
     * @param gameID ID do jogo a ser parado.
     * @return true se o jogo foi parado com sucesso. false do contrário.
     */
    public boolean stopGame(Integer gameID);
    
    /**
     * Faz a requisição para eliminar todos os dados de um jogo.
     * @param gameID ID do jogo a ser eliminado.
     * @return true se a eliminação foi bem sucedida. false do contrário.
     */
    public boolean purgeGame(Integer gameID);
    
    /**
     * Retorna os dados de um jogo finalizado a fim de mostrar o relatório.
     * @param gameName Nome do jogo como salvo.
     * @return Dados do relatório restaurado.
     */
    public Game getGameReport(String gameName);
    
    /**
     * Retorna a lista de jogos não encerrados.
     * A lista possui apenas o ID e o nome do jogo.
     * @return Lista de jogos não encerrados.
     */
    public Game[] getUnfinishedGamesID();
 
    /**
     * Retorna a lista de jogos finalizados com o devido relatório disponível.
     * @return lista de relatórios disponíveis.
     */
    public String[] getAvailableReports();
    
    /**
     * Faz a requisição para elimição de um dado relatório.
     * @param gameName Nome do jogo/relatório salvo
     * @return true se a eliminação foi bem sucedida. false do contrário.
     */
    public boolean purgeReport(String gameName);
    
    /**
     * Faz requisição para alterar o jogador responsável por um Node.
     * @param gameID ID do jogo onde o jogador será alocado.
     * @param nodeID ID do node que terá o jogador alterado.
     * @param playerName Nome do novo jogador responsável pelo Node.
     * @return true se a mudança foi bem sucedida. false do contrário.
     */
    public boolean changePlayerForNode(Integer gameID, Integer nodeID, String playerName);
    
    /**
     * Remove o jogador do Node indicado. (Banimento)
     * @param gameID ID do jogo onde o jogador será removido.
     * @param nodeID ID do node de onde remover o jogador.
     * @return true se a remoção foi bem sucedida. false do contrário.
     */
    public boolean removePlayerFromNode(Integer gameID, Integer nodeID);
    
    /**
     * Realiza uma jogada para um Node.
     * A jogado só será válida se estiver na vez do Node correspondente, e se 
     * outra jogada não for realizada antes. Útil quando um jogador foi banido ou 
     * está a muito tempo ausente.
     * @param gameID ID do jogo onde a jogada será realizada.
     * @param nodeID ID do Node em que a jogada será realizada.
     * @param move Jogada realizada.
     * @return O montante atendido pela jogada. -1 caso a jogada tenha sido inválida.
     */
    public Integer postMoveForNode(Integer gameID, Integer nodeID, Integer move);
    
    /**
     * Faz requisição para pegar os dados de um jogo.
     * @param gameID ID do jogo em que os dados serão acessados.
     * @return Dados do jogo.
     */
    public Game getGameRoomData(Integer gameID);
}
