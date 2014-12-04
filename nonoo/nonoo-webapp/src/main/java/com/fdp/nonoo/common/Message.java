package com.fdp.nonoo.common;

public class Message {
	private Type type;
	private String content;

	public Message() {
	}

	public Message(Type type, String content) {
		this.type = type;
		this.content = content;
	}

	public static Message success(String content) {
		return new Message(Type.success, content);
	}

	public static Message warnning(String content) {
		return new Message(Type.warnning, content);
	}

	public static Message error(String content) {
		return new Message(Type.danger, content);
	}

	public Type getType() {
		return this.type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String toString() {
		return content;
	}

	public static enum Type {
		success,info, warnning, danger
		
	}

}