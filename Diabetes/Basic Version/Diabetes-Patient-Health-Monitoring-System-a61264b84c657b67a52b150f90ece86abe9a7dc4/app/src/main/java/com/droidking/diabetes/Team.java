package com.droidking.diabetes;

/**
 * Created by kstanoev on 1/14/2015.
 */
public class Team {
    String time;
    double BEat ;
    double AEat ;
    int BPsys ;
    int BPdys ;

    public Team(String time, double BEat ,double AEat,int BPsys ,int BPdys )
    {
        this.setTime(time);
        this.setBEat(BEat);
        this.setAEat(AEat);
        this.setBPsys(BPsys);
        this.setBPdys(BPdys);

    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time= time;
    }

    public double getBEat() {
        return BEat;
    }

    public void setBEat(double BEat) {
        this.BEat = BEat;
    }

    public double getAEat() {
        return BEat;
    }

    public void setAEat(double AEat) {
        this.AEat = AEat;
    }

    public int getBPsys() {
        return BPsys;
    }

    public void setBPsys(int BPsys) {
        this.BPdys = BPsys;
    }

    public int getBPdys() {
        return BPdys;
    }

    public void setBPdys(int BPdys) {
        this.BPdys = BPdys;
    }


}
