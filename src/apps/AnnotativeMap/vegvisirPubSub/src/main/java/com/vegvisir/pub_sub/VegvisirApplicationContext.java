package com.vegvisir.pub_sub;

import java.util.Set;

/**
 * This class contains core information about the application, such as name, desc, and which
 * channel this application has subscribed so far.
 */
public class VegvisirApplicationContext {

    private String appId;

    private String desc;

    private Set<String> channels;

}
