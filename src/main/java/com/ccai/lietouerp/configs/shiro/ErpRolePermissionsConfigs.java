package com.ccai.lietouerp.configs.shiro;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.ccai.lietouerp.auth.ErpRolePermissions;

/**
 * 角色权限配置类
 * @author Administrator
 *
 */
@ConfigurationProperties(prefix = "erp.authp",locations={"classpath:config/roles-permissions.yml"})
@Component
public class ErpRolePermissionsConfigs {

	private List<ErpRolePermissions> roles;

	public List<ErpRolePermissions> getRoles() {
		return roles;
	}

	public void setRoles(List<ErpRolePermissions> roles) {
		this.roles = roles;
	}
	
	
	public ErpRolePermissions getErpRolePermissions(String role){
		if(roles!=null && roles.size()>0){
			for (int i = 0; i < roles.size(); i++) {
				ErpRolePermissions erole=roles.get(i);
				if(erole.getRole().equalsIgnoreCase(role)){
					return erole;
				}
			}
		}
		return null;
	}
	
}
