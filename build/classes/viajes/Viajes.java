/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package viajes;

import interfaces.conexionViaje;

/**
 *
 * @author Invitado_PC16
 */
public class Viajes {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        conexionViaje cn = new conexionViaje();
        cn.conectar();
    }
}
