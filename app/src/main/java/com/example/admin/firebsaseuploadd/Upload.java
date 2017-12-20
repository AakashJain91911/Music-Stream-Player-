package com.example.admin.firebsaseuploadd;

/**
 * Created by Admin on 15/12/2017.
 */

public class Upload {

    public String name;
    public String url;
    public String ar;
    public  String date;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Upload() {

    }

    public Upload(String name, String url,String ar, String date) {
        this.name = name;
        this.url = url;
      this.ar=ar;
      this.date=date;
    }


    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
   public String getAr ()
  {
    return ar;
  }
  public String getDate()
  {
      return date;
  }
}