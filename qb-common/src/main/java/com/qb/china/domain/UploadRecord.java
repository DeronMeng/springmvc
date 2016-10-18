package com.qb.china.domain;

import java.util.Date;

public class UploadRecord{
	public static final int DEFAULT_PAGE_SIZE = 50;

	public static final int STATUS_CANCELLED = -3;
	public static final int STATUS_PROCESS_FAILED = -2;
	public static final int STATUS_UPLOAD_FAILED = -1;
	public static final int STATUS_PENDING = 0;
	public static final int STATUS_UPLOADING = 1;
	public static final int STATUS_FINISHED = 2;

	public static final int UPLOAD_TYPE_PRODUCT = 1;
	public static final int UPLOAD_TYPE_GALLARY = 2;
	public static final int UPLOAD_TYPE_MEMBER_REQUEST = 3;

	private String uploadSn;
	private int fileCount;
	private long duration;
	private String server;
	private String uploadPath;
	private long totalSize;
	private long uploadedSize;
	private long uploadedPercent;
	private int fileIndex = 0;

	private String country;
	private String provice;
	private String city;
	private String ip;
	private String clientType;
	private String userAgent;
	private String os;
	private String browser;
	private int uploadType;
	private boolean foceClosed;
	private String comment;
	private int status = STATUS_PENDING;
	
	private Date createDate;
	private Date modifyDate;

	synchronized public int getStatus() {
		return this.status;
	}

	synchronized public void setStatus(int status) {
		this.status = status;
	}

	public String getUploadSn() {
		return this.uploadSn;
	}

	public void setUploadSn(String uploadSn) {
		this.uploadSn = uploadSn;
	}

	synchronized public int getFileCount() {
		return this.fileCount;
	}

	synchronized public void setFileCount(int fileCount) {
		this.fileCount = fileCount;
	}

	public int getUploadType() {
		return uploadType;
	}

	public void setUploadType(int uploadType) {
		this.uploadType = uploadType;
	}

	public long getTotalSize() {
		return this.totalSize;
	}

	public void setTotalSize(long totalSize) {
		this.totalSize = totalSize;
	}

	synchronized public int getFileIndex() {
		return this.fileIndex;
	}

	synchronized public void setFileIndex(int fileIndex) {
		this.fileIndex = fileIndex;
	}

	public long getDuration() {
		long duration = (getModifyDate().getTime() - getCreateDate().getTime()) / 1000;
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	synchronized public long getUploadedSize() {
		return uploadedSize;
	}

	synchronized public void setUploadedSize(long uploadedSize) {
		this.uploadedSize = uploadedSize;
	}

	synchronized public long getUploadedPercent() {
		long uploadedPercent = 0;
		if (this.getTotalSize() != 0) {
			uploadedPercent = (int) (getUploadedSize() * 100 / getTotalSize());
		}
		return uploadedPercent;
	}

	synchronized public void setUploadedPercent(long uploadedPercent) {
		this.uploadedPercent = uploadedPercent;
	}

	public String getClientType() {
		return this.clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
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

	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUserAgent() {
		return this.userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getUploadPath() {
		return uploadPath;
	}

	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}

	public boolean isFoceClosed() {
		return foceClosed;
	}

	public void setFoceClosed(boolean foceClosed) {
		this.foceClosed = foceClosed;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	@Override
	public String toString() {
		return "UploadInfo [uploadSn=" + uploadSn + ", fileCount=" + fileCount + ", duration=" + duration
				+ ", uploadPath=" + uploadPath + ", totalSize=" + totalSize + ", uploadedSize=" + uploadedSize
				+ ", uploadedPercent=" + uploadedPercent + ", fileIndex=" + fileIndex + ", ip=" + ip + ", clientType="
				+ clientType + ", userAgent=" + userAgent + ", os=" + os + ", browser=" + browser + ", status="
				+ status + "]";
	}
}
