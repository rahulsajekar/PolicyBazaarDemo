package PolicyBazaar.TermLifeInsurance;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Functions {
	WebDriverWait wait;
	int ssCounter=0;
	public void fillBasicDetails(RemoteWebDriver driver, String gender, String name, String dob, String mobileNo) {
		// Select Gender
		// I located in browser but not able to interact through driver
		
		// full name
		// wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#nameAdd")));
		wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nameAdd")));
		WebElement fullName = driver.findElementById("nameAdd");
		fullName.clear();
		fullName.sendKeys(name);

		// date of birth
		driver.findElementById("dob").sendKeys(dob);

		// mobile number
		driver.findElementById("mobileNo").sendKeys(mobileNo);

		// click on view free quotes
		driver.findElementById("submitButton").click();
	}

	public void answerQuestionnaries(RemoteWebDriver driver, String smoker, String income, String occupation,
			String education) {

		try {
			// smoker
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[value=\"Yes\"]")));
			if (smoker.equals("Yes")) {
				driver.findElementByCssSelector("input[value=\"Yes\"]").click();
			} else {
				driver.findElementByCssSelector("input[value=\"No\"]").click();
			}
			// income
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[class=\"radio_ul\"]")));
			switch (income) {
			case "15 Lac +":
				driver.findElementByCssSelector("li[value='6']").click();
				break;
			case "10 Lac to 14.9 Lac":
				driver.findElementByCssSelector("li[value='5']").click();
				break;
			case "7 Lac to 9.9 Lac":
				driver.findElementByCssSelector("li[value='4']").click();
				break;
			case "5 Lac to 6.9 Lac":
				driver.findElementByCssSelector("li[value='3']").click();
				break;
			case "3 Lac to 4.9 Lac":
				driver.findElementByCssSelector("li[value='2']").click();
				break;
			case "2 Lac to 2.9 Lac":
				driver.findElementByCssSelector("li[value='8']").click();
				break;
			case "Less than 2 Lac":
				driver.findElementByCssSelector("li[value='7']").click();
				break;
			}
			// need assistance(sometime ask)
			try {
				wait.until(ExpectedConditions
						.visibilityOfElementLocated(By.cssSelector("div.btngroup button[value='No']")));
				driver.findElementByCssSelector("div.btngroup button[value='No']").click();
			} catch (Exception e) {
				System.out.println("Need Assistance queation does not ask.");
			}

			// Occupation
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[class=\"btns-grp\"]")));
			if (occupation.equals("Salaried")) {
				driver.findElementByCssSelector("input[value='Salaried']").click();
			} else {
				driver.findElementByCssSelector("input[value='Self Employed']").click();
			}

			// qualification
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("li[value='1']")));
			switch (education) {
			case "College graduate & above":
				driver.findElementByCssSelector("li[value='1']").click();
				break;
			case "12th Pass":
				driver.findElementByCssSelector("li[value='3']").click();
				break;
			case "10th Pass & below":
				driver.findElementByCssSelector("xli[value='4']").click();
				break;
			}
			// maharashtra resident
			try {
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[value='Yes']")));
				driver.findElementByCssSelector("input[value='Yes']").click();
			} catch (Exception e) {
				System.out.println("Maharashtra Resident Question does not ask");
			}
		} catch (Exception e) {
			System.out.println("No questions Asked");
		}
	}

	public void closeChatBot(RemoteWebDriver driver) {
		// Sometime the chatbot is offline
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("rocketchat-iframe")));
			driver.switchTo().frame("rocketchat-iframe");
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[class='close-btn']")));
			driver.findElementByCssSelector("[class='close-btn']").click();
			driver.switchTo().parentFrame();
		} catch (TimeoutException e) {
			System.out.println("Chat Bot is Offline");
		}
	}

	public void selectAgionITermAndDownloadBrochure(RemoteWebDriver driver) {
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[@supplierid='11117']//button")));
		driver.findElementByXPath("//ul[@supplierid='11117']//button").click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[class='productHeadRight'] a")));
		driver.findElementByCssSelector("[class='productHeadRight'] a").click();
	}

	public void takeScreenshot(RemoteWebDriver driver) {
		String mainWindowHandle = driver.getWindowHandle();
		Set<String> handles = driver.getWindowHandles();
		for (String handle : handles) {
			if (!handle.equals(mainWindowHandle)) {
				driver.switchTo().window(handle);
				break;
			}
		}
		// Wait to display pdf
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// take screenshot and close window
		TakesScreenshot scrShot = ((TakesScreenshot) driver);
		File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
		File DestFile = new File(".\\ScreenShots\\"+Integer.toString(ssCounter++)+".png");
		try {
			FileUtils.copyFile(SrcFile, DestFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		driver.close();
		driver.switchTo().window(mainWindowHandle);
	}

	public void selectPlanType(RemoteWebDriver driver, String planType) {
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[class='productList']")));
		switch (planType) {
		case "Life Protect":
			driver.findElementById("lifeID").click();
			break;
		case "Protect Plus":
			driver.findElementById("lifePlusID").click();
			break;
		case "Dual Protect":
			driver.findElementById("lifeHealthID").click();
			break;
		}
	}

	public void enterAdditionalInfo(RemoteWebDriver driver, String emailID, String annualIncome, String Education,
			String Occupation, String natureOfDuties, String city, String pinCode, String residentStatus) {
		// wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div#divAdditionalInfo
		// .editData")));
		driver.findElementByCssSelector("div#divAdditionalInfo .editData").click();
		// enter email
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("txtEmail")));
		driver.findElementById("txtEmail").sendKeys(emailID);
		// annual income
		driver.findElementById("txtAnnualIncome").sendKeys(annualIncome);
		// education
		WebElement education = driver.findElementById("ddlEducation");
		Select dropDown = new Select(education);
		dropDown.selectByVisibleText(Education);

		// occupation
		WebElement occupation = driver.findElementById("ddlOccupation");
		dropDown = new Select(occupation);
		dropDown.selectByVisibleText(Occupation);

		// nature of duty
		WebElement duty = driver.findElementById("ddlNatureOfDuties");
		dropDown = new Select(duty);
		dropDown.selectByVisibleText(natureOfDuties);

		// city
		driver.findElementById("txtCity").clear();
		driver.findElementById("txtCity").sendKeys(city);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#ui-id-1 a")));
		driver.findElementByCssSelector("#ui-id-1 a").click();

		// pin
		driver.findElementById("txtPincode").sendKeys(pinCode);

		// resident status
		WebElement resident = driver.findElementById("ddlResidentialStatus");
		dropDown = new Select(resident);
		dropDown.selectByVisibleText(residentStatus);

	}

	public void generateReport(String testID, String actualPreiumAmount, String expectdPreiumAmount) {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Output");
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
		LocalDateTime now = LocalDateTime.now();
		String folderName = "./Reports/"+dtf.format(now);
		try {
			Files.createDirectories(Paths.get(folderName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		XSSFRow row1 = sheet.createRow(0);
		row1.createCell(0).setCellValue("testID");
		row1.createCell(1).setCellValue("actualPremiumAmount");
		row1.createCell(2).setCellValue("expectdPremiumAmount");
		row1.createCell(3).setCellValue("Result");
		
		XSSFRow row2 = sheet.createRow(1);
		row2.createCell(0).setCellValue(testID);
		row2.createCell(1).setCellValue(actualPreiumAmount);
		row2.createCell(2).setCellValue(expectdPreiumAmount);
		String result = actualPreiumAmount.equals(expectdPreiumAmount)?"Pass":"Fail";
		row2.createCell(3).setCellValue(result);

		FileOutputStream file;
		try {
			file = new FileOutputStream(folderName + "/output.xlsx");
			workbook.write(file);
			file.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
