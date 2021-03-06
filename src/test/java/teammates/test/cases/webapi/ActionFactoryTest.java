package teammates.test.cases.webapi;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.testng.annotations.Test;

import teammates.common.exception.ActionMappingException;
import teammates.common.util.Const;
import teammates.test.cases.BaseTestCase;
import teammates.test.driver.MockHttpServletRequest;
import teammates.ui.webapi.action.Action;
import teammates.ui.webapi.action.ActionFactory;
import teammates.ui.webapi.action.GetAuthInfoAction;

/**
 * SUT: {@link ActionFactory}.
 */
public class ActionFactoryTest extends BaseTestCase {

    @Test
    public void testGetAction() throws Exception {
        ActionFactory actionFactory = new ActionFactory();

        ______TS("Action exists and is retrieved");

        MockHttpServletRequest existingActionServletRequest = new MockHttpServletRequest(
                HttpGet.METHOD_NAME, Const.ResourceURIs.URI_PREFIX + Const.ResourceURIs.AUTH);
        existingActionServletRequest.addHeader("Backdoor-Key", "samplekey");
        Action existingAction = actionFactory.getAction(existingActionServletRequest, HttpGet.METHOD_NAME);
        assertTrue(existingAction instanceof GetAuthInfoAction);

        ______TS("Action does not exist and ActionMappingException is thrown");

        MockHttpServletRequest nonExistentActionServletRequest = new MockHttpServletRequest(
                HttpGet.METHOD_NAME, Const.ResourceURIs.URI_PREFIX + "blahblahblah");
        nonExistentActionServletRequest.addHeader("Backdoor-Key", "samplekey");
        ActionMappingException nonExistentActionException = assertThrows(ActionMappingException.class,
                () -> actionFactory.getAction(nonExistentActionServletRequest, HttpGet.METHOD_NAME));
        assertTrue(nonExistentActionException.getMessage()
                .equals("Resource with URI " + Const.ResourceURIs.URI_PREFIX + "blahblahblah" + " is not found."));

        ______TS("Method does not exist on action and ActionMappingException is thrown");

        MockHttpServletRequest nonExistentMethodOnActionServletRequest = new MockHttpServletRequest(
                HttpGet.METHOD_NAME, Const.ResourceURIs.URI_PREFIX + Const.ResourceURIs.AUTH);
        nonExistentMethodOnActionServletRequest.addHeader("Backdoor-Key", "samplekey");
        ActionMappingException nonExistentMethodOnActionException = assertThrows(ActionMappingException.class,
                () -> actionFactory.getAction(nonExistentMethodOnActionServletRequest, HttpPost.METHOD_NAME));
        assertTrue(nonExistentMethodOnActionException.getMessage()
                .equals("Method [" + HttpPost.METHOD_NAME + "] is not allowed for URI "
                + Const.ResourceURIs.URI_PREFIX + Const.ResourceURIs.AUTH + "."));
    }
}
