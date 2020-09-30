package com.livetv.normal.utils.networking;

public class WebConfig {
    public static final String GetCodeURL = "https://superteve.com/ggG9GXRJ8nconGQd2/Connect/loginCode.php?request_code";
    public static final String LoginCodeURL = "https://superteve.com/ggG9GXRJ8nconGQd2/Connect/loginCode.php?dealer_code={CODE}";
    public static final String baseURL = "https://superteve.com/RFst4v3WoGvtk7fqMO/";
    public static final String deleteMessageURL = "https://superteve.com/ggG9GXRJ8nconGQd2/Connect/tracking.php?username={USER}&seen";
    private static final String domain = "https://superteve.com";
    public static final String liveTVCategoriesURL = "https://superteve.com/ggG9GXRJ8nconGQd2/Connect/live_categorias.php";
    public static final String liveTVChannelsURL = "https://superteve.com/ggG9GXRJ8nconGQd2/Connect/live_canales.php?cve={CAT_ID}";
    public static final String loginURL = "https://superteve.com/ggG9GXRJ8nconGQd2/Connect/login.php?user={USER}&pass={PASS}&device_id={DEVICE_ID}&model={MODEL}&fw={FW}&country={COUNTRY}";
    public static final String LoginSplash = "https://superteve.com/ggG9GXRJ8nconGQd2/Connect/login.php?user={USER}&pass={PASS}&device_id={DEVICE_ID}&splash";
    public static final String removeUserURL = "https://superteve.com/ggG9GXRJ8nconGQd2/Connect/login.php?user={USER}&delete";
    public static final String setPassCodeURL = "https://superteve.com/ggG9GXRJ8nconGQd2/Connect/loginCode.php?password_code={CODE}&user={USER}&pass={PASS}";
    public static final String trackingURL = "https://superteve.com/ggG9GXRJ8nconGQd2/Connect/tracking.php?username={USER}&movie={MOVIE}";
    public static final String updateURL = "https://superteve.com/ggG9GXRJ8nconGQd2/Connect/upgrade_version.php?new_version";
    public static final String videoSearchURL = "https://superteve.com/ggG9GXRJ8nconGQd2/Connect/searchVideo.php?type={TYPE}&pattern={PATTERN}";
    public static final String addRecent = baseURL + "view_recent.php?tipo={TIPO}&user={USER}&cve={CVE}";

}
