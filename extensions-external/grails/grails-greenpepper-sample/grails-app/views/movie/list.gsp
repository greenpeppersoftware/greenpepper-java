<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Movie List</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><g:link class="create" action="create">New Movie</g:link></span>
        </div>
        <div class="body">
            <h1>Movie List</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:render template="searchForm"/>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                   	        <g:sortableColumn property="id" title="Id" />
                        
                   	        <g:sortableColumn property="title" title="Title" />
                        
                   	        <g:sortableColumn property="director" title="Director" />
                        
                   	        <g:sortableColumn property="year" title="Year" />
                        
                   	        <g:sortableColumn property="price" title="Price" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${movieInstanceList}" status="i" var="movieInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${movieInstance.id}">${fieldValue(bean:movieInstance, field:'id')}</g:link></td>
                        
                            <td>${fieldValue(bean:movieInstance, field:'title')}</td>
                        
                            <td>${fieldValue(bean:movieInstance, field:'director')}</td>
                        
                            <td>${fieldValue(bean:movieInstance, field:'year')}</td>
                        
                            <td>${fieldValue(bean:movieInstance, field:'price')}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${Movie.count()}" />
            </div>
        </div>
    </body>
</html>
