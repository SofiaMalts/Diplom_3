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
import site.nomoreparties.stellarburgers.pom.HomePageObjects;
import site.nomoreparties.stellarburgers.pom.LoginPageObject;
import site.nomoreparties.stellarburgers.pom.ProfilePageObjects;

import static helpers.api.methods.PreconditionRequests.createNewUser;
import static site.nomoreparties.stellarburgers.constants.Browser.CHROME;
import static site.nomoreparties.stellarburgers.constants.Browser.YANDEX;
import static site.nomoreparties.stellarburgers.constants.Url.STELLARBURGER_HOME_PAGE_URL;

@RunWith(Parameterized.class)
public class LogoutTest {

    private final String browser;
    WebDriver driver;
    private String name = RandomStringUtils.random(6, true, true);
    private String email = RandomStringUtils.random(6, true, true) + "@mail.com";
    private String password = RandomStringUtils.random(7, true, true);

    public LogoutTest(String browser) {
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
    public void runPrecondition() {
        User user = new User(email, password, name);
        createNewUser(user);
        driver = Functions.runBrowser(browser);
    }

    @Test
    @DisplayName("Проверить выход из системы")
    public void testLogoutUser() {
        driver.get(STELLARBURGER_HOME_PAGE_URL);
        HomePageObjects objHomePage = new HomePageObjects(driver);
        ProfilePageObjects objProfilePage = new ProfilePageObjects(driver);
        LoginPageObject objLoginPage = new LoginPageObject(driver);
        loginUser();
        objHomePage.waitUntilHomePageForLoggedInUserLoaded();
        objHomePage.clickProfileBtn();
        objProfilePage.waitUntilProfilePageLoaded();
        objProfilePage.clickLogoutBtn();
        objLoginPage.checkIfLoginPageDisplayed();
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

    @After
    public void tearDown() {
        Functions.closeBrowser();
        User user = new User(password, email);
        CleanUpRequests.deleteUser(user);
    }
}
