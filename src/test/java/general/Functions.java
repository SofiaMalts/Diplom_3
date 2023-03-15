package general;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

public class Functions {
    private static WebDriver driver;

    public Functions(WebDriver driver) {
        this.driver = driver;
    }


    public static void navigateToUrl(String url) {
        driver.get(url);
    }


    // Assert that button is enabled and displayed
    public static void checkIfButtonReady(By locator) {
        WebElement element = driver.findElement(locator);
        String buttonText = element.getText();
        assertTrue("Кнопка " + buttonText + " не найдена", elementExists(locator));
        assertTrue("Кнопка " + buttonText + " не отображается", isElementDisplayed(locator));
        assertTrue("Кнопка " + buttonText + " неактивна", isElementEnabled(locator));
    }

    // Assert that button is enabled and displayed
    public static void checkIfElementIsReady(By locator) {
        assertTrue("Элемент c тегом" + driver.findElement(locator).getTagName() + " и классом " + driver.findElement(locator).getAttribute("class") + " не найден", elementExists(locator));
        assertTrue("Элемент c тегом" + driver.findElement(locator).getTagName() + " и классом " + driver.findElement(locator).getAttribute("class") + " не отображается", isElementDisplayed(locator));
        assertTrue("Элемент c тегом" + driver.findElement(locator).getTagName() + " и классом " + driver.findElement(locator).getAttribute("class") + " неактивен", isElementEnabled(locator));
    }

    // Return boolean value depending on element presence
    public static boolean elementExists(By selector) {
        List<WebElement> element = driver.findElements(selector);
        if (element.size() == 0) {
            return false;
        }
        return true;
    }

    public static boolean isElementDisplayed(By locator) {
        WebElement element = driver.findElement(locator);
        if (element.isDisplayed()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isElementEnabled(By locator) {
        WebElement element = driver.findElement(locator);
        if (element.isEnabled()) {
            return true;
        } else {
            return false;
        }
    }

    public static void checkEnteredValueInField(WebElement field, String expEntry) {
        String fieldValue = field.getAttribute("value");
        assertThat("Не введены данные в поле.", expEntry, is(not("")));
        assertThat("Некорректные данные введены в поле. Фактическое значение: " + fieldValue + "; Ожидалось значение " + expEntry + "", expEntry, is(fieldValue));
    }

    public static void enterValueIntoField(By locator, String value) {
        WebElement field = driver.findElement(locator);
        checkIfElementIsReady(locator);
        clickElement(field);
        enterStringValue(field, value);
        checkEnteredValueInField(field, value);
    }

    public static void clickElement(WebElement element) {
        new WebDriverWait(driver, 20)
                .until(ExpectedConditions.elementToBeClickable(element));
        element.click();
    }

    public static void enterStringValue(WebElement element, String value) {
        element.sendKeys(value);
    }

    public static void pauseInMilliSeconds(int timeMs) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
