package com.maya.vgarages.constants;

import com.android.volley.Request;
import com.google.android.gms.maps.model.LatLng;
import com.maya.vgarages.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Gokul Kalagara on 3/13/2018.
 */

public class Constants
{

    public static final String CONNECTION_ERROR="Network Error";

    public static final String PLEASE_CHECK_INTERNET="Please Check Internet";

    public static final String PREFS ="com.maya.vgarage";

    public static final String SOMETHING_WENT_WRONG = "Something Went Wrong";

    public static final String ERROR= "Error";

    public static final String REJECTED = "Rejected";

    public static final String ACCEPTED = "Accepted";

    public static final String INVALID_OPERATION = "Invalid Operation";

    public static final int SECOND_MILLIS = 1000;

    public static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;

    public static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;

    public static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static final int POST_REQUEST = Request.Method.POST;

    public static final int GET_REQUEST = Request.Method.GET;

    public static final String FRAGMENT_KEY = "fragment_key";

    public static final int LOCATION_CODE = 10101;

    public static final String COLOR_CODES[] = {"#BA4141","#E07C5C","#00B3FF","#0069C9"};

    public static final String TAG_COLOR_CODES[] = {"#000000","#8B4513","#990000","#0069C9"};

    public static final String COLOR_START_CODES[] = {"#6ACAD3","#816CD4","#BEE05C","#f07C5C","#E8DF16"};

    public static final String COLOR_END_CODES[] = {"#4791BE","#BA4198","#41BA63","#BA4141","#F9D006"};

    public static final String PARKING_COLOR_START_CODES[] = {"#00B2FF","#816CD4","#BEE05C","#f07C5C","#E8DF16"};

    public static final String PARKING_COLOR_END_CODES[] = {"#0069C9","#BA4198","#41BA63","#BA4141","#F9D006"};

    public static final List<String> FB_PERMISSIONS = Arrays.asList("public_profile", "email");
    
    public static final String USER_ID = "user_id";

    public static final int SAMPLE_USER_ID = 3;

    public static final String ACCESS_TOKEN = "access_token";

    public static final String EXPIRES_IN = "expires_in";

    public static final String TOKEN_TYPE = "token_type";

    public static final String LAST_NAME = "last_name";

    public static final String FIRST_NAME = "first_name";

    public static final String USER_EMAIL = "user_email";

    public static final String USER_NAME = "user_name";

    public static final String USER_PHONE_NUMBER = "user_phone_number";

    public static final String USER_PHOTO_URL = "user_photo_url";

    public static final String USER_ADDRESS = "user_address";

    public static final String USER_COMPLETE_ADDRESS = "user_complete_address";

    public static final String USER_LOCALITY_ADDRESS = "user_locality_address";

    public static final String USER_ADMIN_AREA = "user_admin_area";

    public static final String USER_PIN_CODE = "user_pin_code";

    public static final String USER_ADDRESS1 = "user_address1";

    public static final String USER_ADDRESS2 = "user_address2";

    public static final String LOGIN = "login";

    public static final String CART_DATA = "cart_data";

    public static final String DEFAULT_CAR_DATA = "DEFAULT_CAR_DATA";

    public static final String VGARAGE_SERVICES = "vgarage_services";

    public static final String USER_FCM_TOKEN = "user_fcm_token";

    public static final String AUTH_TOKEN = "auth_token";

    public static final String CURRENT_USER_FCM_TOKEN = "current_user_fcm_token";

    public static final int IMAGE_PLACE_HOLDER = R.drawable.place_holder;

    public static final String AVATAR_IMAGE = "https://ssl.gstatic.com/images/branding/product/1x/avatar_circle_blue_512dp.png";

    public static final String SAMPLE_ERROR_IMAGE = "https://i.pinimg.com/originals/3e/3e/14/3e3e14d932098c3c3140c419daaa61c6.jpg";

    public static final String SAMPLE_CUSTOMER_PHONE_NUMBER = "1234567890";

    public static final String SERIAL_PROJECT_ID = "509471007783";

    public static final int SUCCESS_RESULT = 0;

    public static final int FAILURE_RESULT = 1;

    public static final String PACKAGE_NAME = "com.maya.vgarages";

    public static final String RECEIVER = PACKAGE_NAME + ".AddressReceiver";

    public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";

    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";

    public static final int RUPEE_PAISA = 100;

    public static final String URL_APP_LOGO = "http://devassets.warrous.com/Mobile%20Assets/png/app_logo.png";

    public static final String URL = "http://13.59.34.59:8080/";
    //main url

    public static final String URL_GET_GARAGES_LIST_BY_TYPE = URL + "warrous.ms.vgarage/warrous.ms.vgarage.api/api/Garage/GetGaragesByLatLongnType";
    //?latitude=17.473042 &longitude=78.562382 &GarageType=General%20Service &pagecount=1

    public static final String URL_GET_GARAGES_SERVICES = URL + "warrous.ms.vgarage/warrous.ms.vgarage.api/api/Garage/GetServicesByDealerId";
    //?dealerId=1&pageCount=1

    public static final String URL_GET_USER_BOOKMARK_GARAGES = URL + "warrous.ms.vgarage/warrous.ms.vgarage.api/api/Garage/BookmarksByUserIdGet";
    //?userId=3 &pageCount=1 &latitude=17.473042 &longitude=78.562382";

    public static final String URL_GET_REVIEW_OF_GARAGES = URL + "warrous.ms.vgarage/warrous.ms.vgarage.api/api/Appointment/GetDealerReviews";
    //warrous.ms.vgarage/warrous.ms.vgarage.api/api/Appointment/GetDealerReviews
    // ?dealerId=
    //warrous.ms.vgarage/warrous.ms.vgarage.api/api/Garage/ReviewsByDealerIdGet
    //?dealerId=1&pageCount=1";

    public static final String URL_FOR_SEARCH_GARAGES = URL + "/warrous.ms.vgarage/warrous.ms.vgarage.api/api/Garage/GetGaragesByLatLongnsearchparm";
    //?latitude=17.473042&longitude=78.562382&searchParm=sri&pagecount=1";

    public static final String URL_LOGIN = URL + "warrous.ms.auth/warrous.ms.vgarage_auth.api/connect/token";
    //username & password post

    public static final String URL_REGISTER = URL + "warrous.ms.vgarage/warrous.ms.vgarage.api/api/Admin/Register";
    //?

    public static final String URL_GARAGE_SERVICE_TYPES = URL + "warrous.ms.vgarage/warrous.ms.vgarage.api/api/Garage/GetGarageTypes";
    //?pageCount=1";

    public static final String URL_RECOMMENDED_GARAGES = URL + "warrous.ms.vgarage/warrous.ms.vgarage.api/api/Garage/GetRecomendedGaragesByLatLongnType";
    //?latitude=17.473042&longitude=78.562382&GarageType=all&pagecount=1";

    public static final String URL_ADD_REVIEW = URL + "warrous.ms.vgarage/warrous.ms.vgarage.api/api/Garage/SaveReview";
    //?dealerId=1&userId=1&review=testreview&rating=8&isEdit=0";

    public static final String URL_ADD_REMOVE_BOOKMARK = URL + "warrous.ms.vgarage/warrous.ms.vgarage.api/api/Garage/SaveBookmark";
    //?dealerId=1&userId=1&isBookmark=1";

    public static final String URL_GET_IS_BOOKMARK = URL + "warrous.ms.vgarage/warrous.ms.vgarage.api/api/Garage/IsBookMarkByDealerIdGet";
    //?dealerid=10&user_id=7"

    public static final String URL_GET_GARAGE_IMAGES = URL + "warrous.ms.vgarage/warrous.ms.vgarage.api/api/Garage/MediaByDealerIdGet";
    //?dealerId=1&pageCount=1";

    public static final String URL_GET_MAKES = URL + "warrous.ms.vgarage/warrous.ms.vgarage.api/api/Garage/GetVehicleMakes";
    //?

    public static final String URL_GET_MAKE_MODELS = URL + "warrous.ms.vgarage/warrous.ms.vgarage.api/api/Garage/GetVehicleModels";
    //?make=Acura";

    public static final String URL_GET_MODEL_YEARS = URL + "warrous.ms.vgarage/warrous.ms.vgarage.api/api/Garage/GetVehicleYear";
    //?model=ILX";

    public static final String URL_SAVE_VEHICLE = URL + "warrous.ms.vgarage/warrous.ms.vgarage.api/api/Garage/SaveVehicleByUserId";
    //?userId=1&makeModelYearId=484&vehicleName=Test";

    public static final String URL_DELETE_VEHICLE  = URL + "warrous.ms.vgarage/warrous.ms.vgarage.api/api/Garage/VehicleByVehicleIdDelete";
    //?userId=1&vehicleId=1";

    public static final String URL_USER_VEHICLES = URL + "warrous.ms.vgarage/warrous.ms.vgarage.api/api/Garage/VehiclesByUseridGet";

    public static final String URL_INSERT_USER_PNS = URL + "warrous.ms.vgarage/warrous.ms.vgarage.api/api/Garage/CustomerPNSInsert";
    //?userId=1";

    public static final String URL_USER_CART_OPCODES = URL + "warrous.ms.vgarage/warrous.ms.vgarage.api/api/Garage/CartOpcodesByUserIdGet";
    //?userId=7";

    public static final String URL_INSERT_CART_OPCODE = URL + "warrous.ms.vgarage/warrous.ms.vgarage.api/api/Garage/OpCodeCartSave";
    //?opCodeId=6&userId=7&dealerId=13 ";

    public static final String URL_DELETE_CART_OPCODE = URL  + "warrous.ms.vgarage/warrous.ms.vgarage.api/api/Garage/CartOpcodesByUserIdDelete";
    //?dealerId=13&userId=7&opCodeId=2";

    public static final String URL_GET_DEALER_DETAILS = URL + "warrous.ms.vgarage/warrous.ms.vgarage.api/api/Garage/GaragesDetailsByDealerIdGet";
    //?dealerId=10"

    public static final String URL_CREATE_APPOINTMENT = URL + "warrous.ms.vgarage/warrous.ms.vgarage.api/api/Appointment/CreateAppointment";
    //dealerId=1&opCodes=1,2
    /*
        {
        "ServiceAppointmentId": 0,
        "VehicleId": 38,
        "ApptDate": "2018-05-09",
        "ApptTime": "10:28:43",
        "OdometerStatus": 453,
        "OpCount": 2,
        "ServiceAdvisorId": 1,
        "IsWait": false,
        "TransportationOptionId": 1,
        "CustomerId": 7,
        "PhoneNumber": "9440408107",
        "YearId":485,
        "AppointmentTypeId": 1
        }
     */

    public static final String URL_USER_ADDRESS = URL + "warrous.ms.vgarage/warrous.ms.vgarage.api/api/Garage/GetUserAddress";
    //?userId=7";

    public static final String URL_SAVE_USER_ADDRESS = URL + "warrous.ms.vgarage/warrous.ms.vgarage.api/api/Garage/SaveUserAddress";
    /*
    Address1=HSR Layout
    Address2=Bengaluru
    City=Hydrabad
    PinCode=587412
    State=Karnataka
    UserId=23
    */

    public static final String URL_APPOINTMENT_DETAILS = URL + "warrous.ms.vgarage/warrous.ms.vgarage.api/api/Appointment/GetAppointmentInfo";
    //?serviceAppointmentId=44"

    public static final String URL_USER_APPOINTMENTS = URL + "warrous.ms.vgarage/warrous.ms.vgarage.api/api/Appointment/AppointmentsByUserIdGet";
    //?userId=7";

    public static final String URL_CANCEL_APPOINTMENT = URL + "warrous.ms.vgarage/warrous.ms.vgarage.api/api/Appointment/AppointmentByServiceAppointmentIdUpdate";
    //?serviceAppointmentId=' + serviceAppointmentId + '&appointmentType=' + 'Rejected'+ '&rejectReason=' + 'User Cancelled'";

    public static final String URL_RESCHEDULE_APPOINTMENT = URL + "warrous.ms.vgarage/warrous.ms.vgarage.api/api/Appointment/AppointmentByDatetimeUpdate";
    //?serviceAppointmentId=' + serviceAppointmentId + '&apptDate=' + apptDate + '&apptTime=' + apptTime'
}
