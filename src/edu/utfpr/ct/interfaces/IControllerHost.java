package edu.utfpr.ct.interfaces;

import edu.utfpr.ct.datamodel.Game;

public interface IControllerHost
{

        /**
         * Cria um novo jogo com os parâmetros passados.
         * Os parâmetros devem respeitar as condições:
         *  - Um nome deve ser único e não vazio;
         *  - O custo de não atendimento, custo de estocagem e lucro sobre a venda devem ser iguais ou maiores que zero;
         *  - A duração real e informada deve ser um número natural ( >= 0 ). A duração real deve ser >= a informada;
         *  - O atraso da entrega deve ser um número natural ( >= 0 );
         *  - O estoque inicial deve ser um número natural ( >= 0 );
         *  - Unidades a caminho devem ser um número natural ( >= 0 ).
         * 
         * @param game Joga com as configurações a ser criado.
         * @return Verdadeiro se o jogo tiver sido criado corretamente.
         * @throws IllegalArgumentException Lança exceção se o nome já tiver sido tomado, ou se algum parâmetro for inválido.
         */
        boolean createGame(Game game) throws IllegalArgumentException;

        /**
         * Retorna todos os jogos existentes.
         * 
         * @return Lista de jogos.
         */
        Game[] getGames();

        /**
         * Retorna a lista de relatórios existentes.
         * 
         * @return Lista de relatórios.
         */
        Game[] getReports();

        /**
         * Retorna a lista de jogadores que estão ligados a um jogo no momento.
         * Null se o jogo não for encontrado.
         * 
         * @param gameName Nome do jogo.
         * @return Lista de jogadores. Null se o jogo não existir.
         */
        String[] getPlayersOnGame(String gameName);

        /**
         * Retorna um jogo com seu estado atual.
         * Null se não for encontrado.
         * 
         * @param gameName Nome do jogo.
         * @return Jogo com seu estado atual.
         */
        Game getGame(String gameName);

        /**
         * Retorna a semana atual (turno) de um jogo.
         * 
         * @param gameName Nome do jogo.
         * @return Semana atual.
         */
        int getGameWeek(String gameName);

        /**
         * Retorna o código do estado do jogo cujo nome foi passado como parâmetro.
	 *
	 * SETUP = 1;
	 * RUNNING = 2;
	 * PAUSED = 4;
	 * FINISHED = 8;
	 *
	 * Se for um relatório, retorna -1.
         * Se não existir um jogo, nem um relatório, retorna -2.
	 *
         * @param gameName Nome do jogo.
	 * @return Código do estado do jogo.
         */
        int getGameState(String gameName);

        /**
         * Retorna o código do estado do relatório cujo nome foi passado como parâmetro.
         * 
         * RUNNING = 8;
         * PAUSED = 16;
         * 
         * Se não existir um relatório com o nome, retorna -1.
         * 
         * @param gameName Nome do relatório.
         * @return Código do estado do relatório.
         */
        int getReportState(String gameName);

        /**
         * Retorna um relatório na forma de um Game.
         * Null caso não tenha sido encontrado.
         * 
         * @param gameName Nome do relatório.
         * @return Relatório ou null.
         */
        Game getReport(String gameName);

        /**
         * Excluí um jogo definitivamente. 
         * A operação não pode ser revertida.
         * 
         * @param gameName Nome do jogo.
         * @return Verdadeiro se a operação for bem sucedida.
         */
        boolean purgeGame(String gameName);

        /**
         * Excluí um relatório definitivamente. 
         * A operação não pode ser revertida.
         * 
         * @param gameName Nome do relatório.
         * @return Verdadeiro se a operação for bem sucedida.
         */
        boolean purgeReport(String gameName);

        /**
         * Retorna verdadeiro se o estado do jogo tiver sido alterado para , ou já estivesse em, RUNNING ou SETUP, falso do contrário.
         * 
         * @param gameName Nome do jogo
         * @return Verdadeiro se o estado final for RUNNING ou SETUP. Falso do contrário.
         */
        boolean startGame(String gameName);

        /**
         * Retorna verdadeiro se o estado do jogo tiver sido alterado para, ou já estivesse, PAUSED, falso do contrário.
         * 
         * @param gameName Nome do jogo
         * @return Verdadeiro se o estado final for PAUSED. Falso do contrário.
         */
        boolean pauseGame(String gameName);

        /**
         * Retorna verdadeiro se o estado do relatório tiver sido alterado para, ou já estivesse, RUNNING, falso do contrário.
         * 
         * @param gameName Nome do relatório
         * @return Verdadeiro se o estado final for RUNNING. Falso do contrário.
         */
        boolean startReport(String gameName);

        /**
         * Retorna verdadeiro se o estado do relatório tiver sido alterado para, ou já estivesse, PAUSED, falso do contrário.
         * 
         * @param gameName Nome do relatório
         * @return Verdadeiro se o estado final for PAUSED. Falso do contrário.
         */
        boolean pauseReport(String gameName);

        /**
         * Adiciona um jogador na lista de uma sessão de jogo.
         * 
         * @param gameName Nome do jogo.
         * @param playerName Nome do jogador.
         * @return Verdadeiro se o jogador tiver sido adicionado. Falso do contrário.
         */
        boolean addPlayerOnGame(String gameName, String playerName);

        /**
         * Remove um jogador na lista de uma sessão de jogo.
         * 
         * @param gameName Nome do jogo.
         * @param playerName Nome do jogador.
         * @return Verdadeiro se o jogador tiver sido removido. Falso do contrário.
         */
        boolean purgePlayerOnGame(String gameName, String playerName);

        /**
         * Altera o jogador de uma posição em um jogo.
         * O nome do jogador deve estar na lista de jogadores da sessão de jogo, e não ser vazio.
         * 
         * @param gameName Nome do jogo.
         * @param function Posição a ser ocupada.
         * @param playerName Nome do jogador.
         * @return Verdadeiro se o jogador tiver sido alterado. Falso do contrário.
         */
        boolean changePlayerForNode(String gameName, IFunction function, String playerName);

        /**
         * Remove o jogador de uma posição em um jogo.
         * 
         * @param gameName Nome do jogo.
         * @param function Posição a ser removido o jogador.
         * @return Verdadeiro se bem sucedido.
         */
        boolean removePlayerFromNode(String gameName, IFunction function);

        /**
         * Realiza o movimento do jogador atual em uma sessão de jogo.
         * 
         * @param gameName Nome do jogo.
         * @param order Movimento realizado.
         * @return Movimento realizado.
         * @throws IllegalStateException O jogo não foi encontrado.
         * @throws IllegalArgumentException O movimento é negativo.
         */
        int postMoveForNode(String gameName, int order) throws IllegalStateException, IllegalArgumentException;

        /**
         * Encerra a aplicação, liberando os recursos utilizados.
         */
        void closeApplication();
}
