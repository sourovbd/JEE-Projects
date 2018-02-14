package com.sv.pghms.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.sv.pghms.model.TCourseDetails;
import com.sv.pghms.model.TResultForm;
import com.sv.pghms.service.AdminService;

import groovy.ui.SystemOutputInterceptor;

@Controller
@RequestMapping("/main")
public class CourseController {
	
	@Autowired
	private AdminService adminService;
	
	//For showing CourseDetailsEntry.html page
	@RequestMapping(value="/saveCourseDetails", method=RequestMethod.GET)
	public String SaveCourseDetails(Model model){
		
		TCourseDetails courseDetailsForm = new TCourseDetails();
		List<TCourseDetails> courseDetailsFormList = new ArrayList<TCourseDetails>();
		
		try{
			courseDetailsFormList = adminService.getCourseDetailsList();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		model.addAttribute("courseDetailsForm",courseDetailsForm);
		model.addAttribute("courseDetailsFormList", courseDetailsFormList);
			
		return "CourseDetailsEntry";
	}
	// For saving entry from CourseDetailsEntry.html page
	@RequestMapping(value="/saveCourseDetails", method=RequestMethod.POST)
	public String SaveCourseDetails(@ModelAttribute("courseDetailsForm") TCourseDetails courseDetailsForm){
		
		try{
			
			adminService.insertCourseDetails(courseDetailsForm);
			
		}catch(Exception e) {
			
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		return "redirect:/main/saveCourseDetails";
	}
	//For course search page
	@RequestMapping(value="/courseViewPage", method=RequestMethod.GET)
	public String CourseViewPage(Model model){

		TCourseDetails courseDetailsForm = new TCourseDetails();
		List<TCourseDetails> courseDetailsFormList = new ArrayList<TCourseDetails>();
		
		try{
			courseDetailsFormList = adminService.getCourseDetailsList();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		model.addAttribute("courseDetailsForm",courseDetailsForm);
		model.addAttribute("courseDetailsFormList", courseDetailsFormList);
		
		return "SearchCourse";
	}
	//For course view page
	@RequestMapping(value="/viewCourses", method=RequestMethod.GET)
	public String ViewCourses(Model model,@RequestParam("courseNo") String courseNo, @RequestParam("batchNo") String batchNo){
		
		TCourseDetails courseDetailsForm = new TCourseDetails();
		List<TCourseDetails> courseDetailsFormList = new ArrayList<TCourseDetails>();
		System.out.println("courseNo: "+courseNo);
		System.out.println("batchNo: "+batchNo);
		try{
			courseDetailsFormList = adminService.getSingleCourse(courseNo, batchNo);
			System.out.println("courseDetailsFormList: "+courseDetailsFormList);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		model.addAttribute("courseDetailsForm",courseDetailsForm);
		model.addAttribute("courseDetailsFormList", courseDetailsFormList);
			
		return "CourseView";
	}
	// For edit   
	@RequestMapping(value="/editSingleCourse/{courseNo}/{batchNo}")
	public ModelAndView EditUser(@PathVariable("courseNo") String courseNo, @PathVariable("batchNo")  String batchNo) {
		
		ModelAndView model = new ModelAndView("CourseDetailsEntry");
		TCourseDetails courseDetailsForm = new TCourseDetails();
		List<TCourseDetails> courseDetailsFormList = new ArrayList<TCourseDetails>();
		
		try{
			courseDetailsFormList = adminService.getSingleCourse(courseNo, batchNo);
			System.out.println("courseDetailsFormList: "+courseDetailsFormList);
		}catch(Exception e){
			
			e.printStackTrace();
		}
		Iterator it = courseDetailsFormList.iterator();
		while(it.hasNext()){
			courseDetailsForm = (TCourseDetails) it.next();
		}
		
		model.addObject("courseDetailsForm",courseDetailsForm);
		model.addObject("courseDetailsFormList", courseDetailsFormList);
		return model;
	}
	// For delete 
	@RequestMapping(value="/deleteSingleCourse/{courseNo}/{batchNo}")
	public ModelAndView DeleteSingleCourse(@PathVariable("courseNo") String courseNo, @PathVariable("batchNo") String batchNo) {
		
		ModelAndView model = new ModelAndView("redirect:/main/saveCourseDetails");
		try{
			adminService.deleteSingleCourse(courseNo, batchNo);
		}catch(Exception e){
			
			e.printStackTrace();
		}
		return model;
	}
}
