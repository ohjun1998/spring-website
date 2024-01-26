/*package com.mysite.sbb;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class AutoLogin implements CommandLineRunner {
    @Override
    public void run(String... args) {
        System.out.println("test1");

        System.setProperty("webdriver.chrome.driver", "C:\\webdriver\\chromedriver.exe"); // 여기에 chromedriver의 경로를 입력하세요.

        System.out.println("test");

        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));;

        String url = "http://127.0.0.1:8080/user/login"; // 여기에 로그인 페이지의 URL을 입력하세요.

        for (int i = 0; i < 10; i++) { // 예를 들어, 이 작업을 10번 반복하도록 설정했습니다. 필요에 따라 숫자를 변경하세요.
            driver.get(url);

            // 아이디와 비밀번호 입력
            WebElement id = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("username")));
            WebElement password = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("password")));
            id.sendKeys("admin"); // 여기에 실제 아이디를 입력하세요.
            password.sendKeys("asdzxc123"); // 여기에 실제 비밀번호를 입력하세요.

            // 로그인 버튼 클릭
            WebElement loginButton = driver.findElement(By.id("login")); // 로그인 버튼의 id나 name를 입력하세요.
            loginButton.click();

            Cookie jsessionid = driver.manage().getCookieNamed("JSESSIONID");
            if (jsessionid != null) {
                System.out.println("JSESSIONID: " + jsessionid.getValue());
            } else {
                System.out.println("JSESSIONID not found.");
            }

            // 로그아웃 버튼 클릭
            WebElement logoutButton = driver.findElement(By.id("logout")); // 로그아웃 버튼의 id나 name를 입력하세요.
            logoutButton.click();
        }

        // 브라우저 종료
        driver.quit();
    }
}*/
