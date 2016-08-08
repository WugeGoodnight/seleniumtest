package com.xueqiu.fund.selenium.test;


import com.xueqiu.fund.selenium.service.HttpService;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.safari.SafariDriver;


public class FundBuy {

    WebDriver driver;
    private int a = 0;
    private int b = 0;

    @Before
    public void setUp()
    {
        driver = new ChromeDriver(); //新建一个driver
    }

    @Test
    public void fundBuy() throws InterruptedException
    {
        for (int i = 0;i <10;i++)
        {
            bincCard(driver);
            System.out.println("*************************\n " +
                               "a版本绑卡次数:" + a + "\nb版本绑卡次数:" + b
                                + "\n*************************\n");
        }

    }

    @Test
    public void proportion() throws InterruptedException
    {
        int a = 0;
        int b = 0;

        for (int i =0;i < 20;i ++)
        {
            driver.get("https://danjuanapp.com/openaccount/bindcard");
            Thread.sleep(3000l);

            try {
                driver.findElement(By.className("nativeLink"));
                a++;
            }catch (NoSuchElementException e) {
                b++;
            }
            System.out.println("a的值是:" + a +"\nb的值是:" + b);
        }
    }

    @After
    public void endTest()
    {
        //driver.quit();
    }
    private void bincCard(WebDriver driver) throws InterruptedException
    {
        driver.get("https://danjuanapp.com/openaccount/bindcard");
        Thread.sleep(3000l);
        try {
            driver.findElement(By.className("nativeLink"));
            bindCardA(driver);
            a++;

        }catch (NoSuchElementException e) {
            bindCardB(driver);
            b++;
        }
    }

    private void bindCardB(WebDriver driver) throws InterruptedException
    {
        JSONObject jsonObject =  HttpService.getJsonResBody("/test_data/get2",HttpService.HttpMethod.get);
        String telphone = jsonObject.getJSONObject("data").getString("手机号");
        String cardname = jsonObject.getJSONObject("data").getString("姓名");
        String bankCard = jsonObject.getJSONObject("data").getString("银行卡");
        String idCard = jsonObject.getJSONObject("data").getString("身份证");

        driver.findElement(By.name("cardname")).sendKeys(cardname);
        driver.findElement(By.name("idno")).sendKeys(idCard);
        driver.findElement(By.id("next")).click();

        Thread.sleep(2000l);
        driver.findElement(By.id("cardno")).sendKeys(bankCard);
        driver.findElement(By.id("telno")).sendKeys(telphone);
        driver.findElement(By.className("sendVCode")).click();

        Thread.sleep(3000l);
        driver.findElement(By.id("code")).sendKeys("111111");
        driver.findElement(By.id("next")).click();

        Thread.sleep(4000l);
        driver.manage().deleteAllCookies();

    }

    private void bindCardA(WebDriver driver) throws InterruptedException
    {
        JSONObject jsonObject =  HttpService.getJsonResBody("/test_data/get2",HttpService.HttpMethod.get);
        String telphone = jsonObject.getJSONObject("data").getString("手机号");
        String cardname = jsonObject.getJSONObject("data").getString("姓名");
        String bankCard = jsonObject.getJSONObject("data").getString("银行卡");
        String idCard = jsonObject.getJSONObject("data").getString("身份证");

        driver.findElement(By.id("cardno")).sendKeys(bankCard);
        driver.findElement(By.id("next")).click();

        Thread.sleep(2000l);
        driver.findElement(By.name("cardname")).sendKeys(cardname);
        driver.findElement(By.name("idno")).sendKeys(idCard);
        driver.findElement(By.id("telno")).sendKeys(telphone);
        driver.findElement(By.className("sendVCode")).click();

        Thread.sleep(3000l);
        driver.findElement(By.id("code")).sendKeys("111111");
        driver.findElement(By.id("next")).click();

        Thread.sleep(4000l);
        driver.manage().deleteAllCookies();
    }






}
