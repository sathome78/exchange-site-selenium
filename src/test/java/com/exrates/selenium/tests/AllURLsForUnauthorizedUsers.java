package com.exrates.selenium.tests;

import com.exrates.selenium.page_objects.ExratesAllURLsForUnauthorizedUsers;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class AllURLsForUnauthorizedUsers {
    private ExratesAllURLsForUnauthorizedUsers exrates;
    private String file = "links_list.txt";
    private Object[] urls;

    @BeforeClass
    public void init() throws Exception {
        exrates = new ExratesAllURLsForUnauthorizedUsers();
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