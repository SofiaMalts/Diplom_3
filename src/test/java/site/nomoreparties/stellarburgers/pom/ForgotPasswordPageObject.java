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

public class ForgotPasswordPageObject {
    private static WebDriver driver;
    private static By pageTitle = By.xpath(".//h2[text() = 'Восстановление пароля']");
    private static By resetBtn = By.xpath(".//button[text() = 'Восстановить']");
    private static By loginLink = By.xpath(".//a[text() = 'Войти']");
    private static By emailField = By.xpath(".//label[text() = 'Email']");

    public ForgotPasswordPageObject(WebDriver driver) {
        this.driver = driver;
    }

    public void waitUntilHomePageLoaded() {
        waitUntilElementsPresent();
        waitUntilElementsDisplayed();
    }

    public void waitUntilElementsDisplayed() {
        List<WebElement> listOfAllFields = new ArrayList<WebElement>();
        listOfAllFields.add(driver.findElement(pageTitle));
        listOfAllFields.add(driver.findElement(resetBtn));
        listOfAllFields.add(driver.findElement(loginLink));
        listOfAllFields.add(driver.findElement(emailField));
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOfAllElements(listOfAllFields));
    }

    public void waitUntilElementsPresent() {
        List<By> listOfAllLocators = new ArrayList<By>();
        listOfAllLocators.add(pageTitle);
        listOfAllLocators.add(resetBtn);
        listOfAllLocators.add(loginLink);
        listOfAllLocators.add(emailField);
        for (By locator : listOfAllLocators) {
            new WebDriverWait(driver, 10)
                    .until(ExpectedConditions.presenceOfElementLocated(locator));
        }
    }

    @Step("Кликнуть ссылку \"Войти\"")
    public void clickLoginButton() {
        WebElement button = driver.findElement(loginLink);
        Functions func = new Functions(driver);
        func.clickElement(button);
    }
}
