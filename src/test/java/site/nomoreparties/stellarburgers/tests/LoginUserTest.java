package site.nomoreparties.stellarburgers.tests;

import general.Functions;
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
import site.nomoreparties.stellarburgers.pom.ForgotPasswordPageObject;
import site.nomoreparties.stellarburgers.pom.HomePageObjects;
import site.nomoreparties.stellarburgers.pom.LoginPageObject;
import site.nomoreparties.stellarburgers.pom.RegisterPageObject;

import java.util.concurrent.TimeUnit;

import static helpers.api.methods.PreconditionRequests.createNewUser;
import static site.nomoreparties.stellarburgers.constants.Browser.CHROME;
import static site.nomoreparties.stellarburgers.constants.Browser.YANDEX;
import static site.nomoreparties.stellarburgers.constants.Path.*;
import static site.nomoreparties.stellarburgers.constants.Url.*;

@RunWith(Parameterized.class)
public class LoginUserTest {
    private final String browser;
    WebDriver driver;
    private String name = RandomStringUtils.random(6, true, true);
    private String email = RandomStringUtils.random(6, true, true) + "@mail.com";
    private String password = RandomStringUtils.random(7, true, true);

    public LoginUserTest(String browser) {
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

    @Test
    @DisplayName("Проверить вход по кнопке \"Войти в аккаунт\"")
    public void testLoginUsingLoginButton() {
        runBrowser();
        driver.get(STELLARBURGER_HOME_PAGE_URL);
        loginUsingLoginBtn();
    }

    @Test
    @DisplayName("Проверить вход через кнопку \"Личный кабинет\"")
    public void testLoginUsingProfileButton() {
        runBrowser();
        driver.get(STELLARBURGER_HOME_PAGE_URL);
        loginUsingProfileBtn();
    }

    @Test
    @DisplayName("Проверить вход через страницу регистрации")
    public void testLoginFromRegisterPage() {
        runBrowser();
        driver.get(STELLARBURGER_REGISTER_PAGE_URL);
        loginFromRegisterPage();
    }

    @Test
    @DisplayName("Проверить вход через страницу восстановления пароля")
    public void testLoginFromForgotPasswordPage() {
        runBrowser();
        driver.get(STELLARBURGER_FORGOT_PASSWORD_PAGE_URL);
        loginFromForgotPasswordPage();
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

    @Step("Войти по кнопке \"Войти в аккаунт\"")
    public void loginUsingLoginBtn() {
        HomePageObjects objHomePage = new HomePageObjects(driver);
        LoginPageObject objLoginPage = new LoginPageObject(driver);
        objHomePage.waitUntilHomePageLoaded();
        objHomePage.clickLoginBtn();
        objLoginPage.waitUntilLoginPageLoaded();
        objLoginPage.fillLoginForm(email, password);
        objLoginPage.clickLoginBtn();
        checkLogIn();
    }

    @Step("Войти через кнопку \"Личный кабинет\"")
    public void loginUsingProfileBtn() {
        HomePageObjects objHomePage = new HomePageObjects(driver);
        LoginPageObject objLoginPage = new LoginPageObject(driver);
        objHomePage.waitUntilHomePageLoaded();
        objHomePage.clickProfileBtn();
        objLoginPage.fillLoginForm(email, password);
        objLoginPage.clickLoginBtn();
        checkLogIn();
    }

    @Step("Войти через кнопку через страницу регистрации")
    public void loginFromRegisterPage() {
        LoginPageObject objLoginPage = new LoginPageObject(driver);
        RegisterPageObject objRegisterPage = new RegisterPageObject(driver);
        objRegisterPage.waitUntilRegisterPageLoaded();
        objRegisterPage.clickLoginButton();
        objLoginPage.fillLoginForm(email, password);
        objLoginPage.clickLoginBtn();
        checkLogIn();
    }

    @Step("Войти через кнопку через страницу восстановления пароля")
    public void loginFromForgotPasswordPage() {
        LoginPageObject objLoginPage = new LoginPageObject(driver);
        ForgotPasswordPageObject objForgotPasswordPage = new ForgotPasswordPageObject(driver);
        objForgotPasswordPage.waitUntilHomePageLoaded();
        objForgotPasswordPage.clickLoginButton();
        objLoginPage.fillLoginForm(email, password);
        objLoginPage.clickLoginBtn();
        checkLogIn();
    }

    @Step("Проверить, что пользователь был авторизован и перенаправлен на домашнюю страницу")
    public void checkLogIn() {
        HomePageObjects objHomePage = new HomePageObjects(driver);
        LoginPageObject objLoginPage = new LoginPageObject(driver);
        Functions.pauseInMilliSeconds(5000);
        objHomePage.waitUntilHomePageForLoggedInUserLoaded();
        objLoginPage.checkRedirect();
        objHomePage.clickProfileBtn();
        Functions.pauseInMilliSeconds(5000);
        objLoginPage.checkLoggedInUser(name, email);
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
