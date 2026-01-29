<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %><%@
taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %><%@
taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %><%@
taglib uri="http://liferay.com/tld/frontend" prefix="liferay-frontend" %>

<%@
page import="com.liferay.portal.kernel.language.LanguageUtil" %><%@
page import="com.liferay.petra.string.StringPool" %><%@
page import="com.liferay.portal.kernel.util.ArrayUtil" %><%@
page import="com.liferay.portal.kernel.util.Constants" %><%@
page import="com.liferay.portal.kernel.util.KeyValuePair" %><%@
page import="com.liferay.portal.kernel.util.KeyValuePairComparator" %><%@
page import="com.liferay.portal.kernel.util.ListUtil" %><%@
page import="com.liferay.portal.kernel.util.LocaleUtil" %><%@
page import="com.liferay.portal.kernel.util.PrefsPropsUtil" %><%@
page import="com.liferay.portal.kernel.util.PropsKeys" %><%@
page import="com.liferay.portal.kernel.util.SetUtil" %><%@
page import="com.liferay.portal.kernel.util.StringUtil" %><%@
page import="com.liferay.portal.kernel.exception.LocaleException" %><%@
page import="com.liferay.portal.util.PropsValues" %><%@
page import="br.com.venturecode.stripfriendlyurlaccents.exception.CustomRequiredLocaleException" %><%@ 
page import="com.liferay.portal.configuration.module.configuration.ConfigurationProviderUtil" %><%@
page import="br.com.venturecode.stripfriendlyurlaccents.configuration.StripFriendlyURLAccentsConfiguration" %><%@
page import="com.liferay.portal.settings.configuration.admin.display.PortalSettingsConfigurationScreenContributor" %>

<%@ page import="java.util.ArrayList" %><%@
page import="java.util.List" %><%@
page import="java.util.Locale" %>

<liferay-frontend:defineObjects />

<liferay-theme:defineObjects />

<portlet:defineObjects />

<%
StripFriendlyURLAccentsConfiguration stripFriendlyURLAccentsConfiguration = (StripFriendlyURLAccentsConfiguration)ConfigurationProviderUtil.getSystemConfiguration(StripFriendlyURLAccentsConfiguration.class);
%>

<style>
.fieldset-legend {
	display: none;
}
</style>