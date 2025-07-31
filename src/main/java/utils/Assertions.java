package utils;

import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.List;


public class Assertions {

    static SoftAssert softAssert = new SoftAssert();

    public static void assertEquals(String actualResult, String expectedResult) {
        Assert.assertEquals(actualResult, expectedResult, "Actual Result == " + actualResult + " is not matching with Expected Result == " + expectedResult);
    }

    public static void assertEquals(String actualResult, String expectedResult, String msg) {
        Assert.assertEquals(actualResult, expectedResult, msg + " because of below reason \n Actual Result == " + actualResult + " is not matching with Expected Result == " + expectedResult);
    }

    public static void assertNotEquals(String actualResult, String expectedResult) {
        Assert.assertNotEquals(actualResult, expectedResult, "Actual Result == " + actualResult + " is matching with Expected Result == " + expectedResult);
    }

    public static void assertTrue(Boolean value) {
        Assert.assertTrue(value, "Value is: " + value);
    }

    public static void assertTrue(Boolean value, String message) {
        Assert.assertTrue(value, "Message is: " + message);
    }

    public static void assertFalse(Boolean value) {
        Assert.assertFalse(value, "Value is: " + value);
    }

    public static void assertFalse(Boolean value, String message) {
        Assert.assertFalse(value, "Message is: " + message);
    }

    public static void assertNull(Object objectValue) {
        Assert.assertNull(objectValue, "Value is: " + objectValue);
    }

    public static void assertNotNull(Object objectValue) {
        Assert.assertNotNull(objectValue, "Value is: " + objectValue);
    }

    public static void softAssertEquals(String actualResult, String expectedResult) {
        System.out.println("Actual Result == '" + actualResult + "' Expected Result == '" + expectedResult + "'");
        softAssert.assertEquals(actualResult, expectedResult, "Actual Result == " + actualResult + " is not matching with Expected Result == " + expectedResult);
    }

    public void softAssertNotEquals(String actualResult, String expectedResult) {
        softAssert.assertNotEquals(actualResult, expectedResult, "Actual Result == " + actualResult + " is matching with Expected Result == " + expectedResult);
    }

    public static void softAssertTrue(Boolean value, String feature) {
        softAssert.assertTrue(value, "Value is not matching expecting true but actual is" + value + " for " + feature);
    }

    public static void softAssertTrue(Boolean value) {
        softAssert.assertTrue(value, "Value is not matching expecting true but actual is" + value);
    }

    public static void softAssertFalse(Boolean value) {
        softAssert.assertFalse(value, "Value is: " + value);
    }

    public static void softAssertNull(Object objectValue) {
        softAssert.assertNull(objectValue, "Value is: " + objectValue);
    }

    public static void softAssertNotNull(Object objectValue) {
        softAssert.assertNotNull(objectValue, "Value is: " + objectValue);
    }

    /**
     * Used to check the two list value;
     *
     * @param actualResult   : ArrayList of type String
     * @param expectedResult : ArrayList of type String
     */
    public static void softAssertEqualsList(List<ArrayList<String>> actualResult, List<Object> expectedResult) {
        softAssert.assertEquals(actualResult, expectedResult, "Actual Result == " + actualResult + " is not matching with Expected Result == " + expectedResult);
    }

    /**
     * Based oon the outcome testcase status will be displayed if any assert fail than testcase will be fail
     */
    public static void softAssertAll() {
        softAssert.assertAll();
    }
}
