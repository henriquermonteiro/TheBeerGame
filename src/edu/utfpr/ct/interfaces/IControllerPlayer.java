package edu.utfpr.ct.interfaces;

import edu.utfpr.ct.datamodel.Game;

public interface IControllerPlayer
{
	/**
	 * Checa se o jogador está associado a algum jogo em execução.
	 * Se sim retorna o ID do jogo.
	 *
	 * @param playerName Nome do jogador que entrou no sistema.
	 * @return ID do jogo que o usuário entrou, -1 caso o usuário não esteja ligado a nenhum jogo.
	 */
	public Integer checkIn(String playerName);

	/**
	 * Realiza uma jogada de um jogador.
	 * Retorna o montante do pedido atendido. -1 se a jogada for inválida.
	 *
	 * @param gameName Nome do jogo onde a jogada será realizada.
	 * @param nodeID ID do Node onde a jogada será realizada.
	 * @param playerName Nome do jogador que realizou a jogada.
	 * @param move Jogada realizada.
	 * @return Montante do pedido atendido. -1 se a jogada for inválida.
	 */
	public Integer postMove(String gameName, Integer nodeID, String playerName, Integer move);

	/**
	 * Lista os jogos disponíveis para serem acessados.
	 * A lista consiste do nome e do ID dos jogos.
	 * O jogo estar disponível não implica em o usuário estar autorizado a jogar.
	 *
         * @param playerName Nome do usuário. Jogadores convidados podem não ter acesso a todos os recursos.
	 * @return lista de jogos disponíveis.
	 */
	public Game[] listAvailableGameRooms(String playerName);

	/**
	 * Faz requisição para entrar em um jogo.
	 * O jogo pode ser apenas um relatório de um jogo finalizado.
	 * Pode ser a sala de espera para o início de uma partida.
	 * Pode ser a recuperação de um jogador no decorrer de uma partida.
	 * Ao entrar em um jogo em andamento, o jogador fica na sala de espera,
	 * o Host da partida pode alocar os jogadores nas posições desejadas.
	 *
	 * @param gameName Nome do jogo em que se deseja entrar.
	 * @param playerName Nome do jogador fazendo a requisição.
         * @param password Senha para o jogo. Se o jogo não necessitar de senha, esta será ignorada.
	 * @return true se a entrada foi autorizada. false se a entrada foi negada.
	 */
	public boolean enterGameRoom(String gameName, String playerName, String password);

	/**
	 * Permite ao jogador selecionar uma posição disponível se a seleção for automática.
	 * Atenção com condição de corrida na implementação!
	 *
	 * @param gameName Nome do jogo que se está selecionando a posição.
	 * @param nodeID ID do Node selecionado.
	 * @param playerName Nome do jogador selecionando a posição.
	 * @return true se o jogador obteve a posição escolhida. false do contrário.
	 */
	public boolean selectPlayableNode(String gameName, Integer nodeID, String playerName);
        
        /**
         * Retorna os dados de um jogo pertinentes a um jogador.
         * Se o jogador ou o jogo forem inválidos, retorna null.
         * 
         * @param gameName Nome do jogo em que se espera obter os dados.
         * @param playerName Nome do jogador que está requisistando os dados.
         * @return Dados do jogo pertinentes ao jogador, ou null se ele não tiver permissão ou o jogo for inválido.
         */
        public Game getGameData(String gameName, String playerName);
        
        /**
         * Retorna o código do estado do jogo cujo nome foi passado como parâmetro.
         * 
         * SETUP = 1;
         * RUNNING = 2;
         * PAUSED = 4;
         * FINISHED = 8;
         * 
         * @param gameName
         * @return Código do estado do jogo.
         */
        public int getGameState(String gameName);
}
