package controllers;

import models.User;

import org.apache.commons.lang3.StringUtils;

import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Http.Context;
import play.mvc.SimpleResult;

public class SideMenu extends Action.Simple {

	public F.Promise<SimpleResult> call(Http.Context ctx) throws Throwable {
		// FIXME unauthorized
		User user = getUser(ctx);
		if (user != null) {
			ctx.args.put("sideMenu", new SideMenuItem(user));
		}
		return delegate.call(ctx);
	}

	public static SideMenuItem current() {
		return (SideMenuItem) Http.Context.current().args.get("sideMenu");
	}

	// TODO refactor
	private static User getUser(Context ctx) {
		String username = ctx.session().get("username");
		if (StringUtils.isNotBlank(username)) {
			return getRepository().findByUsername(username);
		}
		return null;
	}

	// FIXME this is crap
	private static UserRepository getRepository() {
		return new UserRepository(EktorpDb.getDb());
	}
}