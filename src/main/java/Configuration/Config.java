package Configuration;

public class Config extends Credentials {
    protected static final String EMAIL = _EMAIL;
    protected static final String PASSWORD = _PASSWORD;

    public static final String DRIVER_TYPE = "webdriver.chrome.driver";
    public static final String DRIVER_PATH = "src/driver/chromedriver.exe";

    public static final String SITE_URL = "https://www.linkedin.com/login";
    public static final String LOGOUT_URL = "https://www.linkedin.com/m/logout";
}
