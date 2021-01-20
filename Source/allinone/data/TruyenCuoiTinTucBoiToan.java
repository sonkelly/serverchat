package allinone.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;

import vn.game.common.LoggerContext;

public class TruyenCuoiTinTucBoiToan {
	private String baseUrl = "http://wap1.yupii.vn:6666/webyupii2/Vipgame/";
	private HTTPPoster poster;
        private static Logger mLog = LoggerContext.getLoggerFactory()
			.getLogger(TruyenCuoiTinTucBoiToan.class);

	public TruyenCuoiTinTucBoiToan(HTTPPoster p, String s) {
		String d = userName+"&"+password+"2&15&" + s;
		link = (AES.encrypt(d));
		poster = p;
	}
	public TruyenCuoiTinTucBoiToan(HTTPPoster p) {
		poster = p;
	}
	//TODO: AES
	public static void main(String[] args){
		HTTPPoster p = new HTTPPoster();
		TruyenCuoiTinTucBoiToan data = new TruyenCuoiTinTucBoiToan(p, "");
		//String[] d = new String[]{userName, password, "2","15","Binh"};
		
		
		//link = (AES.encrypt(d));
		String res = data.danhMucBoi();
		try{
			JSONObject obj = new JSONObject(res);
			JSONArray arr = obj.getJSONArray("Boi");
			for(int i = 0; i< arr.length(); i++){
				JSONObject o = arr.getJSONObject(i);
//				System.out.println("=====");
//				System.out.println(o.get("MABOI"));
//				System.out.println(o.get("TENBOI"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String userName = "land";
	public static String password = "vip.land@123";
	private static String link = "";
	String baseLink = "land&vip.land@123&2&15&";
	public void setUpLink(String s){
		StringBuilder d = new StringBuilder();
		d.append(baseLink);
		d.append(s);
		d.deleteCharAt(d.length()-1);
		link = (AES.encrypt(d.toString()));
	}
	public void setUpLink(String[] ss){
		StringBuilder d = new StringBuilder();
		d.append(baseLink);
		for(String s : ss) {
			if((s != null) && (s.compareTo("") != 0))
				d.append(s).append("&");
		}
		d.deleteCharAt(d.length()-1);
		link = (AES.encrypt(d.toString()));
	}
	/*
	 * Boi
	 */

	public String layDieuKienBoi(int code) {
		String res = "";
		String url = "DieuKienBoi.aspx";
		url = baseUrl + url;
		String param = "MaBoi="+code;
		res = poster.sendGetRequest(url, param);
		return res;
	}
	public String danhMucBoi() {
		String res = "";
		String url = "BoiMenu.xml";
		url = baseUrl + url;
		String param = "";
		res = poster.sendGetRequest(url, param);
		return res;
	}

	public String danhMucBoi(int code) {
		String res = "";
		String url = "BoiMenuDetail.xml";
		url = baseUrl + url;
		String param = "MaBoi=" + code;
		res = poster.sendGetRequest(url, param);
		return res;
	}

	// link = AES(name&pass&1&15&HovaTen)
	public String boiAiCap() {
		String res = "";
		String url = "BoiAiCap.xml";
		url = baseUrl + url;
		String param = "link=" + link;
		res = poster.sendGetRequest(url, param);
		return res;
	}
        
        private String parseData(String resjson)
        {
            try {
                JSONObject jsonPkg = new JSONObject(resjson);
//                System.out.println(resjson);

                JSONArray arrNews = jsonPkg.getJSONArray("Table");
                int size = arrNews.length();
                String content = null;
                
                for(int i = 0; i< size; i++)
                {
                    JSONObject json = arrNews.getJSONObject(i);
                    content = json.getString("NOIDUNG");
                   
                    break;
                }
                
                return content;
            } catch (JSONException ex) {
                mLog.error(ex.getMessage(), ex);
            }
            
            
            return "";
        }
        
	// link = AES(name&pass&1&15&code) code = 1102
	public String boiChuCaiDauTien() {
		String res = "";
		String url = "BoiChuCaiDauCuaTen.xml";
		url = baseUrl + url;
		String param = "link=" + link;
		res = poster.sendGetRequest(url, param);
		return res;
	}

	// link = AES(name&pass&1&15&HovaTen)
	public String boiTenTiengHan() {
		String res = "";
		String url = "BoiTenTiengHan.xml";
		url = baseUrl + url;
		String param = "link=" + link;
		res = poster.sendGetRequest(url, param);
		return res;
	}

	// link = AES(name&pass&1&15&HovaTen)
	public String timBanNuChoBanNam() {
		String res = "";
		String url = "TimBanNuChoNam.xml";
		url = baseUrl + url;
		String param = "link=" + link;
		res = poster.sendGetRequest(url, param);
                
		return parseData(res);
	}
        
        
        
	// link = AES(name&pass&1&15&HovaTen)
	public String timBanNamChoBanNu() {
		String res = "";
		String url = "TimBanNamChoNu.xml";
		url = baseUrl + url;
		String param = "link=" + link;
		res = poster.sendGetRequest(url, param);
		return parseData(res);
	}

	// link = AES(name&pass&1&15&code) code = 1201
	public String boiDangDi() {
		String res = "";
		String url = "BoiDangDi.xml";
		url = baseUrl + url;
		String param = "link=" + link;
		res = poster.sendGetRequest(url, param);
		return res;
	}

	// link = AES(name&pass&1&15&code) code = 1202
	public String boiMongTay() {
		String res = "";
		String url = "BoiMongTay.xml";
		url = baseUrl + url;
		String param = "link=" + link;
		res = poster.sendGetRequest(url, param);
		return res;
	}

	// link = AES(name&pass&1&15&code) code = 1203
	public String boiNgonTay() {
		String res = "";
		String url = "BoiNgonTay.xml";
		url = baseUrl + url;
		String param = "link=" + link;
		res = poster.sendGetRequest(url, param);
		return res;
	}

	// link = AES(name&pass&1&15&code) code = 1204
	public String boiLongMay() {
		String res = "";
		String url = "BoiLongMay.xml";
		url = baseUrl + url;
		String param = "link=" + link;
		res = poster.sendGetRequest(url, param);
		return res;
	}

	// link = AES(name&pass&1&15&code) code = 1205
	public String boiNotRuoi() {
		String res = "";
		String url = "BoiNotRuoi.xml";
		url = baseUrl + url;
		String param = "link=" + link;
		res = poster.sendGetRequest(url, param);
		return res;
	}

	// link = AES(name&pass&1&15&code) code = 1301
	public String boiTinhYeuQuaNhomMau() {
		String res = "";
		String url = "TinhYeuQuaNhomMau.xml";
		url = baseUrl + url;
		String param = "link=" + link;
		res = poster.sendGetRequest(url, param);
		return res;
	}

	// link = AES(name&pass&1&15&code) code = 1302
	public String boiTinhCachBanTrai() {
		String res = "";
		String url = "DoanTinhCachBanTrai.xml";
		url = baseUrl + url;
		String param = "link=" + link;
		res = poster.sendGetRequest(url, param);
		return res;
	}

	// link = AES(name&pass&1&15&HovaTen)
	public String mauDanOngPhuHopvoiBan() {
		String res = "";
		String url = "MauDanOngPhuHopvoiBan.xml";
		url = baseUrl + url;
		String param = "link=" + link;
		res = poster.sendGetRequest(url, param);
		return res;
	}

	// link = AES(name&pass&1&15&NgaySinh)
	public String ngaySinhVaTinhCach() {
		String res = "";
		String url = "NgaySinhVaTinhCach.xml";
		url = baseUrl + url;
		String param = "link=" + link;
		res = poster.sendGetRequest(url, param);
		return parseData(res);
	}

	// link = AES(name&pass&1&15&code) code = 1402
	public String biQuyetHenHoQuaNgaySinh() {
		String res = "";
		String url = "BiQuyetHenHoQuaNgaySinh.xml";
		url = baseUrl + url;
		String param = "link=" + link;
		res = poster.sendGetRequest(url, param);
		return res;
	}

	// link = AES(name&pass&1&15&code) code = 1403
	public String biQuyetHonQuaNgaySinh() {
		String res = "";
		String url = "BiQuyetHonQuaNgaySinh.xml";
		url = baseUrl + url;
		String param = "link=" + link;
		res = poster.sendGetRequest(url, param);
		return res;
	}

	// link = AES(name&pass&1&15&NgaySinh&ThangSinh&NamSinh)
	public String tuViTronDoiNu() {
		String res = "";
		String url = "TuViChonDoiNu.xml";
		url = baseUrl + url;
		String param = "link=" + link;
		res = poster.sendGetRequest(url, param);
		return parseData(res);
	}

	// link = AES(name&pass&1&15&NgaySinh&ThangSinh&NamSinh)
	public String tuViTronDoiNam() {
		String res = "";
		String url = "TuViChonDoiNam.xml";
		url = baseUrl + url;
		String param = "link=" + link;
		res = poster.sendGetRequest(url, param);
		return parseData(res);
	}

	// link = AES(name&pass&1&15&NgaySinh&ThangSinh&NamSinh)
	public String tinhDuyenNu() {
		String res = "";
		String url = "TinhDuyenNu2011.xml";// TODO: change year
		url = baseUrl + url;
		String param = "link=" + link;
		res = poster.sendGetRequest(url, param);
		return res;
	}

	// link = AES(name&pass&1&15&NgaySinh&ThangSinh&NamSinh)
	public String tinhDuyenNam() {
		String res = "";
		String url = "TinhDuyenNam2011.xml";// TODO: change year
		url = baseUrl + url;
		String param = "link=" + link;
		res = poster.sendGetRequest(url, param);
		return res;
	}

	// link = AES(name&pass&1&15&NgaySinh&ThangSinh&NamSinh)
	public String taiLoc() {
		String res = "";
		String url = "TaiLoc2011.xml";// TODO: change year
		url = baseUrl + url;
		String param = "link=" + link;
		res = poster.sendGetRequest(url, param);
		return res;
	}

	// link = AES(name&pass&1&15&NgaySinh&ThangSinh&NamSinh)
	public String boiVanHan() {
		String res = "";
		String url = "BoiVanHan2011.xml";// TODO: change year
		url = baseUrl + url;
		String param = "link=" + link;
		res = poster.sendGetRequest(url, param);
		return res;
	}

	// link = AES(name&pass&1&15&NgaySinh&ThangSinh&NamSinh&ThangXem)
	public String boiVanHanThang() {
		String res = "";
		String url = "BoiVanHanThang2011.xml";// TODO: change year
		url = baseUrl + url;
		String param = "link=" + link;
		res = poster.sendGetRequest(url, param);
		return res;
	}

	// link = AES(name&pass&1&15&BenSoXe)
	public String boiBienSoXe() {
		String res = "";
		String url = "BoiBienSoXe.xml";
		url = baseUrl + url;
		String param = "link=" + link;
		res = poster.sendGetRequest(url, param);
		return res;
	}

	// link = AES(name&pass&1&15&SoChungMinhThu)
	public String boiChungMinhThu() {
		String res = "";
		String url = "BoiChungMinhThu.xml";
		url = baseUrl + url;
		String param = "link=" + link;
		res = poster.sendGetRequest(url, param);
		return res;
	}

	// link = AES(name&pass&1&15&SoSoDienThoai)
	public String boiSoDienThoai() {
		String res = "";
		String url = "BoiSoDienThoai.xml";
		url = baseUrl + url;
		String param = "link=" + link;
		res = poster.sendGetRequest(url, param);
		return parseData(res);
	}
	
	/*
	 * TODO: Truyen Cuoi
	 */
}
