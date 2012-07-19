Web Service Facade (wsfacade)

Version ${pom.version}

The service is configured using /META-INF/context.xml.

To point to a particular WMTS Rest Service, change proxies.wmts_soap2rest.server.url, EG:

<Environment name="proxies.wmts_soap2rest.server.url" type="java.lang.String" value="http://localhost/tilecache/wmts.py/" ></Environment>

You can also add your own proxies by adding to the proxies list EG:

	<Environment name="proxies" type="java.lang.String" value="wmts_local,wmts_landgate,newproxy" description="List of proxies." ></Environment>

Then duplicate and rename all the settings for an existing proxy. EG:

	<Environment name="proxies.newproxy.custom_proxy" type="java.lang.String" value="com.lisasoft.wsfacade.proxies.WmtsProxy" ></Environment>
	<Environment name="proxies.newproxy.url" type="java.lang.String" value="/newproxy" description="URL of this proxy" ></Environment>
	<Environment name="proxies.newproxy.proxy_managed" type="java.lang.String" value="?WSDL" description="Comma separated list of URL's managed by the proxy and not passed on to the translated service." ></Environment>

	<Environment name="proxies.newproxy.client.type" type="java.lang.String" value="soap" ></Environment>
	<Environment name="proxies.newproxy.client.custom_mapper" type="java.lang.String" value="com.lisasoft.wsfacade.mappers.wmts.WmtsSoapMapper" ></Environment>

	<Environment name="proxies.newproxy.server.type" type="java.lang.String" value="rest" ></Environment>
	<Environment name="proxies.newproxy.server.url" type="java.lang.String" value="http://www.newproxy.com/tilecache/wmts.py/" ></Environment>
	<Environment name="proxies.newproxy.server.custom_mapper" type="java.lang.String" value="com.lisasoft.wsfacade.mappers.wmts.WmtsRestMapper" ></Environment>

	<Environment name="proxies.newproxy.mappings" type="java.lang.String" value="GetCapabilities,GetTile,GetFeatureInfo" ></Environment>

You will need to change the value of the following settings to something more appropriate: 
- proxies.newproxy.url
- proxies.newproxy.server.url

Once deployed, browse to /wsfacade/index.jsp for the test harness.

You may also want to change the logging configuration by editing the /WEB-INF/classes/log4j.properties file.