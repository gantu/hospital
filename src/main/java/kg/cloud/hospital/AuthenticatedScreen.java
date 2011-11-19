package kg.cloud.hospital;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class AuthenticatedScreen extends VerticalLayout{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MyVaadinApplication app;


	public AuthenticatedScreen(MyVaadinApplication app)
    {
        super();
        this.app = app;

        Subject currentUser = SecurityUtils.getSubject();
        Label label = new Label("Logged in as " +  currentUser.getPrincipal().toString());
        
        Button admin = new Button("For administrators only");
        Button user  = new Button("For users only");
        if (!currentUser.hasRole("admin"))
        {
        	admin.setEnabled(false);
        } 
        else if (!currentUser.hasRole("user"))
        {
        	user.setEnabled(false);
        }
        
        
        Button perm = new Button("For all with permission 'permission_2' only");
        if(!currentUser.isPermitted("permission_2"))
        {
        	perm.setEnabled(false);
        }
        
        
        Button logout = new Button("logout");
        logout.addListener(new MyVaadinApplication.LogoutListener(this.app));

        this.addComponent(label);
        
        this.addComponent(admin);
        this.addComponent(user);
        this.addComponent(perm);
        this.addComponent(logout);
    
    }

}
