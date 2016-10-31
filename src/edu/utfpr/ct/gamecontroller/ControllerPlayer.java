package edu.utfpr.ct.gamecontroller;

import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.interfaces.IControllerPlayer;

public class ControllerPlayer implements IControllerPlayer
{
	@Override
	public Integer checkIn(String playerName)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Integer postMove(Integer gameID, Integer nodeID, String playerName, Integer move)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Game[] listAvailableGameRooms()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean enterGameRoom(Integer gameID, String playerName)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean selectPlayableNode(Integer gameID, Integer nodeID, String playerName)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
