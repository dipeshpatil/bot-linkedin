import Configuration.Config;
import Helpers.LinkedInHelper;
import Helpers.SeleniumHelper;
import QueryBuilder.QueryBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.sk.PrettyTable;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class LinkedInFollowersBot extends Config {
    private static WebDriver driver = null;

    private static final By byFollowersNameXPath = By.xpath(".//div/div/div[2]/div[1]/div/div[1]/span/div/span[1]/span/a/span/span[1]");
    private static final By byLinkedInMemberXPath = By.xpath(".//div/div/div[2]/div/div/div[1]/span/div/span/span/a");
    private static final By byFollowersBioXPath = By.xpath(".//div/div/div[2]/div[1]/div/div[2]/div[1]");
    private static final By byFollowersLocationXPath = By.xpath(".//div/div/div[2]/div[1]/div/div[2]/div[2]");
    private static final By byFollowersActionButtonXPath = By.xpath(".//button[contains(@class, \"artdeco-button\") and contains(@class, \"artdeco-button--2\") and contains(@class, \"artdeco-button--secondary\")]/span[contains(@class, \"artdeco-button__text\")]");
    private static final By bySendButtonInModalXPath = By.xpath("//button[contains(@aria-label, \"Send now\")]");

    private static final By byResultsCssSelector = By.cssSelector("li.reusable-search__result-container");
    private static final By bySearchResultsDivCssSelector = By.cssSelector("div.search-results-container>div");

    private static final String CONNECT = "Connect";
    private static final String FOLLOW = "Follow";
    private static final String PENDING = "Pending";
    private static final String FOLLOWING = "Following";
    private static final String MESSAGE = "Message";

    private static final String SENT = "Sent";
    private static final String FOLLOWED = "Followed";

    private static LinkedInHelper lHelper;
    private static SeleniumHelper sHelper;

    public static void main(String[] args) throws InterruptedException {
        System.setProperty(DRIVER_TYPE, Config.DRIVER_PATH);

        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--headless");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

        driver.navigate().to(Config.SITE_URL);

        lHelper = new LinkedInHelper(driver);
        sHelper = new SeleniumHelper(driver);

        lHelper.logInToLinkedIn();
        lHelper.disableMessagesDropdown();

        System.out.println("Enter Query: ");
        Scanner scanner = new Scanner(System.in);
        String query = scanner.nextLine();
        String URL = new QueryBuilder(query).generateUrl();

        sHelper.navigateTo(URL);

        System.out.println(sHelper.waitForVisibilityOf(bySearchResultsDivCssSelector).getText());

        System.out.println("Enter Followers Count: ");
        int followersCount = Integer.parseInt(scanner.nextLine());
        int totalPages = followersCount / 10;

        PrettyTable table = new PrettyTable("ID", "Name", "Bio", "Location", "Status");
        int count = 1;
        displayUsers(URL, 1, totalPages, table, count);
        System.out.println(table);
        lHelper.logOutFromLinkedIn();
    }

    private static void displayUsers(String URL, int currentPage, int totalPages, PrettyTable table, int currentCount) {
        sHelper.navigateTo(nextPage(URL, currentPage));
        sHelper.waitFor(3);

        int count = 1;
        List<WebElement> elements = sHelper.waitUntilAllElementsLocatedOf(byResultsCssSelector);

        for (WebElement node : elements) {
            if (sHelper.isElementPresent(node, byFollowersNameXPath)) {
                WebElement name = node.findElement(byFollowersNameXPath);
                WebElement bio = node.findElement(byFollowersBioXPath);
                WebElement location = node.findElement(byFollowersLocationXPath);
                WebElement button = node.findElement(byFollowersActionButtonXPath);

                String nameText = name.getAttribute("textContent").strip();
                String bioText = bio.getAttribute("textContent").strip();
                String locationText = location.getAttribute("textContent").strip();
                String actionButtonText = button.getAttribute("textContent").strip();

                if (!actionButtonText.equals(MESSAGE))
                    table.addRow(currentCount++ + "", nameText, trimString(bioText, 50), locationText, getButtonStatus(actionButtonText));

                handleActionButton(button, actionButtonText, false);
            }
        }

        if (currentPage < totalPages)
            displayUsers(URL, ++currentPage, totalPages, table, currentCount);
    }

    private static String nextPage(String URL, int pageNumber) {
        return URL + "&page=" + pageNumber;
    }

    private static void handleActionButton(WebElement buttonElement, String buttonText, boolean confirm) {
        if (confirm)
            switch (buttonText) {
                case CONNECT:
                    sendConnectionRequest(buttonElement);
                    break;
                case FOLLOW:
                    followTheUser(buttonElement);
                    break;
                default:
                    break;
            }
    }

    private static String getButtonStatus(String buttonText) {
        switch (buttonText) {
            case CONNECT:
                return SENT;
            case FOLLOW:
                return FOLLOWED;
        }
        return buttonText;
    }

    private static void followTheUser(WebElement followButton) {
        if (followButton.getAttribute("textContent").strip().equals(FOLLOW))
            followButton.click();
    }

    private static void sendConnectionRequest(WebElement connectButton) {
        if (connectButton.getAttribute("textContent").strip().equals(CONNECT)) {
            connectButton.click();
            sHelper.waitForVisibilityOf(bySendButtonInModalXPath).click();
        }
    }

    private static String trimString(String text, int size) {
        if (text.length() > size) return text.substring(0, size - 3) + "...";
        return text;
    }
}