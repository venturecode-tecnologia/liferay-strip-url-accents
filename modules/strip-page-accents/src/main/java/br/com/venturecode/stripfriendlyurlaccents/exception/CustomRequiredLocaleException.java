package br.com.venturecode.stripfriendlyurlaccents.exception;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;

import java.util.List;

/**
 * @author Danilo Buzar
 */
public class CustomRequiredLocaleException extends PortalException {

	private static final long serialVersionUID = 3319694011046396529L;
	private String[] messageArguments;
	private String messageKey;

	public CustomRequiredLocaleException() {
	}

	public CustomRequiredLocaleException(List<Group> groups) throws PortalException {
		this(CustomRequiredLocaleException.getRequiredLocaleMessageArguments(groups),
				CustomRequiredLocaleException.getRequiredLocaleMessageKey(groups));
	}

	public CustomRequiredLocaleException(String messageKey) {
		this.messageKey = messageKey;
	}

	public CustomRequiredLocaleException(String[] messageArguments, String messageKey) {

		this.messageArguments = messageArguments;
		this.messageKey = messageKey;
	}

	public CustomRequiredLocaleException(Throwable throwable) {
		super(throwable);
	}

	public String[] getMessageArguments() {
		return this.messageArguments;
	}

	public String getMessageKey() {
		return this.messageKey;
	}

	public void setMessageArguments(String[] messageArguments) {
		this.messageArguments = messageArguments;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	private static String[] getRequiredLocaleMessageArguments(List<Group> groups) throws PortalException {

		if (groups.isEmpty()) {
			return new String[0];
		}
		if (groups.size() == 1) {
			final Group group = groups.get(0);

			return new String[] { group.getDescriptiveName() };
		}
		if (groups.size() == 2) {
			final Group group1 = groups.get(0);
			final Group group2 = groups.get(1);

			return new String[] { group1.getDescriptiveName(), group2.getDescriptiveName() };
		}

		final Group group1 = groups.get(0);
		final Group group2 = groups.get(1);

		return new String[] { group1.getDescriptiveName(), group2.getDescriptiveName(),
				String.valueOf(groups.size() - 2) };
	}

	private static String getRequiredLocaleMessageKey(List<Group> groups) {
		if (groups.isEmpty()) {
			return StringPool.BLANK;
		}
		if (groups.size() == 1) {
			return "language-cannot-be-removed-because-it-is-in-use-by-the-" + "following-site-x";
		}
		if (groups.size() == 2) {
			return "one-or-more-languages-cannot-be-removed-because-they-are-"
					+ "in-use-by-the-following-sites-x-and-x";
		}

		return "one-or-more-languages-cannot-be-removed-because-they-are-in-"
				+ "use-by-the-following-sites-x,-x-and-x-more";
	}

}
