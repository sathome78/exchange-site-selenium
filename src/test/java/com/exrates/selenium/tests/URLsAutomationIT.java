package com.exrates.selenium.tests;

import com.exrates.selenium.page_objects.URLsAutomation;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class URLsAutomationIT {
    private URLsAutomation exrates;
    private String file = "links_list.txt";
    private Object[] urls;

    @BeforeClass
    public void init() throws Exception {
        exrates = new URLsAutomation();
        urls = exrates.parseURLs(file).toArray();
    }

    @DataProvider(name = "testURLs")
    public Object[] allURLs() {
        return urls;
    }

    @Test(dataProvider = "testURLs")
    public void responseCode200(String url) {
        Assert.assertEquals(exrates.getResponseCode(url), 200);
    }
}