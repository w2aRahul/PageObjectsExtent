package by.stest.utilities;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Hashtable;

import by.stest.base.Page;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.*;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import org.testng.annotations.DataProvider;

public class TestUtil extends Page {
	
	public static String screenshotName;
	
	public static void captureScreenshot() throws IOException {
		System.setProperty("org.uncommons.reportng.escape-output", "false");
		// org.uncommons.reportng.escape-output
		// Used to turn off escaping for log output in the reports (not recommended). The default is for output
		// to be escaped, since this prevents characters such as '<' and '&' from causing mark-up problems.
		// If escaping is turned off, then log text is included as raw HTML/XML, which allows for the insertion
		// of hyperlinks and other nasty hacks.
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		Date d = new Date();
		screenshotName = d.toString().replace(":", "_")
				.replace(" ", "_") + ".jpg";
		FileUtils.copyFile(scrFile,	new File("target\\surefire-reports\\html\\" + screenshotName));
	}

	@DataProvider(name = "dp")
	public static Object[][] getData(Method m) {
		String sheetName = m.getName();
		int rows = excel.getRowCount(sheetName);
		int cols = excel.getColumnCount(sheetName);

		Object[][] data = new Object[rows - 1][1];
		Hashtable<String, String> table;

		for (int rowNum = 2; rowNum <= rows; rowNum++) {	//2
			table = new Hashtable<String, String>();

			for (int colNum = 0; colNum < cols; colNum++) {
				// data[0][0]
				table.put(excel.getCellData(sheetName, colNum, 1), excel.getCellData(sheetName, colNum, rowNum));
				data[rowNum - 2][0] = table;
			}
		}
		// to print multi-dimensional array
		// System.out.println(Arrays.deepToString(data));
		return data;
	}

	public static boolean isTestRunnable(String testName, ExcelReader excel) {
		String sheetName = "test_suite";
		int rows = excel.getRowCount(sheetName);

		for (int rNum = 2; rNum <= rows; rNum++) {
			String testCase = excel.getCellData(sheetName, "TCID", rNum);
			if (testCase.equalsIgnoreCase(testName)) {
				String runMode = excel.getCellData(sheetName, "RunMode", rNum);
				return runMode.equalsIgnoreCase("Y");
			}
		}

		return false;
	}

	public static void initializeYourLogger(String fileName, String pattern) {
		ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory
				.newConfigurationBuilder();

		builder.setStatusLevel(Level.INFO);
		builder.setConfigurationName("DefaultLogger");

		// create a console appender
		AppenderComponentBuilder appenderBuilder = builder.newAppender("Console", "CONSOLE")
				.addAttribute("target", ConsoleAppender.Target.SYSTEM_OUT);
		appenderBuilder.add(builder.newLayout("PatternLayout").addAttribute("pattern", pattern));
		RootLoggerComponentBuilder rootLogger = builder.newRootLogger(Level.INFO);
		rootLogger.add(builder.newAppenderRef("Console"));

		builder.add(appenderBuilder);

		// create a rolling file appender
		LayoutComponentBuilder layoutBuilder = builder.newLayout("PatternLayout")
				.addAttribute("pattern", pattern);
		ComponentBuilder triggeringPolicy = builder
				.newComponent("Policies")
				.addComponent(builder
						.newComponent("SizeBasedTriggeringPolicy")
						.addAttribute("size", "10MB"));
		appenderBuilder = builder.newAppender("LogToRollingFile", "RollingFile")
				.addAttribute("fileName", fileName)
				.addAttribute("filePattern", fileName+"-%d{MM-dd-yy-HH-mm-ss}.log.")
				.add(layoutBuilder)
				.addComponent(triggeringPolicy);
		builder.add(appenderBuilder);
		rootLogger.add(builder.newAppenderRef("LogToRollingFile"));
		builder.add(rootLogger);
		Configurator.reconfigure(builder.build());
	}
}
