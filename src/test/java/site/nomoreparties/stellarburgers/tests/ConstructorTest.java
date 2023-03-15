package site.nomoreparties.stellarburgers.tests;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import site.nomoreparties.stellarburgers.pom.HomePageObjects;

import java.util.concurrent.TimeUnit;

import static site.nomoreparties.stellarburgers.constants.Browser.CHROME;
import static site.nomoreparties.stellarburgers.constants.Browser.YANDEX;
import static site.nomoreparties.stellarburgers.constants.Path.*;
import static site.nomoreparties.stellarburgers.constants.Url.STELLARBURGER_HOME_PAGE_URL;


@RunWith(Parameterized.class)
public class ConstructorTest {
    private final String browser;
    WebDriver driver;
    private String name = RandomStringUtils.random(6, true, true);
    private String email = RandomStringUtils.random(6, true, true) + "@mail.com";
    private String password = RandomStringUtils.random(7, true, true);
    private int selectedConstructorItemPosition = 243;

    public ConstructorTest(String browser) {
        this.browser = browser;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][]{
                {CHROME},
                {YANDEX},
        };
    }

    @Test
    @DisplayName("Проверить переход по разделам конструктора")
    public void testConstructorItems() {
        int expectedPosition = selectedConstructorItemPosition;
        runBrowser();
        driver.get(STELLARBURGER_HOME_PAGE_URL);
        HomePageObjects objHomePage = new HomePageObjects(driver);
        objHomePage.waitUntilHomePageLoaded();
        objHomePage.clickFillingBtn();
        objHomePage.checkIfClickedItemIsSelected("Начинки");
        objHomePage.checkFillingPosition(expectedPosition);
        objHomePage.clickSaucesBtn();
        objHomePage.checkIfClickedItemIsSelected("Соусы");
        objHomePage.checkSaucesPosition(expectedPosition);
        objHomePage.clickBunsBtn();
        objHomePage.checkIfClickedItemIsSelected("Булки");
        objHomePage.checkBunsPosition(expectedPosition);

    }

    @Step("Запустить браузер")
    public void runBrowser() {
        switch (browser) {
            case CHROME:
                System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
                driver = new ChromeDriver();
                break;
            case YANDEX:
                System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FOR_YANDEX_PATH);
                ChromeOptions options = new ChromeOptions();
                options.setBinary(YANDEX_BROWSER_PATH);
                driver = new ChromeDriver(options);
                break;
        }
        driver.manage().timeouts().implicitlyWait(5000,
                TimeUnit.MILLISECONDS);
        driver.manage().timeouts().pageLoadTimeout(10000,
                TimeUnit.MILLISECONDS);
        // driver.manage().window().maximize();
    }

    @Step("Закрыть браузер")
    public void closeBrowser() {

        driver.quit();
    }

    @After
    public void tearDown() {
        closeBrowser();
    }
}