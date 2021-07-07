
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;

public class App {

    public static void main(String[] args) {

        ArrayList<String> names = new ArrayList<>();
        String fullName = "";
        for (String name : args) {
            names.add(name);
            fullName += name + " ";
        }

        System.setProperty("webdriver.chrome.driver", "chromedriver");
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, 30);
        try {
            // open the browser and navigate to the nba site and accept cookies
            driver.navigate().to("http://www.nba.com/stats/");
            String acceptCookies = "//*[@id=\"onetrust-accept-btn-handler\"]";
            click_xpath(acceptCookies, driver, wait);

            // navigate to the per player page
            click_xpath("/html/body/div[2]/div/div/nav/div[2]/button", driver, wait);
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/section[1]/ul/li[2]/a")));
            click_xpath("/html/body/div[2]/section[1]/ul/li[2]/a", driver, wait);

            // search for the player
            String playerSearchBar = "//*[@id=\"__next\"]/div[2]/div[3]/section/div/div[1]/div/input";
            use_searchbar(playerSearchBar, fullName, driver, wait);
            //wait.until(ExpectedConditions.elementToBeClickable(By.xpath(playerSearchBar)));
            //driver.findElement(By.xpath(playerSearchBar)).sendKeys(fullName);

        }
        finally {
            driver.close();
        }
    }

    public static void click_xpath (String element, WebDriver driver, WebDriverWait wait) {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(element)));
        driver.findElement(By.xpath(element)).click();
    }

    public static void use_searchbar (String element, String key, WebDriver driver, WebDriverWait wait) {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(element)));
        driver.findElement(By.xpath(element)).sendKeys(key);
    }
}
