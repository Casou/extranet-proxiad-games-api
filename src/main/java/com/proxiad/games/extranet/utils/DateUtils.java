package com.proxiad.games.extranet.utils;

import java.time.LocalDate;
import java.time.Period;

public class DateUtils {

	public static int calculateAge(LocalDate birthDate, LocalDate currentDate) {
		if ((birthDate != null) && (currentDate != null)) {
			return Period.between(birthDate, currentDate).getYears();
		} else {
			return 0;
		}
	}

}
