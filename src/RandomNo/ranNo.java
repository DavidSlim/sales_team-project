/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RandomNo;

import java.util.Random;

/**
 *
 * @author DEAdMEAt
 */
public class ranNo {

    public String ranNo() {

        Random r = new Random();
        int bcNo1 = r.ints(1, 1000, 9999).findAny().getAsInt();
        int bcNo2 = r.ints(1, 1000, 9999).findAny().getAsInt();
        int bcNo3 = r.ints(1, 10, 99).findAny().getAsInt();
        char c1 = (char) (r.nextInt(26) + (byte) 'A');
        char c2 = (char) (r.nextInt(26) + (byte) 'A');
        char c3 = (char) (r.nextInt(26) + 'A');
        char c4 = (char) (r.nextInt(26) + 'A');
        char c5 = (char) (r.nextInt(26) + 'A');

        String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (int i = 0; i < 2; i++) {
            int al = alpha.charAt(r.nextInt(alpha.length()));
        }

        String BCNo = bcNo1 + "" + c3 + "" + c4;

        return BCNo;

    }
}
