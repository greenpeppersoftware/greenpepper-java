<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
    <title><g:layoutTitle default="GreenPepper"/></title>
    <link rel="stylesheet" href="${createLinkTo(dir: 'css', file: 'main.css')}" type="text/css"/>
    <link rel="stylesheet" href="<g:createLinkTo dir="${pluginContextPath}" file="css/greenpepper.css"/>" type="text/css"/>
    <link rel="shortcut icon" href="<g:createLinkTo dir="${pluginContextPath}" file="images/greenpepper/greenpepper.ico"/>" type="image/x-icon"/>
    <g:layoutHead/>
    <g:javascript library="prototype"/>
    <g:javascript library="scriptaculous"/>
    <g:javascript library="application"/>
</head>
<body>
<div id="body">
    <div id="spinner" class="spinner" style="display:none;">
        <img src="${createLinkTo(dir: 'images', file: 'spinner.gif')}" alt="Spinner"/>
    </div>

    <div class="logo"><img src="<g:createLinkTo dir="${pluginContextPath}" file="images/greenpepper/greenpepper-title.png"/>" alt="GreenPepper"/></div>

    <div class="nav">
        <span class="menuButton"><a class="home" href="${createLinkTo(dir: '')}">Home</a></span>
        <span class="menuButton"><g:link class="list" action="index">Runner List</g:link></span>
    </div>

    <g:layoutBody/>
</div>
</body>
</html>