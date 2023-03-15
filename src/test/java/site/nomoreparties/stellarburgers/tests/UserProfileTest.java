package site.nomoreparties.stellarburgers.tests;

import helpers.api.methods.CleanUpRequests;
import helpers.api.model.User;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import site.nomoreparties.stellarburgers.pom.HomePageObjects;
import site.nomoreparties.stellarburgers.pom.LoginPageObject;
import site.nomoreparties.stellarburgers.pom.ProfilePageObjects;

import java.util.concurrent.TimeUnit;

import static helpers.api.methods.PreconditionRequests.createNewUser;
import static site.nomoreparties.stellarburgers.constants.Browser.CHROME;
import static site.nomoreparties.stellarburgers.constants.Browser.YANDEX;
import static site.nomoreparties.stellarburgers.constants.Path.*;
import static site.nomoreparties.stellarburgers.constants.Url.STELLARBURGER_HOME_PAGE_URL;

@RunWith(Parameterized.class)
public class UserProfileTest {
    private final String browser;
    WebDriver driver;
    private String name = RandomStringUtils.random(6, true, true);
    private String email = RandomStringUtils.random(6, true, true) + "@mail.com";
    private String password = RandomStringUtils.random(7, true, true);

    public UserProfileTest(String browser) {
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
    public void createUserAsPrecondition() {
        User user = new User(email, password, name);
        createNewUser(user);
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
        driver.manage().window().maximize();
    }

    @Test
    @DisplayName("Проверить переход на страницу входа в аккаунт по клику на кнопку \"Личный Кабинет\" неавторизованным пользователем")
    public void testNavigateToProfilePage() {
        runBrowser();
        driver.get(STELLARBURGER_HOME_PAGE_URL);
        HomePageObjects objHomePage = new HomePageObjects(driver);
        LoginPageObject objLoginPage = new LoginPageObject(driver);
        objHomePage.clickProfileBtn();
        objLoginPage.checkIfLoginPageDisplayed();
    }

    @Test
    @DisplayName("Проверить переход на страницу личного кабинета по клику на кнопку \"Личный Кабинет\" авторизованным пользователем")
    public void testNavigateToProfilePageAsLoggedInUser() {
        runBrowser();
        driver.get(STELLARBURGER_HOME_PAGE_URL);
        HomePageObjects objHomePage = new HomePageObjects(driver);
        ProfilePageObjects objProfilePage = new ProfilePageObjects(driver);
        loginUser();
        objHomePage.waitUntilHomePageForLoggedInUserLoaded();
        objHomePage.clickProfileBtn();
        objProfilePage.checkIfProfilePageDisplayed();

    }

    @Test
    @DisplayName("Проверить переход со страницы личного кабинета на домашнюю страницу по клику на кнопку \"Конструктор\" авторизованным пользователем")
    public void testNavigateFromProfileToHomePageClickingConstructor() {
        runBrowser();
        driver.get(STELLARBURGER_HOME_PAGE_URL);
        HomePageObjects objHomePage = new HomePageObjects(driver);
        ProfilePageObjects objProfilePage = new ProfilePageObjects(driver);
        loginUser();
        objHomePage.waitUntilHomePageForLoggedInUserLoaded();
        objHomePage.clickProfileBtn();
        objProfilePage.waitUntilProfilePageLoaded();
        objHomePage.clickConstructorBtn();
        objHomePage.checkIfHomePageDisplayedForLoggedInUser();

    }

    @Test
    @DisplayName("Проверить переход со страницы личного кабинета на домашнюю страницу по клику на логотип \"Stellar Burgers\" авторизованным пользователем")
    public void testNavigateProfileToHomePageClickConstructor() {
        runBrowser();
        driver.get(STELLARBURGER_HOME_PAGE_URL);
        HomePageObjects objHomePage = new HomePageObjects(driver);
        ProfilePageObjects objProfilePage = new ProfilePageObjects(driver);
        loginUser();
        objHomePage.waitUntilHomePageForLoggedInUserLoaded();
        objHomePage.clickProfileBtn();
        objProfilePage.waitUntilProfilePageLoaded();
        objHomePage.clickLogoBtn();
        objHomePage.checkIfHomePageDisplayedForLoggedInUser();
    }

    @Step("Войти в систему")
    public void loginUser() {
        HomePageObjects objHomePage = new HomePageObjects(driver);
        LoginPageObject objLoginPage = new LoginPageObject(driver);
        objHomePage.waitUntilHomePageLoaded();
        objHomePage.clickLoginBtn();
        objLoginPage.waitUntilLoginPageLoaded();
        objLoginPage.fillLoginForm(email, password);
        objLoginPage.clickLoginBtn();
    }

    @Step("Закрыть браузер")
    public void closeBrowser() {

        driver.quit();
    }

    @After
    public void tearDown() {
        closeBrowser();
        User user = new User(password, email);
        CleanUpRequests.deleteUser(user);
    }
}
