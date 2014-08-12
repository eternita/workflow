<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<!DOCTYPE html>
<fmt:setBundle basename="languages/messages" />
<html lang="en_US">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="ROBOTS" content="noindex, nofollow" />

<title><fmt:message key="message.meta.title" /></title>
<link rel="StyleSheet" href="../resources/css/960.css" />
<link rel="stylesheet" href="../resources/css/style.css" />
	<meta name="_csrf" content="${_csrf.token}"/>
	<meta name="_csrf_header" content="${_csrf.headerName}"/>
</head>
<body>

	<div id="xHeader">
		<div>
			<a id="uLogo" href="http://neuro4j.org/projects/java-workflow-engine">Neuro4j
				Web Demo</a>
		</div>
	</div>


	<div id="xContentHeaderContainer" style="height: 112px;">

		<div class="container_12">
			<div class="grid_10">
				<div class="xContentHeader">
					<a style="text-decoration: none;"
						href="http://neuro4j.org/projects/java-workflow-engine">
						<h3>
							<fmt:message key="message.application.title" />
						</h3>
					</a>
					<ul>
						<%@ include file="/WEB-INF/jsp/inc/main_menu.jsp"%>
					</ul>
				</div>
			</div>
			<div class="grid_5">
				<br />

			</div>
		</div>
	</div>
	<div class="container_12">
		<div align="center"><jsp:include
				page="/WEB-INF/jsp/inc/message.jsp"></jsp:include></div>
		<br />

		<jsp:include page="/WEB-INF/${page}"></jsp:include>
	</div>


	<div class="container_12">
		<div class="grid_12"></div>
	</div>

</body>
</html>
