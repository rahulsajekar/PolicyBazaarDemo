package PolicyBazaar.TermLifeInsurance;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestingTermLifeInsurance {

	ChromeDriver driver;
	WebDriverWait wait;
	Functions f;

	@BeforeTest(alwaysRun = true)
	public void init() {
		System.out.println("In a Before Test");
		System.setProperty("webdriver.chrome.driver", ".\\Resources\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		wait = new WebDriverWait(driver, 40);
		f = new Functions();
	}

	@Test(dataProvider = "provideTestData")
	public void testTermLifeInsurance(String testID, String gender, String name, String dob, String mobileNO,
			String smoker, String income, String occupation, String education, String productType, String emailID,
			String annualIncome, String Education, String Occupation, String natureOfDuties, String city,
			String pinCode, String residentStatus) {
		// 1) Launch Policy Bazaar
		driver.get("https://www.policybazaar.com/");
		
		// 2) Click on Term Life Insurance
		wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath("//*[@class='prd-row']//div[@class='prd-block'][1]")));
		driver.findElementByXPath("//*[@class='prd-row']//div[@class='prd-block'][1]").click();

		// 3) Fill Basic Details
		// 4) Click on View free Quotes
		f.fillBasicDetails(driver, gender, name, dob, mobileNO);

		// 5) Provide answer to questionnaires
		f.answerQuestionnaries(driver, smoker, income, occupation, education);

		// 6) Close live chat
		f.closeChatBot(driver);

		// 7) Select Aegon Life iTerm
		// 8) Click on Download Brochure
		f.selectAgionITermAndDownloadBrochure(driver);

		// 9) Capture ScreenShot and close window
		f.takeScreenshot(driver);

		// 10) Select plan type based on test data provided
		f.selectPlanType(driver, productType);

		// 11) Enter additional information
		f.enterAdditionalInfo(driver, emailID, annualIncome, Education, Occupation, natureOfDuties, city, pinCode,
				residentStatus);

		// 12) Fetch premium amount
		String actualPreiumAmount = driver.findElementById("spnPremium").getText();

		// 13) Capture and verify premium amount
		String expectdPreiumAmount = "1,016";
		Assert.assertEquals(actualPreiumAmount, expectdPreiumAmount);
		f.generateReport(testID, actualPreiumAmount, expectdPreiumAmount);
	}

	@DataProvider
	public Object[][] provideTestData() {

		try {
			FileInputStream file = new FileInputStream(".\\Resources\\Selenium Assignment.xlsx");
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheet("TestData");
			int totalRows = sheet.getLastRowNum();
			int totalCols = sheet.getRow(0).getLastCellNum();
			Object[][] testData = new Object[totalRows][totalCols];
			for (int i = 1; i <= totalRows; i++) {

				XSSFRow currentRow = sheet.getRow(i);
				for (int j = 0; j < totalCols; j++) {
					XSSFCell currentCell = currentRow.getCell(j);
					String cellData = currentCell.getStringCellValue();
					testData[i - 1][j] = cellData;
				}
			}
			workbook.close();
			return testData;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
