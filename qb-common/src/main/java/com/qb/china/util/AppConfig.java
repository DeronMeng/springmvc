package com.qb.china.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class AppConfig extends PropertyPlaceholderConfigurer {
	private static Logger log = Logger.getLogger(AppConfig.class);

	public static String FILE_SERVER;
	public static String CDN_SERVER;
	public static int LOW_STOCK_THRESHOLD;

	private static Map<String, String> propertiesMap;
	private static Pattern placeholderPattern = Pattern.compile("\\{.*?\\}");

	// 货币种类（人民币、美元、欧元、英磅、加拿大元、澳元、卢布、港币、新台币、韩元、新加坡元、新西兰元、日元、马元、瑞士法郎、瑞典克朗、丹麦克朗、兹罗提、挪威克朗、福林、捷克克朗、葡币）
	public static enum CurrencyType {
		CNY, USD, EUR, HKD
	};

	@SuppressWarnings("serial")
	public static Map<String, String> CURRENCY_SYMBOL_MAPPING = new HashMap<String, String>() {
		{
			this.put(CurrencyType.CNY.toString(), "￥");
			this.put(CurrencyType.USD.toString(), "$");
			this.put(CurrencyType.EUR.toString(), "€");
			this.put(CurrencyType.HKD.toString(), "$");
		}
	};

	// 小数位精确方式（四舍五入、向上取整、向下取整）
	public static enum RoundType {
		roundHalfUp, roundUp, roundDown
	}

	// 库存预占时间点（下订单、订单付款、订单发货）
	public static enum StoreFreezeTime {
		order, payment, ship
	}

	// 水印位置（无、左上、右上、居中、左下、右下）
	public static enum WatermarkPosition {
		no, topLeft, topRight, center, bottomLeft, bottomRight
	}

	// 积分获取方式（禁用积分获取、按订单总额计算、为商品单独设置）
	public static enum PointType {
		disable, orderAmount, productSet
	}

	// 获取价格（根据精确小数点位数和四舍五入类型）
	public static BigDecimal getPriceScaleBigDecimal(BigDecimal price) {
		Integer orderScale = Integer.parseInt(getProperty("Price_Scale"));
		String roundType = getProperty("Price_Round_Type");
		if (RoundType.roundHalfUp.toString().equals(roundType)) {
			return price.setScale(orderScale, BigDecimal.ROUND_HALF_UP);
		} else if (RoundType.roundUp.toString().equals(roundType)) {
			return price.setScale(orderScale, BigDecimal.ROUND_UP);
		} else {
			return price.setScale(orderScale, BigDecimal.ROUND_DOWN);
		}
	}

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props)
			throws BeansException {
		super.processProperties(beanFactory, props);

		propertiesMap = new HashMap<String, String>();
		for (Object key : props.keySet()) {
			String keyStr = key.toString();
			String valueStr = props.getProperty(keyStr);

			if (valueStr != null) {
				Matcher matcher = placeholderPattern.matcher(valueStr);
				StringBuilder value = new StringBuilder(valueStr.length());
				int index = 0;
				while (matcher.find()) {
					int start = matcher.start();
					int end = matcher.end();
					String template = valueStr.substring(start + 1, end - 1);
					String templateVal = props.getProperty(template);
					if (templateVal == null || templateVal.isEmpty()) {
						value.append(valueStr.substring(index, end));
					} else {
						value.append(valueStr.substring(index, start));
						value.append(templateVal);
					}
					index = end;
				}
				value.append(valueStr.substring(index));
				valueStr = value.toString();
			}
			propertiesMap.put(keyStr, valueStr);
		}

		FILE_SERVER = getProperty(Constans.FILE_SERVER);
		CDN_SERVER = getProperty(Constans.CDN_SERVER);
		LOW_STOCK_THRESHOLD = getInt(Constans.LOW_STOCK_THRESHOLD);
	}

	public static String getProperty(String name) {
		return propertiesMap.get(name);
	}

	public static int getInt(String name) {
		int result = 0;
		try {
			result = Integer.parseInt(propertiesMap.get(name));
		} catch (Exception e) {
			log.error("invalid property value for '" + name + "'.", e);
		}
		return result;
	}
}