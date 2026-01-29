package br.com.venturecode.stripfriendlyurlaccents.wrapper;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.service.LayoutLocalServiceWrapper;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceWrapper;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Validator;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import br.com.venturecode.stripfriendlyurlaccents.configuration.StripFriendlyURLAccentsConfiguration;

/**
 * @author Danilo Buzar
 */
//@formatter:off
@Component(
	immediate = true,
	property = {},
	service = ServiceWrapper.class
)
//@formatter:on
public class CustomLayoutFriendlyURLLocalServiceWrapper extends LayoutLocalServiceWrapper {

	@Reference
	private ConfigurationProvider configurationProvider;

	@Override
	public Layout addLayout(String externalReferenceCode, long userId, long groupId, boolean privateLayout,
			long parentLayoutId, long classNameId, long classPK, Map<Locale, String> nameMap,
			Map<Locale, String> titleMap, Map<Locale, String> descriptionMap, Map<Locale, String> keywordsMap,
			Map<Locale, String> robotsMap, String type, String typeSettings, boolean hidden, boolean system,
			Map<Locale, String> friendlyURLMap, long masterLayoutPlid, ServiceContext serviceContext)
			throws PortalException {

		return super.addLayout(externalReferenceCode, userId, groupId, privateLayout, parentLayoutId, classNameId,
				classPK, nameMap, titleMap, descriptionMap, keywordsMap, robotsMap, type, typeSettings, hidden, system,
				this.getFriendlyNormalizedURL(nameMap), masterLayoutPlid, serviceContext);
	}

	@Override
	public Layout updateLayout(long groupId, boolean privateLayout, long layoutId, long parentLayoutId,
			Map<Locale, String> nameMap, Map<Locale, String> titleMap, Map<Locale, String> descriptionMap,
			Map<Locale, String> keywordsMap, Map<Locale, String> robotsMap, String type, boolean hidden,
			Map<Locale, String> friendlyURLMap, boolean hasIconImage, byte[] iconBytes, long styleBookEntryId,
			long faviconFileEntryId, long masterLayoutPlid, ServiceContext serviceContext) throws PortalException {

		return super.updateLayout(groupId, privateLayout, layoutId, parentLayoutId, nameMap, titleMap, descriptionMap,
				keywordsMap, robotsMap, type, hidden, this.getFriendlyNormalizedURL(friendlyURLMap), hasIconImage,
				iconBytes, styleBookEntryId, faviconFileEntryId, masterLayoutPlid, serviceContext);
	}

	private String stripAccents(String s) {
		s = Normalizer.normalize(s, Normalizer.Form.NFD);
		s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
		return s;
	}

	private Map<Locale, String> getFriendlyNormalizedURL(Map<Locale, String> map) throws ConfigurationException {
		final Map<Locale, String> newFriendlyURLMap = new HashMap<>();
		final StripFriendlyURLAccentsConfiguration config = this.configurationProvider
				.getSystemConfiguration(StripFriendlyURLAccentsConfiguration.class);
		final String[] stripLanguages = config.currentLanguageIds();

		if (Validator.isNull(stripLanguages)) {
			return map;

		}

		map.forEach((k, v) -> {
			for (final Locale locale : LocaleUtil.fromLanguageIds(stripLanguages)) {
				if (k.equals(locale)) {
					final String friendlyURL = this.stripAccents(v);

					newFriendlyURLMap.put(k, StringPool.SLASH + friendlyURL.replace(StringPool.SLASH, ""));

					break;
				}
				newFriendlyURLMap.put(k, v);
			}
		});

		return newFriendlyURLMap;

	}
}
