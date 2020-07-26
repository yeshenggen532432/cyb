package com.qwb.view.base.model;


/**
 *	Jwt
 */

public class JwtBean {

//	"jwt": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJjb21wYW55SWQiOjU5MCwibW9iaWxlIjoiMTM4NTk5ODU0MDQiLCJleHAiOjE1ODc3MTcwNDQsInVzZXJJZCI6MjIzMX0.h71mkOOkG_7oC3-xW_gxZtEGFQrdCjeOEpzlJ7N2xcU",
//			"domain": "http://lbc.vip.qweib.com",
//			"to": 139,
//			"from": 590


	private String jwt;
	private String domain;
	private String to;
	private String from;

	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}
}
