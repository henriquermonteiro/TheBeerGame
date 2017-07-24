package edu.utfpr.ct.gamecontroller;

import edu.utfpr.ct.hostgui.StartFrame;
import edu.utfpr.ct.report.ReportManager;
import edu.utfpr.ct.webclient.ActionService;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javax.servlet.ServletException;
import org.apache.catalina.LifecycleException;
import test.tests.Demo;
import test.tests.LoggerTest;
import test.tests.PlaceOrderTest;
import test.tests.ReportTest;

public class TheBeerGame
{
        private static Controller controller;
        private static ActionService server;
        private static StartFrame gUI;
        
	public static void main(String[] args) throws InterruptedException, ServletException, LifecycleException, IOException
	{
//		new LoggerTest().test();
//		new ReportTest().test();
//		new ReportManager().getReports();
//		new PlaceOrderTest().test();
//		new Demo().testPlayThrough();
//		new Demo().testGameOperations();
//		new Demo().testLoggerOperations();
            
            controller = Controller.getController();
            server = new ActionService(controller);
            gUI = new StartFrame(controller);
            
            gUI.runGUI();
	}
}
