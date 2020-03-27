package me.kazechin.janword.extra.weblio;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
public class WeblioController {


	private Weblio weblio;

	public WeblioController(Weblio weblio) {
		this.weblio = weblio;
	}

	@GetMapping("/weblio")
	public List<String> getMeanings(@RequestParam("word") String word) {
		return weblio.getMeanings(word);
	}

	@GetMapping("/weblio/lexical")
	public List<String> getLexicalChange(@RequestParam("word") String word) {
		return weblio.getLexicalChange(word);
	}
}
