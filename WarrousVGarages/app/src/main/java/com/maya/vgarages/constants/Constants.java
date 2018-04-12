package com.maya.vgarages.constants;

import com.android.volley.Request;
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

    public static final String INVALID_OPERATION = "Invalid Operation";

    public static final int SECOND_MILLIS = 1000;

    public static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;

    public static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;

    public static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static final int POST_REQUEST = Request.Method.POST;

    public static final int GET_REQUEST = Request.Method.GET;

    public static final String FRAGMENT_KEY = "fragment_key";

    public static final String COLOR_CODES[] = {"#BA4141","#E07C5C","#00B3FF","#0069C9"};

    public static final String TAG_COLOR_CODES[] = {"#000000","#8B4513","#990000","#0069C9"};

    public static final String COLOR_START_CODES[] = {"#6ACAD3","#816CD4","#BEE05C","#f07C5C","#E8DF16"};

    public static final String COLOR_END_CODES[] = {"#4791BE","#BA4198","#41BA63","#BA4141","#F9D006"};

    public static final String PARKING_COLOR_START_CODES[] = {"#00B2FF","#816CD4","#BEE05C","#f07C5C","#E8DF16"};

    public static final String PARKING_COLOR_END_CODES[] = {"#0069C9","#BA4198","#41BA63","#BA4141","#F9D006"};

    public static final List<String> FB_PERMISSIONS = Arrays.asList("public_profile", "email");
    
    public static final String USER_ID = "user_id";

    public static final String LAST_NAME = "last_name";

    public static final String FIRST_NAME = "first_name";

    public static final String USER_EMAIL = "user_email";

    public static final String USER_NAME = "user_name";

    public static final String USER_PHOTO_URL = "user_photo_url";

    public static final String LOGIN = "login";

    public static final int IMAGE_PLACE_HOLDER = R.drawable.place_holder;

    public static final String SAMPLE_ERROR_IMAGE = "https://i.pinimg.com/originals/3e/3e/14/3e3e14d932098c3c3140c419daaa61c6.jpg";
}
