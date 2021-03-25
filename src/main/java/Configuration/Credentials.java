package Configuration;

import io.github.cdimascio.dotenv.Dotenv;

public class Credentials {
    public static final Dotenv dotenv = Dotenv.configure()
            .directory("src")
            .ignoreIfMalformed()
            .ignoreIfMissing()
            .load();

    public static final String _EMAIL = dotenv.get("EMAIL");
    public static final String _PASSWORD = dotenv.get("PASSWORD");
}
