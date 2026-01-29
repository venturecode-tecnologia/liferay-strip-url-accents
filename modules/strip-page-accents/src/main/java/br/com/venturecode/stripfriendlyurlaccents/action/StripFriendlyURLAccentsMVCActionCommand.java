package br.com.venturecode.stripfriendlyurlaccents.action;

import com.liferay.configuration.admin.constants.ConfigurationAdminPortletKeys;
import com.liferay.portal.configuration.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.exception.NoSuchListTypeException;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseFormMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PropertiesParamUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.Validator;

import java.util.Dictionary;
import java.util.Hashtable;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import br.com.venturecode.stripfriendlyurlaccents.configuration.StripFriendlyURLAccentsConfiguration;
import br.com.venturecode.stripfriendlyurlaccents.exception.CustomRequiredLocaleException;

/**
 * @author Danilo Buzar
 */
//@formatter:off
@Component(
	property = {
		"javax.portlet.name=" + ConfigurationAdminPortletKeys.INSTANCE_SETTINGS,
		"mvc.command.name=/venturecode/edit_strip_accents"
	},
	service = MVCActionCommand.class
)
//@formatter:on
public class StripFriendlyURLAccentsMVCActionCommand extends BaseFormMVCActionCommand {

	@Reference
	private Language language;

	@Reference
	private ConfigurationProvider configurationProvider;

	@Override
	protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
		final String cmd = ParamUtil.getString(actionRequest, Constants.CMD);

		try {
			if (cmd.equals(Constants.ADD) || cmd.equals(Constants.UPDATE)) {
				final Dictionary<String, Object> allProperties = new Hashtable<>();
				final UnicodeProperties unicodeProperties = PropertiesParamUtil.getProperties(actionRequest,
						"settings--");

				final String newLanguageIds = unicodeProperties.getProperty(PropsKeys.LOCALES);

				allProperties.put("currentLanguageIds", newLanguageIds);
				this.saveConfiguration(StripFriendlyURLAccentsConfiguration.class, allProperties);

				final String redirect = ParamUtil.getString(actionRequest, "redirect");
				this.sendRedirect(actionRequest, actionResponse, redirect);
			}
		} catch (final Exception exception) {
			if (exception instanceof PrincipalException) {
				SessionErrors.add(actionRequest, exception.getClass());

				actionResponse.setRenderParameter("mvcPath", "/error.jsp");

				return;
			}
			if (!(exception instanceof CustomRequiredLocaleException)) {
				throw exception;
			}
			if (exception instanceof final NoSuchListTypeException noSuchListTypeException) {
				final Class<?> clazz = exception.getClass();

				SessionErrors.add(actionRequest, clazz.getName() + noSuchListTypeException.getType());
			} else {
				SessionErrors.add(actionRequest, exception.getClass(), exception);
			}

			SessionErrors.add(actionRequest, exception.getClass(), exception);

			final String redirect = ParamUtil.getString(actionRequest, "redirect");

			if (Validator.isNotNull(redirect)) {
				actionResponse.sendRedirect(redirect);
			}
		}
	}

	private void saveConfiguration(Class<?> clazz, Dictionary<String, Object> properties)
			throws ConfigurationException {
		this.configurationProvider.saveSystemConfiguration(clazz, properties);
	}

	@Override
	protected void doValidateForm(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
	}

}
