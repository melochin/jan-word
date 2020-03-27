package me.kazechin.janword.extra.weblio;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;

public class WeblioHttp {

	private static final String url = "https://www.weblio.jp/";

	public Document getMeanings(String word) {
		try {
			return Jsoup.parse(new URL(url + "/content" + "/" + word), 10*000);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 活用形辞書
	 * @param word
	 * @return
	 */
	public Document getLexicalChange(String word) {
		try {
			return Jsoup.parse(
					new URL(url + "/content" + "/" + word + "?dictCode=NHGKT"), 10*000);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
