/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

/**
 *
 * @author lab206
 */
public class Processo {
    
    private String id;
    private int at;
    private int bt;    
    private int btTotal;
    private int priority;
    private int tat;
    private int wt;
    private int[] io;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAt() {
        return at;
    }

    public void setAt(int at) {
        this.at = at;
    }

    public int getBt() {
        return bt;
    }

    public void setBt(int bt) {
        this.bt = bt;
    }
    
    public int getBtTotal() {
        return btTotal;
    }

    public void setBtTotal(int btTotal) {
        this.btTotal = btTotal;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getTat() {
        return tat;
    }

    public void setTat(int tat) {
        this.tat = tat;
    }

    public int getWt() {
        return wt;
    }

    public void setWt(int wt) {
        this.wt = wt;
    }
    
    public int[] getIo()
    {
        return io;
    }
    
    public void setIO(int[] io)
    {
        this.io = io;
    }
    
}
