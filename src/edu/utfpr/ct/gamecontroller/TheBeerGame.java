package edu.utfpr.ct.gamecontroller;

import edu.utfpr.ct.hostgui.StartFrame;
import edu.utfpr.ct.logmanager.database.Database;
import edu.utfpr.ct.webserver.ActionService;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class TheBeerGame
{
	public static void main(String[] args)
	{
		try
		{
			if(!Database.isConnected())
			{
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				JOptionPane.showMessageDialog(null, "Only one instance of the game can be running at a time", "Error", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}

			ActionService.getService();
			new StartFrame(Controller.getController()).runGUI();
		}
		catch(Exception e)
		{
			System.out.println("TheBeerGame::main: " + e.getMessage());
		}
	}
}
