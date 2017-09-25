package edu.utfpr.ct.gamecontroller;

import edu.utfpr.ct.hostgui.StartFrame;
import edu.utfpr.ct.logmanager.database.Database;
import edu.utfpr.ct.webclient.ActionService;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class TheBeerGame
{
	public static void main(String[] args)
	{
		try
		{
			if(!Database.isConnectionFree())
			{
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				JOptionPane.showMessageDialog(null, "Apenas uma instância pode ser aberta por vez.", "Erro", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}

			new ActionService(Controller.getController());
			new StartFrame(Controller.getController()).runGUI();
		}
		catch(Exception e)
		{
			System.out.println("TheBeerGame::main: " + e.getMessage());
		}
	}
}
