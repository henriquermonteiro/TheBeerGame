package edu.utfpr.ct.interfaces;

import edu.utfpr.ct.gamecontroller.Table;
import edu.utfpr.ct.datamodel.EngineData;
import edu.utfpr.ct.datamodel.Game;

public interface IControllerPlayer
{
	/**
	 * Checa se o jogador está associado a algum jogo em execução. Se sim
	 * retorna o ID do jogo.
	 *
	 * @param playerName Nome do jogador que entrou no sistema.
	 * @return ID do jogo que o usuário entrou, -1 caso o usuário não esteja
	 * ligado a nenhum jogo.
	 */
	public boolean checkIn(String playerName);

	/**
	 * Realiza uma jogada de um jogador. Retorna o montante do pedido atendido.
	 * -1 se a jogada for inválida.
	 *
	 * @param gameName ID do jogo onde a jogada será realizada.
	 * @param playerName Nome do jogador que realizou a jogada.
	 * @param order Jogada realizada.
	 * @return Montante do pedido atendido. -1 se a jogada for inválida.
	 */
	public Integer postMove(String gameName, String playerName, Integer order);

	/**
	 * Lista os jogos disponíveis para serem acessados. A lista consiste do nome
	 * e do ID dos jogos. O jogo estar disponível não implica em o usuário estar
	 * autorizado a jogar.
	 *
	 * @return lista de jogos disponíveis.
	 */
	public Game[] listAvailableGameRooms();

	/**
	 * Faz requisição para entrar em um jogo. O jogo pode ser apenas um
	 * relatório de um jogo finalizado. Pode ser a sala de espera para o início
	 * de uma partida. Pode ser a recuperação de um jogador no decorrer de uma
	 * partida. Ao entrar em um jogo em andamento, o jogador fica na sala de
	 * espera, o Host da partida pode alocar os jogadores nas posições
	 * desejadas.
	 *
	 * @param gameName Nome do jogo em que se deseja entrar.
	 * @param playerName Nome do jogador fazendo a requisição.
	 * @param password Senha para o jogo. Se o jogo não necessitar de senha, esta será ignorada.
	 * @return true se a entrada foi autorizada. false se a entrada foi negada.
	 */
	public boolean enterGameRoom(String gameName, String playerName, String password);

	/**
	 * Permite ao jogador selecionar uma posição disponível se a seleção for
	 * automática. Atenção com condição de corrida na implementação!
	 *
	 * @param gameName ID do jogo que se está selecionando a posição.
	 * @param function ID do Node selecionado.
	 * @param playerName Nome do jogador selecionando a posição.
	 * @return true se o jogador obteve a posição escolhida. false do contrário.
	 */
	public boolean selectPlayableNode(String gameName, IFunction function, String playerName);

	/**
	 * Retorna os dados de um jogo pertinentes a um jogador. Se o jogador ou o
	 * jogo forem inválidos, retorna null.
	 *
	 * @param gameName ID do jogo em que se espera obter os dados.
	 * @param playerName Nome do jogador que está requisistando os dados.
	 * @return Dados do jogo pertinentes ao jogador, ou null se ele não tiver
	 * permissão ou o jogo for inválido.
	 */
	public EngineData getGameData(String gameName, String playerName);

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
	public int getGameState(String gameName);

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
        public int getReportState(String gameName);

        /**
         * Retorna a tabela de histórico de um jogo cujo nome foi passado como parâmetro.
         * Retorna null se não tiver sido encontrado.
         * 
         * @param gameName
         * @return tabela de histórico.
         */
        public Table getTable(String gameName);

        /**
         * Retorna verdadeiro se o nome de jogador está disponível, falso do contrário.
         * 
         * @param playerName
         * @return disponibilidade de nome de jogador.
         */
        public boolean isNameAvailable(String playerName);

        /**
         * Faz o logout de um jogador. Retorna verdadeiro se um jogador foi removido, falso se não houve alteração.
         * 
         * @param player
         * @return Verdadeiro se o jogador foi removido, falso se não tiver sido encontrado.
         */
        public boolean logout(String player);
}
