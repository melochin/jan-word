package me.kazechin.janword.extra.kuromoji;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 片假名和平假名的映射关系
 */
public class KatakanaMap {

	protected static final HashMap<Character, Character> map;

	static {
		map = new HashMap<>();
		map.put('ア','あ');
		map.put('イ','い');
		map.put('ウ','う');
		map.put('エ','え');
		map.put('オ','お');

		map.put('カ','か');
		map.put('キ','き');
		map.put('ク','く');
		map.put('ケ','け');
		map.put('コ','こ');

		map.put('サ','さ');
		map.put('シ','し');
		map.put('ス','す');
		map.put('セ','せ');
		map.put('ソ','そ');

		map.put('タ','た');
		map.put('チ','ち');
		map.put('ツ','つ');
		map.put('テ','て');
		map.put('ト','と');

		map.put('ナ','な');
		map.put('ニ','に');
		map.put('ヌ','ぬ');
		map.put('ネ','ね');
		map.put('ノ','の');

		map.put('ハ','は');
		map.put('ヒ','ひ');
		map.put('フ','ふ');
		map.put('ヘ','へ');
		map.put('ホ','ほ');

		map.put('マ','ま');
		map.put('ミ','み');
		map.put('ム','む');
		map.put('メ','め');
		map.put('モ','も');

		// や
		map.put('ヤ','や');
		map.put('ユ','ゆ');
		map.put('ヨ','よ');

		// ら
		map.put('ラ','ら');
		map.put('リ','り');
		map.put('ル','る');
		map.put('レ','れ');
		map.put('ロ','ろ');

		map.put('ワ','わ');
		map.put('ヲ','を');

		map.put('ガ','が');
		map.put('ギ','ぎ');
		map.put('グ','ぐ');
		map.put('ゲ','げ');
		map.put('ゴ','ご');

		map.put('ザ','ざ');
		map.put('ジ','じ');
		map.put('ズ','ず');
		map.put('ゼ','ぜ');
		map.put('ゾ','ぞ');

		map.put('ダ','だ');
		map.put('ヂ','ぢ');
		map.put('ヅ','づ');
		map.put('デ','で');
		map.put('ド','ど');

		map.put('バ','ば');
		map.put('ビ','び');
		map.put('ブ','ぶ');
		map.put('ベ','べ');
		map.put('ボ','ぼ');

		map.put('パ','ぱ');
		map.put('ピ','ぴ');
		map.put('プ','ぷ');
		map.put('ペ','ぺ');
		map.put('ポ','ぽ');

		map.put('ャ','ゃ');
		map.put('ュ','ゅ');
		map.put('ョ','ょ');
		map.put('ァ','ぁ');

		map.put('ァ','ぁ');
		map.put('ィ','ぃ');
		map.put('ゥ','ぅ');
		map.put('ェ','ぇ');
		map.put('ォ','ぉ');
		map.put('ン','ん');
		map.put('ッ','っ');
	}

	public static Character get(Character character) {
		return map.get(character);
	}

}

