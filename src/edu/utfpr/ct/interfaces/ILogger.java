package edu.utfpr.ct.interfaces;

import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.datamodel.Node;

public interface ILogger
{

        /**
         * Registra o início de um jogo.
         * 
         * @param game
         */
        void logGameStart(Game game);

        /**
         * Registra o movimento de um jogador.
         * 
         * @param gameID ID do jogo onde o movimento foi feito.
         * @param node Node do jogador que realizou a jogada.
         */
        void logPlayerMove(int gameID, Node node);

        /**
         * Remove todas as informações de um jogo.
         * A operação é irreversível.
         * 
         * @param gameID ID do jogo a ser destruído.
         */
        void purgeGame(Integer gameID);

        /**
         * Chamada para finalizar o logger e devolver recursos utilizados.
         */
        void stopLogger();

        /**
         * Retorna a lista de jogos armazenados no logger, com seu estado salvo.
         * O estado inclui as jogadas efetuadas de cada jogador e as configurações do jogo.
         * 
         * @return Lista de jogos armazenados.
         */
        Game[] getGames();
}
