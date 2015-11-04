/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package symbolic;

/**
 *
 * @author Roberto Montaño
 */
public class Cookiefloat {
    private static final int DIGITSIZE = 999999999;
    private static final int DIGITLENGTH = (DIGITSIZE+"").length();
    
    private long[] ent;
    private float fl;
    
    public Cookiefloat(String num){
        int pi = num.indexOf(".");
        if(pi!=-1){
            String h = num.substring(0, pi);
            String k = num.substring(pi+1);
            this.parser(h);
            
            fl = Float.parseFloat("0."+k);
        }else{
            this.parser(num);
        }
    }

    private Cookiefloat(Cookiefloat a) {
        this.ent = new long[a.ent.length];
        for(int i=0; i<ent.length; ++i) ent[i] = a.ent[i];
        this.fl = a.fl;
    }
    
    public Cookiefloat(long[] a){
        this.ent = a;
        fl = 0;
    }
    
    private void parser(String h){
        int hl = h.length();
        ent = new long[hl/DIGITLENGTH+(hl%DIGITLENGTH!=0?1:0)];
        int i=0;
        for(; i<ent.length-1; ++i){
            ent[i] = Long.parseLong(h.substring(h.length()-DIGITLENGTH));
            h = h.substring(0, h.length()-DIGITLENGTH);
        }
        ent[i] = Long.parseLong(h);
    }
    
    public String get(){
        String r = "";
        int i=0, l=ent.length-1;
        
        try{
            
            while(ent[l]==0)--l;
        }catch(ArrayIndexOutOfBoundsException ex){
            return fl+"";
        }
        
        for(; i<l; ++i){ 
            r=(ent[i]<100000?(ent[i]<1000?(ent[i]<100?(ent[i]<10?"00000000":"0000000"):"000000"):(ent[i]<10000?"00000":"0000")):(ent[i]<10000000?(ent[i]<1000000?"000":"00"):(ent[i]<100000000?"0":"")))+ent[i]+r;
        }
        return ent[i]+r+(fl+"").substring(1);
    }

    private void addDigit(long num) {
        if(ent==null){
            ent = new long[] {num};
            return;
        }
        long niunum[] = new long[ent.length+1];
        int i=0;
        for(; i<ent.length; ++i)
            niunum[i] = ent[i];
        niunum[i]=num;
        ent = niunum;
    }
    
    public static Cookiefloat s(Cookiefloat a, Cookiefloat b){
        Cookiefloat r = new Cookiefloat(a);
        r.fl+=b.fl;
        boolean acr=false;
        if(r.fl>=1){
            acr=true;
            r.fl-=1;
        }
        int i=0;
        for(; i<r.ent.length ;++i){
            r.ent[i]+=(i<b.ent.length?b.ent[i]: 0)+(acr?1:0);
            if(r.ent[i]>DIGITSIZE){
                r.ent[i]-=DIGITSIZE+1;
                acr=true;
            }else acr=false;
        }
        for(; i<b.ent.length; ++i){
            r.addDigit(b.ent[i]+(acr?1:0));
            if(r.ent[i]>DIGITSIZE){
                r.ent[i]-=DIGITSIZE+1;
                acr=true;
            }else acr=false;
        }
        if(acr) r.addDigit(1);
        
        return r;
    }
    
    public static Cookiefloat r(Cookiefloat a, Cookiefloat b){
        if(a.ent.length<b.ent.length)
            System.out.println("fallo en la resta");
        Cookiefloat r = new Cookiefloat(a);
        r.fl-=b.fl;
        boolean acr=false;
        if(r.fl<0){
            r.fl+=1;
            acr=true;
        }
        int i=0;
        for(; i<r.ent.length ;++i){
            r.ent[i]-=(i<b.ent.length?b.ent[i]: 0)+(acr?1:0);
            if(r.ent[i]<0){
                r.ent[i]+=DIGITSIZE+1;
                acr=true;
            }else acr=false;
        }
        if(acr) System.out.println("fallo en la resta");
        return r;
    }
    
    public static Cookiefloat m(Cookiefloat a, Cookiefloat b){
        int l = a.ent.length+b.ent.length - (a.ent[a.ent.length-1]*b.ent[b.ent.length-1]>DIGITSIZE? 0 : 1);
        Cookiefloat r = new Cookiefloat(new long[l]);
        r.fl = a.fl*b.fl;
        double d = a.fl*b.ent[0];
        r.ent[0] = (long)d; //HUEHUEHUEHUEHUH
        r.fl+=d-r.ent[0];
        if(r.fl>1){
            r.ent[0]+=r.fl;
            r.fl-=(int)r.fl;
        }
        for(int j=1; j<b.ent.length; ++j){
            d=b.ent[j]*a.fl;
            r.ent[j]=(long)d; ///////////////////////////
            r.ent[j-1]+=(d-r.ent[j])*(DIGITSIZE+1);
            r.ent[j]+=r.ent[j-1]/(DIGITSIZE+1);
            r.ent[j-1]%=DIGITSIZE+1;
        }
        d = a.ent[0]*b.fl;
        r.ent[0]+=d;
        if(r.ent[0]>DIGITSIZE){
            r.ent[1]+=r.ent[0]/(DIGITSIZE+1);
            r.ent[0]%=DIGITSIZE+1;
        }
        r.fl+=d-(long)d;
        if(r.fl>1){
            r.ent[0]+=r.fl;
            r.fl-=(int)r.fl;
        }
        for(int i=1; i<a.ent.length; ++i){
            d=a.ent[i]*b.fl;
            r.ent[i]+=(long)d; ///////////////////////////
            r.ent[i-1]+=(d-(long)d)*(DIGITSIZE+1);
            r.ent[i]+=r.ent[i-1]/(DIGITSIZE+1);
            r.ent[i-1]%=DIGITSIZE+1;
        }
        try{
            for(int i=0; i<a.ent.length; ++i){
                for(int j=0; j<b.ent.length; ++j){
                    d=a.ent[i]*b.ent[j];
                    r.ent[i+j]+=d;
                    r.ent[i+j+1]+=r.ent[i+j]/(DIGITSIZE+1);
                    r.ent[i+j]%=DIGITSIZE+1;
                }
            }
        }catch(ArrayIndexOutOfBoundsException ex){}
        return r;
    }
}
