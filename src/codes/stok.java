/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author ozkan
 */
public class stok {
    private String urun_kodu,tarih, olay,musteri_ad;
    private int marka_id,grup_id,adet,stok_id,toplam,musteri_id;
    PreparedStatement ps;
    ResultSet rs;
    
    public List<stok> StokGirisTumu(boolean eskiye) throws SQLException{
        //STOK GİREN ÇIKAN TAMAMI---------------------------
        baglanti vb = new baglanti();
        vb.baglan();
        Statement st = vb.con.createStatement();
        List<stok> girenlist = new ArrayList<>();
        stok[] stokgiren;
        String sorgu;
        if(eskiye){
            sorgu="select sg.urun_id, sg.adet, sg.tarih,'GİRİŞ' from stok_giren sg union select sc.urun_id, sc.adet, sc.tarih,'ÇIKIŞ' from stok_cikan sc order by tarih desc";
        }else{
            sorgu="select sg.urun_id, sg.adet, sg.tarih,'GİRİŞ' from stok_giren sg union select sc.urun_id, sc.adet, sc.tarih,'ÇIKIŞ' from stok_cikan sc order by tarih asc";
        }
        ps = vb.con.prepareStatement(sorgu);
        rs = ps.executeQuery(sorgu);
        int sayac=0;
         while(rs.next()){
           sayac++;
        }
        stokgiren=new stok[sayac];
        int i =0;
        ps = vb.con.prepareStatement(sorgu);
        rs = ps.executeQuery(sorgu);
         while(rs.next()){
              stokgiren[i] = new stok();
              stokgiren[i].setUrun_kodu(rs.getString("urun_id"));
              stokgiren[i].setAdet(rs.getInt("adet"));
              stokgiren[i].setTarih(rs.getString("tarih"));
              stokgiren[i].setOlay(rs.getString(4));
             // stokgiren[i].setStok_id(rs.getInt("id"));
              girenlist.add(stokgiren[i]);
              i++;
          }
         ps.close();
         rs.close();
         vb.con.close();
         return girenlist;
    }
    
    public int StokToplam(String urunId) throws SQLException{
         baglanti vb = new baglanti();
        vb.baglan();
        Statement st = vb.con.createStatement();
        List<stok> girenlist = new ArrayList<>();
        stok[] stokgiren;
        String sorgu;
        int giren=0,cikan=0,toplam=0;
        sorgu="SELECT sum(sg.adet) FROM stok_giren sg where sg.urun_id='"+urunId+"' union select sum(sc.adet) from stok_cikan sc where sc.urun_id='"+urunId+"'";
        ps = vb.con.prepareStatement(sorgu);
        rs = ps.executeQuery(sorgu);
        int sayac=0;
         while(rs.next()){
           sayac++;
        }
        stokgiren=new stok[sayac];
        int i =0;
        ps = vb.con.prepareStatement(sorgu);
        rs = ps.executeQuery(sorgu);
         while(rs.next()){
              stokgiren[i] = new stok();
              giren = rs.getInt(1);
              cikan = rs.getInt(2);
              System.out.println(giren+"-"+cikan);
              i++;
          }
         toplam = giren - cikan;
         if(toplam<0){
             return 0;
         }
         ps.close();
         rs.close();
         vb.con.close();
         return toplam;
    }
    public int StokToplamGiris(String urunKodu) throws SQLException{
         baglanti vb = new baglanti();
        vb.baglan();
        Statement st = vb.con.createStatement();
        List<stok> girenlist = new ArrayList<>();
        stok[] stokgiren;
        String sorgu;
        int toplam = 0;
        sorgu="SELECT sum(sg.adet) as toplam,sg.urun_id FROM stok_giren sg where urun_id='"+urunKodu+"' ORDER BY sg.tarih desc";
        ps = vb.con.prepareStatement(sorgu);
        rs = ps.executeQuery(sorgu);
        int sayac=0;
         while(rs.next()){
           sayac++;
        }
        stokgiren=new stok[sayac];
        int i =0;
        ps = vb.con.prepareStatement(sorgu);
        rs = ps.executeQuery(sorgu);
         while(rs.next()){
              stokgiren[i] = new stok();
              stokgiren[i].setToplam(rs.getInt(1));
              toplam = rs.getInt(1);
              girenlist.add(stokgiren[i]);
              i++;
          }
         ps.close();
         rs.close();
         vb.con.close();
         return toplam;
    }
    
    public int StokToplamCikis(String urunKodu) throws SQLException{
         baglanti vb = new baglanti();
        vb.baglan();
        Statement st = vb.con.createStatement();
        List<stok> girenlist = new ArrayList<>();
        stok[] stokgiren;
        String sorgu;
        int toplam = 0;
        sorgu="SELECT sum(sg.adet) as toplam,sg.urun_id FROM stok_cikan sg where urun_id='"+urunKodu+"' ORDER BY sg.tarih desc";
        ps = vb.con.prepareStatement(sorgu);
        rs = ps.executeQuery(sorgu);
        int sayac=0;
         while(rs.next()){
           sayac++;
        }
        stokgiren=new stok[sayac];
        int i =0;
        ps = vb.con.prepareStatement(sorgu);
        rs = ps.executeQuery(sorgu);
         while(rs.next()){
              stokgiren[i] = new stok();
              stokgiren[i].setToplam(rs.getInt(1));
              toplam = rs.getInt(1);
              girenlist.add(stokgiren[i]);
              i++;
          }
         ps.close();
         rs.close();
         vb.con.close();
         return toplam;
    }
    public List<stok> StokToplamArama(String urunKodu) throws SQLException{
         baglanti vb = new baglanti();
        vb.baglan();
        Statement st = vb.con.createStatement();
        List<stok> girenlist = new ArrayList<>();
        stok[] stokgiren;
        String sorgu;
        sorgu="SELECT (sum(sg.adet)-sum(sc.adet)) ,sg.urun_id as fark FROM stok_giren sg JOIN stok_cikan sc ON sg.urun_id = sc.urun_id where sg.urun_id like'"+urunKodu+"%' ORDER BY sg.tarih desc";
        ps = vb.con.prepareStatement(sorgu);
        rs = ps.executeQuery(sorgu);
        int sayac=0;
         while(rs.next()){
           sayac++;
        }
        stokgiren=new stok[sayac];
        int i =0;
        ps = vb.con.prepareStatement(sorgu);
        rs = ps.executeQuery(sorgu);
         while(rs.next()){
              stokgiren[i] = new stok();
              stokgiren[i].setToplam(rs.getInt(1));
              stokgiren[i].setUrun_kodu(rs.getString(2));
              girenlist.add(stokgiren[i]);
              i++;
          }
         ps.close();
         rs.close();
         vb.con.close();
         return girenlist;
    }
    public boolean StokGiris(String urunKodu, String tarih, int adet, int M_id) throws SQLException{
        baglanti vb = new baglanti();
        vb.baglan();
        String[] tarih1 = tarih.split("/");
        String tarih2 = tarih1[2]+tarih1[1]+tarih1[0];
        try {
            String sorgu = "insert into stok_giren (urun_id, tarih, adet, gitti, musteri_id) values ('"+urunKodu+"',DATE_FORMAT('"+tarih2+"','%Y/%m/%d'),"+adet+","+0+","+M_id+")";
            ps = vb.con.prepareStatement(sorgu);
            ps.executeUpdate();
            ps.close();
            vb.con.close();
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            return false;
        }
      }
    public boolean StokCikis(String urunKodu, String tarih, int adet, int M_id) throws SQLException{
        baglanti vb = new baglanti();
        vb.baglan();
        String[] tarih1 = tarih.split("/");
        String tarih2 = tarih1[2]+tarih1[1]+tarih1[0];
        try {
            String sorgu = "insert into stok_cikan (urun_id, tarih, adet, musteri_id) values ('"+urunKodu+"',DATE_FORMAT('"+tarih2+"','%Y/%m/%d'),"+adet+","+M_id+")";
            ps = vb.con.prepareStatement(sorgu);
            ps.executeUpdate();
            ps.close();
            vb.con.close();
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            return false;
        }
    }
    
    public boolean StokCikisGuncelle(String urunKodu) throws SQLException{
        baglanti vb = new baglanti();
        vb.baglan();
        try {
            Statement st = vb.con.createStatement();
            String sorgu = "update stok_giren set gitti="+1+" where urun_id='"+urunKodu+"'";
            ps = vb.con.prepareStatement(sorgu);
            ps.executeUpdate();
            ps.close();
            st.close();
            vb.con.close();
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            return false;
        }
    }
    public List<stok> StokGirisListele() throws SQLException{
        baglanti vb = new baglanti();
        vb.baglan();
        Statement st = vb.con.createStatement();
        List<stok> girenlist = new ArrayList<>();
        stok[] stokgiren;
        String sorgu;
        sorgu="SELECT * from stok_giren";
        ps = vb.con.prepareStatement(sorgu);
        rs = ps.executeQuery(sorgu);
        int sayac=0;
         while(rs.next()){
           sayac++;
        }
        stokgiren=new stok[sayac];
        int i =0;
        ps = vb.con.prepareStatement(sorgu);
        rs = ps.executeQuery(sorgu);
         while(rs.next()){
              stokgiren[i] = new stok();
              stokgiren[i].setUrun_kodu(rs.getString("urun_id"));
              stokgiren[i].setAdet(rs.getInt("adet"));
              stokgiren[i].setTarih(rs.getString("tarih"));
              stokgiren[i].setOlay("GİRİŞ");
              girenlist.add(stokgiren[i]);
              i++;
          }
         ps.close();
         rs.close();
         vb.con.close();
         return girenlist;
    }
      public List<stok> StokCikisListele() throws SQLException{
        baglanti vb = new baglanti();
        vb.baglan();
        Statement st = vb.con.createStatement();
        List<stok> girenlist = new ArrayList<>();
        stok[] stokgiren;
        String sorgu;
        sorgu="SELECT * from stok_cikan";
        ps = vb.con.prepareStatement(sorgu);
        rs = ps.executeQuery(sorgu);
        int sayac=0;
         while(rs.next()){
           sayac++;
        }
        stokgiren=new stok[sayac];
        int i =0;
        ps = vb.con.prepareStatement(sorgu);
        rs = ps.executeQuery(sorgu);
         while(rs.next()){
              stokgiren[i] = new stok();
              stokgiren[i].setUrun_kodu(rs.getString("urun_id"));
              stokgiren[i].setAdet(rs.getInt("adet"));
              stokgiren[i].setTarih(rs.getString("tarih"));
              stokgiren[i].setOlay("ÇIKIŞ");
              girenlist.add(stokgiren[i]);
              i++;
          }
         ps.close();
         rs.close();
         vb.con.close();
         return girenlist;
    }
    public List<stok> MusteriListesi() throws SQLException{
        baglanti vb = new baglanti();
        vb.baglan();
        Statement st = vb.con.createStatement();
        List<stok> girenlist = new ArrayList<>();
        stok[] stokgiren;
        String sorgu;
        sorgu="SELECT * from musteriler";
        ps = vb.con.prepareStatement(sorgu);
        rs = ps.executeQuery(sorgu);
        int sayac=0;
         while(rs.next()){
           sayac++;
        }
        stokgiren=new stok[sayac];
        int i =0;
        ps = vb.con.prepareStatement(sorgu);
        rs = ps.executeQuery(sorgu);
         while(rs.next()){
              stokgiren[i] = new stok();
              stokgiren[i].setMusteri_id(rs.getInt("id"));
              stokgiren[i].setMusteri_ad(rs.getString("ad"));
              girenlist.add(stokgiren[i]);
              i++;
          }
         ps.close();
         rs.close();
         vb.con.close();
         return girenlist;
    }  
    public String getUrun_kodu() {
        return urun_kodu;
    }

    public void setUrun_kodu(String urun_kodu) {
        this.urun_kodu = urun_kodu;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }

    public int getMarka_id() {
        return marka_id;
    }

    public String getOlay() {
        return olay;
    }

    public void setOlay(String olay) {
        this.olay = olay;
    }

    public void setMarka_id(int marka_id) {
        this.marka_id = marka_id;
    }

    public int getGrup_id() {
        return grup_id;
    }

    public String getMusteri_ad() {
        return musteri_ad;
    }

    public void setMusteri_ad(String musteri_ad) {
        this.musteri_ad = musteri_ad;
    }

    public int getMusteri_id() {
        return musteri_id;
    }

    public void setMusteri_id(int musteri_id) {
        this.musteri_id = musteri_id;
    }

    public int getToplam() {
        return toplam;
    }

    public void setToplam(int toplam) {
        this.toplam = toplam;
    }

    public int getStok_id() {
        return stok_id;
    }

    public void setStok_id(int stok_id) {
        this.stok_id = stok_id;
    }

    public void setGrup_id(int grup_id) {
        this.grup_id = grup_id;
    }

    public int getAdet() {
        return adet;
    }

    public void setAdet(int adet) {
        this.adet = adet;
    }
    
}
