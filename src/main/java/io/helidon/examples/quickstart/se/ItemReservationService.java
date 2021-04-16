
package io.helidon.examples.quickstart.se;

import io.helidon.common.http.Headers;
import io.helidon.common.http.Http;
import io.helidon.common.http.MediaType;
import io.helidon.common.http.Parameters;
import io.helidon.common.reactive.Single;
import io.helidon.config.Config;
import io.helidon.media.jsonp.JsonpSupport;

import io.helidon.webclient.WebClient;
import io.helidon.webclient.WebClientRequestHeaders;
import io.helidon.webserver.*;
import org.fluentd.logger.FluentLogger;

import javax.json.*;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fluentd.logger.FluentLogger;

/**
 * A simple service to greet you. Examples:
 *
 * Get default greeting message:
 * curl -X GET http://localhost:8080/greet
 *
 * Get greeting message for Joe:
 * curl -X GET http://localhost:8080/greet/Joe
 *
 * Change greeting
 * curl -X PUT -H "Content-Type: application/json" -d '{"greeting" : "Howdy"}' http://localhost:8080/greet/greeting
 *
 * The message is returned as a JSON object
 */

public class ItemReservationService implements Service {

    /**
     * The config value for the key {@code greeting}.
     */
    private final AtomicReference<String> greeting = new AtomicReference<>();

    private static final JsonBuilderFactory JSON = Json.createBuilderFactory(Collections.emptyMap());

    private static final Logger LOGGER = Logger.getLogger(ItemReservationService.class.getName());

    ItemReservationService(Config config) {
        greeting.set(config.get("app.greeting").asString().orElse("Ciao"));
    }

    /**
     * A service registers itself by updating the routing rules.
     * @param rules the routing rules.
     */
    @Override
    public void update(Routing.Rules rules) {
        rules
            .get("/", this::getDefaultMessageHandler)
                .get("/saas",this::getSaasResponse)
                .get("/on-hand/{name}",this::getOnhand)
            //.get("/{name}", this::getMessageHandler)
                .get("/item-search/{name}", this::getSaasResponse)
            .put("/greeting", this::updateGreetingHandler)
            .post("/reserve/{orgCode}/{subInvCode}/{itemNum}/{Qty}",this::reserve);
    }

    /**
     * Return a worldly greeting message.
     * @param request the server request
     * @param response the server response
     */
    private void getDefaultMessageHandler(ServerRequest request, ServerResponse response) {
        sendResponse(response, "World");
    }

    /**
     * Return a greeting message using the name that was provided.
     * @param request the server request
     * @param response the server response
     */
    private void getMessageHandler(ServerRequest request, ServerResponse response) {
        String name = request.path().param("name");
        //getSaasResponse(request,response);
        sendResponse(response, name);
    }

    private void reserve(ServerRequest request, ServerResponse response){
        //CreateReservation createReservation = (CreateReservation) request.content().as(CreateReservation.class);
        try {
            System.out.println("Reached");
            String orgCode = request.path().param("orgCode");
            System.out.println("orgCode "+orgCode);

            String subInvCode = request.path().param("subInvCode");
            System.out.println("Sub Inv Code "+subInvCode);

            String itemNum = request.path().param("itemNum");
            System.out.println("Item Num "+itemNum);

            String qty = request.path().param("Qty");
            System.out.println("Qty "+qty);

            JsonObjectBuilder jsonRequestBuilder = Json.createObjectBuilder();
            jsonRequestBuilder.add("OrganizationCode",orgCode);
            jsonRequestBuilder.add("ItemNumber",itemNum);
            jsonRequestBuilder.add("SubinventoryCode",subInvCode);
            jsonRequestBuilder.add("ReservationQuantity",qty);

            this.reserveQty(jsonRequestBuilder.build(),response);

            // request.content().as(JsonObject.class)
            // .thenAccept(jo->reserveQty(jo,response));

            //System.out.println("Reserve >> " + request.content().as(JsonObject.class).get());
            /*request.content().as(JsonObject.class);
            JsonObject jo = request.content().as(JsonObject.class).get();
            System.out.println("Jo "+jo);
            jo.getString("ReservationQuantity");*/
            //System.out.println("Reserve >> " + request.content().as(JsonObject.class)
                    //.thenAccept(reservation->System.out.println("Reservation "+ reservation)));
            //Reservation reservation= new Reservation();// = new Reservation();
            /*request.content().as(Reservation.class)
                    .thenApply(e-> Reservation.of(e.getOrganizationCode(),e.getItemNumber()
                            ,e.getSubinventoryCode(),e.getReservationQuantity()))
                    .thenApply(this::reserveQty);

                            request.content().as(JsonObject.class)
            .thenAccept(jo -> updateGreetingFromJson(jo, response))
            .exceptionally(ex -> processErrors(ex, request, response));
                    */
            //System.out.println("Json "+request.content().as(JsonObject.class).get());
            //System.out.println("Getting value "+request.content().as(Reservation.class).get().getOrganizationCode());
                    //.thenAccept(e->reserveQty(e,response));

            //System.out.println("ReservationQty "+reservation.getSubinventoryCode());
            //System.out.println("ReservationQty "+jo.getString("ReservationQuantity"));
            //
            //sendResponse(response, "reserve");
        }
        catch(Exception e){
            System.out.println("Exception "+e);
        }
    }

   /* private Reservation reserveQty(Reservation reservation){
        System.out.println("ReservationQty in reserveQty "+reservation.getSubinventoryCode());
        return reservation;
    }*/

    private void reserveQty(JsonObject jo, ServerResponse response){
        try {
            /*String itemNumber = jo.getString("itemNumber");
            String orgCode = jo.getString("organizationCode");
            String subInvCode = jo.getString("SubinventoryCode");
            String reservationQuantity = jo.getString("reservationQuantity");
            System.out.println("Item Number "+itemNumber);
            System.out.println("subInvCode "+subInvCode);
            System.out.println("reservationQuantity "+reservationQuantity);*/
            /*
            RequirementDate;
    private String DemandSourceType;
    private String DemandSourceName;
    private String SupplySourceType;
             */

            JsonObjectBuilder jsonRequestBuilder = Json.createObjectBuilder(jo);

            jsonRequestBuilder.add("DemandSourceType","User Defined");
            jsonRequestBuilder.add("DemandSourceName","SAAS-Data-Import1");
            jsonRequestBuilder.add("SupplySourceType","On hand");
            jsonRequestBuilder.add("RequirementDate","2021-02-22T00:00:00+00:00");


            WebClient webclient = WebClient.builder()
                    .baseUri("https://edrx-test.fa.us2.oraclecloud.com/fscmRestApi/resources/11.13.18.05/inventoryReservations")

                    .addMediaSupport(JsonpSupport.create())

                    .addHeader("Authorization", "Basic U0NNVVNFUjpPcmFjbGUxMjM=")
                    .build();
            System.out.println("Webclient " + webclient);


            /*Reservation reservation = new Reservation(orgCode,itemNumber,subInvCode,2,
                    "2021-02-22T00:00:00+00:00","User Defined","SAAS-Data-Import1"
            ,"On hand");*/
            //Object obj = JsonValue
            JsonObject content = webclient
                    .post()//.su
                    .submit(jsonRequestBuilder.build(),JsonObject.class).get();

            System.out.println("Json content :: " + content);
            //JsonObject joResp = content.get();
            Long reservationId = content.getJsonNumber("ReservationId").longValue();
            Long orgId = content.getJsonNumber("OrganizationId").longValue();
            //JsonArray array = content.getJsonArray();
            System.out.println("Reservation ID :: " + reservationId);

            //System.out.println("Json content :: " + content.getString("ReservationId").toString());

            this.sendResponseForReservation(response, content);
        }
        catch(Exception e){
            System.out.println("Exception "+e.getCause()+" "+e.getMessage());
        }
    }
    /**
     * Method that gets the data from SaaS. The itemId is obtained as a parameter and sent as a query string to get
     * the response.
     * @param request
     * @param response
     */
    private void getSaasResponse(ServerRequest request, ServerResponse response){
        try {
            String itemId = request.path().param("name");
            FluentLogger LOG = FluentLogger.getLogger("app", "129.213.13.244", 24224);
            LOG.log("kubernetes","Msg1","{source: 'find_item_api', message: 'Successful call to SaaS API }");

            WebClient webclient = WebClient.builder()
                    .baseUri("https://edrx-test.fa.us2.oraclecloud.com/fscmRestApi/resources/11.13.18.05/itemsV2")
                    .addMediaSupport(JsonpSupport.create())
                    .addHeader("Authorization", "Basic U0NNVVNFUjpPcmFjbGUxMjM=")
                    .build();
            System.out.println("Webclient "+webclient);
            JsonObject content = webclient
                    .get()
                    .queryParam("fields","ItemId,ItemNumber,ItemDescription,OrganizationCode")
                    .queryParam("onlyData","true")
                    //.queryParam("q","ItemNumber=IBM1")
                    .queryParam("q","ItemNumber="+itemId)
                    .request(JsonObject.class).get();
            System.out.println("Json content :: "+content);
            JsonArray itemArray = content.getJsonArray("items");
            System.out.println("Array content :: "+itemArray);

            //content.forEach()
            sendResponse(response,itemArray);
        }catch (Exception e){
            System.out.println(e);
        }
        //sendResponse(response,"Brati");

    }

    private void getOnhand(ServerRequest request,ServerResponse response){
        //System.out.println("Name "+request.path().param("name"));// + "Org code "+request.path().param("org-code"));
        //https://edrx-test.fa.us2.oraclecloud.com/fscmRestApi/resources/11.13.18.05/onhandQuantityDetails?
        // onlyData=true&q=ItemNumber="FILTER, OIL";OrganizationCode=RAK_MAINT
        String name = request.path().param("name");
        System.out.println("Name "+name);
        String [] param = name.split(";;");
        System.out.println("param "+param[0]+" && "+param[1]);
        try {
            WebClient webclient = WebClient.builder()
                    .baseUri("https://edrx-test.fa.us2.oraclecloud.com/fscmRestApi/resources/11.13.18.05/onhandQuantityDetails")
                    .addMediaSupport(JsonpSupport.create())
                    .addHeader("Authorization", "Basic U0NNVVNFUjpPcmFjbGUxMjM=")
                    .build();
            System.out.println("Webclient " + webclient);
            JsonObject content = webclient
                    .get()
                    .queryParam("onlyData", "true")
                    //.queryParam("q","ItemNumber=IBM1")
                    .queryParam("q", "ItemNumber=\""+param[0]+"\";OrganizationCode=\""+param[1]+"\"")
                    .request(JsonObject.class).get();
            System.out.println("Json content :: " + content);
            JsonArray itemArray = content.getJsonArray("items");
            System.out.println("Array content :: " + itemArray);
            sendResponse(response, itemArray);
        }
        catch (Exception e){
            System.out.println("Exception in onhand "+e);
        }
    }
    private void sendResponse(ServerResponse response, String name) {
        String msg = String.format("%s %s!", greeting.get(), name);

        JsonObject returnObject = JSON.createObjectBuilder()
                .add("message", msg)
                .build();
        response.send(returnObject);
    }

    /**
     * Overloaded sendResponse for sending back saas response.
     * @param response
     * @param jArray
     */
    private void sendResponse(ServerResponse response, JsonArray jArray) {
        //String msg = String.format("%s %s!", greeting.get(), name);
        /*
        response.headers().add("Access-Control-Allow-Credentials","true");

        response.headers().add("Access-Control-Allow-Headers",
                "X-Requested-With,content-type");
        response.headers().add("Access-Control-Allow-Methods",
                "GET, POST, OPTIONS, PUT, PATCH, DELETE");
        response.headers().add("Access-Control-Allow-Origin","*");

        response.headers().add("Transfer-Encoding","chunked");
        */
        //String msg = String.format("%s %s!", jArray.toString());

        JsonObjectBuilder returnObjectBuilder = Json.createObjectBuilder();
        /*JsonObject returnObject = JSON.createObjectBuilder()
                .add("message", msg)
                .build();*/

        returnObjectBuilder.add("items", jArray);
        //returnObjectBuilder.add("Access-Control-Allow-Origin","*");
        JsonObject returnObject = returnObjectBuilder.build();

        response.send(returnObject);
    }

    private void sendResponseForReservation(ServerResponse response, JsonObject jo) {
        //String msg = String.format("%s %s!", greeting.get(), name);
        /*
        response.headers().add("Access-Control-Allow-Credentials","true");

        response.headers().add("Access-Control-Allow-Headers",
                "X-Requested-With,content-type");
        response.headers().add("Access-Control-Allow-Methods",
                "GET, POST, OPTIONS, PUT, PATCH, DELETE");
        response.headers().add("Access-Control-Allow-Origin","*");

        response.headers().add("Transfer-Encoding","chunked");
        */
        //String msg = String.format("%s %s!", jArray.toString());

        JsonObjectBuilder returnObjectBuilder = Json.createObjectBuilder();
        /*JsonObject returnObject = JSON.createObjectBuilder()
                .add("message", msg)
                .build();*/
        returnObjectBuilder.add("status","Success");
        returnObjectBuilder.add("items", jo);
        //returnObjectBuilder.add("Access-Control-Allow-Origin","*");
        JsonObject returnObject = returnObjectBuilder.build();
        System.out.println ("Return content "+returnObject);

        response.send(returnObject);
    }
    private static <T> T processErrors(Throwable ex, ServerRequest request, ServerResponse response) {

         if (ex.getCause() instanceof JsonException){

            LOGGER.log(Level.FINE, "Invalid JSON", ex);
            JsonObject jsonErrorObject = JSON.createObjectBuilder()
                .add("error", "Invalid JSON")
                .build();
            response.status(Http.Status.BAD_REQUEST_400).send(jsonErrorObject);
        }  else {

            LOGGER.log(Level.FINE, "Internal error", ex);
            JsonObject jsonErrorObject = JSON.createObjectBuilder()
                .add("error", "Internal error")
                .build();
            response.status(Http.Status.INTERNAL_SERVER_ERROR_500).send(jsonErrorObject);
        }

        return null;
    }

    private void updateGreetingFromJson(JsonObject jo, ServerResponse response) {
        if (!jo.containsKey("greeting")) {
            JsonObject jsonErrorObject = JSON.createObjectBuilder()
                    .add("error", "No greeting provided")
                    .build();
            response.status(Http.Status.BAD_REQUEST_400)
                    .send(jsonErrorObject);
            return;
        }

        greeting.set(jo.getString("greeting"));
        response.status(Http.Status.NO_CONTENT_204).send();
    }

    /**
     * Set the greeting to use in future messages.
     * @param request the server request
     * @param response the server response
     */
    private void updateGreetingHandler(ServerRequest request,
                                       ServerResponse response) {
        request.content().as(JsonObject.class)
            .thenAccept(jo -> updateGreetingFromJson(jo, response))
            .exceptionally(ex -> processErrors(ex, request, response));
    }
}