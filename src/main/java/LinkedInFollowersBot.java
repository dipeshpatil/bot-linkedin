import Configuration.Config;
import DataModel.People;
import Helpers.LinkedInHelper;
import Helpers.SeleniumHelper;
import QueryBuilder.QueryBuilder;
import Writer.ExcelWriter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.sk.PrettyTable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class LinkedInFollowersBot extends Config {
    private static final boolean HIDE_BROWSER = false;
    private static final boolean SEND_CONNECTION_REQUEST = false;

    private static WebDriver driver = null;

    private static String[] PEOPLE_COLUMNS = {"ID", "Name", "Bio", "Location"};
    private static List<People> peopleList;

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

    public static void main(String[] args) throws InterruptedException, IOException {
        System.setProperty(DRIVER_TYPE, Config.DRIVER_PATH);

        ChromeOptions options = new ChromeOptions();
        if (HIDE_BROWSER)
            options.addArguments("--headless");

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
        QueryBuilder builder = new QueryBuilder(query);
        String URL = builder.generateUrl();
        String sheetName = builder.getParamsSlug().replace(" ", "_");

        sHelper.navigateTo(URL);

        System.out.println(sHelper.waitForVisibilityOf(bySearchResultsDivCssSelector).getText());
        peopleList = new ArrayList<>();

        System.out.println("Enter Followers Count: ");
        int followersCount = Integer.parseInt(scanner.nextLine());

        PrettyTable table = new PrettyTable("ID", "Name", "Bio", "Location", "Status");

        displayUsers(URL, 1, 1, followersCount, table, peopleList);
        System.out.println(table);
        lHelper.logOutFromLinkedIn();

        ExcelWriter writer = new ExcelWriter(PEOPLE_COLUMNS, peopleList, sheetName);
        writer.generateExcelSheet();
    }

    private static void displayUsers(String URL, int currentPage, int currentFollower, int totalFollowers, PrettyTable table, List<People> peopleList) {
        sHelper.navigateTo(nextPage(URL, currentPage));
        sHelper.waitFor(3);

        List<WebElement> elements = driver.findElements(byResultsCssSelector);
        if (elements.size() > 0) {
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

                    if (!actionButtonText.equals(MESSAGE) && totalFollowers > 0) {
                        table.addRow(currentFollower + "", nameText, trimString(bioText, 50), locationText, getButtonStatus(actionButtonText));
                        peopleList.add(new People(currentFollower + "", nameText, bioText, locationText));
                        handleActionButton(button, actionButtonText, SEND_CONNECTION_REQUEST);
                    }

                    currentFollower++;
                    totalFollowers--;
                }
            }
        } else
            return;

        if (totalFollowers > 0)
            displayUsers(URL, ++currentPage, currentFollower, totalFollowers, table, peopleList);
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