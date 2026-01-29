package br.com.venturecode.stripfriendlyurlaccents.configuration;

import com.liferay.configuration.admin.display.ConfigurationScreen;
import com.liferay.configuration.admin.display.ConfigurationScreenWrapper;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.settings.configuration.admin.display.PortalSettingsConfigurationScreenContributor;
import com.liferay.portal.settings.configuration.admin.display.PortalSettingsConfigurationScreenFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Danilo Buzar
 */
@Component(service = ConfigurationScreen.class)
public class StripFriendlyURLAccentsConfigurationScreen extends ConfigurationScreenWrapper {

	@Reference
	private PortalSettingsConfigurationScreenFactory portalSettingsConfigurationScreenFactory;

	@Reference(target = "(osgi.web.symbolicname=br.com.venturecode.stripfriendlyurlaccents)", unbind = "-")
	private ServletContext _servletContext;

	private static final String REQUEST_ATTR = "strip.friendly.url.accents";

	@Override
	protected ConfigurationScreen getConfigurationScreen() {
		return this.portalSettingsConfigurationScreenFactory
				.create(new StripFriendlyURLAccentsConfigurationScreenContributor());
	}

	private class StripFriendlyURLAccentsConfigurationScreenContributor
			implements PortalSettingsConfigurationScreenContributor {

		@Override
		public String getCategoryKey() {
			return "localization";
		}

		@Override
		public String getJspPath() {
			return "/strip_friendlyurl_accents.jsp";
		}

		@Override
		public String getKey() {
			return "strip-friendly-url-accents";
		}

		@Override
		public String getSaveMVCActionCommandName() {
			return "/venturecode/edit_strip_accents";
		}

		@Override
		public ServletContext getServletContext() {
			return StripFriendlyURLAccentsConfigurationScreen.this._servletContext;
		}

		@Override
		public void setAttributes(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

			final ThemeDisplay themeDisplay = (ThemeDisplay) httpServletRequest.getAttribute(WebKeys.THEME_DISPLAY);

			httpServletRequest.setAttribute(REQUEST_ATTR,
					PrefsPropsUtil.getInteger(themeDisplay.getCompanyId(), REQUEST_ATTR));
		}

	}
}
