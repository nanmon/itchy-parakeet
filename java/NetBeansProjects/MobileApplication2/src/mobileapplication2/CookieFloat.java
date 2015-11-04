/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobileapplication2;

/**
 *
 * @author nancio
 */
public class CookieFloat {
    protected String ent;
    protected String fl;
    
    public CookieFloat(int n){
        this.ent = Integer.toString(n);
        this. fl = "";
    }
    
    public CookieFloat(double n){
        this(Double.toString(n));
    }
    
    public CookieFloat(String m){
        String h, k;
        if(m.indexOf(",")<0){ 
            h = m;
            while(h.length()!=0 ? h.charAt(0) == '0': false) h = h.substring(1);
            k="";
        }
        else{
            h = m.substring(0, m.indexOf(","));
            k = m.substring(m.indexOf(",")+1, m.length());
            while(h.length()>=1 ? h.charAt(0) == '0': false) h = h.substring(1);
            while(k.length()>=1 ? k.charAt(k.length()-1) == '0' : false) k = k.substring(0, k.length()-1);
        }
        
        this.ent = h;
        this.fl = k;
    }
    
    public CookieFloat(String n, String m){
        String h=n, k=m;
        while(h.length()>1 ? h.charAt(0) == '0' : false) h = h.substring(1);
        while(k.length()>1 ? k.charAt(k.length()-1) == '0' : false) k = k.substring(0, k.length()-1);
        this.ent = h;
        this.fl = k;
    }
    
    public String get(boolean pretty){
        if(!pretty) return ent+(fl!="" ? "."+fl : "");
        else{
            if(ent.length()==1 ? Integer.parseInt(ent)==0 : false) return "0";
            String n= ent;
            String s="";
            int m = 100;
            while(n.length()>0){
                s = (m<10 ? "00" : (m<100 ? "0" : "")) + s;
                m = Integer.parseInt(n.length()>=3 ? n.substring(n.length()-3) : n);//n%1000;
                s= m+","+s;
                n=n.length()>=3 ? n.substring(0, n.length()-3) : "";
            }
            s = s.substring(0, s.length()-1);
            return s+"."+(fl!="" ? fl.charAt(0)+"" : "0");
        }
    }
    
    public String getRound(boolean ceil, boolean pretty){
        if(!pretty) return ceil ? (Integer.parseInt(fl)!=0 ? ""+(Integer.parseInt(ent)+1) : ent) : ent;
        else{
            if(ent.length()==1 ? Integer.parseInt(ent)==0 : false) return "0";
            String n= ent;
            if(ceil && (fl.length()==1 ? Integer.parseInt(fl)!=0 : false)) 
                n = CookieFloat.s(new CookieFloat(n, ""), new CookieFloat(1)).ent;
            String s="";
            int m = 100;
            while(n.length()>0){
                s = (m<10 ? "00" : (m<100 ? "0" : "")) + s;
                m = Integer.parseInt(n.length()>=3 ? n.substring(n.length()-3) : n);//n%1000;
                s= m+","+s;
                n=n.length()>=3 ? n.substring(0, n.length()-3) : "";
            }
            s = s.substring(0, s.length()-1);
            return s;
        }
    }
    
    public static CookieFloat s(CookieFloat cf1, CookieFloat cf){
        String a = cf1.fl.length()>=cf.fl.length() ? cf1.fl : cf.fl;
        String b = cf1.fl.length()<cf.fl.length() ? cf1.fl : cf.fl;
        String s="";
        int c;
        boolean acr=false;
        for(int i=a.length()-1; i>=0 ;--i){
            c = i<b.length() ? Integer.parseInt(b.charAt(i)+"") : 0;
            c+= Integer.parseInt(a.charAt(i)+"");
            if(acr) ++c;
            acr=false;
            if(c>=10){
                acr=true;
                c-=10;
            }
            s=c+s;
        }
        String h = s;
        a = cf1.ent.length()>=cf.ent.length() ? cf1.ent : cf.ent;
        b = cf1.ent.length()<cf.ent.length() ? cf1.ent : cf.ent;
        
        s="";
        for(int i=1; i<=a.length(); ++i){
            c = b.length()>=i ? Integer.parseInt(b.charAt(b.length()-i)+"") : 0;
            c+= Integer.parseInt(a.charAt(a.length()-i)+"");
            if(acr) ++c;
            acr=false;
            if(c>=10){
                acr=true;
                c-=10;
            }
            s=c+s;
        }
        String k= acr ? 1+s: s;
        
        return new CookieFloat(k, h);
    }
    
    public static CookieFloat r(CookieFloat cf1, CookieFloat cf){
        String a = cf1.fl;
        String b = cf.fl;
        String s="";
        int c;
        boolean acr=false;
        for(int i=Math.max(a.length(), b.length())-1; i>=0;--i){
            c = a.length()>i ? Integer.parseInt(a.charAt(i)+"") : 0;
            c-= b.length()>i ? Integer.parseInt(b.charAt(i)+"") : 0;
            if(acr) --c;
            acr=false;
            if(c<0){
                acr=true;
                c+=10;
            }
            s=c+s;
        }
        String k = s;
        
        a=cf1.ent;
        b=cf.ent;
        s="";
        for(int i=1; i<=Math.max(a.length(), b.length()); ++i){
            c = a.length()>=i ? Integer.parseInt(a.charAt(a.length()-i)+"") : 0;
            c-= b.length()>=i ? Integer.parseInt(b.charAt(b.length()-i)+"") : 0;
            if(acr) --c;
            acr=false;
            if(c<0){
                acr=true;
                c+=10;
            }
            s=c+s;
        }
        String h = acr ? "-"+s: s;
        
        return new CookieFloat(h, k);
    }
    
    public static CookieFloat m(CookieFloat cf1, CookieFloat cf){
        CookieFloat c1 = new CookieFloat(cf1.ent + cf1.fl);
        CookieFloat c2 = new CookieFloat(cf.ent + cf.fl);
        while(!c2.r(new CookieFloat(1)).equals("0")){
            c1.s(new CookieFloat(cf1.ent+cf1.fl));
        }
        
        String h = c1.ent.substring(0, c1.ent.length()-(cf1.fl.length()+cf.fl.length()));
        String k = c1.ent.substring(c1.ent.length()-(cf1.fl.length()+cf.fl.length()));
        
        CookieFloat r = new CookieFloat(h, k);
        
        return r;
    }
    
    public String s(CookieFloat cf){
        CookieFloat f = CookieFloat.s(this, cf);
        this.ent = f.ent;
        this.fl = f.fl;
        
        return ent+(fl!="" ? "."+fl : "");
    }
    
    public String r(CookieFloat cf){
        CookieFloat f = CookieFloat.r(this, cf);
        this.ent = f.ent;
        this.fl = f.fl;
        
        return ent+(fl!="" ? "."+fl : "");
    }
    
    public String m(CookieFloat cf){
        CookieFloat f = CookieFloat.m(this, cf);
        this.ent = f.ent;
        this.fl = f.fl;
        
        return ent+(fl!="" ? "."+fl : "");
    }
    
    public boolean gt(CookieFloat cf){
        if(this.ent.length()>cf.ent.length()) return true;
        if(this.ent.length()<cf.ent.length()) return false;
        for(int i=0; i<ent.length(); ++i){
            if(this.ent.charAt(i)>cf.ent.charAt(i)) return true;
            if(this.ent.charAt(i)<cf.ent.charAt(i)) return false;
        }
        for(int i=0; i<Math.min(fl.length(), cf.fl.length()); ++i){
            if(this.fl.charAt(i)>cf.fl.charAt(i)) return true;
            if(this.fl.charAt(i)<cf.fl.charAt(i)) return false;
        }
        if(this.fl.length()>=cf.fl.length()) return true;
        
        return false;
    }
}
