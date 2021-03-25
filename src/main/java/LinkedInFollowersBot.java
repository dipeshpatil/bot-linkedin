import Configuration.Config;
import QueryBuilder.QueryBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.sk.PrettyTable;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class LinkedInFollowersBot extends Config {
    private static WebDriver driver = null;

    private static final By byEmailFieldXPath = By.xpath("/html/body/div/main/div[2]/div[1]/form/div[1]/input");
    private static final By byPasswordFieldXPath = By.xpath("/html/body/div/main/div[2]/div[1]/form/div[2]/input");
    private static final By bySigningButtonXPath = By.xpath("/html/body/div/main/div[2]/div[1]/form/div[3]/button");
    private static final By byMessageDropdownButtonXPath = By.xpath("//button[contains(@class,\"msg-overlay-bubble-header__control\")]/li-icon[contains(@type,\"chevron-down-icon\")]");
    private static final By byFollowersNameXPath = By.xpath(".//div/div/div[2]/div[1]/div/div[1]/span/div/span[1]/span/a/span/span[1]");
    private static final By byLinkedInMemberXPath = By.xpath(".//div/div/div[2]/div/div/div[1]/span/div/span/span/a");
    private static final By byFollowersBioXPath = By.xpath(".//div/div/div[2]/div[1]/div/div[2]/div[1]");
    private static final By byFollowersLocationXPath = By.xpath(".//div/div/div[2]/div[1]/div/div[2]/div[2]");
    private static final By byFollowersActionButtonXPath = By.xpath(".//button[contains(@class, \"artdeco-button\") and contains(@class, \"artdeco-button--2\") and contains(@class, \"artdeco-button--secondary\")]/span[contains(@class, \"artdeco-button__text\")]");

    private static final By byResultsCssSelector = By.cssSelector("li.reusable-search__result-container");
    private static final By bySearchResultsDivCssSelector = By.cssSelector("div.search-results-container>div");

    public static void main(String[] args) throws InterruptedException {
        System.setProperty(DRIVER_TYPE, Config.DRIVER_PATH);

        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--headless");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

        driver.navigate().to(Config.SITE_URL);

        logInToLinkedIn();
        disableMessagesDropdown();

        System.out.println("Enter Query: ");
        Scanner scanner = new Scanner(System.in);
        String query = scanner.nextLine();
        String URL = new QueryBuilder(query).generateUrl();
        driver.navigate().to(URL);

        System.out.println(waitForVisibilityOf(bySearchResultsDivCssSelector).getText());

        System.out.println("Enter Followers Count: ");
        int followersCount = Integer.parseInt(scanner.nextLine());
        int totalPages = followersCount / 10;

        PrettyTable table = new PrettyTable("ID", "Name", "Bio", "Location", "Button");
        displayUsers(URL, 1, totalPages, table);
        System.out.println(table);
        logOutFromLinkedIn();
    }

    private static void displayUsers(String URL, int currentPage, int totalPages, PrettyTable table) throws InterruptedException {
        driver.navigate().to(nextPage(URL, currentPage));
        Thread.sleep(4000);
        int count = 1;

        List<WebElement> elements = waitUntilAllElementsLocatedOf(byResultsCssSelector);

        for (WebElement node : elements) {
            if (isElementPresent(node, byFollowersNameXPath)) {
                WebElement name = node.findElement(byFollowersNameXPath);
                WebElement bio = node.findElement(byFollowersBioXPath);
                WebElement location = node.findElement(byFollowersLocationXPath);
                WebElement button = node.findElement(byFollowersActionButtonXPath);

                String nameText = name.getAttribute("textContent").strip();
                String bioText = bio.getAttribute("textContent").strip();
                String locationText = location.getAttribute("textContent").strip();
                String actionButtonText = button.getAttribute("textContent").strip();

                table.addRow((currentPage * 10 - 10 + count++) + "", nameText, bioText, locationText, actionButtonText);
            }
        }

        if (currentPage < totalPages)
            displayUsers(URL, ++currentPage, totalPages, table);
    }

    private static String nextPage(String URL, int pageNumber) {
        return URL + "&page=" + pageNumber;
    }

    private static void logInToLinkedIn() {
        WebElement emailField = driver.findElement(byEmailFieldXPath);
        WebElement passwordField = driver.findElement(byPasswordFieldXPath);

        emailField.sendKeys(Config.EMAIL);
        passwordField.sendKeys(Config.PASSWORD);
        waitForVisibilityOf(bySigningButtonXPath).click();
    }

    private static void logOutFromLinkedIn() {
        driver.navigate().to(LOGOUT_URL);
        driver.close();
    }

    private static void disableMessagesDropdown() {
        waitForVisibilityOf(byMessageDropdownButtonXPath).click();
    }

    private static WebElement waitForVisibilityOf(By elementId) {
        return (WebElement) (new WebDriverWait(driver, 10L)).until(ExpectedConditions.elementToBeClickable(elementId));
    }

    private static List<WebElement> waitUntilAllElementsLocatedOf(By elementId) {
        return (List<WebElement>) (new WebDriverWait(driver, 10L)).until(ExpectedConditions.presenceOfAllElementsLocatedBy(elementId));
    }

    private static boolean isElementPresent(By elementId) {
        return driver.findElements(elementId).size() > 0;
    }

    private static boolean isElementPresent(WebElement element, By elementId) {
        return element.findElements(elementId).size() > 0;
    }
}