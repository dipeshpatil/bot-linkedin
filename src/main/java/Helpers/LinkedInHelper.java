package Helpers;

import Configuration.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LinkedInHelper extends Config {
    private final WebDriver driver;
    private final SeleniumHelper sHelper;

    private static final By byEmailFieldXPath = By.xpath("/html/body/div/main/div[2]/div[1]/form/div[1]/input");
    private static final By byPasswordFieldXPath = By.xpath("/html/body/div/main/div[2]/div[1]/form/div[2]/input");
    private static final By bySigningButtonXPath = By.xpath("/html/body/div/main/div[2]/div[1]/form/div[3]/button");
    private static final By byMessageDropdownButtonXPath = By.xpath("//button[contains(@class,\"msg-overlay-bubble-header__control\")]/li-icon[contains(@type,\"chevron-down-icon\")]");

    public LinkedInHelper(WebDriver driver) {
        this.driver = driver;
        this.sHelper = new SeleniumHelper(this.driver);
    }

    public void logInToLinkedIn() {
        WebElement emailField = driver.findElement(byEmailFieldXPath);
        WebElement passwordField = driver.findElement(byPasswordFieldXPath);

        emailField.sendKeys(Config.EMAIL);
        passwordField.sendKeys(Config.PASSWORD);
        sHelper.waitForVisibilityOf(bySigningButtonXPath).click();
    }

    public void logOutFromLinkedIn() {
        sHelper.navigateTo(LOGOUT_URL);
        driver.close();
    }

    public void disableMessagesDropdown() {
        sHelper.waitForVisibilityOf(byMessageDropdownButtonXPath).click();
    }
}
