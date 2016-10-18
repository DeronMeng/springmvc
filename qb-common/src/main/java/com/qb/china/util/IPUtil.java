package com.qb.china.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.qb.china.util.iputil.IPLocation;
import com.qb.china.util.iputil.IPSeeker;

public class IPUtil {

	// 纯真IP数据库名
	private static final String IP_FILE = "qqwry.dat";

	@SuppressWarnings({ "serial" })
	private static Map<String, String> COUNTRY_CONTINENT_MAPPING = new HashMap<String, String>() {
		{
			put("埃及", "非洲");
			put("利比亚", "非洲");
			put("突尼斯", "非洲");
			put("阿尔及利亚", "非洲");
			put("摩洛哥", "非洲");
			put("西撒哈拉", "非洲");
			put("毛里塔尼亚", "非洲");
			put("塞内加尔", "非洲");
			put("佛得角", "非洲");
			put("冈比亚", "非洲");
			put("几内亚比绍", "非洲");
			put("几内亚", "非洲");
			put("马里", "非洲");
			put("布基纳法索", "非洲");
			put("尼日尔", "非洲");
			put("乍得", "非洲");
			put("苏丹", "非洲");
			put("厄立特里亚", "非洲");
			put("吉布提", "非洲");
			put("索马里", "非洲");
			put("埃塞俄比亚", "非洲");
			put("中非", "非洲");
			put("喀麦隆", "非洲");
			put("尼日利亚", "非洲");
			put("贝宁", "非洲");
			put("多哥", "非洲");
			put("加纳", "非洲");
			put("科特迪瓦", "非洲");
			put("塞拉利昂", "非洲");
			put("利比里亚", "非洲");
			put("赤道几内亚", "非洲");
			put("圣多美和普林西比", "非洲");
			put("加蓬", "非洲");
			put("刚果", "非洲");
			put("刚果民主共和国", "非洲");
			put("乌干达", "非洲");
			put("卢旺达", "非洲");
			put("布隆迪", "非洲");
			put("肯尼亚", "非洲");
			put("坦桑尼亚", "非洲");
			put("莫桑比克", "非洲");
			put("马拉维", "非洲");
			put("赞比亚", "非洲");
			put("安哥拉", "非洲");
			put("纳米比亚", "非洲");
			put("博茨瓦纳", "非洲");
			put("津巴布韦", "非洲");
			put("南非", "非洲");
			put("斯威士兰", "非洲");
			put("莱索托", "非洲");
			put("塞舌尔", "非洲");
			put("科摩罗", "非洲");
			put("马达加斯加", "非洲");
			put("毛里求斯", "非洲");
			put("中国", "亚洲");
			put("香港", "亚洲");
			put("澳门", "亚洲");
			put("蒙古", "亚洲");
			put("朝鲜", "亚洲");
			put("韩国", "亚洲");
			put("日本", "亚洲");
			put("哈萨克斯坦", "亚洲");
			put("土库曼斯坦", "亚洲");
			put("乌兹别克斯坦", "亚洲");
			put("吉尔吉斯斯坦", "亚洲");
			put("塔吉克斯坦", "亚洲");
			put("格鲁吉亚", "亚洲");
			put("亚美尼亚", "亚洲");
			put("阿塞拜疆", "亚洲");
			put("土耳其", "亚洲");
			put("塞浦路斯", "亚洲");
			put("叙利亚", "亚洲");
			put("黎巴嫩", "亚洲");
			put("伊拉克", "亚洲");
			put("科威特", "亚洲");
			put("约旦", "亚洲");
			put("巴勒斯坦", "亚洲");
			put("以色列", "亚洲");
			put("沙特阿拉伯", "亚洲");
			put("巴林", "亚洲");
			put("卡塔尔", "亚洲");
			put("阿拉伯联合酋长国", "亚洲");
			put("也门", "亚洲");
			put("阿曼", "亚洲");
			put("伊朗", "亚洲");
			put("阿富汗", "亚洲");
			put("巴基斯坦", "亚洲");
			put("印度", "亚洲");
			put("斯里兰卡", "亚洲");
			put("马尔代夫", "亚洲");
			put("尼泊尔", "亚洲");
			put("不丹", "亚洲");
			put("孟加拉国", "亚洲");
			put("缅甸", "亚洲");
			put("泰国", "亚洲");
			put("老挝", "亚洲");
			put("柬埔寨", "亚洲");
			put("越南", "亚洲");
			put("马来西亚", "亚洲");
			put("新加坡", "亚洲");
			put("文莱", "亚洲");
			put("菲律宾", "亚洲");
			put("印度尼西亚", "亚洲");
			put("东帝汶", "亚洲");
			put("格陵兰岛", "北美洲");
			put("加拿大", "北美洲");
			put("圣皮埃尔和密克隆", "北美洲");
			put("美国", "北美洲");
			put("墨西哥", "北美洲");
			put("危地马拉", "北美洲");
			put("伯利兹", "北美洲");
			put("萨尔瓦多", "北美洲");
			put("洪都拉斯", "北美洲");
			put("尼加拉瓜", "北美洲");
			put("哥斯达黎加", "北美洲");
			put("百慕大", "北美洲");
			put("巴拿马", "北美洲");
			put("巴哈马", "北美洲");
			put("特克斯和凯科斯群岛", "北美洲");
			put("古巴", "北美洲");
			put("开曼群岛", "北美洲");
			put("牙买加", "北美洲");
			put("海地", "北美洲");
			put("多米尼加", "北美洲");
			put("波多黎各", "北美洲");
			put("美属维尔京群岛", "北美洲");
			put("英属维尔京群岛", "北美洲");
			put("安圭拉", "北美洲");
			put("蒙特塞拉特", "北美洲");
			put("安提瓜和巴布达", "北美洲");
			put("瓜德罗普", "北美洲");
			put("多米尼克", "北美洲");
			put("马提尼克", "北美洲");
			put("圣卢西亚", "北美洲");
			put("圣文森特和格林纳丁斯", "北美洲");
			put("阿鲁巴", "北美洲");
			put("巴巴多斯", "北美洲");
			put("格林纳达", "北美洲");
			put("特立尼达和多巴哥", "北美洲");
			put("安的列斯", "北美洲");
			put("圣其茨和尼维斯", "北美洲");
			put("委内瑞拉", "南美洲");
			put("哥伦比亚", "南美洲");
			put("圭亚那", "南美洲");
			put("苏里南", "南美洲");
			put("法属圭亚那", "南美洲");
			put("厄瓜多尔", "南美洲");
			put("秘鲁", "南美洲");
			put("巴西", "南美洲");
			put("玻利维亚", "南美洲");
			put("巴拉圭", "南美洲");
			put("智利", "南美洲");
			put("阿根廷", "南美洲");
			put("乌拉圭", "南美洲");
			put("马尔维纳斯群岛", "南美洲");
			put("福克兰群岛", "南美洲");
			put("冰岛", "欧洲");
			put("法罗群岛", "欧洲");
			put("挪威", "欧洲");
			put("瑞典", "欧洲");
			put("芬兰", "欧洲");
			put("爱尔兰", "欧洲");
			put("英国", "欧洲");
			put("丹麦", "欧洲");
			put("爱沙尼亚", "欧洲");
			put("拉脱维亚", "欧洲");
			put("立陶宛", "欧洲");
			put("白俄罗斯", "欧洲");
			put("俄罗斯", "欧洲");
			put("乌克兰", "欧洲");
			put("波兰", "欧洲");
			put("德国", "欧洲");
			put("荷兰", "欧洲");
			put("比利时", "欧洲");
			put("卢森堡", "欧洲");
			put("法国", "欧洲");
			put("摩纳哥", "欧洲");
			put("捷克", "欧洲");
			put("斯洛伐克", "欧洲");
			put("奥地利", "欧洲");
			put("列支敦士登", "欧洲");
			put("圣马力诺", "欧洲");
			put("意大利", "欧洲");
			put("梵蒂冈", "欧洲");
			put("瑞士", "欧洲");
			put("匈牙利", "欧洲");
			put("斯洛文尼亚", "欧洲");
			put("摩尔多瓦", "欧洲");
			put("罗马尼亚", "欧洲");
			put("塞尔维亚", "欧洲");
			put("黑山", "欧洲");
			put("克罗地亚", "欧洲");
			put("波斯尼亚和黑塞哥维那", "欧洲");
			put("保加利亚", "欧洲");
			put("马其顿", "欧洲");
			put("阿尔巴尼亚", "欧洲");
			put("希腊", "欧洲");
			put("安道尔", "欧洲");
			put("西班牙", "欧洲");
			put("葡萄牙", "欧洲");
			put("马耳他", "欧洲");
			put("澳大利亚", "大洋洲");
			put("新西兰", "大洋洲");
			put("巴布亚新几内亚", "大洋洲");
			put("所罗门群岛", "大洋洲");
			put("瓦努阿图", "大洋洲");
			put("新喀里多尼亚", "大洋洲");
			put("斐济群岛", "大洋洲");
			put("基里巴斯", "大洋洲");
			put("瑙鲁", "大洋洲");
			put("密克罗尼西亚", "大洋洲");
			put("马绍尔群岛", "大洋洲");
			put("帕劳", "大洋洲");
			put("北马里亚纳群岛", "大洋洲");
			put("关岛", "大洋洲");
			put("图瓦卢", "大洋洲");
			put("瓦利斯及富图纳", "大洋洲");
			put("西萨摩亚", "大洋洲");
			put("美属萨摩亚", "大洋洲");
			put("纽埃", "大洋洲");
			put("托克劳", "大洋洲");
			put("库克群岛", "大洋洲");
			put("汤加", "大洋洲");
			put("法属波利尼西亚", "大洋洲");
			put("皮特凯恩群岛", "大洋洲");
		}
	};

	@SuppressWarnings({ "serial" })
	public static Map<String, String> COUNTRY_CURRENCY_MAPPING = new HashMap<String, String>() {
		{
			put("埃及", "USD");
			put("利比亚", "USD");
			put("突尼斯", "USD");
			put("阿尔及利亚", "USD");
			put("摩洛哥", "USD");
			put("西撒哈拉", "USD");
			put("毛里塔尼亚", "USD");
			put("塞内加尔", "USD");
			put("佛得角", "USD");
			put("冈比亚", "USD");
			put("几内亚比绍", "USD");
			put("几内亚", "USD");
			put("马里", "USD");
			put("布基纳法索", "USD");
			put("尼日尔", "USD");
			put("乍得", "USD");
			put("苏丹", "USD");
			put("厄立特里亚", "USD");
			put("吉布提", "USD");
			put("索马里", "USD");
			put("埃塞俄比亚", "USD");
			put("中非", "USD");
			put("喀麦隆", "USD");
			put("尼日利亚", "USD");
			put("贝宁", "USD");
			put("多哥", "USD");
			put("加纳", "USD");
			put("科特迪瓦", "USD");
			put("塞拉利昂", "USD");
			put("利比里亚", "USD");
			put("赤道几内亚", "USD");
			put("圣多美和普林西比", "USD");
			put("加蓬", "USD");
			put("刚果", "USD");
			put("刚果民主共和国", "USD");
			put("乌干达", "USD");
			put("卢旺达", "USD");
			put("布隆迪", "USD");
			put("肯尼亚", "USD");
			put("坦桑尼亚", "USD");
			put("莫桑比克", "USD");
			put("马拉维", "USD");
			put("赞比亚", "USD");
			put("安哥拉", "USD");
			put("纳米比亚", "USD");
			put("博茨瓦纳", "USD");
			put("津巴布韦", "USD");
			put("南非", "USD");
			put("斯威士兰", "USD");
			put("莱索托", "USD");
			put("塞舌尔", "USD");
			put("科摩罗", "USD");
			put("马达加斯加", "USD");
			put("毛里求斯", "USD");
			put("中国", "CNY");
			put("香港", "HKD");
			put("澳门", "USD");
			put("蒙古", "USD");
			put("朝鲜", "USD");
			put("韩国", "USD");
			put("日本", "USD");
			put("哈萨克斯坦", "USD");
			put("土库曼斯坦", "USD");
			put("乌兹别克斯坦", "USD");
			put("吉尔吉斯斯坦", "USD");
			put("塔吉克斯坦", "USD");
			put("格鲁吉亚", "USD");
			put("亚美尼亚", "USD");
			put("阿塞拜疆", "USD");
			put("土耳其", "USD");
			put("塞浦路斯", "USD");
			put("叙利亚", "USD");
			put("黎巴嫩", "USD");
			put("伊拉克", "USD");
			put("科威特", "USD");
			put("约旦", "USD");
			put("巴勒斯坦", "USD");
			put("以色列", "USD");
			put("沙特阿拉伯", "USD");
			put("巴林", "USD");
			put("卡塔尔", "USD");
			put("阿拉伯联合酋长国", "USD");
			put("也门", "USD");
			put("阿曼", "USD");
			put("伊朗", "USD");
			put("阿富汗", "USD");
			put("巴基斯坦", "USD");
			put("印度", "USD");
			put("斯里兰卡", "USD");
			put("马尔代夫", "USD");
			put("尼泊尔", "USD");
			put("不丹", "USD");
			put("孟加拉国", "USD");
			put("缅甸", "USD");
			put("泰国", "USD");
			put("老挝", "USD");
			put("柬埔寨", "USD");
			put("越南", "USD");
			put("马来西亚", "USD");
			put("新加坡", "USD");
			put("文莱", "USD");
			put("菲律宾", "USD");
			put("印度尼西亚", "USD");
			put("东帝汶", "USD");
			put("格陵兰岛", "USD");
			put("加拿大", "USD");
			put("圣皮埃尔和密克隆", "USD");
			put("美国", "USD");
			put("墨西哥", "USD");
			put("危地马拉", "USD");
			put("伯利兹", "USD");
			put("萨尔瓦多", "USD");
			put("洪都拉斯", "USD");
			put("尼加拉瓜", "USD");
			put("哥斯达黎加", "USD");
			put("百慕大", "USD");
			put("巴拿马", "USD");
			put("巴哈马", "USD");
			put("特克斯和凯科斯群岛", "USD");
			put("古巴", "USD");
			put("开曼群岛", "USD");
			put("牙买加", "USD");
			put("海地", "USD");
			put("多米尼加", "USD");
			put("波多黎各", "USD");
			put("美属维尔京群岛", "USD");
			put("英属维尔京群岛", "USD");
			put("安圭拉", "USD");
			put("蒙特塞拉特", "USD");
			put("安提瓜和巴布达", "USD");
			put("瓜德罗普", "USD");
			put("多米尼克", "USD");
			put("马提尼克", "USD");
			put("圣卢西亚", "USD");
			put("圣文森特和格林纳丁斯", "USD");
			put("阿鲁巴", "USD");
			put("巴巴多斯", "USD");
			put("格林纳达", "USD");
			put("特立尼达和多巴哥", "USD");
			put("安的列斯", "USD");
			put("圣其茨和尼维斯", "USD");
			put("委内瑞拉", "USD");
			put("哥伦比亚", "USD");
			put("圭亚那", "USD");
			put("苏里南", "USD");
			put("法属圭亚那", "USD");
			put("厄瓜多尔", "USD");
			put("秘鲁", "USD");
			put("巴西", "USD");
			put("玻利维亚", "USD");
			put("巴拉圭", "USD");
			put("智利", "USD");
			put("阿根廷", "USD");
			put("乌拉圭", "USD");
			put("马尔维纳斯群岛", "USD");
			put("福克兰群岛", "USD");
			put("冰岛", "EUR");
			put("法罗群岛", "EUR");
			put("挪威", "EUR");
			put("瑞典", "EUR");
			put("芬兰", "EUR");
			put("爱尔兰", "EUR");
			put("英国", "EUR");
			put("丹麦", "EUR");
			put("爱沙尼亚", "EUR");
			put("拉脱维亚", "EUR");
			put("立陶宛", "EUR");
			put("白俄罗斯", "EUR");
			put("俄罗斯", "EUR");
			put("乌克兰", "EUR");
			put("波兰", "EUR");
			put("德国", "EUR");
			put("荷兰", "EUR");
			put("比利时", "EUR");
			put("卢森堡", "EUR");
			put("法国", "EUR");
			put("摩纳哥", "EUR");
			put("捷克", "EUR");
			put("斯洛伐克", "EUR");
			put("奥地利", "EUR");
			put("列支敦士登", "EUR");
			put("圣马力诺", "EUR");
			put("意大利", "EUR");
			put("梵蒂冈", "EUR");
			put("瑞士", "EUR");
			put("匈牙利", "EUR");
			put("斯洛文尼亚", "EUR");
			put("摩尔多瓦", "EUR");
			put("罗马尼亚", "EUR");
			put("塞尔维亚", "EUR");
			put("黑山", "EUR");
			put("克罗地亚", "EUR");
			put("波斯尼亚和黑塞哥维那", "EUR");
			put("保加利亚", "EUR");
			put("马其顿", "EUR");
			put("阿尔巴尼亚", "EUR");
			put("希腊", "EUR");
			put("安道尔", "EUR");
			put("西班牙", "EUR");
			put("葡萄牙", "EUR");
			put("马耳他", "EUR");
			put("澳大利亚", "USD");
			put("新西兰", "USD");
			put("巴布亚新几内亚", "USD");
			put("所罗门群岛", "USD");
			put("瓦努阿图", "USD");
			put("新喀里多尼亚", "USD");
			put("斐济群岛", "USD");
			put("基里巴斯", "USD");
			put("瑙鲁", "USD");
			put("密克罗尼西亚", "USD");
			put("马绍尔群岛", "USD");
			put("帕劳", "USD");
			put("北马里亚纳群岛", "USD");
			put("关岛", "USD");
			put("图瓦卢", "USD");
			put("瓦利斯及富图纳", "USD");
			put("西萨摩亚", "USD");
			put("美属萨摩亚", "USD");
			put("纽埃", "USD");
			put("托克劳", "USD");
			put("库克群岛", "USD");
			put("汤加", "USD");
			put("法属波利尼西亚", "USD");
			put("皮特凯恩群岛", "USD");
		}
	};

	public static IPInfo parseIpAddress(HttpServletRequest request, String ip) {
		String ipDBFilePath = request.getServletContext().getRealPath(IP_FILE);
		IPLocation ipLocation = IPSeeker.getInstance(ipDBFilePath).getIPLocation(ip);
		return handleCountry(ipLocation.getCountry());
	}

	public static IPInfo parseIpAddress(String ipDBFilePath, String ip) {
		IPLocation ipLocation = IPSeeker.getInstance(ipDBFilePath).getIPLocation(ip);
		return handleCountry(ipLocation.getCountry());
	}

	public static String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("http_client_ip");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		// 如果是多级代理，那么取第一个ip为客户ip
		if (ip != null && ip.indexOf(",") != -1) {
			ip = ip.substring(ip.lastIndexOf(",") + 1, ip.length()).trim();
		}
		return ip;
	}

	/**
	 * 对country进行处理， 如果是国内的ip地址， 则会显示XX省XX市，或者XX市XX区，不会显示国家 如果是国外的地址，则会显示国家名，如英国
	 * 
	 * @return
	 */
	private static IPInfo handleCountry(String country) {
		IPInfo ipInfo = new IPInfo();

		if (country != null && country.length() > 0) {
			// 这是在国内，并且不是直辖市
			if (country.contains("省")) {
				ipInfo.setCountry("中国");
				String[] array = country.split("省");
				if (array != null && array.length >= 2) {
					ipInfo.setProvice(array[0] + "省");
					ipInfo.setCity(array[1]);
				} else {
					ipInfo.setProvice(country);
				}
			} else if (country.contains("市")) {
				// 地址里面只有市，没有省，那很可能就是直辖市
				ipInfo.setCountry("中国");
				ipInfo.setProvice(country);
			} else if (country.equals("IANA")) {
				// 地址里面只有市，没有省，那很可能就是直辖市
				ipInfo.setCountry("中国");
			} else {
				// 不包括省，也不包括市，那就是国外了
				ipInfo.setCountry(country);
			}
			ipInfo.setContinent(COUNTRY_CONTINENT_MAPPING.get(ipInfo.getCountry()));
		}
		return ipInfo;
	}

	public static class IPInfo {
		private static final String DEFAULT_VALUE = "#";
		private String continent = DEFAULT_VALUE;
		private String country = DEFAULT_VALUE;
		private String provice = DEFAULT_VALUE;
		private String city = DEFAULT_VALUE;

		public String getContinent() {
			return continent;
		}

		public void setContinent(String continent) {
			this.continent = continent;
		}

		public String getCountry() {
			return country;
		}

		public void setCountry(String country) {
			this.country = country;
		}

		public String getProvice() {
			return provice;
		}

		public void setProvice(String provice) {
			this.provice = provice;
		}

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		@Override
		public String toString() {
			return "IPInfo [continent=" + continent + ", country=" + country + ", provice=" + provice + ", city="
					+ city + "]";
		}
	}
}
