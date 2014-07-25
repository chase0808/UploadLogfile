package htmlLogfileExtractor;

public class Transaction {
	private String clientIpAddress;
	private String unknown;
	private String remoteLogname;
	private String authenticatedUsername;
	private String requestTimestamp;
	private String requestLine;
	private String statusCode;
	private String bytes;
	private String referrer;
	private String userAgent;
	private String filename;
	private String requestMethod;
	private String transportProtocol;
	private String requestStem;
	private String requestQueryString;

	public String getClientIpAddress() {
		return clientIpAddress;
	}

	public void setClientIpAddress(String clientIpAddress) {
		this.clientIpAddress = clientIpAddress;
	}

	public String getUnknown() {
		return unknown;
	}

	public void setUnknown(String unknown) {
		this.unknown = unknown;
	}

	public String getRemoteLogname() {
		return remoteLogname;
	}

	public void setRemoteLogname(String remoteLogname) {
		this.remoteLogname = remoteLogname;
	}

	public String getAuthenticatedUsername() {
		return authenticatedUsername;
	}

	public void setAuthenticatedUsername(String authenticatedUsername) {
		this.authenticatedUsername = authenticatedUsername;
	}

	public String getRequestTimestamp() {
		return requestTimestamp;
	}

	public void setRequestTimestamp(String requestTimestamp) {
		this.requestTimestamp = requestTimestamp;
	}

	public String getRequestLine() {
		return requestLine;
	}

	public void setRequestLine(String requestLine) {
		this.requestLine = requestLine;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getBytes() {
		return bytes;
	}

	public void setBytes(String bytes) {
		this.bytes = bytes;
	}

	public String getReferrer() {
		return referrer;
	}

	public void setReferrer(String referrer) {
		this.referrer = referrer;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}

	public String getTransportProtocol() {
		return transportProtocol;
	}

	public void setTransportProtocol(String transportProtocol) {
		this.transportProtocol = transportProtocol;
	}

	public String getRequestStem() {
		return requestStem;
	}

	public void setRequestStem(String requestStem) {
		this.requestStem = requestStem;
	}

	public String getRequestQueryString() {
		return requestQueryString;
	}

	public void setRequestQueryString(String requestQueryString) {
		this.requestQueryString = requestQueryString;
	}
}
