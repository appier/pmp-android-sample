package com.appier.android.sample.helper;

import com.appier.ads.Appier;

public class AppierAdHelper {

    public static void setAppierGlobal() {

        // Enable test mode
        Appier.setTestMode(Appier.TestMode.BID);

        // TODO: Setup Appier Global vars, include GDPR,... etc.

    }

}
