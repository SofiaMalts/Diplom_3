package general;

import io.qameta.allure.Step;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import site.nomoreparties.stellarburgers.constants.Browser;
import site.nomoreparties.stellarburgers.constants.Path;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

public class Functions {
    private static WebDriver driver;

    public Functions(WebDriver driver) {
        this.driver = driver;
    }

    public static void checkIfElementIsReady(By locator) {
        Assert.assertTrue("Элемент c тегом" + driver.findElement(locator).getTagName() + " и классом " + driver.findElement(locator).getAttribute("class") + " не найден", elementExists(locator));
        Assert.assertTrue("Элемент c тегом" + driver.findElement(locator).getTagName() + " и классом " + driver.findElement(locator).getAttribute("class") + " не отображается", isElementDisplayed(locator));
        Assert.assertTrue("Элемент c тегом" + driver.findElement(locator).getTagName() + " и классом " + driver.findElement(locator).getAttribute("class") + " неактивен", isElementEnabled(locator));
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
        new WebDriverWait(driver, 20000)
                .until(ExpectedConditions.elementToBeClickable(element));
        element.click();
    }

    public static void enterStringValue(WebElement element, String value) {
        element.sendKeys(value);
    }

    @Step("Запустить браузер")
    public static WebDriver runBrowser(String browser) {
        // WebDriver driver = null;
        switch (browser) {
            case Browser.CHROME:
                System.setProperty("webdriver.chrome.driver", Path.CHROME_DRIVER_PATH);
                driver = new ChromeDriver();
                break;
            case Browser.YANDEX:
                System.setProperty("webdriver.chrome.driver", Path.CHROME_DRIVER_FOR_YANDEX_PATH);
                ChromeOptions options = new ChromeOptions();
                options.setBinary(Path.YANDEX_BROWSER_PATH);
                driver = new ChromeDriver(options);
                break;
        }
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10000,
                TimeUnit.MILLISECONDS);
        driver.manage().timeouts().pageLoadTimeout(20000,
                TimeUnit.MILLISECONDS);
        return driver;
    }

    @Step("Закрыть браузер")
    public static void closeBrowser() {
        driver.quit();
    }


}
