package WEBSITE;



import static org.testng.Assert.fail;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Honda {
    public static ChromeDriver driver;

    public static void main(String[] args) throws InterruptedException {
        // Launch Honda site
        System.setProperty("webdriver.chrome.driver", "./Drivers/chromedriver.exe");
        driver = new ChromeDriver();
        driver.get("https://www.honda2wheelersindia.com/");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));

        String title = driver.getTitle();
        if (title.contains("Honda")) {
            System.out.println("Honda launched successfully: " + title);
        } else
            fail("Honda launch failed");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Actions builder = new Actions(driver);

        // Close the pop-up
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='modal-body']")));
        driver.findElement(By.xpath("//div[@class='modal-body']/button")).click();

        // Call method to select Dio scooter and return Displacement value
        double dioDisplacement = getDisplacement("dio");
        System.out.println("Dio Displacement value is: " + dioDisplacement);
        Thread.sleep(1000);

        // Call method to select Activa125 scooter and return Displacement value
        double activa125Displacement = getDisplacement("activa125");
        System.out.println("Activa 125 displacement value is: " + activa125Displacement);

        // Compare Displacement of Dio and Activa 125 and print the Scooter name
        // having better Displacement
        if (dioDisplacement > activa125Displacement) {
            System.out.println("Displacement value of Dio is better");
        } else {
            System.out.println("Displacement value of Activa 125 is better");
        }

        // Click FAQ from Menu
        driver.findElement(By.xpath("//a[text()='FAQ']")).click();
        if (driver.getTitle().contains("FAQ")) {
            System.out.println("FAQ is loaded");
        } else {
            System.err.println("FAQ loading failed");
        }

        // Click Activa 125 BS-VI under Browse By Product
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h6[text()='Browse by Product']")));
        driver.findElement(By.xpath("//h6[text()='Browse by Product']/following-sibling::div//a[text()='Activa 125 BS-VI']")).click();
        Thread.sleep(1000);

        // Click Vehicle Price
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[text()=' Vehicle Price']")));
        driver.findElement(By.xpath("//a[text()=' Vehicle Price']")).click();

        // Make sure Activa 125 BS-VI is selected and click submit
        WebElement selectedOption = driver.findElement(By.xpath("//option[@selected='selected' and text()='Activa 125 BS-VI']"));
        if (selectedOption.isSelected()) {
            System.out.println("Activa 125 is selected");
        } else {
            System.err.println("Activa 125 is not selected");
        }
        Thread.sleep(500);
        WebElement eleClickCart = driver.findElement(By.xpath("(//button[text()='Submit'])[6]"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", eleClickCart);

        // Click the price link
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//td[3]/a[text()='Click here to know the price of Activa 125 BS-VI.']")));
        driver.findElement(By.xpath("//td[3]/a[text()='Click here to know the price of Activa 125 BS-VI.']")).click();

        // Go to the new Window and select the state as Tamil Nadu and city as Chennai
        Set<String> setHandle = driver.getWindowHandles();
        List<String> listHandle = new ArrayList<>(setHandle);
        driver.switchTo().window(listHandle.get(1));
        if (driver.getTitle().contains("Price")) {
            System.out.println("Price page of Activa 125 is loaded");
        }

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("StateID")));
        Select state = new Select(driver.findElement(By.id("StateID")));
        state.selectByVisibleText("Tamil Nadu");
        Thread.sleep(1000);
        Select city = new Select(driver.findElement(By.id("CityID")));
        city.selectByVisibleText("Chennai");
        Thread.sleep(1000);

        // Click Search
        driver.findElement(By.xpath("//button[text()='Search']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//td[text()='Chennai']")));
        System.out.println("Price table loaded for Chennai city");

        // Print all the 3 models and their prices
        Map<String, String> map = new LinkedHashMap<>();
        List<WebElement> eleModel = driver.findElements(By.xpath("//table[@id='gvshow']/tbody/tr/td[contains(text(),'ACTIVA')]"));
        List<WebElement> elePrice = driver.findElements(By.xpath("//table[@id='gvshow']/tbody/tr/td[contains(text(),'Rs')]"));
        for (int i = 0; i < eleModel.size(); i++) {
            String modelType = eleModel.get(i).getText();
            String modelPrice = elePrice.get(i).getText();
            map.put(modelType, modelPrice);
        }
        for (Entry<String, String> entry : map.entrySet()) {
            System.out.println("Model with prices:");
            System.out.println(entry.getKey() + " - " + entry.getValue());
        }

        // Close browser
        driver.quit();
    }

    // Method to get Displacement value depending on scooter make
    public static double getDisplacement(String scooterMake) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Actions builder = new Actions(driver);

        // Click on scooters and select scooter make
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Scooter']")));
        driver.findElement(By.xpath("//a[text()='Scooter']")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//a[contains(@href,'" + scooterMake + "')])[1]/img")));
        driver.findElement(By.xpath("(//a[contains(@href,'" + scooterMake + "')])[1]/img")).click();

        // Click on Specifications and mouseover on ENGINE
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[text()='Specifications']")));
        driver.findElement(By.xpath("//a[text()='Specifications']")).click();
        Thread.sleep(500);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[text()='ENGINE']")));
        builder.moveToElement(driver.findElement(By.xpath("//a[text()='ENGINE']"))).perform();

        // Get Displacement value
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='Displacement']/following-sibling::span")));
        String rawDisplacement = driver.findElement(By.xpath("//span[text()='Displacement']/following-sibling::span")).getText();
        return Double.parseDouble(rawDisplacement.replaceAll("[a-zA-Z]+", ""));
    }
}
