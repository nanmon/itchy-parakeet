package cookie;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.microedition.lcdui.game.*;
import javax.microedition.lcdui.*;
import java.util.*;

/**
 *
 * @author nancio
 */
public class Game extends GameCanvas{

    private static final int STATS = 3;
    private static final int BIGCOOKIE = 2;
    private static final int BUILDINGS = 1;
    private static final int OPTIONS = 0;
    private static final int UPGRADES = 4;

    private final int backgroundColor = 0x61a5d0;

    private CookieFloat cookies;
    private CookieFloat expend;
    private long time_ShouldBeGoing;
    private final Graphics g;
    private CookieFloat cps;
    private UpgradeManager updog;
    
    private final Building[] buildings;
    private int tab;
    private final int[] drag;
    
    private Timer timer;
    private final TimerTask taskMaster;
    
    private class BigCookie extends LayerManager{
        private Sprite bigCookie;
        private final int w;
        private final CookieFloat baseCpc;
        private final CookieFloat handmade;
        private int clicks;
        private int multi;
        private CookieFloat extraCpc;
        private CookieFloat fEach;
        private double cpsPercent;
        private CookieFloat cpc;

        public BigCookie(int width){
            try{
                bigCookie = new Sprite(Image.createImage("/rsc/cookie.png"), 250, 250);
                bigCookie.defineReferencePixel(125, 0);
                bigCookie.setRefPixelPosition(width/2, 0);
            }catch(Exception err){}
            w = width;
            baseCpc = new CookieFloat(1);
            multi = 1;
            this.append(bigCookie);
            this.setViewWindow(0,0,width,250);
            clicks = 0;
            handmade = new CookieFloat(0);
            fEach = new CookieFloat(0);
            extraCpc = new CookieFloat(0);
            cpsPercent = 0;
            cpc = baseCpc;
        }
        
        public CookieFloat getCpc(){
            return CookieFloat.s(cpc, CookieFloat.m(cps, new CookieFloat(cpsPercent)));
        }

        public int getClicks(){
            return clicks;
        }

        public CookieFloat getHandmade(){
            return handmade;
        }
        
        public CookieFloat click(){
            ++clicks;
            CookieFloat made = this.getCpc();
            handmade.s(made);
            return made;
        }
        
        public int getFrame(){
            return bigCookie.getFrame();
        }
        
        public void changeFrame(){
            bigCookie.nextFrame();
        }
        
        public void changeFrame(int n){
            bigCookie.setFrame(n);
        }
        
        public void paintFrame(Graphics g, int n){
            this.paintFrame(g, n, false);
        }
        
        public void paintFrame(Graphics g, int n, boolean force){
            g.setColor(0x61a5d0);
            if(bigCookie.getFrame()!=n){
                bigCookie.nextFrame();
                //g.fillRect(0, 90, w, 250);
                this.paint(g, 0, 90); //galleta en y=90
            }else if(force){ 
                //g.fillRect(0, 90, w, 250);
                this.paint(g, 0, 90);
            }
            
        }

        public void addBase(double v) {
            baseCpc.s(new CookieFloat(v));
            reCalcCpc();
        }

        public void twice(){
            multi*=2;
            reCalcCpc();
        }

        public void addExtra(CookieFloat e){
            extraCpc.s(e);
            reCalcCpc();
        }

        public void addfEach(CookieFloat fe){
            fEach.s(fe);
        }

        public void NonCursorBuyed(){
            if(fEach.get(false).length()==1) return;
            addExtra(fEach);
            buildings[0].addExtra(fEach);
            cps.s(CookieFloat.m(fEach, new CookieFloat(Building.getN(0))));
            drawBuilding(0);
        }

        public void OneCpsPercent() {
            cpsPercent+=0.01;
            reCalcCpc();
        }

        public void reCalcCpc(){
            cpc =CookieFloat.s(
                    CookieFloat.m(baseCpc, new CookieFloat(multi)),
                    extraCpc
            );
        }
    }
    private final BigCookie bigCookie;
    
    private class PlusMan {
        private int[][] times;//t,x,y
        
        public PlusMan(){
            times = new int[20][3];
            for(int i=0; i<times.length; ++i) times[i][0] = 0;
        }
        
        public void add(){
            int fw = Font.getDefaultFont().stringWidth("+"+bigCookie.getCpc().getRound(false, true));
            int rand[] = this.bestCandidate();
            g.setColor(0xFFFFFF);
            g.drawString("+"+bigCookie.getCpc().getRound(false, true), rand[0], rand[1], 
                    Graphics.TOP|Graphics.LEFT);
            drawCookies();

            flushGraphics(rand[0], rand[1], fw, 25);
            int i=0;
            while(times[i][0]>=0) ++i;
            times[i] = new int[] {10, rand[0], rand[1]};
        }
        
        public void minus(){
            for(int i=0; i<times.length; ++i){
                if(--times[i][0]==0 && tab==BIGCOOKIE){
                    int l = Font.getDefaultFont().stringWidth("+"+bigCookie.getCpc().getRound(false, true));
                    g.setColor(0x61a5d0);
                    g.fillRect(times[i][1], times[i][2], l, 25);
                    bigCookie.paint(g, 0, 90);
                    flushGraphics(times[i][1], times[i][2], l, 25);
                }
            }
        }
        
        public int[] bestCandidate(){
            int bestCandidate[] = {}, xCandidate[], 
                l=Font.getDefaultFont().stringWidth("+"+bigCookie.getCpc().getRound(false, true)); 
            double bestDistance=0, xDistance=1000000000000D;
            Random rand = new Random();
            for(int i=0; i<10; ++i){
                xCandidate = new int[] {rand.nextInt(getWidth()-l), 90+rand.nextInt(getHeight()-115)};
                for(int j=0; j<times.length; ++j){
                    if(times[j][0]>0){
                        double d = (times[j][1]-xCandidate[0])*(times[j][1]-xCandidate[0])
                                  +(times[j][2]-xCandidate[1])*(times[j][2]-xCandidate[1]);
                        xDistance = d<xDistance ? d : xDistance;
                    }
                }
                if(xDistance>bestDistance){
                    bestDistance = xDistance;
                    bestCandidate = xCandidate;
                }
                xDistance = 10000000;
            }
            return bestCandidate;
        }
    }
    private PlusMan littleTimer;
    
    public Game(){
        super(false);
        this.setFullScreenMode(true);
        cookies = new CookieFloat(0);
        expend = new CookieFloat(0);
        time_ShouldBeGoing = new Date().getTime();
        cps = new CookieFloat(0);
        g = this.getGraphics();
        bigCookie = new BigCookie(this.getWidth());
        tab = BIGCOOKIE;
        drag = new int[2];
        buildings = new Building[11];
        for(int i=0; i<buildings.length; ++i) buildings[i] = new Building(i);
        updog = new UpgradeManager(0);
        
        timer = new Timer();
        littleTimer = new PlusMan();
        
        taskMaster = new TimerTask() {
            int t=0;
            public void run(){
                ++t;
                if(isShown()) {
                    if (tab % 3 != 0) drawCookies();
                    else if(t%60==0){
                        t=0;
                        drawStats(false);
                    }
                }
                cookies.s(CookieFloat.m(cps, new CookieFloat("0", "1")));
                littleTimer.minus();
            }
        };
    }
    
    public void startGame(){
        g.setColor(0x61a5d0);
        g.fillRect(0,0,this.getWidth(), this.getHeight());
        bigCookie.paintFrame(g, 0, true);
        drawCps(false);
        this.flushGraphics();
        timer.scheduleAtFixedRate(taskMaster, 0, 100);
        
    }

    protected void keyPressed(int keyCode){
        if(keyCode<0 && keyCode>-5) cambio(keyCode);
        else if(tab==BIGCOOKIE){
            if(keyCode==-5){
                cookies.s(new CookieFloat(10000000));
                littleTimer.add();
            }
        }else if(tab==BUILDINGS){
            if(keyCode>=Canvas.KEY_NUM1 && keyCode<=Canvas.KEY_NUM9 ||
                    keyCode==Canvas.KEY_NUM0 || keyCode==Canvas.KEY_POUND){
                int i = keyCode-49;
                if(keyCode==35) i=10;
                if(keyCode==48) i=9;
                if(cookies.gt(buildings[i].getPrice())){
                    cookies.r(buildings[i].buy());
                    cps.s(buildings[i].getCps());
                    if(i!=0){
                        bigCookie.NonCursorBuyed();
                    }
                    drawCookies();
                    drawBuilding(i);
                    drawCps();
                }
            }
        }else if(tab==UPGRADES){
            int i = keyCode-48;
            if(i<updog.falseLength && i>=0) {
                updog.selected = i;
                g.setColor(backgroundColor);
                g.fillRect(0, getHeight()-90, getWidth(), 90);
                drawSelectedUpgrade(i);
            }if(keyCode==-5){
                i = updog.selected;
                if (cookies.gt(updog.getUpgrades()[updog.falseIndex[i]].getCost())) {
                    cookies.r(updog.getUpgrades()[updog.falseIndex[i]].getCost());
                    updog.getUpgrades()[updog.falseIndex[i]].buyed();
                    buyedUpgrade(updog.getUpgrades()[updog.falseIndex[i]]);
                    drawUpgrades();
                    this.flushGraphics(0, 90, getWidth(), (((updog.falseLength) * 30 - 1) / getWidth()) * 30 + 30);
                    drawCps();
                    g.setColor(backgroundColor);
                    g.fillRect(0, 2*getHeight()/3, getWidth(), getHeight()/3);
                    this.flushGraphics(0, getHeight()-90, getWidth(), 90);
                }
            }
        }
    }
    
    protected void pointerReleased(int x, int y){
        drag[0] += x;
        drag[1] += y;

        int changeTab = drag[0] >= 100 ? -3 :
                       (drag[0] <=-100 ? -4 :
                       (drag[1] >= 100 ? -1 :
                       (drag[1] <=-100 ? -2 : 0)));
        
        if(changeTab!=0) cambio(changeTab);
        else{
            if(bigCookie.getFrame()==1){ 
                cookies.s(bigCookie.click());
                bigCookie.paintFrame(g, 0);
                this.flushGraphics(0, 90, this.getWidth(), 250);
                littleTimer.add();
            }
            if(tab==BUILDINGS) {
                if (Math.abs(drag[0]) <= 20 && Math.abs(drag[1]) <= 20) {
                    if (y >= this.getHeight() - buildings.length * 48) {
                        int i = buildings.length - (int) Math.ceil((getHeight() - y) / 48.0);
                        if (cookies.gt(buildings[i].getPrice())) {
                            expend.s(buildings[i].getPrice());
                            cookies.r(buildings[i].buy());
                            cps.s(buildings[i].getCps());
                            if(i!=0) bigCookie.NonCursorBuyed();
                            drawBuilding(i);
                            drawCps();
                            drawCookies();
                        }
                    }
                }
            }else if(tab==UPGRADES){

                if(Math.abs(drag[0])<=20 && Math.abs(drag[1])<=20){
                    int i = (int)((y-90)/30)*getWidth()/30 + (int)(x/30);
                    if(i<updog.falseLength && i>=0) {
                        updog.selected = i;
                        g.setColor(backgroundColor);
                        g.fillRect(0, 2*getHeight()/3, getWidth(), getHeight()/3);
                        drawSelectedUpgrade(i);
                    }if(y>getHeight()-40 && x>getWidth()-100){
                        i = updog.selected;
                        if (cookies.gt(updog.getUpgrades()[updog.falseIndex[i]].getCost())) {
                            cookies.r(updog.getUpgrades()[updog.falseIndex[i]].getCost());
                            updog.getUpgrades()[updog.falseIndex[i]].buyed();
                            buyedUpgrade(updog.getUpgrades()[updog.falseIndex[i]]);
                            drawUpgrades();
                            this.flushGraphics(0, 90, getWidth(), (((updog.falseLength) * 30 - 1) / getWidth()) * 30 + 30);
                            drawCps();
                            g.setColor(backgroundColor);
                            g.fillRect(0, 2*getHeight()/3, getWidth(), getHeight()/3);
                            this.flushGraphics(0, getHeight()-90, getWidth(), 90);
                        }
                    }
                }
            }
        }
        drag[0] = 0;
        drag[1] = 0;
    }

    private void drawSelectedUpgrade(int i) {
        g.setColor(0xFFFFFF);
        g.drawRect(5,2*getHeight()/3, getWidth()-10, getHeight()/3-5);
        Upgrade upgrade = updog.getUpgrades()[updog.falseIndex[i]];
        upgrade.paint(g, 10, 2*getHeight()/3+5);
        String dr = upgrade.getDescription();
        /*int dl = Font.getDefaultFont().stringWidth(dr), dh=Font.getDefaultFont().getHeight(), u=0;
        while(dl>getWidth()-50){
            String siCabe = dr.substring(0, dr.lastIndexOf((int)(' ')+1));
            String falta = dr.substring(dr.lastIndexOf((int)(' ')+1));
            int siCabe_l = Font.getDefaultFont().stringWidth(siCabe);
            while(siCabe_l>getWidth()-50){
                siCabe =  siCabe.substring(0, siCabe.lastIndexOf((int)(' ')+1));
                falta =  siCabe.substring(siCabe.lastIndexOf((int)(' ')+1))+falta;
                siCabe_l = Font.getDefaultFont().stringWidth(siCabe);
            }
            g.drawString(siCabe, 45, 2*getHeight()/3+5+u*dh, Graphics.TOP|Graphics.LEFT);
            ++u;
            dr = falta;
            dl = Font.getDefaultFont().stringWidth(dr);
        }*/
        g.drawString(dr, 45, 2*getHeight()/3+5, Graphics.TOP|Graphics.LEFT);
        g.drawString(upgrade.getCost().get(true), 10, getHeight()-5, Graphics.BOTTOM|Graphics.LEFT);
        g.drawRect(getWidth()-100, getHeight()-40, 90, 30);
        g.drawString("buy", getWidth()-55, getHeight()-38, Graphics.TOP|Graphics.HCENTER);
        this.flushGraphics(0, 2*getHeight()/3, getWidth(), getHeight()/3);
    }

    public void pointerPressed(int x, int y){
        if(tab==BIGCOOKIE){
            if((x-this.getWidth()/2.0)*(x-this.getWidth()/2.0) +
               (y-215)*(y-215) <=15625){
                bigCookie.paintFrame(g, 1);
                this.flushGraphics(0, 90, this.getWidth(), 250);
            }
        }
        
        drag[0] -= x;
        drag[1] -= y;
        
        
    }
    
    public void pointerDragged(int x, int y){
        if(bigCookie.getFrame()==1){
            if((x-this.getWidth()/2.0)*(x-this.getWidth()/2.0) +
               (y-215)*(y-215) > 15625 || Math.abs(drag[0]) < 20){
                bigCookie.paintFrame(g, 0);
            }else bigCookie.paintFrame(g, 1);
            this.flushGraphics(0, 90, this.getWidth(), 250);
        }        
    }
    
    public void drawCookies(boolean flush){
        g.setColor(0x61a5d0);
        g.fillRect(0,30,this.getWidth(), 25);

        g.setColor(0xFFFFFF);
        g.drawString(cookies.getRound(false, true)+" cookies", this.getWidth()/2, 30, Graphics.TOP|Graphics.HCENTER);
        
        if(flush) this.flushGraphics(0, 30, this.getWidth(), 25);
    }
    
    public void drawCps(boolean flush){
        
        g.setColor(0x61a5d0);
        g.fillRect(0,55,this.getWidth(), 25);
        
        g.setColor(0xFFFFFF);
        g.drawString("per second: "+cps.get(true), this.getWidth()/2, 55, Graphics.TOP|Graphics.HCENTER);
        
        if(flush) this.flushGraphics(0, 55, this.getWidth(), 25);
    }
    
    public void drawBuilding(int i, boolean flush){
        g.setColor(0x949494);
        int y = this.getHeight()-(buildings.length-i)*48;
        g.fillRect(0, y, this.getWidth(), 48);
        buildings[i].paint(g, 0, y);
        g.setColor(0x646464);
        g.drawLine(48, y, this.getWidth(), y);
        g.setColor(0xFFFFFF);
        g.drawString(buildings[i].getN()+"", this.getWidth(), y+48, Graphics.BOTTOM|Graphics.RIGHT);
        g.drawString(buildings[i].getCps().get(true)+" cps", this.getWidth(), y, Graphics.TOP|Graphics.RIGHT);
        g.drawString(buildings[i].getPrice().getRound(true, true), 48, y+48, Graphics.BOTTOM|Graphics.LEFT);
        
        if(flush) this.flushGraphics(0, y, this.getWidth(), 48);
    }

    public void drawStats(boolean all){
        g.setColor(0x2A2A2A);
        g.fillRect(0,all? 0 : 30,this.getWidth(), all? getHeight() : 90);
        //Cookies:
        g.setColor(0xCBCBCB);
        g.drawString("Current: "+cookies.getRound(false, true), 10, 30, Graphics.TOP|Graphics.LEFT);
        //All time cookies
        g.drawString("All-time: "+CookieFloat.s(expend, cookies).getRound(false, true), 10, 60, Graphics.TOP|Graphics.LEFT);
        //Started
        long t = (new Date().getTime()-time_ShouldBeGoing)/(6000);
        g.drawString("Started: "+(t/10)+"."+t%10+" minutes ago", 10, 90, Graphics.TOP|Graphics.LEFT);
        if(all) {
            //Buildings
            int b=0;
            for(int i=0; i<buildings.length; ++i) b+=buildings[i].getN();
            g.drawString("Buildings: "+b, 10, 120, Graphics.TOP|Graphics.LEFT);
            //Per second
            g.drawString("Per second: "+cps.get(true), 10, 150, Graphics.TOP|Graphics.LEFT);
            //Per click
            g.drawString("Cookies per click: "+bigCookie.getCpc().getRound(false, true), 10, 180, Graphics.TOP|Graphics.LEFT);
            //Clicks
            g.drawString("Cookie clicks: "+bigCookie.getClicks(), 10, 210, Graphics.TOP|Graphics.LEFT);
            //Hand-made cookies
            g.drawString("Handmade: "+bigCookie.getHandmade().getRound(false, true), 10, 240, Graphics.TOP|Graphics.LEFT);
            //Golden cookie clicks
            //Version
            g.drawString("Version: 0.5.1", 10, 270, Graphics.TOP|Graphics.LEFT);
        }else flushGraphics(0, 30, getWidth(), 90);
    }
    
    public void drawCookies(){
        drawCookies(true);
    }
    
    public void drawCps(){
        drawCps(true);
    }
    
    public void drawBuilding(int i){
        drawBuilding(i, true);
    }

    private void cambio(int point) {
        if (point <= -3) {
            bigCookie.changeFrame(0);
            int pretab = tab;
            if(point==-3 && tab%3!=0) tab = tab%3==2? STATS : BIGCOOKIE;
            if(point==-4 && tab%3!=1) tab = tab%3==2? BUILDINGS : BIGCOOKIE;
            if(pretab!=tab) {
                g.setColor(0x61a5d0);
                g.fillRect(0, 0, this.getWidth(), this.getHeight());
                switch (tab) {
                    case BIGCOOKIE:
                        /*g.setColor(0xFFFFFF);
                        g.setFont(Font.getFont(Font.FACE_SYSTEM,Font.STYLE_BOLD,Font.SIZE_LARGE));
                        g.drawString("COOKIE", getWidth()/2, 0, Graphics.TOP|Graphics.HCENTER);
                        g.setFont(Font.getDefaultFont());*/
                        drawCookies(false);
                        drawCps(false);
                        bigCookie.paintFrame(g, 0, true);
                        break;
                    case BUILDINGS:
                        for (int i = 0; i < buildings.length; ++i) drawBuilding(i, false);
                        drawCps(false);
                        break;
                    case STATS:
                        drawStats(true);
                        break;
                }
                this.flushGraphics();
            }
        } else {
            if(point==-1 && tab<=1) tab+= 3;
            if(point==-2 && tab>=3) tab-= 3;
            g.setColor(0x61a5d0);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            switch(tab){
                case OPTIONS:
                    g.setColor(0xFF0000);
                    g.fillRect(0,0,getWidth(), getHeight());
                    break;
                case BUILDINGS:
                    for (int i = 0; i < buildings.length; ++i) drawBuilding(i, false);
                    drawCps(false);
                    break;
                case STATS:
                    drawStats(true);
                    break;
                case UPGRADES:
                    drawUpgrades();
                    drawCps(false);
            }
            this.flushGraphics();
        }
    }

    protected void showNotify(){
        if(tab==0){
            drawStats(false);
        }
    }

    private void buyedUpgrade(Upgrade up){
        if(up.getType()[0]==Upgrade.BUILDINGTYPE){
            if(up.getEffect()==null) {
                bigCookie.OneCpsPercent();
            }else if(up.getEffect().length==1){
                if(up.getEffect()[0]==0){
                    cps.s(CookieFloat.m(
                            buildings[up.getType()[1]].getCps(),
                            new CookieFloat(Building.getN(up.getType()[1]))
                    ));
                    buildings[up.getType()[1]].twice();
                }else{
                    cps.s(
                            new CookieFloat(Building.getN(up.getType()[1]) * up.getEffect()[0])
                    );
                    buildings[up.getType()[1]].addBase(up.getEffect()[0]);
                }
            }else if(up.getEffect().length==2){
                if(up.getEffect()[0]<0){
                    int otherN=0;
                    for(int i=1; i<buildings.length; ++i) otherN+=Building.getN(i);
                    cps.s(new CookieFloat(up.getEffect()[1] * otherN * Building.getN(0)));
                    buildings[0].addExtra(new CookieFloat(up.getEffect()[1] * otherN));
                    bigCookie.addExtra(new CookieFloat(up.getEffect()[1]*otherN));
                    bigCookie.addfEach(new CookieFloat(up.getEffect()[1]));
                }else if(up.getEffect()[0]==0){
                    cps.s(CookieFloat.m(
                            buildings[0].getCps(),
                            new CookieFloat(Building.getN(0))
                    ));
                    buildings[0].twice();
                    bigCookie.twice();
                }else{
                    cps.s(
                            new CookieFloat(Building.getN(0) * up.getEffect()[0])
                    );
                    buildings[0].addBase(up.getEffect()[0]);
                    bigCookie.addBase(up.getEffect()[1]);
                }
            }

        }//else if(up.getType()[0]==OTHERTYPE)
    }

    private void drawUpgrades(){

        CookieFloat [] buildN = new CookieFloat[buildings.length+1];
        for(int i=0; i<buildings.length; ++i) buildN[i] = new CookieFloat(Building.getN(i));
        buildN[buildings.length] = bigCookie.getHandmade();
        CookieFloat key[][] = {buildN};
        Upgrade [] upgrades = updog.getBuyable(key);

        g.setColor(0x61a5d0);
        g.fillRect(0, 90, getWidth(), (((upgrades.length)*30-1)/getWidth())*30+30);

        for(int i=0; i<upgrades.length; ++i){
            int x = (i%(getWidth()/30))*30;
            int y = 90+(((i+1)*30-1)/getWidth())*30;
            upgrades[i].paint(g, x, y);
        }
    }

}