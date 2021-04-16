package io.helidon.examples.quickstart.se;


import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;

public final class Reservation {
    private String organizationCode;
    private String itemNumber;
    private String SubinventoryCode;
    private int reservationQuantity;
    private String RequirementDate;
    private String DemandSourceType;
    private String DemandSourceName;
    private String SupplySourceType;

    public Reservation(String organizationCode, String itemNumber, String subinventoryCode,
                       int reservationQuantity, String requirementDate, String demandSourceType,
                       String demandSourceName, String supplySourceType) {
        this.organizationCode = organizationCode;
        this.itemNumber = itemNumber;
        SubinventoryCode = subinventoryCode;
        this.reservationQuantity = reservationQuantity;
        RequirementDate = requirementDate;
        DemandSourceType = demandSourceType;
        DemandSourceName = demandSourceName;
        SupplySourceType = supplySourceType;
    }

    public String getRequirementDate() {
        return RequirementDate;
    }

    public void setRequirementDate(String requirementDate) {
        RequirementDate = requirementDate;
    }

    public String getDemandSourceType() {
        return DemandSourceType;
    }

    public void setDemandSourceType(String demandSourceType) {
        DemandSourceType = demandSourceType;
    }

    public String getDemandSourceName() {
        return DemandSourceName;
    }

    public void setDemandSourceName(String demandSourceName) {
        DemandSourceName = demandSourceName;
    }

    public String getSupplySourceType() {
        return SupplySourceType;
    }

    public void setSupplySourceType(String supplySourceType) {
        SupplySourceType = supplySourceType;
    }





    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getSubinventoryCode() {
        return SubinventoryCode;
    }

    public void setSubinventoryCode(String subinventoryCode) {
        SubinventoryCode = subinventoryCode;
    }

    public int getReservationQuantity() {
        return reservationQuantity;
    }

    public void setReservationQuantity(int reservationQuantity) {
        this.reservationQuantity = reservationQuantity;
    }



    /*
    public   Reservation (){
        this.OrganizationCode = "";
        this.ItemNumber = "";
        this.SubinventoryCode = "";
        this.ReservationQuantity = 0;
    }


    private Reservation(String organizationCode, String itemNumber, String SubinventoryCode, int reservationQuantity) {
        this.OrganizationCode = organizationCode;
        this.ItemNumber = itemNumber;
        this.SubinventoryCode = SubinventoryCode;
        this.ReservationQuantity = reservationQuantity;

    }
   */
    /*@JsonbCreator
    public static Reservation of(@JsonbProperty("organizationCode") String organizationCode,
                          @JsonbProperty("itemNumber") String itemNumber,
                          @JsonbProperty("SubinventoryCode") String subinventoryCode,
                          @JsonbProperty("reservationQuantity") int reservationQuantity){

        System.out.println("Org code "+organizationCode);
        System.out.println("Item Number "+itemNumber);
        System.out.println("Sub Inv "+subinventoryCode);
        System.out.println("Reserv Qty "+reservationQuantity);

        Reservation reservation = new Reservation(organizationCode,itemNumber,subinventoryCode,reservationQuantity);
        return reservation;
    }*/

    /*public static Reservation of(String organizationCode,
                                  String itemNumber,
                                  String subinventoryCode,
                                  int reservationQuantity){

        System.out.println("Org code "+organizationCode);
        System.out.println("Item Number "+itemNumber);
        System.out.println("Sub Inv "+subinventoryCode);
        System.out.println("Reserv Qty "+reservationQuantity);

        Reservation reservation = new Reservation(organizationCode,itemNumber,subinventoryCode,reservationQuantity);
        return reservation;
    }*/


}
