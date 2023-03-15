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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ProfilePageObjects {
    private static WebDriver driver;
    private static By nameField = By.xpath(".//input[@class = 'text input__textfield text_type_main-default input__textfield-disabled' and @type = 'text' and @name = 'Name']");
    private static By loginField = By.xpath(".//input[@class = 'text input__textfield text_type_main-default input__textfield-disabled' and @type = 'text' and @name = 'name']");
    private static By passwordField = By.xpath(".//input[@class = 'text input__textfield text_type_main-default input__textfield-disabled' and @type = 'password' and @name = 'name']");
    private static By saveBtn = By.xpath(".//button[text() = 'Сохранить']");
    private static By cancelBtn = By.xpath(".//button[text() = 'Отмена']");
    private static By profileMenuItem = By.xpath(".//a[text() = 'Профиль']");
    private static By historyMenuItem = By.xpath(".//a[text() = 'История заказов']");
    private static By exitMenuItem = By.xpath(".//button[text() = 'Выход']");

    public ProfilePageObjects(WebDriver driver) {
        this.driver = driver;
    }

    public static void waitUntilProfilePageLoaded() {
        waitUntilElementsPresent();
        waitUntilElementsDisplayed();
    }

    public static void waitUntilElementsDisplayed() {
        List<WebElement> listOfAllFields = new ArrayList<WebElement>();
        listOfAllFields.add(driver.findElement(nameField));
        listOfAllFields.add(driver.findElement(loginField));
        listOfAllFields.add(driver.findElement(passwordField));
        listOfAllFields.add(driver.findElement(saveBtn));
        listOfAllFields.add(driver.findElement(profileMenuItem));
        listOfAllFields.add(driver.findElement(historyMenuItem));
        listOfAllFields.add(driver.findElement(exitMenuItem));
        listOfAllFields.add(driver.findElement(cancelBtn));
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOfAllElements(listOfAllFields));

    }

    public static void waitUntilElementsPresent() {
        List<By> listOfAllLocators = new ArrayList<By>();
        listOfAllLocators.add(nameField);
        listOfAllLocators.add(loginField);
        listOfAllLocators.add(passwordField);
        listOfAllLocators.add(saveBtn);
        listOfAllLocators.add(profileMenuItem);
        listOfAllLocators.add(historyMenuItem);
        listOfAllLocators.add(exitMenuItem);
        listOfAllLocators.add(cancelBtn);
        for (By locator : listOfAllLocators) {
            new WebDriverWait(driver, 10)
                    .until(ExpectedConditions.presenceOfElementLocated(locator));
        }
    }

    public static void checkUserName(String expectedName) {
        Functions func = new Functions(driver);
        assertTrue("Поле \"Имя\" не отображается на странице", func.isElementDisplayed(nameField));
        WebElement nameFieldElement = driver.findElement(nameField);
        String actualName = nameFieldElement.getAttribute("value");
        assertEquals("Имя в лично кабинете не соответствует ожидаемомму. Ожидалось: " + expectedName + "; Фактически: " + actualName + "", expectedName, actualName);
    }

    public static void checkUserLogin(String expectedLogin) {
        Functions func = new Functions(driver);
        assertTrue("Поле \"Логин\" не отображается на странице", func.isElementDisplayed(loginField));
        WebElement nameFieldElement = driver.findElement(loginField);
        String actualLogin = nameFieldElement.getAttribute("value");
        assertEquals("Логин в личном кабинете не соответствует ожидаемомму. Ожидалось: " + expectedLogin + "; Фактически: " + actualLogin + "", expectedLogin, actualLogin);

    }
@Step("Проверить имя и логин в личном кабинете соответствуют ожидаемым")
    public static void checkNameAndLoginValues(String expectedName, String expectedLogin) {
        checkUserName(expectedName);
        checkUserLogin(expectedLogin);
    }

    @Step("Нажать кнопку \"Выход\"")
    public static void clickLogoutBtn() {
        Functions func = new Functions(driver);
        WebElement button = driver.findElement(exitMenuItem);
        func.clickElement(button);
    }

    public static boolean isProfilePageDisplayed() {
        Functions func = new Functions(driver);
        if (func.isElementDisplayed(nameField) && func.isElementDisplayed(loginField) && func.isElementDisplayed(passwordField) && func.isElementDisplayed(saveBtn) && func.isElementDisplayed(profileMenuItem) && func.isElementDisplayed(historyMenuItem) && func.isElementDisplayed(exitMenuItem) && func.isElementDisplayed(cancelBtn)) {
            return true;
        } else return false;
    }

    @Step("Проверить, что страница личного кабинета отображается")
    public static void checkIfProfilePageDisplayed() {
        Functions func = new Functions(driver);
        assertTrue(isProfilePageDisplayed());
        if (isProfilePageDisplayed()) {
            assertTrue("Поле \"Имя\" не отображается на странице", func.isElementDisplayed(nameField));
            assertTrue("Поле \"Логин\" не отображается на странице", func.isElementDisplayed(loginField));
            assertTrue("Поле \"Пароль\" не отображается на странице", func.isElementDisplayed(passwordField));
            assertTrue("Кнопка \"Сохранить\" не отображается на странице", func.isElementDisplayed(saveBtn));
            assertTrue("Кнопка \"Отмена\" не отображается на странице", func.isElementDisplayed(cancelBtn));
            assertTrue("Пункт меню \"Профиль\" не отображается на странице", func.isElementDisplayed(profileMenuItem));
            assertTrue("Пункт меню \"История заказов\" не отображается на странице", func.isElementDisplayed(historyMenuItem));
            assertTrue("Пункт меню \"Выход\" не отображается на странице", func.isElementDisplayed(exitMenuItem));
        }
    }

}
