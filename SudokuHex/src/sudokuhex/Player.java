/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudokuhex;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Roy Soldin
 */
public class Player implements Serializable{
    
    String username;
    String password;
    String history;

    public String getHistory() {
        return history;
    }

     public Player(String username, String password) {
        this.username = username;
        this.password = password;
        this.history = new String();
    }
    
//    @Override
//    public boolean equals(Object obj) {
//        
//        if (obj == null) {
//            return false;
//        }
//        final Player other = (Player) obj;
//        if (!this.username.equals(other.username)) {
//            return false;
//        }
//        return this.password.equals(other.password);
//    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String addToHistory(int levelFlag, String time){
        String output = getCurrentTimeStamp();
        String level = new String();
        if(levelFlag == 1) level = "    Easy    ";
        if(levelFlag == 2) level = "    Medium    ";
        if(levelFlag == 3) level = "    Hard    ";
        if(levelFlag == 4) level = "    Demo    ";
        
        output = output.concat(level);
        output = output.concat(time);
        this.history = output.concat('\n'+history);
        
        return history;
        
        
    }
    
    public static String getCurrentTimeStamp() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    Date now = new Date();
    String strDate = sdf.format(now);
    return strDate;
}

}
