package me.kazechin.janword.extra.weblio;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.lang.Nullable;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Weblio {

	private WeblioHttp weblioHttp;

	public Weblio() {
		this.weblioHttp = new WeblioHttp();
	}

	public List<String> getMeanings(String word) {
		Document document = this.weblioHttp.getMeanings(word);

		Elements dics = document.select(".NetDicBody");

		if(dics.size() > 0) {
			return getMeaningsFromFirstDicBody(dics.get(0));
		}

		//日本語活用形辞書 Nhgkt
		dics = document.select(".Nhgkt");
		if (dics.size() > 0) {
			return Arrays.asList(dics.get(0).text());
		}

		// 地铁辞书
		dics = document.select(".Nchgj");
		if (dics.size() > 0) {
			return Arrays.asList(document.select(".Nchgj").first().text());
		}

		return document.select(".nrCntSgH").eachText();
	}

	private List<String> getMeaningsFromFirstDicBody(Element dicBody) {
		Elements elements = dicBody.select("div > a,span");

		return elements.stream()
				.distinct()
				.map(e -> e.parent().text())
				.distinct()
				.collect(toList());
	}

	@Nullable
	public List<String> getLexicalChange(String word) {
		Document document = this.weblioHttp.getLexicalChange(word);

		Elements elements = document.select(".Nhgkt");

		if (elements.size() == 0) return null;

		return document.select(".Nhgkt").eachText();
	}

	public static void main(String args[]) {
		Weblio weblio = new Weblio();
		// 伝いに 查找不到，会返回推荐词语
		// 行灯
		// ともり
		// 掻き立て
		// まる
		System.out.println(weblio.getMeanings("伝いに"));
	}

}
