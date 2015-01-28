package com.github.donkirkby.vograbulary.client;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.github.donkirkby.vograbulary.VograbularyPreferences;
import com.google.gwt.user.client.Cookies;

public class GwtPreferences extends VograbularyPreferences {
    private static final String COOKIE_ROOT = 
            "com.github.donkirkby.vograbulary.preferences.";
    private static final long COOKIE_LIFETIME =
            100L * 365 * 24 * 60 * 60 * 1000; // 100 years in milliseconds

    @Override
    protected void putStringSet(String key, Set<String> values) {
//        JsArrayString jsValues = JavaScriptObject.createArray().cast();
//        for (String value : values) {
//            jsValues.push(value);
//        }
//        // TODO: upgrade to GWT 2.7 so we can use JsonUtils.stringify().
//        JsonUtils.stringify(jsValues);
        StringBuilder cookie = new StringBuilder();
        for (String string : values) {
            if (cookie.length() > 0) {
                cookie.append("|");
            }
            cookie.append(string);
        }
        
        Date expires = new Date(System.currentTimeMillis() + COOKIE_LIFETIME);
        Cookies.setCookie(COOKIE_ROOT + key, cookie.toString(), expires);
    }

    @Override
    protected Set<String> getStringSet(String key, Set<String> defValues) {
        String cookie = Cookies.getCookie(COOKIE_ROOT + key);
        if (cookie == null) {
            return defValues;
        }
        return new HashSet<>(Arrays.asList(cookie.split("\\|")));
    }

}
