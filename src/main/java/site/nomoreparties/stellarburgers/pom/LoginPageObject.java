package site.nomoreparties.stellarburgers.pom;

import general.Functions;
import io.qameta.allure.Step;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class LoginPageObject {
    private static WebDriver driver;
    private static By loginFormTitle = By.xpath(".//h2[text() = 'Вход']");
    private static By emailInput = By.xpath(".//input[@class = 'text input__textfield text_type_main-default' and @type = 'text' and @name = 'name']");
    private static By passwordInput = By.xpath(".//input[@class = 'text input__textfield text_type_main-default' and @type = 'password' and @name = 'Пароль']");
    private static By enterBtn = By.xpath(".//button[text() = 'Войти']");
    private static By registerLink = By.xpath(".//a[text() = 'Зарегистрироваться']");

    public LoginPageObject(WebDriver driver) {
        this.driver = driver;
    }

    public static void waitUntilElementsDisplayed() {
        List<WebElement> listOfAllFields = new ArrayList<WebElement>();
        listOfAllFields.add(driver.findElement(loginFormTitle));
        listOfAllFields.add(driver.findElement(emailInput));
        listOfAllFields.add(driver.findElement(passwordInput));
        listOfAllFields.add(driver.findElement(enterBtn));
        listOfAllFields.add(driver.findElement(registerLink));
        new WebDriverWait(driver, 10000)
                .until(ExpectedConditions.visibilityOfAllElements(listOfAllFields));

    }

    public static void waitUntilElementsPresent() {
        List<By> listOfAllLocators = new ArrayList<By>();
        listOfAllLocators.add(loginFormTitle);
        listOfAllLocators.add(emailInput);
        listOfAllLocators.add(passwordInput);
        listOfAllLocators.add(enterBtn);
        listOfAllLocators.add(registerLink);
        for (By locator : listOfAllLocators) {
            new WebDriverWait(driver, 10000)
                    .until(ExpectedConditions.presenceOfElementLocated(locator));
        }
    }

    public static void waitUntilLoginPageLoaded() {
        waitUntilElementsPresent();
        waitUntilElementsDisplayed();
    }

    public static boolean isLoginFormDisplayed() {
        Functions func = new Functions(driver);
        if (func.elementExists(loginFormTitle) && func.elementExists(enterBtn) && func.elementExists(registerLink) && func.elementExists(emailInput) && func.elementExists(passwordInput)) {
            return true;
        } else return false;
    }

    public static void fillEmailField(String email) {
        Functions func = new Functions(driver);
        func.enterValueIntoField(emailInput, email);
    }

    public static void fillPasswordField(String password) {
        Functions func = new Functions(driver);
        func.enterValueIntoField(passwordInput, password);
    }

    @Step("Нажать кнопку \"Войти\"")
    public static void clickLoginBtn() {
        Functions func = new Functions(driver);
        WebElement button = driver.findElement(enterBtn);
        func.clickElement(button);
    }

    @Step("Заполнить форму для входа")
    public static void fillLoginForm(String email, String password) {
        waitUntilLoginPageLoaded();
        fillEmailField(email);
        fillPasswordField(password);
    }

    @Step("Проверить, что пользователь был перенаправлен на домашнюю страницу")
    public static void checkRedirect() {
        HomePageObjects objHomePage = new HomePageObjects(driver);
        Assert.assertTrue("Пользователь не был перенаправлен на домашнюю страницу", objHomePage.isHomepageForLoggedInUserExists());
    }

    @Step("Проверить имя и логин авторизованного пользователя")
    public static void checkLoggedInUser(String expectedName, String expectedLogin) {
        ProfilePageObjects objProfilePage = new ProfilePageObjects(driver);
        HomePageObjects objHomePage = new HomePageObjects(driver);
        objHomePage.clickProfileBtn();
        objProfilePage.waitUntilProfilePageLoaded();
        objProfilePage.checkNameAndLoginValues(expectedName, expectedLogin.toLowerCase());
    }

    @Step("Проверить, что отображается страница входа в аккаунт")
    public static void checkIfLoginPageDisplayed() {
        Assert.assertTrue("Форма для логина не отображается", isLoginFormDisplayed());
    }

    @Step("Кликнуть ссылку \"Зарегистрироваться\"")
    public void clickRegisterLink() {
        WebElement link = driver.findElement(registerLink);
        Functions func = new Functions(driver);
        func.clickElement(link);
    }
}
