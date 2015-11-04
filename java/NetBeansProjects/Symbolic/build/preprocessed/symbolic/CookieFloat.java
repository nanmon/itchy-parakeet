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
public class CookieFloat {
    private static final int DIGITSIZE = 999999999;
    private static final int DIGITLENGTH = (DIGITSIZE+"").length();
    
    private long[] ent;
    private float fl;
    
    public CookieFloat(String num){
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
    
    private void parser(String h){
        ent = new long[h.length()/DIGITLENGTH+1];
        for(int i=ent.length-1; i>0; --i){
            ent[i] = Long.parseLong(h.substring(h.length()-DIGITLENGTH));
            h = h.substring(0, h.length()-DIGITLENGTH);
        }
        ent[0] = Long.parseLong(h);
    }
    
    public String get(){
        String r = ent[0]+",";
        for(int i=1; i<ent.length; ++i){ 
            r=r+(ent[i]<100000?(ent[i]<1000?(ent[i]<100?(ent[i]<10?"00000000":"0000000"):"000000"):(ent[i]<10000?"00000":"0000")):(ent[i]<10000000?(ent[i]<1000000?"000":"00"):(ent[i]<100000000?"0":"")))+ent[i];
        }
        return r+(fl+"").substring(1);
    }

    private void addDigit(long num) {
        if(ent==null){
            ent = new long[] {num};
            return;
        }
        long niunum[] = new long[ent.length+1];
        niunum[0] = num;
        for(int i=1; i<niunum.length; ++i)
            niunum[i] = ent[i-1];
        ent = niunum;
    }
}
