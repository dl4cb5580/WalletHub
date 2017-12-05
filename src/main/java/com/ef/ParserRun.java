package com.ef;
// Generated Dec 4, 2017 3:50:18 AM by Hibernate Tools 5.2.6.Final

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * ParserRun generated by hbm2java
 */
@Entity
@Table(name = "parser_run", catalog = "wallethub")
public class ParserRun implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private ParserRunId id;
	private String command;
	private String comments;

	public ParserRun() {
	}

	public ParserRun(ParserRunId id) {
		this.id = id;
	}

	public ParserRun(ParserRunId id, String command, String comments) {
		this.id = id;
		this.command = command;
		this.comments = comments;
	}

	@EmbeddedId
	@AttributeOverrides({ @AttributeOverride(name = "runId", column = @Column(name = "Run_ID", nullable = false)),
			@AttributeOverride(name = "clientIp", column = @Column(name = "Client_IP", nullable = false, length = 16)) })
	public ParserRunId getId() {
		return this.id;
	}

	public void setId(ParserRunId id) {
		this.id = id;
	}

	@Column(name = "Command", length = 128)
	public String getCommand() {
		return this.command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	@Column(name = "Comments", length = 512)
	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

}