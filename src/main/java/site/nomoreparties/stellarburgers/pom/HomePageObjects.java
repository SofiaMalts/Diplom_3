package site.nomoreparties.stellarburgers.pom;

import general.Functions;
import io.qameta.allure.Step;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class HomePageObjects {
    private static WebDriver driver;

    private static By profileBtn = By.xpath(".//p[text() = 'Личный Кабинет']");
    private static By loginBtn = By.xpath(".//button[text() = 'Войти в аккаунт']");
    private static By createOrder = By.xpath(".//button[text() = 'Оформить заказ']");
    private static By buildYourBurger = By.xpath(".//h1[text() = 'Соберите бургер']");
    private static By constructorBtn = By.xpath(".//p[text() = 'Конструктор']");
    private static By burgerLogo = By.xpath(".//div[@class = 'AppHeader_header__logo__2D0X2']/a");
    //Constructor elements
    private static By bunsButtonParentDiv = By.xpath(".//span[text() = 'Булки']/parent::div");
    private static By saucesButtonParentDiv = By.xpath(".//span[text() = 'Соусы']/parent::div");
    private static By fillingButtonParentDiv = By.xpath(".//span[text() = 'Начинки']/parent::div");
    private static By bunsTitle = By.xpath(".//h2[text() = 'Булки']");
    private static By saucesTitle = By.xpath(".//h2[text() = 'Соусы']");
    private static By fillingTitle = By.xpath(".//h2[text() = 'Начинки']");
    private static String selectedConstructorDivClass = "tab_tab__1SPyG tab_tab_type_current__2BEPc pt-4 pr-10 pb-4 pl-10 noselect";
    private static String selectedSectionBoxShadow = "rgb(76, 76, 255) 0px -2px 0px 0px inset";

    public HomePageObjects(WebDriver driver) {
        this.driver = driver;
    }

    public static void waitUntilElementsDisplayedForLoggedInUser() {
        List<WebElement> listOfAllFields = new ArrayList<WebElement>();
        listOfAllFields.add(driver.findElement(profileBtn));
        listOfAllFields.add(driver.findElement(createOrder));
        listOfAllFields.add(driver.findElement(buildYourBurger));
        new WebDriverWait(driver, 10000)
                .until(ExpectedConditions.visibilityOfAllElements(listOfAllFields));

    }

    public static void waitUntilElementsPresentForLoggedInUser() {
        List<By> listOfAllLocators = new ArrayList<By>();
        listOfAllLocators.add(profileBtn);
        listOfAllLocators.add(createOrder);
        listOfAllLocators.add(buildYourBurger);
        for (By locator : listOfAllLocators) {
            new WebDriverWait(driver, 10000)
                    .until(ExpectedConditions.presenceOfElementLocated(locator));
        }
    }

    public static void waitUntilHomePageForLoggedInUserLoaded() {
        waitUntilElementsPresentForLoggedInUser();
        waitUntilElementsDisplayedForLoggedInUser();
    }

    @Step("Нажать кнопку \"Личный кабинет\"")
    public static void clickProfileBtn() {
        WebElement button = driver.findElement(profileBtn);
        Functions func = new Functions(driver);
        func.clickElement(button);
    }

    public static boolean isHomepageForLoggedInUserExists() {
        Functions func = new Functions(driver);
        waitUntilHomePageForLoggedInUserLoaded();
        if (func.elementExists(profileBtn) && func.elementExists(createOrder) && func.elementExists(buildYourBurger)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isHomepageForLoggedInUserDisplayed() {
        Functions func = new Functions(driver);
        waitUntilHomePageForLoggedInUserLoaded();
        if (func.isElementDisplayed(profileBtn) && func.isElementDisplayed(createOrder) && func.isElementDisplayed(buildYourBurger)) {
            return true;
        } else {
            return false;
        }
    }

    @Step("Проверить, что домашняя страница отображается")
    public static void checkIfHomePageDisplayedForLoggedInUser() {
        Functions func = new Functions(driver);
        Assert.assertTrue(isHomepageForLoggedInUserDisplayed());
        if (isHomepageForLoggedInUserDisplayed()) {
            Assert.assertTrue("Кнопка \"Личный Кабинет\" не отображается на странице", func.isElementDisplayed(profileBtn));
            Assert.assertTrue("Кнопка \"Оформить заказ\" не отображается на странице", func.isElementDisplayed(createOrder));
            Assert.assertTrue("Заголовок конструктора \"Соберите бургер\" не отображается на странице", func.isElementDisplayed(buildYourBurger));
        }
    }

    @Step("Нажать на раздел \"Булки\"")
    public static void clickBunsBtn() {
        WebElement button = driver.findElement(bunsButtonParentDiv);
        Functions func = new Functions(driver);
        func.clickElement(button);
    }

    @Step("Нажать на раздел \"Начинки\"")
    public static void clickFillingBtn() {
        WebElement button = driver.findElement(fillingButtonParentDiv);
        Functions func = new Functions(driver);
        func.clickElement(button);
    }

    public static String getSelectedSectionName() {
        String selectedSectionName = "";
        WebElement selected = driver.findElement(By.xpath(".//div[@class = '" + selectedConstructorDivClass + "']"));
        String actualBoxShadow = selected.getCssValue("box-shadow");
        String expectedBoxShadow = selectedSectionBoxShadow;
        if (actualBoxShadow.contains(expectedBoxShadow)) {
            selectedSectionName = selected.getText();
        }
        return selectedSectionName;
    }

    @Step("Проверить, что секция раздела конструктора выбрана")
    public static void checkIfSectionIsSelected(String sectionName) {
        String actualSelectedSectionName = getSelectedSectionName();
        String expectedSelectedSectionName = sectionName;
        int counter = 0;
        int maxWaitCounter = 100;
        if (!actualSelectedSectionName.equals(expectedSelectedSectionName)) {
            while ((!actualSelectedSectionName.equals(expectedSelectedSectionName)) && counter < maxWaitCounter) {
                actualSelectedSectionName = getSelectedSectionName();
                counter++;
            }
            if (counter == maxWaitCounter) {
                assertThat("Ожидаемая секция раздела конструктора не выбрана", actualSelectedSectionName, is(expectedSelectedSectionName));
            }
        }
    }

    public void waitUntilHomePageLoaded() {
        waitUntilElementsPresent();
        waitUntilElementsDisplayed();
    }

    public void waitUntilElementsDisplayed() {
        List<WebElement> listOfAllFields = new ArrayList<WebElement>();
        listOfAllFields.add(driver.findElement(profileBtn));
        listOfAllFields.add(driver.findElement(loginBtn));
        listOfAllFields.add(driver.findElement(buildYourBurger));
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.visibilityOfAllElements(listOfAllFields));

    }

    public void waitUntilElementsPresent() {
        List<By> listOfAllLocators = new ArrayList<By>();
        listOfAllLocators.add(profileBtn);
        listOfAllLocators.add(loginBtn);
        listOfAllLocators.add(buildYourBurger);
        for (By locator : listOfAllLocators) {
            new WebDriverWait(driver, 10)
                    .until(ExpectedConditions.presenceOfElementLocated(locator));
        }
    }

    @Step("Нажать кнопку \"Войти в аккаунт\"")
    public void clickLoginBtn() {
        WebElement button = driver.findElement(loginBtn);
        Functions func = new Functions(driver);
        func.clickElement(button);
    }

    @Step("Нажать кнопку \"Конструктор\"")
    public void clickConstructorBtn() {
        WebElement button = driver.findElement(constructorBtn);
        Functions func = new Functions(driver);
        func.clickElement(button);
    }

    @Step("Нажать на логотип \"Stellar Burger\"")
    public void clickLogoBtn() {
        WebElement button = driver.findElement(burgerLogo);
        Functions func = new Functions(driver);
        func.clickElement(button);

    }

    @Step("Нажать на раздел \"Соусы\"")
    public void clickSaucesBtn() {
        WebElement button = driver.findElement(saucesButtonParentDiv);
        WebElement title = driver.findElement(saucesTitle);
        Functions func = new Functions(driver);
        func.clickElement(button);
    }

    public int getElementYPosition(By locator) {
        WebElement element = driver.findElement(locator);
        Point location = element.getLocation();
        int yLocation = location.getY();
        return yLocation;
    }

    @Step("Проверить, что раздел \"Булки\" находится на ожидаемом месте")
    public void checkBunsPosition(int expectedPosition) {
        int actualPosition = getElementYPosition(bunsTitle);
        assertThat(actualPosition, is(expectedPosition));
    }

    @Step("Проверить, что раздел \"Соусы\" находится на ожидаемом месте")
    public void checkSaucesPosition(int expectedPosition) {
        int actualPosition = getElementYPosition(saucesTitle);
        assertThat(actualPosition, is(expectedPosition));
    }

    @Step("Проверить, что раздел \"Начинки\" находится на ожидаемом месте")
    public void checkFillingPosition(int expectedPosition) {
        int actualPosition = getElementYPosition(fillingTitle);
        assertThat(actualPosition, is(expectedPosition));
    }

}
