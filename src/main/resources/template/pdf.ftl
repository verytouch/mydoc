<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>${name}</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            font-size: 16px;
            color: #333;
        }

        body {
            display: flex;
            font-family: "SimHei", sans-serif;
        }

        #right {
            width: 100%;
            height: 100%;
            overflow-y: auto;
            margin-left: 1em;
        }

        .api-group {
            font-size: larger;
        }

        .api > h2 {
            line-height: 2em;
            width: 80%;
            font-weight: bold;
            margin-top: 1em;
        }

        .api > p {
            margin-top: 1em;
            text-indent: 5px;
        }

        .api > p span {
            width: 4px;
            height: 4px;
            display: inline-block;
            background-color: #333;
            border-radius: 50%;
            margin-right: 5px;
            margin-bottom: 0.2em;
        }

        #right p.sub-table-title {
            border-left: 2px solid #999;
            padding-left: 5px;
            color: #888;
        }

        #right p.sub-table-title > a {
            cursor: pointer;
            color: #668;
            margin-left: 1em;
            font-size: smaller;
        }

        pre {
            background-color: #f9f9f9;
            border: 1px solid #ddd;
            margin-top: 1em;
            width: 80%;
            overflow-x: auto;
            padding: 10px 0;
        }

        .api {
            margin-bottom: 3em;
        }

        tr, .sub-table-title {
            page-break-inside: avoid !important;
        }

        td, th {
            border: 1px solid #aaa;
            text-align: left;
            padding: 7px 0.5em;
            font-weight: normal;
        }

        th {
            background-color: #efefef;
        }

        table {
            width: 80%;
            border-collapse: collapse;
            margin-top: 1em;
        }

        ::-webkit-scrollbar {
            width: 8px;
        }

        ::-webkit-scrollbar-thumb {
            background-color: #aaa;
        }
    </style>
</head>
<body>
<div id="right">
<#macro ftable fields>
    <#if fields?? && (fields ? size > 0)>
        <#nested>
        <table>
            <tr>
                <th>名称</th>
                <th>类型</th>
                <#if config.showRequired>
                    <th>必填</th>
                </#if>
                <th>说明</th>
            </tr>
            <#list fields as f>
                <tr>
                    <td>${f.name}</td>
                    <td>${f.type ? html}</td>
                    <#if config.showRequired>
                        <td>${(f.required ! false) ? string('是', '否')}</td></#if>
                    <td>${f.desc}</td>
                </tr>
            </#list>
        </table>
        <#list fields as f>
            <@ftable fields=f.children>
                <p class="sub-table-title">${f.type ? html}</p>
            </@ftable>
        </#list>
    </#if>
</#macro>
<#list data as group>
    <h1 class="api-group">${group.name}</h1>
    <div class="api">
    <#list group.operationList as api>
        <#if config.showApiDesc><h2>${api.desc ! api.name}</h2></#if>
        <p><span></span>URL: ${group.path}${api.path}</p>
        <p><span></span>Method: ${api.method}</p>
        <p><span></span>ContentType: ${api.contentType}</p>
        <@ftable fields=api.pathVariable>
            <p><span></span>RequestPath</p>
        </@ftable>
        <@ftable fields=api.requestParam>
            <p><span></span>RequestParam</p>
        </@ftable>
        <#if api.requestFile?? && (api.requestFile ? size > 0)>
            <@ftable fields=api.requestFile>
                <p><span></span>RequestBody</p>
            </@ftable>
        <#else>
            <@ftable fields=api.requestBody>
                <p><span></span>RequestBody</p>
            </@ftable>
            <#if api.requestBodyExample>
                <p class="sub-table-title">RequestBody示例</p>
                <pre>${api.requestBodyExample}</pre>
            </#if>
        </#if>
        <@ftable fields=api.responseBody>
            <p><span></span>ResponseBody</p>
        </@ftable>
        <#if api.responseBodyExample>
            <p class="sub-table-title">ResponseBody示例</p>
            <pre>${api.responseBodyExample}</pre>
        </#if>
    </#list>
    </div>
</#list>
</div>
</body>
</html>