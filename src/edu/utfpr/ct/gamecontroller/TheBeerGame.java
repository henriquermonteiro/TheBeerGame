package edu.utfpr.ct.gamecontroller;

import edu.utfpr.ct.report.ReportManager;
import edu.utfpr.ct.tests.Demo;
import edu.utfpr.ct.tests.LoggerTest;
import edu.utfpr.ct.tests.PlaceOrderTest;
import edu.utfpr.ct.tests.ReportTest;

public class TheBeerGame
{
	public static void main(String[] args) throws InterruptedException
	{
//		new LoggerTest().test();
//		new ReportTest().test();
		new ReportManager().getReports();
//		new PlaceOrderTest().test();
//		new Demo().testPlayThrough();
//		new Demo().testGameOperations();
//		new Demo().testLoggerOperations();
	}
}
