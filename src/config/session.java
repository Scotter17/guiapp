/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

/**
 *
 * @author USER36
 */
public class session {
    
    private static int id;
    private static String email;
    private static String type;
    private static String address;

    public static void setSession(int id, String e, String t, String a) {
        id = id;
        
        email = e;
        type = t;
        address = a;
    }

    public static int getUserId() {
        return id;
    }

  

    public static String getEmail() {
        return email;
    }

    public static String getType() {
        return type;
    }

    public static String getAddress(){
        return address;
    }
    public static void clearSession() {
        id = 0;
        email = null;
        type = null;
        address = null;
    }
}
