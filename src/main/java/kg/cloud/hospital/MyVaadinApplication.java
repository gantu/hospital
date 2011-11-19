/*
 * Copyright 2009 IT Mill Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package kg.cloud.hospital;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import com.vaadin.Application;
import com.vaadin.service.ApplicationContext;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
public class MyVaadinApplication extends Application implements ApplicationContext.TransactionListener
{
	private static ThreadLocal<MyVaadinApplication> currentApplication=new ThreadLocal<MyVaadinApplication>();
    private Window window;

    @Override
    public void init()
    {
       this.getContext().addTransactionListener(this);
       this.window=new Window("DentalCare Hospital");
       this.setMainWindow(window);
       window.setContent(new LoginScreen(this));
    }

    public void logout()
	{
		getMainWindow().getApplication().close();

		Subject currentUser = SecurityUtils.getSubject();

		if (currentUser.isAuthenticated())
		{
			currentUser.logout();
		}
	}
    
    public void transactionStart(Application application, Object transactionData)
	{
		if (application == MyVaadinApplication.this)
		{
			MyVaadinApplication.currentApplication.set(this);
		}
	}


	public void transactionEnd(Application application, Object transactionData)
	{
		if (application == MyVaadinApplication.this)
		{
			MyVaadinApplication.currentApplication.set(null);

			MyVaadinApplication.currentApplication.remove();
		}
	}

    
	public Window getMainWindow(){
		return window;
	}

	public static MyVaadinApplication getInstance() {
		return MyVaadinApplication.currentApplication.get();
	}

	public void login(String username, String password) {
		UsernamePasswordToken token;

		token = new UsernamePasswordToken(username, password);
		// ”Remember Me” built-in, just do this:
		token.setRememberMe(true);

		// With most of Shiro, you'll always want to make sure you're working with the currently executing user,
		// referred to as the subject
		Subject currentUser = SecurityUtils.getSubject();

		// Authenticate
		currentUser.login(token);
		
	}
	public static class LogoutListener implements Button.ClickListener
	{
		private static final long serialVersionUID = 1L;
		private MyVaadinApplication app;


		public LogoutListener(MyVaadinApplication app)
		{
			this.app = app;
		}


		public void buttonClick(ClickEvent event)
		{
			this.app.logout();
		}
	}
}
