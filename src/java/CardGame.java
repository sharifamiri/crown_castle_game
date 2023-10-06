import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static io.restassured.RestAssured.*;

public class CardGame {
    public static void main(String[] args) {

        // Initialize WebDriver
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        try {
            // Step 1: Navigate to the Card Game Website
            driver.get("https://deckofcardsapi.com/");

            // Step 2: Confirm the Site is Up
            String actualMessage = driver.findElement(By.xpath("//div/h1[@class='title']")).getText();
            String expectedMessage = "Deck of Cards";
            Assert.assertEquals(expectedMessage, actualMessage);
        } finally {
            // Close the browser window
            driver.quit();
        }

        // Step 3: Get a New Deck
        Response newDockResponse = get("https://deckofcardsapi.com/api/deck/new/");
        Assert.assertEquals(200, newDockResponse.statusCode());
        String deckId = newDockResponse.jsonPath().getString("deck_id");

        // Step 4: Shuffle the Deck
        Response shuffleResponse = given().when().get("https://deckofcardsapi.com/api/deck/" + deckId + "/shuffle/");
        Assert.assertEquals(200, shuffleResponse.statusCode());
        Assert.assertEquals(deckId, shuffleResponse.jsonPath().getString("deck_id"));

        // Step 5: Deal three cards to each of two players
        // Step 6: Check whether either has blackjack
        // Step 7: If either has, write out which one does
        for (int i = 0; i < 2; i++){
            Response dealResponse = given().queryParam("count", 3).when().get("https://deckofcardsapi.com/api/deck/" + deckId + "/draw/");
            Assert.assertEquals(200, dealResponse.statusCode());
            JSONArray cardsArray = new JSONObject(dealResponse.body().asString()).getJSONArray("cards");
            for (int j = 0; j < cardsArray.length(); j++){
                if (cardsArray.getJSONObject(j).get("code").equals("JS")){
                    System.out.println(cardsArray.getJSONObject(j));
                    Assert.assertEquals("SPADES", cardsArray.getJSONObject(j).get("suit"));
                    Assert.assertEquals("JACK", cardsArray.getJSONObject(j).get("value"));
                }
            }
        }
    }
}

