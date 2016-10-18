package com.qb.china.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


@SuppressWarnings("serial")
public class Constans {
	/**后台用户session*/
	public static String M_SESSION_KEY = "m_session_key";
	public static String LOGIN_ADMIN_ID_SESSION_NAME = "loginAdminId";
	public static String WEB_CONTEXT_PATH = "Web_Context_Path";
	/**上传图片保存根目录 对应的 配置 key*/
	public static String FILE_UPLOAD_PATH = "File_Upload_Path";
	/**文件服务器地址对应的 配置 key*/
	public static String FILE_SERVER = "File_Server";
	public static String CDN_SERVER = "CDN_Server";
	public static String EXPORT_DIR = "Export_Dir";
	public static String EXCEL_DIR = "excel_dir";
	public static String ZIP_PATH = "zip_dir";

	/**app升级相关header*/
	public static String APP_VERSION = "AppVersion";
	public static String FORCE_UPGRADE = "ForceUpgrade";
	public static String UPGRADE_MESSAGE = "UpgradeMessage";

	public static String LOW_STOCK_THRESHOLD = "Low_Stock_Threshold";

	/**图片配置存放路径*/
	public static String IMAGE_CONFIG_DIR = "Image_Config_Dir";

	/**音频配置存放路径*/
	public static String AUDIO_CONFIG_DIR = "Audio_Config_Dir";

	/**** 查询数据库最大的id,生成订单编号  ***/
	public final static String ORDER_SN_NUMBER = "odr_id";
	/**** 订单编号格式的前缀***/
	public final static String ORDER_SN_FORMAT = "order.sn";
	

	/**欧元区域名称*/
	public static String EUR_REGION = "欧洲";
	/**亚洲*/
	public static String AISA_REGION = "亚洲";
	/**人民币区域名称*/
	public static String CNY_REGION = "中国";
	/**港币区域名称 */
	public static String HKD_REGION = "香港";

	public static final String CLIENT_TYPE_ANDROID = "Android";
	public static final String CLIENT_TYPE_IOS = "iOS";
	public static final String CLIENT_TYPE_WEB = "Web";
	public static final String CLIENT_TYPE_MOBILEWEB = "MobileWeb";
	public static final String CLIENT_TYPE_WECHAT = "WeChat";
	
	public class LANGUAGE {
		public static final String EN_US = "en";
		public static final String ZH_CN = "zh";
		public static final String ZH_TW = "tw";
		public static final String FR_FRA = "fr";
	}

	/**
	 * 商品参数
	 */
	public class PRODUCT {
		
	}


	/**
	 * 商品属性编号
	 */
	public class ATTRIBUTE {
		
	}


	public class PAGE {
		public static final String HOME = "Home";
		public static final String GALLERY = "Gallery";
		public static final String ABOUTUS = "Aboutus";
		public static final String JOURNAL = "JOURNAL";
	}

	public static final List<String> PAGE_NAMES = new ArrayList<String>() {
		{
			try {
				Field[] fields = PAGE.class.getDeclaredFields();
				for (Field field : fields) {
					if (!field.isSynthetic()) {
						this.add(field.get(PAGE.class).toString());
					}
				}
			} catch (Exception e) {
			}
		}
	};

	public static Map<String, String> product_mapping = new TreeMap<String, String>() {
		{
			try {
				Field[] fields = PRODUCT.class.getDeclaredFields();
				for (Field field : fields) {
					if (!field.isSynthetic()) {
						this.put(field.get(PRODUCT.class).toString(),
								field.getName().replace("_SN", "").replace("_", " "));
					}
				}
			} catch (Exception e) {
			}
		}
	};

	public static Map<String, String> arribute_mapping = new TreeMap<String, String>() {
		{
			try {
				Field[] fields = ATTRIBUTE.class.getDeclaredFields();
				for (Field field : fields) {
					if (!field.isSynthetic()) {
						this.put(field.get(ATTRIBUTE.class).toString(), field.getName().replace("_", " "));
					}
				}
			} catch (Exception e) {
			}
		}
	};

	public static List<String> page_list = new ArrayList<String>() {
		{
			try {
				Field[] fields = ATTRIBUTE.class.getDeclaredFields();
				for (Field field : fields) {
					if (!field.isSynthetic()) {
						this.add(field.get(ATTRIBUTE.class).toString());
					}
				}
			} catch (Exception e) {
			}
		}
	};

	public static final Map<String, String> productionIdMap = new TreeMap<String, String>() {
		{
		
		}
	};
	
	public static final Map<String, String> giftCardAttributeSnSMap = new TreeMap<String, String>() {
		{
			try {
				Field[] fields = ATTRIBUTE.class.getDeclaredFields();
				for (Field field : fields) {
					if (!field.isSynthetic()) {
						this.put(field.getName(), field.get(ATTRIBUTE.class).toString());
					}
				}
			} catch (Exception e) {
			}
		}
	};

	/**
	 * paypal
	 */
	public class PAYPAL {
		/**提交表单地址*/
		public static final String ACTION_URL = "paypal.payUrl";
		/**商户*/
		public static final String MERCHANT = "paypal.merchant";
		/**通知支付结果*/
		public static final String RETURN_URL = "paypal.callback.returnUrl";
		public static final String NOTIFY_URL = "paypal.callback.notifyUrl";

		public static final String MOBILE_SERVICE_ENDPOINT = "paypal.mobile.service.EndPoint";
		public static final String MOBILE_SERVICE_TYPE = "paypal.mobile.service.Type";
		public static final String MOBILE_CLIENTID = "paypal.mobile.clientID";
		public static final String MOBILE_CLIENTSECRET = "paypal.mobile.clientSecret";
		public static final String MOBILE_CANCELURL = "paypal.mobile.CancelUrl";
		public static final String MOBILE_RETURNURL = "paypal.mobile.ReturnUrl";
		public static final String MOBILE_ANDROID_CANCELURL = "paypal.mobile.android.cancelUrl";
		public static final String MOBILE_ANDROID_RETURNURL = "paypal.mobile.android.ReturnUrl";

	}

	/**
	 * paydollar
	 */
	public class PAYDOLLAR {
		/**提交表单地址*/
		public static final String ACTION_URL = "paydollar.payUrl";
		/**商户*/
		public static final String MERCHANT = "paydollar.merchant";
		/**secureHash用于验证商户的身份以及交易信息的完整 性 */
		public static final String SECUREHASH = "paydollar.secureHash";
		/**支付货币种类*/
		public static final String CURRCODE = "paydollar.currCode";
		/**"DCC" –开启 MPS 动态货币转换"MCP" –开启 MPS 多货币计价*/
		public static final String MPSMODE = "paydollar.mpsMode";
		/**支付类型 N消费交易 H预授权交易*/
		public static final String PAYTYPE = "paydollar.payType";
		/**语言*/
		public static final String LANG = "paydollar.lang";
		/**支付方式*/
		public static final String PAYMETHOD = "paydollar.payMethod";
		/**发生在 PayDollar 的支付成功/失败页面上对商家网站 自动复位向的秒数*/
		public static final String REDIRECT = "paydollar.redirect";
		/**通知支付结果*/
		public static final String CALLBACK_SUCCESS_URL = "paydollar.callback.success";
		public static final String CALLBACK_FAIL_URL = "paydollar.callback.fail";
		public static final String CALLBACK_CANCEL_URL = "paydollar.callback.cancel";
		/**登录商户后台用户名*/
		public static final String LOGIN_MERCHANTBACK_ACCOUNT = "paydollar.merchant.background.account";
		/**登录商户后台密码*/
		public static final String LOGIN_MERCHANTBACK_PASSWORD = "paydollar.merchant.background.password";
		/**查询接口地址*/
		public static final String QUERY_URL = "paydollar.query.url";

		public static final String MOBILE_MPSMODE = "paydollar.mobile.mpsMode";
		public static final String MOBILE_CALLBACK_SUCCESS = "paydollar.mobile.callback.success";
		public static final String MOBILE_CALLBACK_FAIL = "paydollar.mobile.callback.fail";
		public static final String MOBILE_CALLBACK_CANCEL = "paydollar.mobile.callback.cancel";
		public static final String MOBILE_ANDROID_CALLBACK_SUCCESS = "paydollar.mobile.android.callback.success";
		public static final String MOBILE_ANDROID_CALLBACK_FAIL = "paydollar.mobile.android.callback.fail";
		public static final String MOBILE_ANDROID_CALLBACK_CANCEL = "paydollar.mobile.android.callback.cancel";
	}

	/**
	 * alipay
	 */
	public class ALIPAY {
		/**商户*/
		public static final String MERCHANT = "alipay.merchant";
		/**通知支付结果*/
		public static final String RETURN_URL = "alipay.callback.returnUrl";
		public static final String NOTIFY_URL = "alipay.callback.notifyUrl";
		public static final String MOBILE_NOTIFY_URL = "mobile.alipay.callback.notifyUrl";
	}

	/**
	 * union
	 */
	public class UNION {
		/**提交表单地址*/
		public static final String ACTION_URL = "chinapay.payment.url";
		/**商户*/
		public static final String MERCHANT = "chinapay.merid";
		/**Mer Key*/
		public static final String MER_KEY = "chinapay.merkey.filepath";
		/**Public Key*/
		public static final String PUBLIC_KEY = "chinapay.pubkey.filepath";
		/**KeyUsage*/
		public static final String KEYUSAGE = "chinapay.keyusage";
		/**签名版本*/
		public static final String VERSION = "chinapay.sign.version";
		/**支付币种*/
		public static final String CURR_CODE = "chinapay.currCode";
		/**交易类型*/
		public static final String TRANS_TYPE = "chinapay.transtype";
		public static final String MOBILE_TRANS_TYPE = "chinapay.mobile.transtype";
		/**商户私有域*/
		public static final String MERCHANT_PRIV1 = "chinapay.priv1";
		/**退款*/
		public static final String REFUND_URL = "chinapay.refund.url";
		/**查询*/
		public static final String QUERY_URL = "chinapay.query.url";
		/**后台应答回调*/
		public static final String BG_URL = "chinapay.callback.bg.url";
		/**前端回调*/
		public static final String CALLBACK_URL = "chinapay.callback.url";
		/**支付网关号*/
		public static final String GATE_ID = "chinapay.gate.id";
		/**以下均是手机端相关参数 商户*/
		public static final String MOBILE_MERCHANT = "chinapay.mobile.merchant";
		/**成功后发起的回调*/
		public static final String SUCCESS_URL = "chinapay.mobile.success.url";
		/**付款安全码*/
		public static final String SECRITY_KEY = "chinapay.mobile.security.key";
		/**版本号*/
		public static final String MOBILE_VERSION = "chinapay.mobile.version";
		/**编码*/
		public static final String MOBILE_CHARSET = "chinapay.mobile.charset";
		/**加密方式*/
		public static final String SIGN_METHOD = "chinapay.mobile.sign.method";
		/**交易接口*/
		public static final String TRDE_URL = "chinapay.mobile.trade.url";
		/**查询接口*/
		public static final String MOBILE_QUERY_URL = "chinapay.mobile.query.url";
		public static final String CHINAPAY_MOBILE_SERVICE_TYPE = "chinapay.mobile.serivce.type";
	}

	/**
	 * wechat
	 */
	public class WX {
		/**异步通知回调地址*/
		public static final String NOTIFY_URL = "wx.callback.notifyUrl";
	}

	public static Map<String, Integer> collectionProductionDaysMap = new HashMap<String, Integer>();
	public static Map<String, Integer> albumProductionDaysMap = new HashMap<String, Integer>();
	public static Map<String, Integer> premiumPrintsProductionDaysMap = new HashMap<String, Integer>();
	public static Map<String, Integer> giftcardProductionDaysMap = new HashMap<String, Integer>();

	/**
	 * 测试金额
	 */
	public class PAYMENT {
		public static final String TEST_FEE = "testing.total.fee";
	}

	/**
	 * 邮件模版常量参数
	 */
	public class EMAIL {

	}

	/***库存预占时间点（下订单、订单付款、订单发货）*/
	public enum StoreFreezeTime {
		order, payment, ship
	}

	public class IMAGE_MINIMUM_LIMIT {
		public static final int PRINTS_IMG = 80 * 1024;
		

	}
}
