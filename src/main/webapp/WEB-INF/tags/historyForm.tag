<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@attribute name="information" type="br.com.caelum.brutal.model.Information" required="true" %>
<%@attribute name="index" type="java.lang.Integer" required="true" %>

<form method="post" class="history-form moderate-form ${index != 0 ? 'hidden' : ''}" action="${linkTo[HistoryController].publish}${information.moderatable.typeName}">
	<a href="#" class="toggle-version"><fmt:message key="moderation.formatted"/></a>
	<a href="#" class="toggle-version hidden"><fmt:message key="moderation.diff"/></a>
	<div class="history-version hidden">
		<jsp:doBody/>	
	</div>
	<div class="history-diff post-text"></div>

	<ul class="post-touchs clear">
		<li class="touch author-touch">
			<tags:completeUser touchText="touch.edited" user="${information.author}" date="${information.createdAt}"/>
		</li>
	</ul>

	<h2 class="history-title page-title"><fmt:message key="moderation.comment"/></h2>
	<p class="post-text">
		${information.comment}
	</p>
	

	<input type="hidden" name="aprovedInformationType" value="${information.typeName}"/>
	<input type="hidden" name="moderatableId" value="${information.moderatable.id}"/>
	<input type="hidden" name="aprovedInformationId" value="${information.id}"/>
	<input type="submit" class="post-submit big-submit" value='<fmt:message key="moderation.accept" />' />
</form>