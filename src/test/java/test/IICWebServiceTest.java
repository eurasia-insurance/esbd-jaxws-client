package test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.xml.rpc.ServiceException;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import tech.lapsa.esbd.jaxws.wsimport.ArrayOfClient;
import tech.lapsa.esbd.jaxws.wsimport.Client;
import tech.lapsa.esbd.jaxws.wsimport.IICWebService;
import tech.lapsa.esbd.jaxws.wsimport.IICWebServiceSoap;
import tech.lapsa.esbd.jaxws.wsimport.User;
import tech.lapsa.java.commons.logging.MyLogger;

public class IICWebServiceTest {

    private static MyLogger logger = MyLogger.getDefault();

    private static DateFormat ESBD_DATE_FORMATER = new SimpleDateFormat("dd.MM.yyyy");

    private static final String TEST_WS_USER_NAME = System.getenv("ASB_USER");
    private static final String TEST_WS_USER_PASSWORD = System.getenv("ASB_PASSWORD");

    private static final String TEST_DATA_IIN = "870622300359";
    private static final String TEST_DATA_RNN = "600320595367";

    private IICWebServiceSoap soap;
    private User user;
    private String aSessionID;

    private void logMsg(String method, String message) {
	logger.INFO.log(method + " : " + message);
    }

    @Before
    public void init() throws MalformedURLException, RemoteException, ServiceException {
	IICWebService service = new IICWebService();
	soap = service.getIICWebServiceSoap();
	user = soap.authenticateUser(TEST_WS_USER_NAME, TEST_WS_USER_PASSWORD);
	assertThat(user, notNullValue());
	aSessionID = user.getSessionID();
	logMsg("init()", "Session established aSessionID = " + aSessionID);
    }

    @Test
    public void testAuthentificateUser() {
	assertThat(user, notNullValue());
	assertThat(user.getCLIENTID(), greaterThan(0));
	logMsg("testAuthentificateUser()", "Authentificated user " + user.getName() + " " + user.getID());
    }

    private Client getTestClientByRNN(String aRNN) throws RemoteException {
	ArrayOfClient result = soap.getClientsByRNN(aSessionID, aRNN);
	assertThat(result, notNullValue());
	assertThat(result.getClient(),
		allOf(notNullValue(), not(Matchers.emptyCollectionOf(Client.class))));
	logMsg("getTestClientByRNN()", "Found client ID = '" + result.getClient().iterator().next().getID() + "'");
	return result.getClient().iterator().next();
    }

    private Client getTestClientByIIN(String aIIN) throws RemoteException {
	Client aClient = new Client();
	aClient.setNaturalPersonBool(1);
	aClient.setRESIDENTBOOL(1);
	aClient.setIIN(aIIN);
	ArrayOfClient result = soap.getClientsByKeyFields(aSessionID, aClient);
	assertThat(result, notNullValue());
	assertThat(result.getClient(),
		allOf(notNullValue(), not(Matchers.emptyCollectionOf(Client.class))));
	return result.getClient().iterator().next();
    }

    @Test
    public void testCheckTestClientByIIN() throws RemoteException {
	Client client = getTestClientByIIN(TEST_DATA_IIN);
	assertThat(client, notNullValue());
	logMsg("testCheckTestClientByIIN()",
		"Client with IIN " + TEST_DATA_IIN + " client name " + client.getFirstName() + " "
			+ client.getLastName() + " " + client.getBorn());
    }

    @Test
    public void testCheckTestClientByRNN() throws RemoteException {
	Client client = getTestClientByRNN(TEST_DATA_RNN);
	assertThat(client, notNullValue());
	logMsg("testCheckTestClientByRNN()",
		"Client with IIN " + TEST_DATA_IIN + " client name " + client.getFirstName() + " "
			+ client.getLastName() + " " + client.getBorn());
    }

    @Test
    public void testCkeckTestClientClassId() throws RemoteException {
	Calendar now = Calendar.getInstance();
	logMsg("testCkeckTestClientClassId()", "Checking for date " + ESBD_DATE_FORMATER.format(now.getTime()));
	Client cl = getTestClientByRNN(TEST_DATA_RNN);
	int aClassId = soap.getClassId(aSessionID, cl.getID(), ESBD_DATE_FORMATER.format(now.getTime()), 0);
	assertThat(aClassId, greaterThan(0));
	String aClassText = soap.getClassText(aSessionID, aClassId);
	assertNotNull(aClassText);
	assertNotEquals("", aClassText);
	logMsg("testCkeckTestClientClassId()", " ClassId " + aClassId + " ClassText " + aClassText);
    }

}
