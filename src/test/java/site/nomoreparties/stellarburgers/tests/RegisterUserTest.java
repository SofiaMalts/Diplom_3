package site.nomoreparties.stellarburgers.tests;

import general.Functions;
import helpers.api.methods.CleanUpRequests;
import helpers.api.model.User;
import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import site.nomoreparties.stellarburgers.pom.HomePageObjects;
import site.nomoreparties.stellarburgers.pom.LoginPageObject;
import site.nomoreparties.stellarburgers.pom.RegisterPageObject;

import static site.nomoreparties.stellarburgers.constants.Browser.CHROME;
import static site.nomoreparties.stellarburgers.constants.Browser.YANDEX;
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

    @Before
    public void runBrowserOne() {
        driver = Functions.runBrowser(browser);
    }

    @Test
    @DisplayName("Зарегистрировать пользователя через \"Личный Кабинет\"")
    public void registerUserUsingProfileButton() {
        driver.get(STELLARBURGER_HOME_PAGE_URL);
        registerUserFromProfileButton();
    }

    @Test
    @DisplayName("Зарегистрировать пользователя через \"Войти в аккаунт\"")
    public void registerUserUsingLoginButton() {
        driver.get(STELLARBURGER_HOME_PAGE_URL);
        registerUserFromLoginBtn();
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

    @After
    public void tearDown() {
        Functions.closeBrowser();
        User user = new User(password, email);
        CleanUpRequests.deleteUser(user);
    }
}
