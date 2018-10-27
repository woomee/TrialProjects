package trial;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class TrialSeleniumu {

	public static void main(String[] args) {
		/*
		 * Chromeの場合
		 *
		 * 以下よりWebDriverをダウンロードしてシステムプロパティで指定する
		 * https://sites.google.com/a/chromium.org/chromedriver/downloads
		 * -Dwebdriver.chrome.driver=./bin/chromedriver.exe
		 */
		WebDriver driver = new ChromeDriver();

		/* FireFoxの場合
		 *
		 * 以下よりWebDriverをダウンロードしてシステムプロパティで指定する
		 * https://github.com/mozilla/geckodriver/releases
		 * -Dwebdriver.gecko.driver=./bin/geckodriver.exe
		 */
//		WebDriver driver = new FirefoxDriver();


		/*
		 * ブラウザを開いて実行
		 */
		driver.get("http://example.selenium.jp/reserveApp");
		driver.findElement(By.id("guestname")).sendKeys("サンプルユーザ");
		driver.findElement(By.id("goto_next")).click();
		driver.quit();
	}

}
