import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CheckersGame {
    public static void main(String[] args) {

        // Initialize WebDriver
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        try {
            // Step 1: Navigate to the Checkers Game Website
            driver.get("https://www.gamesforthebrain.com/game/checkers/");

            // Step 2: Confirm the Site is Up
            WebElement gameBoard = driver.findElement(By.id("message"));
            String actualMessage = gameBoard.getText().toString();
            String expectedMessage = "Select an orange piece to move.";
            Assert.assertEquals(expectedMessage, actualMessage);

            // Step 3: Make Four of the Five Legal Moves as Orange
            for (int i = 8; i >= 1; i-=2) {
                // Find the orange piece and the destination square for each move
                String orangeElement = "//*[@id=\"board\"]/div[6]/img[" + i + "]";
                String destinationElement = "//*[@id=\"board\"]/div[5]/img[" + (i-1) + "]";
                driver.findElement(By.xpath(orangeElement)).click();
                driver.findElement(By.xpath(destinationElement)).click();

                //"Make a move" as confirmation for the next move
                Thread.sleep(2000);
                String expectedMakeAMoveMessage = "Make a move.";
                String actualMakeAMoveMessage = driver.findElement(By.id("message")).getText();
                Assert.assertEquals(expectedMakeAMoveMessage, actualMakeAMoveMessage);
            }

            // Step 3: Fifth move to get a blue
            String orangeElement = "//*[@id=\"board\"]/div[7]/img[5]";
            String destinationElement = "//*[@id=\"board\"]/div[5]/img[3]";
            driver.findElement(By.xpath(orangeElement)).click();
            driver.findElement(By.xpath(destinationElement)).click();

            //"Make a move" as confirmation for the next move
            String expectedMakeAMoveMessage = "Make a move.";
            String actualMakeAMoveMessage = driver.findElement(By.id("message")).getText();
            Assert.assertEquals(expectedMakeAMoveMessage, actualMakeAMoveMessage);

            // Step 4: Restart the Game
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//p[@class='footnote']/a[1]")))).click();

            // Step 5: Confirm Successful Restart and verify that the game board is in its initial state
            WebElement newGameBoard = driver.findElement(By.id("message"));
            actualMessage = newGameBoard.getText().toString();
            Assert.assertEquals(expectedMessage, actualMessage);


        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            // Close the browser window
            driver.quit();
        }
    }
}

