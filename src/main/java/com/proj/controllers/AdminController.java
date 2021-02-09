package com.proj.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.proj.entities.Admin;
import com.proj.entities.Medecin;
import com.proj.repositories.AdminRepository;
import com.proj.services.AdminService;
import com.proj.vo.ResponseTemplateVO;
import com.proj.vo.ResponseTemplateVO2;

@RestController
@RequestMapping("/Personnel")
public class AdminController {

	@Autowired
	private AdminService adminService;
	
	@Autowired
	private AdminRepository adminrepo;
	
	@PostMapping("/Admin/add")
    public Admin addAdmin(@RequestBody Admin admin) {
        return adminService.saveAdmin(admin);
    }

	
	@GetMapping("/Admin/admins")
    public List<Admin> findAllAdmins() {
        return adminService.getAdmins();
    }
	
	@GetMapping("/Admin/adminById/{id}")
    public Admin findAdminById(@PathVariable Long id) {
        return adminService.getAdminById(id);
    }
	
	@HystrixCommand(fallbackMethod = "callCompte_Fallback")
	@GetMapping("/Admin/adminWithCompte/{id}")
    public ResponseTemplateVO2 getAdminWithCompte(@PathVariable("id") Long adminId) {
        return adminService.getAdminWithCompte(adminId);
    }
	
	@PutMapping("/Admin/update")
    public Admin updateAdmin(@RequestBody Admin admin) {
        return adminService.updateAdmin(admin);
    }
	
	@DeleteMapping("/Admin/delete/{id}")
    public String deleteAdmin(@PathVariable Long id) {
        return adminService.deleteAdmin(id);
    }
	
	private ResponseTemplateVO2 callCompte_Fallback(Long id) {
		 
        System.out.println("Admin Service is down!!! fallback route enabled...");
 
        ResponseTemplateVO2 rt = new ResponseTemplateVO2();
        Admin admin = adminrepo.findById(id).orElse(null);
        
        rt.setAdmin(admin);
        
        return rt;
    }
}
