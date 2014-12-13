package org.wso2.carbon.bam.activityMonitor;

import org.apache.log4j.Logger;
import org.wso2.carbon.databridge.agent.thrift.Agent;
import org.wso2.carbon.databridge.agent.thrift.DataPublisher;
import org.wso2.carbon.databridge.agent.thrift.conf.AgentConfiguration;
import org.wso2.carbon.databridge.agent.thrift.exception.AgentException;
import org.wso2.carbon.databridge.commons.Event;
import org.wso2.carbon.databridge.commons.exception.DifferentStreamDefinitionAlreadyDefinedException;
import org.wso2.carbon.databridge.commons.exception.MalformedStreamDefinitionException;
import org.wso2.carbon.databridge.commons.exception.NoStreamDefinitionExistException;
import org.wso2.carbon.databridge.commons.exception.StreamDefinitionException;
import org.wso2.carbon.databridge.commons.exception.TransportException;

import javax.security.sasl.AuthenticationException;
import java.lang.String;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Random;

public class ActivityMonitor {
    private static Logger logger = Logger.getLogger(ActivityMonitor.class);
    public static final String ACTIVITY_MONITORING_STREAM = "org.wso2.bam.activity.monitoring";
    public static final String VERSION = "1.0.0";

    private static String currOperationName = "";
    private static String currServiceName = "";
    private static String currActivityID = "";
    private static boolean currIsError = false;

    public static final String[] activityId = {"1cecbb16-6b89-46f3-bd2f-fd9f7ac447b6",
                                               "2cecbb16-6b89-46f3-bd2f-fd9f7ac447b6",
                                               "3cecbb16-6b89-46f3-bd2f-fd9f7ac447b6",
                                               "4cecbb16-6b89-46f3-bd2f-fd9f7ac447b6",
                                               "5cecbb16-6b89-46f3-bd2f-fd9f7ac447b6",
                                               "6cecbb16-6b89-46f3-bd2f-fd9f7ac447b6",
                                               "7cecbb16-6b89-46f3-bd2f-fd9f7ac447b6",
                                               "8cecbb16-6b89-46f3-bd2f-fd9f7ac447b6",
                                               "9cecbb16-6b89-46f3-bd2f-fd9f7ac447b6",
                                               "0cecbb16-6b89-46f3-bd2f-fd9f7ac447b6",
                                               "1decbb16-6b89-46f3-bd2f-fd9f7ac447b6",
                                               "2decbb16-6b89-46f3-bd2f-fd9f7ac447b6",
                                               "3decbb16-6b89-46f3-bd2f-fd9f7ac447b6",
                                               "4decbb16-6b89-46f3-bd2f-fd9f7ac447b6",
                                               "5decbb16-6b89-46f3-bd2f-fd9f7ac447b6",
    };

    public static final String[] hostAddress = {"192.168.1.1:9763",
                                               "192.168.1.2:9764",
                                               "192.168.1.3:9765",
                                               "192.168.1.4:9766",
                                               "192.168.1.4:9767"};

    public static final String[] service = {"Simple_Stock_Quote_Service_Proxy",
                                                "Test_Service_Proxy",
                                                "Echo_Service_Proxy"};

    public static final String[] messageDirection = {"IN", "OUT"};

    public static final String[] operationName = {"mediate", "operation1", "operation2",
                                                  "operation3"};

    public static final int[] tenantId = {1111, 2222, 3333, 4444, 5555};

    public static final String[] bodyOperationName = {"getfullquote", "getStatistics", "getValues",
                                                        "getAnalytics", "getSummary"};

    public static final String[] bodySymbol = {"AAA", "BBB", "CCC", "DDD", "EEE"};


    public static void main(String[] args) throws AgentException,
                                                  MalformedStreamDefinitionException,
                                                  StreamDefinitionException,
                                                  DifferentStreamDefinitionAlreadyDefinedException,
                                                  MalformedURLException,
                                                  AuthenticationException,
                                                  NoStreamDefinitionExistException,
                                                  TransportException, SocketException,
                                                  org.wso2.carbon.databridge.commons.exception.AuthenticationException {
        System.out.println("Starting Activity Monitoring Sample");
        AgentConfiguration agentConfiguration = new AgentConfiguration();
        String currentDir = System.getProperty("user.dir");
        System.setProperty("javax.net.ssl.trustStore", currentDir + "/src/main/resources/client-truststore.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
        Agent agent = new Agent(agentConfiguration);
        String host;

        if (getLocalAddress() != null) {
           host = getLocalAddress().getHostAddress();
        } else {
           host = "localhost"; // Defaults to localhost
        }

        String url = getProperty("url", "tcp://" + host + ":" + "7611");
        String username = getProperty("username", "admin");
        String password = getProperty("password", "admin");

        //create data publisher

        DataPublisher dataPublisher = new DataPublisher(url, username, password, agent);
        String streamId = null;

        try {
            streamId = dataPublisher.findStream(ACTIVITY_MONITORING_STREAM, VERSION);
            System.out.println("Stream already defined");

        } catch (NoStreamDefinitionExistException e) {
            streamId = dataPublisher.defineStream("{" +
                                                  "  'name':'" + ACTIVITY_MONITORING_STREAM + "'," +
                                                  "  'version':'" + VERSION + "'," +
                                                  "  'nickName': 'Activity_Monitoring'," +
                                                  "  'description': 'A sample for Activity Monitoring'," +
                                                  "  'metaData':[" +
                                                  "          {'name':'character_set_encoding','type':'STRING'}," +
                                                  "          {'name':'host','type':'STRING'}," +
                                                  "          {'name':'http_method','type':'STRING'}," +
                                                  "          {'name':'message_type','type':'STRING'}," +
                                                  "          {'name':'remote_address','type':'STRING'}," +
                                                  "          {'name':'remote_host','type':'STRING'}," +
                                                  "          {'name':'service_prefix','type':'STRING'}," +
                                                  "          {'name':'tenant_id','type':'INT'}," +
                                                  "          {'name':'transport_in_url','type':'STRING'}" +
                                                  "  ]," +
                                                  "  'correlationData':[" +
                                                  "          {'name':'bam_activity_id','type':'STRING'}" +
                                                  "  ]," +
                                                  "  'payloadData':[" +
                                                  "          {'name':'soap_body','type':'STRING'}," +
                                                  "          {'name':'soap_header','type':'STRING'}," +
                                                  "          {'name':'message_direction','type':'STRING'}," +
                                                  "          {'name':'message_id','type':'STRING'}," +
                                                  "          {'name':'operation_name','type':'STRING'}," +
                                                  "          {'name':'service_name','type':'STRING'}," +
                                                  "          {'name':'timestamp','type':'LONG'}" +
                                                  "  ]" +
                                                  "}");
//            //Define event stream
        }


        //Publish event for a valid stream
        if (!streamId.isEmpty()) {
            System.out.println("Stream ID: " + streamId);

            for (int i = 0; i < 100; i++) {
                publishEvents(dataPublisher, streamId, i);
                System.out.println("Events published : " + (i + 1));
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }

            dataPublisher.stop();
        }
    }

    private static void publishEvents(DataPublisher dataPublisher, String streamId, int i) throws AgentException {
        Event eventOne = new Event(streamId, System.currentTimeMillis(), getMetadata(), getCorrelationdata(),
                                   getPayloadData());
        dataPublisher.publish(eventOne);
    }

    private static Object[] getMetadata(){
        currServiceName = getRandomServiceName();
        return new Object[]{
                "UTF-8",
                getRandomHostAddress(),
                "POST",
                "text/xml",
                "127.0.0.1",
                "localhost",
                "https://my:8244",
                getRandomTenantID(),
                "/services/" + currServiceName
        } ;
    }

    private static Object[] getCorrelationdata(){
        currActivityID = getRandomActivityID();
        return new Object[]{
                currActivityID
        } ;
    }

    private static Object[] getPayloadData(){
        return new Object[]{
                getRandomMessageBody(),
                getRandomMessageHeader(),
                getMessageDirection(),
                "urn:uuid:c70bae36-b163-4f3e-a341-d7079c58f1ba",
                getOperationName(),
                currServiceName,
                System.currentTimeMillis()
        } ;
    }

    private static String getRandomHostAddress() {
        return hostAddress[getRandomId(5)];
    }

    private static String getRandomServiceName() {
        return service[getRandomId(3)];
    }

    private static String getMessageDirection() {
        return messageDirection[getRandomId(2)];
    }

    private static String getOperationName() {
        return operationName[getRandomId(4)];
    }

    private static String getRandomActivityID() {
        return activityId[getRandomId(15)];
    }

    private static int getRandomTenantID() {
        return tenantId[getRandomId(5)];
    }

    private static String getBodyOperationName() {
        return bodyOperationName[getRandomId(5)];
    }
    
    private static String getBodySymbol() {
        return bodySymbol[getRandomId(5)];
    }

    private static String getRandomMessageBody() {
        currOperationName = getBodyOperationName();
        currIsError = getRandomId(10) == 1 ;
        if(currIsError) {
            return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                   "   <soapenv:Body>" +
                   "      <soapenv:Fault xmlns:axis2ns2=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                   "         <faultcode>axis2ns2:Client</faultcode>" +
                   "         <faultstring>Invalid value \"string1\" for element in</faultstring>" +
                   "         <detail/>" +
                   "      </soapenv:Fault>" +
                   "   </soapenv:Body>" +
                   "</soapenv:Envelope>";
        } else {
            return "<soapenv:body xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"><m0:" + currOperationName +
                   " xmlns:m0=\"http://services.samples\"><m0:request><m0:symbol>" + getBodySymbol() +
                   "</m0:symbol></m0:request></m0:" + currOperationName +
                   "></soapenv:body>";
        }

    }

    private static String getRandomMessageHeader() {
        if(currIsError) {
            return "";
        } else {
            return "<soapenv:header xmlns:wsa=\"http://www.w3.org/2005/08/addressing\" xmlns:" +
                   "soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                   "<wsa:to>https://my:8244/services/" + currServiceName +
                   "</wsa:to><wsa:messageid>urn:uuid:c70bae36-b163-4f3e-a341-d7079c58f1ba" +
                   "</wsa:messageid><wsa:action>urn:" + currOperationName +
                   "</wsa:action><ns:bamevent activityid=\"" + currActivityID +
                   "\" xmlns:ns=\"http://wso2.org/ns/2010/10/bam\"></ns:bamevent></soapenv:header>";
        }

    }

    private static int getRandomId(int i) {
        Random randomGenerator = new Random();
        return randomGenerator.nextInt(i);
    }

    public static InetAddress getLocalAddress() throws SocketException {
        Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
        while (ifaces.hasMoreElements()) {
            NetworkInterface iface = ifaces.nextElement();
            Enumeration<InetAddress> addresses = iface.getInetAddresses();

            while (addresses.hasMoreElements()) {
                InetAddress addr = addresses.nextElement();
                if (addr instanceof Inet4Address && !addr.isLoopbackAddress()) {
                    return addr;
                }
            }
        }

        return null;
    }

    private static String getProperty(String name, String def) {
        String result = System.getProperty(name);
        if (result == null || result.length() == 0 || result == "") {
            result = def;
        }
        return result;
    }
}
