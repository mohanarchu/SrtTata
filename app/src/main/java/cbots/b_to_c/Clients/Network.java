package cbots.b_to_c.Clients;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public  class Network {
    static Context context;
    public Network(Context context)
    {
        this.context = context;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)context. getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
