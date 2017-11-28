/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FilaDinamica;

import DAO.Processo;

/**
 *
 * @author lab206
 */
public class Tester {
    
    public static void main(String[] args)
    {
        
        Fila fila = new Fila();
        Processo processo = new Processo();
        processo.setAt(1);
        
        fila.enqueue(processo);
        processo = new Processo();
        processo.setAt(2);
        
        fila.enqueue(processo);
        processo = new Processo();
        processo.setAt(3);
        
        fila.enqueue(processo);
        processo = new Processo();
        processo.setAt(4);
        
        fila.dequeue();
        
        fila.enqueue(processo);
        processo = new Processo();
        processo.setAt(5);
        
        fila.dequeue();
        fila.enqueue(processo);
        processo = new Processo();
        processo.setAt(6);
        
    }
    
}
