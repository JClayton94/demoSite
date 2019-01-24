package qa.newExample.demoSiteDDT.demoSite;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class DDTTest {

	private WebDriver driver;
	private ExtentReports report;
	private ExtentTest test;
	private String LoginData = "";
	private FileInputStream file;
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private XSSFCell Cell;

	@Before
	public void setup() {

		System.setProperty("webdriver.gecko.driver", Constant.WEBDRIVER_LOCATION);
		driver = new FirefoxDriver();
		report = new ExtentReports(Constant.TEST_LOG_LOCATION);

		// Test Report
		test = report.startTest("Starting Test");

		try {
			file = new FileInputStream(Constant.PATH_TESTDATA + Constant.FILE_TESTDATA);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			workbook = new XSSFWorkbook(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		sheet = workbook.getSheetAt(0);

	}

	@Test
	public void tertert() {

		driver.manage().window().maximize();
		driver.get("http://www.thedemosite.co.uk/index.php");

		Homepage homepage = PageFactory.initElements(driver, Homepage.class);

		test.log(LogStatus.INFO, "Moved to add user page");

		homepage.addUser();

		AddUser newUserPage = PageFactory.initElements(driver, AddUser.class);

		for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {

			XSSFCell username = sheet.getRow(i).getCell(0);
			XSSFCell password = sheet.getRow(i).getCell(1);

			String user = username.getStringCellValue();
			String pw = password.getStringCellValue();

			test.log(LogStatus.INFO, "Moved to login page");
			newUserPage.newUser(user, pw);

			LoginPage login = PageFactory.initElements(driver, LoginPage.class);
			test.log(LogStatus.INFO, "Logged in using user " + user + " with password " + pw);
			login.login(user, pw);

		}
		report.endTest(test);
		report.flush();

	}

	@After
	public void teardown() {

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		driver.quit();

	}

}
