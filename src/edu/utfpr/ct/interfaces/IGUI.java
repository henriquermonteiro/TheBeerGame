package edu.utfpr.ct.interfaces;

public interface IGUI
{
	/**
	 * Informa a interface que um jogo teve atualizações.
	 *
	 * @param gameID ID do jogo que teve atualizações.
	 */
	public void pushGameRoomUpdate(Integer gameID);
}
