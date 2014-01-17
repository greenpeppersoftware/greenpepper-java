<g:form id="searchForm" url="[controller:'movie',action:'search']">
    <table style="border: none;">
        <tbody>
            <tr>
                <td>
                    <strong>Title:</strong> <input type="text" name="qTitle" value="${qTitle}"/>
                </td>
                <td>
                    <strong>Director:</strong> <input type="text" name="qDirector" value="${qDirector}"/>
                </td>
                <td>
                    <input type="submit" value="search"/>
                </td>
            </tr>
        </tbody>
    </table>
</g:form>