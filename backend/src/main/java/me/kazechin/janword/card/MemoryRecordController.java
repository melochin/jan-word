package me.kazechin.janword.card;

import me.kazechin.janword.user.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO 了解时间API
@CrossOrigin
@RestController
public class MemoryRecordController {

	@Autowired
	private MemoryRecordDao memoryRecordDao;

	@GetMapping("/memory/record")
	public List<MemoryRecord> listRecord(@AuthenticationPrincipal UserInfo user,
										 @RequestParam("date")
										 @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {

		List<MemoryRecord> records = memoryRecordDao.list(user.getUserId(), date);

		return records;
	}

	@GetMapping("/memory/record/count")
	public Map<String, Object> listCountByDate(@AuthenticationPrincipal UserInfo user) {

		Map<String, Object> res = new HashMap<>();
		Date end = new Date();
		Date start = new Date();

		start.setYear(end.getYear() - 1);
		start.setMonth(end.getMonth() + 1);
		start.setDate(1);

		res.put("start", new SimpleDateFormat("yyyy-MM-dd").format(start));
		res.put("end", new SimpleDateFormat("yyyy-MM-dd").format(end));
		res.put("data", memoryRecordDao.groupByUserIdAndDate(user.getUserId(), start, end));

		return res;
	}

}
