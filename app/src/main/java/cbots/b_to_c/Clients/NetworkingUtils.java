package cbots.b_to_c.Clients;

public class NetworkingUtils {

    private static MainInterface userService;

    public static MainInterface getUserApiInstance() {
        if (userService == null)
            userService =  NetworkClient.getRetrofit().create(MainInterface.class);
        return userService;
    }
    
}