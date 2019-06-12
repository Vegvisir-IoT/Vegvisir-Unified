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

    public String getAppID(){
      return this.appId;
    }

    public String getDesc(){
      return this.desc;
    }

    public Set<String> getChannels(){
      return this.channels;
    }

    public void setAppID(String newAppID){
      this.appId = newAppID;
    }

    public void setDesc(String newDesc){
      this.desc = newDesc;
    }

    public void setChannels(newChannels){
      this.channels = newChannels;
    }



}
