package me.kazechin.janword.controller;

import me.kazechin.janword.model.MemoryRecord;
import me.kazechin.janword.dao.MemoryRecordDao;
import me.kazechin.janword.user.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 记忆记录 API
 */
@CrossOrigin
@RestController
public class MemoryRecordController {

	@Autowired
	private MemoryRecordDao memoryRecordDao;

	/**
	 * 读取用户某一天的记忆记录
	 * @param user
	 * @param date
	 * @return
	 */
	@GetMapping("/memory/record")
	public List<MemoryRecord> listRecord(@AuthenticationPrincipal UserInfo user,
										 @RequestParam("date")
										 @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {

		List<MemoryRecord> records = memoryRecordDao.list(user.getUserId(), date);

		return records;
	}

	/**
	 * 对用户近一年的记录情况，进行汇总
	 * @param user
	 * @return
	 */
	@GetMapping("/memory/record/count")
	public Map<String, Object> listCountByDate(@AuthenticationPrincipal UserInfo user) {
		Map<String, Object> res = new HashMap<>();
		LocalDate end = LocalDate.now();
		LocalDate start = end.minusYears(1).plusMonths(1).withDayOfMonth(1);

		res.put("start", start.toString());
		res.put("end", end.toString());
		res.put("data", memoryRecordDao.groupByUserIdAndDate(user.getUserId(), start, end));

		return res;
	}

}
