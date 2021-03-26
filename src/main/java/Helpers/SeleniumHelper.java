package Helpers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class SeleniumHelper {
    private final WebDriver driver;

    public SeleniumHelper(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement waitForVisibilityOf(By elementId) {
        return new WebDriverWait(driver, 10L).until(ExpectedConditions.elementToBeClickable(elementId));
    }

    public List<WebElement> waitUntilAllElementsLocatedOf(By elementId) {
        return new WebDriverWait(driver, 10L).until(ExpectedConditions.presenceOfAllElementsLocatedBy(elementId));
    }

    public boolean isElementPresent(By elementId) {
        return driver.findElements(elementId).size() > 0;
    }

    public boolean isElementPresent(WebElement element, By elementId) {
        return element.findElements(elementId).size() > 0;
    }

    public void navigateTo(String url) {
        driver.navigate().to(url);
    }

    public synchronized void waitFor(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException ignored) {
        }
    }
}