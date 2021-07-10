
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class App {

    public static void main(String[] args) {

        // save the player name
        ArrayList<String> names = new ArrayList<>();
        String fullName = "";
        for (String name : args) {
            fullName += name + " ";
        }

        System.setProperty("webdriver.chrome.driver", "chromedriver");
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, 5);
        ChromeOptions options = new ChromeOptions();
        //options.addArguments("--disable-dev-shm-usage");

        try {
            // open the browser and navigate to the nba site and accept cookies
            driver.navigate().to("http://www.nba.com/stats/");
            driver.manage().window().maximize();
            String acceptCookies = "//*[@id=\"onetrust-accept-btn-handler\"]";
            click_xpath(acceptCookies, driver, wait);
            wait(1500);

            // open per-player page
            String URL = driver.getCurrentUrl();
            open_perplayer(driver, wait);

            // search for the player
            String playerSearchBar = "//*[@id=\"__next\"]/div[2]/div[3]/section/div/div[1]/div/input";
            use_searchbar(playerSearchBar, fullName, driver, wait);
            wait(1000);

            // get the wanted player's page
            String player = "//*[@id=\"__next\"]/div[2]/div[3]/section/div/div[2]/div[2]/div/div/div/table/tbody/tr[1]/td[1]/a";
            click_xpath(player, driver, wait);
            wait(1000);

            // stats
            click_xpath("//*[@id=\"__next\"]/div[2]/div[3]/div/div[1]/div/ul/li[2]/a", driver, wait);
            wait(1000);

            // save score objects into an Arraylist
            List<Score> scores = new ArrayList<>();

            List<WebElement> rows;
            rows = driver.findElements(By.xpath("/html/body/main/div/div/div/div[4]/div/div/div/div/nba-stat-table[1]/div[2]/div[1]/table/tbody/tr"));
            //System.out.println("number of rows found: " + rows.size());

            for (int i = 0; i < rows.size(); i++) {
                WebElement date = driver.findElement(By.xpath("/html/body/main/div/div/div/div[4]/div/div/div/div/nba-stat-table[1]/div[2]/div[1]/table/tbody/tr[" + (i+1) + "]/td[1]"));
                WebElement score = driver.findElement(By.xpath("/html/body/main/div/div/div/div[4]/div/div/div/div/nba-stat-table[1]/div[2]/div[1]/table/tbody/tr[" + (i+1) + "]/td[10]"));
                scores.add(new Score(date.getText(), score.getText()));
            }

            for (Score outcome : scores) {
                System.out.println(outcome.toString());
            }

        }
        finally {
            driver.close();
        }
    }

    public static void wait (int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ie) {
            System.out.println(ie);
        }
    }

    // click element pointed to by given xpath
    public static void click_xpath (String element, WebDriver driver, WebDriverWait wait) {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(element)));
        driver.findElement(By.xpath(element)).click();
    }

    // navigate to the per player page
    public static void open_perplayer (WebDriver driver, WebDriverWait wait) {
        click_xpath("/html/body/div[2]/div/div/nav/div[2]/button", driver, wait);   // Players tab
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/section[1]/ul/li[2]/a")));
        click_xpath("/html/body/div[2]/section[1]/ul/li[2]/a", driver, wait);       // Player index tab
    }

    public static void use_searchbar (String element, String key, WebDriver driver, WebDriverWait wait) {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(element)));
        driver.findElement(By.xpath(element)).sendKeys(key);
    }
}
