package SessionManager;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by GMSoft on 1/25/2017.
 */

public class seekbarsession {
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "bhim";

    // All Shared Preferences Keys

    // User name (make variable public to access from outside)
    public static  final String KEY_RANGE="range";
    // Email address (make variable public to access from outside)
    // public static final String KEY_EMAIL = "email";


    // Constructor
    public seekbarsession(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void createsession(String range) {

        editor.putString(KEY_RANGE, range);
        // commit changes
        editor.commit();
    }



    /**
     * Get stored session data
     */
    public HashMap<String, String> gettseekdetail() {
        HashMap<String, String> user1 = new HashMap<String, String>();
        // user name
        user1.put(KEY_RANGE, pref.getString(KEY_RANGE, null));


        // return user
        return user1;
    }

    /**
     * Clear session details
     */
    public void clear() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();


}
}
