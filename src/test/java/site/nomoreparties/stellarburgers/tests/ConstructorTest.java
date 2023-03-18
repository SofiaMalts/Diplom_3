package site.nomoreparties.stellarburgers.tests;

import general.Functions;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import site.nomoreparties.stellarburgers.pom.HomePageObjects;

import static site.nomoreparties.stellarburgers.constants.Browser.CHROME;
import static site.nomoreparties.stellarburgers.constants.Browser.YANDEX;
import static site.nomoreparties.stellarburgers.constants.Url.STELLARBURGER_HOME_PAGE_URL;


@RunWith(Parameterized.class)
public class ConstructorTest {
    private final String browser;
    WebDriver driver;
    private int selectedConstructorItemPosition = 243;
    private String bunsSectionText = "Булки";
    private String sauceSectionText = "Соусы";
    private String fillingSectionText = "Начинки";

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

    @Before
    public void runBrowserOne() {
        driver = Functions.runBrowser(browser);
    }

    @Test
    @DisplayName("Проверить переход по разделам конструктора")
    public void testConstructorItems() {
        int expectedPosition = selectedConstructorItemPosition;

        driver.get(STELLARBURGER_HOME_PAGE_URL);
        HomePageObjects objHomePage = new HomePageObjects(driver);
        objHomePage.waitUntilHomePageLoaded();
        objHomePage.clickFillingBtn();
        objHomePage.checkIfSectionIsSelected(fillingSectionText);
        objHomePage.checkFillingPosition(expectedPosition);
        objHomePage.clickSaucesBtn();
        objHomePage.checkIfSectionIsSelected(sauceSectionText);
        objHomePage.checkSaucesPosition(expectedPosition);
        objHomePage.clickBunsBtn();
        objHomePage.checkIfSectionIsSelected(bunsSectionText);
        objHomePage.checkBunsPosition(expectedPosition);

    }

    @After
    public void tearDown() {
        Functions.closeBrowser();
    }

}