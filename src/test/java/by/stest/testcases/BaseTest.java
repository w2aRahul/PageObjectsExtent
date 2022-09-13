package by.stest.testcases;

import by.stest.base.Page;
import org.testng.annotations.AfterSuite;

public class BaseTest {

    @AfterSuite
    public void tearDown() {
        Page.quit();
    }
}
