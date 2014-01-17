<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="greenPepper"/>
    <title>GreenPepper Dashboard</title>
    <g:javascript>
        function clearResults() {
            if ($('resultSpinner')) {
                Effect.Appear('resultSpinner', {duration:0.0});
            }
            if ($('result')) {
                Effect.Fade('result', {duration:0.0});
            }
        }
        function showResults() {
            if ($('resultSpinner')) {
                Effect.Fade('resultSpinner', {duration:0.0});
            }
            if ($('result')) {
                Effect.Appear('result', {duration:0.0});
            }
        }
    </g:javascript>
</head>
<body>
<div class="body">
    <h1>Runner List</h1>
    <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
    </g:if>
    <div class="list">
        <table>
            <thead>
            <tr>
                <th>Name</th>
                <th>Action</th>
            </tr>
            </thead>
            <tbody>
            <g:each in="${runners}" status="i" var="runner">
                <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                    <td>${runner.toString()}</td>
                    <td><g:remoteLink class="greenpepper-run" controller="greenPepperRunner" action="run" id="${runner.toString()}" onLoading="clearResults()" onComplete="showResults();" update="result">Run</g:remoteLink></td>
                </tr>
            </g:each>
            </tbody>
        </table>
    </div>
    <div class="paginateButtons">
        <g:paginate total="${runners.length}"/>
    </div>
    <div>
        <div id="resultSpinner" style="display:none;">
            <img src="<g:createLinkTo dir="${pluginContextPath}" file="images/greenpepper/ajax-loader.gif"/>" alt="Executing specification(s)..."/>
        </div>
        <div id="result" style="display:none; font-size: larger"></div>
    </div>
</div>
</body>
</html>