package br.com.venturecode.stripfriendlyurlaccents.configuration;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

import aQute.bnd.annotation.metatype.Meta;

/**
 * @author Danilo Buzar
 */
//@formatter:off
@ExtendedObjectClassDefinition(
	category = "localization",
	generateUI = false,
	scope = ExtendedObjectClassDefinition.Scope.COMPANY
)
@Meta.OCD(
	id = "br.com.venturecode.stripfriendlyurlaccents.configuration.StripFriendlyURLAccentsConfiguration",
	localization = "content/Language",
	name = "strip-friendly-url-accents-configuration-name"
)
//@formatter:on
public interface StripFriendlyURLAccentsConfiguration {

	@Meta.AD(required = false)
	public String[] currentLanguageIds();
}
