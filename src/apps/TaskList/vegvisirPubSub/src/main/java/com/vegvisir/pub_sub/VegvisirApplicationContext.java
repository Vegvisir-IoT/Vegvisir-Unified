package com.vegvisir.pub_sub;

import java.util.Set;

/**
 * This class contains core information about the application, such as name, desc, and which
 * channel this application has subscribed so far.
 */
public class VegvisirApplicationContext {

    private String appId;

    private String desc;

    private Set<String> topics;

    /**
     * Public Constructor
     * @param appId : String representation that reflects the application name
     * @param desc  : String
     * @param channels : Set of Strings pertaining to topics the application
     *                 wants updates from the Vegvisir blockchain.
     */
    public VegvisirApplicationContext(String appId, String desc, Set<String> channels) {
        this.appId = appId;
        this.desc = desc;
        this.topics = channels;
    }


    /*##############################
     *  Getters & Setters         3
     ############################*/
    public String getAppID(){ return this.appId; }

    public String getDesc(){ return this.desc;  }

    public void setAppID(String newAppID){ this.appId = newAppID; }

    public void setDesc(String newDesc){      this.desc = newDesc;   }

    public Set<String> getTopics() {
        return topics;
    }

    public void setTopics(Set<String> topics) {
        this.topics = topics;
    }
}
