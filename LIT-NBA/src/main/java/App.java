
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;


public class App {

    public static void main(String[] args) {

        // save the player name
        String fullName = "";
        for (String name : args) {
            fullName += name + " ";
        }

        // prepare the necessary drivers
        System.setProperty("webdriver.chrome.driver", "chromedriver");
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, 8);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");

        try {
            // open the browser and navigate to the nba site and accept cookies
            driver.navigate().to("http://www.nba.com/stats/");
            driver.manage().window().maximize();
            String acceptCookies = "//*[@id=\"onetrust-accept-btn-handler\"]";
            click_xpath(acceptCookies, driver, wait);
            wait(2000);

            // open per-player page
            open_perplayer(driver, wait);

            // search for the player
            String playerSearchBar = "//*[@id=\"__next\"]/div[2]/div[3]/section/div/div[1]/div/input";
            use_searchbar(playerSearchBar, fullName, driver, wait);
            wait(1000);

            // get the wanted player's page
            String player = "//*[@id=\"__next\"]/div[2]/div[3]/section/div/div[2]/div[2]/div/div/div/table/tbody/tr[1]/td[1]/a";
            click_xpath(player, driver, wait);

            // stats
            click_xpath("//*[@id=\"__next\"]/div[2]/div[3]/div/div[1]/div/ul/li[2]/a", driver, wait);
            wait(1000);

            Select dropMode = new Select(driver.findElement(By.name("PerMode")));
            dropMode.selectByVisibleText("Per 40 Minutes");
            wait(3000);     // the results often need a while to load

            // save score objects into an Arraylist
            List<Score> scores = new ArrayList<>();

            List<WebElement> rows;
            rows = driver.findElements(By.xpath("/html/body/main/div/div/div/div[4]/div/div/div/div/nba-stat-table[1]/div[2]/div[1]/table/tbody/tr"));

            if (rows.size() == 0) {
                driver.navigate().refresh();
                System.out.println("There are no results available for this player.");
                //driver.close();
            }

            for (int i = 0; i < rows.size(); i++) {
                WebElement date = driver.findElement(By.xpath("/html/body/main/div/div/div/div[4]/div/div/div/div/nba-stat-table[1]/div[2]/div[1]/table/tbody/tr[" + (i+1) + "]/td"));
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

    // needed because of slow loading speeds
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
