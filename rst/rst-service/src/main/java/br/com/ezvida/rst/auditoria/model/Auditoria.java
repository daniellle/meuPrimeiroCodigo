package br.com.ezvida.rst.auditoria.model;

import java.io.Serializable;

public class Auditoria implements Serializable {

	private static final long serialVersionUID = 1320900584491998992L;

	private String loggerClassName;

	private String level;

	private String message;

	private String ndc;

	private Mdc mdc;

	private String threadName;

	private String threadId;

	private String sequence;
	
	private String timestamp;

	private String port;


	private String version;

	private String host;

	private String loggerName;

	public String getLoggerClassName() {
		return loggerClassName;
	}

	public void setLoggerClassName(String loggerClassName) {
		this.loggerClassName = loggerClassName;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getNdc() {
		return ndc;
	}

	public void setNdc(String ndc) {
		this.ndc = ndc;
	}

	public Mdc getMdc() {
		return mdc;
	}

	public void setMdc(Mdc mdc) {
		this.mdc = mdc;
	}

	public String getThreadName() {
		return threadName;
	}

	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}

	public String getThreadId() {
		return threadId;
	}

	public void setThreadId(String threadId) {
		this.threadId = threadId;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getLoggerName() {
		return loggerName;
	}

	public void setLoggerName(String loggerName) {
		this.loggerName = loggerName;
	}
	
	

}