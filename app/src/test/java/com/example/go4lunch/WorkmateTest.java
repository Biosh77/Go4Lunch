package com.example.go4lunch;

import com.example.go4lunch.models.Workmate;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class WorkmateTest {


    private Workmate workmateTest;


    @Before
    public void setUp() throws Exception {
        workmateTest = new Workmate("1234", "username", null);
    }

        @Test
        public void getWorkmateInfo(){

            assertEquals("1234", workmateTest.getUid());
            assertEquals("username", workmateTest.getUsername());
            assertEquals(null, workmateTest.getUrlPicture());
        }

    @Test
    public void setWorkmateInfo() {
        workmateTest.setUid("1111");
        workmateTest.setUsername("test_username");
        workmateTest.setUrlPicture("url_picture");


        assertEquals("1111", workmateTest.getUid());
        assertEquals("test_username", workmateTest.getUsername());
        assertEquals("url_picture", workmateTest.getUrlPicture());
    }

}