package site.nomoreparties.stellarburgers.tests;

import helpers.api.methods.CleanUpRequests;
import helpers.api.model.User;
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
import site.nomoreparties.stellarburgers.pom.LoginPageObject;
import site.nomoreparties.stellarburgers.pom.RegisterPageObject;

import static site.nomoreparties.stellarburgers.constants.Browser.CHROME;
import static site.nomoreparties.stellarburgers.constants.Browser.YANDEX;
import static site.nomoreparties.stellarburgers.constants.Path.*;
import static site.nomoreparties.stellarburgers.constants.Url.STELLARBURGER_HOME_PAGE_URL;

@RunWith(Parameterized.class)
public class RegisterUserTest {
    private final String name;
    private final String email;
    private final String password;
    private final String registerResult;
    WebDriver driver;
    private String browser;


    public RegisterUserTest(String browser, String name, String email, String password, String registerResult) {
        this.browser = browser;
        this.name = name;
        this.email = email;
        this.password = password;
        this.registerResult = registerResult;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][]{
                {CHROME, "fluff", RandomStringUtils.random(6, true, true) + "@mail.com", "123456", "success"},
                {CHROME, "puff", RandomStringUtils.random(6, true, true) + "@mail.com", "1234567", "success"},
                {CHROME, "guff", RandomStringUtils.random(6, true, true) + "@mail.com", "12345", "failure"},
                {YANDEX, "fluff", RandomStringUtils.random(6, true, true) + "@mail.com", "123456", "success"},
                {YANDEX, "puff", RandomStringUtils.random(6, true, true) + "@mail.com", "1234567", "success"},
                {YANDEX, "guff", RandomStringUtils.random(6, true, true) + "@mail.com", "12345", "failure"},
        };
    }

    @Test
    @DisplayName("Зарегистрировать пользователя через \"Личный Кабинет\"")
    public void registerUserUsingProfileButton() {
        runBrowser();
        driver.get(STELLARBURGER_HOME_PAGE_URL);
        registerUserFromProfileButton();
    }

    @Test
    @DisplayName("Зарегистрировать пользователя через \"Войти в аккаунт\"")
    public void registerUserUsingLoginButton() {
        runBrowser();
        driver.get(STELLARBURGER_HOME_PAGE_URL);
        registerUserFromLoginBtn();

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
        driver.manage().window().maximize();
    }

    public void registerUserFromProfileButton() {
        HomePageObjects objHomePage = new HomePageObjects(driver);
        LoginPageObject objLoginPage = new LoginPageObject(driver);
        RegisterPageObject objRegisterPage = new RegisterPageObject(driver);
        objHomePage.waitUntilHomePageLoaded();
        objHomePage.clickProfileBtn();
        objLoginPage.waitUntilLoginPageLoaded();
        objLoginPage.clickRegisterLink();
        objRegisterPage.waitUntilRegisterPageLoaded();
        objRegisterPage.fillRegisterForm(name, email, password);
        objRegisterPage.clickRegisterButton();
        objRegisterPage.checkRegistrationResult(registerResult);
    }

    public void registerUserFromLoginBtn() {
        HomePageObjects objHomePage = new HomePageObjects(driver);
        LoginPageObject objLoginPage = new LoginPageObject(driver);
        RegisterPageObject objRegisterPage = new RegisterPageObject(driver);
        objHomePage.waitUntilHomePageLoaded();
        objHomePage.clickLoginBtn();
        objLoginPage.waitUntilLoginPageLoaded();
        objLoginPage.clickRegisterLink();
        objRegisterPage.waitUntilRegisterPageLoaded();
        objRegisterPage.fillRegisterForm(name, email, password);
        objRegisterPage.clickRegisterButton();
        objRegisterPage.checkRegistrationResult(registerResult);
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
