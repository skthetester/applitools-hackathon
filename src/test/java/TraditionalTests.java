import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.*;

/**
 * Traditional Tests without using Applitools
 * Author: Sivakumar Ganesan
 * Created: 11/06/2019
 */
public class TraditionalTests {

    WebDriver driver;
    //Choose the ACME app version. It can either be 1.0 or 2.0.
    private String ACMEAppVersion = "1.0";
    private String baseURL = "https://demo.applitools.com/";

    @BeforeTest
    public void setupDriver() {
        //Setup Chrome driver
        System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");
        driver = new ChromeDriver(options);

        //Setup the base URL based on the given version
        if (ACMEAppVersion.equals("1.0")) baseURL = baseURL + "hackathon.html";
        else if (ACMEAppVersion.equals("2.0")) baseURL = baseURL + "hackathonV2.html";
        else Assert.fail("Incorrect app version. It can either be 1.0 or 2.0.");
    }

    @Test(priority = 1)
    public void Verify_Login_Page_Elements() {
        //Open the ACME app
        driver.get(baseURL);

        //Validate the UI elements in login page
        WebElement formTitle = driver.findElement(By.xpath("/html/body/div/div/h4"));
        WebElement userNameHeader = driver.findElement(By.xpath("/html/body/div/div/form/div[1]/label"));
        WebElement pwdHeader = driver.findElement(By.xpath("/html/body/div/div/form/div[2]/label"));
        WebElement rememberMeText = driver.findElement(By.xpath("/html/body/div/div/form/div[3]/div[1]/label"));
        WebElement userNamePlaceholder = driver.findElement(By.cssSelector("#username"));
        WebElement pwdPlaceholder = driver.findElement(By.cssSelector("#password"));

        int userNameImg = driver.findElements(By.xpath("/html/body/div/div/form/div[1]/div")).size();
        int pwdImg = driver.findElements(By.xpath("/html/body/div/div/form/div[2]/div")).size();
        int loginBtn = driver.findElements(By.id("log-in")).size();
        int rememberMeChkBx = driver.findElements(By.xpath("/html/body/div/div/form/div[3]/div[1]/label/input")).size();
        int twitterLogo = driver.findElements(By.xpath("/html/body/div/div/form/div[3]/div[2]/a[1]/img")).size();
        int fbLogo = driver.findElements(By.xpath("/html/body/div/div/form/div[3]/div[2]/a[2]/img")).size();
        int linkedInLogo = driver.findElements(By.xpath("/html/body/div/div/form/div[3]/div[2]/a[3]/img")).size();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(formTitle.getText(), "Login Form", "Form title doesn't match >>");
        softAssert.assertEquals(userNameHeader.getText(), "Username", "Username header doesn't match >>");
        softAssert.assertEquals(pwdHeader.getText(), "Password", "Password header doesn't match >>");
        softAssert.assertEquals(userNamePlaceholder.getAttribute("placeholder"), "Enter your username", "Username field placeholder doesn't match >>");
        softAssert.assertEquals(pwdPlaceholder.getAttribute("placeholder"), "Enter your password", "Password field placeholder doesn't match >>");
        softAssert.assertEquals(rememberMeText.getText(), "Remember Me", "Remember me text doesn't match >>");

        /*ISSUE: Able to check if the image exists, but not if it's the same image or not */
        softAssert.assertEquals(userNameImg, 1, "Username image doesn't exist >>");
        softAssert.assertEquals(pwdImg, 1, "Password image doesn't exist >>");
        softAssert.assertEquals(loginBtn, 1, "Login button doesn't exist >>");
        softAssert.assertEquals(rememberMeChkBx, 1, "Remember me checkbox doesn't exist >>");

        /*ERROR: Even though Twitter logo exists in Version 2, it fails as the XPATH changed */
        softAssert.assertEquals(twitterLogo, 1, "Twitter logo doesn't exist >>");
        /*ERROR: Even though Facebook logo exists in Version 2, it fails as the XPATH changed */
        softAssert.assertEquals(fbLogo, 1, "Facebook logo doesn't exist >>");
        softAssert.assertEquals(linkedInLogo, 1, "LinkedIn logo doesn't exist >>");
        softAssert.assertAll();
    }

    @DataProvider(name = "data-provider")
    public Object[][] dataProviderMethod() {
        return new Object[][]{{"", "", "Both Username and Password must be present"},
                {"only username", "", "Password must be present"},
                /* ERROR: Failed to detect the error message in Version 2 for this data as XPATH changed */
                {"", "only password", "Username must be present"},
                {"test", "password", ""}};
    }

    @Test(dataProvider = "data-provider", priority = 2)
    public void Data_Driven_Test(String userNameText, String passwordText, String errorText) {
        //Open the ACME app
        driver.get(baseURL);

        //Validate the login feature
        WebElement userName = driver.findElement(By.id("username"));
        WebElement pwd = driver.findElement(By.id("password"));
        WebElement loginBtn = driver.findElement(By.id("log-in"));

        userName.sendKeys(userNameText);
        pwd.sendKeys(passwordText);
        loginBtn.click();

        if (!errorText.equals("")) {
            WebElement errorAlert = driver.findElement(By.xpath("/html/body/div/div/div[3]"));
            Assert.assertEquals(errorAlert.getText(), errorText);
        } else {
            Assert.assertTrue(driver.getCurrentUrl().contains("hackathonApp"), "Login unsuccessful >>");
        }
    }

    @Test(priority = 3)
    public void Table_Sort_Test() throws InterruptedException {
        //Open the ACME app
        driver.get(baseURL);
        //Login
        WebElement userName = driver.findElement(By.id("username"));
        WebElement pwd = driver.findElement(By.id("password"));
        WebElement loginBtn = driver.findElement(By.id("log-in"));
        userName.sendKeys("user");
        pwd.sendKeys("password");
        loginBtn.click();

        //Get table info before sort
        Thread.sleep(5000);
        Map<Double, Map<String, String>> mapBefore = new HashMap<Double, Map<String, String>>();
        for (int i = 1; i <= 6; i++) {
            Map<String, String> myMap1 = new HashMap<String, String>();
            String status = driver.findElement(By.xpath("//*[@id=\"transactionsTable\"]/tbody/tr[" + i + "]/td[1]/span[2]")).getText();
            String date = driver.findElement(By.xpath("//*[@id=\"transactionsTable\"]/tbody/tr[" + i + "]/td[2]/span[1]")).getText();
            String time = driver.findElement(By.xpath("//*[@id=\"transactionsTable\"]/tbody/tr[" + i + "]/td[2]/span[2]")).getText();
            String description = driver.findElement(By.xpath("//*[@id=\"transactionsTable\"]/tbody/tr[" + i + "]/td[3]/span")).getText();
            String category = driver.findElement(By.xpath("//*[@id=\"transactionsTable\"]/tbody/tr[" + i + "]/td[4]/a")).getText();
            String amount = driver.findElement(By.xpath("//*[@id=\"transactionsTable\"]/tbody/tr[" + i + "]/td[5]/span")).getText();
            double amount2Chk = Double.parseDouble(amount.replace(" ", "")
                    .replace(",", "")
                    .replace("+", "")
                    .replace("USD", ""));
            myMap1.put("status", status);
            myMap1.put("date", date);
            myMap1.put("time", time);
            myMap1.put("description", description);
            myMap1.put("category", category);
            myMap1.put("amount", amount);
            mapBefore.put(amount2Chk, myMap1);
        }
        List<Double> manualSortedKeys = new ArrayList(mapBefore.keySet());
        Collections.sort(manualSortedKeys);

        //Sort Table
        WebElement amountHeader = driver.findElement(By.id("amount"));
        amountHeader.click();
        Thread.sleep(5000);

        //Get table info after sort
        LinkedHashMap<Double, Map<String, String>> mapAfter = new LinkedHashMap<Double, Map<String, String>>();
        List<Double> appSortedKeys = new ArrayList();
        for (int j = 1; j <= 6; j++) {
            Map<String, String> myMap2 = new HashMap<String, String>();
            String status = driver.findElement(By.xpath("//*[@id=\"transactionsTable\"]/tbody/tr[" + j + "]/td[1]/span[2]")).getText();
            String date = driver.findElement(By.xpath("//*[@id=\"transactionsTable\"]/tbody/tr[" + j + "]/td[2]/span[1]")).getText();
            String time = driver.findElement(By.xpath("//*[@id=\"transactionsTable\"]/tbody/tr[" + j + "]/td[2]/span[2]")).getText();
            String description = driver.findElement(By.xpath("//*[@id=\"transactionsTable\"]/tbody/tr[" + j + "]/td[3]/span")).getText();
            String category = driver.findElement(By.xpath("//*[@id=\"transactionsTable\"]/tbody/tr[" + j + "]/td[4]/a")).getText();
            String amount = driver.findElement(By.xpath("//*[@id=\"transactionsTable\"]/tbody/tr[" + j + "]/td[5]/span")).getText();
            double amount2Chk = Double.parseDouble(amount.replace(" ", "")
                    .replace(",", "")
                    .replace("+", "")
                    .replace("USD", ""));
            myMap2.put("status", status);
            myMap2.put("date", date);
            myMap2.put("time", time);
            myMap2.put("description", description);
            myMap2.put("category", category);
            myMap2.put("amount", amount);
            appSortedKeys.add(amount2Chk);
            mapAfter.put(amount2Chk, myMap2);
        }

        //Validate if rows are sorted by "Amount"
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(manualSortedKeys.equals(appSortedKeys), "Sorting by Amount failed >>");

        //Validate if all rows are sorted and matching
        for (int k = 0; k < appSortedKeys.size(); k++) {
            double amountKey = appSortedKeys.get(0);
            softAssert.assertEquals(mapBefore.get(amountKey), mapAfter.get(amountKey), "Mismatch in data row after sorting >>");
        }
        softAssert.assertAll();
    }

    @Test(priority = 4)
    public void Canvas_Chart_Test() {
        //Open the ACME app
        driver.get(baseURL);

        //Login
        WebElement userName = driver.findElement(By.id("username"));
        WebElement pwd = driver.findElement(By.id("password"));
        WebElement loginBtn = driver.findElement(By.id("log-in"));
        userName.sendKeys("user");
        pwd.sendKeys("password");
        loginBtn.click();

        //Compare Expenses
        WebElement compareExp = driver.findElement(By.id("showExpensesChart"));
        compareExp.click();
        /* ERROR: Unable to validate the canvas */
        Assert.fail("Unable to validate!");

        //Show data for next year
        WebElement nxtYearData = driver.findElement(By.id("addDataset"));
        nxtYearData.click();
        /* ERROR: Unable to validate the canvas with new data */
        Assert.fail("Unable to validate!");
    }

    @Test(priority = 5)
    public void Dynamic_Content_Test() {
        //Open the ACME app
        driver.get(baseURL.replace(".html", ".html?showAd=true"));

        //Login
        WebElement userName = driver.findElement(By.id("username"));
        WebElement pwd = driver.findElement(By.id("password"));
        WebElement loginBtn = driver.findElement(By.id("log-in"));
        userName.sendKeys("user");
        pwd.sendKeys("password");
        loginBtn.click();

        int flashSale1 = driver.findElements(By.xpath("//*[@id=\"flashSale\"]/img")).size();
        int flashSale2 = driver.findElements(By.xpath("//*[@id=\"flashSale2\"]/img")).size();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(flashSale1, 1, "Flash sale 1 not found >>");
        softAssert.assertEquals(flashSale2, 1, "Flash sale 2 not found >>");
        /*ERROR: Presence of Image is validated but not the image itself. */
        softAssert.assertAll();
    }

    @AfterTest
    public void closeDriver() {
        driver.close();
    }
}
