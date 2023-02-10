package org.evs.vtiger.utilitycode;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.google.common.io.Files;

public class WebDriverUtil {
	static ExtentTest test;
	static ExtentReports reports;
	static WebDriver driver;

	public static void main(String[] args) throws IOException {
		extentReport("size ", "TC001");
		launchBrowserAndOpenUrl("ChromeDriver", "http://localhost:8888/");// launchBrowser method call
		waitSetImplicitWait(5);
	//	attachScreenshot("sky.png");
		sendTextboxValue("name", "user_name", "userName", "admin");
		sendTextboxValue("cssSelector", "input[name='user_password']", "userPassword", "sarita");
// WebElement colorTheme = getWebElement("xpath", "//select[@name='login_theme']");
	//	giveFirstSelected("xpath", "//select[@name='login_theme']");
//		giveAllOption("xpath", "//select[@name='login_theme']","colorTheme");
//		giveAllOption("name", "login_language","Language");
		// giveSelectedOption("xpath", "//select[@name='login_theme']");
		//giveSize("name", "Login", " loginButtonImage");
		validateSize("name", "Login", " loginButtonImage", 138, 40 );

		click("name", "Login", " loginButtonImage");
//		click("linkText", "Marketing", "homePage");
		//validateInnerText("xpath", "//td[@class=\"moduleName\"]", "home Page header", "My Home Page > Home");
		//validateSize("name", "Login", " loginButtonImage", null);
		flushNew();

	}

	/*
	 * Description : this is used to find out time details
	 */
	public static String timeDetails() {
		DateFormat df = new SimpleDateFormat("MM_dd_yyyy__hh_mm_ss");
		return df.format(new Date());
	}

	public static void extentReport(String fileName, String testCaseName) throws IOException {
		File file = new File(fileName + timeDetails() + ".html");
		ExtentSparkReporter reportObj = new ExtentSparkReporter(file);
		reports = new ExtentReports();
		reports.attachReporter(reportObj);
		test = reports.createTest(testCaseName);
	}

	/**
		 * 
		 */
	public static void flushNew() {
		// new ExtentReports();
		reports.flush();
	}

	/**
	 * Description :with the help of this method we can invoke our browser
	 * 
	 * @param browserName: it is used to give browser name
	 * @param url          : it is used to give to open application
	 * @return : WebDriver reference variable
	 */
	public static WebDriver launchBrowserAndOpenUrl(String browserName, String url) {
		try {
			if (browserName.equalsIgnoreCase("chromeDriver")) {
				driver = new ChromeDriver();
				test.log(Status.INFO, " Chrome Browser has been launched succesfully");
			} else if (browserName.equalsIgnoreCase("firfoxDriver")) {
				driver = new FirefoxDriver();
				test.log(Status.INFO, " Firefox browser has been launched succesfully");
			} else if (browserName.equalsIgnoreCase("edgeDriver")) {
				driver = new EdgeDriver();
				test.log(Status.INFO, " EdgeDriver browser has been launched succesfully");
			} else if (browserName.equalsIgnoreCase("InternetExplorerDriver")) {
				driver = new InternetExplorerDriver();
				test.log(Status.INFO, " InternetExplorerDriver browser has been launched succesfully");
			} else if (browserName.equalsIgnoreCase("SafariDriver")) {
				driver = new SafariDriver();
				test.log(Status.INFO, " SafariDriver browser has been launched succesfully");
			}
		} catch (Exception e) {
			e.printStackTrace();
			test.log(Status.FAIL, " browser is not launched,browser name is  not valid, please check.. ");
		}
		try {
			driver.get(url);
			test.log(Status.INFO, url + " is entered succesfully");
		} catch (Exception e) {
			driver.navigate().to(url);
			test.log(Status.INFO, url + " is entered succesfully");
		} catch (Throwable e) {
			e.getMessage();
			screenShot("url");
			test.log(Status.FAIL, url + " is not entered succesfully");
		}
		return driver;
	}

	public static WebElement getWebElement(String locatorType, String locatorValue) {
		WebElement userObj = null;
		if (locatorType.equalsIgnoreCase("xpath")) {
			return userObj = driver.findElement(By.xpath(locatorValue));
		} else if (locatorType.equalsIgnoreCase("linkText")) {
			return userObj = driver.findElement(By.linkText(locatorValue));
		} else if (locatorType.equalsIgnoreCase("name")) {
			return userObj = driver.findElement(By.name(locatorValue));
		} else if (locatorType.equalsIgnoreCase("className")) {
			return userObj = driver.findElement(By.className(locatorValue));
		} else if (locatorType.equalsIgnoreCase("id")) {
			return userObj = driver.findElement(By.id(locatorValue));
		} else if (locatorType.equalsIgnoreCase("cssSelector")) {
			return userObj = driver.findElement(By.cssSelector(locatorValue));
		} else if (locatorType.equalsIgnoreCase("tagName")) {
			return userObj = driver.findElement(By.tagName(locatorValue));
		} else if (locatorType.equalsIgnoreCase("partialLinkText")) {
			return userObj = driver.findElement(By.partialLinkText(locatorValue));
		} else {
			test.log(Status.FAIL, locatorType + "Locator Type is  wrong..please check");
		}
		return userObj;
	}

	public static boolean checkElement(String locatorType, String locatorValue, String elementName) {
		boolean status = false;
		WebElement userObj = getWebElement(locatorType, locatorValue);
		if (userObj.isDisplayed() == true) {
			test.log(Status.PASS, elementName + " is visible");
			if (userObj.isEnabled() == true) {
				test.log(Status.PASS, elementName + " is enabled");
				status = true;
			} else {
				test.log(Status.FAIL, elementName + " is not enabled");
			}
		} else {
			test.log(Status.FAIL, elementName + " is not visible");

		}
		return status;
	}

	public static void sendTextboxValue(String locatorType, String locatorValue, String elementName, String sendValue) {
		WebElement userObj = null;
		try {
			userObj = getWebElement(locatorType, locatorValue);
			boolean st = checkElement(locatorType, locatorValue, elementName);
			if (st == true) {
				userObj.clear();
				userObj.sendKeys(sendValue);
				test.log(Status.INFO, sendValue + " is entered succesfully by WebElement");
			}
		} catch (ElementNotInteractableException e) {
			new Actions(driver).sendKeys(sendValue).build().perform();
			test.log(Status.INFO, sendValue + " is entered succesfully by Actions ");

		} catch (Exception e) {
			((JavascriptExecutor) driver).executeScript("arguments[0].value=\"" + sendValue + "", userObj);
			test.log(Status.INFO, sendValue + " is entered succesfully by JavascriptExecutor ");

		} catch (Throwable e) {
			e.printStackTrace();
			test.log(Status.FAIL, sendValue + " is not entered succesfully ,please check ");
			screenShot(elementName);
		}
	}

	public static void click(String locatorType, String locatorValue, String elementName) throws IOException {
		WebElement userObj = null;
		try {
			userObj = getWebElement(locatorType, locatorValue);
			boolean st = checkElement(locatorType, locatorValue, elementName);
			if (st == true) {
				userObj.click();
				test.log(Status.INFO, elementName + " is clicked succesfully by WebElement");
			}
		} catch (ElementNotInteractableException e) {
			new Actions(driver).click().build().perform();
			test.log(Status.INFO, elementName + " is clicked succesfully by Actions");

		} catch (Exception e) {
			((JavascriptExecutor) driver).executeScript("arguments[0].click()", userObj);
			test.log(Status.INFO, elementName + " is clicked succesfully by JavascriptExecutor ");

		} catch (Throwable e) {
			e.printStackTrace();
			test.log(Status.FAIL, elementName + " is not clicked succesfully ,please check ");
			screenShot(elementName);
		}
	}

	public static void screenShot(String screenshotImageName) {
		TakesScreenshot scr = (TakesScreenshot) driver;
		File from = scr.getScreenshotAs(OutputType.FILE);
		String tm = timeDetails();
		File to = new File("snapshots\\ " + screenshotImageName + tm + ".png");
		try {
			Files.copy(from, to);
			test.log(Status.INFO, "ScreenShot copy on your Destination");

		} catch (Exception e) {
			e.printStackTrace();
			test.log(Status.FAIL, "ScreenShot not copy on your Destination");
		}
	}

	/**
	 * @param path
	 */
	public static void attachScreenshot(String path) {
		File f = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		File t = new File(path);
		try {
			Files.copy(f, t);
			test.log(Status.INFO, "ScreenShot copy on your Destination");
		} catch (IOException e) {
			e.printStackTrace();
			test.log(Status.FAIL, "ScreenShot not copy on your Destination");
		}
		test.addScreenCaptureFromPath(t.getAbsolutePath());
	}

	public static void selectByText(String locatorType, String locatorValue, String elementName, String textValue) {
		WebElement userObj = null;
		try {
			userObj = getWebElement(locatorType, locatorValue);

			boolean st = checkElement(locatorType, locatorValue, elementName);
			if (st == true) {
				Select selectObj = new Select(userObj);
				selectObj.selectByVisibleText(textValue);
				test.log(Status.INFO, "DropDown is handled with the " + textValue);
			}

		} catch (Exception e) {
			screenShot(elementName);
			test.log(Status.FAIL, "DropDown is not handled with the " + textValue);
		}
	}

	public static void selectByValueAttribute(String locatorType, String lovatorValue, String elementName,
			String valueAttribute) {
		try {
			WebElement userObj = getWebElement(locatorType, lovatorValue);
			boolean st = checkElement(locatorType, lovatorValue, elementName);
			if (st == true) {
				Select selectObj = new Select(userObj);
				selectObj.selectByValue(valueAttribute);
				test.log(Status.INFO, "DropDown is handled with the " + valueAttribute);

			}
		} catch (Exception e) {
			screenShot("loginButton");
			test.log(Status.FAIL, "DropDown is not handled with the " + valueAttribute);

		}
	}

	public static void selectByIndexed(String locatorType, String lovatorValue, String elementName, int indexValue) {
		try {
			WebElement userObj = getWebElement(locatorType, lovatorValue);
			boolean st = checkElement(locatorType, lovatorValue, elementName);
			if (st == true) {
				Select selectObj = new Select(userObj);
				selectObj.selectByIndex(indexValue);
				test.log(Status.INFO, "DropDown is handled with the" + indexValue);

			}
		} catch (Exception e) {
			screenShot("loginButton");
			test.log(Status.FAIL, "DropDown is not handled with the" + indexValue);

		}
	}

	public static void validateInnerText(String locatorType, String locatorValue, String elementName, String exceptedText) {
		String actualtext = null;
		try {
			actualtext = getInnerText(locatorType, locatorValue, elementName);
			if (actualtext.equalsIgnoreCase(exceptedText)) {
				test.log(Status.INFO,
						actualtext + " test validation passed. actualtext is matched with expectedtext" + exceptedText);
			}
		} catch (Exception e) {
			test.log(Status.FAIL,
					actualtext + " test validation failed.  actualtext is matched with expectedtext" + exceptedText);
			screenShot(elementName);
		}

	}

	public static String getInnerText(String locatorType, String locatorValue, String elementName) {
		String getinn = null;
		try {
			WebElement userObj = getWebElement(locatorType, locatorValue);
			boolean ce = checkElement(locatorType, locatorValue, elementName);
			if (ce == true) {
				getinn = userObj.getText();
				test.log(Status.INFO, "this method is worked succesfully");
			}
		} catch (Exception e) {
			screenShot(elementName);
			test.log(Status.FAIL, "this method is not worked succesfully");
		}
		return getinn;
	}

	public static void validateAttributeValue(String locatorType, String locatorValue, String attributeValue,
			String elementName, String expectedText) {
		String actualText = null;
		try {
			actualText = getAttributeValue(locatorType, locatorValue, elementName, attributeValue);
			if (actualText.equalsIgnoreCase(expectedText)) {
				test.log(Status.PASS, elementName + "  test validation passed. actualText- " + actualText
						+ " expectedText-" + expectedText);
			}
		} catch (Exception e) {
			test.log(Status.FAIL, elementName + "  test validation failed. actualText- " + actualText + " expectedText-"
					+ expectedText);
		}
	}

	public static String getAttributeValue(String locatorType, String locatorValue, String elementName,
			String attributeValue) {
		String getinn = null;
		try {
			WebElement userObj = getWebElement(locatorType, locatorValue);
			boolean ce = checkElement(locatorType, locatorValue, elementName);
			if (ce == true) {
				getinn = userObj.getAttribute(attributeValue);
				test.log(Status.INFO, "this method is worked succesfully");

			}
		} catch (Exception e) {
			screenShot(elementName);
			test.log(Status.FAIL, "this method is not worked succesfully");

		}
		return getinn;
	}
public static void validateSize(String locatorType, String locatorValue, String elementName, int wh,int ht) {
	Dimension expectedSize=new Dimension(wh, ht);
	Dimension actualSize=null;
	try{
		actualSize=	giveSize(locatorType, locatorValue, elementName);
		System.out.println(actualSize+"=="+expectedSize);
	if(actualSize.equals(expectedSize)) {
		System.out.println("matched");
		test.log(Status.INFO,actualSize +" is matched with "+ expectedSize);
	}else {
		System.out.println("miss-matched");

	}
}catch(Exception e){
	test.log(Status.FAIL,actualSize+ " is not matched with "+ expectedSize);
}
}
	public static Dimension giveSize(String locatorType, String locatorValue, String elementName ) {
		Dimension getSize = null;
		try {
			WebElement userObj = getWebElement(locatorType, locatorValue);
			boolean ce = checkElement(locatorType, locatorValue, elementName);
			if (ce == true) {
				getSize = userObj.getSize();
			int	 ht = getSize.getHeight();
			int	 wh = getSize.getWidth();
				 
				//System.out.println("height is "+ht +"width is "+wh);
			System.out.println(getSize);
				test.log(Status.INFO, "Size of " + elementName + " Height is " + ht + " Widht is " + wh);
			}
		} catch (Exception e) {
			screenShot(elementName);
			test.log(Status.FAIL, "The Size is not matched of " + elementName);
		}
		return getSize;
	}

	public static Point giveLocation(String locatorType, String locatorValue, String elementName) {
		Point getLocationObj = null;
		try {
			WebElement userObj = getWebElement(locatorType, locatorValue);
			boolean ce = checkElement(locatorType, locatorValue, elementName);
			if (ce == true) {
				getLocationObj = userObj.getLocation();
				int xValue = getLocationObj.getX();
				int yValue = getLocationObj.getY();
				test.log(Status.INFO, "Location of " + elementName + " X- " + xValue + " Y- " + yValue);
			}
		} catch (Exception e) {
			screenShot(elementName);
			test.log(Status.FAIL, "The Location is not matched of " + elementName);
		}
		return getLocationObj;
	}
	public static void validateLocation(String locatorType, String locatorValue, String elementName,String expectedLocation) {
		Point actualLocation=null;
		try{
		
		if(actualLocation.equals(expectedLocation)) {
			test.log(Status.INFO,actualLocation+ " is matched with "+ expectedLocation);
		}
	}catch(Exception e){
		test.log(Status.FAIL,actualLocation+ " is not matched with "+ expectedLocation);
	}
	}
	public static void titleName(String locatorType, String locatorValue, String elementName) {
		String getTitle = null;
		try {
			WebElement userObj = getWebElement(locatorType, locatorValue);
			boolean ce = checkElement(locatorType, locatorValue, elementName);
			if (ce == true) {
				getTitle = driver.getTitle();
			}
		} catch (Exception e) {
			screenShot(elementName);
		}
	}

	public static void windowHandle(String locatorType, String locatorValue, String elementName) {
		String winHandleObj = null;
		try {
			WebElement userObj = getWebElement(locatorType, locatorValue);
			boolean ce = checkElement(locatorType, locatorValue, elementName);
			if (ce == true) {
				winHandleObj = driver.getWindowHandle();
			}
		} catch (Exception e) {
			screenShot(elementName);
		}
	}

	public static void windowHandles(String locatorType, String locatorValue, String elementName, String title) {
		Set<String> winHandlesObj1 = null;
		try {
			WebElement userObj = getWebElement(locatorType, locatorValue);
			boolean ce = checkElement(locatorType, locatorValue, elementName);
			if (ce == true) {
				winHandlesObj1 = driver.getWindowHandles();
				for (String str : winHandlesObj1) {
					String title1 = driver.switchTo().window(str).getTitle();
					if (title1.equalsIgnoreCase(title)) {
						break;
					} else {
						test.log(Status.FAIL, MarkupHelper.createLabel(
								title + " title is not matched with " + title1 + " title", ExtentColor.RED));
					}
				}
			}
		} catch (Exception e) {
			screenShot(elementName);
		}
	}

	public static void handleFrame(int frameNumber) {
		try {
			driver.switchTo().frame(frameNumber);
			test.log(Status.INFO, frameNumber + " is entered succesfully ");
		} catch (Exception e) {
			test.log(Status.FAIL, frameNumber + " is not entered succesfully ");

		}
	}

	public static void handleFrame(String IDOrName) {
		try {
			driver.switchTo().frame(IDOrName);
			test.log(Status.INFO, IDOrName + " is correct");
		} catch (Exception e) {
			test.log(Status.FAIL, IDOrName + " is not correct..please check");

		}
	}

	public static void handleFrame(WebElement frameElement) {
		try {
			driver.switchTo().frame(frameElement);
			test.log(Status.INFO, frameElement + " is correct");
		} catch (Exception e) {
			test.log(Status.FAIL, frameElement + " is not correct..please check");

		}
	}

	public static void goParentFrame() {
		try {
			driver.switchTo().parentFrame();
			test.log(Status.INFO, "it is entered in parent frame succesfully");
		} catch (Exception e) {
			test.log(Status.FAIL, "it is not entered in parent frame succesfully");
		}
	}

	public static void outFromFrame() {
		try {
			driver.switchTo().defaultContent();
			test.log(Status.INFO, "");
		} catch (Exception e) {

		}
	}

	public static void acceptPopUp() {
		driver.switchTo().alert().accept();
	}

	public static void dismissPopUp() {
		driver.switchTo().alert().dismiss();
	}

	public static void getTextFromPopUp() {
		try {
			String text = driver.switchTo().alert().getText();
			test.log(Status.INFO, "the text of this popup is " + text);
		} catch (Exception e) {
			test.log(Status.FAIL, "the text of this popup is wrong");
		}
	}

	public static void sendValueInPopUp(String sendValue) {
		try {
			driver.switchTo().alert().sendKeys(sendValue);
			test.log(Status.INFO, sendValue + " is entered succesfully");
		} catch (Exception e) {
			test.log(Status.FAIL, sendValue + " is not entered succesfully");

		}
	}

	public static void uploadfile(String locatorType, String locatorValue, String fileValuePath) {
		try {
			WebElement fileObj = getWebElement(locatorType, locatorValue);
			fileObj.sendKeys(fileValuePath);
			test.log(Status.INFO, fileValuePath + " is correct file is uploded");
		} catch (Exception e) {
			test.log(Status.FAIL, fileValuePath + " is not correct file is not uploded");

		}
	}

	public static void scrollwithJavaScriptUsingElement(WebElement scrollObj) {
		((JavascriptExecutor) driver).executeScript("arguments[0].ScrollIntoview()", scrollObj);
	}

	public static void scrollwithJavaScriptusingCoordinates(int x, int y) {
		((JavascriptExecutor) driver).executeScript("window.scrollBy(" + x + "," + y + ")", " ");
	}

	public static void colorOrBackGroundColorOfElement(String locatorType, String locatorValue, String colorElementName,
			String codeOfColorElement) {
		try {
			String colorObj = getWebElement(locatorType, locatorValue).getCssValue(colorElementName);
			String value = Color.fromString(colorObj).asHex();
			if (value.equals(codeOfColorElement)) {
				test.log(Status.INFO, colorElementName + " is right");
			}
		} catch (Exception e) {
			test.log(Status.FAIL, colorElementName + " is wrong");
			e.printStackTrace();
		}
	}

	public static void rightClick(String locatorType, String locatorValue) {
		try {
			WebElement click = getWebElement(locatorType, locatorValue);
			Actions action = new Actions(driver);
			action.contextClick(click).build().perform();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void doClickAndRelease(String locatorType, String locatorValue, String locatorType1,
			String locatorValue1) {
		try {
			WebElement hold = getWebElement(locatorType, locatorValue);
			WebElement real = getWebElement(locatorType1, locatorValue1);
			Actions action = new Actions(driver);
			action.clickAndHold(hold).release(real).build().perform();

			test.log(Status.INFO, " this element released by action class");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void doDragAndDrop(String locatorType, String locatorValue, String locatorTypeDrop,
			String locatorValueDrop, String elementName) {
		try {
			WebElement drag = getWebElement(locatorType, locatorValue);
			WebElement drop = getWebElement(locatorTypeDrop, locatorValueDrop);
			Actions action = new Actions(driver);
			test.log(Status.INFO, MarkupHelper
					.createLabel("Drag and drop is performed on" + elementName + " successfully", ExtentColor.BLACK));
			action.dragAndDrop(drag, drop);
		} catch (Exception e) {
			e.printStackTrace();
			test.log(Status.FAIL, MarkupHelper
					.createLabel("Drag and drop is not performed on" + elementName + " successfully", ExtentColor.RED));
		}
	}

	public static void waitSetImplicitWait(int timeunit) {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeunit));
	}

	public static void waitElementToBeClickable(String locatortype, String locatorvalue, int seconds) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
		wait.until(ExpectedConditions
				.not(ExpectedConditions.elementToBeClickable(getWebElement(locatortype, locatorvalue))));
	}

	public static void giveFirstSelected(String locatorType, String locatorValue) {
		WebElement selElement = getWebElement(locatorType, locatorValue);
		Select first = new Select(selElement);
		String firstObj = first.getFirstSelectedOption().getText();
		test.log(Status.INFO,
				MarkupHelper.createLabel(firstObj + " is selected option in dropDown", ExtentColor.GREEN));
	}

	public static void giveAllOption(String locatorType, String locatorValue, String elementName) {
		ArrayList<String> arr = new ArrayList<String>();
		Select sel = new Select(getWebElement(locatorType, locatorValue));
		List<WebElement> list = sel.getOptions();
		for (int i = 0; i <= list.size() - 1; i++) {
			WebElement webobj = list.get(i);
			String g = webobj.getText();
			arr.add(g);
		}
		test.log(Status.INFO, MarkupHelper.createLabel(arr + " all are options of " + elementName, ExtentColor.BLUE));

	}
}
