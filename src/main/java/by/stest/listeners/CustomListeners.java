package by.stest.listeners;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Date;

import by.stest.base.Page;
import by.stest.utilities.MonitoringMail;
import by.stest.utilities.TestConfig;
import org.testng.*;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;

import by.stest.utilities.TestUtil;

import javax.mail.MessagingException;

public class CustomListeners extends Page implements ITestListener, ISuiteListener {
	
	private static String fileName = "Extent_" + d.toString().replace(":", "_")
			.replace(" ", "_") + ".html";
	static String messageBody;
	
	public void onTestStart(ITestResult result) {
		ExtentTest test = extent.createTest(result.getTestClass().getName() +
				"     @TestCase : "+result.getMethod().getMethodName());
		testThread.set(test);
		if (!TestUtil.isTestRunnable(result.getName(), excel)) {
			throw new SkipException("Skipping the " + result.getName().toUpperCase() +
					" as the RunMode is NO");
		}
	}

	public void onTestSkipped(ITestResult result) {
		String methodName = result.getMethod().getMethodName();
		String logText="<b>" + "Test Case:- " + methodName+ " Skipped" + "</b>";
		Markup m = MarkupHelper.createLabel(logText, ExtentColor.YELLOW);
		testThread.get().skip(m);
		String exceptionMessage = result.getThrowable().getMessage();
		testThread.get().skip(exceptionMessage);
	}

	public void onTestSuccess(ITestResult result) {
		String methodName = result.getMethod().getMethodName();
		String logText = "<b>" + "TEST CASE: - " + methodName.toUpperCase() + " PASSED" + "</b>";
		Markup m = MarkupHelper.createLabel(logText, ExtentColor.GREEN);
		testThread.get().pass(m);
	}

	public void onTestFailure(ITestResult result) {
		String exceptionMessage = Arrays.toString(result.getThrowable().getStackTrace());
		testThread.get().fail("<details>" + "<summary>" + "<b>" + "<font color=" + "red>" +
				"Exception Occurred:Click to see" + "</font>" + "</b >" + "</summary>" +
				exceptionMessage.replaceAll(",", "<br>") + "</details>" + " \n");

		try {
			TestUtil.captureScreenshot();
			testThread.get().fail("<b>" + "<font color=" + "red>" + "Screenshot of failure" +
					"</font>" + "</b>", MediaEntityBuilder.createScreenCaptureFromPath(TestUtil.screenshotName)
					.build());
		} catch (IOException e) {
			e.printStackTrace();
		}

		String failureLogg = "TEST CASE FAILED";
		Markup m = MarkupHelper.createLabel(failureLogg, ExtentColor.RED);
		testThread.get().log(Status.FAIL, m);

		System.setProperty("org.uncommons.reportng.escape-output", "false");
		Reporter.log("Click to see screenshot");
		Reporter.log("<a target=\"_blank\" href=" + TestUtil.screenshotName + ">Screenshot</a>");
		Reporter.log("<br>");
		Reporter.log("<br>");
		Reporter.log("<a target=\"_blank\" href=" + TestUtil.screenshotName + "><img src=" +
				TestUtil.screenshotName + " height=200 width=240></img></a>");
		Reporter.log("<br>");
	}

	public void onFinish(ISuite arg0) {

		MonitoringMail mail = new MonitoringMail();

		try {
			messageBody = "http://" + InetAddress.getLocalHost().getHostAddress()
					+ ":8080/job/Project03PageObjectModel/Extent_20Report/";
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		try {
			mail.sendMail(TestConfig.server, TestConfig.from, TestConfig.to, TestConfig.subject, messageBody);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	public void onFinish(ITestContext context) {
		if (extent != null) {
			extent.flush();
		}
	}

	public void onStart(ISuite suite) {
		// TODO Auto-generated method stub
		
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub
		
	}

	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub
		
	}
}
