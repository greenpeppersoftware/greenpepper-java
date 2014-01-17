<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
    <head>
        <title><g:layoutTitle default="Grails" /></title>
        <link rel="stylesheet" href="${createLinkTo(dir:'css',file:'main.css')}" type="text/css"/>
        <link rel="shortcut icon" href="${createLinkTo(dir:'images',file:'favicon.ico')}" type="image/x-icon"/>
        <g:layoutHead />
        <g:javascript library="application" />				
    </head>
    <body>
        <div id="spinner" class="spinner" style="display:none;">
            <img src="${createLinkTo(dir:'images',file:'spinner.gif')}" alt="Spinner" />
        </div>	
        <div class="logo"><img src="${createLinkTo(dir:'images',file:'grails_logo.jpg')}" alt="Grails" /></div>	
        
        <div class="content">
            <div class="nav">
                <span class="menuButton">
                    <a class="home" href="${createLinkTo(dir:'')}">Home</a>
                </span>
                <span class="menuButton">
                    <g:link class="list" controller="movie" action="list">Movies</g:link>
                </span>
                <gp:ifTestEnv>
                <span class="menuButton">
                    <gp:link/>
                </span>
                </gp:ifTestEnv>
            </div>
            <g:layoutBody />
        </div>
    </body>
</html>