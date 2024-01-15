package com.GameManager;

import java.io.FileInputStream;
import java.io.InputStream;

/*
 * MinimHelper Class"
 * - to load files for sounds
 */
public class MinimHelper {

    public String sketchPath( String fileName ) {
        return "assets/"+fileName;
    }

    public InputStream createInput(String fileName) {
        InputStream is = null;
        try{
            is = new FileInputStream(sketchPath(fileName));
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
        return is;
    }
}
