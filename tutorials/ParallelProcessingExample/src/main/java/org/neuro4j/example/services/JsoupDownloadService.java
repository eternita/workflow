package org.neuro4j.example.services;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

@Service
public class JsoupDownloadService implements DownloadService{

	@Override
	public String download(String url) throws IOException {
		Document doc = Jsoup.connect(url).validateTLSCertificates(false).get();
		if (doc != null) {
			String content = doc.html();
			return content;
		}
		return "";
	}

}
