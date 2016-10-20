package share.fair.miflas;

import java.util.HashMap;

/**
 * Created by Ori on 4/25/2016.
 */
public class MiflasUser {

    String uid;
    String name;
    String email;

    public MiflasUser(String uid,String name, String email){
        this.uid= uid;
        this.name = name;
        this.email= email;
    }
    public MiflasUser(HashMap hashMap){
        this.uid = hashMap.get("uid").toString();
        this.email = hashMap.get("email").toString();
        this.name = hashMap.get("name").toString();
    }
    public String getName(){
        return this.name;
    }
    public String getEmail(){
        return this.email;
    }
    public  String getUid(){
        return this.uid;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setUid(String uid){
        this.uid= uid;
    }
}
