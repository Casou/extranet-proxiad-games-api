package com.proxiad.games.extranet.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proxiad.games.extranet.annotation.BypassSecurity;

@RestController
public class PingController {

	@GetMapping("/ping")
	@BypassSecurity
	public void ping() {

	}

}
