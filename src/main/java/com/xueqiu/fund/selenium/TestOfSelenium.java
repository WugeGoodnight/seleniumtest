package com.xueqiu.fund.selenium;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.annotations.*;


public class TestOfSelenium
{
    WebDriver driver;

    @BeforeClass
    public void setUp() {
        driver = new SafariDriver(); //新建一个driver
    }

    @Test
    public void test()
    {
        driver.get("https://www.baidu.com"); //输入网址

        WebElement input = driver.findElement(By.name("wd")); //找到输入框

        input.sendKeys("selenium"); //在输入框中输入关键字

        WebElement searchButton = driver.findElement(By.id("su")); //找到搜索按钮

        searchButton.click(); //点击搜索按钮

        driver.manage().window().maximize();

    }

    @AfterClass
    public void allDone()
    {
        System.out.print("Successed!");
    }

}
