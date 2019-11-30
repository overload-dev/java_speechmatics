package com.overload.ex;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.debug("connected!");
		return "home";
	}
	
	@RequestMapping(value ="/upload", method = RequestMethod.POST)
	public ModelAndView upload(MultipartHttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		MultipartFile file = request.getFile("audio");
		
		SpeechmaticsService ss = new SpeechmaticsService();
		
		String res = ss.uploadAudio(file);
		
		String jobId = ss.getlastestJobID();
		
		String transcript = ss.getTranscriptByJobID(jobId);
		
		mav.addObject("res", res);
		mav.addObject("transcript", transcript);
		mav.setViewName("result");
		return mav;
	}
	
}
