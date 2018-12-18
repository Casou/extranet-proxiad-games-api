package com.proxiad.games.extranet.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum LanguageEnum {

	FRANCAIS("Français", "france"),
	ANGLAIS("Anglais", "angleterre"),
	ALLEMAND("Allemand", "allemagne"),
	ARABE("Arabe", "arabie-saoudite"),
	BELGE("Belge", "belgique"),
	BRESILIEN("Brésilien", "bresil"),
	CANADIEN("Canadien-Québécois", "canada"),
	COREEN("Coréen", "coree"),
	EGYPTIEN("Egypte", "egypte"),
	GREC("Grec", "grece"),
	INDIEN("Indien", "inde"),
	ITALIEN("Italien", "italie"),
	JAPONAIS("Japnais", "japon"),
	KLINGON("Klingon", "klingon"),
	SWATI("Swati", "swaziland")
	;

	private String label;
	private String icon;

	LanguageEnum(String label, String icon) {
		this.label = label;
		this.icon = icon;
	}

	public static List<LanguageEnum> list() {
		return Arrays.asList(LanguageEnum.values()).stream()
				.filter(e -> e != FRANCAIS)
				.collect(Collectors.toList());
	}

}
