/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobileapplication1;

import javax.microedition.lcdui.game.*;
import javax.microedition.lcdui.*;
import java.util.*;

/**
 *
 * @author nancio
 */
public class Cookie extends GameCanvas{

    private static final int
    
    private CookieFloat cookies;
    private final Graphics g;
    private CookieFloat cps;
    
    private final Building [] buildings;
    private int tab;
    private final int[] drag;
    
    private final Timer timer;
    
    private final TimerTask taskMaster;
    
    private class BigCookie extends LayerManager{
        private Sprite bigCookie;
        private final int w;
        private final CookieFloat cpc;
        private final CookieFloat handmade;
        private int clicks;
        private int CPCDL;
        
        public BigCookie(int width){
            try{
                bigCookie = new Sprite(Image.createImage("/rsc/cookie.png"), 250, 250);
                bigCookie.defineReferencePixel(125, 0);
                bigCookie.setRefPixelPosition(width/2, 0);
            }catch(Exception err){}
            w = width;
            cpc = new CookieFloat(1);
            CPCDL = Font.getDefaultFont().stringWidth("+"+cpc.getRound(false, true));
            this.append(bigCookie);
            this.setViewWindow(0,0,width,250);
            clicks = 0;
            handmade = new CookieFloat(0);
        }
        
        public CookieFloat getCpc(){
            return cpc;
        }
        
        public int getCPCDL(){
            return CPCDL;
        }
        
        public CookieFloat click(){
            ++clicks;
            handmade.s(cpc);
            return cpc;
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
            while(times[i][0]>0) ++i;
            times[i] = new int[] {10, rand[0], rand[1]};
        }
        
        public void minus(){
            for(int i=0; i<times.length; ++i){
                if(--times[i][0]==0 && tab==1){
                    int l = Font.getDefaultFont().stringWidth("+"+bigCookie.getCpc().getRound(false, true));
                    System.out.println(l);
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
            double bestDistance=0, xDistance=1000000;
            Random rand = new Random();
            for(int i=0; i<10; ++i){
                xCandidate = new int[] {rand.nextInt(getWidth()-l), 50+rand.nextInt(getHeight()-75)};
                for(int j=0; j<times.length; ++j){
                    if(times[j][0]>0){
                        double d = Math.sqrt((times[j][1]-xCandidate[0])*(times[j][1]-xCandidate[0])
                                            +(times[j][2]-xCandidate[1])*(times[j][2]-xCandidate[1]));
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
    
    public Cookie(boolean b){
        super(b);
        this.setFullScreenMode(true);
        cookies = new CookieFloat(0);
        cps = new CookieFloat(0);
        g = this.getGraphics();
        bigCookie = new BigCookie(this.getWidth());
        tab = 1;
        drag = new int[2];
        
        buildings = new Building[11];
        for(int i=0; i<buildings.length; ++i) buildings[i] = new Building(i);
        
        timer = new Timer();
        littleTimer = new PlusMan();
        
        taskMaster = new TimerTask() {
            public void run(){
                if(tab%3>0 && isShown()){
                    drawCps();
                    drawCookies();
                }
                cookies.s(CookieFloat.m(cps,new CookieFloat("0","1")));
                littleTimer.minus();
            }
        };
    }
    
    public void startGame(){
        //int frames=0;
        g.setColor(0x61a5d0);
        g.fillRect(0,0,this.getWidth(), this.getHeight());
        bigCookie.paintFrame(g, 0, true);
        this.flushGraphics();
        timer.schedule( taskMaster, 0, 100 );
        
    }
    
    protected void keyPressed(int keyCode){
        if(tab==1){
            if(keyCode==Canvas.KEY_NUM5){
                cookies.s(new CookieFloat("100000000000"));                
                littleTimer.add();
            }if(keyCode==Canvas.KEY_POUND){
                tab=2;
                bigCookie.changeFrame(0);
                g.setColor(0x61a5d0);
                g.fillRect(0, 0, this.getWidth(), this.getHeight());
                for(int i=0; i<buildings.length; ++i) drawBuilding(i, false);
                this.flushGraphics();
            }
        }if(tab==2){
            if(keyCode>=Canvas.KEY_NUM1 && keyCode<=Canvas.KEY_NUM9 ||
                    keyCode==Canvas.KEY_NUM0 || keyCode==Canvas.KEY_POUND){
                int i = keyCode-49;
                if(keyCode==35) i=10;
                if(keyCode==48) i=9;
                if(cookies.gt(buildings[i].getPrice())){
                    cookies.r(buildings[i].buy());
                    cps.s( buildings[i].getCps());
                    drawCookies();
                    drawCps();
                    drawBuilding(i);
                }
            }if(keyCode==Canvas.KEY_STAR){
                tab=1;
                bigCookie.changeFrame(0);
                g.setColor(0x61a5d0);
                g.fillRect(0, 0, this.getWidth(), this.getHeight());
                drawCookies(false);
                drawCps(false);
                bigCookie.paintFrame(g, 0, true);
                this.flushGraphics();
            }
        }
    }
    
    protected void pointerReleased(int x, int y){
        drag[0] += x;
        drag[1] += y;
        
        int cht[] = {0, 0};
        if(drag[0] >= 100){
            cht[0] = tab%3>0 ? -1 : 0;
        }else if(drag[0] <= -100){
            cht[0] = tab%3<2 ? 1 : 0;
        }if(drag[1] >= 100){
            cht[1] = 1;
        }else if(drag[1] <= -100){
            cht[1] = -1;
        }
        if(cht[0]!=0){
            bigCookie.changeFrame(0);
            g.setColor(0x61a5d0);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            
            tab+=cht[0];
            switch(tab%3){
                case 1:            
                    drawCookies(false);
                    drawCps(false);
                    bigCookie.paintFrame(g, 0, true);
                    break;
                case 2:
                    for(int i=0; i<buildings.length; ++i) drawBuilding(i, false);
                    break;
                case 0:
                    break;
            }
            this.flushGraphics();
        }else if(cht[1]!=0 && tab!=1){ 
            tab+=3*cht[1];
            if(tab%3==0) tab = tab>6 ? 0 : (tab<0 ? 6 : tab);
            if(tab%3==2) tab = tab>5 ? 2 : (tab<2 ? 5 : tab);
            switch(tab){
                case 5:
                    //upgrades.draw;
            }
            
        
        }else{
            if(bigCookie.getFrame()==1){ 
                cookies.s(bigCookie.click());
                drawCookies();
                bigCookie.paintFrame(g, 0);
                this.flushGraphics(0,0,this.getWidth(), 25);
                this.flushGraphics(0, 90, this.getWidth(), 250);
                littleTimer.add();
            }
            if(tab==2)
                if(Math.abs(drag[0])<=20 && Math.abs(drag[1])<=20){
                    if(y>=this.getHeight() - buildings.length*48){
                        int i = buildings.length - (int)Math.ceil((getHeight()-y)/48.0);
                        if(cookies.gt(buildings[i].getPrice())){
                            cookies.r(buildings[i].buy());
                            cps.s(buildings[i].getCps());
                            drawBuilding(i);
                        }
                    }
                }
        }
        drag[0] = 0;
        drag[1] = 0;
    }
    
    public void pointerPressed(int x, int y){
        if(tab==1){
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
        g.fillRect(0,0,this.getWidth(), 25);

        g.setColor(0xFFFFFF);
        g.drawString(cookies.getRound(false, true)+" cookies", this.getWidth()/2, 0, Graphics.TOP|Graphics.HCENTER);
        
        if(flush) this.flushGraphics(0, 0, this.getWidth(), 25);
    }
    
    public void drawCps(boolean flush){
        
        g.setColor(0x61a5d0);
        g.fillRect(0,25,this.getWidth(), 25);
        
        g.setColor(0xFFFFFF);
        g.drawString("per second: "+cps.get(true), this.getWidth()/2, 25, Graphics.TOP|Graphics.HCENTER);
        
        if(flush) this.flushGraphics(0, 25, this.getWidth(), 25);
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
    
    public void drawCookies(){
        drawCookies(true);
    }
    
    public void drawCps(){
        drawCps(true);
    }
    
    public void drawBuilding(int i){
        drawBuilding(i, true);
    }
}
