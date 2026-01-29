<%@ include file="/init.jsp" %>

<%
String[] currentLanguageIds = stripFriendlyURLAccentsConfiguration.currentLanguageIds();
%>

<aui:fieldset>
	<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />

	<liferay-ui:error exception="<%= LocaleException.class %>">

		<%
		LocaleException le = (LocaleException)errorException;
		%>

		<c:if test="<%= le.getType() == LocaleException.TYPE_DISPLAY_SETTINGS %>">
			<liferay-ui:message key="please-enter-a-valid-locale" />
		</c:if>
	</liferay-ui:error>

	<liferay-ui:error exception="<%= CustomRequiredLocaleException.class %>">

		<%
		CustomRequiredLocaleException rle = (CustomRequiredLocaleException)errorException;
		%>

		<liferay-ui:message arguments="<%= rle.getMessageArguments() %>" key="<%= rle.getMessageKey() %>" translateArguments="<%= false %>" />
	</liferay-ui:error>

	<div id="<portlet:namespace />languageWarning"></div>

	<div class="alert alert-info" role="alert">
	   <div class="alert-autofit-row autofit-row">
	      <div class="autofit-col">
	         <div class="autofit-section">
	            <span class="alert-indicator">
	               <svg class="lexicon-icon lexicon-icon-info-circle" role="presentation">
	                  <use xlink:href="http://localhost:8080/o/admin-theme/images/clay/icons.svg#info-circle"></use>
	               </svg>
	            </span>
	         </div>
	      </div>
	      <div class="autofit-col autofit-col-expand">
	         <div class="autofit-section"><liferay-ui:message key="strip-friendly-url-accents-info" /></div>
	      </div>
	   </div>
	</div>

	<aui:fieldset cssClass="available-languages" label="available-languages">

		<%
		String[] availableLanguageIds = PrefsPropsUtil.getStringArray(company.getCompanyId(), PropsKeys.LOCALES, StringPool.COMMA, PropsValues.LOCALES_ENABLED);
		%>

		<aui:input name='<%= "settings--" + PropsKeys.LOCALES + "--" %>' type="hidden" value="<%= StringUtil.merge(availableLanguageIds) %>" />

		<%
		List<KeyValuePair> leftList = new ArrayList<>();

		for (Locale currentLocale : LocaleUtil.fromLanguageIds(currentLanguageIds)) {
			leftList.add(new KeyValuePair(LanguageUtil.getLanguageId(currentLocale), currentLocale.getDisplayName(locale)));
		}

		String[] availableSystemLanguagesIds = PrefsPropsUtil.getStringArray(company.getCompanyId(), PropsKeys.LOCALES, StringPool.COMMA, PropsValues.LOCALES_ENABLED);
		List<KeyValuePair> rightList = new ArrayList<>();

		for (String propsValuesLanguageId : SetUtil.fromArray(availableSystemLanguagesIds)) {
			if (!ArrayUtil.contains(currentLanguageIds, propsValuesLanguageId)) {
				Locale propsValuesLocale = LocaleUtil.fromLanguageId(propsValuesLanguageId, false);

				if (propsValuesLocale != null) {
					rightList.add(new KeyValuePair(propsValuesLanguageId, propsValuesLocale.getDisplayName(locale)));
				}
			}
		}

		rightList = ListUtil.sort(rightList, new KeyValuePairComparator(false, true));
		%>

		<liferay-ui:input-move-boxes
			leftBoxName="currentLanguageIds"
			leftList="<%= leftList %>"
			leftReorder="<%= Boolean.TRUE.toString() %>"
			leftTitle="current"
			rightBoxName="availableLanguageIds"
			rightList="<%= rightList %>"
			rightTitle="available"
		/>
	</aui:fieldset>
</aui:fieldset>

<aui:script use="aui-alert,aui-base">
	const languageSelectInput = document.getElementById(
		'<portlet:namespace />languageId'
	);

	if (languageSelectInput) {
		languageSelectInput.addEventListener('change', () => {
			new A.Alert({
				bodyContent:
					'<liferay-ui:message key="this-change-will-only-affect-the-newly-created-localized-content" />',
				boundingBox: '#<portlet:namespace />languageWarning',
				closeable: true,
				cssClass: 'alert-warning',
				destroyOnHide: false,
				render: true,
			});
		});
	}

	function <portlet:namespace />saveLocales() {
		var form = document.<portlet:namespace />fm;

		var currentLanguageIdsElement = Liferay.Util.getFormElement(
			form,
			'currentLanguageIds'
		);

		if (currentLanguageIdsElement) {
			Liferay.Util.setFormValues(form, {
				<%= PropsKeys.LOCALES %>: Liferay.Util.getSelectedOptionValues(
					currentLanguageIdsElement
				),
			});
		}
	}

	Liferay.after(
		['form:registered', 'inputmoveboxes:moveItem', 'inputmoveboxes:orderItem'],
		<portlet:namespace />saveLocales
	);
</aui:script>