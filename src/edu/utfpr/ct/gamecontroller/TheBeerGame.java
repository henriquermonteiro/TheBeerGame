package edu.utfpr.ct.gamecontroller;

import edu.utfpr.ct.report.ReportManager;
import test.tests.Demo;
import test.tests.LoggerTest;
import test.tests.PlaceOrderTest;
import test.tests.ReportTest;

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
