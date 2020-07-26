package com.qwb.view.company.model;


import com.qwb.view.base.model.XbaseBean;

/**
 * 公司信息-图片
 */
public class CompanyPicBean extends XbaseBean{

//	{
//		"code": 200,
//			"data": {
//		"name": "/cloud/license/2019/11/07/61ca81accd3142ae9005f4cfcfe49c74.jpeg",
//				"bucket": "cloud",
//				"origin": "1573099497663_0.jpeg",
//				"size": 264700,
//				"url": "http://172.16.184.194:19500/cloud/license/2019/11/07/61ca81accd3142ae9005f4cfcfe49c74.jpeg",
//				"domain": "http://mp.qweib.com",
//				"ext": "jpeg",
//				"mime": "image/jpeg"
//	},
//		"success": true
//	}

	private Data data ;

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public static class Data{
		String name;
		String bucket;
		String origin;
		Long size;
		String url;
		String domain;
		String domextain;
		String mime;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getBucket() {
			return bucket;
		}

		public void setBucket(String bucket) {
			this.bucket = bucket;
		}

		public String getOrigin() {
			return origin;
		}

		public void setOrigin(String origin) {
			this.origin = origin;
		}

		public Long getSize() {
			return size;
		}

		public void setSize(Long size) {
			this.size = size;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getDomain() {
			return domain;
		}

		public void setDomain(String domain) {
			this.domain = domain;
		}

		public String getDomextain() {
			return domextain;
		}

		public void setDomextain(String domextain) {
			this.domextain = domextain;
		}

		public String getMime() {
			return mime;
		}

		public void setMime(String mime) {
			this.mime = mime;
		}
	}

}
