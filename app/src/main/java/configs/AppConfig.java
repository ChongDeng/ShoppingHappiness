package configs;

public class AppConfig {
//	public static String restful_url_prefix = "http://192.168.1.88/ShoppingBackend/";
    public static String restful_url_prefix = "http://192.168.1.88/ShoppingBackend/";
	// Server user login url
	public static String URL_LOGIN = restful_url_prefix + "login.php";

	// Server user register url
	public static String URL_REGISTER = restful_url_prefix + "register.php";

	//merchandise cover url
	public static String URL_MERCHANDISE_COVER = restful_url_prefix + "images/merchandise_cover.jpg";

	public static final String URL_MERCHANDISE_LIST = restful_url_prefix + "json/merchandise_info.json";

	public static final String URL_MERCHANDISE_Review = restful_url_prefix + "json/merchandise_review.json";

	public static final String URL_HOME_PAGE_DATA = restful_url_prefix + "json/home_page_data.json";
}
