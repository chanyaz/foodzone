package com.maya.wcustomer.constants;

import com.android.volley.Request;

/**
 * Created by Gokul Kalagara on 3/13/2018.
 */

public class Constants
{

    public static final String CONNECTION_ERROR="Network Error";

    public static final String PLEASE_CHECK_INTERNET="Please Check Internet";

    public static final String PREFS ="com.maya.wcustomer";

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

    public static final String COLOR_START_CODES[] = {"#6ACAD3","#816CD4","#BEE05C","#f07C5C","#E8DF16"};

    public static final String COLOR_END_CODES[] = {"#4791BE","#BA4198","#41BA63","#BA4141","#F9D006"};

    public static final String PARKING_COLOR_START_CODES[] = {"#00B2FF","#816CD4","#BEE05C","#f07C5C","#E8DF16"};

    public static final String PARKING_COLOR_END_CODES[] = {"#0069C9","#BA4198","#41BA63","#BA4141","#F9D006"};

    public static final String USER_ID = "user_id";

    public static final String LAST_NAME = "last_name";

    public static final String FIRST_NAME = "first_name";

    public static final String USER_EMAIL = "user_email";

    public static final String USER_NAME = "user_name";

    public static final String USER_PHOTO_URL = "user_photo_url";

    public static final String USER_FCM_TOKEN = "user_fcm_token";

    public static final String LOGIN = "login";

    public static final String AVATAR_IMAGE = "https://ssl.gstatic.com/images/branding/product/1x/avatar_circle_blue_512dp.png";

    public static final String SAMPLE_TRIP_POINTS = "[{\"tripId\":\"1439\",\"lat\":17.43713077,\"lng\":78.39373409,\"bearing\":92.699996948242188,\"speed\":93.0,\"rpm\":174.0,\"isSaved\":false},\r\n{\"tripId\":\"1439\",\"lat\":17.43715858,\"lng\":78.39374362,\"bearing\":0.0,\"speed\":37.0,\"rpm\":104.0,\"isSaved\":false},\r\n{\"tripId\":\"1439\",\"lat\":17.43717607,\"lng\":78.3937369,\"bearing\":50.5,\"speed\":101.0,\"rpm\":175.0,\"isSaved\":false},\r\n{\"tripId\":\"1439\",\"lat\":17.43720057,\"lng\":78.39375059,\"bearing\":23.100000381469727,\"speed\":102.0,\"rpm\":177.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43725081,\"lng\":78.39377951,\"bearing\":31.299999237060547,\"speed\":28.0,\"rpm\":94.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43734728,\"lng\":78.39381285,\"bearing\":19.600000381469727,\"speed\":56.0,\"rpm\":125.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43742497,\"lng\":78.39379956,\"bearing\":1.7000000476837158,\"speed\":77.0,\"rpm\":148.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43747729,\"lng\":78.3938392,\"bearing\":50.400001525878906,\"speed\":82.0,\"rpm\":155.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43752959,\"lng\":78.39388833,\"bearing\":64.5999984741211,\"speed\":58.0,\"rpm\":127.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43746519,\"lng\":78.39393238,\"bearing\":145.10000610351563,\"speed\":71.0,\"rpm\":142.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43745329,\"lng\":78.3940473,\"bearing\":56.0,\"speed\":92.0,\"rpm\":165.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43744573,\"lng\":78.39411317,\"bearing\":111.69999694824219,\"speed\":26.0,\"rpm\":91.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43740452,\"lng\":78.3941831,\"bearing\":120.30000305175781,\"speed\":25.0,\"rpm\":90.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43737512,\"lng\":78.3942531,\"bearing\":115.90000152587891,\"speed\":81.0,\"rpm\":153.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43734296,\"lng\":78.39432915,\"bearing\":120.5,\"speed\":83.0,\"rpm\":156.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43730874,\"lng\":78.39439029,\"bearing\":116.30000305175781,\"speed\":104.0,\"rpm\":179.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43729205,\"lng\":78.39446316,\"bearing\":89.0999984741211,\"speed\":31.0,\"rpm\":144.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43731976,\"lng\":78.39453091,\"bearing\":51.099998474121094,\"speed\":83.0,\"rpm\":155.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43736828,\"lng\":78.39456154,\"bearing\":22.5,\"speed\":51.0,\"rpm\":119.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43744278,\"lng\":78.39460755,\"bearing\":29.899999618530273,\"speed\":68.0,\"rpm\":96.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43751273,\"lng\":78.39468293,\"bearing\":55.299999237060547,\"speed\":78.0,\"rpm\":150.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.4375681,\"lng\":78.39471541,\"bearing\":14.199999809265137,\"speed\":88.0,\"rpm\":161.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43769769,\"lng\":78.39475041,\"bearing\":14.199999809265137,\"speed\":29.0,\"rpm\":94.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43774673,\"lng\":78.39476206,\"bearing\":60.700000762939453,\"speed\":77.0,\"rpm\":149.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43778819,\"lng\":78.39479959,\"bearing\":60.799999237060547,\"speed\":42.0,\"rpm\":109.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43779972,\"lng\":78.39485321,\"bearing\":85.0999984741211,\"speed\":94.0,\"rpm\":125.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43779288,\"lng\":78.39495956,\"bearing\":98.9000015258789,\"speed\":104.0,\"rpm\":179.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43780046,\"lng\":78.39508334,\"bearing\":78.5999984741211,\"speed\":34.0,\"rpm\":100.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.4378322,\"lng\":78.39514655,\"bearing\":82.5999984741211,\"speed\":47.0,\"rpm\":115.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43778839,\"lng\":78.39518616,\"bearing\":119.19999694824219,\"speed\":61.0,\"rpm\":131.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43770965,\"lng\":78.39523858,\"bearing\":114.80000305175781,\"speed\":29.0,\"rpm\":95.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43762095,\"lng\":78.39534707,\"bearing\":91.5999984741211,\"speed\":47.0,\"rpm\":161.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43764584,\"lng\":78.39537708,\"bearing\":41.299999237060547,\"speed\":26.0,\"rpm\":91.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43771327,\"lng\":78.39540074,\"bearing\":23.600000381469727,\"speed\":36.0,\"rpm\":102.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43779696,\"lng\":78.39540159,\"bearing\":4.3000001907348633,\"speed\":39.0,\"rpm\":106.0,\"isSaved\":false},\r\n{\"tripId\":\"1439\",\"lat\":17.43787255,\"lng\":78.39540196,\"bearing\":11.100000381469727,\"speed\":49.0,\"rpm\":117.0,\"isSaved\":false},\r\n{\"tripId\":\"1439\",\"lat\":17.437947,\"lng\":78.39541368,\"bearing\":16.200000762939453,\"speed\":101.0,\"rpm\":132.0,\"isSaved\":false},\r\n{\"tripId\":\"1439\",\"lat\":17.4380152,\"lng\":78.39542918,\"bearing\":20.899999618530273,\"speed\":31.0,\"rpm\":97.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43808029,\"lng\":78.39545794,\"bearing\":25.700000762939453,\"speed\":79.0,\"rpm\":151.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43813459,\"lng\":78.39548647,\"bearing\":21.600000381469727,\"speed\":93.0,\"rpm\":167.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43819597,\"lng\":78.39552304,\"bearing\":32.0,\"speed\":103.0,\"rpm\":178.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43827143,\"lng\":78.39557454,\"bearing\":26.399999618530273,\"speed\":75.0,\"rpm\":146.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43834856,\"lng\":78.39561984,\"bearing\":24.399999618530273,\"speed\":43.0,\"rpm\":158.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43840445,\"lng\":78.39567112,\"bearing\":35.0,\"speed\":92.0,\"rpm\":122.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43846916,\"lng\":78.39570743,\"bearing\":14.600000381469727,\"speed\":25.0,\"rpm\":91.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43851872,\"lng\":78.39572374,\"bearing\":21.5,\"speed\":32.0,\"rpm\":98.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43858159,\"lng\":78.39575895,\"bearing\":33.599998474121094,\"speed\":46.0,\"rpm\":113.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43864543,\"lng\":78.39578519,\"bearing\":28.200000762939453,\"speed\":56.0,\"rpm\":125.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43871334,\"lng\":78.39579429,\"bearing\":22.600000381469727,\"speed\":62.0,\"rpm\":132.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43877855,\"lng\":78.39580887,\"bearing\":24.0,\"speed\":34.0,\"rpm\":100.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43884817,\"lng\":78.39582918,\"bearing\":21.100000381469727,\"speed\":75.0,\"rpm\":147.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43890978,\"lng\":78.39585881,\"bearing\":26.0,\"speed\":89.0,\"rpm\":162.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43896959,\"lng\":78.39589095,\"bearing\":20.799999237060547,\"speed\":61.0,\"rpm\":131.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43903411,\"lng\":78.39592316,\"bearing\":24.399999618530273,\"speed\":33.0,\"rpm\":99.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43909957,\"lng\":78.39594909,\"bearing\":20.299999237060547,\"speed\":43.0,\"rpm\":110.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43916456,\"lng\":78.39597739,\"bearing\":21.899999618530273,\"speed\":91.0,\"rpm\":165.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43923542,\"lng\":78.39601065,\"bearing\":29.799999237060547,\"speed\":98.0,\"rpm\":172.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43930428,\"lng\":78.39605902,\"bearing\":34.700000762939453,\"speed\":70.0,\"rpm\":98.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43935567,\"lng\":78.39609732,\"bearing\":20.100000381469727,\"speed\":38.0,\"rpm\":105.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.4394015,\"lng\":78.39609821,\"bearing\":337.89999389648438,\"speed\":48.0,\"rpm\":116.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43946296,\"lng\":78.39604482,\"bearing\":320.39999389648438,\"speed\":100.0,\"rpm\":175.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43951472,\"lng\":78.3959785,\"bearing\":319.60000610351563,\"speed\":72.0,\"rpm\":100.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43955308,\"lng\":78.39591266,\"bearing\":310.89999389648438,\"speed\":79.0,\"rpm\":150.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43958188,\"lng\":78.39585063,\"bearing\":310.29998779296875,\"speed\":89.0,\"rpm\":162.0,\"isSaved\":false},\r\n{\"tripId\":\"1439\",\"lat\":17.43961828,\"lng\":78.39579607,\"bearing\":314.79998779296875,\"speed\":64.0,\"rpm\":134.0,\"isSaved\":false},\r\n{\"tripId\":\"1439\",\"lat\":17.4396637,\"lng\":78.39575392,\"bearing\":335.70001220703125,\"speed\":67.0,\"rpm\":138.0,\"isSaved\":false},\r\n{\"tripId\":\"1439\",\"lat\":17.4397254,\"lng\":78.39574478,\"bearing\":3.2000000476837158,\"speed\":81.0,\"rpm\":153.0,\"isSaved\":false},\r\n{\"tripId\":\"1439\",\"lat\":17.43976261,\"lng\":78.39570732,\"bearing\":297.20001220703125,\"speed\":53.0,\"rpm\":121.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43974908,\"lng\":78.39564613,\"bearing\":249.80000305175781,\"speed\":59.0,\"rpm\":129.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.4396893,\"lng\":78.39558628,\"bearing\":216.19999694824219,\"speed\":31.0,\"rpm\":97.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43961634,\"lng\":78.39554926,\"bearing\":206.5,\"speed\":80.0,\"rpm\":152.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43954128,\"lng\":78.39551739,\"bearing\":203.89999389648438,\"speed\":48.0,\"rpm\":116.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43947888,\"lng\":78.39548327,\"bearing\":216.19999694824219,\"speed\":100.0,\"rpm\":131.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43943134,\"lng\":78.39542895,\"bearing\":242.19999694824219,\"speed\":30.0,\"rpm\":96.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43939691,\"lng\":78.39539092,\"bearing\":225.0,\"speed\":78.0,\"rpm\":150.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43934059,\"lng\":78.39537467,\"bearing\":195.89999389648438,\"speed\":54.0,\"rpm\":123.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.4392775,\"lng\":78.39535739,\"bearing\":213.10000610351563,\"speed\":102.0,\"rpm\":177.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43920262,\"lng\":78.39532539,\"bearing\":207.5,\"speed\":29.0,\"rpm\":94.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.4391187,\"lng\":78.39528587,\"bearing\":204.10000610351563,\"speed\":81.0,\"rpm\":153.0,\"isSaved\":false},\r\n{\"tripId\":\"1439\",\"lat\":17.43906097,\"lng\":78.3952476,\"bearing\":215.10000610351563,\"speed\":56.0,\"rpm\":125.0,\"isSaved\":false},\r\n{\"tripId\":\"1439\",\"lat\":17.4390094,\"lng\":78.39520867,\"bearing\":214.89999389648438,\"speed\":101.0,\"rpm\":176.0,\"isSaved\":false},\r\n{\"tripId\":\"1439\",\"lat\":17.43895274,\"lng\":78.39519139,\"bearing\":202.69999694824219,\"speed\":84.0,\"rpm\":156.0,\"isSaved\":false},\r\n{\"tripId\":\"1439\",\"lat\":17.43888622,\"lng\":78.39518514,\"bearing\":199.60000610351563,\"speed\":79.0,\"rpm\":151.0,\"isSaved\":false},\r\n{\"tripId\":\"1439\",\"lat\":17.438818,\"lng\":78.39517012,\"bearing\":202.69999694824219,\"speed\":90.0,\"rpm\":120.0,\"isSaved\":false},\r\n{\"tripId\":\"1439\",\"lat\":17.43875503,\"lng\":78.39516087,\"bearing\":188.0,\"speed\":58.0,\"rpm\":127.0,\"isSaved\":false},\r\n{\"tripId\":\"1439\",\"lat\":17.43867763,\"lng\":78.39513946,\"bearing\":188.10000610351563,\"speed\":33.0,\"rpm\":99.0,\"isSaved\":false},\r\n{\"tripId\":\"1439\",\"lat\":17.43859964,\"lng\":78.39507757,\"bearing\":202.10000610351563,\"speed\":61.0,\"rpm\":130.0,\"isSaved\":false},\r\n{\"tripId\":\"1439\",\"lat\":17.43853913,\"lng\":78.39506811,\"bearing\":189.19999694824219,\"speed\":60.0,\"rpm\":130.0,\"isSaved\":false},\r\n{\"tripId\":\"1439\",\"lat\":17.43849278,\"lng\":78.39503501,\"bearing\":234.39999389648438,\"speed\":50.0,\"rpm\":118.0,\"isSaved\":false},\r\n{\"tripId\":\"1439\",\"lat\":17.4384514,\"lng\":78.39495479,\"bearing\":251.69999694824219,\"speed\":57.0,\"rpm\":126.0,\"isSaved\":false},\r\n{\"tripId\":\"1439\",\"lat\":17.43842408,\"lng\":78.39488212,\"bearing\":256.20001220703125,\"speed\":91.0,\"rpm\":164.0,\"isSaved\":false},\r\n{\"tripId\":\"1439\",\"lat\":17.4384409,\"lng\":78.39479323,\"bearing\":281.89999389648438,\"speed\":62.0,\"rpm\":132.0,\"isSaved\":false},\r\n{\"tripId\":\"1439\",\"lat\":17.43845859,\"lng\":78.3947193,\"bearing\":272.60000610351563,\"speed\":76.0,\"rpm\":148.0,\"isSaved\":false},\r\n{\"tripId\":\"1439\",\"lat\":17.43846771,\"lng\":78.39466713,\"bearing\":273.60000610351563,\"speed\":86.0,\"rpm\":159.0,\"isSaved\":false},\r\n{\"tripId\":\"1439\",\"lat\":17.43852037,\"lng\":78.39457859,\"bearing\":289.89999389648438,\"speed\":54.0,\"rpm\":123.0,\"isSaved\":false},\r\n{\"tripId\":\"1439\",\"lat\":17.43858267,\"lng\":78.39450154,\"bearing\":298.29998779296875,\"speed\":68.0,\"rpm\":138.0,\"isSaved\":false},\r\n{\"tripId\":\"1439\",\"lat\":17.43863147,\"lng\":78.39438618,\"bearing\":253.30000305175781,\"speed\":89.0,\"rpm\":162.0,\"isSaved\":false},\r\n{\"tripId\":\"1439\",\"lat\":17.438588,\"lng\":78.3943399,\"bearing\":216.5,\"speed\":57.0,\"rpm\":126.0,\"isSaved\":false},\r\n{\"tripId\":\"1439\",\"lat\":17.43852658,\"lng\":78.3943272,\"bearing\":202.30000305175781,\"speed\":25.0,\"rpm\":90.0,\"isSaved\":false},\r\n{\"tripId\":\"1439\",\"lat\":17.4384367,\"lng\":78.39432213,\"bearing\":199.10000610351563,\"speed\":69.0,\"rpm\":100.0,\"isSaved\":false},\r\n{\"tripId\":\"1439\",\"lat\":17.43837558,\"lng\":78.39431584,\"bearing\":202.60000610351563,\"speed\":36.0,\"rpm\":102.0,\"isSaved\":false},\r\n{\"tripId\":\"1439\",\"lat\":17.43830676,\"lng\":78.39429037,\"bearing\":211.39999389648438,\"speed\":46.0,\"rpm\":113.0,\"isSaved\":false},\r\n{\"tripId\":\"1439\",\"lat\":17.43824635,\"lng\":78.39424671,\"bearing\":222.0,\"speed\":25.0,\"rpm\":90.0,\"isSaved\":false},\r\n{\"tripId\":\"1439\",\"lat\":17.4381846,\"lng\":78.39419139,\"bearing\":220.30000305175781,\"speed\":104.0,\"rpm\":179.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43813905,\"lng\":78.39412779,\"bearing\":234.60000610351563,\"speed\":80.0,\"rpm\":152.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43811567,\"lng\":78.3940529,\"bearing\":257.20001220703125,\"speed\":79.0,\"rpm\":108.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43812668,\"lng\":78.39398022,\"bearing\":285.70001220703125,\"speed\":93.0,\"rpm\":166.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43816757,\"lng\":78.39390488,\"bearing\":296.5,\"speed\":99.0,\"rpm\":174.0,\"isSaved\":false},\r\n{\"tripId\":\"1439\",\"lat\":17.43819781,\"lng\":78.39383528,\"bearing\":293.89999389648438,\"speed\":33.0,\"rpm\":99.0,\"isSaved\":false},\r\n{\"tripId\":\"1439\",\"lat\":17.43821783,\"lng\":78.39377607,\"bearing\":294.79998779296875,\"speed\":50.0,\"rpm\":119.0,\"isSaved\":false},\r\n{\"tripId\":\"1439\",\"lat\":17.43825375,\"lng\":78.39370316,\"bearing\":295.89999389648438,\"speed\":57.0,\"rpm\":126.0,\"isSaved\":false},\r\n{\"tripId\":\"1439\",\"lat\":17.43830818,\"lng\":78.39362425,\"bearing\":297.60000610351563,\"speed\":66.0,\"rpm\":137.0,\"isSaved\":false},\r\n{\"tripId\":\"1439\",\"lat\":17.43831722,\"lng\":78.39354684,\"bearing\":257.10000610351563,\"speed\":49.0,\"rpm\":117.0,\"isSaved\":false},\r\n{\"tripId\":\"1439\",\"lat\":17.43826603,\"lng\":78.39353212,\"bearing\":205.30000305175781,\"speed\":63.0,\"rpm\":133.0,\"isSaved\":false},\r\n{\"tripId\":\"1439\",\"lat\":17.43818403,\"lng\":78.39349552,\"bearing\":212.30000305175781,\"speed\":38.0,\"rpm\":105.0,\"isSaved\":false},\r\n{\"tripId\":\"1439\",\"lat\":17.43809766,\"lng\":78.39346162,\"bearing\":202.89999389648438,\"speed\":76.0,\"rpm\":147.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43802972,\"lng\":78.39345945,\"bearing\":194.69999694824219,\"speed\":86.0,\"rpm\":159.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43795942,\"lng\":78.39344681,\"bearing\":196.19999694824219,\"speed\":89.0,\"rpm\":162.0,\"isSaved\":false},\r\n {\"tripId\":\"1439\",\"lat\":17.43789161,\"lng\":78.3934215,\"bearing\":204.89999389648438,\"speed\":26.0,\"rpm\":92.0,\"isSaved\":false},\r\n{\"tripId\":\"1439\",\"lat\":17.43783687,\"lng\":78.3934131,\"bearing\":198.30000305175781,\"speed\":78.0,\"rpm\":150.0,\"isSaved\":false},\r\n{\"tripId\":\"1439\",\"lat\":17.43779073,\"lng\":78.39339877,\"bearing\":191.0,\"speed\":96.0,\"rpm\":169.0,\"isSaved\":false},\r\n{\"tripId\":\"1439\",\"lat\":17.43775794,\"lng\":78.39338547,\"bearing\":185.0,\"speed\":57.0,\"rpm\":126.0,\"isSaved\":false},\r\n{\"tripId\":\"1439\",\"lat\":17.43774967,\"lng\":78.39335024,\"bearing\":0.0,\"speed\":66.0,\"rpm\":136.0,\"isSaved\":false}]";




}