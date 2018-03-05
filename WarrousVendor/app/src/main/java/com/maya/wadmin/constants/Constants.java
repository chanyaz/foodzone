package com.maya.wadmin.constants;

import com.android.volley.Request;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gokul Kalagara on 1/25/2018.
 */

public class Constants
{

    public static final String CONNECTION_ERROR="Network Error";

    public static final String PLEASE_CHECK_INTERNET="Please Check Internet";

    public static final String PREFS ="com.maya.wadmin";

    public static final String SOMETHING_WENT_WRONG = "Something Went Wrong";

    public static final String ERROR= "Error";

    public static final String INVALID_OPERATION = "Invalid Operation";

    public static final int SECOND_MILLIS = 1000;

    public static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;

    public static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;

    public static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static final int POST_REQUEST = Request.Method.POST;

    public static final int GET_REQUEST = Request.Method.GET;

    public static final String FRAGMENT_KEY = "fragment_key";

    public static final String ACCESS_TOKEN = "access_token";

    public static final String APP_OVERVIEW = "app_overview";

    public static final String EXPIRES_IN = "expires_in";

    public static final String TOKEN_TYPE = "token_type";

    public static final String USER_TYPE = "user_type";

    public static final String USER_ROLL_NAME = "user_roll_name";

    public static final String USER_NAME = "user_name";

    public static final String COMPLETE_USERNAME = "complete_username";

    public static final String FIRST_NAME = "first_name";

    public static final String LAST_NAME = "last_name";

    public static final String PAYLOAD = "pay_load";

    public static final String LOGIN = "login";

    public static final int VEHICLE_TYPE_IDS[] = {1,2,3,4,5,6,7,8,9,10,11,12,13};

    public static final int ALERT_TYPE_IDS[] = {1,3,2,4,5,6,7,8,9};

    public static final String portalsTypeIDS[] = {
            "Distribution & Logistics",
            "Sales Operations"
    };

    public static final String PORTAL_TYPE = "PORTAL_TYPE";

    public static final String ALERT_TYPES[] =
    {
        "Speed",
        "Geofence",
        "Mileage",
        "Theft",
        "Delivery",
        "DTC",
        "Billing",
        "Vehicle Status",
        "Customer Status"
    };

    public static final String VEHICLE_TYPES[] =
    {
            "Inventory",//1
            "Test Drive",
            "Sold",
            "Demo", //4
            "Customer",
            "Transit",
            "Delivery Received", //7
            "Preparing For Lot",
            "Marked for PDI", //9
            "PDI Incompleted",
            "PDI Completed", //11
            "Rental",
            "Loan"
    };

    public static final String URL = "http://13.59.34.59:8080/"; //http://18.218.212.109:8080  http://13.59.34.59:8080/

    public static final String URL_LOGIN = URL + "warrous.ms.auth/warrous.ms.auth.api/api/user/login";

    public static final String URL_CHECK_USERNAME = URL + "warrous.ms.auth/warrous.ms.auth.api/api/User/GetUserCountByUserName?userName=";

    public static final String URL_GET_YEAR = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/GetYear";

    public static final String URL_GET_MAKE = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/GetMake";

    public static final String URL_GET_MODEL = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/GetModel";

    public static final String URL_VEHICLES_TYPE = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/GetVehicleTypes";

    public static final String URL_GET_FILTER_VEHICLES = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/getFilteredFindVehicleList";

    public static final String URL_FETCH_VEHICLES = URL + "warrous.ms.cust_vehicle/warrous.ms.cust_vehicle.api/api/Vehicle/InventoryVehiclesgetDealerId?dealerId=1";

    public static final String URL_FETCH_VEHICLES1 = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/GetVehicleDetailsWithParameters?MakeId=-1&ModelId=-1&YearId=-1&TypeId=-1&StatusId=-1";

    public static final String URL_FETCH_VEHICLES_TYPE_BASED = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/GetVehicleDetailsWithParameters?MakeId=-1&StatusId=-1&ModelId=-1&YearId=-1&TypeId=";

    public static final String URL_VEHICLES_DETAILS = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/GetVehiclesLocations?VehicleId=";

    public static final String URL_EXTRA_DETAILS_VEHICLES = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/GetVehiclesLocations?VehicleId=";

    public static final String URL_FETCH_TEST_DRIVE_LIST = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/GetVehicleDetailsWithParameters?MakeId=-1&StatusId=-1&ModelId=-1&YearId=-1&TypeId=2";

    public static final String URL_FETCH_TEST_DRIVE_LIST_NEW = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/vehicle/GetTestDriveVehiclesList";

    public static final String URL_FETCH_VEHICLES_PDI = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/GetVehicleDetailsWithParameters?MakeId=-1&StatusId=-1&ModelId=-1&YearId=-1&TypeId=-1";

    public static final String URL_FETCH_TESTDRIVE_VEHICLES = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/GetVehicleDetailsWithParameters?MakeId=-1&StatusId=-1&ModelId=-1&YearId=-1&TypeId=2";

    public static final String URL_FETCH_INVENTORY_VEHICLES = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/GetVehicleDetailsWithParameters?MakeId=-1&StatusId=-1&ModelId=-1&YearId=-1&TypeId=1";

    public static final String URL_ASSIGN_TEST_DRIVE = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/UpdateTestDrivevehiclesList";

    public static final String URL_SAVE_TEST_DRIVE = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/vehicle/SaveTestDrive";

    public static final String URL_SALES_PERSONS = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/GetSalesPersonsList";

    public static final String URL_FETCH_VEHICLES_TYPE = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/GetVehicleDetailsWithParameters?MakeId=-1&StatusId=-1&ModelId=-1&YearId=-1&TypeId=-1";

    public static final String URL_TEST_DRIVES = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/GetTestDrive";

    public static final String URL_REPORTS = URL + "warrous.ms.org/warrous.ms.org.api/api/org/ReportsByDealerId?DealerId=";

    public static final String URL_CUSTOMER_DETAILS = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/GetTestDriveList?vehicleId=";

    public static final String URL_GEOFENCES_LIST = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/GetGeofences?DealerId=";

    public static final String URL_EDIT_GEOFENCE = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/getVechicleGeofencesByGeoId?GeofenceGuid=";

    public static final String URL_CREATE_GEOFENCE = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/SaveGeofences?";

    public static final String URL_DELETE_GEOFENCE = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/DeletegeofencesDetails?GeofenceGuid=";

    public static final String URL_FAQ = "https://support.google.com/mail/?hl=en#topic=7065107";

    public static final String URL_APP_OVERVIEW = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/GetAllSoDoCount?DealerId=";

    public static final String URL_FETCH_NON_TEST_DRIVES = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/GetNonTestDriveVehiclesList?MakeId=-1&ModelId=-1&YearId=-1";
    //public static final String URL_FETCH_NON_TEST_DRIVES = URL + "/warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/GetNonTestDriveVehiclesList?MakeId=-1&ModelId=-1&YearId=-1";

    public static final String CUSTOMER_LOOKUP = URL + "warrous.ms.consumer/warrous.ms.consumer.api/api/Consumer/CustomersList?DealerId=";

    public static final String CUSTOMER_LOOKUP_VEHICLES = URL + "warrous.ms.consumer/warrous.ms.consumer.api/api/Consumer/GetVehicleDetailsbyCustId?CustomerId=";

    public static final String DIAGNOSTICS_VEHICLES_LIST = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/GetVehicleDetails";

    public static final String VEHICLE_HEALTH_REPORT = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/GetVehicleDTCCodeStatusByVehicleId?vehicleId=";

    public static final String URL_PROGRAM_INCENTIVES = URL + "warrous.ms.media/warrous.ms.media.api/api/media/GetCpnpRep?programId=-1&langId=-1&isDealerSpecific=false&orgId=4&portalName=Owner%20and%20Warranty%20Operations&dealerId=";

    public static final String URL_OEM_PROGRAMS = URL + "warrous.ms.campaigns/warrous.ms.campaigns.api/api/camp/GetPrograms?OrgId=4&PortalName=Owner%20and%20Warranty%20Operations&LangCode=eng";

    public static final String URL_MORE_TRUCK_DETAILS = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/GetTruckLocations?TransitTruckId=";

    public static final String URL_ALERTS_LIST_BASED_ON_CATEGORY = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/GetAlertsListByCateogyId?categoryId=";

    public static final String URL_ALERT_PICK_VEHICLES = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/GetVehicleDetailsByMakeModelYearAndType?MakeId=-1&StatusId=-1&ModelId=-1&YearId=-1&TypeId=-1";

    public static final String URL_CATEGORY_OPERATION = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/alert/GetOperatorsListByCategoryId?categoryId=";

    public static final String URL_ALERT_GEOFENCE = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/GetGeofences?DealerId=";

    public static final String URL_GENERATE_ALERT_ID = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/CreateAlert?DealerId=";

    public static final String URL_GENERATE_ACTION_CHANNEL_BASED_ALERT_ID = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/GetChannelDetailsByAlertId?alertId=";

    public static final String URL_CREATE_ALERT_CHANNEL = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/CreateAlertChannel?";

    public static final String URL_DELETE_ALERT_CHANNEL = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/DeleteAlertChannelByAlertChannelId?alertChannelId=";

    public static final String URL_UPDATE_ALERT = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/UpdateAlert?alertId=";

    public static final String URL_PDI_VEHICLES = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/GetPdiVehicles?MakeId=-1&StatusId=-1&ModelId=-1&YearId=-1&TypeId=";

    public static final String URL_ROUTES_BASED_ON_VEHICLE_ID = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/vehicle/GetRouteDetailsByVehicleId?vehicleid=";

    public static final String URL_PDI_PREPARATION = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/vehicle/GetVehiclePDIPreparationDetailsByVehicleGuid?vehicleGuid=";

    public static final String URL_LOT_PREPARATION = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/vehicle/GetVehicleLotPreparationDetailsByVehicleGuid?vehicleGuid=";

    public static final String URL_PDI_UPDATE_VALUES = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/UpdateVehiclePDIPreparationDetails?pdiPreparationId=";

    public static final String URL_LOT_UPDATE_VALUES = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/UpdateVehicleLotPreparationDetails?vehicleId=";

    public static final String URL_SAVE_CUSTOMER = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/vehicle/SaveCustomer?dealerid=";

    public static final String URL_FETCH_TEST_DRIVE_CUSTOMERS = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/vehicle/GetCustomersListByDealerId";

    public static final String SAMPLE_CUSTOMER = "https://d2e70e9yced57e.cloudfront.net/wallethub/posts/29912/eric-klinenberg.jpg";

    public static final String SAMPLE_REPORT = "https://www.google.co.in/";

    public static final String SAMPLE_OTHER_CUSTOMER = "http://ww4.msu.ac.zw/mainsite/wp-content/uploads/2014/03/U-member-1.jpg";

    public static final String SAMPLE_OTHER_SALES_PERSON = "https://myhowbook.com/uploads/images/2013/07/2013-07-15-adrien_grenier.jpg";

    public static final String URL_START_TRIP = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/vehicle/SaveTestDriveTrip";

    public static final String URL_SAVE_TRIPPOINTS = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/vehicle/SaveTestDriveTripString?tripData=";

    public static final String URL_STOP_TRIP = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/vehicle/SaveTestDriveTripString?tripData=";

    public static final String URL_GET_TEST_DRIVE_VEHICLES_BY_SALESPERSON = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/GetTestDriveVehiclesBySalesperson?MakeId=-1&ModelId=-1&YearId=-1&TypeId=-1&salesPersonId=-1";

    public static final String URL_FETCH_ALL_VEHICLES = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/GetFindVehicleDetailsWithParameters?MakeId=-1&ModelId=-1&YearId=-1";

    public static final String URL_GET_PDI_VEHICLES_BASED_ON_SALESPERSON = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/GetPDIVehiclesBySalesperson?MakeId=-1&ModelId=-1&YearId=-1&TypeId=-1&salesPersonId=-1";

    public static final String URL_SALES_TECHNICIANS = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/GetDealerSalesTechniciansList";

    public static final String URL_PREPARE_VEHICLES_FOR_LOT = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/PrepareVehiclesForLot";

    public static final String URL_ASSIGN_VEHICLES_FOR_PDI = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/PrepareVehiclesForPDI";

    public static final String URL_LOT_TECHNICIANS = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/GetDealerTechniciansList";

    public static final String URL_LOT_VEHICLES = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/GetLotVehicles?MakeId=-1&ModelId=-1&YearId=-1&TypeId=-1";

    public static final String URL_TRANSIT_TRUCKS_AND_VEHICLES = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/GetTransitTrucksAndVehiclesList";

    public static final String URL_DELIVERY_TRUCKS_VEHICLES = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/GetTransitTruckVehicleDetails";

    public static final String URL_ARCHIEVED_PDI_VEHICLES = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/GetArchievedPdiVehicles?MakeId=-1&ModelId=-1&YearId=-1&TypeId=-1";

    public static final String URL_ARCHIEVED_LOT_VEHICLES = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/GetArchievedLotVehicles?MakeId=-1&ModelId=-1&YearId=-1&TypeId=-1";

    public static final String URL_ARCHIEVED_DELIEVERY_VEHICLES = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/GetDeliveryArchivedVehicleDetailsWithParameters?MakeId=-1&ModelId=-1&YearId=-1";

    public static final String URL_ARCHIEVED_TESTDRIVE_VEHICLES = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/GetArchievedLotVehicles?MakeId=-1&ModelId=-1&YearId=-1&TypeId=-1";

    public static final String URL_SALESPERSON_WITH_VEHICLES = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/GetPDIVehiclesBySalesperson?MakeId=-1&ModelId=-1&YearId=-1&TypeId=-1&salesPersonId=-1";

    public static final String URL_SALESPERSON_WITH_LOT_VEHICLES = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/GetLotVehiclesBySalesperson?MakeId=-1&ModelId=-1&YearId=-1&TypeId=-1&salesPersonId=-1";

    public static final String URL_ALERT_DETAILS = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/GetAlertsDetailsByAlertId?alertId=";

    public static final String URL_CLONE_ALERT = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/CloneAlert?alertId=";

    public static final String URL_DELETE_ALERT = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/UpdateAlertByAlertId?AlertId=";

    public static final String URL_VIOLATION_VEHICLES_OF_ALERT = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/GetViolatedVehiclesListByAlertId?alertId=";

    public static final String URL_ALERT_VEHICLES_DETAILS = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/GetVehiclesListByAlertId?alertId=";

    public static final String URL_VIOLATION_VEHICLES = URL + "warrous.ms.obdm/warrous.ms.obdm.api/api/Vehicle/GetViolatedVehicles";

    public static final String SUPER_ADMIN = "SuperAdmin";

    public static final String GENERAL_MANAGER = "GeneralManager";

    public static final String VEHICLE_SERVICE_TECHNICIAN = "VehicleServiceTechnician";

    public static final String SALES_PERSON = "SalesPerson";

    public static final String VEHICLE_SALES_MANAGER = "VehicleSalesManager";

    public static final String DEALER_DB = "dealer_db";

    public static final String DEALER_ID = "dealerid";

    public static final String RouteSource = "WaterFront Park, Sandiego, New York, USA";

    public static final String RouteDestination = "NationalCity, Sandiego, New York, USA";

    public static final String RouteSalesPerson = "Robert Key";

    public static final String TRACKING_TEST_DRIVE = "tracking_test_drive";

    public static final String TRIP_MASTER_ID = "trip_master_id";

    public static final String TRIP_START_TIME = "trip_start_time";

    public static final String TRIP_END_TIME = "trip_end_time";

    public static final String TRIP_DISTANCE = "trip_total_distance";

    public static final String TRIP_DATA = "trip_data";

    public static final String ON_LOT = "On Lot";

    public static final String TEST_DRIVE = "Test Drive";

    public static final String RETURN = "Return";

    public static final String PDI_INCOMPLETE = "PDI Incomplete";

    public static final String PDI_COMPLETE = "PDI Completed";

    public static final String MARK_FOR_PDI = "Marked for PDI";

    public static final String DELIVERY_RECEIVED = "Delivery Received";

    public static final String PREPARING_FOR_LOT = "Preparing for Lot";

    public static final String INVENTORY = "Inventory";

    public static final String TODAY = "Today";

    public static final String TOMORROW = "Tomorrow";

    public static final String NEXT_WEEK = "Next Week";

    public static final String TYPE = "type";

    public static final String SAMPLE_IMAGE = "http://images.dealer.com/ddc/vehicles/2017/Kia/Rio/Sedan/trim_LX_ed3585/color/Aurora%20Black%20Pearl-ABP-22%2C24%2C29-640-en_US.jpg";

    public static final  String SAMPLE_VEHICLE_DATA = "{\"VehicleId\":0,\"BodyStyle\":null,\"VehicleGuid\":\"9667e118-6a63-4948-8394-76d51c201217\",\"CustomerId\":0,\"DealerId\":0,\"MakeId\":0,\"MakemodelId\":0,\"ModelyearId\":0,\"TransitTruckId\":0,\"TransitTruckTripId\":0,\"Vin\":\"AF9937A0E50C4BC9A\",\"DeliveryDate\":null,\"FirstName\":null,\"LastName\":null,\"TruckName\":null,\"DriverName\":null,\"Latitude\":null,\"LicensePlateNo\":null,\"Origin\":null,\"Destination\":null,\"CurrentLocation\":null,\"Longitude\":null,\"Make\":\"KIA\",\"Model\":\"Sorento\",\"Year\":\"2018\",\"IsConnected\":false,\"Type\":\"PDI Incomplete\",\"VehicleDetails\":null,\"OdometerStatus\":0,\"TransmissionType\":null,\"DeviceId\":null,\"ConnectionStatus\":false,\"EnableStatus\":false,\"ExteriorColor\":null,\"InteriorColor\":null,\"IsActive\":false,\"EstimatedDelivery\":null,\"VehicleCount\":0,\"RouteId\":0,\"AssignedTo\":\"Steve Jorgan\",\"VehicleTypeId\":1,\"Role\":null,\"Status\":null,\"TestDriveTypeId\":0,\"Recived\":\"0001-01-01T00:00:00\",\"CustomerName\":null,\"DoorsQuantity\":null,\"EngineNumber\":null,\"TrimCode\":null}";

    public static final String SAMPLE_APP_OVERVIEW = "{\"FleetCount\":69,   \"LotCount\":125,   \"TestCount\":30,   \"PDICount\":4,   \"VehicleDeliveryCount\":7,   \"FindVehicleCount\":125,   \"MarkedCount\":0,   \"ZoneCount\":19,   \"VehicleCount\":0,   \"Type\":null,   \"TypeId\":0,   \"TestDriveTypeId\":0,   \"vehicleTypeCount\":[        {           \"FleetCount\":0,         \"LotCount\":0,         \"TestCount\":0,         \"PDICount\":0,         \"VehicleDeliveryCount\":0,         \"FindVehicleCount\":0,         \"MarkedCount\":0,         \"ZoneCount\":0,         \"VehicleCount\":39,         \"Type\":\"Delivery Received\",         \"TypeId\":7,         \"TestDriveTypeId\":0,         \"vehicleTypeCount\":null      },      {           \"FleetCount\":0,         \"LotCount\":0,         \"TestCount\":0,         \"PDICount\":0,         \"VehicleDeliveryCount\":0,         \"FindVehicleCount\":0,         \"MarkedCount\":0,         \"ZoneCount\":0,         \"VehicleCount\":1,         \"Type\":\"Inventory\",         \"TypeId\":1,         \"TestDriveTypeId\":0,         \"vehicleTypeCount\":null      },      {           \"FleetCount\":0,         \"LotCount\":0,         \"TestCount\":0,         \"PDICount\":0,         \"VehicleDeliveryCount\":0,         \"FindVehicleCount\":0,         \"MarkedCount\":0,         \"ZoneCount\":0,         \"VehicleCount\":3,         \"Type\":\"Marked for PDI\",         \"TypeId\":9,         \"TestDriveTypeId\":0,         \"vehicleTypeCount\":null      },      {           \"FleetCount\":0,         \"LotCount\":0,         \"TestCount\":0,         \"PDICount\":0,         \"VehicleDeliveryCount\":0,         \"FindVehicleCount\":0,         \"MarkedCount\":0,         \"ZoneCount\":0,         \"VehicleCount\":25,         \"Type\":\"PDI Incomplete\",         \"TypeId\":10,         \"TestDriveTypeId\":0,         \"vehicleTypeCount\":null      },      {           \"FleetCount\":0,         \"LotCount\":0,         \"TestCount\":0,         \"PDICount\":0,         \"VehicleDeliveryCount\":0,         \"FindVehicleCount\":0,         \"MarkedCount\":0,         \"ZoneCount\":0,         \"VehicleCount\":27,         \"Type\":\"Preparing for Lot\",         \"TypeId\":8,         \"TestDriveTypeId\":0,         \"vehicleTypeCount\":null      },      {           \"FleetCount\":0,         \"LotCount\":0,         \"TestCount\":0,         \"PDICount\":0,         \"VehicleDeliveryCount\":0,         \"FindVehicleCount\":0,         \"MarkedCount\":0,         \"ZoneCount\":0,         \"VehicleCount\":30,         \"Type\":\"On Lot\",         \"TypeId\":2,         \"TestDriveTypeId\":1,         \"vehicleTypeCount\":null      }   ]}";

}
