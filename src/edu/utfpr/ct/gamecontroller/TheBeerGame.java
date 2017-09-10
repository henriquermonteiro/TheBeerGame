package edu.utfpr.ct.gamecontroller;

import edu.utfpr.ct.hostgui.StartFrame;
import edu.utfpr.ct.webclient.ActionService;
import java.io.IOException;
import javax.servlet.ServletException;
import org.apache.catalina.LifecycleException;
import test.tests.Demo;
import test.tests.ReportTest;

public class TheBeerGame
{
	public static void main(String[] args) throws InterruptedException, ServletException, LifecycleException, IOException
	{
//		new LoggerTest().test();
//		new ReportTest().test();
//		new ReportManager().getReports();
//		new PlaceOrderTest().test();
//		new Demo().testPlayThrough();
//		new Demo().testGameOperations();
//		new Demo().testLoggerOperations();

		new ReportTest().test();
		//new ActionService(Controller.getController());
		//new StartFrame(Controller.getController()).runGUI();
	}
}
