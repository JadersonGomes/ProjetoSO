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
public class Fila {
    
    private No inicio;
    private No fim;
    
    public Fila()
    {
        No inicio = null;
        No fim = null;
    }
    
    public Fila copy()
    {
        Fila filaRet = new Fila();
        No no = inicio;
        while(no != null)
        {
            filaRet.enqueue(no.getProcesso());
            no = no.getProximo();
        }
        return filaRet;
    }
    
    public boolean isEmpty()
    {
        return inicio == null;
    }
    
    public void enqueue(Processo processo)
    {
        No no = new No();
        no.setProcesso(processo);
        
        if(isEmpty())
        {
            inicio = no;
            fim = no;
        }
        else
        {
            fim.setProximo(no);
            fim = no;
        }
        
    }
    
    public Processo dequeue()
    {
        if(!isEmpty())
        {
            if(inicio == fim)
            {
                Processo processo = inicio.getProcesso();
                inicio = null;
                fim = null;
                return processo;
            }
            else
            {
                Processo processo = inicio.getProcesso();
                inicio = inicio.getProximo();
                return processo;
            }
        }
        else
        {
            return null;
        }
    }
    
    public boolean find(Processo processo)
    {
        No no = inicio;
        while(no != null)
        {
            if(no.getProcesso() == processo)
            {
                return true;
            }
            no = no.getProximo();
            
        }
        return false;
    }
    
    public Processo get(Processo processo)
    {
        No no = inicio.getProximo();
        No anterior = inicio;
        if(processo != inicio.getProcesso())
        {
            while(no != null)
            {          
                if(no.getProcesso() == processo)
                {
                    anterior.setProximo(no.getProximo());
                    return no.getProcesso();
                }
                anterior = no;
                no = no.getProximo();

            }
        }
        else
        {
            return inicio.getProcesso();
        }
        
        return null;
    }
    
    public Processo consult()
    {
        return inicio.getProcesso();
    }
    
}
