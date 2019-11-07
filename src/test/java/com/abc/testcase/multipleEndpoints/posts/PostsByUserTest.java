package com.abc.testcase.multipleEndpoints.posts;

import com.abc.businesslayer.post.AllPostsBusinessLogic;
import com.abc.businesslayer.user.SingleUserBusinessLogic;
import com.abc.data.dataprovider.CommonDataProvider;
import com.abc.pojo.post.AllPosts;
import com.abc.pojo.post.SinglePost;
import com.abc.pojo.user.SingleUser;
import com.abc.util.ObjectFactory;
import com.abc.util.PropertyReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

import static com.abc.constant.ScenarioNameConstant.VALIDATE_TOTAL_NUMBER_OF_POSTS_MADE_BY_USER_WITH_RESPONSE_BODY;

/**
 * Test Class to perform all the Posts related test cases that are associated with a User
 * Endpoints that are used for Posts test cases in the following Test Class are:
 * - https://<domain>/users
 * - https://<domain>/posts?userId={userId}
 *
 * @author Arsalan Inam
 */

public class PostsByUserTest extends PropertyReader {

    private static final Logger log = LoggerFactory.getLogger(PostsByUserTest.class);
    private SoftAssert softAssert = ObjectFactory.getSoftAssert();

    /*************************************************************************************
     * Test Scenario:
     * - Search for the user.
     * - Use the details fetched to make a search for the posts written by the user.
     * - Validate total number of posts made by individual user,
     * - Validate response body of posts made by individual user contains userId
     * - Validate response body of posts made by individual user is not null
     *
     * @param username - A valid username e.g. "Antonette", "Karianne" etc.
     * @param numberOfPosts - Total number of posts associated with a user
     *************************************************************************************/

    @Test(dataProvider = "validUsernameWithNumberOfPosts", dataProviderClass = CommonDataProvider.class)
    public void testTotalCountOfPostsByUserAndValidateResponseBody(String username, int numberOfPosts) {
        log.info(VALIDATE_TOTAL_NUMBER_OF_POSTS_MADE_BY_USER_WITH_RESPONSE_BODY);

        SingleUser singleUser = SingleUserBusinessLogic.getSingleUserByUserName(username);
        log.info("Username : " + username);
        softAssert.assertNotNull(singleUser);
        int userId = singleUser.getId();
        log.info("User Id: " + userId);

        AllPosts allPostsFromUserId = AllPostsBusinessLogic.getAllPostsForUserId(userId);
        List<SinglePost> allPostsList = allPostsFromUserId.getListOfPosts();
        softAssert.assertEquals(allPostsList.size(), numberOfPosts, "Total number of posts by user");
        log.info("Total Number of Posts : " + allPostsList.size() + " made by User : " + username);

        for (SinglePost singlePost : allPostsList) {
            softAssert.assertEquals(singlePost.getUserId(), userId);
            softAssert.assertNotNull(singlePost.getTitle());
            softAssert.assertNotNull(singlePost.getBody());
        }
        softAssert.assertAll();
    }
}