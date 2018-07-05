package automation_project1_3;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Olympics20161_2 {
	WebDriver driver;

	@BeforeClass
	public void setUp() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().window().fullscreen();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	@BeforeMethod
	public void start() {

	}
	
	@AfterMethod
	public void tearDown() {
		driver.quit();
	}

	public static String[][] getMedalTable(WebDriver driver) throws InterruptedException {
		driver.get("https://en.wikipedia.org/wiki/2016_Summer_Olympics#Medal_table");
		Thread.sleep(1000);
		String[][] medalTable = new String[10][6];
		List<WebElement> medalTableT = driver.findElements(By.xpath("//table[8]/tbody/tr/td"));
		if (medalTableT.size() > 50) {
			for (int i = 0; i < medalTableT.size()-50; i++) {
				medalTableT.remove(medalTableT.size() - i - 1);
			}
		}
		List<String> medalTableStr = new ArrayList<>();
		for (WebElement webE : medalTableT) {
			medalTableStr.add(webE.getText());
		}
		List<WebElement> medalCountry = driver.findElements(By.xpath("//table[8]/tbody/tr/th/a"));
		List<String> medalCountryNames = new ArrayList<>();
		for (WebElement webE : medalCountry) {
			medalCountryNames.add(webE.getText());
		}
		for (int i = 0; i < medalTable.length; i++) {
			for (int j = 0; j < medalTable[i].length; j++) {
				medalTable[i][j] = (j < 5) ? (medalTableStr.get(5 * i + j)) : medalCountryNames.get(i);
			}
		}
		return medalTable;
	}
	// Test Case 1: SORT TEST
	// 1. Go to website
	// https://en.wikipedia.org/wiki/2016_Summer_Olympics#Medal_table.

	// 3. Click link NOC.
	// 4. Now verify that the table is now sorted by the country names. To do that
	// you need to capture all the names in the NOC column and check
	// if they are in ascending/alphabetical order (highlighted in the picture). Use
	// TestNG assertions.
	// 5. Verify that Rank column is not in ascending order anymore. Use TestNG
	// assertions.

	@Test(priority = 1)
	public void sortTest() throws InterruptedException {
		// 2. Verify that by default the Medal table is sorted by rank.
		// To do that you need to capture all the cells in the Rank column and
		// check if they are in ascending order (highlighted in the picture). Use TestNG
		// assertions.
		String[][] medals = getMedalTable(driver);
		List<Integer> actualN = new ArrayList<>();
		for (int i = 0; i < medals.length; i++) {
			actualN.add(Integer.parseInt(medals[i][0]));
		}
		SortedSet<Integer> expectedN = new TreeSet<Integer>(actualN);
		assertEquals(actualN, expectedN);
		// 3. Click link NOC.
		// 4. Now verify that the table is now sorted by the country names. To do that
		// you need to capture all the names in the NOC column and check
		// if they are in ascending/alphabetical order (highlighted in the picture). Use
		// TestNG assertions.
		driver.findElement(By.xpath("//table[8]/thead/tr/th[2]")).click();
		Thread.sleep(1000);
		medals = getMedalTable(driver);
		List<String> actualS = new ArrayList<>();
		for (int i = 0; i < medals.length; i++) {
			actualS.add(medals[i][5]);
		}
		SortedSet<String> expectedS = new TreeSet<String>(actualS);
		assertEquals(actualS, expectedS);
		// 5. Verify that Rank column is not in ascending order anymore. Use TestNG
		// assertions.
		actualN = new ArrayList<Integer>();
		for (int i = 0; i < medals.length; i++) {
			actualN.add(Integer.parseInt(medals[i][0]));
		}
		expectedN = new TreeSet<Integer>(actualN);
		assertFalse(actualN.equals(expectedN));
	}

	// Test Case 2: THE MOST
	//
	// 1. Go to website https://en.wikipedia.org/wiki/2016_Summer_Olympics.
	// 2. Write a method that returns the name of the country with the most number
	// of gold medals.
	public static String nameOfTheCountryWithTheMostColoredMedals(WebDriver driver, String[][] medals, int a) {
		List<Integer> medalN = new ArrayList<Integer>();
		for (int i = 0; i < medals.length; i++) {

			medalN.add(Integer.parseInt(medals[i][a]));
		}
		int medalNum = Collections.max(medalN);
		int indexOfMedalNum = medalN.indexOf(medalNum);
		String name = medals[indexOfMedalNum][5];
		return name;
	}
	// 3. Write a method that returns the name of the country with the most number
	// of silver medals.
	// 4. Write a method that returns the name of the country with the most number
	// of bronze medals.
	// 5. Write a method that returns the name of the country with the most number
	// of medals.
	// 6. Write TestNG test for your methods.

	@Test(priority = 2)
	public void theMostTest() throws InterruptedException {
		String[][] medals = getMedalTable(driver);
		String actual = nameOfTheCountryWithTheMostColoredMedals(driver, medals, 1);
		String expected = "United States";
		assertEquals(actual, expected);
		actual = nameOfTheCountryWithTheMostColoredMedals(driver, medals, 2);
		assertEquals(actual, expected);
		actual = nameOfTheCountryWithTheMostColoredMedals(driver, medals, 3);
		assertEquals(actual, expected);
	}

	// Test Case 3: COUNTRY BY MEDAL
	// 1. Go to website https://en.wikipedia.org/wiki/2016_Summer_Olympics.
	// 2. Write a method that returns a list of countries whose silver medal count
	// is equal to 18.
	public static List<String> countryByMedal(WebDriver driver, String[][] medals, int a, int b) {
		List<String> aList = new ArrayList<String>();
		List<Integer> medalN = new ArrayList<Integer>();
		for (int i = 0; i < medals.length; i++) {

			medalN.add(Integer.parseInt(medals[i][a]));
		}
		for (int i = 0; i < medalN.size(); i++) {
			if (medalN.get(i) == b)
				aList.add(medals[i][5]);
		}

		return aList;
	}
	// 3. Write TestNG test for your method.

	@Test(priority = 3)
	public void countryByMedal() throws InterruptedException {
		String[][] medals = getMedalTable(driver);
		List<String> actual = countryByMedal(driver, medals, 2, 18);
		List<String> expected = Arrays.asList("China", "France");
		assertEquals(actual, expected);
	}

	// Test Case 4: GET INDEX
	// 1. Go to website https://en.wikipedia.org/wiki/2016_Summer_Olympics.
	// 2. Write a method that takes country name and returns the row and column
	// number. You decide the datatype of the return.
	public static String getIndex(WebDriver driver, String[][] medals, String a) {
		String rowandColumn = "";
		for (int i = 0; i < medals.length; i++) {
			if (medals[i][5].equals(a)) {
				rowandColumn = "row " + (i+1) + " and column 2";
			}
		}

		return rowandColumn;
	}
	// 3. Write TestNG test for your method (use Japan as test input).

	@Test(priority = 4)
	public void getIndex() throws InterruptedException {
		String[][] medals = getMedalTable(driver);
		String actual = getIndex(driver, medals, "China");
		String expected = "row 2 and column 2";
		assertEquals(actual, expected);
	}

	// Test Case 5: GET SUM
	// 1. Go to website https://en.wikipedia.org/wiki/2016_Summer_Olympics.
	// 2. Write a method that returns a list of two countries whose sum of bronze
	// medals is 18.
	public static List<String> countryByMedalgetSum(WebDriver driver, String[][] medals, int a, int b) {
		List<String> aList = new ArrayList<String>();
		List<Integer> medalN = new ArrayList<Integer>();
		for (int i = 0; i < medals.length; i++) {
			medalN.add(Integer.parseInt(medals[i][a]));
		}
		OUTER: for (int i = 0; i < medalN.size(); i++) {
			for (int j = 0; j < medalN.size(); j++) {
				if (medalN.get(i) + medalN.get(j) == b&&i!=j) {
					aList.add(medals[i][5]);
					aList.add(medals[j][5]);
					break OUTER;
				}
			}
		}
		return aList;
	}
	// 3. Write TestNG test for your method.

	@Test(priority = 5)
	public void getSumTest() throws InterruptedException {
		String[][] medals = getMedalTable(driver);
		List<String> actual = countryByMedalgetSum(driver, medals, 3, 18);
		List<String> expected = Arrays.asList("Australia", "Italy");
		assertEquals(actual, expected);
	}
}
