import BotConfig.Config;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

import static Tools.LinkedInConstants.*;

public class LinkedInFollowersBot extends Config {
    private static WebDriver driver = null;

    public static void main(String[] args) throws InterruptedException {
        System.setProperty(DRIVER_TYPE, Config.DRIVER_PATH);

        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        driver.manage().window().maximize();

        driver.navigate().to(Config.SITE_URL);

        logInToLinkedIn();
        Thread.sleep(5000);
        logOutFromLinkedIn();
    }

    private static void logInToLinkedIn() {
        WebElement emailField = driver.findElement(byEmailFieldXPath);
        WebElement passwordField = driver.findElement(byPasswordFieldXPath);
        WebElement signingButton = driver.findElement(bySigningButtonXPath);

        emailField.sendKeys(Config.EMAIL);
        passwordField.sendKeys(Config.PASSWORD);
        signingButton.click();
    }

    private static void logOutFromLinkedIn() {
        driver.navigate().to(LOGOUT_URL);
    }
}
