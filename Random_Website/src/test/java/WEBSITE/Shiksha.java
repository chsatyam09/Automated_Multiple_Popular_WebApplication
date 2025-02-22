package WEBSITE;

package program;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Shiksha {

    public static void main(String[] args) throws InterruptedException {
        // Launch Shiksha site
        System.setProperty("webdriver.chrome.driver", "./Drivers/chromedriver.exe");
        System.setProperty("webdriver.chrome.silentOutput", "true");

        ChromeOptions options = new ChromeOptions();
        WebDriver driver = new ChromeDriver(options);
        driver.get("https://studyabroad.shiksha.com/");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

        String title = driver.getTitle();
        if (title.contains("Study Abroad"))
            System.out.println("Shiksha Study Abroad launched successfully: " + title);
        else
            System.err.println("Shiksha Study Abroad launch failed");

        // Mouse over on Colleges and click MS in Computer Science & Engg under MS Colleges 
        Actions build = new Actions(driver);
        WebDriverWait wait = new WebDriverWait(driver, 10);
        build.moveToElement(driver.findElement(By.xpath("//label[text()='Colleges ']"))).pause(2000).perform();
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//label[text()='MS Colleges']"))));
        driver.findElement(By.xpath("//label[text()='MS Colleges']/following-sibling::ul//a[text()='MS in Computer Science &Engg']")).click();
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//h1[text()='MS in Computer Science & Engineering Abroad']"))));
        if (driver.findElement(By.xpath("//h1[text()='MS in Computer Science & Engineering Abroad']")).isDisplayed()) {
            System.out.println("MS in Computer Science & Engineering screen is displayed ");
        }

        // Select GRE under Exam Accepted and Score 300 & Below 
        driver.findElement(By.xpath("//p[text()='GRE']/parent::label")).click();
        Thread.sleep(1000);
        Select eleScore = new Select(driver.findElement(By.xpath("//p[text()='GRE']/parent::label/following-sibling::div/select")));
        eleScore.selectByVisibleText("300 & below");
        Thread.sleep(1000);

        // Max 10 Lakhs under 1st year Total fees, USA under countries 
        driver.findElement(By.xpath("//label[contains(@for,'fee')]/p[text()='Max 10 Lakhs']")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//a[text()='USA']/ancestor::label[1]/span")).click();
        Thread.sleep(1000);

        // Verify if above filters are applied
        WebElement eleGRE = driver.findElement(By.xpath("//div[@class='selected-items']/p[text()='GRE']"));
        if (eleGRE.isDisplayed()) {
            System.out.println("GRE filter applied");
        }
        WebElement eleFirstyrFee = driver.findElement(By.xpath("//div[@class='selected-items']/p[text()='Max 10 Lacs']"));
        if (eleFirstyrFee.isDisplayed()) {
            System.out.println("Max 10 Lakhs exam fee filter applied");
        }
        WebElement eleCountry = driver.findElement(By.xpath("//div[@class='selected-items']/p[text()='USA']"));
        if (eleCountry.isDisplayed()) {
            System.out.println("USA filter applied");
        }

        // Select Sort By: Low to high 1st year total fees
        Select eleSort = new Select(driver.findElement(By.xpath("//select[@class='sort-select']")));
        eleSort.selectByVisibleText("Low to high 1st year total fees");
        Thread.sleep(500);
        if (driver.findElement(By.xpath("//option[text()='Low to high 1st year total fees']")).isSelected()) {
            System.out.println("Sorted by Low to High 1st year total fees");
        }

        // Verify colleges displayed count
        String collegesCount = driver.findElement(By.xpath("//span[@id='foundCoursesCount']")).getText();
        List<WebElement> eleColleges = driver.findElements(By.xpath("//div[contains(@class,'univ-tab-details')]//div[contains(@id,'categoryPageListing')]"));
        if (eleColleges.size() == Integer.parseInt(collegesCount.replaceAll("\\D", ""))) {
            System.out.println("Number of colleges returned for the filters applied: " + eleColleges.size());
        } else {
            System.err.println("College count mismatch between results fetched and the one displayed in header section");
        }

        // Click Add to compare of the College having least fees with Public University, Scholarship and Accommodation facilities
        String clge1Selected = "";
        boolean isClgeAdded = false;
        for (int i = 0; i < eleColleges.size(); i++) {
            List<WebElement> eleClgeFeatures = driver.findElements(By.xpath("(//div[@class='uni-course-details flLt'])[" + (i + 1) + "]//div[3]//p[not(contains(@class,'non-available'))]"));
            if (eleClgeFeatures.size() == 3) {
                clge1Selected = driver.findElement(By.xpath("(//div[@class='uni-course-details flLt'])[" + (i + 1) + "]/ancestor::div[3]//a")).getText();
                System.out.println("College with all features and least KM: " + clge1Selected);
                Thread.sleep(1000);
                WebElement eleSelect = driver.findElement(By.xpath("(//div[@class='uni-course-details flLt'])[" + (i + 1) + "]/ancestor::div[1]/following-sibling::div[2]//span"));
                eleSelect.click();
                isClgeAdded = true;
            }
            if (isClgeAdded) break;
        }

        // Select the first college under Compare with similar colleges and click on Compare college
        Thread.sleep(1000);
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//p[text()='Compare with similar colleges']"))));
        String clge2Selected = driver.findElement(By.xpath("//p[text()='Compare with similar colleges']/parent::div[1]//a")).getText();
        driver.findElement(By.xpath("//p[text()='Compare with similar colleges']/parent::div[1]//a/span[@class='add-tag']")).click();
        Thread.sleep(2000);
        if (driver.findElement(By.xpath("//strong[text()='Compare Colleges >']/parent::a")).isEnabled()) {
            driver.findElement(By.xpath("//strong[text()='Compare Colleges >']/parent::a")).click();
            System.out.println("Second college selected for compare: " + clge2Selected.replaceAll("[ Add ] ", ""));
        }

        // Select When to Study as 2021
        Thread.sleep(2000);
        driver.findElement(By.xpath("//label[text()='When do you plan to start your studies?']/parent::div/div[2]/label")).click();

        // Select Preferred Countries as USA 
        driver.findElement(By.xpath("//div[contains(@class,'selectCountry')]")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//div[contains(@class,'selectCountry')]//li[2]//label[contains(@for,'USA')]")).click();
        Thread.sleep(500);
        driver.findElement(By.xpath("//div[@class='choose-count']/a[text()='ok']")).click();

        // Select Level of Study as Masters 
        driver.findElement(By.xpath("//label[contains(@for,'Masters')]")).click();

        // Select Preferred Course as MS
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//div[text()='Preferred Course']"))));
        driver.findElement(By.xpath("//div[text()='Preferred Course']/parent::div")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//div[text()='Preferred Course']/parent::div//li[text()='MS']")).click();

        // Select Specialization as "Computer Science & Engineering"
        Thread.sleep(2000);
        driver.findElement(By.xpath("//div[text()='Preferred Specialisations']/parent::div")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//div[text()='Preferred Specialisations']/parent::div//li[text()='Computer Science & Engineering']")).click();

        // Click on Sign Up
        Thread.sleep(1000);
        driver.findElement(By.xpath("//a[contains(text(),'Sign Up')]")).click();

        // Print all the warning messages displayed on the screen for missed mandatory fields
        List<WebElement> listErrorMsg = driver.findElements(By.xpath("//div[contains(@id,'error')]/div[@class='helper-text' and contains(text(),'Please')]"));
        System.out.println("Error message displayed are missed mandatory fields:");
        for (int i = 0; i < listErrorMsg.size(); i++) {
            String errorMsg = listErrorMsg.get(i).getText();
            if (errorMsg.length() > 0) {
                System.out.println(errorMsg);
            }
        }
    }
}

