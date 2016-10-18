package com.qb.china.domain;

import javax.persistence.Transient;


public class ProductType implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	public static final String ROOT_SN = "0";
	public static final String COLLECTION_SN = "01";
	public static final String PRINTS_SN = "011";
	public static final String INTEGER_PRINTS = "0111";
	public static final String FINE_ART_EDITION = "0112";
	public static final String TABLE_TOP_PRINTS = "0113";
	public static final String POSTCARD = "0114";
	public static final String ALBUM_SN = "012";
	public static final String PREMIUM_PRINTS_SN = "013";
	public static final String FRAMED_SLIVER_SN = "0131";
	public static final String ULTRASECM_SN = "0132";
	public static final String GIFTCARD_SN = "014";
	public static final String GIFTCARD_EMAIL_SN = "0141";
	public static final String GIFTCARD_PHSICAL_SN = "0142";

	public static final String GALLERY_SN = "02";
	public static final String GALLERY_AUTHORIZED_SN = "021";
	public static final String GALLERY_LIMITED_SN = "022";
	public static final String GALLERY_SIGNED_SN = "023";
	public static final String DARKROOM_SN = "03";
	public static final String DARKROOM_SLIVER_SN = "031";
	public static final String DARKROOM_GOLD_SN = "032";
	public static final String DARKROOM_PLATINUM_SN = "033";

	public static final String MINI_SITE = "04";
	public static final String MINI_SITE_POSTCARD = "041";
	public static final String MINI_SITE_FINEART = "042";

	public static final String[] COLLECTION_SUB_TYPE_LIST = { PRINTS_SN, ALBUM_SN, PREMIUM_PRINTS_SN, GIFTCARD_SN };

	public static final String[] MOBILE_COLLECTION_SUB_TYPE_LIST = { INTEGER_PRINTS, TABLE_TOP_PRINTS, POSTCARD,
			FINE_ART_EDITION, ALBUM_SN, FRAMED_SLIVER_SN, GIFTCARD_SN };


	private String productTypeSn;
	private String title;
	private String cname;
	private String ename;
	private String tname;
	private String fname;
	private int rank; // 类别的层级

	private Long parentId;// 上级分类

	@Transient
	public String getName(boolean isEnLanguage) {
		if (isEnLanguage) {
			return getEname();
		} else {
			return getCname();
		}
	}

	public String getProductTypeSn() {
		return productTypeSn;
	}

	public void setProductTypeSn(String productTypeSn) {
		this.productTypeSn = productTypeSn;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getEname() {
		return this.ename;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public void setEname(String ename) {
		this.ename = ename;
	}

	public String getTname() {
		return tname;
	}

	public void setTname(String tname) {
		this.tname = tname;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
}
