package site.nomoreparties.stellarburgers.pom;

import general.Functions;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RegisterPageObject {
    private static WebDriver driver;
    private static By registerFormTitle = By.xpath(".//h2[text() = 'Регистрация']");
    private static By nameInput = By.xpath(".//fieldset[@class = 'Auth_fieldset__1QzWN mb-6'][1]/div[@class = 'input__container']/div[@class = 'input pr-6 pl-6 input_type_text input_size_default']/input[@class = 'text input__textfield text_type_main-default' and @type = 'text' and @name = 'name']");
    private static By emailInput = By.xpath(".//fieldset[@class = 'Auth_fieldset__1QzWN mb-6'][2]/div[@class = 'input__container']/div[@class = 'input pr-6 pl-6 input_type_text input_size_default']/input[@class = 'text input__textfield text_type_main-default' and @type = 'text' and @name = 'name']");
    private static By passwordInput = By.xpath(".//input[@class = 'text input__textfield text_type_main-default' and @type = 'password' and @name = 'Пароль']");
    private static By submitBtn = By.xpath(".//button[text() = 'Зарегистрироваться']");
    private static By loginLink = By.xpath(".//a[text() = 'Войти']");
    private By errorPasswordField = By.xpath(".//fieldset[@class = 'Auth_fieldset__1QzWN mb-6'][3]/div[@class = 'input__container']/div[@class = 'input pr-6 pl-6 input_type_password input_size_default input_status_error']");
    private By errorPasswordTxt = By.xpath(".//p [@class = 'input__error text_type_main-default' and text() = 'Некорректный пароль']");

    public RegisterPageObject(WebDriver driver) {
        this.driver = driver;
    }

    public static void waitUntilElementsDisplayed() {
        List<WebElement> listOfAllFields = new ArrayList<WebElement>();
        listOfAllFields.add(driver.findElement(registerFormTitle));
        listOfAllFields.add(driver.findElement(nameInput));
        listOfAllFields.add(driver.findElement(emailInput));
        listOfAllFields.add(driver.findElement(passwordInput));
        listOfAllFields.add(driver.findElement(submitBtn));
        listOfAllFields.add(driver.findElement(loginLink));
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOfAllElements(listOfAllFields));

    }

    public static void waitUntilElementsPresent() {
        List<By> listOfAllLocators = new ArrayList<By>();
        listOfAllLocators.add(registerFormTitle);
        listOfAllLocators.add(nameInput);
        listOfAllLocators.add(passwordInput);
        listOfAllLocators.add(emailInput);
        listOfAllLocators.add(submitBtn);
        listOfAllLocators.add(loginLink);
        for (By locator : listOfAllLocators) {
            new WebDriverWait(driver, 10)
                    .until(ExpectedConditions.presenceOfElementLocated(locator));
        }
    }

    public static boolean isRegisterFormDisplayed() {
        Functions func = new Functions(driver);
        if (func.elementExists(registerFormTitle) && func.elementExists(submitBtn)) {
            return true;
        } else return false;
    }

    public void waitUntilRegisterPageLoaded() {
        waitUntilElementsPresent();
        waitUntilElementsDisplayed();
    }

    public void fillNameField(String name) {
        Functions func = new Functions(driver);
        func.enterValueIntoField(nameInput, name);
    }

    public void fillEmailField(String email) {
        Functions func = new Functions(driver);
        func.enterValueIntoField(emailInput, email);
    }

    public void fillPasswordField(String password) {
        Functions func = new Functions(driver);
        func.enterValueIntoField(passwordInput, password);
    }

    @Step("Нажать кнопку \"Зарегистрироваться\"")
    public void clickRegisterButton() {
        WebElement button = driver.findElement(submitBtn);
        Functions func = new Functions(driver);
        func.clickElement(button);
    }

    @Step("Кликнуть ссылку \"Войти\"")
    public void clickLoginButton() {
        WebElement button = driver.findElement(loginLink);
        Functions func = new Functions(driver);
        func.clickElement(button);
    }

    @Step("Заполнить форму для регистрации")
    public void fillRegisterForm(String name, String email, String password) {
        fillNameField(name);
        fillEmailField(email);
        fillPasswordField(password);
    }

    public boolean isRegistrationFailed() {
        Functions func = new Functions(driver);
        if (func.elementExists(errorPasswordField) && func.elementExists(errorPasswordTxt)) {
            return true;
        } else {
            return false;
        }
    }
@Step("Проверить, что при корректной регистрации пользователь направляется на страницу входа в аккаунт, при некорректной регистрации система показывает ошибку и оставляет пользователя на странице регистрации")
    public void checkRegistrationResult(String expectedResult) {
        LoginPageObject objLoginPage = new LoginPageObject(driver);
        Functions func = new Functions(driver);
        switch (expectedResult) {
            case "success":
                assertFalse("Система не позволяет войти", isRegistrationFailed());
                objLoginPage.waitUntilLoginPageLoaded();
                assertTrue("Пользователь не был перенаправлен на страницу входа", objLoginPage.isLoginFormDisplayed());
                break;
            case "failure":
                assertTrue("Форма для регистрации не отображается", isRegisterFormDisplayed());
                if (!isRegistrationFailed()) {
                    assertTrue("Поле \"Пароль\" не подсвечено красным", func.isElementDisplayed(errorPasswordField));
                    assertTrue("Не отображается сообщение об ошибке", func.isElementDisplayed(errorPasswordTxt));
                }
                break;
        }
    }
}
